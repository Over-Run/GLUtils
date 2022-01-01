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

package org.overrun.glutils.wnd;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.opengl.GL11;
import org.overrun.glutils.SizedObject;
import org.overrun.glutils.game.GameApp;

import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;

/**
 * @author squid233
 * @since 1.0.0
 */
public class Framebuffer implements SizedObject {
    /**
     * Default OpenGL viewport function
     *
     * @since 1.5.0
     */
    public static final GLFWFramebufferSizeCallbackI GL_VIEWPORT_FUNC =
        (window, w, h) -> GL11.glViewport(0, 0, w, h);

    /**
     * size callback (pre)
     */
    public GLFWFramebufferSizeCallbackI cb;
    /**
     * framebuffer size
     */
    private int width, height;

    /**
     * construct, set cb and init
     *
     * @param cb     size callback
     * @param parent parent window
     * @since 1.3.0
     */
    public Framebuffer(GLFWFramebufferSizeCallbackI cb,
                       GLFWindow parent) {
        this.cb = cb;
        init(parent);
    }

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
     * Construct.
     * <p>
     * Note: You must init yourself.
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
            (window, w, h) -> {
                if (cb != null) {
                    cb.invoke(window, w, h);
                }
                width = w;
                height = h;
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
     * Set width. Only call by {@link GameApp}
     *
     * @param width The width.
     * @return this
     */
    public Framebuffer setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set height. Only call by {@link GameApp}
     *
     * @param height The height.
     * @return this
     */
    public Framebuffer setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * get framebuffer width
     *
     * @return framebuffer width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * get framebuffer height
     *
     * @return framebuffer height
     */
    @Override
    public int getHeight() {
        return height;
    }
}
