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

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.overrun.glutils.GLUtils;
import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.glutils.wnd.GLFWindow;

import javax.swing.UIManager;
import java.awt.Color;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.math.Math.*;

/**
 * @author squid233
 */
public class GLUTest implements AutoCloseable {
    public static final float STEP = 0.5f;
    public static final float SENSITIVITY = 0.05f;
    public static final int TARGET_UPS = 30;
    public static final Timer TIMER = new Timer();
    public final GameRenderer renderer = new GameRenderer();
    public final Vector3f pos = new Vector3f(0.5f, 0.5f, 0.5f);
    public GLFWindow window;
    public Framebuffer fb;
    public float xRot, yRot;
    public boolean resized;
    public boolean grabbing;
    public double lastX, lastY;
    public double lastFps;

    public boolean pressing(int key) {
        return window.key(key) == GLFW_PRESS;
    }

    public float speed(float delta) {
        double base = STEP * delta;
        if (pressing(GLFW_KEY_LEFT_CONTROL)
                || pressing(GLFW_KEY_RIGHT_CONTROL)) {
            return (float) (base * 2);
        }
        return (float) base;
    }

    public void run() throws Exception {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = new GLFWindow(854, 480, "Game");
        lastX = 854 / 2.0;
        lastY = 480 / 2.0;
        fb = new Framebuffer();
        fb.cb = (window1, width, height) -> resized = true;
        fb.init(window);
        window.keyCb((window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(window, true);
                }
                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    grabbing = !grabbing;
                    glfwSetInputMode(window,
                            GLFW_CURSOR,
                            grabbing ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
                    if (grabbing) {
                        try (MemoryStack stack = MemoryStack.stackPush()) {
                            DoubleBuffer px = stack.mallocDouble(1);
                            DoubleBuffer py = stack.mallocDouble(1);
                            glfwGetCursorPos(window, px, py);
                            lastX = px.get(0);
                            lastY = py.get(0);
                        }
                    }
                }
            }
        });
        window.cursorPosCb((window, xpos, ypos) -> {
            if (grabbing) {
                double xOffset = xpos - lastX;
                double yOffset = lastY - ypos;
                lastX = xpos;
                lastY = ypos;
                xOffset *= SENSITIVITY;
                yOffset *= SENSITIVITY;
                xRot += yOffset;
                yRot += xOffset;
                if (xRot > 90) {
                    xRot = 90;
                }
                if (xRot < -90) {
                    xRot = -90;
                }
            }
        });
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (mode != null) {
            window.setPos((mode.width() - window.getWidth()) / 2,
                    (mode.height() - window.getHeight()) / 2);
        }
        window.makeCurr();
        GL.createCapabilities(true);
        glfwSwapInterval(1);
        Color color = UIManager.getColor("control");
        glClearColor(color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f);
        System.out.println("GL Version " + glGetString(GL_VERSION));
        TIMER.init();
        renderer.init();
        lastFps = TIMER.getTime();
        TIMER.fps = 0;
        window.show();
        window.focus();
        grabbing = true;
        window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        loop();
    }

    public void loop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;
        while (!window.shouldClose()) {
            elapsedTime = TIMER.getElapsedTime();
            accumulator += elapsedTime;
            while (accumulator >= interval) {
                accumulator -= interval;
            }
            input(accumulator);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render(accumulator);
            window.swapBuffers();
            glfwPollEvents();
        }
    }

    public void input(float delta) {
        if (pressing(GLFW_KEY_W)) {
            pos.x += sin(toRadians(yRot)) * speed(delta);
            pos.z -= cos(toRadians(yRot)) * speed(delta);
        }
        if (pressing(GLFW_KEY_S)) {
            pos.x -= sin(toRadians(yRot)) * speed(delta);
            pos.z += cos(toRadians(yRot)) * speed(delta);
        }
        if (pressing(GLFW_KEY_A)) {
            pos.x += sin(toRadians(yRot - 90)) * speed(delta);
            pos.z -= cos(toRadians(yRot - 90)) * speed(delta);
        }
        if (pressing(GLFW_KEY_D)) {
            pos.x -= sin(toRadians(yRot - 90)) * speed(delta);
            pos.z += cos(toRadians(yRot - 90)) * speed(delta);
        }
        if (pressing(GLFW_KEY_LEFT_SHIFT)) {
            pos.y -= speed(delta);
        }
        if (pressing(GLFW_KEY_SPACE)) {
            pos.y += speed(delta);
        }
    }

    public void render(float delta) {
        int w = fb.getWidth(), h = fb.getHeight();
        if (TIMER.getLastLoopTime() - lastFps > 1) {
            lastFps = TIMER.getLastLoopTime();
            TIMER.fps = 0;
        }
        TIMER.fps++;
        renderer.render(w, h, xRot, yRot, pos);
        if (resized) {
            glViewport(0, 0, w, h);
            resized = false;
        }
    }

    @Override
    public void close() {
        Textures.close();
        window.free();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void start() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        System.out.println("Testing GLUtils " + GLUtils.VERSION);
        GLFWErrorCallback.createPrint().set();
        glfwInit();
        run();
    }

    public static void main(String[] args) throws Exception {
        try (GLUTest test = new GLUTest()) {
            test.start();
        }
    }
}
