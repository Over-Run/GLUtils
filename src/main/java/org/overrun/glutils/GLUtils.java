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

package org.overrun.glutils;

import org.overrun.glutils.callback.ErrorCallback;
import org.overrun.glutils.callback.ThrowableCallback;
import org.overrun.glutils.callback.WarningCallback;

import static org.lwjgl.opengl.GL30.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class GLUtils {
    public static final String VERSION = "0.6.0";
    public static final String NO_ERR = "GL_NO_ERROR (0)";
    private static ThrowableCallback throwableCb = Throwable::printStackTrace;
    private static WarningCallback warningCb = GLUtils::defaultWarningCb;
    private static ErrorCallback errorCb = GLUtils::defaultErrorCb;

    /**
     * glGetError to string
     *
     * @return error name
     * @since 0.5.0
     */
    public static String glErrorString() {
        int err = glGetError();
        switch (err) {
            case GL_NO_ERROR:
                return NO_ERR;
            case GL_INVALID_ENUM:
                return "GL_INVALID_ENUM (1280)";
            case GL_INVALID_VALUE:
                return "GL_INVALID_VALUE (1281)";
            case GL_INVALID_OPERATION:
                return "GL_INVALID_OPERATION (1282)";
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                return "GL_INVALID_FRAMEBUFFER_OPERATION (1286)";
            case GL_OUT_OF_MEMORY:
                return "GL_OUT_OF_MEMORY (1285)";
            case GL_STACK_UNDERFLOW:
                return "GL_STACK_UNDERFLOW (1284)";
            case GL_STACK_OVERFLOW:
                return "GL_STACK_OVERFLOW (1283)";
            default:
                return "GL_UNKNOWN_ERROR";
        }
    }

    /**
     * Print error code and name.
     * <p>Only for debugging.</p>
     *
     * @param i mark
     * @since 0.5.0
     */
    public static void glPrintError(int i) {
        String s = glErrorString();
        if (s.equals(NO_ERR)) {
            System.out.println(i + ":" + s);
        } else {
            System.err.println(i + ":" + s);
        }
    }

    public static void defaultWarningCb(Object msg, Object... format) {
        defaultErrorCb(msg, format);
    }

    public static void defaultErrorCb(Object msg, Object... format) {
        System.err.printf(msg + "%n", format);
    }

    public static void setWarningCb(WarningCallback cb) {
        warningCb = cb;
    }

    public static WarningCallback getWarningCb() {
        return warningCb;
    }

    public static void setErrorCb(ErrorCallback cb) {
        errorCb = cb;
    }

    public static ErrorCallback getErrorCb() {
        return errorCb;
    }

    /**
     * Set the throwable callback.
     * <p>
     * You can set the callback to your logger.
     * </p>
     *
     * @param cb Consumer with a Throwable.
     */
    public static void setThrowableCb(ThrowableCallback cb) {
        throwableCb = cb;
    }

    public static ThrowableCallback getThrowableCb() {
        return throwableCb;
    }
}
