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

package org.overrun.glutils.tex.stitching;

import org.lwjgl.system.MemoryStack;
import org.overrun.commonutils.MapSorter;
import org.overrun.glutils.tex.StbImg;
import org.overrun.glutils.tex.TexParam;

import java.util.Comparator;
import java.util.LinkedHashMap;

import static org.lwjgl.stb.STBImage.*;
import static org.overrun.glutils.FilesReader.getBytes;
import static org.overrun.glutils.FilesReader.ntoBBuffer;
import static org.overrun.glutils.GLUtils.getLogger;

/**
 * <h2>Stitching textures</h2>
 * <p>
 * You may need to merge many textures into one atlas to speed up loading, so we
 * provide a class to do this, it's {@code Stitcher}!
 * </p><p>
 * {@code Stitcher} will automatically delete duplicate filenames.
 * </p>
 *
 * @author squid233
 * @since 2.0.0
 */
@Deprecated
public class Stitcher {
    public static SpriteAtlas stitchStb(Class<?> c,
                                        TexParam param,
                                        String... filenames) {
        return stitchStb(c.getClassLoader(), param, filenames);
    }

    public static SpriteAtlas stitchAwt(Class<?> c,
                                        TexParam param,
                                        String... filenames) {
        return stitchAwt(c.getClassLoader(), param, filenames);
    }

    public static SpriteAtlas stitchStb(ClassLoader cl,
                                        TexParam param,
                                        String... filenames) {
        final int len = filenames.length;
        final var images = new LinkedHashMap<String, StbImg>(len);
        final var slots = new LinkedHashMap<String, SpriteAtlas.Slot>(len);
        try (var stack = MemoryStack.stackPush()) {
            var px = stack.mallocInt(1);
            var py = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            for (var s : filenames) {
                var bb = stbi_load_from_memory(ntoBBuffer(getBytes(cl, s)),
                    px,
                    py,
                    pc,
                    STBI_rgb_alpha);
                var f = bb == null;
                var cr = StbImg.CLEANER;
                int w, h;
                if (f) {
                    getLogger().error("Can't load image \"" +
                        s +
                        "\": " +
                        stbi_failure_reason());
                    w = 2;
                    h = 2;
                    bb = stack.malloc(4);
                    cr = null;
                } else {
                    w = px.get(0);
                    h = py.get(0);
                }
                images.put(s,
                    new StbImg(w,
                        h,
                        bb,
                        cr,
                        f)
                );
            }
            MapSorter.sortByValue(images, Comparator.comparing((StbImg t) -> -t.height())
                .thenComparing(t -> -t.width()));
        }
        throw new UnsupportedOperationException("TODO");
        /*int awidth, aheight, aid;
        int maxWPer = 2, maxHPer = 2;
        int minWPer = 2, minHPer = 2;
        final int len = filenames.length;
        final var images = new LinkedHashMap<String, StbImg>(len);
        final var slots = new LinkedHashMap<String, SpriteAtlas.Slot>(len);
        try (var stack = MemoryStack.stackPush()) {
            var px = stack.mallocInt(1);
            var py = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            for (var s : filenames) {
                var bb = stbi_load_from_memory(ntoBBuffer(getBytes(cl, s)),
                    px,
                    py,
                    pc,
                    STBI_rgb_alpha);
                var f = bb == null;
                var cr = StbImg.CLEANER;
                int w, h;
                if (f) {
                    getLogger().error("Can't load image \"" +
                        s +
                        "\": " +
                        stbi_failure_reason());
                    w = 2;
                    h = 2;
                    bb = stack.malloc(4);
                    cr = null;
                } else {
                    w = px.get(0);
                    h = py.get(0);
                }
                images.put(s,
                    new StbImg(w,
                        h,
                        bb,
                        cr,
                        f)
                );
                if (w > maxWPer) {
                    maxWPer = w;
                }
                if (h > maxHPer) {
                    maxHPer = h;
                }
                if (w < minWPer) {
                    minWPer = w;
                }
                if (h < minHPer) {
                    minHPer = h;
                }
            }
            int sz = (int) ceil(sqrt(len));
            awidth = aheight = max(sz * maxWPer, sz * maxHPer);
            aid = Textures.gen();
            Textures.bind2D(aid);
            Textures.texParameter(param);
            glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_RGBA,
                awidth,
                aheight,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                new int[awidth * aheight]);
            int x = 0, y = 0;
            for (var e : images.entrySet()) {
                var i = e.getValue();
                int w = i.getWidth();
                int h = i.getHeight();
                ByteBuffer pixels;
                if (i.isFailed()) {
                    pixels = memAlloc(w * h);
                    var ib = pixels.asIntBuffer();
                    if (x + w > awidth) {
                        x = 0;
                        y += maxHPer;
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
                    pixels = i.getData();
                    if (x + w > awidth) {
                        x = 0;
                        y += maxHPer;
                    }
                }
                glTexSubImage2D(GL_TEXTURE_2D,
                    0,
                    x,
                    y,
                    w,
                    h,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels);
                x += w;
                slots.put(e.getKey(), new SpriteAtlas.Slot(x,
                    y,
                    x + w,
                    y + h));
            }
        }
        Textures.genMipmap2D();
        for (var img : images.values()) {
            img.free();
        }
        return new SpriteAtlas(awidth, aheight, aid, slots);*/
    }

    public static SpriteAtlas stitchAwt(ClassLoader cl,
                                        TexParam param,
                                        String... filenames) {
        throw new UnsupportedOperationException("TODO");
    }

    public static SpriteAtlas stitchFsStb(TexParam param,
                                          String... filenames) {
        throw new UnsupportedOperationException("TODO");
    }

    public static SpriteAtlas stitchFsAwt(TexParam param,
                                          String... filenames) {
        throw new UnsupportedOperationException("TODO");
    }
}
