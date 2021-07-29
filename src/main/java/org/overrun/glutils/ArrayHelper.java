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
import java.util.Arrays;
import java.util.List;

/**
 * @author squid233
 * @since 0.7.0
 */
public class ArrayHelper {
    /**
     * remove null or empty strings
     *
     * @param arr array
     * @return new array
     */
    public static String[] removeNull(String[] arr) {
        List<String> list = new ArrayList<>(arr.length);
        list.addAll(Arrays.asList(arr));
        while (list.remove(null) || list.remove("")) ;
        return list.toArray(new String[0]);
    }

    /**
     * Convert String array to int array
     *
     * @param arr String array
     * @return int array
     * @since 0.9.0
     */
    public static int[] toIArray(String[] arr) {
        int[] ia = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ia[i] = Integer.parseInt(arr[i]);
        }
        return ia;
    }

    /**
     * Expand array length by number 0
     *
     * @param arr src array
     * @param newLength new length
     * @return Expanded array
     * @since 0.9.0
     */
    public static int[] expand(int[] arr, int newLength) {
        if (newLength <= arr.length) {
            return arr;
        }
        int[] ints = new int[newLength];
        System.arraycopy(arr, 0, ints, 0, arr.length);
        for (int i = arr.length; i < newLength; i++) {
            ints[i] = 0;
        }
        return ints;
    }
}
