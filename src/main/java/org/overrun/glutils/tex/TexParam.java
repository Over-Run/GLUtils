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

package org.overrun.glutils.tex;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 1.5.0
 */
public class TexParam {
    public int minFilter;
    public int magFilter;

    /**
     * Construct a TexParam by param {@link GL11#GL_NEAREST NEAREST}.
     *
     * @return The mipmap mode.
     */
    public static TexParam glNearest() {
        var mode = new TexParam();
        return mode.minFilter(GL_NEAREST).magFilter(GL_NEAREST);
    }

    /**
     * Construct a TexParam by param {@link GL11#GL_LINEAR LINEAR}.
     *
     * @return The mipmap mode.
     */
    public static TexParam glLinear() {
        var mode = new TexParam();
        return mode.minFilter(GL_LINEAR).magFilter(GL_LINEAR);
    }

    /**
     * Call {@link GL11#glTexParameteri TexParameteri} to set
     * {@link GL11#GL_TEXTURE_MIN_FILTER MIN_FILTER}.
     *
     * @param target The texture target.
     * @since 2.0.0
     */
    public void glMinFilter(int target) {
        if (minFilter != 0) {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
        }
    }

    /**
     * Call {@link GL11#glTexParameteri TexParameteri} to set
     * {@link GL11#GL_TEXTURE_MAG_FILTER MAG_FILTER}.
     *
     * @param target The texture target.
     * @since 2.0.0
     */
    public void glMagFilter(int target) {
        if (magFilter != 0) {
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        }
    }

    /**
     * Set {@link #minFilter}.
     *
     * @param minFilter minFilter to set
     * @return this
     */
    public TexParam minFilter(int minFilter) {
        this.minFilter = minFilter;
        return this;
    }

    /**
     * Set {@link #magFilter}.
     *
     * @param magFilter magFilter to set
     * @return this
     */
    public TexParam magFilter(int magFilter) {
        this.magFilter = magFilter;
        return this;
    }
}
