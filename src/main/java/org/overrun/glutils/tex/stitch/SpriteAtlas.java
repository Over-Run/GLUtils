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

import org.overrun.glutils.SizedObject;
import org.overrun.glutils.tex.Textures;

import java.util.Map;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

/**
 * @author squid233
 * @since 2.0.0
 */
public class SpriteAtlas<T> implements SizedObject {
    private final int width;
    private final int height;
    private final int id;
    protected Map<T, Sprite> sprites;

    public SpriteAtlas(int width,
                       int height,
                       int id,
                       Map<T, Sprite> sprites) {
        this(width, height, id);
        this.sprites = sprites;
    }

    public SpriteAtlas(int width,
                       int height,
                       int id) {
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public void bind() {
        Textures.bind2D(id);
    }

    public void unbind() {
        Textures.unbind2D();
    }

    public float getU0(T id) {
        return getSprite(id).block.fit.x / (float) width;
    }

    public float getV0(T id) {
        return getSprite(id).block.fit.y / (float) height;
    }

    public float getU1(T id) {
        var s = getSprite(id);
        return (s.block.fit.x + s.block.w) / (float) width;
    }

    public float getV1(T id) {
        var s = getSprite(id);
        return (s.block.fit.y + s.block.h) / (float) height;
    }

    public int getId() {
        return id;
    }

    public Sprite getSprite(T id) {
        return sprites.get(id);
    }

    public Map<T, Sprite> getSprites() {
        return sprites;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void free() {
        glDeleteTextures(id);
    }
}
