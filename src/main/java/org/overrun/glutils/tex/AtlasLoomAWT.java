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

package org.overrun.glutils.tex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.GLUtils.getLogger;

/**
 * @author squid233
 * @since 0.4.0
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class AtlasLoomAWT extends AtlasLoom<AWTImage> {
    /**
     * constructor
     *
     * @param name target id
     * @since 0.4.0
     */
    public AtlasLoomAWT(String name) {
        super(name);
    }

    @Override
    public int load(ClassLoader cl,
                    int defaultW,
                    int defaultH,
                    TexParam param,
                    String... images) {
        for (String img : images) {
            addImg(img);
        }
        int maxWPer = defaultW, maxHPer = defaultH;
        for (String img : imageMap.keySet()) {
            BufferedImage bi;
            boolean isNull = false;
            try (InputStream is = cl.getResourceAsStream(img)) {
                bi = ImageIO.read(Objects.requireNonNull(is));
            } catch (IOException e) {
                getLogger().catching(e);
                bi = new BufferedImage(defaultW,
                    defaultH,
                    BufferedImage.TYPE_INT_ARGB);
                isNull = true;
            }
            imageMap.put(img, new AWTImage(isNull, bi));
            int w = bi.getWidth();
            int h = bi.getHeight();
            if (w > maxWPer) {
                maxWPer = w;
            }
            if (h > maxHPer) {
                maxHPer = h;
            }
        }
        int siz = (int) ceil(sqrt(images.length));
        width = height = max(siz * maxWPer, siz * maxHPer);
        atlasId = Textures.load(name + "-atlas",
            width,
            height,
            new int[width * height],
            param);
        int u0 = 0, v0 = 0;
        for (var e : imageMap.entrySet()) {
            var awti = e.getValue();
            var bi = awti.img;
            int w = bi.getWidth();
            int h = bi.getHeight();
            int[] pixels;
            if (awti.isNull) {
                pixels = new int[w * h];
                if (u0 + w > width) {
                    u0 = 0;
                    v0 += maxHPer;
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
                pixels = AWTImage.getRGB(bi);
                if (u0 + w > width) {
                    u0 = 0;
                    v0 += maxHPer;
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
            uvMap.put(e.getKey(), new UV(u0, v0, u0 + w, v0 + h));
        }
        return atlasId;
    }
}
