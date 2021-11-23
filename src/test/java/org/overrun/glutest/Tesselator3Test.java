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
import org.overrun.glutils.MipmapMode;
import org.overrun.glutils.Tesselator3;
import org.overrun.glutils.game.*;
import org.overrun.glutils.wnd.Framebuffer;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GLStateManager.enableDepthTest;
import static org.overrun.glutils.game.GameEngine.*;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class Tesselator3Test extends Game {
    private float x = 0, y = 0, z = 0,
        xRot = 0, yRot = 0,
        xo = 0, yo = 0, zo = 0,
        xd = 0, yd = 0, zd = 0;
    private final Matrix4f mvp = new Matrix4f();
    private IndexedTesselator3 it;
    private Tesselator3 t;
    private Texture2D sth;

    @Override
    public void create() {
        input.register((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) {
                    window.closeWindow();
                }
                if (key == GLFW_KEY_GRAVE_ACCENT) {
                    window.setGrabbed(!window.isGrabbed());
                }
            }
        });
        input.register((hWnd, xp, yp) -> {
            if (window.isGrabbed()) {
                xRot -= input.getDeltaMY() * 0.15;
                yRot += input.getDeltaMX() * 0.15;
                if (xRot < -90) {
                    xRot = -90;
                } else if (xRot > 90) {
                    xRot = 90;
                }
            }
        });
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        enableDepthTest();
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
        sth = new Texture2D(ClassLoader.getSystemClassLoader(),
            "tstest.png",
            new MipmapMode()
                .minFilter(GL_NEAREST)
                .magFilter(GL_NEAREST));
    }

    @Override
    public void render() {
        Framebuffer fb = GameEngine.framebuffer;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        float delta = timer.getDelta();
        float tx = xo + (x - xo) * delta;
        float ty = yo + (y - yo) * delta;
        float tz = zo + (z - zo) * delta;
        it.setMatrix(rotateY(
            rotateX(
                setPerspective(mvp, 90, fb, 0.05f, 1000)
                    .translate(0, 0, -0.3f),
                -xRot),
            yRot).translate(-tx, -ty, -tz));
        it.draw();
        glClear(GL_DEPTH_BUFFER_BIT);
        sth.bind();
        t.setMatrix(mvp.setOrtho2D(0, fb.width(), fb.height(), 0));
        t.draw();
        sth.unbind();
        super.render();
    }

    @Override
    public void tick() {
        super.tick();
        xo = x;
        yo = y;
        zo = z;
        float xa = 0, ya = 0, za = 0;
        if (input.pressed(GLFW_KEY_W)) {
            --za;
        }
        if (input.pressed(GLFW_KEY_S)) {
            ++za;
        }
        if (input.pressed(GLFW_KEY_A)) {
            --xa;
        }
        if (input.pressed(GLFW_KEY_D)) {
            ++xa;
        }
        if (input.pressed(GLFW_KEY_LEFT_SHIFT)) {
            --ya;
        }
        if (input.pressed(GLFW_KEY_SPACE)) {
            ++ya;
        }
        float dist = xa * xa + ya * ya + za * za;
        if (dist >= 0.01) {
            dist = (float) (0.1 / sqrt(dist));
            xa *= dist;
            ya *= dist;
            za *= dist;
            float sin = (float) sin(toRadians(yRot));
            float cos = (float) cos(toRadians(yRot));
            xd += xa * cos - za * sin;
            yd += ya;
            zd += za * cos + xa * sin;
        }
        x += xd;
        y += yd;
        z += zd;
        xd *= 0.91 * 0.7;
        yd *= 0.98 * 0.65;
        zd *= 0.91 * 0.7;
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        super.resize(width, height);
    }

    @Override
    public void onUpdated() {
        super.onUpdated();
        window.setTitle("Tesselator3 Test FPS:" + graphics.getFps());
    }

    @Override
    public void free() {
        sth.free();
        it.free();
        t.free();
    }

    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        config.width = 854;
        config.height = 480;
        config.title = "Tesselator3 Test";
        config.glVersion = 3.3;
        config.coreProfile = true;
        new GameApp(new Tesselator3Test(), config);
    }
}
