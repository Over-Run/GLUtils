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

package org.overrun.glutils.internal;

/**
 * @author squid233
 * @since 1.1.0
 */
public class MeshManagerConstant {
    public static final byte COLORED = 0b1000;
    public static final byte TEXTURED = 0b0100;
    public static final byte INDEXED = 0b0010;
    public static final byte NORMALIZED = 0b0001;

    public static boolean colored(byte bit) {
        return (bit & COLORED) == COLORED;
    }

    public static boolean textured(byte bit) {
        return (bit & TEXTURED) == TEXTURED;
    }

    public static boolean indexed(byte bit) {
        return (bit & INDEXED) == INDEXED;
    }

    public static boolean normalized(byte bit) {
        return (bit & NORMALIZED) == NORMALIZED;
    }
}
