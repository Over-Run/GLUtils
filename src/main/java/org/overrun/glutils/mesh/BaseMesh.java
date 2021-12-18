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

import org.overrun.glutils.gl.Vbo;
import org.overrun.glutils.light.Material;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public abstract class BaseMesh<T extends IMesh> implements IMesh {
    /**
     * vertex vbo
     */
    protected final Vbo vertVbo = new Vbo(GL_ARRAY_BUFFER);
    /**
     * color vbo
     */
    protected Vbo colorVbo;
    /**
     * texture vbo
     */
    protected Vbo texVbo;
    /**
     * normal vbo
     *
     * @since 1.1.0
     */
    protected Vbo normalVbo;
    /**
     * ibo
     */
    protected Vbo ibo;
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
     * normal vertices
     *
     * @since 1.1.0
     */
    protected float[] normalVert;
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
     * normal vertex usage
     *
     * @since 1.1.0
     */
    protected int normalUsage = GL_STATIC_DRAW;
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
     * normal dimensions
     *
     * @since 1.1.0
     */
    protected int normalDim = 3;
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
     * normals normalized
     *
     * @since 1.1.0
     */
    protected boolean normalNormalized;
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
     * normal stride
     *
     * @since 1.1.0
     */
    protected int normalStride;
    /**
     * material
     *
     * @since 1.2.0
     */
    protected Material material;
    /**
     * vertex count
     */
    protected int vertexCount;

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
     * set colors
     *
     * @param colors colors
     * @return this
     */
    public T colors(float[] colors) {
        this.colors = colors;
        if (colorVbo == null) {
            colorVbo = new Vbo(GL_ARRAY_BUFFER);
        }
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
        if (material == null) {
            material = new Material(texture, 1);
        } else {
            material.setTexture(texture);
        }
        return getThis();
    }

    /**
     * set material
     *
     * @param material material
     * @return this
     * @since 1.2.0
     */
    public T material(Material material) {
        this.material = material;
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
        if (texVbo == null) {
            texVbo = new Vbo(GL_ARRAY_BUFFER);
        }
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
     * set normals
     *
     * @param normalVert normal vertex
     * @return this
     * @since 1.1.0
     */
    public T normalVert(float[] normalVert) {
        this.normalVert = normalVert;
        if (normalVbo == null) {
            normalVbo = new Vbo(GL_ARRAY_BUFFER);
        }
        return getThis();
    }

    /**
     * set normal usage
     *
     * @param normalUsage normal usage
     * @return this
     * @since 1.1.0
     */
    public T normalUsage(int normalUsage) {
        this.normalUsage = normalUsage;
        return getThis();
    }

    /**
     * set normal dim
     *
     * @param normalDim normal dim
     * @return this
     * @since 1.1.0
     */
    public T normalDim(int normalDim) {
        this.normalDim = normalDim;
        return getThis();
    }

    /**
     * set normal normalized
     *
     * @param normalNormalized normal normalized
     * @return this
     * @since 1.1.0
     */
    public T normalNormalized(boolean normalNormalized) {
        this.normalNormalized = normalNormalized;
        return getThis();
    }

    /**
     * set normal stride
     *
     * @param normalStride normal stride
     * @return this
     * @since 1.1.0
     */
    public T normalStride(int normalStride) {
        this.normalStride = normalStride;
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
        if (ibo == null) {
            ibo = new Vbo(GL_ELEMENT_ARRAY_BUFFER);
        }
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
    public Vbo getVertVbo() {
        return vertVbo;
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
     * get vertex usage
     *
     * @return vertex usage
     */
    public int getVertUsage() {
        return vertUsage;
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
     * is vertex normalized
     *
     * @return vertex normalized
     */
    public boolean isVertNormalized() {
        return vertNormalized;
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
     * get color vbo
     *
     * @return color vbo
     */
    public Vbo getColorVbo() {
        return colorVbo;
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
     * get color usage
     *
     * @return color usage
     */
    public int getColorUsage() {
        return colorUsage;
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
     * is color normalized
     *
     * @return color normalized
     */
    public boolean isColorNormalized() {
        return colorNormalized;
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
     * get texture vbo
     *
     * @return texture vbo
     */
    public Vbo getTexVbo() {
        return texVbo;
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
     * get texture usage
     *
     * @return texture usage
     */
    public int getTexUsage() {
        return texUsage;
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
     * is texture normalized
     *
     * @return texture normalized
     */
    public boolean isTexNormalized() {
        return texNormalized;
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
     * get ibo
     *
     * @return ibo
     */
    public Vbo getIbo() {
        return ibo;
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
     * get index usage
     *
     * @return index usage
     */
    public int getIndexUsage() {
        return indexUsage;
    }

    /**
     * get normal vbo
     *
     * @return normal vbo
     * @since 1.1.0
     */
    public Vbo getNormalVbo() {
        return normalVbo;
    }

    /**
     * get normal vertices
     *
     * @return normal vertices
     * @since 1.1.0
     */
    public float[] getNormalVert() {
        return normalVert;
    }

    /**
     * get normal usage
     *
     * @return normal usage
     * @since 1.1.0
     */
    public int getNormalUsage() {
        return normalUsage;
    }

    /**
     * get normal dim
     *
     * @return normal dim
     * @since 1.1.0
     */
    public int getNormalDim() {
        return normalDim;
    }

    /**
     * is normal normalized
     *
     * @return normal normalized
     * @since 1.1.0
     */
    public boolean isNormalNormalized() {
        return normalNormalized;
    }

    /**
     * get normal stride
     *
     * @return normal stride
     * @since 1.1.0
     */
    public int getNormalStride() {
        return normalStride;
    }

    /**
     * get texture id
     *
     * @return texture id
     */
    public int getTexture() {
        return material.getTexture();
    }

    /**
     * get material
     *
     * @return {@link #material}
     * @since 1.2.0
     */
    public Material getMaterial() {
        return material;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }
}
