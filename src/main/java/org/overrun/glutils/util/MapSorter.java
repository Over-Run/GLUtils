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

package org.overrun.glutils.util;

import java.util.*;

/**
 * @author squid233
 * @since 2.0.0
 */
public class MapSorter {
    public static <K, V> LinkedHashMap<K, V> sort(Map<K, V> src,
                                                  Comparator<Map.Entry<K, V>> comparator) {
        var list = new LinkedList<>(src.entrySet());
        list.sort(comparator);
        var dst = new LinkedHashMap<K, V>();
        for (var e : list) {
            dst.put(e.getKey(), e.getValue());
        }
        return dst;
    }

    public static <K, V> LinkedHashMap<K, V> sortByKey(Map<K, V> src,
                                                       Comparator<K> comparator) {
        return sort(src, (o1, o2) ->
            comparator.compare(o1.getKey(), o2.getKey()));
    }

    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> src,
                                                         Comparator<V> comparator) {
        return sort(src, (o1, o2) ->
            comparator.compare(o1.getValue(), o2.getValue()));
    }
}
