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

import java.awt.Font;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 1.1.0
 */
public class FontTextures {
    private static final Map<String, FontTexture> FONT_TEXTURES =
            new HashMap<>();

    /**
     * construct builder
     *
     * @param texName texture name
     * @return font texture builder
     */
    public static Builder builder(String texName) {
        return new Builder(texName);
    }

    public static class Builder {
        private final String texName;
        private Font font;
        private Charset charset;
        private int padding = 0;

        public Builder(String texName) {
            this.texName = texName;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder charset(String charsetName) {
            this.charset = Charset.forName(charsetName);
            return this;
        }

        public Builder padding(int padding) {
            this.padding = padding;
            return this;
        }

        public FontTexture build() throws Exception {
            if (FONT_TEXTURES.containsKey(texName)) {
                return FONT_TEXTURES.get(texName);
            }
            FontTexture ft = new FontTexture(font,
                    charset,
                    padding);
            FONT_TEXTURES.put(texName, ft);
            return ft;
        }
    }
}
