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

package org.overrun.glutils.internal;

import org.overrun.commonutils.Dimension;
import org.overrun.glutils.tex.AtlasLoom;
import org.overrun.glutils.tex.TexParam;
import org.overrun.glutils.tex.Textures;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 1.1.0
 * @deprecated experimentation
 */
@Deprecated
public class AtlasLoomArray extends AtlasLoom<AtlasLoomArray> {
    /**
     * constructor
     *
     * @param name target id
     * @since 0.4.0
     */
    public AtlasLoomArray(String name) {
        super(name);
    }

    /**
     * load
     *
     * @param defaultW default width
     * @param defaultH default height
     * @param param    Texture parameters
     * @param images   images
     * @param dims     dimensions
     * @return {@link #atlasId}
     * @since 2.0.0
     */
    public int load(int defaultW,
                    int defaultH,
                    TexParam param,
                    int[][] images,
                    Dimension[] dims) {
        int maxWper = defaultW, maxHper = defaultH;
        for (int i = 0; i < images.length; i++) {
            var img = images[i];
            if (img == null) {
                dims[i] = new Dimension(defaultW, defaultH);
            }
            var dim = dims[i];
            int w = dim.getWidth();
            int h = dim.getHeight();
            if (w > maxWper) {
                maxWper = w;
            }
            if (h > maxHper) {
                maxHper = h;
            }
        }
        int siz = (int) ceil(sqrt(images.length));
        width = height = max(siz * maxWper, siz * maxHper);
        atlasId = Textures.load(name + "-atlas",
            width,
            height,
            new int[width * height],
            param);
        int u0 = 0, v0 = 0;
        for (int i = 0; i < images.length; i++) {
            var img = images[i];
            var dim = dims[i];
            int w = dim.getWidth();
            int h = dim.getHeight();
            int[] pixels;
            if (img == null) {
                pixels = new int[w * h];
                if (u0 + w > width) {
                    u0 = 0;
                    v0 += maxHper;
                }
                int j = 0;
                for (int k = 0, s = h / 2; k < s; k++) {
                    for (int l = 0, t = w / 2; l < t; l++) {
                        pixels[j++] = 0xfff800f8;
                    }
                    for (int l = 0, t = w / 2; l < t; l++) {
                        pixels[j++] = 0xff000000;
                    }
                }
                for (int k = 0, s = h / 2; k < s; k++) {
                    for (int l = 0, t = w / 2; l < t; l++) {
                        pixels[j++] = 0xff000000;
                    }
                    for (int l = 0, t = w / 2; l < t; l++) {
                        pixels[j++] = 0xfff800f8;
                    }
                }
            } else {
                pixels = img;
                if (u0 + w > width) {
                    u0 = 0;
                    v0 += maxHper;
                }
            }
            glTexSubImage2D(GL_TEXTURE_2D,
                0,
                width - u0 - 1,
                height - v0 - 1,
                w,
                h,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                pixels);
            u0 += w;
        }
        return atlasId;
    }

    /**
     * throw {@link UnsupportedOperationException}
     *
     * @param loader   class loader
     * @param defaultW default width
     * @param defaultH default height
     * @param param    Texture parameters
     * @param images   images
     * @return an exception
     */
    @Override
    public int load(ClassLoader loader,
                    int defaultW,
                    int defaultH,
                    TexParam param,
                    String... images) {
        throw new UnsupportedOperationException();
    }
}
