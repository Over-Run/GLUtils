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

import org.jetbrains.annotations.Range;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Textures {
    private static final Map<String, Integer> ID_MAP = new HashMap<>();
    private static int maxSize;

    /**
     * Bind texture for 2D
     *
     * @param id Texture ID.
     * @since 1.5.0
     */
    public static void bind2D(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Unbind texture for 2D
     *
     * @since 1.5.0
     */
    public static void unbind2D() {
        bind2D(0);
    }

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
     * Load texture from stream by AWT.
     *
     * @param loader ClassLoader of loader class.
     * @param name   The filename.
     * @param param  The texture parameters.
     * @return The texture id.
     * @throws RuntimeException When file not found.
     * @since 2.0.0
     */
    public static int loadAWT(ClassLoader loader,
                              String name,
                              TexParam param)
        throws RuntimeException {
        if (ID_MAP.containsKey(name)) {
            return ID_MAP.get(name);
        }
        var img = Images.loadAwt(loader, name);
        int id = gen();
        bind2D(id);
        pushToGL(param,
            img.getWidth(),
            img.getHeight(),
            Images.getRGB(img));
        ID_MAP.put(name, id);
        return id;
    }

    /**
     * Load texture from stream by AWT.
     *
     * @param c     Loader class.
     * @param name  The filename.
     * @param param The texture parameters.
     * @return The texture id.
     * @throws RuntimeException When file not found.
     * @since 2.0.0
     */
    public static int loadAWT(Class<?> c,
                              String name,
                              TexParam param)
        throws RuntimeException {
        return loadAWT(c.getClassLoader(), name, param);
    }

    /**
     * Load texture from file system.
     *
     * @param name  The filename.
     * @param param The texture parameters.
     * @return The texture id.
     * @since 2.0.0
     */
    public static int loadFS(String name,
                             TexParam param) {
        if (ID_MAP.containsKey(name)) {
            return ID_MAP.get(name);
        }
        int w, h;
        ByteBuffer data;
        try (var stack = MemoryStack.stackPush()) {
            var pw = stack.mallocInt(1);
            var ph = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            data = stbi_load(name, pw, ph, pc, STBI_rgb_alpha);
            if (data == null) {
                throw new RuntimeException("Error loading image [" +
                    name +
                    "] from file system: " +
                    stbi_failure_reason());
            }
            w = pw.get(0);
            h = ph.get(0);
        }
        int id = gen();
        bind2D(id);
        pushToGL(param, w, h, data);
        stbi_image_free(data);
        ID_MAP.put(name, id);
        return id;
    }

    /**
     * Load texture by buffer.
     *
     * @param identifier The identifier of texture.
     * @param buffer     The ByteBuffer that contains pixel data.
     * @param param      The texture parameters.
     * @return The texture id.
     * @since 2.0.0
     */
    public static int load(String identifier,
                           ByteBuffer buffer,
                           TexParam param) {
        if (ID_MAP.containsKey(identifier)) {
            return ID_MAP.get(identifier);
        }
        int w, h;
        ByteBuffer data;
        try (var stack = MemoryStack.stackPush()) {
            var pw = stack.mallocInt(1);
            var ph = stack.mallocInt(1);
            var pc = stack.mallocInt(1);
            data = stbi_load_from_memory(buffer, pw, ph, pc, STBI_rgb_alpha);
            if (data == null) {
                Images.thrRE(identifier);
            }
            w = pw.get(0);
            h = ph.get(0);
        }
        int id = gen();
        bind2D(id);
        pushToGL(param, w, h, data);
        stbi_image_free(data);
        ID_MAP.put(identifier, id);
        return id;
    }

    /**
     * Load texture by array.
     *
     * @param identifier The identifier of texture.
     * @param w          Texture width
     * @param h          Texture height
     * @param data       The array that contains pixel data.
     * @param param      The texture parameters.
     * @return The texture id.
     * @since 2.0.0
     */
    public static int load(String identifier,
                           int w,
                           int h,
                           int[] data,
                           TexParam param) {
        if (ID_MAP.containsKey(identifier)) {
            return ID_MAP.get(identifier);
        }
        int id = gen();
        bind2D(id);
        pushToGL(param, w, h, data);
        ID_MAP.put(identifier, id);
        return id;
    }

    /**
     * @param param The texture parameters.
     * @since 2.0.0
     */
    public static void texParameter(TexParam param) {
        if (param != null) {
            param.glMinFilter(GL_TEXTURE_2D);
            param.glMagFilter(GL_TEXTURE_2D);
        }
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
    public static void pushToGL(TexParam param,
                                int w,
                                int h,
                                ByteBuffer data) {
        texParameter(param);
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
        genMipmap2D();
    }

    /**
     * Generate a texture by OpenGL.
     *
     * @return The texture id.
     */
    public static int gen() {
        return glGenTextures();
    }

    /**
     * Generate mipmap 2D.
     * <p>
     * This method has no effect when GPU doesn't support to OpenGL 3.0.
     * </p>
     *
     * @since 1.5.0
     */
    public static void genMipmap2D() {
        if (GL.getCapabilities().glGenerateMipmap != NULL) {
            glGenerateMipmap(GL_TEXTURE_2D);
        }
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
    public static void pushToGL(TexParam param,
                                int w,
                                int h,
                                int[] data) {
        texParameter(param);
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
        genMipmap2D();
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

    /**
     * Cleanup all resources.
     *
     * @since 1.5.0
     */
    public static void free() {
        for (int id : ID_MAP.values()) {
            glDeleteTextures(id);
        }
        ID_MAP.clear();
    }
}
