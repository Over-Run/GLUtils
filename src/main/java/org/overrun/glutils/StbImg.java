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

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

/**
 * @author squid233
 * @since 0.4.0
 */
public class StbImg {
    /**
     * Default cleaner
     */
    public static final Cleaner CLEANER = STBImage::stbi_image_free;
    private final int width;
    private final int height;
    private final ByteBuffer data;
    private final Cleaner cleaner;
    private final boolean failed;

    /**
     * @author squid233
     * @since 0.4.0
     */
    public interface Cleaner {
        /**
         * free memory
         *
         * @param data image data
         */
        void free(ByteBuffer data);
    }

    /**
     * get default cleaner
     *
     * @return {@link #CLEANER}
     */
    public static Cleaner defaultCleaner() {
        return CLEANER;
    }

    /**
     * construct
     *
     * @param width   image width
     * @param height  image height
     * @param data    image data
     * @param cleaner cleaner
     * @param failed  is failed
     */
    public StbImg(int width,
                  int height,
                  ByteBuffer data,
                  Cleaner cleaner,
                  boolean failed) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.cleaner = cleaner;
        this.failed = failed;
    }

    /**
     * get width
     *
     * @return {@link #width}
     */
    public int getWidth() {
        return width;
    }

    /**
     * get height
     *
     * @return {@link #height}
     */
    public int getHeight() {
        return height;
    }

    /**
     * get data
     *
     * @return {@link #data}
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * get cleaner
     *
     * @return {@link #cleaner}
     */
    public Cleaner getCleaner() {
        return cleaner;
    }

    /**
     * is failed
     *
     * @return {@link #failed}
     */
    public boolean isFailed() {
        return failed;
    }

    public void free() {
        cleaner.free(data);
    }
}
