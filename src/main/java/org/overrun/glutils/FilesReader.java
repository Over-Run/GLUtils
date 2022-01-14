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

package org.overrun.glutils;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.lwjgl.system.MemoryUtil.memAlloc;

/**
 * @author squid233
 * @since 2.0.0
 */
public class FilesReader {
    /**
     * Convert a byte array to a direct ByteBuffer.
     *
     * @param bytes The bytes.
     * @return The native ByteBuffer.
     * @since 2.0.0
     */
    public static ByteBuffer ntoBBuffer(byte[] bytes) {
        return memAlloc(bytes.length).put(bytes).flip();
    }

    /**
     * Convert a byte array to a ByteBuffer.
     *
     * @param bytes The bytes.
     * @return The ByteBuffer in JVM.
     * @since 2.0.0
     */
    public static ByteBuffer toBBuffer(byte[] bytes) {
        return ByteBuffer.allocate(bytes.length).put(bytes).flip();
    }

    public static byte[] getBytes(Object o,
                                  String name) {
        return getBytes(getInputStream(o, name));
    }

    public static byte[] getBytes(InputStream stream) {
        try (var is = requireNonNull(stream);
             var bis = new BufferedInputStream(is)) {
            return bis.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get input stream of resource in jar
     *
     * @param o    The object Class or ClassLoader
     * @param name The resource name
     * @return The input stream
     * @since 2.0.0
     */
    @Nullable
    public static InputStream getInputStream(Object o,
                                             String name) {
        return getClassLoader(o).getResourceAsStream(name);
    }

    /**
     * Get the ClassLoader of the Object.
     * <p>
     * Input:<br>
     * An object. May be null, ClassLoader, Class, primitive types.
     * <hr>
     * Output:<br>
     * The ClassLoader. If the param {@code o} provided null or a primitive
     * type, the method returns {@link ClassLoader#getSystemClassLoader() the
     * system ClassLoader}.
     * </p>
     *
     * @param o The object.
     * @return The ClassLoader.
     * @since 2.0.0
     */
    public static ClassLoader getClassLoader(@Nullable Object o) {
        if (o == null) {
            return getSystemClassLoader();
        }
        if (o instanceof ClassLoader) {
            return (ClassLoader) o;
        }
        if (o instanceof Class) {
            return ((Class<?>) o).getClassLoader();
        }
        var cl = o.getClass().getClassLoader();
        return cl != null ? cl : getSystemClassLoader();
    }

    /**
     * Read lines from stream by Object.
     *
     * @param o    The object Class or ClassLoader.
     * @param name The filename.
     * @return File contents.
     * @since 2.0.0
     */
    public static String lines(Object o,
                               String name) {
        return lines(getInputStream(o, name));
    }

    /**
     * Read lines from stream.
     *
     * @param stream The InputStream.
     * @return File contents.
     * @since 0.1.0
     */
    public static String lines(InputStream stream) {
        try (var is = requireNonNull(stream);
             var br = new BufferedReader(
                 new InputStreamReader(is, UTF_8)
             )
        ) {
            var sb = new StringBuilder();
            String read;
            for (int i = 0; (read = br.readLine()) != null; i++) {
                if (i > 0) {
                    sb.append("\n");
                }
                sb.append(read);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
