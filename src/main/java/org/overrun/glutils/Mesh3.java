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

import static org.lwjgl.opengl.GL30.*;

/**
 * {@link Mesh} for OpenGL 3<br>
 * Used VAO
 *
 * @author squid233
 * @since 0.2.0
 */
public class Mesh3 implements IMesh {
    private final int vao;
    private final int vertVbo;
    private final int colorVbo;
    private int texVbo;
    private final int idxVbo;
    private final int vertexCount;
    private final int vertIdx;
    private final int colorIdx;
    private final int texIdx;
    private final int texture;

    private Mesh3(float[] vertices,
                  int vertUsage,
                  int vertIdx,
                  int vertDim,
                  boolean vertNormalized,
                  int vertStride,
                  float[] colors,
                  int colorUsage,
                  int colorIdx,
                  int colorDim,
                  boolean colorNormalized,
                  int colorStride,
                  float[] texCoords,
                  int texture,
                  int texUsage,
                  int texIdx,
                  int texDim,
                  boolean texNormalized,
                  int texStride,
                  int[] indices,
                  int indexUsage,
                  int vertexCount) {
        this.vertIdx = vertIdx;
        this.colorIdx = colorIdx;
        this.texIdx = texIdx;
        this.vertexCount = vertexCount;
        this.texture = texture;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vertVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, vertUsage);
        glEnableVertexAttribArray(vertIdx);
        glVertexAttribPointer(vertIdx,
                vertDim,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
        colorVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, colors, colorUsage);
        glEnableVertexAttribArray(colorIdx);
        glVertexAttribPointer(colorIdx,
                colorDim,
                GL_FLOAT,
                colorNormalized,
                colorStride,
                0);
        if (texture != 0) {
            texVbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texVbo);
            glBufferData(GL_ARRAY_BUFFER, texCoords, texUsage);
            glEnableVertexAttribArray(texIdx);
            glVertexAttribPointer(texIdx,
                    texDim,
                    GL_FLOAT,
                    texNormalized,
                    texStride,
                    0);
        }
        idxVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, indexUsage);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void render(int mode) {
        if (texture != 0) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
        }
        glBindVertexArray(getVao());
        glDrawElements(mode, getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getVao() {
        return vao;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    public static Mesh3 of(float[] vertices,
                           int vertIdx,
                           float[] colors,
                           int colorIdx,
                           float[] texCoords,
                           int texture,
                           int texIdx,
                           int[] indices) {
        return builder()
                .vertices(vertices)
                .vertIdx(vertIdx)
                .colors(colors)
                .colorIdx(colorIdx)
                .texCoords(texCoords)
                .texture(texture)
                .texIdx(texIdx)
                .indices(indices)
                .build();
    }

    @Deprecated
    public static class Builder extends IMesh.Builder<Builder> {
        private int vertIdx;
        private int colorIdx;
        private int texIdx = -1;

        public Builder vertIdx(int vertIdx) {
            this.vertIdx = vertIdx;
            return this;
        }

        public Builder colorIdx(int colorIdx) {
            this.colorIdx = colorIdx;
            return this;
        }

        public Builder texIdx(int texIdx) {
            this.texIdx = texIdx;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public Mesh3 build() {
            return new Mesh3(
                    vertices,
                    vertUsage,
                    vertIdx,
                    vertDim,
                    vertNormalized,
                    vertStride,
                    colors,
                    colorUsage,
                    colorIdx,
                    colorDim,
                    colorNormalized,
                    colorStride,
                    texCoords,
                    texture,
                    texUsage,
                    texIdx,
                    texDim,
                    texNormalized,
                    texStride,
                    indices,
                    indexUsage,
                    vertexCount
            );
        }
    }

    @Override
    public void close() {
        glDisableVertexAttribArray(vertIdx);
        glDisableVertexAttribArray(colorIdx);
        if (texIdx != -1) {
            glDisableVertexAttribArray(texIdx);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertVbo);
        glDeleteBuffers(colorVbo);
        if (texVbo != 0) {
            glDeleteBuffers(texVbo);
        }
        glDeleteBuffers(idxVbo);
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}
