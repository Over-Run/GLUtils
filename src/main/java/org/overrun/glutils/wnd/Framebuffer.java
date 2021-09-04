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

package org.overrun.glutils.wnd;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;

import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;

/**
 * @author squid233
 * @since 1.0.0
 */
public class Framebuffer {
    /**
     * size callback (pre)
     */
    public GLFWFramebufferSizeCallbackI cb;
    /**
     * framebuffer size
     */
    private int width, height;

    /**
     * construct and init
     *
     * @param parent parent window
     */
    public Framebuffer(long parent) {
        init(parent);
    }

    /**
     * construct and init
     *
     * @param parent parent window
     */
    public Framebuffer(GLFWindow parent) {
        this(parent.hWnd);
    }

    /**
     * construct
     * <p>
     * must init
     * </p>
     */
    public Framebuffer() {
    }

    /**
     * init
     *
     * @param parent parent window
     */
    public void init(long parent) {
        glfwSetFramebufferSizeCallback(parent,
                (window, width1, height1) -> {
                    if (cb != null) {
                        cb.invoke(window, width1, height1);
                    }
                    width = width1;
                    height = height1;
                });
    }

    /**
     * construct and init
     *
     * @param parent parent window
     */
    public void init(GLFWindow parent) {
        init(parent.hWnd);
    }

    /**
     * get framebuffer width
     *
     * @return framebuffer width
     */
    public int getWidth() {
        return width;
    }

    /**
     * get framebuffer height
     *
     * @return framebuffer height
     */
    public int getHeight() {
        return height;
    }
}
