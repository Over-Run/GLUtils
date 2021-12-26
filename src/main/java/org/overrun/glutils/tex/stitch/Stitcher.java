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

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.overrun.commonutils.MapSorter;
import org.overrun.glutils.tex.*;
import org.overrun.glutils.tex.stitch.SpriteAtlas.Slot;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAlloc;
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
        return stitchStb0((filename,
                           px,
                           py,
                           pc,
                           format) -> stbi_load_from_memory(
            ntoBBuffer(getBytes(cl, filename)),
            px,
            py,
            pc,
            format
        ), param, filenames);
    }

    public static SpriteAtlas stitchAwt(ClassLoader cl,
                                        TexParam param,
                                        String... filenames) {
        return stitchAwt0(filename -> AWTImage.load(cl, filename),
            param, filenames);
    }

    public static SpriteAtlas stitchFsStb(TexParam param,
                                          String... filenames) {
        return stitchStb0(STBImage::stbi_load, param, filenames);
    }

    public static SpriteAtlas stitchFsAwt(TexParam param,
                                          String... filenames) {
        return stitchAwt0(AWTImage::loadFs, param, filenames);
    }

    private static SpriteAtlas stitchAwt0(AwtiLoadFunc func,
                                          TexParam param,
                                          String... filenames) {
        var slots = new LinkedHashMap<String, Slot>();
        int aw, ah, aid;
        var images = new LinkedHashMap<String, AWTImage>();
        for (var filename : filenames) {
            BufferedImage bi;
            try {
                bi = func.load(filename);
            } catch (Exception e) {
                bi = null;
            }
            var failed = bi == null;
            if (failed) {
                bi = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
                bi.setRGB(0, 0, 2, 2, new int[]{
                    0xfff800f8, 0xff000000,
                    0xff000000, 0xfff800f8
                }, 0, 2);
            }
            images.put(filename, new AWTImage(failed, bi));
        }
        MapSorter.sortByValue(images,
            Comparator.comparing((AWTImage i) -> -i.getHeight())
                .thenComparing(i -> -i.getWidth())
        );
        var blocks = new Block[images.size()];
        int i = 0;
        for (var img : images.values()) {
            blocks[i] = new Block(img.width(), img.height());
            ++i;
        }
        var packer = new GrowingPacker();
        packer.fit(blocks);
        i = 0;
        for (var e : images.entrySet()) {
            var block = blocks[i];
            if (block.fit != null) {
                var id = e.getKey();
                var img = e.getValue();
                slots.put(id, new Slot(
                    memAlloc(img.width() * img.height() * Integer.BYTES),
                    MemoryUtil::memFree,
                    block,
                    id));
            }
            ++i;
        }
        aw = packer.root.w;
        ah = packer.root.h;
        aid = Textures.gen();
        Textures.bind2D(aid);
        Textures.texParameter(param);
        glTexImage2D(GL_TEXTURE_2D,
            0,
            GL_RGBA,
            aw,
            ah,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            new int[aw * ah]);
        for (var s : slots.values()) {
            if (s.fit != null) {
                glTexSubImage2D(GL_TEXTURE_2D,
                    0,
                    s.fit.x,
                    s.fit.y,
                    s.w,
                    s.h,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    s.data);
            }
            s.freeData();
        }
        Textures.genMipmap2D();
        return new SpriteAtlas(aw, ah, aid, slots);
    }

    private static SpriteAtlas stitchStb0(StbiLoadFunc func,
                                          TexParam param,
                                          String... filenames) {
        var slots = new LinkedHashMap<String, Slot>();
        int aw, ah, aid;
        try (var stack = MemoryStack.stackPush()) {
            var px = stack.mallocInt(1);
            var py = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            var images = new LinkedHashMap<String, StbImg>();
            for (var filename : filenames) {
                ByteBuffer bb;
                try {
                    bb = func.load(
                        filename,
                        px,
                        py,
                        pc,
                        STBI_rgb_alpha
                    );
                } catch (Exception e) {
                    bb = null;
                    StbImg.thrOut(filename);
                }
                var failed = bb == null;
                var cleaner = StbImg.CLEANER;
                int w, h;
                if (failed) {
                    cleaner = null;
                    w = 2;
                    h = 2;
                    bb = stack.malloc(4 * Integer.BYTES);
                    bb.asIntBuffer()
                        .put(0xfff800f8)
                        .put(0xff000000)
                        .put(0xff000000)
                        .put(0xfff800f8)
                        .flip();
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
            var blocks = new Block[images.size()];
            int i = 0;
            for (var img : images.values()) {
                blocks[i] = new Block(img.width(), img.height());
                ++i;
            }
            var packer = new GrowingPacker();
            packer.fit(blocks);
            i = 0;
            for (var e : images.entrySet()) {
                var block = blocks[i];
                if (block.fit != null) {
                    var id = e.getKey();
                    var img = e.getValue();
                    slots.put(id, new Slot(img.getData(),
                        img.getCleaner(),
                        block,
                        id));
                }
                ++i;
            }
            aw = packer.root.w;
            ah = packer.root.h;
            aid = Textures.gen();
            Textures.bind2D(aid);
            Textures.texParameter(param);
            glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_RGBA,
                aw,
                ah,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                new int[aw * ah]);
            for (var s : slots.values()) {
                if (s.fit != null) {
                    glTexSubImage2D(GL_TEXTURE_2D,
                        0,
                        s.fit.x,
                        s.fit.y,
                        s.w,
                        s.h,
                        GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        s.data);
                }
                s.freeData();
            }
            Textures.genMipmap2D();
        }
        return new SpriteAtlas(aw, ah, aid, slots);
    }
}
