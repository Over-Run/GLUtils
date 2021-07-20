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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author squid233
 * @since 0.4.0
 */
public class AWTImage {
    public final boolean isNull;
    public final BufferedImage img;

    public AWTImage(boolean isNull, BufferedImage img) {
        this.isNull = isNull;
        this.img = img;
    }

    public static BufferedImage load(ClassLoader loader, String name)
            throws Exception {
        try (InputStream is = loader.getResourceAsStream(name)) {
            return ImageIO.read(Objects.requireNonNull(is));
        }
    }

    public static int[] getBGR(BufferedImage img) {
        int w = img.getWidth();
        return img.getRGB(0,
                0,
                w,
                img.getHeight(),
                null,
                0,
                w);
    }

    public static int[] getRGB(BufferedImage img) {
        int[] pixels = getBGR(img);
        for (int i = 0; i < pixels.length; ++i) {
            int a = pixels[i] >> 24 & 255;
            int r = pixels[i] >> 16 & 255;
            int g = pixels[i] >> 8 & 255;
            int b = pixels[i] & 255;
            pixels[i] = a << 24 | b << 16 | g << 8 | r;
        }
        return pixels;
    }
}
