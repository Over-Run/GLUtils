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

package org.overrun.glutils.tex.stitch;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.overrun.glutils.tex.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.overrun.glutils.FilesReader.getBytes;
import static org.overrun.glutils.FilesReader.ntoBBuffer;
import static org.overrun.glutils.tex.Images.getRGB;

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
    private static final int[] MISSING_TEXTURE = {
        0xfff800f8, 0xff000000,
        0xff000000, 0xfff800f8
    };

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
        return stitchAwt0(filename -> Images.loadAwt(cl, filename),
            param, filenames);
    }

    public static SpriteAtlas stitchFsStb(TexParam param,
                                          String... filenames) {
        return stitchStb0(STBImage::stbi_load, param, filenames);
    }

    public static SpriteAtlas stitchFsAwt(TexParam param,
                                          String... filenames) {
        return stitchAwt0(Images::loadFsAwt, param, filenames);
    }

    private static SpriteAtlas stitchAwt0(AwtiLoadFunc func,
                                          TexParam param,
                                          String... filenames) {
        var sprites = new Sprite[filenames.length];
        for (int i = 0; i < filenames.length; ++i) {
            var filename = filenames[i];
            ByteBuffer bb;
            int w, h;
            int[] pixels;
            try {
                var bi = func.load(filename);
                w = bi.getWidth();
                h = bi.getHeight();
                pixels = getRGB(bi);
            } catch (Exception e) {
                e.printStackTrace();
                w = 2;
                h = 2;
                pixels = MISSING_TEXTURE;
            }
            bb = memAlloc(w * h * Integer.BYTES);
            bb.asIntBuffer().put(pixels);
            sprites[i] = new Sprite(filename,
                new Block(w, h),
                new NativeImage(w, h, bb, false));
        }
        return stitchBuf(sprites, param);
    }

    private static SpriteAtlas stitchStb0(StbiLoadFunc func,
                                          TexParam param,
                                          String... filenames) {
        var sprites = new Sprite[filenames.length];
        try (var stack = MemoryStack.stackPush()) {
            var px = stack.mallocInt(1);
            var py = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            for (int i = 0; i < filenames.length; ++i) {
                var filename = filenames[i];
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
                    Images.thrOut(filename);
                    e.printStackTrace();
                }
                var failed = bb == null;
                int w, h;
                if (failed) {
                    w = 2;
                    h = 2;
                    bb = memAlloc(4 * Integer.BYTES);
                    bb.asIntBuffer().put(MISSING_TEXTURE);
                } else {
                    w = px.get(0);
                    h = py.get(0);
                }
                sprites[i] = new Sprite(filename,
                    new Block(w, h),
                    new NativeImage(w, h, bb));
            }
        }
        return stitchBuf(sprites, param);
    }

    private static SpriteAtlas stitchBuf(Sprite[] sprites,
                                         TexParam param) {
        Arrays.sort(sprites,
            Comparator.comparing((Sprite s) -> -s.buffer.height)
                .thenComparing(s -> -s.buffer.width));
        int aw, ah, aid;
        var blocks = new Block[sprites.length];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = sprites[i].block;
        }
        var packer = new GrowingPacker();
        packer.fit(blocks);
        aw = packer.root.w;
        ah = packer.root.h;
        aid = Textures.gen();
        stitchTex(aw, ah, aid, param, sprites);
        var map = new LinkedHashMap<String, Sprite>();
        for (var sprite : sprites) {
            map.put(sprite.id, sprite);
        }
        return new SpriteAtlas(aw, ah, aid, map);
    }

    private static void stitchTex(int aw,
                                  int ah,
                                  int aid,
                                  TexParam param,
                                  Sprite[] sprites) {
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
        for (var s : sprites) {
            var b = s.block;
            if (b.fit != null) {
                glTexSubImage2D(GL_TEXTURE_2D,
                    0,
                    b.fit.x,
                    b.fit.y,
                    b.w,
                    b.h,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    s.buffer.data);
            }
            s.free();
        }
        Textures.genMipmap2D();
    }
}
