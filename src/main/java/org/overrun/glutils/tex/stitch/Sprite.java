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

package org.overrun.glutils.tex.stitch;

import org.jetbrains.annotations.NotNull;
import org.overrun.glutils.tex.NativeImage;

import java.util.StringJoiner;

import static java.lang.Integer.compare;

/**
 * @author squid233
 * @since 2.0.0
 */
public class Sprite implements Comparable<Sprite> {
    public final Object id;
    public final Block block;
    public final NativeImage buffer;

    public Sprite(Object id,
                  Block block,
                  NativeImage buffer) {
        this.id = id;
        this.block = block;
        this.buffer = buffer;
    }

    public void free() {
        buffer.free();
    }

    @Override
    public int compareTo(@NotNull Sprite o) {
        var c = compare(o.buffer.height, buffer.height);
        return c == 0 ? compare(o.buffer.width, buffer.width) : c;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Sprite.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("block=" + block)
            .add("buffer=" + buffer)
            .toString();
    }
}
