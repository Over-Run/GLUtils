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

package org.overrun.glutils;

/**
 * Recommend implementing yourself.
 *
 * @author squid233
 * @since 1.2.0
 */
public interface IVertexBuilder extends IMR {
    /**
     * Add a vertex
     *
     * @param x x
     * @param y y
     * @param z z
     * @return this
     */
    IVertexBuilder vertex(float x, float y, float z);

    /**
     * Add a vertex color
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     * @return this
     */
    IVertexBuilder color(float r, float g, float b, float a);

    /**
     * Add a vertex color
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return this
     */
    default IVertexBuilder color(float r, float g, float b) {
        return color(r, g, b, 1);
    }

    /**
     * Add a vertex texture
     *
     * @param x x
     * @param y y
     * @return this
     */
    IVertexBuilder texture(float x, float y);

    /**
     * Add a vertex normal
     *
     * @param x x
     * @param y y
     * @param z z
     * @return this
     */
    IVertexBuilder normal(float x, float y, float z);

    /**
     * Render this mesh
     *
     * @see #render(int)
     * @since 1.3.0
     */
    void render();

    /**
     * Render this mesh with specified geometry mode
     *
     * @param mode Geometry mode
     * @see #render()
     * @since 1.3.0
     */
    void render(int mode);

    /**
     * No effect
     */
    default void next() {
    }

    @Override
    default void imr_vertex(float x, float y, float z) {
        vertex(x, y, z);
    }
}
