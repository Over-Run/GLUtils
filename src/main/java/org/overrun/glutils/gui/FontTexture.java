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

package org.overrun.glutils.gui;

import org.overrun.glutils.gl.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.RenderingHints.*;
import static org.overrun.glutils.AWTImage.getRGB;
import static org.overrun.glutils.gl.MipmapMode.glLinear;
import static org.overrun.glutils.gl.MipmapMode.glNearest;

/**
 * @author squid233
 * @since 1.1.0
 */
public class FontTexture {
    private final Font font;
    private final Charset charset;
    private final Map<Character, Glyph> charMap = new LinkedHashMap<>();
    private final int padding;
    private int textureId;
    private int width;
    private int height;
    private int glyphHeight;

    /**
     * construct and build texture
     *
     * @param font    font
     * @param charset charset
     * @param padding char padding
     */
    public FontTexture(Font font,
                       Charset charset,
                       int padding) {
        this(font, charset, padding, true);
    }

    /**
     * construct and build texture
     *
     * @param font    font
     * @param charset charset
     * @param padding char padding
     * @param antialias enable antialias
     * @since 1.4.0
     */
    public FontTexture(Font font,
                       Charset charset,
                       int padding,
                       boolean antialias) {
        this.font = font;
        this.charset = charset;
        this.padding = padding;
        buildTexture(antialias);
    }

    /**
     * construct and build texture
     *
     * @param font    font
     * @param charset charset
     */
    public FontTexture(Font font,
                       Charset charset) {
        this(font, charset, 0);
    }

    /**
     * construct and build texture
     *
     * @param font        font
     * @param charsetName charset name
     * @param padding     char padding
     */
    public FontTexture(Font font,
                       String charsetName,
                       int padding) {
        this(font, Charset.forName(charsetName), padding);
    }

    /**
     * construct and build texture
     *
     * @param font        font
     * @param charsetName charset name
     */
    public FontTexture(Font font,
                       String charsetName) {
        this(font, charsetName, 0);
    }

    private String getAllAvailableChars() {
        CharsetEncoder ce = charset.newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private void buildTexture(boolean antialias) {
        Object aa = antialias
            ? VALUE_ANTIALIAS_ON
            : VALUE_ANTIALIAS_OFF;
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(KEY_ANTIALIASING, aa);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        char[] allChars = getAllAvailableChars().toCharArray();
        glyphHeight = fm.getHeight();
        int maxSize = Textures.getMaxSize();
        int startX = 0;
        int y = 0;
        for (char c : allChars) {
            int cw = fm.charWidth(c);
            if (startX > maxSize) {
                int w = startX - cw - padding;
                if (w > width) {
                    width = w;
                }
                startX = 0;
                y += glyphHeight;
            }
            Glyph glyph = new Glyph(startX, y, cw);
            charMap.put(c, glyph);
            startX += glyph.getWidth() + padding;
        }
        height = y + glyphHeight;
        g.dispose();
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = bi.createGraphics();
        g.setRenderingHint(KEY_ANTIALIASING, aa);
        g.setFont(font);
        fm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        startX = 0;
        y = 0;
        for (char c : allChars) {
            int cw = fm.charWidth(c);
            if (startX > maxSize) {
                startX = 0;
                y += glyphHeight;
            }
            Glyph glyph = new Glyph(startX, y, cw);
            g.drawString(String.valueOf(c), startX, y + fm.getAscent());
            startX += glyph.getWidth() + padding;
        }
        g.dispose();
        textureId = Textures.load(font.toString() + charset + padding,
                width,
                height,
                getRGB(bi),
                antialias ? glLinear() : glNearest());
    }

    /**
     * char info
     */
    public static class Glyph {
        private final int startX;
        private final int startY;
        private final int width;

        /**
         * construct
         *
         * @param startX start x
         * @param startY start y
         * @param width  char width
         */
        public Glyph(int startX,
                     int startY,
                     int width) {
            this.startX = startX;
            this.startY = startY;
            this.width = width;
        }

        /**
         * get start x
         *
         * @return start x
         */
        public int getStartX() {
            return startX;
        }

        /**
         * get start y
         *
         * @return start y
         */
        public int getStartY() {
            return startY;
        }

        /**
         * get char width
         *
         * @return char width
         */
        public int getWidth() {
            return width;
        }
    }

    /**
     * get width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * get height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * get glyph height
     *
     * @return glyph height
     */
    public int getGlyphHeight() {
        return glyphHeight;
    }

    /**
     * get texture id
     *
     * @return texture id
     */
    public int getTextureId() {
        return textureId;
    }

    /**
     * get char padding
     *
     * @return char padding
     */
    public int getPadding() {
        return padding;
    }

    /**
     * get glyph by char
     *
     * @param c char
     * @return glyph
     */
    public Glyph getGlyph(char c) {
        return charMap.get(c);
    }
}
