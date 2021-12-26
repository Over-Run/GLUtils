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

import org.overrun.glutils.SizedObject;
import org.overrun.glutils.tex.Textures;

import java.util.Map;

/**
 * @author squid233
 * @since 2.0.0
 */
public class SpriteAtlas implements SizedObject {
    private final int width;
    private final int height;
    private final int id;
    private final Map<String, Block> blocks;

    public SpriteAtlas(int width,
                       int height,
                       int id,
                       Map<String, Block> blocks) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.blocks = blocks;
    }

    public void bind() {
        Textures.bind2D(id);
    }

    public void unbind() {
        Textures.unbind2D();
    }

    public float getU0(String id) {
        return getSlot(id).fit.x / (float) width;
    }

    public float getV0(String id) {
        return getSlot(id).fit.y / (float) height;
    }

    public float getU1(String id) {
        var s = getSlot(id);
        return (s.fit.x + s.w) / (float) width;
    }

    public float getV1(String id) {
        var s = getSlot(id);
        return (s.fit.y + s.h) / (float) height;
    }

    public int getId() {
        return id;
    }

    public Block getSlot(String id) {
        return blocks.get(id);
    }

    public Map<String, Block> getBlocks() {
        return blocks;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
