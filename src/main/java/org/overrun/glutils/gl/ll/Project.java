/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static java.lang.Math.*;
import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2.0.0
 */
public class Project {
    private static final float[] IDENTITY_MATRIX = {
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    };
    private static final FloatBuffer matrix = createFloatBuffer(16);

    /**
     * Make matrix an identity matrix
     *
     * @param m the matrix
     */
    private static void __gluMakeIdentityf(FloatBuffer m) {
        int oldPos = m.position();
        m.put(IDENTITY_MATRIX);
        m.position(oldPos);
    }

    public static void gluPerspective(
        float fovy,
        float aspect,
        float zNear,
        float zFar) {
        float sine, cotangent, deltaZ;
        float radians = (float) toRadians(fovy / 2);

        deltaZ = zFar - zNear;
        sine = (float) sin(radians);

        if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
            return;
        }

        cotangent = (float) cos(radians) / sine;

        __gluMakeIdentityf(matrix);

        matrix.put(0 * 4 + 0, cotangent / aspect);
        matrix.put(1 * 4 + 1, cotangent);
        matrix.put(2 * 4 + 2, -(zFar + zNear) / deltaZ);
        matrix.put(2 * 4 + 3, -1);
        matrix.put(3 * 4 + 2, -2 * zNear * zFar / deltaZ);
        matrix.put(3 * 4 + 3, 0);

        glMultMatrixf(matrix);
    }

    public static void gluPickMatrix(
        float x,
        float y,
        float w,
        float h,
        IntBuffer viewport) {
        if (w <= 0 || h <= 0) {
            return;
        }

        glTranslatef(
            (viewport.get(viewport.position() + 2) - 2 * (x - viewport.get(viewport.position() + 0))) / w,
            (viewport.get(viewport.position() + 3) - 2 * (y - viewport.get(viewport.position() + 1))) / h,
            0);
        glScalef(
            viewport.get(viewport.position() + 2) / w,
            viewport.get(viewport.position() + 3) / h,
            1.0f);
    }
}
