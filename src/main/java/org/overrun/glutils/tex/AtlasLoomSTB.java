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

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.overrun.glutils.tex.StbImg.Cleaner;

import java.nio.ByteBuffer;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.overrun.glutils.GLUtils.getLogger;

/**
 * @author squid233
 * @since 0.4.0
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class AtlasLoomSTB extends AtlasLoom<StbImg> {
    /**
     * constructor
     *
     * @param name target id
     * @since 0.4.0
     */
    public AtlasLoomSTB(String name) {
        super(name);
    }

    @Override
    public int load(ClassLoader loader,
                    int defaultW,
                    int defaultH,
                    TexParam param,
                    String... images) {
        for (String img : images) {
            addImg(img);
        }
        int maxWPer = defaultW, maxHPer = defaultH;
        try (var stack = MemoryStack.stackPush()) {
            var pw = stack.mallocInt(1);
            var ph = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            for (String img : imageMap.keySet()) {
                int w, h;
                Cleaner cleaner;
                var bb = stbi_load(img, pw, ph, pc, STBI_rgb_alpha);
                boolean failed = bb == null;
                if (failed) {
                    getLogger().error("Can't load image \"" +
                        img +
                        "\": " +
                        stbi_failure_reason());
                    bb = memAlloc(defaultW * defaultH * 4);
                    w = defaultW;
                    h = defaultH;
                    cleaner = MemoryUtil::memFree;
                } else {
                    pw.flip();
                    ph.flip();
                    w = pw.get();
                    h = ph.get();
                    cleaner = StbImg.CLEANER;
                }
                imageMap.put(img, new StbImg(w, h, bb, cleaner, failed));
                if (w > maxWPer) {
                    maxWPer = w;
                }
                if (h > maxHPer) {
                    maxHPer = h;
                }
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
            var si = e.getValue();
            int w = si.getWidth();
            int h = si.getHeight();
            ByteBuffer pixels;
            if (si.isFailed()) {
                pixels = memAlloc(w * h);
                var ib = pixels.asIntBuffer();
                if (u0 + w > width) {
                    u0 = 0;
                    v0 += maxHPer;
                }
                int j = 0;
                for (int k = 0, s = h / 2; k < s; k++) {
                    for (int l = 0, t = w / 2; l < t; l++) {
                        ib.put(j++, 0xfff800f8);
                    }
                    for (int l = 0, t = w / 2; l < t; l++) {
                        ib.put(j++, 0xff000000);
                    }
                }
                for (int k = 0, s = h / 2; k < s; k++) {
                    for (int l = 0, t = w / 2; l < t; l++) {
                        ib.put(j++, 0xff000000);
                    }
                    for (int l = 0, t = w / 2; l < t; l++) {
                        ib.put(j++, 0xfff800f8);
                    }
                }
            } else {
                pixels = si.getData();
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
