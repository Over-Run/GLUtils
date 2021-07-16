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

package org.overrun.glutest;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 */
public class Window {
    public final long hwnd;
    public int w, h;

    public Window(int w, int h, String title) {
        this.hwnd = glfwCreateWindow(w, h, title, NULL, NULL);
        this.w = w;
        this.h = h;
    }

    public int key(int k) {
        return glfwGetKey(hwnd, k);
    }

    public void keyCb(GLFWKeyCallbackI cb) {
        glfwSetKeyCallback(hwnd, cb);
    }

    public void frbufSizCb(GLFWFramebufferSizeCallbackI cb) {
        glfwSetFramebufferSizeCallback(hwnd, cb);
    }

    public void cursorPosCb(GLFWCursorPosCallbackI cb) {
        glfwSetCursorPosCallback(hwnd, cb);
    }

    public void setPos(int x, int y) {
        glfwSetWindowPos(hwnd, x, y);
    }

    public void makeCurr() {
        glfwMakeContextCurrent(hwnd);
    }

    public void show() {
        glfwShowWindow(hwnd);
    }

    public void focus() {
        glfwFocusWindow(hwnd);
    }

    public void setInputMode(int mode, int value) {
        glfwSetInputMode(hwnd, mode, value);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(hwnd);
    }

    public void swapBuffers() {
        glfwSwapBuffers(hwnd);
    }
}
