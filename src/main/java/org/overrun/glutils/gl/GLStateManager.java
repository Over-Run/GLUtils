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

package org.overrun.glutils.gl;

import static org.lwjgl.opengl.GL11.*;

/**
 * Some methods for calling GL functions quickly.
 *
 * @author squid233
 * @since 1.5.0
 */
public class GLStateManager {
    public static void enableDepthTest() {
        glEnable(GL_DEPTH_TEST);
    }

    public static void enableCullFace() {
        glEnable(GL_CULL_FACE);
    }

    public static void enableBlend() {
        glEnable(GL_BLEND);
    }

    public static void enableTexture2D() {
        glEnable(GL_TEXTURE_2D);
    }

    public static void disableDepthTest() {
        glDisable(GL_DEPTH_TEST);
    }

    public static void disableCullFace() {
        glDisable(GL_CULL_FACE);
    }

    public static void disableBlend() {
        glDisable(GL_BLEND);
    }

    public static void disableTexture2D() {
        glDisable(GL_TEXTURE_2D);
    }
}
