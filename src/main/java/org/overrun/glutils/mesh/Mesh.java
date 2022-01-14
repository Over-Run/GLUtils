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

import org.overrun.glutils.gl.GLProgram;
import org.overrun.glutils.gl.GLState;
import org.overrun.glutils.tex.Textures;
import org.overrun.glutils.gl.VertexAttrib;
import org.overrun.glutils.light.Material;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 0.2.0
 */
public class Mesh extends BaseMesh<Mesh> {
    private GLProgram program;

    /**
     * @param program program
     * @return this
     * @since 0.6.0
     */
    public Mesh program(GLProgram program) {
        this.program = program;
        return this;
    }

    /**
     * @param vertIdx vertices index name
     * @return this
     * @since 0.6.0
     */
    public Mesh vertIdx(String vertIdx) {
        this.vertIdx = new VertexAttrib(program.getAttrib(vertIdx));
        return this;
    }

    /**
     * @param colorIdx colors index name
     * @return this
     * @since 0.6.0
     */
    public Mesh colorIdx(String colorIdx) {
        this.colorIdx = new VertexAttrib(program.getAttrib(colorIdx));
        return this;
    }

    /**
     * @param texIdx texture coordinates index name
     * @return this
     * @since 0.6.0
     */
    public Mesh texIdx(String texIdx) {
        this.texIdx = new VertexAttrib(program.getAttrib(texIdx));
        return this;
    }

    /**
     * @param normalIdx normal vertices index name
     * @return this
     * @since 1.1.0
     */
    public Mesh normalIdx(String normalIdx) {
        this.normalIdx = new VertexAttrib(program.getAttrib(normalIdx));
        return this;
    }

    /**
     * construct
     *
     * @param program   program
     * @param vertices  vertices
     * @param vertIdx   vertIdx
     * @param colors    colors
     * @param colorIdx  colorIdx
     * @param texCoords texCoords
     * @param material  material
     * @param texIdx    texIdx
     * @param indices   indices
     * @return mesh
     * @since 1.2.0
     */
    public static Mesh of(GLProgram program,
                          float[] vertices,
                          String vertIdx,
                          float[] colors,
                          String colorIdx,
                          float[] texCoords,
                          Material material,
                          String texIdx,
                          int[] indices) {
        return of(program,
            vertices,
            vertIdx,
            colors,
            colorIdx,
            indices)
            .material(material)
            .texIdx(texIdx)
            .texCoords(texCoords);
    }

    /**
     * construct
     *
     * @param program   program
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
    public static Mesh of(GLProgram program,
                          float[] vertices,
                          String vertIdx,
                          float[] colors,
                          String colorIdx,
                          float[] texCoords,
                          GLState texture,
                          String texIdx,
                          int[] indices) {
        return of(program,
            vertices,
            vertIdx,
            colors,
            colorIdx,
            texCoords,
            new Material(texture, 1),
            texIdx,
            indices);
    }

    /**
     * construct without texture
     *
     * @param program  program
     * @param vertices vertices
     * @param vertIdx  vertIdx
     * @param colors   colors
     * @param colorIdx colorIdx
     * @param indices  indices
     * @return mesh
     * @since 1.1.0
     */
    public static Mesh of(GLProgram program,
                          float[] vertices,
                          String vertIdx,
                          float[] colors,
                          String colorIdx,
                          int[] indices) {
        return new Mesh()
            .program(program)
            .vertIdx(vertIdx)
            .vertices(vertices)
            .colorIdx(colorIdx)
            .colors(colors)
            .indices(indices);
    }

    @Override
    public void render(int primitive) {
        if (vertIdx != null) {
            vertVbo.bind();
            vertVbo.data(vertices, vertUsage);
            vertIdx.pointer(vertDim,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
            vertIdx.enable();
        }
        if (colorVbo != null && colorIdx != null) {
            colorVbo.bind();
            colorVbo.data(colors, colorUsage);
            colorIdx.pointer(colorDim,
                GL_FLOAT,
                colorNormalized,
                colorStride,
                0);
            colorIdx.enable();
        }
        if (texVbo != null && texIdx != null) {
            texVbo.bind();
            texVbo.data(texCoords, texUsage);
            texIdx.pointer(texDim,
                GL_FLOAT,
                texNormalized,
                texStride,
                0);
            texIdx.enable();
        }
        if (normalVbo != null && normalIdx != null) {
            normalVbo.bind();
            normalVbo.data(normalVert, normalUsage);
            normalIdx.pointer(normalDim,
                GL_FLOAT,
                normalNormalized,
                normalStride,
                0);
            normalIdx.enable();
        }
        if (ibo != null) {
            ibo.bind();
            ibo.data(indices, indexUsage);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (material != null) {
            Textures.active(0);
            getTexture().bind();
        }
        if (ibo == null) {
            glDrawArrays(primitive, 0, getVertexCount());
        } else {
            glDrawElements(primitive, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        if (material != null) {
            getTexture().unbind();
        }
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
    }

    @Override
    public Mesh getThis() {
        return this;
    }
}
