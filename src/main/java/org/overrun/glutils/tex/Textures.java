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

package org.overrun.glutils.tex;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;
import static org.overrun.glutils.FilesReader.*;
import static org.overrun.glutils.gl.ll.GLU.gluBuild2DMipmaps;
import static org.overrun.glutils.tex.Images.loadAwt;
import static org.overrun.glutils.tex.Images.loadFsAwt;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Textures {
    private static int maxSize;

    /**
     * Active texture
     *
     * @param unit Texture unit.
     * @since 1.5.0
     */
    public static void active(@Range(from = 0, to = 31) int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
    }

    /**
     * Load texture from jar.
     *
     * @param o      The object Class or ClassLoader.
     * @param name   The file name.
     * @param param  The texture parameters.
     * @param useStb Use STB to load
     * @return The texture object
     */
    public static Texture2D load2D(
        Object o,
        String name,
        TexParam param,
        boolean useStb
    ) {
        return load2DBuf(getClassLoader(o),
            name,
            param,
            useStb);
    }

    public static Texture2D loadFs2D(
        String name,
        TexParam param,
        boolean useStb
    ) {
        return load2DBuf(null, name, param, useStb);
    }

    private static Texture2D load2DBuf(
        @Nullable Object o,
        String name,
        TexParam param,
        boolean useStb
    ) {
        int w, h, id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        if (useStb) {
            try (var stack = stackPush()) {
                var px = stack.mallocInt(1);
                var py = stack.mallocInt(1);
                var pc = stack.mallocInt(1);
                ByteBuffer img;
                if (o != null) {
                    var bb = ntoBBuffer(getBytes(o, name));
                    img = stbi_load_from_memory(
                        bb,
                        px,
                        py,
                        pc,
                        STBI_rgb_alpha
                    );
                    memFree(bb);
                } else {
                    img = stbi_load(
                        name,
                        px,
                        py,
                        pc,
                        STBI_rgb_alpha
                    );
                }
                if (img == null) {
                    Images.thrRE(name);
                }
                w = px.get(0);
                h = py.get(0);
                pushToGL2D(param,
                    w,
                    h,
                    img);
                stbi_image_free(img);
            }
        } else {
            var img = o != null
                ? loadAwt(o, name)
                : loadFsAwt(name);
            var arr = Images.getRGB(img);
            var bb = memAlloc(arr.length);
            bb.asIntBuffer().put(arr).flip();
            w = img.getWidth();
            h = img.getHeight();
            pushToGL2D(param,
                w,
                h,
                bb);
            memFree(bb);
        }
        return new Texture2D(w, h, id);
    }

    /**
     * push image data to OpenGL state manager
     *
     * @param param The texture parameters.
     * @param w     texture width
     * @param h     texture height
     * @param data  pixel data
     * @since 2.0.0
     */
    public static void pushToGL2D(TexParam param,
                                  int w,
                                  int h,
                                  ByteBuffer data) {
        param.glSet(GL_TEXTURE_2D);
        if (hasGenMipmap()) {
            glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_RGBA,
                w,
                h,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            );
            glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            gluBuild2DMipmaps(GL_TEXTURE_2D,
                GL_RGBA,
                w,
                h,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            );
        }
    }

    /**
     * Check if {@link GLCapabilities#glGenerateMipmap} is not null.
     *
     * @return true or false
     * @since 2.0.0
     */
    public static boolean hasGenMipmap() {
        return GL.getCapabilities().glGenerateMipmap != NULL;
    }

    /**
     * Generate mipmap.
     * <p>
     * This method has no effect when GPU doesn't support to OpenGL 3.0.
     * </p>
     *
     * @param target The target
     * @since 2.0.0
     */
    public static void genMipmap(int target) {
        if (hasGenMipmap()) {
            glGenerateMipmap(target);
        }
    }

    /**
     * get max texture size
     *
     * @return max texture size
     * @since 1.1.0
     */
    public static int getMaxSize() {
        if (maxSize == 0) {
            maxSize = glGetInteger(GL_MAX_TEXTURE_SIZE);
        }
        return maxSize;
    }
}
