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

package org.overrun.glutils.ll;

import org.overrun.glutils.IVertexBuilder;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 1.4.0
 */
public class DisplayListBuilder
    implements IVertexBuilder, AutoCloseable {
    private final int list;

    /**
     * Construct and generate a GL list.
     */
    public DisplayListBuilder() {
        list = glGenLists(1);
        if (!glIsList(list)) {
            throw new RuntimeException("Can't create GL list");
        }
    }

    /**
     * New list
     *
     * @return this
     */
    public DisplayListBuilder newList() {
        glNewList(1, GL_COMPILE);
        return this;
    }

    /**
     * Begin drawing
     *
     * @param mode Geometry mode
     * @return this
     */
    public DisplayListBuilder begin(int mode) {
        glBegin(mode);
        return this;
    }

    /**
     * End drawing
     *
     * @return this
     */
    public DisplayListBuilder end() {
        glEnd();
        return this;
    }

    /**
     * End list
     *
     * @return this
     */
    public DisplayListBuilder endList() {
        glEndList();
        return this;
    }

    /**
     * Get the list number
     *
     * @return the list
     */
    public int getList() {
        return list;
    }

    @Override
    public DisplayListBuilder vertex(float x, float y, float z) {
        glVertex3f(x, y, z);
        return this;
    }

    @Override
    public DisplayListBuilder color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        return this;
    }

    @Override
    public IVertexBuilder color(float r, float g, float b) {
        glColor3f(r, g, b);
        return this;
    }

    @Override
    public DisplayListBuilder texture(float x, float y) {
        glTexCoord2f(x, y);
        return this;
    }

    @Override
    public DisplayListBuilder normal(float x, float y, float z) {
        glNormal3f(x, y, z);
        return this;
    }

    /**
     * Render the list
     */
    @Override
    public void render() {
        glCallList(list);
    }

    /**
     * Render the list
     * <p>
     * <b>Note:</b> {@code mode} is ignored.
     *
     * @param mode Geometry mode
     */
    @Override
    public void render(int mode) {
        glCallList(list);
    }

    /**
     * Delete the list.
     */
    @Override
    public void close() {
        glDeleteLists(list, 1);
    }
}
