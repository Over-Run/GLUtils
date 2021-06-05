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

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class GlProgram implements AutoCloseable {
    private final int id;
    private int vshId, fshId, gshId;
    private final Map<String, Integer> uniforms = new HashMap<>();
    private final Map<String, Integer> attributes = new HashMap<>();

    public GlProgram()
            throws RuntimeException {
        id = glCreateProgram();
        if (id == 0) {
            throw new RuntimeException("An error occurred creating the program object.");
        }
    }

    public void createVsh(String src) {
        vshId = createShader(src, ShaderType.VERTEX_SHADER);
    }

    public void createFsh(String src) {
        fshId = createShader(src, ShaderType.FRAGMENT_SHADER);
    }

    public void createGsh(String src) {
        gshId = createShader(src, ShaderType.GEOMETRY_SHADER);
    }

    /**
     * Create a shader.
     *
     * @param src The shader source code. May get from
     * {@link ShaderReader#lines(ClassLoader, String) lines}.
     * @param type The shader type.
     */
    private int createShader(String src, ShaderType type)
            throws RuntimeException {
        int shader = glCreateShader(type.getType());
        if (shader == 0) {
            throw new RuntimeException(
                    "An error occurred creating the " +
                            type.getName() +
                            " object."
            );
        }
        glShaderSource(shader, src);
        glCompileShader(shader);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling shader src: " +
                    glGetShaderInfoLog(shader));
        }
        glAttachShader(id, shader);
        return shader;
    }

    public void link()
            throws RuntimeException {
        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking GL program: " +
                    glGetProgramInfoLog(id));
        }
        glDetachShader(id, vshId);
        glDetachShader(id, fshId);
        glDetachShader(id, gshId);
        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
            GLUtils.getWarningCb().warn(glGetProgramInfoLog(id));
        }
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public boolean hasUniform(String name) {
        return glGetUniformLocation(id, name) >= 0;
    }

    public int getUniform(String name)
            throws RuntimeException {
        if (uniforms.containsKey(name)) {
            return uniforms.get(name);
        }
        int loc = glGetUniformLocation(id, name);
        if (loc < 0) {
            throw new RuntimeException("Couldn't find uniform: \"" +
                    name +
                    "\"");
        }
        uniforms.put(name, loc);
        return loc;
    }

    public int getAttrib(String name)
            throws RuntimeException {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }
        int loc = glGetAttribLocation(id, name);
        if (loc < 0) {
            GLUtils.getErrorCb().error("Couldn't find attribute: \"" +
                    name +
                    "\"");
            return -1;
        }
        attributes.put(name, loc);
        return loc;
    }

    public void enableVertexAttribArrays(String... names) {
        for (String name : names) {
            glEnableVertexAttribArray(getAttrib(name));
        }
    }

    public void disableVertexAttribArrays(String... names) {
        for (String name : names) {
            glDisableVertexAttribArray(getAttrib(name));
        }
    }

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
