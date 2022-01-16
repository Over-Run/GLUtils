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

package org.overrun.glutils.tex;

import org.overrun.glutils.SizedObject;
import org.overrun.glutils.gl.GLState;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;

/**
 * @author squid233
 * @since 2.0.0
 */
public class Texture implements SizedObject, GLState {
    /**
     * The target
     */
    protected final int target;
    /**
     * The size width
     */
    protected final int width;
    /**
     * The size height
     */
    protected final int height;
    /**
     * The texture id
     */
    protected final int id;

    /**
     * Construct
     *
     * @param target The target
     * @param width  The size width
     * @param height The size height
     * @param id     The texture id
     */
    public Texture(int target,
                   int width,
                   int height,
                   int id) {
        this.target = target;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    @Override
    public void bind() {
        glBindTexture(target, id);
    }

    @Override
    public void unbind() {
        glBindTexture(target, 0);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Get texture id
     *
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * Delete texture
     */
    public void free() {
        glDeleteTextures(id);
    }
}
