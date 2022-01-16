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

import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL12.*;

/**
 * @author squid233
 * @since 1.5.0
 */
public class TexParam {
    /**
     * Filter
     */
    public int minFilter, magFilter;
    /**
     * Wrap
     *
     * @since 2.0.0
     */
    public int wrapS, wrapT, wrapR;

    /**
     * Construct a TexParam by param {@link GL12#GL_NEAREST NEAREST}.
     *
     * @return The mipmap mode.
     */
    public static TexParam glNearest() {
        var mode = new TexParam();
        return mode.minFilter(GL_NEAREST).magFilter(GL_NEAREST);
    }

    /**
     * Construct a TexParam by param {@link GL12#GL_LINEAR LINEAR}.
     *
     * @return The mipmap mode.
     */
    public static TexParam glLinear() {
        var mode = new TexParam();
        return mode.minFilter(GL_LINEAR).magFilter(GL_LINEAR);
    }

    public void glSet(int target) {
        glMinFilter(target);
        glMagFilter(target);
        glWrapS(target);
        glWrapT(target);
        glWrapR(target);
    }

    /**
     * Call {@link GL12#glTexParameteri TexParameteri} to set
     * {@link GL12#GL_TEXTURE_MIN_FILTER MIN_FILTER}.
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
     * Call {@link GL12#glTexParameteri TexParameteri} to set
     * {@link GL12#GL_TEXTURE_MAG_FILTER MAG_FILTER}.
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
     * Call {@link GL12#glTexParameteri TexParameteri} to set
     * {@link GL12#GL_TEXTURE_WRAP_S WRAP_S}.
     *
     * @param target The texture target.
     * @since 2.0.0
     */
    public void glWrapS(int target) {
        if (wrapS != 0) {
            glTexParameteri(target, GL_TEXTURE_WRAP_S, wrapS);
        }
    }

    /**
     * Call {@link GL12#glTexParameteri TexParameteri} to set
     * {@link GL12#GL_TEXTURE_WRAP_T WRAP_T}.
     *
     * @param target The texture target.
     * @since 2.0.0
     */
    public void glWrapT(int target) {
        if (wrapT != 0) {
            glTexParameteri(target, GL_TEXTURE_WRAP_T, wrapT);
        }
    }

    /**
     * Call {@link GL12#glTexParameteri TexParameteri} to set
     * {@link GL12#GL_TEXTURE_WRAP_R WRAP_R}.
     *
     * @param target The texture target.
     * @since 2.0.0
     */
    public void glWrapR(int target) {
        if (wrapS != 0) {
            glTexParameteri(target, GL_TEXTURE_WRAP_R, wrapR);
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

    /**
     * Set {@link #wrapS}.
     *
     * @param wrapS wrapS to set
     * @return this
     * @since 2.0.0
     */
    public TexParam wrapS(int wrapS) {
        this.wrapS = wrapS;
        return this;
    }

    /**
     * Set {@link #wrapT}.
     *
     * @param wrapT wrapT to set
     * @return this
     * @since 2.0.0
     */
    public TexParam wrapT(int wrapT) {
        this.wrapT = wrapT;
        return this;
    }

    /**
     * Set {@link #wrapR}.
     *
     * @param wrapR wrapR to set
     * @return this
     * @since 2.0.0
     */
    public TexParam wrapR(int wrapR) {
        this.wrapR = wrapR;
        return this;
    }
}
