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

package org.overrun.glutils.tex;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.overrun.glutils.FilesReader.getInputStream;
import static org.overrun.glutils.GLUtils.getLogger;

/**
 * @author squid233
 * @since 2.0.0
 */
public class Images {
    @Contract("_ -> fail")
    public static void thr(String filename) throws IOException {
        throw new IOException(strErrStbi(filename));
    }

    @Contract("_ -> fail")
    public static void thrRE(String filename) {
        throw new RuntimeException(strErrStbi(filename));
    }

    public static void thrOut(String filename) {
        getLogger().error(strErrStbi(filename));
    }

    @NotNull
    public static String strErrStbi(String filename) {
        return "Error loading image \"" +
            filename +
            "\": " +
            stbi_failure_reason();
    }

    /**
     * load AWT image from classpath
     *
     * @param o The Class or ClassLoader
     * @param name   image absolute name
     * @return loaded image
     */
    public static BufferedImage loadAwt(Object o,
                                        String name) {
        try (var is = getInputStream(o, name)) {
            return ImageIO.read(requireNonNull(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * load AWT image from filesystem
     *
     * @param name image absolute name
     * @return loaded image
     */
    public static BufferedImage loadFsAwt(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get pixels array
     *
     * @param img image
     * @return pixels array
     */
    public static int[] getBGR(@NotNull BufferedImage img) {
        int w = img.getWidth();
        return img.getRGB(0,
            0,
            w,
            img.getHeight(),
            null,
            0,
            w);
    }

    /**
     * get pixels array and convert to RGB
     *
     * @param img image
     * @return converted pixels array
     */
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

    /**
     * Convert BGR to RGB pixels
     *
     * @param pixels BGR pixels
     * @return Copy of converted pixels
     * @since 1.0.0
     */
    public static int @NotNull [] toRGB(int[] pixels) {
        int[] copy = Arrays.copyOf(pixels, pixels.length);
        for (int i = 0; i < copy.length; ++i) {
            int a = copy[i] >> 24 & 0xff;
            int r = copy[i] >> 16 & 0xff;
            int g = copy[i] >> 8 & 0xff;
            int b = copy[i] & 0xff;
            copy[i] = a << 24 | b << 16 | g << 8 | r;
        }
        return copy;
    }
}
