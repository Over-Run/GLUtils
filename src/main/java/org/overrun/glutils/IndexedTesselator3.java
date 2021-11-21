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

import org.joml.Matrix4fc;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 1.5.0
 */
public class IndexedTesselator3 extends Tesselator3 {
    private final int[] array = new int[10 * VERTEX_COUNT];
    private final IntBuffer buffer = MemoryUtil.memAllocInt(10 * VERTEX_COUNT);
    private boolean firstTime = true;

    public IndexedTesselator3(boolean fixed) {
        super(fixed);
    }

    @Override
    protected void clear() {
        super.clear();
        buffer.clear();
    }

    @Override
    public IndexedTesselator3 init() {
        super.init();
        return this;
    }

    @Override
    public IndexedTesselator3 color(float r, float g, float b, float a) {
        super.color(r, g, b, a);
        return this;
    }

    @Override
    public IndexedTesselator3 color(float r, float g, float b) {
        super.color(r, g, b);
        return this;
    }

    @Override
    public IndexedTesselator3 tex(float u, float v) {
        super.tex(u, v);
        return this;
    }

    @Override
    public IndexedTesselator3 vertexUV(float x, float y, float z, float u, float v) {
        super.vertexUV(x, y, z, u, v);
        return this;
    }

    @Override
    public IndexedTesselator3 vertex(float x, float y, float z) {
        super.vertex(x, y, z);
        return this;
    }

    @Override
    public IndexedTesselator3 draw(Matrix4fc mvp) {
        super.draw(mvp);
        return this;
    }

    public IndexedTesselator3 indices(final long offset,
                                      final int... indices) {
        System.arraycopy(indices, 0, array, (int) offset, indices.length);
        return this;
    }

    public IndexedTesselator3 indices(final int... indices) {
        return indices(0, indices);
    }

    @Override
    protected void render() {
        if (!fixed) {
            buffer.clear();
            buffer.put(array, 0, pos).flip();
        } else if (firstTime) {
            firstTime = false;
            buffer.put(array, 0, pos).flip();
        }
        glDrawElements(GL_TRIANGLES, buffer);
    }

    @Override
    public IndexedTesselator3 getThis() {
        return this;
    }
}
