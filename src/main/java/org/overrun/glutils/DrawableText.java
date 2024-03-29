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

import org.jetbrains.annotations.Nullable;
import org.overrun.commonutils.FloatArray;
import org.overrun.commonutils.IntArray;

import java.util.function.BiFunction;

/**
 * vertex dimensions are 3
 *
 * @author squid233
 * @since 1.1.0
 */
public class DrawableText {
    /**
     * white color
     */
    public static final float[] DEFAULT_COLOR = {
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f
    };
    /**
     * white color with alpha value
     */
    public static final float[] DEFAULT_COLOR_ALPHA = {
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f
    };

    /**
     * build mesh
     *
     * @param texture  font texture
     * @param text     text for rendering
     * @param bgColor  Background color function.
     * @param fgColor  Foreground color function.
     * @param consumer Consumer to set mesh.
     */
    public static void build(final FontTexture texture,
                             final String text,
                             final ColorFunction bgColor,
                             final ColorFunction fgColor,
                             final Consumer consumer) {
        build(texture, text, 3, bgColor, fgColor, consumer);
    }

    /**
     * build mesh
     *
     * @param font     font texture
     * @param text     text for rendering
     * @param colorDim Color dimensions.
     * @param bgColor  Background color function.
     * @param fgColor  Foreground color function.
     * @param consumer Consumer to set mesh.
     */
    public static void build(final FontTexture font,
                             final String text,
                             final int colorDim,
                             @Nullable final ColorFunction bgColor,
                             @Nullable final ColorFunction fgColor,
                             final Consumer consumer) {
        FloatArray vertices = new FloatArray();
        FloatArray colors = new FloatArray();
        FloatArray tex = new FloatArray();
        IntArray indices = new IntArray();
        FloatArray bcolors = new FloatArray();
        char[] ca = text.toCharArray();
        float startY = 0;
        int i = 0;
        int numChar = 0;
        int ftw = font.getWidth();
        int fth = font.getHeight();
        int gh = font.getGlyphHeight();
        // indices
        int i0 = 0;
        int i1 = 1;
        int i2 = 2;
        int i3 = 3;
        for (char c : ca) {
            if (c == '\r') {
                // TODO: 2021/8/31 test
                int next = i + 1;
                if (ca.length > next && ca[next] == '\n') {
                    i++;
                }
                startY += gh;
                numChar = 0;
                continue;
            } else if (c == '\n') {
                startY += gh;
                numChar = 0;
                continue;
            }
            FontTexture.Glyph glyph = font.getGlyph(c);
            int gw = glyph.getWidth();
            int gsx = glyph.getStartX();
            int gsy = glyph.getStartY();
            float startX = gw * numChar;
            float endX = startX + gw;
            float endY = startY + gh;
            float texStartX = (float) gsx / (float) ftw;
            float texStartY = (float) gsy / (float) fth;
            float texEndX = ((float) gsx + (float) gw) / (float) ftw;
            float texEndY = ((float) gsy + (float) gh) / (float) fth;
            if (bgColor != null) {
                bcolors.addAll(bgColor.apply(c, i));
            }

            // foreground
            // left top
            vertices.addAll(startX, startY, 0);
            tex.addAll(texStartX, texStartY);
            indices.add(i0);
            // left bottom
            vertices.addAll(startX, endY, 0);
            tex.addAll(texStartX, texEndY);
            indices.add(i1);
            // right bottom
            vertices.addAll(endX, endY, 0);
            tex.addAll(texEndX, texEndY);
            indices.add(i2);
            // right top
            vertices.addAll(endX, startY, 0);
            tex.addAll(texEndX, texStartY);
            if (fgColor != null) {
                colors.addAll(fgColor.apply(c, i));
            } else {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < colorDim; k++) {
                        colors.add(1);
                    }
                }
            }
            indices.add(i3);
            indices.add(i0);
            indices.add(i2);
            ++i;
            ++numChar;
            i0 += 4;
            i1 += 4;
            i2 += 4;
            i3 += 4;
        }
        float[] vtf = vertices.toFArray();
        int[] idi = indices.toIArray();
        consumer.accept(vtf,
            colors.toFArray(),
            tex.toFArray(),
            font.getTextureId(),
            idi,
            bcolors.toFArray());
    }

    /**
     * Consumer for setting mesh
     *
     * @author squid233
     */
    @FunctionalInterface
    public interface Consumer {
        /**
         * set mesh
         *
         * @param vertices vertices
         * @param colors   colors
         * @param texCoord texture coordinates
         * @param tex      texture id
         * @param indices  indices
         * @param bcolors  background colors
         * @since 1.2.0
         */
        void accept(float[] vertices,
                    float[] colors,
                    float[] texCoord,
                    int tex,
                    int[] indices,
                    float[] bcolors);
    }

    /**
     * Custom color per char
     *
     * @author squid233
     */
    @FunctionalInterface
    public interface ColorFunction
        extends BiFunction<Character, Integer, float[]> {
        /**
         * Add return value to color list
         *
         * @param c     current char
         * @param index char index
         * @return an array contains 4 vertices of color
         * (maybe {@code [1, 1, 1,
         * 1, 1, 1,
         * 1, 1, 1,
         * 1, 1, 1]}, {@link #DEFAULT_COLOR})
         * or {@link #DEFAULT_COLOR_ALPHA})
         */
        float[] apply(char c,
                      int index);

        @Override
        default float[] apply(Character c,
                              Integer index) {
            return apply(c.charValue(), index.intValue());
        }
    }
}
