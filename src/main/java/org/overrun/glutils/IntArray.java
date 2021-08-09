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
 * ArrayList of primitive int
 *
 * @author squid233
 * @since 0.7.0
 */
public class IntArray extends ArrayList<Integer> {
    /**
     * Convert boxed-object type to primitive type
     *
     * @return int array
     */
    public int[] toIArray() {
        return stream().mapToInt(i -> i).toArray();
    }

    @Override
    @Deprecated
    public boolean add(Integer integer) {
        return super.add(integer);
    }

    /**
     * add a number to list
     *
     * @param anInt primitive type float
     * @return is changed
     */
    public boolean add(int anInt) {
        return super.add(anInt);
    }

    /**
     * add all integers
     *
     * @param ints integers
     * @return true
     * @since 1.1.0
     */
    public boolean addAll(int... ints) {
        for (int i : ints) {
            add(i);
        }
        return true;
    }
}
