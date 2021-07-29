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

package org.overrun.glutils.mesh;

import org.overrun.glutils.GLProgram;

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
        return new Mesh()
                .program(program)
                .vertIdx(vertIdx)
                .vertices(vertices)
                .colorIdx(colorIdx)
                .colors(colors)
                .texture(texture)
                .texIdx(texIdx)
                .texCoords(texCoords)
                .indices(indices);
    }

    @Override
    public void render(int mode) {
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, vertUsage);
        glEnableVertexAttribArray(vertIdx);
        glVertexAttribPointer(vertIdx,
                vertDim,
                GL_FLOAT,
                vertNormalized,
                vertStride,
                0);
        if (colorVbo != 0) {
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
        if (texVbo != 0) {
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
        if (ibo != 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, indexUsage);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (texture != 0) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
        }
        if (ibo == 0) {
            glDrawArrays(mode, 0, getVertexCount());
        } else {
            glDrawElements(mode, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
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
        if (texVbo != 0) {
            glDeleteBuffers(texVbo);
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
