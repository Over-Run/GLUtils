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

import java.util.ArrayList;

/**
 * ArrayList of primitive float
 *
 * @author squid233
 * @since 0.7.0
 */
public class FloatArray extends ArrayList<Float> {
    /**
     * Convert boxed-object type to primitive type
     *
     * @return float array
     */
    public float[] toFArray() {
        Float[] floats = toArray(new Float[0]);
        float[] floats1 = new float[floats.length];
        for (int i = 0; i < floats1.length; i++) {
            floats1[i] = floats[i];
        }
        return floats1;
    }

    @Override
    @Deprecated
    public boolean add(Float aFloat) {
        return super.add(aFloat);
    }

    /**
     * add a number to list
     *
     * @param aFloat primitive type float
     * @return is changed
     */
    public boolean add(float aFloat) {
        return super.add(aFloat);
    }
}
