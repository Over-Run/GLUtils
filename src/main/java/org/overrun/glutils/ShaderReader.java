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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * @author squid233
 * @since 0.1.0
 */
public class ShaderReader {
    /**
     * Read lines from stream by loader.
     *
     * @param loader The ClassLoader.
     * @param name   The filename.
     * @return File contents.
     * @throws Exception When file not found.
     */
    public static String lines(ClassLoader loader,
                               String name)
            throws Exception {
        return lines(loader.getResourceAsStream(name));
    }

    /**
     * Read lines from stream.
     *
     * @param stream The InputStream.
     * @return File contents.
     * @throws Exception When file not found.
     */
    public static String lines(InputStream stream)
            throws Exception {
        try (InputStream is = requireNonNull(stream);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(is, UTF_8)
             )
        ) {
            StringBuilder sb = new StringBuilder();
            String read;
            for (int i = 0; (read = br.readLine()) != null; i++) {
                if (i > 0) {
                    sb.append("\n");
                }
                sb.append(read);
            }
            return sb.toString();
        }
    }
}
