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

package org.overrun.glutils.tex.stitch;

import org.lwjgl.system.MemoryStack;
import org.overrun.commonutils.MapSorter;
import org.overrun.glutils.tex.StbImg;
import org.overrun.glutils.tex.TexParam;

import java.util.Comparator;
import java.util.LinkedHashMap;

import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.overrun.glutils.FilesReader.getBytes;
import static org.overrun.glutils.FilesReader.ntoBBuffer;

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
        var images = new LinkedHashMap<String, StbImg>();
        try (var stack = MemoryStack.stackPush()) {
            var px = stack.mallocInt(1);
            var py = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            for (var filename : filenames) {
                var bb = stbi_load_from_memory(
                    ntoBBuffer(getBytes(cl, filename)),
                    px,
                    py,
                    pc,
                    STBI_rgb_alpha
                );
                var failed = bb == null;
                var cleaner = StbImg.CLEANER;
                int w, h;
                if (failed) {
                    cleaner = null;
                    w = 2;
                    h = 2;
                } else {
                    w = px.get(0);
                    h = py.get(0);
                }
                images.put(filename,
                    new StbImg(w,
                        h,
                        bb,
                        cleaner,
                        failed));
            }
            MapSorter.sortByValue(images,
                Comparator.comparing((StbImg i) -> -i.height())
                    .thenComparing(i -> -i.width())
            );
        }
        throw new UnsupportedOperationException("TODO");
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
