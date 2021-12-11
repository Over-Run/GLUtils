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

package org.overrun.glutils.gl.ll;

import org.overrun.glutils.ITesselator;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * {@link ITesselator Tesselator} for OpenGL in immediate mode
 *
 * @author squid233
 * @since 1.6.0
 */
public class Tesselator implements ITesselator {
    public static final int VERTEX_COUNT = 60000;
    public static final int MEMORY_USE = (2 + 3 + 3) * VERTEX_COUNT;
    private static final Tesselator INSTANCE = new Tesselator();
    private final float[] array = new float[MEMORY_USE];
    private final FloatBuffer buffer = memAllocFloat(MEMORY_USE);
    private float r, g, b, u, v;
    private int vertices;
    private int pos;
    private boolean hasColor;
    private boolean hasTexture;

    private Tesselator() {
    }

    public static Tesselator getInstance() {
        return INSTANCE;
    }

    private void clear() {
        buffer.clear();
        vertices = 0;
        pos = 0;
    }

    @Override
    public Tesselator init() {
        clear();
        hasColor = false;
        hasTexture = false;
        return this;
    }

    @Override
    public Tesselator color(float r, float g, float b, float a) {
        hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    @Override
    public Tesselator color(float r, float g, float b) {
        ITesselator.super.color(r, g, b);
        return this;
    }

    @Override
    public Tesselator tex(float u, float v) {
        hasTexture = true;
        this.u = u;
        this.v = v;
        return this;
    }

    @Override
    public Tesselator vertexUV(final float x,
                               final float y,
                               final float z,
                               final float u,
                               final float v) {
        return tex(u, v).vertex(x, y, z);
    }

    @Override
    public Tesselator vertex(final float x,
                             final float y,
                             final float z) {
        if (hasTexture) {
            array[pos++] = u;
            array[pos++] = v;
        }
        if (hasColor) {
            array[pos++] = r;
            array[pos++] = g;
            array[pos++] = b;
        }
        array[pos++] = x;
        array[pos++] = y;
        array[pos++] = z;
        ++vertices;
        return this;
    }

    @Override
    public Tesselator draw() {
        return draw(GL_TRIANGLES);
    }

    @Override
    public Tesselator draw(int primitive) {
        buffer.clear();
        buffer.put(array, 0, pos);
        buffer.flip();

        int mode = GL_V3F;
        if (hasColor) {
            // +c3f
            mode += 3;
        }
        if (hasTexture) {
            // +t2f
            mode += 6;
        }
        glInterleavedArrays(mode, 0, buffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        if (hasTexture) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }
        if (hasColor) {
            glEnableClientState(GL_COLOR_ARRAY);
        }

        glDrawArrays(primitive, 0, vertices);

        glDisableClientState(GL_VERTEX_ARRAY);
        if (hasTexture) {
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        }
        if (hasColor) {
            glDisableClientState(GL_COLOR_ARRAY);
        }

        clear();
        return this;
    }

    @Override
    public void free() {
        memFree(buffer);
    }
}
