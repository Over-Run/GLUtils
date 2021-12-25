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

package org.overrun.glutils.game;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.overrun.glutils.tex.AWTImage;
import org.overrun.glutils.tex.TexParam;
import org.overrun.glutils.tex.Textures;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.overrun.glutils.game.GameEngine.app;

/**
 * Managed texture object
 *
 * @author squid233
 * @since 1.5.0
 */
public class Texture2D {
    private final int width;
    private final int height;
    private final int id;

    public Texture2D(final ClassLoader l,
                     final String filename,
                     final TexParam param) {
        try (var is = requireNonNull(
            l.getResourceAsStream(filename)
        ); var bis = new BufferedInputStream(is)) {
            if (app != null && app.config.useStb) {
                var bytes = new ArrayList<Byte>();
                int read;
                while ((read = bis.read()) != -1) {
                    bytes.add((byte) read);
                }
                var arr = new byte[bytes.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = bytes.get(i);
                }
                var bb = MemoryUtil.memAlloc(arr.length).put(arr);
                bb.flip();
                try (var stack = MemoryStack.stackPush()) {
                    var px = stack.mallocInt(1);
                    var py = stack.mallocInt(1);
                    var pc = stack.mallocInt(1);
                    var img = stbi_load_from_memory(bb, px, py, pc, STBI_rgb_alpha);
                    if (img == null) {
                        throw new IOException("Error loading image [" +
                            filename +
                            "] : " +
                            stbi_failure_reason());
                    }
                    width = px.get(0);
                    height = py.get(0);
                    id = Textures.gen();
                    Textures.bind2D(id);
                    Textures.pushToGL(param,
                        width,
                        height,
                        img);
                    stbi_image_free(img);
                }
            } else {
                var img = ImageIO.read(bis);
                width = img.getWidth();
                height = img.getHeight();
                id = Textures.gen();
                Textures.bind2D(id);
                Textures.pushToGL(param,
                    width,
                    height,
                    AWTImage.getRGB(img));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bind() {
        Textures.bind2D(id);
    }

    public void unbind() {
        Textures.unbind2D();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }

    public void free() {
        glDeleteTextures(id);
    }
}
