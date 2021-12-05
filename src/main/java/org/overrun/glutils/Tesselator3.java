/*
 * MIT License
 *
 * Copyright (c) 2021 Overrun Organization
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

import org.joml.Matrix4fc;

import static java.util.Arrays.fill;
import static org.lwjgl.opengl.GL15.*;

/**
 * Tesselator for OpenGL 3.3
 *
 * @author squid233
 * @since 1.5.0
 */
public class Tesselator3 implements ITesselator {
    public static final int VERTEX_COUNT = 60000;
    public static final int MEMORY_USE = (3 + 4 + 2) * VERTEX_COUNT;
    private final GLProgram program = new GLProgram();
    private final Vao vao = new Vao();
    private final Vbo vbo = new Vbo(GL_ARRAY_BUFFER);
    private final float[] array = new float[MEMORY_USE];
    private final VertexAttrib vertex = new VertexAttrib(0);
    private final VertexAttrib color = new VertexAttrib(1);
    private final VertexAttrib texCoord = new VertexAttrib(2);
    protected final boolean fixed;
    protected boolean rendered;
    private float r, g, b, a, u, v;
    protected int vertices;
    protected int pos;
    private boolean hasColor;
    private boolean hasTexture;
    private Matrix4fc mvp;

    public Tesselator3(boolean fixed) {
        this.fixed = fixed;
        program.createVsh("#version 330\n" +
            "layout(location = 0) in vec3 vertex;\n" +
            "layout(location = 1) in vec4 color;\n" +
            "layout(location = 2) in vec2 texCoord;\n" +
            "out vec4 fragColor;\n" +
            "out vec2 fragTexCoord;\n" +
            "uniform mat4 mvp;\n" +
            "void main() {\n" +
            "    gl_Position = mvp * vec4(vertex, 1.0);\n" +
            "    fragColor = color;\n" +
            "    fragTexCoord = texCoord;\n" +
            "}");
        program.createFsh("#version 330\n" +
            "in vec4 fragColor;\n" +
            "in vec2 fragTexCoord;\n" +
            "out vec4 FragColor;\n" +
            "uniform bool hasColor, hasTexture;\n" +
            "uniform sampler2D sampler;\n" +
            "void main() {\n" +
            "    FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
            "    if (hasColor) {\n" +
            "        FragColor *= fragColor;\n" +
            "    }\n" +
            "    if (hasTexture) {\n" +
            "        FragColor *= texture(sampler, fragTexCoord);\n" +
            "    }\n" +
            "}");
        program.link();
    }

    protected void clear() {
        fill(array, 0);
        vertices = 0;
        pos = 0;
    }

    @Override
    public Tesselator3 init() {
        clear();
        hasColor = false;
        hasTexture = false;
        return this;
    }

    @Override
    public Tesselator3 color(final float r,
                             final float g,
                             final float b,
                             final float a) {
        hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    @Override
    public Tesselator3 color(final float r,
                             final float g,
                             final float b) {
        ITesselator.super.color(r, g, b);
        return this;
    }

    @Override
    public Tesselator3 tex(final float u,
                           final float v) {
        hasTexture = true;
        this.u = u;
        this.v = v;
        return this;
    }

    @Override
    public Tesselator3 vertexUV(final float x,
                                final float y,
                                final float z,
                                final float u,
                                final float v) {
        return tex(u, v).vertex(x, y, z);
    }

    @Override
    public Tesselator3 vertex(final float x,
                              final float y,
                              final float z) {
        array[pos++] = x;
        array[pos++] = y;
        array[pos++] = z;
        if (hasColor) {
            array[pos++] = r;
            array[pos++] = g;
            array[pos++] = b;
            array[pos++] = a;
        } else {
            pos += 4;
        }
        if (hasTexture) {
            array[pos++] = u;
            array[pos++] = v;
        } else {
            pos += 2;
        }
        ++vertices;
        return this;
    }

    protected void setupVbo() {
        final int stride = 9 * Float.BYTES;
        vbo.bind();
        vbo.data(array, GL_STREAM_DRAW);
        vertex.pointer(3,
            GL_FLOAT,
            false,
            stride,
            0);
        vertex.enable();
        if (hasColor) {
            color.pointer(4,
                GL_FLOAT,
                false,
                stride,
                3 * Float.BYTES);
            color.enable();
        }
        if (hasTexture) {
            texCoord.pointer(2,
                GL_FLOAT,
                false,
                stride,
                7 * Float.BYTES);
            texCoord.enable();
        }
        vbo.unbind();
    }

    protected void render(int primitive) {
        glDrawArrays(primitive, 0, vertices);
    }

    public void setMatrix(final Matrix4fc mvp) {
        this.mvp = mvp;
    }

    @Override
    public Tesselator3 draw() {
        return draw(GL_TRIANGLES);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.6.0
     */
    @Override
    public Tesselator3 draw(int primitive) {
        if (!fixed || !rendered) {
            vao.bind();
            setupVbo();
            vao.unbind();
            rendered = true;
        }
        program.bind();
        program.setUniformMat4("mvp", mvp);
        program.setUniform("hasColor", hasColor);
        program.setUniform("hasTexture", hasTexture);
        program.setUniform("sampler", 0);
        if (hasTexture) {
            glActiveTexture(GL_TEXTURE0);
        }
        vao.bind();
        render(primitive);
        vao.unbind();
        program.unbind();
        if (!fixed) {
            clear();
        }
        return this;
    }

    @Override
    public void free() {
        program.close();
        vbo.free();
        vao.free();
    }
}
