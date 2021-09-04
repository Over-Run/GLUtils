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

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.overrun.glutils.GLUtils;
import org.overrun.glutils.Textures;
import org.overrun.glutils.light.DirectionalLight;
import org.overrun.glutils.light.PointLight;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.glutils.wnd.GLFWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 */
public class GLUTest implements AutoCloseable {
    public static final float SENSITIVITY = 0.05f;
    public static final Timer TIMER = new Timer(60);
    public final Player player = new Player();
    private float lightAngle;
    private final Vector3f ambientLight = new Vector3f(0.3f);
    private PointLight pointLight;
    private DirectionalLight directionalLight;
    public GLFWindow window;
    public Framebuffer fb;
    public GameRenderer renderer;
    public boolean resized;
    public boolean grabbing;
    public double lastX, lastY;

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
        window = new GLFWindow(854,
                480,
                "Testing texture, lighting and HUD");
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
        init();
        window.show();
        window.focus();
        grabbing = true;
        window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        loop();
    }

    private void init()
            throws Exception {
        (renderer = new GameRenderer()).init();
        float lightIntensity = 1.0f;
        Vector3f lightColor = new Vector3f(1);
        // player is the lighting source
        Vector3f lightPosition = new Vector3f(0);
        pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        lightPosition = new Vector3f(-1, 0, 0);
        lightColor = new Vector3f(1);
        directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);
    }

    public void loop() {
        long lastTime = System.currentTimeMillis();
        int frames = 0;
        while (!window.shouldClose()) {
            TIMER.advanceTime();
            //float delta = TIMER.delta;
            for (int i = 0; i < TIMER.ticks; i++) {
                tick();
            }
            render();
            window.swapBuffers();
            glfwPollEvents();
            ++frames;
            while (System.currentTimeMillis() >= lastTime + 1000L) {
                TIMER.fps = frames;
                lastTime += 1000L;
                frames = 0;
            }
        }
    }

    public void tick() {
        player.tick();
        // Update directional light direction, intensity and color
        lightAngle += 1f;
        if (lightAngle > 90f) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    public void render() {
        int w = fb.getWidth(), h = fb.getHeight();
        if (resized) {
            glViewport(0, 0, w, h);
            resized = false;
        }
        if (w == 0) {
            w = 1;
        }
        if (h == 0) {
            h = 1;
        }
        renderer.render(w,
                h,
                player,
                ambientLight,
                pointLight,
                directionalLight,
                lightAngle);
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
