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

import org.overrun.glutils.mesh.BaseMesh;

/**
 * vertex dimensions are 3
 * <p>
 * fixme black
 * </p>
 * @author squid233
 * @since 1.1.0
 */
public class DrawableText<T extends BaseMesh<T>> implements Drawable {
    private T mesh;

    /**
     * build mesh
     *
     * @param fontTexture font texture
     * @param text text for rendering
     * @param mesh Mesh object.
     * @param consumer Consumer to set mesh.
     */
    public void build(FontTexture fontTexture,
                      String text,
                      T mesh,
                      Consumer consumer) {
        this.mesh = mesh;
        FloatArray vertices = new FloatArray();
        FloatArray colors = new FloatArray();
        FloatArray tex = new FloatArray();
        IntArray indices = new IntArray();
        char[] ca = text.toCharArray();
        float startY = 0;
        int i = 0;
        int numChar = 0;
        for (char c : ca) {
            int ftw = fontTexture.getWidth();
            int fth = fontTexture.getHeight();
            if (c == '\n') {
                startY += fth;
                numChar = 0;
            }
            FontTexture.Glyph glyph = fontTexture.getGlyph(c);
            int gw = glyph.getWidth();
            int gsx = glyph.getStartX();
            float startX = gw * numChar;
            float endX = startX + gw;
            float endY = startY + fontTexture.getGlyphHeight();
            float texStartX = (float) gsx / (float) ftw;
            float texStartY = 0;
            float texEndX = ((float) gsx + (float) gw) / (float) ftw;
            float texEndY = 1;
            // indices
            int i0 = i * 4;
            int i1 = 1 + i * 4;
            int i2 = 2 + i * 4;
            int i3 = 3 + i * 4;
            // left top
            vertices.addAll(startX, startY, 0);
            colors.addAll(1, 1, 1);
            tex.addAll(texStartX, texStartY);
            indices.add(i0);
            // left bottom
            vertices.addAll(startX, endY, 0);
            colors.addAll(1, 1, 1);
            tex.addAll(texStartX, texEndY);
            indices.add(i1);
            // right bottom
            vertices.addAll(endX, endY, 0);
            colors.addAll(1, 1, 1);
            tex.addAll(texEndX, texEndY);
            indices.add(i2);
            // right top
            vertices.addAll(endX, startY, 0);
            colors.addAll(1, 1, 1);
            tex.addAll(texEndX, texStartY);
            indices.add(i3);
            indices.add(i0);
            indices.add(i2);
            ++i;
            ++numChar;
        }
        consumer.accept(vertices.toFArray(),
                colors.toFArray(),
                tex.toFArray(),
                fontTexture.getTextureId(),
                indices.toIArray());
    }

    @FunctionalInterface
    public interface Consumer {
        void accept(float[] vertices,
                    float[] colors,
                    float[] texCoord,
                    int tex,
                    int[] indices);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use {@link org.joml.Matrix4f#translate Matrix#translate}
     * to set position for rendering
     */
    @Override
    public void render() {
        mesh.render();
    }

    /**
     * get mesh
     *
     * @return mesh
     */
    public T getMesh() {
        return mesh;
    }
}
