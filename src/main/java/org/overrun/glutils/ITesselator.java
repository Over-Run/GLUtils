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

package org.overrun.glutils;

/**
 * @author squid233
 * @since 1.5.0
 */
public interface ITesselator {
    /**
     * Initialize the tesselator with the primitive.
     *
     * @param primitive The primitive
     * @return this
     * @since 2.0.0
     */
    ITesselator init(int primitive);

    default ITesselator color(final float r,
                              final float g,
                              final float b,
                              final float a) {
        return color(r, g, b);
    }

    ITesselator color(final float r,
                      final float g,
                      final float b);

    ITesselator tex(final float u,
                    final float v);

    default ITesselator vertexUV(final float x,
                                 final float y,
                                 final float z,
                                 final float u,
                                 final float v) {
        return tex(u, v).vertex(x, y, z);
    }

    ITesselator vertex(final float x,
                       final float y,
                       final float z);

    /**
     * Draw elements.
     *
     * @return this
     */
    ITesselator draw();

    default void free() {
    }
}
