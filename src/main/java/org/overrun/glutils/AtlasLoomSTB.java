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

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;

public class AtlasLoomSTB extends AtlasLoom<StbImg> {
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
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pw = stack.mallocInt(1);
            IntBuffer ph = stack.mallocInt(1);
            IntBuffer pc = stack.mallocInt(1);
            for (String img : imageMap.keySet()) {
                ByteBuffer bb = stbi_load(img, pw, ph, pc, STBI_rgb_alpha);
                pc.flip();
                boolean failed = false;
                if (bb == null) {
                    failed = true;
                    GLUtils.getErrorCb().error("Can't load image \"" +
                            img +
                            "\": " +
                            stbi_failure_reason());
                    bb = MemoryUtil.memAlloc(defaultW * defaultH * 4);
                }
                int w;
                int h;
                StbImg stbImg;
                if (failed) {
                    stbImg = new StbImg(w = defaultW,
                            h = defaultH,
                            bb,
                            MemoryUtil::memFree);
                } else {
                    stbImg = new StbImg(w = pw.flip().get(),
                            h = ph.flip().get(),
                            bb,
                            StbImg.RECYCLER);
                }
                imageMap.put(img, stbImg);
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
            for (Map.Entry<String, StbImg> e : imageMap.entrySet()) {
                StbImg img = e.getValue();
                int w;
                if (img != null) {
                    w = img.getWidth();
                    int h = img.getHeight();
                    glTexSubImage2D(GL_TEXTURE_2D,
                            0,
                            i,
                            0,
                            w,
                            h,
                            GL_BGRA,
                            GL_UNSIGNED_BYTE,
                            img.getData());
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
                img.close();
            }
        }
    }
}
