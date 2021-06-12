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

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

public class StbImg implements AutoCloseable {
    public static final Recycler RECYCLER = STBImage::stbi_image_free;
    private final int width;
    private final int height;
    private final ByteBuffer data;
    private final Recycler recycler;

    public interface Recycler {
        void free(ByteBuffer data);
    }

    public static Recycler defaultRecycler() {
        return RECYCLER;
    }

    public StbImg(int width,
                  int height,
                  ByteBuffer data,
                  Recycler recycler) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.recycler = recycler;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getData() {
        return data;
    }

    public Recycler getRecycler() {
        return recycler;
    }

    @Override
    public void close() {
        recycler.free(data);
    }
}
