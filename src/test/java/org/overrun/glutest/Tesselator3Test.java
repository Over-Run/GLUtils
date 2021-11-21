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

package org.overrun.glutest;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.overrun.glutils.Tesselator3;
import org.overrun.glutils.wnd.GLFWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 */
public class Tesselator3Test {
    public static void main(String[] args) {
        GLFWErrorCallback.createPrint().set();
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        GLFWindow window = new GLFWindow(800, 600, "Tesselator3 Test");
        window.makeCurr();
        GL.createCapabilities();
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        Tesselator3 t = new Tesselator3();
        Matrix4f mvp = new Matrix4f().setOrtho2D(0, 800, 640, 0);
        window.show();
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            t.init()
                .color(1, 0, 0).vertex(0, 0, 0)
                .color(0, 1, 0).vertex(0, 200, 0)
                .color(0, 0, 1).vertex(200, 200, 0)
                .draw(mvp);
            window.swapBuffers();
            glfwPollEvents();
        }
        window.free();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
