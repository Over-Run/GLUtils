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

package org.overrun.glutils.math;

import org.joml.Matrix4f;

import static org.joml.Math.toRadians;

/**
 * @author squid233
 * @since 0.5.0
 */
public class Transform {
    /**
     * matrix4f.setPerspective
     * <p>
     * {@code fovy} will convert to radians
     * </p>
     *
     * @param matrix4f matrix
     * @param fovy     fov in <b>degree</b>
     * @param aspect   aspect radio
     * @param zNear    zNear
     * @param zFar     zFar
     * @return {@link Matrix4f#setPerspective(float, float, float, float)
     * matrix4f.setPerspective}
     * @see Matrix4f#setPerspective(float, float, float, float) setPerspective
     */
    public static Matrix4f setPerspective(Matrix4f matrix4f,
                                          float fovy,
                                          float aspect,
                                          float zNear,
                                          float zFar) {
        return matrix4f.setPerspective(toRadians(fovy),
                aspect,
                zNear,
                zFar);
    }

    /**
     * matrix4f.setPerspective
     * <p>
     * {@code fovy} will convert to radians
     * </p>
     *
     * @param matrix4f matrix
     * @param fovy     fov in <b>degree</b>
     * @param width    viewport width
     * @param height   viewport height
     * @param zNear    zNear
     * @param zFar     zFar
     * @return {@link #setPerspective(Matrix4f, float, float, float, float)}
     * @see Matrix4f#setPerspective(float, float, float, float) setPerspective
     */
    public static Matrix4f setPerspective(Matrix4f matrix4f,
                                          float fovy,
                                          int width,
                                          int height,
                                          float zNear,
                                          float zFar) {
        return setPerspective(matrix4f,
                fovy,
                (float) width / (float) height,
                zNear,
                zFar);
    }

    /**
     * matrix4f.rotationX
     *
     * @param matrix4f matrix
     * @param angle    the angle in <b>degree</b>
     * @return {@link Matrix4f#rotationX matrix4f.rotationX}
     * @see Matrix4f#rotationX(float) rotationX
     */
    public static Matrix4f rotationX(Matrix4f matrix4f,
                                     float angle) {
        return matrix4f.rotationX(toRadians(angle));
    }

    /**
     * matrix4f.rotateY
     *
     * @param matrix4f matrix
     * @param angle    the angle in <b>degree</b>
     * @return {@link Matrix4f#rotateY matrix4f.rotateY}
     * @see Matrix4f#rotateY(float) rotateY
     */
    public static Matrix4f rotateY(Matrix4f matrix4f,
                                     float angle) {
        return matrix4f.rotateY(toRadians(angle));
    }

    /**
     * matrix4f.rotateZ
     *
     * @param matrix4f matrix
     * @param angle    the angle in <b>degree</b>
     * @return {@link Matrix4f#rotateZ matrix4f.rotateZ}
     * @see Matrix4f#rotateZ(float) rotateZ
     */
    public static Matrix4f rotateZ(Matrix4f matrix4f,
                                   float angle) {
        return matrix4f.rotateZ(toRadians(angle));
    }
}