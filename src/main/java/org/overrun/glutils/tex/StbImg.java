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

package org.overrun.glutils.tex;

import org.jetbrains.annotations.Contract;

import java.io.IOException;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.overrun.glutils.GLUtils.getLogger;

/**
 * @author squid233
 * @since 0.4.0
 */
public class StbImg {
    @Contract("_ -> fail")
    public static void thr(String filename)
        throws IOException {
        throw new IOException("Error loading image [" +
            filename +
            "] : " +
            stbi_failure_reason());
    }

    @Contract("_ -> fail")
    public static void thrRE(String filename) {
        throw new RuntimeException("Error loading image [" +
            filename +
            "] : " +
            stbi_failure_reason());
    }

    @Contract("_ -> fail")
    public static void thrOut(String filename) {
        getLogger().error("Error loading image [" +
            filename +
            "] : " +
            stbi_failure_reason());
    }
}
