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
 * @since 0.6.0
 */
public abstract class BaseMesh<T extends IMesh> implements IMesh {
    protected final int vertVbo;
    protected final int colorVbo;
    protected int texVbo;
    protected int idxVbo;
    protected float[] vertices;
    protected float[] colors;
    protected float[] texCoords;
    protected int[] indices;
    protected int vertUsage = GL_STATIC_DRAW;
    protected int colorUsage = GL_STATIC_DRAW;
    protected int texUsage = GL_STATIC_DRAW;
    protected int indexUsage = GL_STATIC_DRAW;
    protected int vertDim = 3;
    protected int colorDim = 3;
    protected int texDim = 2;
    protected boolean vertNormalized;
    protected boolean colorNormalized;
    protected boolean texNormalized;
    protected int vertStride;
    protected int colorStride;
    protected int texStride;
    protected int texture;
    protected int vertexCount;

    public BaseMesh() {
        vertVbo = glGenBuffers();
        colorVbo = glGenBuffers();
    }

    public abstract T getThis();

    public T vertices(float[] vertices) {
        this.vertices = vertices;
        return getThis();
    }

    public T colors(float[] colors) {
        this.colors = colors;
        return getThis();
    }

    public T texCoords(float[] texCoords) {
        this.texCoords = texCoords;
        if (texVbo == 0) {
            texVbo = glGenBuffers();
        }
        return getThis();
    }

    public T indices(int[] indices) {
        this.indices = indices;
        vertexCount = indices.length;
        if (idxVbo == 0) {
            idxVbo = glGenBuffers();
        }
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

    public T indexUsage(int indexUsage) {
        this.indexUsage = indexUsage;
        return getThis();
    }

    public T vertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
        return getThis();
    }

    public int getVertVbo() {
        return vertVbo;
    }

    public int getColorVbo() {
        return colorVbo;
    }

    public int getTexVbo() {
        return texVbo;
    }

    public int getIdxVbo() {
        return idxVbo;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getColors() {
        return colors;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertUsage() {
        return vertUsage;
    }

    public int getColorUsage() {
        return colorUsage;
    }

    public int getTexUsage() {
        return texUsage;
    }

    public int getIndexUsage() {
        return indexUsage;
    }

    public int getVertDim() {
        return vertDim;
    }

    public int getColorDim() {
        return colorDim;
    }

    public int getTexDim() {
        return texDim;
    }

    public boolean isVertNormalized() {
        return vertNormalized;
    }

    public boolean isColorNormalized() {
        return colorNormalized;
    }

    public boolean isTexNormalized() {
        return texNormalized;
    }

    public int getVertStride() {
        return vertStride;
    }

    public int getColorStride() {
        return colorStride;
    }

    public int getTexStride() {
        return texStride;
    }

    public int getTexture() {
        return texture;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }
}
