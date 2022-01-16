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

package org.overrun.glutils.gl.ll;

import org.overrun.glutils.ITesselator;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * {@link ITesselator Tesselator} for OpenGL in immediate mode<br>
 * You must {@link #free() free} it.
 *
 * @author squid233
 * @since 1.6.0
 */
public class Tesselator implements ITesselator {
    private static int vertexCount = 0x80000;
    private static int memoryUse = (2 + 3 + 3) * vertexCount;
    private static final int[] VERTICES = {
        1, 2, 1, 1, 3, 1, 1, 4, 1, 1
    };
    private static final Tesselator INSTANCE = new Tesselator();
    private static final Field VALIDATE;
    private static final Object VALIDATE_OBJ;

    static {
        Object VALIDATE_OBJ1;
        Field VALIDATE1;
        try {
            VALIDATE_OBJ1 = Class.forName(
                    "org.lwjglx.debug.Properties"
                ).getField("VALIDATE")
                .get(null);
            VALIDATE1 = VALIDATE_OBJ1
                .getClass()
                .getField("enabled");
        } catch (NoSuchFieldException
            | IllegalAccessException
            | ClassNotFoundException e) {
            VALIDATE_OBJ1 = null;
            VALIDATE1 = null;
        }
        VALIDATE_OBJ = VALIDATE_OBJ1;
        VALIDATE = VALIDATE1;
    }

    private static float[] array = new float[memoryUse];
    private static FloatBuffer buffer = memAllocFloat(memoryUse);
    private static float r, g, b, u, v;
    private static int vertices;
    private static int len = 3;
    private static int pos;
    private static boolean hasColor;
    private static boolean hasTexture;
    private static int primitive;

    private Tesselator() {
    }

    public static Tesselator getInstance() {
        return INSTANCE;
    }

    /**
     * Set the vertex count
     *
     * @param count The new vertex count
     * @since 2.0.0
     */
    public static void setVertexCount(int count) {
        vertexCount = count;
        memoryUse = (2 + 3 + 3) * count;
        array = new float[memoryUse];
        buffer = memRealloc(buffer, memoryUse);
    }

    private static void clear() {
        buffer.clear();
        vertices = 0;
        pos = 0;
        len = 3;
    }

    @Override
    public Tesselator init(int newPrimitive) {
        clear();
        primitive = newPrimitive;
        hasColor = false;
        hasTexture = false;
        return this;
    }

    @Override
    public Tesselator color(float r, float g, float b) {
        if (!hasColor) {
            len += 3;
        }
        hasColor = true;
        Tesselator.r = r;
        Tesselator.g = g;
        Tesselator.b = b;
        return this;
    }

    @Override
    public Tesselator tex(float u, float v) {
        if (!hasTexture) {
            len += 2;
        }
        hasTexture = true;
        Tesselator.u = u;
        Tesselator.v = v;
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
        // Vertex count per primitive
        var vc = VERTICES[primitive];
        if ((vertices % vc == 0) && (pos >= (vertexCount - len * vc))) {
            draw();
        }
        return this;
    }

    @Override
    public Tesselator draw() {
        boolean b = false;
        try {
            b = VALIDATE != null && VALIDATE.getBoolean(VALIDATE_OBJ);
            if (b) {
                VALIDATE.setBoolean(VALIDATE_OBJ, false);
            }
        } catch (IllegalAccessException ignored) {
        }

        buffer.clear().put(array, 0, pos);
        if (buffer.position() > 0) {
            buffer.flip();
        }

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

        if (b) {
            try {
                VALIDATE.setBoolean(VALIDATE_OBJ, true);
            } catch (IllegalAccessException ignored) {
            }
        }
        return this;
    }

    @Override
    public void free() {
        memFree(buffer);
    }
}
