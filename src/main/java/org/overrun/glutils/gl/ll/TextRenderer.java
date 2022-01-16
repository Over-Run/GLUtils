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

package org.overrun.glutils.gl.ll;

import org.jetbrains.annotations.Nullable;
import org.overrun.glutils.gui.DrawableText.ColorFunction;
import org.overrun.glutils.gui.FontTexture;

import java.util.Scanner;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.gui.DrawableText.DEFAULT_COLOR;

/**
 * Text renderer for legacy GL.
 * <p>
 * <b>Note:</b> Re-render per lines
 *
 * @author squid233
 * @since 1.4.0
 */
public class TextRenderer {
    public static void drawText(String text,
                                FontTexture font,
                                @Nullable ColorFunction fgColor,
                                boolean bottomToTop) {
        int currLn = 0;
        int ftw = font.getWidth();
        int fth = font.getHeight();
        int gh = font.getGlyphHeight();

        font.getTexture().bind();
        try (var sc = new Scanner(text)) {
            while (sc.hasNextLine()) {
                var t = sc.nextLine();
                var ca = t.toCharArray();
                float startX = 0;
                int i = 0;
                glBegin(GL_QUADS);

                for (char c : ca) {
                    FontTexture.Glyph glyph = font.getGlyph(c);
                    int gw = glyph.getWidth();
                    int gsx = glyph.getStartX();
                    int gsy = glyph.getStartY();
                    float endX = startX + gw;
                    float startY = (currLn - (bottomToTop ? 1 : 0)) * gh;
                    float endY = bottomToTop ? startY - gh : startY + gh;
                    float texStartX = (float) gsx / (float) ftw;
                    float texStartY = (float) gsy / (float) fth;
                    float texEndX = ((float) gsx + (float) gw) / (float) ftw;
                    float texEndY = ((float) gsy + (float) gh) / (float) fth;
                    var colors = DEFAULT_COLOR;
                    if (fgColor != null) {
                        colors = fgColor.apply(c, i);
                    }
                    glColor3f(colors[0], colors[1], colors[2]);
                    glTexCoord2f(texStartX, texStartY);
                    glVertex3f(startX, startY, 0);

                    glColor3f(colors[3], colors[4], colors[5]);
                    glTexCoord2f(texStartX, texEndY);
                    glVertex3f(startX, endY, 0);

                    glColor3f(colors[6], colors[7], colors[8]);
                    glTexCoord2f(texEndX, texEndY);
                    glVertex3f(endX, endY, 0);

                    glColor3f(colors[9], colors[10], colors[11]);
                    glTexCoord2f(texEndX, texStartY);
                    glVertex3f(endX, startY, 0);

                    startX += gw;
                    ++i;
                }

                glEnd();
                ++currLn;
            }
        }
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static int getHeight(FontTexture font) {
        return font.getGlyphHeight();
    }

    public static int getWidth(String text,
                               FontTexture font) {
        int i = 0;
        for (int j = 0, l = text.length(); j < l; j++) {
            i += font.getGlyph(text.charAt(j)).getWidth();
        }
        return i;
    }
}
