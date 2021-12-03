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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

/**
 * @author squid233
 * @since 1.5.0
 */
public enum Direction {
    NORTH(0, 1, 0, 0, -1),
    SOUTH(1, 0, 0, 0, 1),
    WEST(2, 3, -1, 0, 0),
    EAST(3, 2, 1, 0, 0),
    UP(4, 5, 0, 1, 0),
    DOWN(5, 4, 0, -1, 0);

    public final int id;
    public final int opposite;
    public final int x;
    public final int y;
    public final int z;

    Direction(final int id,
              final int opposite,
              final int x,
              final int y,
              final int z) {
        this.id = id;
        this.opposite = opposite;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Direction byId(final int id) {
        return values()[id];
    }

    public Direction opposite() {
        return byId(opposite);
    }

    @Contract(value = " -> new", pure = true)
    @NotNull
    public Vector3i toVector() {
        return new Vector3i(x, y, z);
    }
}
