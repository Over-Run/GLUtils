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

import org.overrun.glutils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author squid233
 * @since 2.0.0
 */
public class GLU {
    /* Errors: (return value 0 = no error) */
    public static final int GLU_INVALID_ENUM = 100900;
    public static final int GLU_INVALID_VALUE = 100901;
    public static final int GLU_OUT_OF_MEMORY = 100902;
    public static final int GLU_INCOMPATIBLE_GL_VERSION = 100903;

    public static int gluBuild2DMipmaps(
        int target,
        int components,
        int width,
        int height,
        int format,
        int type,
        ByteBuffer data) {
        return MipMap.gluBuild2DMipmaps(target,
            components,
            width,
            height,
            format,
            type,
            data);
    }

    public static int gluScaleImage(
        int format,
        int widthIn,
        int heightIn,
        int typeIn,
        ByteBuffer dataIn,
        int widthOut,
        int heightOut,
        int typeOut,
        ByteBuffer dataOut) {
        return MipMap.gluScaleImage(format,
            widthIn,
            heightIn,
            typeIn,
            dataIn,
            widthOut,
            heightOut,
            typeOut,
            dataOut);
    }

    public static void gluPerspective(
        float fovy,
        float aspect,
        float zNear,
        float zFar) {
        Project.gluPerspective(fovy,
            aspect,
            zNear,
            zFar);
    }

    public static void gluPickMatrix(
        float x,
        float y,
        float w,
        float h,
        IntBuffer viewport) {
        Project.gluPickMatrix(x,
            y,
            w,
            h,
            viewport);
    }

    public static String gluErrorString(int errorCode) {
        switch (errorCode) {
            case GLU_INVALID_ENUM:
                return "Invalid enum (glu)";
            case GLU_INVALID_VALUE:
                return "Invalid value (glu)";
            case GLU_OUT_OF_MEMORY:
                return "Out of memory (glu)";
            default:
                return GLUtils.glErrorString(errorCode);
        }
    }
}
