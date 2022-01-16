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

import static org.lwjgl.opengl.GL12.*;

/**
 * @author squid233
 * @since 2.0.0
 */
public class Util {
    /**
     * Method compPerPix.
     *
     * @param format format
     * @return int
     */
    protected static int compPerPix(int format) {
        /* Determine number of components per pixel */
        switch (format) {
            case GL_COLOR_INDEX:
            case GL_STENCIL_INDEX:
            case GL_DEPTH_COMPONENT:
            case GL_RED:
            case GL_GREEN:
            case GL_BLUE:
            case GL_ALPHA:
            case GL_LUMINANCE:
                return 1;
            case GL_LUMINANCE_ALPHA:
                return 2;
            case GL_RGB:
            case GL_BGR:
                return 3;
            case GL_RGBA:
            case GL_BGRA:
                return 4;
            default:
                return -1;
        }
    }

    /**
     * Method nearestPower.
     * <p>
     * Compute the nearest power of 2 number.  This algorithm is a little strange, but it works quite well.
     * </p>
     *
     * @param value value
     * @return int
     */
    protected static int nearestPower(int value) {
        int i;

        i = 1;

        /* Error! */
        if (value == 0)
            return -1;

        for (; ; ) {
            if (value == 1) {
                return i;
            } else if (value == 3) {
                return i << 2;
            }
            value >>= 1;
            i <<= 1;
        }
    }

    protected static int bytesPerPixel(int format, int type) {
        int n, m;

        switch (format) {
            case GL_COLOR_INDEX:
            case GL_STENCIL_INDEX:
            case GL_DEPTH_COMPONENT:
            case GL_RED:
            case GL_GREEN:
            case GL_BLUE:
            case GL_ALPHA:
            case GL_LUMINANCE:
                n = 1;
                break;
            case GL_LUMINANCE_ALPHA:
                n = 2;
                break;
            case GL_RGB:
            case GL_BGR:
                n = 3;
                break;
            case GL_RGBA:
            case GL_BGRA:
                n = 4;
                break;
            default:
                n = 0;
        }

        switch (type) {
            case GL_UNSIGNED_BYTE:
            case GL_BYTE:
            case GL_BITMAP:
                m = 1;
                break;
            case GL_UNSIGNED_SHORT:
            case GL_SHORT:
                m = 2;
                break;
            case GL_UNSIGNED_INT:
            case GL_INT:
            case GL_FLOAT:
                m = 4;
                break;
            default:
                m = 0;
        }

        return n * m;
    }
}
