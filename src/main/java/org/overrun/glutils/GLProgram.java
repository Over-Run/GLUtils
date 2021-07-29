/*
 * MIT License
 *
 * Copyright (c) 2021 OverRun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.overrun.glutils;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.lwjgl.opengl.GL41.*;
import static org.overrun.glutils.GLString.toJava;
import static org.overrun.glutils.GLUtils.getErrorCb;
import static org.overrun.glutils.GLUtils.getWarningCb;

/**
 * @author squid233
 * @since 0.1.0
 */
public class GLProgram implements AutoCloseable {
    /**
     * program id
     */
    private final int id;
    /**
     * shader id
     */
    private int vshId, fshId, gshId;
    /**
     * uniform locations
     */
    private final Map<String, Integer> uniforms = new HashMap<>();
    /**
     * attribute locations
     */
    private final Map<String, Integer> attributes = new HashMap<>();

    /**
     * Construct and create program
     *
     * @throws RuntimeException An error occurred creating the program object
     */
    public GLProgram()
            throws RuntimeException {
        id = glCreateProgram();
        if (id == 0) {
            throw new RuntimeException("An error occurred creating the program object.");
        }
    }

    /**
     * create vertex shader
     *
     * @param src source code
     */
    public void createVsh(String src) {
        vshId = createShader(src, ShaderType.VERTEX_SHADER);
    }

    /**
     * create fragment shader
     *
     * @param src source code
     */
    public void createFsh(String src) {
        fshId = createShader(src, ShaderType.FRAGMENT_SHADER);
    }

    /**
     * create geometry shader
     *
     * @param src source code
     */
    public void createGsh(String src) {
        gshId = createShader(src, ShaderType.GEOMETRY_SHADER);
    }

    /**
     * Create a shader.
     *
     * @param src  The shader source code. May get from
     *             {@link ShaderReader#lines(ClassLoader, String) lines}.
     * @param type The shader type.
     */
    private int createShader(String src, ShaderType type)
            throws RuntimeException {
        int shader = glCreateShader(type.getType());
        if (shader == 0) {
            throw new RuntimeException(
                    "An error occurred creating the " +
                            type +
                            " object."
            );
        }
        glShaderSource(shader, src);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new CompileException("Error compiling shader src: " +
                    toJava(glGetShaderInfoLog(shader)));
        }
        glAttachShader(id, shader);
        return shader;
    }

    /**
     * link and validate program
     *
     * @throws RuntimeException error linking program
     */
    public void link()
            throws RuntimeException {
        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking GL program: " +
                    toJava(glGetProgramInfoLog(id)));
        }
        if (vshId != 0) {
            glDetachShader(id, vshId);
        }
        if (fshId != 0) {
            glDetachShader(id, fshId);
        }
        if (gshId != 0) {
            glDetachShader(id, gshId);
        }
        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
            getWarningCb().warn(toJava(glGetProgramInfoLog(id)));
        }
    }

    /**
     * use program
     */
    public void bind() {
        glUseProgram(id);
    }

    /**
     * don't use program
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * check has uniform
     *
     * @param name uniform name
     * @return has uniform
     */
    public boolean hasUniform(String name) {
        return glGetUniformLocation(id, name) >= 0;
    }

    /**
     * get uniform
     *
     * @param name uniform name
     * @return uniform location
     * @throws RuntimeException uniform not found
     */
    public int getUniform(String name)
            throws RuntimeException {
        if (uniforms.containsKey(name)) {
            return uniforms.get(name);
        }
        int loc = glGetUniformLocation(id, name);
        if (!hasUniform(name)) {
            throw new RuntimeException("Couldn't find uniform: \"" +
                    name +
                    "\"");
        }
        uniforms.put(name, loc);
        return loc;
    }

    /**
     * setUniform
     *
     * @param name  uniform name
     * @param value value
     * @since 0.4.0
     */
    public void setUniform(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    /**
     * setUniform
     *
     * @param name     uniform name
     * @param matrix4f matrix as FloatBuffer
     * @since 0.4.0
     */
    public void setUniformMat4(String name, FloatBuffer matrix4f) {
        glUniformMatrix4fv(getUniform(name), false, matrix4f);
    }

    /**
     * setUniform
     *
     * @param name     uniform name
     * @param matrix4f matrix function (example: <code>matrix4f::get</code>)
     * @since 0.4.0
     */
    public void setUniformMat4(String name,
                               Function<FloatBuffer, FloatBuffer> matrix4f) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            setUniformMat4(name, matrix4f.apply(stack.mallocFloat(16)));
        }
    }

    /**
     * setUniform
     *
     * @param name     uniform name
     * @param matrix4f matrix
     * @since 0.5.0
     */
    public void setUniformMat4(String name,
                               Matrix4f matrix4f) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            setUniformMat4(name, matrix4f.get(stack.mallocFloat(16)));
        }
    }

    /**
     * get attribute
     *
     * @param name attribute name
     * @return attribute location
     */
    public int getAttrib(String name) {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }
        int loc = glGetAttribLocation(id, name);
        if (loc < 0) {
            getErrorCb().error("Couldn't find attribute: \"" +
                    name +
                    "\"");
            return -1;
        }
        attributes.put(name, loc);
        return loc;
    }

    /**
     * glEnableVertexAttribArray
     *
     * @param name attrib name
     * @since 0.3.0
     */
    public void enableVertexAttribArray(String name) {
        glEnableVertexAttribArray(getAttrib(name));
    }

    /**
     * glEnableVertexAttribArray
     *
     * @param names attrib names
     */
    public void enableVertexAttribArrays(String... names) {
        for (String name : names) {
            enableVertexAttribArray(name);
        }
    }

    /**
     * glDisableVertexAttribArray
     *
     * @param names attrib names
     */
    public void disableVertexAttribArrays(String... names) {
        for (String name : names) {
            glDisableVertexAttribArray(getAttrib(name));
        }
    }

    /**
     * glVertexAttribPointer
     *
     * @param name       Attrib name
     * @param size       the number of values per vertex that are stored in the array.
     * @param type       data type
     * @param normalized normalized
     * @param stride     stride
     * @param pointer    pointer
     * @since 0.3.0
     */
    public void vertexAttribPointer(String name,
                                    int size,
                                    int type,
                                    boolean normalized,
                                    int stride,
                                    long pointer) {
        glVertexAttribPointer(getAttrib(name),
                size,
                type,
                normalized,
                stride,
                pointer);
    }

    /**
     * get program id
     *
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * public: ~GlProgram();
     */
    @Override
    public void close() {
        if (id != 0) {
            glDeleteProgram(id);
        }
        if (vshId != 0) {
            glDeleteShader(vshId);
        }
        if (fshId != 0) {
            glDeleteShader(fshId);
        }
        if (gshId != 0) {
            glDeleteShader(gshId);
        }
    }
}
