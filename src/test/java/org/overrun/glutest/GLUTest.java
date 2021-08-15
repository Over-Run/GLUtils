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

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.overrun.glutils.GLUtils;
import org.overrun.glutils.Textures;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.glutils.wnd.GLFWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 */
public class GLUTest implements AutoCloseable {
    public static final float STEP = 0.04f;
    public static final float SENSITIVITY = 0.05f;
    public static final Timer TIMER = new Timer(20);
    public final Player player = new Player();
    public GLFWindow window;
    public Framebuffer fb;
    public GameRenderer renderer;
    public boolean resized;
    public boolean grabbing;
    public double lastX, lastY;

    public boolean pressing(int key) {
        return window.key(key) == GLFW_PRESS;
    }

    public float speed(double delta) {
        double base = STEP * delta;
        if (pressing(GLFW_KEY_LEFT_CONTROL)
                || pressing(GLFW_KEY_RIGHT_CONTROL)) {
            return (float) (base * 2);
        }
        return (float) base;
    }

    public void run() throws Exception {
        boolean coreProfile = Boolean.parseBoolean(System.getProperty(
                "GLUTest.coreProfile", "true"
        ));
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        if (coreProfile) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }
        window = new GLFWindow(854, 480, "Game");
        lastX = 854 / 2.0;
        lastY = 480 / 2.0;
        fb = new Framebuffer();
        fb.cb = (window1, width, height) -> resized = true;
        fb.init(window);
        window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(hWnd, true);
                }
                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    grabbing = !grabbing;
                    glfwSetInputMode(hWnd,
                            GLFW_CURSOR,
                            grabbing ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
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
                player.rotate(xOffset, yOffset);
            }
            lastX = xpos;
            lastY = ypos;
        });
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (mode != null) {
            window.setPos((mode.width() - window.getWidth()) / 2,
                    (mode.height() - window.getHeight()) / 2);
        }
        window.makeCurr();
        GL.createCapabilities(coreProfile);
        glfwSwapInterval(1);
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        System.out.println("GL Version " + glGetString(GL_VERSION));
        TIMER.advanceTime();
        (renderer = new GameRenderer()).init();
        window.show();
        window.focus();
        grabbing = true;
        window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        loop();
    }

    public void loop() {
        while (!window.shouldClose()) {
            TIMER.advanceTime();
            float delta = TIMER.delta;
            input(delta);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render(delta);
            window.swapBuffers();
            glfwPollEvents();
        }
    }

    public void input(double delta) {
        float xo = 0;
        float yo = 0;
        float zo = 0;
        if (pressing(GLFW_KEY_W)) {
            if (!pressing(GLFW_KEY_S)) {
                zo = speed(delta);
            }
        } else if (pressing(GLFW_KEY_S)) {
            zo = -speed(delta);
        }
        if (pressing(GLFW_KEY_A)) {
            if (!pressing(GLFW_KEY_D)) {
                xo = -speed(delta);
            }
        } else if (pressing(GLFW_KEY_D)) {
            xo = speed(delta);
        }
        if (pressing(GLFW_KEY_LEFT_SHIFT)) {
            if (!pressing(GLFW_KEY_SPACE)) {
                yo = -speed(delta);
            }
        } else if (pressing(GLFW_KEY_SPACE)) {
            yo = speed(delta);
        }
        player.moveRelative(xo, yo, zo);
    }

    public void render(double delta) {
        int w = fb.getWidth(), h = fb.getHeight();
        renderer.render(w, h, player);
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
