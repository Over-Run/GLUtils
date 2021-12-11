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

package org.overrun.glutils.internal;

import org.overrun.glutils.mesh.Mesh3;

import java.util.HashMap;
import java.util.Map;

import static org.overrun.glutils.internal.MeshManagerConstant.*;

/**
 * @author squid233
 * @since 1.1.0
 */
public class MeshManager3 {
    private static final Map<Byte, Mesh3> meshes = new HashMap<>();

    /**
     * push a mesh to manager
     *
     * @param bit bits that contains 4 bits
     *            (colored &lt;&lt; 3
     *            | textured &lt;&lt; 2
     *            | indexed &lt;&lt; 1
     *            | normalized)
     * @return a mesh
     */
    public static Mesh3 putOrGet(byte bit) {
        if (meshes.containsKey(bit)) {
            return meshes.get(bit);
        }
        Mesh3 mesh = new Mesh3();
        meshes.put(bit, mesh);
        return mesh;
    }

    /**
     * push a mesh to manager
     *
     * @param colored    enable color
     * @param textured   enable texture
     * @param indexed    enable index
     * @param normalized enable normals
     * @return a mesh
     */
    public static Mesh3 putOrGet(boolean colored,
                                 boolean textured,
                                 boolean indexed,
                                 boolean normalized) {
        byte bit = 0b0000;
        if (colored) {
            bit |= COLORED;
        }
        if (textured) {
            bit |= TEXTURED;
        }
        if (indexed) {
            bit |= INDEXED;
        }
        if (normalized) {
            bit |= NORMALIZED;
        }
        return putOrGet(bit);
    }
}
