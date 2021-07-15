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

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 0.2.0
 */
public interface IMesh extends AutoCloseable {
    /**
     * getVertexCount
     *
     * @return vertex count
     */
    int getVertexCount();

    /**
     * Render this mesh.
     *
     * @param mode Drawing mode.
     */
    void render(int mode);

    /**
     * Render this mesh.
     */
    default void render() {
        render(GL_TRIANGLES);
    }

    /**
     * Cleanup all resources.
     */
    @Override
    void close();

    @Deprecated
    abstract class Builder<T> {
        protected float[] vertices;
        protected int vertUsage = GL_STATIC_DRAW;
        protected int vertDim = 3;
        protected boolean vertNormalized = false;
        protected int vertStride = 0;
        protected float[] colors;
        protected int colorUsage = GL_STATIC_DRAW;
        protected int colorDim = 3;
        protected boolean colorNormalized = false;
        protected int colorStride = 0;
        protected float[] texCoords = null;
        protected int texture = 0;
        protected int texUsage = GL_STATIC_DRAW;
        protected int texDim = 2;
        protected boolean texNormalized = false;
        protected int texStride = 0;
        protected int[] indices;
        protected int indexUsage = GL_STATIC_DRAW;
        protected int vertexCount;

        public abstract T getThis();

        public T vertices(float[] vertices) {
            this.vertices = vertices;
            return getThis();
        }

        public T vertUsage(int vertUsage) {
            this.vertUsage = vertUsage;
            return getThis();
        }

        public T vertDim(int vertDim) {
            this.vertDim = vertDim;
            return getThis();
        }

        public T vertNormalized(boolean vertNormalized) {
            this.vertNormalized = vertNormalized;
            return getThis();
        }

        public T vertStride(int vertStride) {
            this.vertStride = vertStride;
            return getThis();
        }

        public T colors(float[] colors) {
            this.colors = colors;
            return getThis();
        }

        public T colorUsage(int colorUsage) {
            this.colorUsage = colorUsage;
            return getThis();
        }

        public T colorDim(int colorDim) {
            this.colorDim = colorDim;
            return getThis();
        }

        public T colorNormalized(boolean colorNormalized) {
            this.colorNormalized = colorNormalized;
            return getThis();
        }

        public T colorStride(int colorStride) {
            this.colorStride = colorStride;
            return getThis();
        }

        public T texCoords(float[] texCoords) {
            this.texCoords = texCoords;
            return getThis();
        }

        public T texture(int texture) {
            this.texture = texture;
            return getThis();
        }

        public T texUsage(int texUsage) {
            this.texUsage = texUsage;
            return getThis();
        }

        public T texDim(int texDim) {
            this.texDim = texDim;
            return getThis();
        }

        public T texNormalized(boolean texNormalized) {
            this.texNormalized = texNormalized;
            return getThis();
        }

        public T texStride(int texStride) {
            this.texStride = texStride;
            return getThis();
        }

        public T indices(int[] indices) {
            this.indices = indices;
            vertexCount = indices.length;
            return getThis();
        }

        public T indexUsage(int indexUsage) {
            this.indexUsage = indexUsage;
            return getThis();
        }

        public T vertexCount(int vertexCount) {
            this.vertexCount = vertexCount;
            return getThis();
        }

        public abstract IMesh build();
    }
}
