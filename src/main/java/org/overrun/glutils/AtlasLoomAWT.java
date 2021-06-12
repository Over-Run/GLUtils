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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL12.*;

public class AtlasLoomAWT extends AtlasLoom<BufferedImage> {
    @Override
    public void load(String name,
                     ClassLoader loader,
                     int defaultW,
                     int defaultH,
                     int mode,
                     String... images) {
        for (String img : images) {
            addImg(img);
        }
        for (String img : imageMap.keySet()) {
            BufferedImage bi;
            try (InputStream is = loader.getResourceAsStream(img)) {
                bi = ImageIO.read(Objects.requireNonNull(is));
            } catch (IOException e) {
                GLUtils.getThrowableCb().accept(e);
                bi = new BufferedImage(defaultW,
                        defaultH,
                        BufferedImage.TYPE_INT_ARGB);
            }
            imageMap.put(img, bi);
            int w = bi.getWidth();
            int h = bi.getHeight();
            uvMap.put(img, new UV(width, width + w, h));
            width += w;
            if (h > maxH) {
                maxH = h;
            }
        }
        atlasId = Textures.load(name + "-atlas",
                width,
                maxH,
                new int[width * maxH],
                mode);
        int i = 0;
        for (Map.Entry<String, BufferedImage> e : imageMap.entrySet()) {
            BufferedImage bi = e.getValue();
            int w;
            if (bi != null) {
                w = bi.getWidth();
                int h = bi.getHeight();
                glTexSubImage2D(GL_TEXTURE_2D,
                        0,
                        i,
                        0,
                        w,
                        h,
                        GL_BGRA,
                        GL_UNSIGNED_BYTE,
                        bi.getRGB(0, 0, w, h, null, 0, w));
            } else {
                w = defaultW;
                int[] pixels = new int[defaultW * defaultH];
                int j = 0;
                for (int k = 0, s = defaultH / 2; k < s; k++) {
                    for (int l = 0, t = defaultW / 2; l < t; l++) {
                        pixels[j++] = 0xfff800f8;
                    }
                    for (int l = 0, t = defaultW / 2; l < t; l++) {
                        pixels[j++] = 0xff000000;
                    }
                }
                for (int k = 0, s = defaultH / 2; k < s; k++) {
                    for (int l = 0, t = defaultW / 2; l < t; l++) {
                        pixels[j++] = 0xff000000;
                    }
                    for (int l = 0, t = defaultW / 2; l < t; l++) {
                        pixels[j++] = 0xfff800f8;
                    }
                }
                glTexSubImage2D(GL_TEXTURE_2D,
                        0,
                        i,
                        0,
                        w,
                        defaultH,
                        GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        pixels);
            }
            i += w;
        }
    }
}
