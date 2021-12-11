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

import org.overrun.glutils.gl.GLProgram;
import org.overrun.glutils.gl.Textures;
import org.overrun.glutils.light.Material;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author squid233
 * @since 0.2.0
 */
public class Mesh extends BaseMesh<Mesh> {
    private GLProgram program;
    private int vertIdx;
    private int colorIdx;
    private int texIdx;
    private int normalIdx;

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
        this.vertIdx = program.getAttrib(vertIdx);
        return this;
    }

    /**
     * @param colorIdx colors index name
     * @return this
     * @since 0.6.0
     */
    public Mesh colorIdx(String colorIdx) {
        this.colorIdx = program.getAttrib(colorIdx);
        return this;
    }

    /**
     * @param texIdx texture coordinates index name
     * @return this
     * @since 0.6.0
     */
    public Mesh texIdx(String texIdx) {
        this.texIdx = program.getAttrib(texIdx);
        return this;
    }

    /**
     * @param normalIdx normal vertices index name
     * @return this
     * @since 1.1.0
     */
    public Mesh normalIdx(String normalIdx) {
        this.normalIdx = program.getAttrib(normalIdx);
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
                          int texture,
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
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, vertUsage);
        glEnableVertexAttribArray(vertIdx);
        glVertexAttribPointer(vertIdx,
                vertDim,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
        if (colorVbo != 0 && colorIdx >= -1) {
            glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
            glBufferData(GL_ARRAY_BUFFER, colors, colorUsage);
            glEnableVertexAttribArray(colorIdx);
            glVertexAttribPointer(colorIdx,
                    colorDim,
                    GL_FLOAT,
                    colorNormalized,
                    colorStride,
                    0);
        }
        if (texVbo != 0 && texIdx >= -1) {
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
        if (normalVbo != 0 && normalIdx >= -1) {
            glBindBuffer(GL_ARRAY_BUFFER, normalVbo);
            glBufferData(GL_ARRAY_BUFFER, normalVert, normalUsage);
            glEnableVertexAttribArray(normalIdx);
            glVertexAttribPointer(normalIdx,
                    normalDim,
                    GL_FLOAT,
                    normalNormalized,
                    normalStride,
                    0);
        }
        if (ibo != 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, indexUsage);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (material != null) {
            Textures.active(0);
            Textures.bind2D(getTexture());
        }
        if (ibo == 0) {
            glDrawArrays(primitive, 0, getVertexCount());
        } else {
            glDrawElements(primitive, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        Textures.unbind2D();
    }

    @Override
    public void free() {
        if (vertIdx >= 0) {
            glDisableVertexAttribArray(vertIdx);
        }
        if (colorIdx >= 0) {
            glDisableVertexAttribArray(colorIdx);
        }
        if (texIdx >= 0) {
            glDisableVertexAttribArray(texIdx);
        }
        if (normalIdx >= 0) {
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
    }

    @Override
    public Mesh getThis() {
        return this;
    }
}
