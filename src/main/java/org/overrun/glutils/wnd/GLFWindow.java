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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * GLFW Window
 *
 * @author squid233
 * @since 1.0.0
 */
public class GLFWindow implements AutoCloseable {
    /**
     * window handler
     */
    public final long hWnd;
    /**
     * Only for computing border size
     */
    private int width, height;
    private String title;

    /**
     * construct and create window
     *
     * @param width  window width
     * @param height window height
     * @param title  window title
     */
    public GLFWindow(int width, int height, String title) {
        hWnd = glfwCreateWindow(width, height, title, NULL, NULL);
        if (hWnd == NULL) {
            throw new RuntimeException("Can't create window");
        }
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * get key
     *
     * @param k key
     * @return one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}
     */
    public int key(int k) {
        return glfwGetKey(hWnd, k);
    }

    /**
     * set key callback
     *
     * @param cb key callback
     */
    public void keyCb(GLFWKeyCallbackI cb) {
        glfwSetKeyCallback(hWnd, cb);
    }

    /**
     * set cursor pos callback
     *
     * @param cb cursor pos callback
     */
    public void cursorPosCb(GLFWCursorPosCallbackI cb) {
        glfwSetCursorPosCallback(hWnd, cb);
    }

    /**
     * set window pos
     *
     * @param x pos x
     * @param y pos y
     */
    public void setPos(int x, int y) {
        glfwSetWindowPos(hWnd, x, y);
    }

    /**
     * make context current
     */
    public void makeCurr() {
        glfwMakeContextCurrent(hWnd);
    }

    /**
     * show window
     */
    public void show() {
        glfwShowWindow(hWnd);
    }

    /**
     * focus window
     */
    public void focus() {
        glfwFocusWindow(hWnd);
    }

    /**
     * set input mode
     *
     * @param mode  input mode for set
     * @param value input mode
     */
    public void setInputMode(int mode, int value) {
        glfwSetInputMode(hWnd, mode, value);
    }

    /**
     * should close
     *
     * @return should close
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(hWnd);
    }

    /**
     * swap buffers
     */
    public void swapBuffers() {
        glfwSwapBuffers(hWnd);
    }

    /**
     * free callbacks and destroy window
     */
    public void free() {
        glfwFreeCallbacks(hWnd);
        glfwDestroyWindow(hWnd);
    }

    /**
     * set window width
     *
     * @param width new width
     */
    public void setWidth(int width) {
        this.width = width;
        glfwSetWindowSize(hWnd, width, height);
    }

    /**
     * set window height
     *
     * @param height new height
     */
    public void setHeight(int height) {
        this.height = height;
        glfwSetWindowSize(hWnd, width, height);
    }

    /**
     * set window size
     *
     * @param width  new width
     * @param height new height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        glfwSetWindowSize(hWnd, width, height);
    }

    /**
     * set window title
     *
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(hWnd, title);
    }

    /**
     * get window width
     *
     * @return window width
     */
    public int getWidth() {
        return width;
    }

    /**
     * get window height
     *
     * @return window height
     */
    public int getHeight() {
        return height;
    }

    /**
     * get window title
     *
     * @return window title
     */
    public String getTitle() {
        return title;
    }

    /**
     * get window handler
     *
     * @return window handler
     */
    public long getHandle() {
        return hWnd;
    }

    @Override
    public void close() {
        free();
    }
}
