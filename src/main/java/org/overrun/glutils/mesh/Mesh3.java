/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Overrun Organization
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

import org.overrun.glutils.gl.GLState;
import org.overrun.glutils.tex.Textures;
import org.overrun.glutils.gl.Vao;
import org.overrun.glutils.gl.VertexAttrib;

import static org.lwjgl.opengl.GL15.*;

/**
 * {@link Mesh} for OpenGL 3<br>
 * Used VAO
 *
 * @author squid233
 * @since 0.2.0
 */
public class Mesh3 extends BaseMesh<Mesh3> {
    private final Vao vao = new Vao();

    /**
     * construct
     */
    public Mesh3() {
        bindVao();
    }

    /**
     * @param vertIdx vertices index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 vertIdx(int vertIdx) {
        this.vertIdx = new VertexAttrib(vertIdx);
        return this;
    }

    /**
     * @param colorIdx colors index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 colorIdx(int colorIdx) {
        this.colorIdx = new VertexAttrib(colorIdx);
        return this;
    }

    /**
     * @param texIdx texture coordinates index
     * @return this
     * @since 0.6.0
     */
    public Mesh3 texIdx(int texIdx) {
        this.texIdx = new VertexAttrib(texIdx);
        return this;
    }

    /**
     * @param normalIdx normal vertices index
     * @return this
     * @since 1.1.0
     */
    public Mesh3 normalIdx(int normalIdx) {
        this.normalIdx = new VertexAttrib(normalIdx);
        return this;
    }

    /**
     * bind vao
     *
     * @return this
     * @since 1.1.0
     */
    public Mesh3 bindVao() {
        vao.bind();
        return this;
    }

    /**
     * unbind vao
     *
     * @return this
     * @since 1.1.0
     */
    public Mesh3 unbindVao() {
        vao.unbind();
        return this;
    }

    @Override
    public Mesh3 vertices(float[] vertices) {
        if (vertIdx != null) {
            vertVbo.bind();
            vertVbo.data(vertices, vertUsage);
            vertIdx.pointer(vertDim,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
            vertIdx.enable();
            vertVbo.unbind();
        }
        return super.vertices(vertices);
    }

    @Override
    public Mesh3 colors(float[] colors) {
        super.colors(colors);
        if (colorIdx != null) {
            colorVbo.bind();
            colorVbo.data(colors, colorUsage);
            colorIdx.pointer(colorDim,
                GL_FLOAT,
                colorNormalized,
                colorStride,
                0);
            colorIdx.enable();
            colorVbo.unbind();
        }
        return this;
    }

    @Override
    public Mesh3 texCoords(float[] texCoords) {
        super.texCoords(texCoords);
        if (texIdx != null) {
            texVbo.bind();
            texVbo.data(texCoords, texUsage);
            texIdx.pointer(texDim,
                GL_FLOAT,
                texNormalized,
                texStride,
                0);
            texIdx.enable();
            texVbo.unbind();
        }
        return this;
    }

    @Override
    public Mesh3 normalVert(float[] normalVert) {
        super.normalVert(normalVert);
        if (normalIdx != null) {
            normalVbo.bind();
            normalVbo.data(normalVert, normalUsage);
            normalIdx.pointer(normalDim,
                GL_FLOAT,
                normalNormalized,
                normalStride,
                0);
            normalIdx.enable();
            normalVbo.unbind();
        }
        return this;
    }

    @Override
    public Mesh3 indices(int[] indices) {
        super.indices(indices);
        ibo.bind();
        ibo.data(indices, indexUsage);
        return this;
    }

    @Override
    public void render(int primitive) {
        if (material != null) {
            Textures.active(0);
            getTexture().bind();
        }
        bindVao();
        if (ibo == null) {
            glDrawArrays(primitive, 0, getVertexCount());
        } else {
            glDrawElements(primitive, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        unbindVao();
        if (material != null) {
            getTexture().unbind();
        }
    }

    /**
     * get vao
     *
     * @return {@link #vao}
     */
    public Vao getVao() {
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
                           GLState texture,
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
    public void free() {
        if (vertIdx != null) {
            vertIdx.disable();
        }
        if (colorIdx != null) {
            colorIdx.disable();
        }
        if (texIdx != null) {
            texIdx.disable();
        }
        if (normalIdx != null) {
            normalIdx.disable();
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (vertVbo.check()) {
            vertVbo.free();
        }
        if (colorVbo != null && colorVbo.check()) {
            colorVbo.free();
        }
        if (texVbo != null && texVbo.check()) {
            texVbo.free();
        }
        if (normalVbo != null && normalVbo.check()) {
            normalVbo.free();
        }
        if (ibo != null && ibo.check()) {
            ibo.free();
        }
        unbindVao();
        vao.free();
    }

    @Override
    public Mesh3 getThis() {
        return this;
    }
}
