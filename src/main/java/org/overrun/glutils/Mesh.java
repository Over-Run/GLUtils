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

import static org.lwjgl.opengl.GL20.*;

/**
 * @author squid233
 * @since 0.2.0
 */
public class Mesh implements IMesh {
    private final int vertVbo;
    private final int colorVbo;
    private int texVbo = 0;
    private final int idxVbo;
    private final int vertexCount;
    private final int vertIdx;
    private final int colorIdx;
    private int texIdx = -1;
    private final float[] vertices;
    private final int vertUsage;
    private final int vertSize;
    private final boolean vertNormalized;
    private final int vertStride;
    private final float[] colors;
    private final int colorUsage;
    private final int colorSize;
    private final boolean colorNormalized;
    private final int colorStride;
    private float[] texCoords;
    private final int texture;
    private int texUsage;
    private int texSize;
    private boolean texNormalized;
    private int texStride;
    private final int[] indices;
    private final int indexUsage;

    private Mesh(GlProgram program,
                 float[] vertices,
                 int vertUsage,
                 String vertIdx,
                 int vertSize,
                 boolean vertNormalized,
                 int vertStride,
                 float[] colors,
                 int colorUsage,
                 String colorIdx,
                 int colorSize,
                 boolean colorNormalized,
                 int colorStride,
                 float[] texCoords,
                 int texture,
                 int texUsage,
                 String texIdx,
                 int texSize,
                 boolean texNormalized,
                 int texStride,
                 int[] indices,
                 int indexUsage,
                 int vertexCount) {
        this.vertIdx = program.getAttrib(vertIdx);
        if (this.vertIdx < 0) {
            throw new RuntimeException();
        }
        this.colorIdx = program.getAttrib(colorIdx);
        if (this.colorIdx < 0) {
            throw new RuntimeException();
        }
        this.vertexCount = vertexCount;
        vertVbo = glGenBuffers();
        colorVbo = glGenBuffers();
        this.texture = texture;
        if (texture != 0) {
            this.texIdx = program.getAttrib(texIdx);
            if (this.texIdx < 0) {
                throw new RuntimeException();
            }
            texVbo = glGenBuffers();
            this.texCoords = texCoords;
            this.texUsage = texUsage;
            this.texSize = texSize;
            this.texNormalized = texNormalized;
            this.texStride = texStride;
        }
        idxVbo = glGenBuffers();
        this.vertices = vertices;
        this.vertUsage = vertUsage;
        this.vertSize = vertSize;
        this.vertNormalized = vertNormalized;
        this.vertStride = vertStride;
        this.colors = colors;
        this.colorUsage = colorUsage;
        this.colorSize = colorSize;
        this.colorNormalized = colorNormalized;
        this.colorStride = colorStride;
        this.indices = indices;
        this.indexUsage = indexUsage;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Mesh of(GlProgram program,
                          float[] vertices,
                          String vertIdx,
                          float[] colors,
                          String colorIdx,
                          float[] texCoords,
                          int texture,
                          String texIdx,
                          int[] indices) {
        return builder()
                .program(program)
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

    public static class Builder extends IMesh.Builder<Builder> {
        private GlProgram program;
        private String vertIdx;
        private String colorIdx;
        private String texIdx;

        public Builder program(GlProgram program) {
            this.program = program;
            return this;
        }

        public Builder vertIdx(String vertIdx) {
            this.vertIdx = vertIdx;
            return this;
        }

        public Builder colorIdx(String colorIdx) {
            this.colorIdx = colorIdx;
            return this;
        }

        public Builder texIdx(String texIdx) {
            this.texIdx = texIdx;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public Mesh build() {
            return new Mesh(
                    program,
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
    public void render(int mode) {
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, vertUsage);
        glEnableVertexAttribArray(vertIdx);
        glVertexAttribPointer(vertIdx,
                vertSize,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, colors, colorUsage);
        glEnableVertexAttribArray(colorIdx);
        glVertexAttribPointer(colorIdx,
                colorSize,
                GL_FLOAT,
                colorNormalized,
                colorStride,
                0);
        glBindBuffer(GL_ARRAY_BUFFER, texVbo);
        glBufferData(GL_ARRAY_BUFFER, texCoords, texUsage);
        glEnableVertexAttribArray(texIdx);
        glVertexAttribPointer(texIdx,
                texSize,
                GL_FLOAT,
                texNormalized,
                texStride,
                0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, indexUsage);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (texture != 0) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
        }
        glDrawElements(mode, getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void close() {
        if (vertIdx >= 0) {
            glDisableVertexAttribArray(vertIdx);
        }
        if (colorIdx >= 0) {
            glDisableVertexAttribArray(colorIdx);
        }
        if (texIdx >= 0) {
            glDisableVertexAttribArray(texIdx);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (vertVbo != 0) {
            glDeleteBuffers(vertVbo);
        }
        if (colorVbo != 0) {
            glDeleteBuffers(colorVbo);
        }
        if (texVbo != 0 && texVbo > 0) {
            glDeleteBuffers(texVbo);
        }
        if (idxVbo != 0) {
            glDeleteBuffers(idxVbo);
        }
    }
}
