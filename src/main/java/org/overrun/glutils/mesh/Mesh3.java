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

package org.overrun.glutils.mesh;

import static org.lwjgl.opengl.GL30.*;

/**
 * {@link Mesh} for OpenGL 3<br>
 * Used VAO
 *
 * @author squid233
 * @since 0.2.0
 */
public class Mesh3 extends BaseMesh<Mesh3> {
    private final int vao;
    private int vertIdx;
    private int colorIdx = -1;
    private int texIdx = -1;
    private int normalIdx = -1;

    /**
     * construct
     */
    public Mesh3() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }

    /**
     * @param vertIdx vertices index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 vertIdx(int vertIdx) {
        this.vertIdx = vertIdx;
        return this;
    }

    /**
     * @param colorIdx colors index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 colorIdx(int colorIdx) {
        this.colorIdx = colorIdx;
        return this;
    }

    /**
     * @param texIdx texture coordinates index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 texIdx(int texIdx) {
        this.texIdx = texIdx;
        return this;
    }

    /**
     * @param normalIdx normal vertices index
     * @return this
     * @since 1.1.0
     */
    public Mesh3 normalIdx(int normalIdx) {
        this.normalIdx = normalIdx;
        return this;
    }

    /**
     * bind vao
     *
     * @return this
     * @since 1.1.0
     */
    public Mesh3 bindVao() {
        glBindVertexArray(vao);
        return this;
    }

    /**
     * unbind vao
     *
     * @return this
     * @since 1.1.0
     */
    public Mesh3 unbindVao() {
        glBindVertexArray(0);
        return this;
    }

    @Override
    public Mesh3 vertices(float[] vertices) {
        if (vertIdx > -1) {
            glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, vertUsage);
            glEnableVertexAttribArray(vertIdx);
            glVertexAttribPointer(vertIdx,
                    vertDim,
                    GL_FLOAT,
                    vertNormalized,
                    vertStride,
                    0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        return super.vertices(vertices);
    }

    @Override
    public Mesh3 colors(float[] colors) {
        super.colors(colors);
        if (colorIdx > -1) {
            glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
            glBufferData(GL_ARRAY_BUFFER, colors, colorUsage);
            glEnableVertexAttribArray(colorIdx);
            glVertexAttribPointer(colorIdx,
                    colorDim,
                    GL_FLOAT,
                    colorNormalized,
                    colorStride,
                    0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        return this;
    }

    @Override
    public Mesh3 texCoords(float[] texCoords) {
        super.texCoords(texCoords);
        if (texIdx > -1) {
            glBindBuffer(GL_ARRAY_BUFFER, texVbo);
            glBufferData(GL_ARRAY_BUFFER, texCoords, texUsage);
            glEnableVertexAttribArray(texIdx);
            glVertexAttribPointer(texIdx,
                    texDim,
                    GL_FLOAT,
                    texNormalized,
                    texStride,
                    0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        return this;
    }

    @Override
    public Mesh3 normalVert(float[] normalVert) {
        super.normalVert(normalVert);
        if (normalIdx > -1) {
            glBindBuffer(GL_ARRAY_BUFFER, normalVbo);
            glBufferData(GL_ARRAY_BUFFER, normalVert, normalUsage);
            glEnableVertexAttribArray(normalIdx);
            glVertexAttribPointer(normalIdx,
                    normalDim,
                    GL_FLOAT,
                    normalNormalized,
                    normalStride,
                    0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        return this;
    }

    @Override
    public Mesh3 indices(int[] indices) {
        super.indices(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, indexUsage);
        return this;
    }

    @Override
    public void render(int mode) {
        if (material != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, getTexture());
        }
        glBindVertexArray(vao);
        if (ibo == 0) {
            glDrawArrays(mode, 0, getVertexCount());
        } else {
            glDrawElements(mode, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * get vao
     *
     * @return {@link #vao}
     */
    public int getVao() {
        return vao;
    }

    /**
     * construct
     *
     * @param vertices  vertices
     * @param vertIdx   vertIdx
     * @param colors    colors
     * @param colorIdx  colorIdx
     * @param texCoords texCoords
     * @param texture   texture
     * @param texIdx    texIdx
     * @param indices   indices
     * @return mesh
     */
    public static Mesh3 of(float[] vertices,
                           int vertIdx,
                           float[] colors,
                           int colorIdx,
                           float[] texCoords,
                           int texture,
                           int texIdx,
                           int[] indices) {
        return new Mesh3()
                .vertIdx(vertIdx)
                .vertices(vertices)
                .colorIdx(colorIdx)
                .colors(colors)
                .texture(texture)
                .texIdx(texIdx)
                .texCoords(texCoords)
                .indices(indices)
                .unbindVao();
    }

    /**
     * construct without texture
     *
     * @param vertices vertices
     * @param vertIdx  vertices index
     * @param colors   colors
     * @param colorIdx colors index
     * @param indices  indices
     * @return this
     * @since 0.6.0
     */
    public static Mesh3 of(float[] vertices,
                           int vertIdx,
                           float[] colors,
                           int colorIdx,
                           int[] indices) {
        return new Mesh3()
                .vertIdx(vertIdx)
                .vertices(vertices)
                .colorIdx(colorIdx)
                .colors(colors)
                .indices(indices)
                .unbindVao();
    }

    @Override
    public void close() {
        glDisableVertexAttribArray(vertIdx);
        if (colorIdx != -1) {
            glDisableVertexAttribArray(colorIdx);
        }
        if (texIdx != -1) {
            glDisableVertexAttribArray(texIdx);
        }
        if (normalIdx != -1) {
            glDisableVertexAttribArray(normalIdx);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (vertVbo != 0) {
            glDeleteBuffers(vertVbo);
        }
        if (colorVbo != 0) {
            glDeleteBuffers(colorVbo);
        }
        if (texVbo != 0) {
            glDeleteBuffers(texVbo);
        }
        if (normalVbo != 0) {
            glDeleteBuffers(normalVbo);
        }
        if (ibo != 0) {
            glDeleteBuffers(ibo);
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    @Override
    public Mesh3 getThis() {
        return this;
    }
}
