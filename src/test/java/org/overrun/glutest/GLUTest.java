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

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.overrun.glutils.*;

import javax.swing.*;
import java.awt.*;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.joml.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.overrun.glutils.ShaderReader.lines;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class GLUTest {
    public static final ClassLoader cl = GLUTest.class.getClassLoader();
    public static final float STEP = 0.5f;
    public static final float SENSITIVITY = 0.05f;
    public final Matrix4f proj = new Matrix4f();
    public final Matrix4f modelv = new Matrix4f();
    public final Matrix4f view = new Matrix4f();
    public final AtlasLoomAWT loom = AtlasLoom.awt("cube");
    public final int TEST_VER = 1;
    public int texture;
    public long hwnd;
    public GLProgram program;
    public GLProgram program2d;
    public Mesh3 mesh;
    public Mesh3 crossing;
    public int w = 854, h = 480;
    public float xRot, yRot;
    public final Vector3f pos = new Vector3f(0.5f, 0.5f, 0.5f);
    public boolean resized;
    public boolean grabbing;
    public double delta;
    public double lastX = w / 2.0, lastY = h / 2.0;

    public boolean pressing(int key) {
        return glfwGetKey(hwnd, key) == GLFW_PRESS;
    }

    public float speed() {
        double base = STEP * delta;
        if (pressing(GLFW_KEY_LEFT_CONTROL)
                || pressing(GLFW_KEY_RIGHT_CONTROL)) {
            return (float) (base * 2);
        }
        return (float) base;
    }

    public void run() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        hwnd = glfwCreateWindow(w, h, "Game", NULL, NULL);
        glfwSetKeyCallback(hwnd, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(window, true);
                }
                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    grabbing = !grabbing;
                    glfwSetInputMode(hwnd,
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
        glfwSetFramebufferSizeCallback(hwnd, (window, width, height) -> {
            resized = true;
            w = width;
            h = height;
        });
        glfwSetCursorPosCallback(hwnd, (window, xpos, ypos) -> {
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
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer pw = stack.mallocInt(1);
                IntBuffer ph = stack.mallocInt(1);
                glfwGetWindowSize(hwnd, pw, ph);
                glfwSetWindowPos(hwnd,
                        (mode.width() - pw.get(0)) / 2,
                        (mode.height() - ph.get(0)) / 2);
            }
        }
        glfwMakeContextCurrent(hwnd);
        GL.createCapabilities(true);
        glfwSwapInterval(1);
        Color color = UIManager.getColor("control");
        glClearColor(color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f);
        texture = /*loom.load(cl,
                1,
                1,
                GL_NEAREST,
                "img0.png",
                "img1.png",
                "img2.png",
                "img3.png")*/Textures.loadAWT(cl, "face.png", GL_NEAREST);
        System.out.println("GL Version " + glGetString(GL_VERSION));
        glfwShowWindow(hwnd);
        loop();
    }

    public void loop() {
        program = new GLProgram();
        program.createVsh(lines(cl, "shaders/scene.vsh"));
        program.createFsh(lines(cl, "shaders/scene.fsh"));
        program.link();
        program2d = new GLProgram();
        program2d.createVsh(lines(cl, "shaders/scene.vsh"));
        program2d.createFsh(lines(cl, "shaders/scene.fsh"));
        program2d.link();
        float size = 1.0f;
        float[] vert = {
                // front
                0.0f, size, 0.0f, //0
                0.0f, 0.0f, 0.0f, //1
                size, 0.0f, 0.0f, //2
                size, size, 0.0f, //3
                // back
                size, size, -size, // 4
                size, 0.0f, -size, // 5
                0.0f, 0.0f, -size, // 6
                0.0f, size, -size, // 7
                // left
                0.0f, size, -size, // 8
                0.0f, 0.0f, -size, // 9
                0.0f, 0.0f, 0.0f, // 10
                0.0f, size, 0.0f, // 11
                // right
                size, size, 0.0f, // 12
                size, 0.0f, 0.0f, // 13
                size, 0.0f, -size, // 14
                size, size, -size, // 15
                // up
                0.0f, size, -size, // 16
                0.0f, size, 0.0f, // 17
                size, size, 0.0f, // 18
                size, size, -size, // 19
                // down
                size, 0.0f, -size, // 20
                size, 0.0f, 0.0f, // 21
                0.0f, 0.0f, 0.0f, // 22
                0.0f, 0.0f, -size // 23
        };
        float[] col = {
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,

                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,

                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,

                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,

                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,

                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f
        };
        float[] tex = {
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };
        int[] idx = {
                // south
                0, 1, 3, 3, 1, 2,
                // north
                4, 5, 7, 7, 5, 6,
                // west
                8, 9, 11, 11, 9, 10,
                // east
                12, 13, 15, 15, 13, 14,
                // up
                16, 17, 19, 19, 17, 18,
                // down
                20, 21, 23, 23, 21, 22
        };
        mesh = Mesh3.builder()
                .vertIdx(0)
                .vertices(vert)
                .colorIdx(1)
                .colors(col)
                .texIdx(2)
                .texture(texture)
                .texCoords(tex)
                .indices(idx)
                .build();
        vert = new float[]{
                -1, -9, 0, //0
                1, -9, 0, //1
                -1, 9, 0, //2
                1, 9, 0, //3
                -9, -1, 0, //4
                -9, 1, 0, //5
                9, -1, 0, //6
                9, 1, 0 //7
        };
        col = new float[]{
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };
        idx = new int[]{
                0, 2, 1, 1, 2, 3,
                4, 5, 6, 6, 5, 7
        };
        crossing = Mesh3.builder()
                .vertIdx(0)
                .vertices(vert)
                .colorIdx(1)
                .colors(col)
                .indices(idx)
                .build();
        grabbing = true;
        glfwSetInputMode(hwnd, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        double lastTime = 0;
        while (!glfwWindowShouldClose(hwnd)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            double curr = glfwGetTime();
            delta = curr - lastTime;
            lastTime = curr;
            render();
            glfwSwapBuffers(hwnd);
            glfwPollEvents();
        }
    }

    public void render() {
        if (resized) {
            glViewport(0, 0, w, h);
            resized = false;
        }
        if (pressing(GLFW_KEY_W)) {
            pos.x += sin(toRadians(yRot)) * speed();
            pos.z -= cos(toRadians(yRot)) * speed();
        }
        if (pressing(GLFW_KEY_S)) {
            pos.x -= sin(toRadians(yRot)) * speed();
            pos.z += cos(toRadians(yRot)) * speed();
        }
        if (pressing(GLFW_KEY_A)) {
            pos.x += sin(toRadians(yRot - 90)) * speed();
            pos.z -= cos(toRadians(yRot - 90)) * speed();
        }
        if (pressing(GLFW_KEY_D)) {
            pos.x -= sin(toRadians(yRot - 90)) * speed();
            pos.z += cos(toRadians(yRot - 90)) * speed();
        }
        if (pressing(GLFW_KEY_LEFT_SHIFT)) {
            pos.y -= speed();
        }
        if (pressing(GLFW_KEY_SPACE)) {
            pos.y += speed();
        }
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        program.bind();
        program.setUniform("texSampler", 0);
        program.setUniformMat4("proj", setPerspective(proj, 70, w, h, 0.05f, 1000.0f));
        program.setUniformMat4("modelv", rotateY(rotationX(modelv, -xRot), yRot).translate(-pos.x, -pos.y, -pos.z));
        program.setUniform("textured", 1);
        mesh.render();
        program.unbind();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        renderGui();
    }

    public void renderGui() {
        program2d.bind();
        program2d.setUniform("texSampler", 0);
        program2d.setUniformMat4("proj", view.setOrtho2D(0, w, h, 0));
        program2d.setUniformMat4("modelv", view.translation(w / 2f, h / 2f, 0));
        crossing.render();
        program2d.unbind();
    }

    @Test
    public void main() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        System.out.println("Testing GLUtils " + GLUtils.VERSION);
        System.out.println("Version " + TEST_VER);
        GLFWErrorCallback.createPrint().set();
        glfwInit();
        try {
            run();
        } finally {
            if (mesh != null) {
                mesh.close();
            }
            if (program != null) {
                program.close();
            }
            glfwSetErrorCallback(null).free();
            glfwTerminate();
        }
    }
}
