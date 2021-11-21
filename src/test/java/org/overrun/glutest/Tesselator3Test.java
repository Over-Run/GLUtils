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
import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.GLFWindow;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class Tesselator3Test {
    static float x = 0, y = 0, z = 0, xRot = 0, yRot = 0,
        mx = 0, my = 0, dmx = 0, dmy = 0;

    public static void main(String[] args) throws Exception {
        GLFWErrorCallback.createPrint().set();
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        GLFWindow window = new GLFWindow(800, 600, "Tesselator3 Test");
        window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) {
                    window.closeWindow();
                }
            }
        });
        window.cursorPosCb((hWnd, xp, yp) -> {
            dmx = (float) (xp - mx);
            dmy = (float) (yp - my);
            xRot -= dmy * 0.15;
            yRot += dmx * 0.15;
            if (xRot < -90) {
                xRot = -90;
            } else if (xRot > 90) {
                xRot = 90;
            }
            mx = (float) xp;
            my = (float) yp;
        });
        window.makeCurr();
        GL.createCapabilities();
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        Tesselator3 t = new Tesselator3();
        Matrix4f mvp = new Matrix4f();
        int id = Textures.loadAWT(ClassLoader.getSystemClassLoader(), "tstest.png", GL_NEAREST);
        window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        window.show();
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            t.init()
                .color(0, 1, 0).vertex(0, 1, 0)
                .color(0, 0, 0).vertex(0, 0, 0)
                .color(1, 0, 0).vertex(1, 0, 0)
                .color(1, 0, 0).vertex(1, 0, 0)
                .color(1, 1, 0).vertex(1, 1, 0)
                .color(0, 1, 0).vertex(0, 1, 0)
                .draw(rotateY(
                    rotateX(
                        setPerspective(mvp, 90, 800, 600, 0.05f, 1000),
                        -xRot),
                    yRot).translate(-x, -y, -z));
            glClear(GL_DEPTH_BUFFER_BIT);
            glBindTexture(GL_TEXTURE_2D, id);
            t.init()
                .vertexUV(0, 0, 0, 0, 0)
                .vertexUV(0, 17, 0, 0, 1)
                .vertexUV(17, 17, 0, 1, 1)
                .vertexUV(17, 17, 0, 1, 1)
                .vertexUV(17, 0, 0, 1, 0)
                .vertexUV(0, 0, 0, 0, 0)
                .draw(mvp.setOrtho2D(0, 800, 640, 0));
            glBindTexture(GL_TEXTURE_2D, 0);
            window.swapBuffers();
            double sin = sin(toRadians(yRot)) * 0.01;
            double cos = cos(toRadians(yRot)) * 0.01;
            if (window.key(GLFW_KEY_W) == GLFW_PRESS) {
                x += sin;
                z -= cos;
            }
            if (window.key(GLFW_KEY_S) == GLFW_PRESS) {
                x -= sin;
                z += cos;
            }
            if (window.key(GLFW_KEY_A) == GLFW_PRESS) {
                x -= cos;
                z -= sin;
            }
            if (window.key(GLFW_KEY_D) == GLFW_PRESS) {
                x += cos;
                z += sin;
            }
            if (window.key(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
                y -= 0.01;
            }
            if (window.key(GLFW_KEY_SPACE) == GLFW_PRESS) {
                y += 0.01;
            }
            glfwPollEvents();
        }
        window.free();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
