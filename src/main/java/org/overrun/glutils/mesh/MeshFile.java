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

import org.overrun.glutils.FloatArray;
import org.overrun.glutils.IntArray;

/**
 * @author squid233
 * @since 0.7.0
 */
public class MeshFile {
    /**
     * is textured
     */
    public boolean textured = false;
    /**
     * is indexed
     */
    public boolean indexed = false;
    /**
     * dimensions
     */
    public int vertDim = 3, colorDim = 3, texDim = 2;
    /**
     * vertices
     */
    public final FloatArray vertices = new FloatArray();
    /**
     * colors
     */
    public final FloatArray colors = new FloatArray();
    /**
     * texCoords
     */
    public final FloatArray texCoords = new FloatArray();
    /**
     * indices
     */
    public final IntArray indices = new IntArray();
}
