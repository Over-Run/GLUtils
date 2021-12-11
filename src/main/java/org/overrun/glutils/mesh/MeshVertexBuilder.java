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

/**
 * @author squid233
 * @since 1.2.0
 */
public class MeshVertexBuilder extends BaseMeshVertexBuilder {
    private final Mesh mesh = new Mesh().colorDim(4);

    private void preRender() {
        if (isDirty) {
            mesh.vertices(vertices.toFArray());
            vertices.clear();
            if (colored) {
                mesh.colors(colors.toFArray());
                colors.clear();
            }
            if (textured) {
                mesh.texCoords(textureCoord.toFArray());
                textureCoord.clear();
            }
            if (normalized) {
                mesh.normalVert(normals.toFArray());
                normals.clear();
            }
            isDirty = false;
        }
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void render() {
        preRender();
        mesh.render();
    }

    @Override
    public void render(int mode) {
        preRender();
        mesh.render(mode);
    }

    @Override
    public void free() {
        mesh.free();
    }
}
