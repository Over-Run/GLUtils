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

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public abstract class BaseMesh<T extends IMesh> implements IMesh {
    /**
     * vertex vbo
     */
    protected final int vertVbo;
    /**
     * color vbo
     */
    protected int colorVbo;
    /**
     * texture vbo
     */
    protected int texVbo;
    /**
     * ibo
     */
    protected int ibo;
    /**
     * vertices
     */
    protected float[] vertices;
    /**
     * colors
     */
    protected float[] colors;
    /**
     * texture coordinates
     */
    protected float[] texCoords;
    /**
     * indices
     */
    protected int[] indices;
    /**
     * vertex usage
     */
    protected int vertUsage = GL_STATIC_DRAW;
    /**
     * color usage
     */
    protected int colorUsage = GL_STATIC_DRAW;
    /**
     * texture coordinate usage
     */
    protected int texUsage = GL_STATIC_DRAW;
    /**
     * index usage
     */
    protected int indexUsage = GL_STATIC_DRAW;
    /**
     * vertex dimensions
     */
    protected int vertDim = 3;
    /**
     * color dimensions
     */
    protected int colorDim = 3;
    /**
     * texture dimensions
     */
    protected int texDim = 2;
    /**
     * vertex normalized
     */
    protected boolean vertNormalized;
    /**
     * color normalized
     */
    protected boolean colorNormalized;
    /**
     * texture normalized
     */
    protected boolean texNormalized;
    /**
     * vertex stride
     */
    protected int vertStride;
    /**
     * color stride
     */
    protected int colorStride;
    /**
     * texture stride
     */
    protected int texStride;
    /**
     * texture id
     */
    protected int texture;
    /**
     * vertex count
     */
    protected int vertexCount;

    /**
     * construct
     */
    public BaseMesh() {
        vertVbo = glGenBuffers();
    }

    /**
     * get this
     *
     * @return this
     */
    public abstract T getThis();

    /**
     * set vertices
     *
     * @param vertices vertices
     * @return this
     */
    public T vertices(float[] vertices) {
        this.vertices = vertices;
        return getThis();
    }

    /**
     * set colors
     *
     * @param colors colors
     * @return this
     */
    public T colors(float[] colors) {
        this.colors = colors;
        if (colorVbo == 0) {
            colorVbo = glGenBuffers();
        }
        return getThis();
    }

    /**
     * set texture coordinates
     *
     * @param texCoords texture coordinates
     * @return this
     */
    public T texCoords(float[] texCoords) {
        this.texCoords = texCoords;
        if (texVbo == 0) {
            texVbo = glGenBuffers();
        }
        return getThis();
    }

    /**
     * set indices
     *
     * @param indices indices
     * @return this
     */
    public T indices(int[] indices) {
        this.indices = indices;
        vertexCount = indices.length;
        if (ibo == 0) {
            ibo = glGenBuffers();
        }
        return getThis();
    }

    /**
     * set vert usage
     *
     * @param vertUsage vert usage
     * @return this
     */
    public T vertUsage(int vertUsage) {
        this.vertUsage = vertUsage;
        return getThis();
    }

    /**
     * set vert dim
     *
     * @param vertDim vert dim
     * @return this
     */
    public T vertDim(int vertDim) {
        this.vertDim = vertDim;
        return getThis();
    }

    /**
     * set vert normalized
     *
     * @param vertNormalized vert normalized
     * @return this
     */
    public T vertNormalized(boolean vertNormalized) {
        this.vertNormalized = vertNormalized;
        return getThis();
    }

    /**
     * set vert stride
     *
     * @param vertStride vert stride
     * @return this
     */
    public T vertStride(int vertStride) {
        this.vertStride = vertStride;
        return getThis();
    }

    /**
     * set color usage
     *
     * @param colorUsage color usage
     * @return this
     */
    public T colorUsage(int colorUsage) {
        this.colorUsage = colorUsage;
        return getThis();
    }

    /**
     * set color dim
     *
     * @param colorDim color dim
     * @return this
     */
    public T colorDim(int colorDim) {
        this.colorDim = colorDim;
        return getThis();
    }

    /**
     * set color normalized
     *
     * @param colorNormalized color normalized
     * @return this
     */
    public T colorNormalized(boolean colorNormalized) {
        this.colorNormalized = colorNormalized;
        return getThis();
    }

    /**
     * set color stride
     *
     * @param colorStride color stride
     * @return this
     */
    public T colorStride(int colorStride) {
        this.colorStride = colorStride;
        return getThis();
    }

    /**
     * set texture id
     *
     * @param texture texture id
     * @return this
     */
    public T texture(int texture) {
        this.texture = texture;
        return getThis();
    }

    /**
     * set texture coord usage
     *
     * @param texUsage texture coord usage
     * @return this
     */
    public T texUsage(int texUsage) {
        this.texUsage = texUsage;
        return getThis();
    }

    /**
     * set texture coord dim
     *
     * @param texDim texture coord dim
     * @return this
     */
    public T texDim(int texDim) {
        this.texDim = texDim;
        return getThis();
    }

    /**
     * set texture coord normalized
     *
     * @param texNormalized texture coord normalized
     * @return this
     */
    public T texNormalized(boolean texNormalized) {
        this.texNormalized = texNormalized;
        return getThis();
    }

    /**
     * set texture coord stride
     *
     * @param texStride texture coord stride
     * @return this
     */
    public T texStride(int texStride) {
        this.texStride = texStride;
        return getThis();
    }

    /**
     * set index usage
     *
     * @param indexUsage index usage
     * @return this
     */
    public T indexUsage(int indexUsage) {
        this.indexUsage = indexUsage;
        return getThis();
    }

    /**
     * set vertex count
     *
     * @param vertexCount vertex count
     * @return this
     */
    public T vertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
        return getThis();
    }

    /**
     * get vertex vbo
     *
     * @return vertex vbo
     */
    public int getVertVbo() {
        return vertVbo;
    }

    /**
     * get color vbo
     *
     * @return color vbo
     */
    public int getColorVbo() {
        return colorVbo;
    }

    /**
     * get texture vbo
     *
     * @return texture vbo
     */
    public int getTexVbo() {
        return texVbo;
    }

    /**
     * get ibo
     *
     * @return ibo
     */
    public int getIbo() {
        return ibo;
    }

    /**
     * get vertices
     *
     * @return vertices
     */
    public float[] getVertices() {
        return vertices;
    }

    /**
     * get colors
     *
     * @return colors
     */
    public float[] getColors() {
        return colors;
    }

    /**
     * get texture coordinates
     *
     * @return texture coordinates
     */
    public float[] getTexCoords() {
        return texCoords;
    }

    /**
     * get indices
     *
     * @return indices
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * get vertex usage
     *
     * @return vertex usage
     */
    public int getVertUsage() {
        return vertUsage;
    }

    /**
     * get color usage
     *
     * @return color usage
     */
    public int getColorUsage() {
        return colorUsage;
    }

    /**
     * get texture usage
     *
     * @return texture usage
     */
    public int getTexUsage() {
        return texUsage;
    }

    /**
     * get index usage
     *
     * @return index usage
     */
    public int getIndexUsage() {
        return indexUsage;
    }

    /**
     * get vertex dim
     *
     * @return vertex dim
     */
    public int getVertDim() {
        return vertDim;
    }

    /**
     * get color dim
     *
     * @return color dim
     */
    public int getColorDim() {
        return colorDim;
    }

    /**
     * get texture dim
     *
     * @return texture dim
     */
    public int getTexDim() {
        return texDim;
    }

    /**
     * is vertex normalized
     *
     * @return vertex normalized
     */
    public boolean isVertNormalized() {
        return vertNormalized;
    }

    /**
     * is color normalized
     *
     * @return color normalized
     */
    public boolean isColorNormalized() {
        return colorNormalized;
    }

    /**
     * is texture normalized
     *
     * @return texture normalized
     */
    public boolean isTexNormalized() {
        return texNormalized;
    }

    /**
     * get vertex stride
     *
     * @return vertex stride
     */
    public int getVertStride() {
        return vertStride;
    }

    /**
     * get colors stride
     *
     * @return colors stride
     */
    public int getColorStride() {
        return colorStride;
    }

    /**
     * get texture stride
     *
     * @return texture stride
     */
    public int getTexStride() {
        return texStride;
    }

    /**
     * get texture id
     *
     * @return texture id
     */
    public int getTexture() {
        return texture;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }
}
