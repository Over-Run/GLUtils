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

import org.overrun.glutils.util.GLULoggerImpl;
import org.overrun.glutils.util.IGLULogger;

import java.lang.reflect.InvocationTargetException;

import static org.lwjgl.opengl.GL30.*;
import static org.overrun.commonutils.ArrayHelper.expand;
import static org.overrun.commonutils.ArrayHelper.toIArray;
import static org.overrun.glutils.Versions.current;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class GLUtils {
    /**
     * current version
     */
    public static final String VERSION = current();
    /**
     * no error
     */
    public static final String NO_ERR = "GL_NO_ERROR (0)";
    /**
     * The version major index
     */
    private static final int MAJOR = 0;
    /**
     * The version minor index
     */
    private static final int MINOR = 1;
    /**
     * The version patch index
     */
    private static final int PATCH = 2;
    /**
     * The version pre-build index
     */
    private static final int PRE_BUILD = 3;
    /**
     * The logger.
     *
     * @since 2.0.0
     */
    private static IGLULogger logger = GLULoggerImpl.getInstance();

    /**
     * Check current version older than {@code other}
     *
     * @param other Other version
     * @return {@link #VERSION} &lt; {@code other}
     * @since 0.9.0
     */
    public static boolean isOlder(String other) {
        // example: 0.9.0 < 0.10.0
        // so 0.9.0 is older than 0.10.0
        // so return true
        int[] curr = expand(toIArray(VERSION.split("\\.")), 4);
        int[] oth = expand(toIArray(other.split("\\.")), 4);
        if (curr[MAJOR] < oth[MAJOR]) {
            return true;
        }
        if (curr[MAJOR] > oth[MINOR]) {
            return false;
        }

        if (curr[MINOR] < oth[MINOR]) {
            return true;
        }
        if (curr[MINOR] > oth[MINOR]) {
            return false;
        }

        if (curr[PATCH] < oth[MINOR]) {
            return true;
        }
        if (curr[PATCH] > oth[MINOR]) {
            return false;
        }

        if (curr[PRE_BUILD] == 0 && oth[PRE_BUILD] != 0) {
            return false;
        }
        return curr[PRE_BUILD] < oth[PRE_BUILD];
    }

    /**
     * Check current version newer than {@code other}
     *
     * @param other Other version
     * @return {@link #VERSION} &gt; {@code other}
     * @since 0.9.0
     */
    public static boolean isNewer(String other) {
        // example: 0.9.0 > 0.10.0
        // so 0.9.0 is older than 0.10.0
        // so return false
        int[] curr = expand(toIArray(VERSION.split("\\.")), 4);
        int[] oth = expand(toIArray(other.split("\\.")), 4);
        if (curr[MAJOR] > oth[MAJOR]) {
            return true;
        }
        if (curr[MAJOR] < oth[MINOR]) {
            return false;
        }

        if (curr[MINOR] > oth[MINOR]) {
            return true;
        }
        if (curr[MINOR] < oth[MINOR]) {
            return false;
        }

        if (curr[PATCH] > oth[MINOR]) {
            return true;
        }
        if (curr[PATCH] < oth[MINOR]) {
            return false;
        }

        if (curr[PRE_BUILD] == 0 && oth[PRE_BUILD] != 0) {
            return true;
        }
        return curr[PRE_BUILD] > oth[PRE_BUILD];
    }

    /**
     * Check current version equals {@code other}
     *
     * @param other Other version
     * @return {@link #VERSION} == {@code other}
     * @since 0.9.0
     */
    public static boolean isEqual(String other) {
        // example: 0.9.0 > 0.10.0
        // so 0.9.0 is older than 0.10.0
        // so return false
        int[] curr = expand(toIArray(VERSION.split("\\.")), 4);
        int[] oth = expand(toIArray(other.split("\\.")), 4);
        return curr[MAJOR] == oth[MAJOR]
            && curr[MINOR] == oth[MINOR]
            && curr[PATCH] == oth[PATCH]
            && curr[PRE_BUILD] == oth[PRE_BUILD];
    }

    /**
     * new instance of a class with no params
     *
     * @param clazz class type
     * @param <T>   type
     * @return instance of clazz
     * @since 1.1.0
     */
    public static <T> T newClass(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
            | IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * new instance of a class with params
     *
     * @param clazz          class type
     * @param <T>            type
     * @param parameterTypes the parameter array
     * @param initArgs       array of objects to be passed as arguments to
     *                       the constructor call; values of primitive types are wrapped in
     *                       a wrapper object of the appropriate type (e.g. a {@code float}
     *                       in a {@link Float})
     * @return instance of clazz
     * @since 1.1.0
     */
    public static <T> T newClass(Class<T> clazz,
                                 Class<?>[] parameterTypes,
                                 Object... initArgs) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes).newInstance(initArgs);
        } catch (InstantiationException
            | IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

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
     * <p>
     * Only for debugging.
     * </p>
     *
     * @param id marking id
     * @since 0.5.0
     */
    public static void debug(int id) {
        String err = glErrorString();
        String s = id + ": " + err;
        if (err.equals(NO_ERR)) {
            System.out.println(s);
        } else {
            System.err.println(s);
        }
    }

    /**
     * Set the logger for GLUtils.
     *
     * @param l The logger.
     * @since 2.0.0
     */
    public static void setLogger(IGLULogger l) {
        if (l == null) {
            l = GLULoggerImpl.getInstance();
        }
        logger = l;
    }

    /**
     * Get the logger in GLUtils.
     *
     * @return The {@link #logger}.
     * @since 2.0.0
     */
    public static IGLULogger getLogger() {
        return logger;
    }
}
