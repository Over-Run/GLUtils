/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

import org.jetbrains.annotations.Nullable;
import org.overrun.glutils.tex.NativeImage;
import org.overrun.glutils.tex.TexParam;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.overrun.glutils.FilesReader.*;
import static org.overrun.glutils.GLUtils.getLogger;
import static org.overrun.glutils.tex.Images.*;
import static org.overrun.glutils.tex.Textures.genMipmap;

/**
 * <h2>Stitching textures</h2>
 * <p>
 * You may need to merge many textures into one atlas to speed up loading, so we
 * provide a class to do this, it's {@code Stitcher}!
 * </p><p>
 * {@code Stitcher} will automatically remove duplicate filenames.
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

    private static class Slot extends Sprite {
        /**
         * The image info
         */
        public final NativeImage image;

        /**
         * Construct
         *
         * @param id    {@link #id}
         * @param block {@link #block}
         * @param image {@link #image}
         */
        public Slot(Object id,
                    Block block,
                    NativeImage image) {
            super(id, block);
            this.image = image;
        }
    }

    public static StrSpriteAtlas stitch(
        Object o,
        TexParam param,
        boolean useStb,
        String... filenames
    ) {
        return stitchBuf(getClassLoader(o),
            param,
            useStb,
            filenames);
    }

    public static StrSpriteAtlas stitch(
        Object o,
        TexParam param,
        String... filenames
    ) {
        return stitch(o, param, true, filenames);
    }

    public static StrSpriteAtlas stitchFs(
        TexParam param,
        boolean useStb,
        String... filenames
    ) {
        return stitchBuf(null, param, useStb, filenames);
    }

    public static StrSpriteAtlas stitchFs(
        TexParam param,
        String... filenames
    ) {
        return stitchFs(param, true, filenames);
    }

    private static StrSpriteAtlas stitchBuf(
        @Nullable Object o,
        TexParam param,
        boolean useStb,
        String... filenames
    ) {
        var slots = new Slot[filenames.length];
        if (useStb) {
            try (var stack = stackPush()) {
                var px = stack.mallocInt(1);
                var py = stack.mallocInt(1);
                var pc = stack.mallocInt(1);
                for (int i = 0; i < filenames.length; ++i) {
                    var filename = filenames[i];
                    ByteBuffer bb;
                    try {
                        if (o != null) {
                            var nb = ntoBBuffer(getBytes(o, filename));
                            bb = stbi_load_from_memory(
                                nb,
                                px,
                                py,
                                pc,
                                STBI_rgb_alpha
                            );
                            memFree(nb);
                        } else {
                            bb = stbi_load(
                                filename,
                                px,
                                py,
                                pc,
                                STBI_rgb_alpha
                            );
                        }
                    } catch (Exception e) {
                        bb = null;
                        thrOut(filename);
                        getLogger().catching(e);
                    }
                    int w, h;
                    if (bb == null) {
                        w = 2;
                        h = 2;
                        bb = memAlloc(4 * Integer.BYTES);
                        bb.asIntBuffer().put(MISSING_TEXTURE);
                    } else {
                        w = px.get(0);
                        h = py.get(0);
                    }
                    slots[i] = new Slot(filename,
                        new Block(w, h),
                        new NativeImage(w, h, bb));
                }
            }
        } else {
            for (int i = 0; i < filenames.length; ++i) {
                var filename = filenames[i];
                ByteBuffer bb;
                int w, h;
                int[] pixels;
                try {
                    BufferedImage bi;
                    if (o != null) {
                        bi = loadAwt(o, filename);
                    } else {
                        bi = loadFsAwt(filename);
                    }
                    w = bi.getWidth();
                    h = bi.getHeight();
                    pixels = getRGB(bi);
                } catch (Exception e) {
                    getLogger().error("Error loading image \"" +
                        filename +
                        "\"");
                    getLogger().catching(e);
                    w = 2;
                    h = 2;
                    pixels = MISSING_TEXTURE;
                }
                bb = memAlloc(w * h * Integer.BYTES);
                bb.asIntBuffer().put(pixels);
                slots[i] = new Slot(filename,
                    new Block(w, h),
                    new NativeImage(w, h, bb, false));
            }
        }

        Arrays.sort(slots, null);

        var blocks = new Block[slots.length];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = slots[i].block;
        }
        var packer = new GrowingPacker();
        packer.fit(blocks);
        int w = packer.root.w;
        int h = packer.root.h;

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        param.glSet(GL_TEXTURE_2D);
        glTexImage2D(GL_TEXTURE_2D,
            0,
            GL_RGBA,
            w,
            h,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            new int[w * h]);
        for (var s : slots) {
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
                    s.image.data);
            }
            s.image.free();
        }
        genMipmap(GL_TEXTURE_2D);

        var map = new LinkedHashMap<String, Sprite>();
        for (var sprite : slots) {
            map.put((String) sprite.id, sprite);
        }
        return new StrSpriteAtlas(w, h, id, map);
    }
}
