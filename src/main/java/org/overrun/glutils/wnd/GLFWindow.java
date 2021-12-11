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

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * GLFW Window
 *
 * @author squid233
 * @since 1.0.0
 */
public class GLFWindow implements SizedObject {
    /**
     * window handle
     */
    public final long hWnd;
    /**
     * Only for computing border size
     */
    private int width, height;
    /**
     * Mouse position
     */
    public int mouseX, mouseY;
    private String title;
    private boolean resized;
    private boolean grabbed;

    /**
     * construct and create window
     *
     * @param width   window width
     * @param height  window height
     * @param title   window title
     * @param monitor window monitor
     * @param share   window share to
     * @since 1.5.0
     */
    public GLFWindow(final int width,
                     final int height,
                     final String title,
                     final long monitor,
                     final long share) {
        hWnd = glfwCreateWindow(width, height, title, monitor, share);
        if (hWnd == NULL) {
            throw new RuntimeException("Can't create window");
        }
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * construct and create window
     *
     * @param width  window width
     * @param height window height
     * @param title  window title
     */
    public GLFWindow(final int width,
                     final int height,
                     final String title) {
        this(width, height, title, NULL, NULL);
    }

    /**
     * Get key state
     *
     * @param k key
     * @return one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}
     */
    public int key(final int k) {
        return glfwGetKey(hWnd, k);
    }

    /**
     * Get mouse button state
     *
     * @param b Button
     * @return one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}
     * @since 1.5.0
     */
    public int mouse(final int b) {
        return glfwGetMouseButton(hWnd, b);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Window callbacks
    ///////////////////////////////////////////////////////////////////////////

    /**
     * set key callback
     *
     * @param cb key callback
     */
    public void keyCb(final GLFWKeyCallbackI cb) {
        glfwSetKeyCallback(hWnd, cb);
    }

    /**
     * set cursor pos callback
     *
     * @param cb cursor pos callback
     */
    public void cursorPosCb(final GLFWCursorPosCallbackI cb) {
        glfwSetCursorPosCallback(hWnd, cb);
    }

    /**
     * set scroll callback
     *
     * @param cb scroll callback
     * @since 1.3.0
     */
    public void scrollCb(final GLFWScrollCallbackI cb) {
        glfwSetScrollCallback(hWnd, cb);
    }

    /**
     * set mouse button callback
     *
     * @param cb mouse button callback
     * @since 1.4.0
     */
    public void mouseButtonCb(final GLFWMouseButtonCallbackI cb) {
        glfwSetMouseButtonCallback(hWnd, cb);
    }

    /**
     * set char callback
     *
     * @param cb char callback
     * @since 1.4.0
     */
    public void charCb(final GLFWCharCallbackI cb) {
        glfwSetCharCallback(hWnd, cb);
    }

    /**
     * Set window icon.
     *
     * @param images Images in buffer.
     * @since 1.3.0
     */
    public void setIcon(final GLFWImage.Buffer images) {
        glfwSetWindowIcon(hWnd, images);
    }

    /**
     * Set window icon.
     * <p>
     * Ignore when {@code count == 0}.
     *
     * @param images Images in array.
     * @since 1.3.0
     */
    public void setIcon(final GLFWImage... images) {
        if (images.length < 1) {
            return;
        }
        try (GLFWImage.Buffer buffer = GLFWImage.malloc(images.length)) {
            for (int i = 0; i < images.length; i++) {
                buffer.put(i, images[i]);
            }
            setIcon(buffer);
        }
    }

    /**
     * set window pos
     *
     * @param x pos x
     * @param y pos y
     */
    public void setPos(final int x,
                       final int y) {
        glfwSetWindowPos(hWnd, x, y);
    }

    /**
     * Get the key status
     *
     * @param key The key
     * @return {@code PRESS} or {@code RELEASE}
     * @since 1.4.0
     */
    public int getKey(final int key) {
        return glfwGetKey(hWnd, key);
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
    public void setInputMode(final int mode,
                             final int value) {
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
     * set grabbed
     *
     * @param grabbed grabbed
     * @since 1.5.0
     */
    public void setGrabbed(final boolean grabbed) {
        this.grabbed = grabbed;
        setInputMode(GLFW_CURSOR,
            grabbed ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    /**
     * is grabbed
     *
     * @return {@link #grabbed}
     * @since 1.5.0
     */
    public boolean isGrabbed() {
        return grabbed;
    }

    /**
     * Close window
     *
     * @since 2.0.0
     */
    public void close() {
        glfwSetWindowShouldClose(hWnd, true);
    }

    /**
     * Free callbacks and destroy window
     */
    public void free() {
        glfwFreeCallbacks(hWnd);
        glfwDestroyWindow(hWnd);
    }

    /**
     * set window width
     *
     * @param width new width
     * @return this
     */
    public GLFWindow setWidth(final int width) {
        this.width = width;
        glfwSetWindowSize(hWnd, width, height);
        return this;
    }

    /**
     * set window height
     *
     * @param height new height
     * @return this
     */
    public GLFWindow setHeight(final int height) {
        this.height = height;
        glfwSetWindowSize(hWnd, width, height);
        return this;
    }

    /**
     * set window size
     *
     * @param width  new width
     * @param height new height
     * @return this
     */
    public GLFWindow setSize(final int width,
                             final int height) {
        this.width = width;
        this.height = height;
        glfwSetWindowSize(hWnd, width, height);
        return this;
    }

    /**
     * set window title
     *
     * @param title new title
     * @return this
     */
    public GLFWindow setTitle(final String title) {
        this.title = title;
        glfwSetWindowTitle(hWnd, title);
        return this;
    }

    /**
     * is resized
     *
     * @return {@link #resized}
     * @since 1.3.0
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * set resized
     *
     * @param resized window resized
     * @return this
     * @since 1.3.0
     */
    public GLFWindow setResized(final boolean resized) {
        this.resized = resized;
        return this;
    }

    /**
     * Set mouse pos.
     *
     * @param mouseX mouse x
     * @param mouseY mouse y
     * @return this
     * @since 1.3.0
     */
    @Deprecated
    public GLFWindow setMousePos(final int mouseX,
                                 final int mouseY) {
        return setCursorPos(mouseX, mouseY);
    }

    /**
     * Set cursor pos.
     *
     * @param posX cursor x
     * @param posY cursor y
     * @return this
     * @since 1.5.0
     */
    public GLFWindow setCursorPos(final int posX,
                                  final int posY) {
        mouseX = posX;
        mouseY = posY;
        glfwSetCursorPos(hWnd, posX, posY);
        return this;
    }

    /**
     * Get cursor pos and storage to buffers.
     *
     * @param xp Pos x buffer.
     * @param yp Pos y buffer.
     * @since 1.5.0
     */
    public void getCursorPos(@Nullable final DoubleBuffer xp,
                             @Nullable final DoubleBuffer yp) {
        glfwGetCursorPos(hWnd, xp, yp);
    }

    /**
     * set mouse x
     *
     * @param mouseX mouse x
     * @return this
     * @since 1.3.0
     */
    public GLFWindow setMouseX(final int mouseX) {
        this.mouseX = mouseX;
        glfwSetCursorPos(hWnd, mouseX, mouseY);
        return this;
    }

    /**
     * set mouse y
     *
     * @param mouseY mouse y
     * @return this
     * @since 1.3.0
     */
    public GLFWindow setMouseY(final int mouseY) {
        this.mouseY = mouseY;
        glfwSetCursorPos(hWnd, mouseX, mouseY);
        return this;
    }

    /**
     * get window width
     *
     * @return window width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * get window height
     *
     * @return window height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * get window mouse x
     *
     * @return window mouse x
     * @since 1.3.0
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * get window mouse y
     *
     * @return window mouse y
     * @since 1.3.0
     */
    public int getMouseY() {
        return mouseY;
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
     * get window handle
     *
     * @return window handle
     */
    public long getHandle() {
        return hWnd;
    }
}
