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

/**
 * @author squid233
 * @since 0.1.0
 */
public final class GLUtils {
    public static final String VERSION = "0.4.0";
    private static ThrowableCallback throwableCb = Throwable::printStackTrace;
    private static WarningCallback warningCb = GLUtils::defaultWarningCb;
    private static ErrorCallback errorCb = GLUtils::defaultErrorCb;

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
