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

package org.overrun.glutils;

import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public class Textures {
    private static final Map<String, Integer> ID_MAP = new HashMap<>();
    private static int maxSize;

    /**
     * Load texture from stream by AWT.
     *
     * @param loader ClassLoader of loader class.
     * @param name   The filename.
     * @param mode   Processor mode.
     * @return The texture id.
     * @throws Exception When file not found.
     */
    public static int loadAWT(ClassLoader loader,
                              String name,
                              int mode)
        throws Exception {
        if (ID_MAP.containsKey(name)) {
            return ID_MAP.get(name);
        }
        BufferedImage img;
        int w, h;
        try (InputStream is = loader.getResourceAsStream(name)) {
            img = ImageIO.read(Objects.requireNonNull(is));
            w = img.getWidth();
            h = img.getHeight();
        }
        int id = glGenTextures();
        pushToGL(id, mode, w, h, AWTImage.getRGB(img));
        ID_MAP.put(name, id);
        return id;
    }

    /**
     * Load texture from file system.
     *
     * @param name The filename.
     * @param mode Processor mode.
     * @return The texture id.
     */
    public static int loadFS(String name,
                             int mode) {
        if (ID_MAP.containsKey(name)) {
            return ID_MAP.get(name);
        }
        int w, h;
        ByteBuffer data;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pw = stack.mallocInt(1);
            IntBuffer ph = stack.mallocInt(1);
            IntBuffer pc = stack.mallocInt(1);
            data = stbi_load(name, pw, ph, pc, STBI_rgb_alpha);
            if (data == null) {
                GLUtils.getErrorCb().error("Error loading image \"" +
                    name +
                    "\" from file system: " +
                    stbi_failure_reason());
                return 0;
            }
            w = pw.get(0);
            h = ph.get(0);
        }
        int id = glGenTextures();
        pushToGL(id, mode, w, h, data);
        stbi_image_free(data);
        ID_MAP.put(name, id);
        return id;
    }

    /**
     * Load texture by buffer.
     *
     * @param identifier The identifier of texture.
     * @param buffer     The ByteBuffer that contains pixel data.
     * @param mode       Processor mode.
     * @return The texture id.
     * @since 0.2.0
     */
    public static int load(String identifier,
                           ByteBuffer buffer,
                           int mode) {
        if (ID_MAP.containsKey(identifier)) {
            return ID_MAP.get(identifier);
        }
        int w, h;
        ByteBuffer data;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pw = stack.mallocInt(1);
            IntBuffer ph = stack.mallocInt(1);
            IntBuffer pc = stack.mallocInt(1);
            data = stbi_load_from_memory(buffer, pw, ph, pc, STBI_rgb_alpha);
            if (data == null) {
                GLUtils.getErrorCb().error("Error loading image \"" +
                    identifier +
                    "\": " +
                    stbi_failure_reason());
                return 0;
            }
            w = pw.get(0);
            h = ph.get(0);
        }
        int id = glGenTextures();
        pushToGL(id, mode, w, h, data);
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
     * @param mode       Processor mode.
     * @return The texture id.
     * @since 0.3.0
     */
    public static int load(String identifier,
                           int w,
                           int h,
                           int[] data,
                           int mode) {
        if (ID_MAP.containsKey(identifier)) {
            return ID_MAP.get(identifier);
        }
        int id = glGenTextures();
        pushToGL(id, mode, w, h, data);
        ID_MAP.put(identifier, id);
        return id;
    }

    /**
     * @param id   identifier
     * @param mode mode
     * @since 0.3.0
     */
    private static void processTexture(int id,
                                       int mode) {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode);
    }


    /**
     * push image data to OpenGL state manager
     *
     * @param id   texture id
     * @param mode mipmap mode
     * @param w    texture width
     * @param h    texture height
     * @param data pixel data
     */
    public static void pushToGL(int id,
                                int mode,
                                int w,
                                int h,
                                ByteBuffer data) {
        processTexture(id, mode);
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
    }

    /**
     * push image data to OpenGL state manager
     *
     * @param id   texture id
     * @param mode mipmap mode
     * @param w    texture width
     * @param h    texture height
     * @param data pixel data
     * @since 0.3.0
     */
    public static void pushToGL(int id,
                                int mode,
                                int w,
                                int h,
                                int[] data) {
        processTexture(id, mode);
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

    /**
     * Cleanup all resources.
     *
     * @since 0.2.0
     */
    @Deprecated
    public static void close() {
        free();
    }
}
