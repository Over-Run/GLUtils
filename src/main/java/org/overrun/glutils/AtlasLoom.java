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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL12.*;

/**
 * @author squid233
 * @since 0.3.0
 */
public class AtlasLoom {
    private final Map<String, UV> uvMap = new HashMap<>();
    private final Map<String, BufferedImage> imageMap = new HashMap<>();
    private int width, maxH;
    private int atlasId;

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

    public int getAtlasId() {
        return atlasId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return maxH;
    }

    public float getU0(String id) {
        return (float) uvMap.get(id).u0 / (float) width;
    }

    public float getU1(String id) {
        return (float) uvMap.get(id).u1 / (float) width;
    }

    public float getV0(String id) {
        return (float) uvMap.get(id).v0 / (float) maxH;
    }

    public float getV1(String id) {
        return (float) uvMap.get(id).v1 / (float) maxH;
    }

    private void addImg(String img) {
        if (img != null) {
            imageMap.put(img, null);
        }
    }

    public static class UV {
        public final int u0, v0 = 0;
        public final int u1, v1;

        public UV(int u0,
                  int u1, int v1) {
            this.u0 = u0;
            this.u1 = u1;
            this.v1 = v1;
        }
    }
}
