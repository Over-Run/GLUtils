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
import org.overrun.glutils.IndexedTesselator3;
import org.overrun.glutils.Tesselator3;
import org.overrun.glutils.Textures;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.game.GameEngine;
import org.overrun.glutils.game.GameLogic;
import org.overrun.glutils.wnd.Framebuffer;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GameEngine.window;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class Tesselator3Test implements GameLogic {
    private float x = 0, y = 0, z = 0, xRot = 0, yRot = 0,
        mx = 0, my = 0, dmx = 0, dmy = 0;
    private final Matrix4f mvp = new Matrix4f();
    private IndexedTesselator3 it;
    private Tesselator3 t;
    private int id;

    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        config.width = 854;
        config.height = 480;
        config.title = "Tesselator3 Test";
        new GameApp(new Tesselator3Test(), config);
    }

    @Override
    public void create() {
        window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) {
                    window.closeWindow();
                }
                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    window.setGrabbed(!window.isGrabbed());
                }
            }
        });
        window.cursorPosCb((hWnd, xp, yp) -> {
            dmx = (float) (xp - mx);
            dmy = (float) (yp - my);
            if (window.isGrabbed()) {
                xRot -= dmy * 0.15;
                yRot += dmx * 0.15;
                if (xRot < -90) {
                    xRot = -90;
                } else if (xRot > 90) {
                    xRot = 90;
                }
            }
            mx = (float) xp;
            my = (float) yp;
        });
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        window.setGrabbed(true);
        it = new IndexedTesselator3(true)
            .color(0, 1, 0).vertex(0, 1, 0)
            .color(0, 0, 0).vertex(0, 0, 0)
            .color(1, 0, 0).vertex(1, 0, 0)
            .color(1, 1, 0).vertex(1, 1, 0)
            .color(0, 1, 1).vertex(0, 1, -1)
            .color(0, 0, 1).vertex(0, 0, -1)
            .color(1, 0, 1).vertex(1, 0, -1)
            .color(1, 1, 1).vertex(1, 1, -1)
            .indices(
                // south
                0, 1, 2, 2, 3, 0,
                // north
                7, 6, 5, 5, 4, 7,
                // west
                4, 5, 1, 1, 0, 4,
                // east
                3, 2, 6, 6, 7, 3,
                // up
                4, 0, 3, 3, 7, 4,
                // down
                1, 5, 6, 6, 2, 1
            );
        t = new Tesselator3(true)
            .vertexUV(0, 0, 0, 0, 0)
            .vertexUV(0, 17, 0, 0, 1)
            .vertexUV(17, 17, 0, 1, 1)
            .vertexUV(17, 17, 0, 1, 1)
            .vertexUV(17, 0, 0, 1, 0)
            .vertexUV(0, 0, 0, 0, 0);
        id = Textures.loadAWT(ClassLoader.getSystemClassLoader(), "tstest.png", GL_NEAREST);
    }

    @Override
    public void render() {
        Framebuffer fb = GameEngine.framebuffer;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        it.draw(rotateY(
            rotateX(
                setPerspective(mvp, 90, fb, 0.05f, 1000),
                -xRot),
            yRot).translate(-x, -y, -z));
        glClear(GL_DEPTH_BUFFER_BIT);
        glBindTexture(GL_TEXTURE_2D, id);
        t.draw(mvp.setOrtho2D(0, fb.width(), fb.height(), 0));
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void tick() {
        double sin = sin(toRadians(yRot)) * 0.1;
        double cos = cos(toRadians(yRot)) * 0.1;
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
            y -= 0.1;
        }
        if (window.key(GLFW_KEY_SPACE) == GLFW_PRESS) {
            y += 0.1;
        }
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void free() {
        it.free();
        t.free();
    }
}
