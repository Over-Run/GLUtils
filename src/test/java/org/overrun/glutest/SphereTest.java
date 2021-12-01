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

import org.overrun.glutils.draw.Direction;
import org.overrun.glutils.draw.Drawer;
import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.ll.LGLRenderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GameEngine.input;

/**
 * @author squid233
 */
public class SphereTest extends Game {
    private int sphere2DX;
    private int sphere2DY;
    private int sphere2DZ;
    private float xr, yr, zr;

    @Override
    public void create() {
        sphere2DX = glGenLists(1);
        sphere2DY = glGenLists(1);
        sphere2DZ = glGenLists(1);
        glNewList(sphere2DX, GL_COMPILE);
        glBegin(GL_POLYGON);
        Drawer.drawCircle(32, 180, Direction.NORTH, LGLRenderer.getInstance());
        glEnd();
        glEndList();
        glNewList(sphere2DY, GL_COMPILE);
        glBegin(GL_POLYGON);
        Drawer.drawCircle(32, 180, Direction.WEST, LGLRenderer.getInstance());
        glEnd();
        glEndList();
        glNewList(sphere2DZ, GL_COMPILE);
        glBegin(GL_POLYGON);
        Drawer.drawCircle(32, 180, Direction.UP, LGLRenderer.getInstance());
        glEnd();
        glEndList();
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPushMatrix();
        glRotatef(xr, 1, 0, 0);
        glRotatef(yr, 0, 1, 0);
        glRotatef(zr, 0, 0, 1);
        glTranslatef(100, 100, 0);
        glCallList(sphere2DX);
        glTranslatef(100, 100, 0);
        glCallList(sphere2DY);
        glTranslatef(100, 100, 0);
        glCallList(sphere2DZ);
        glPopMatrix();
        super.render();
    }

    @Override
    public void tick() {
        if (input.keyPressed(GLFW_KEY_W)) {
            --xr;
        }
        if (input.keyPressed(GLFW_KEY_S)) {
            ++xr;
        }
        if (input.keyPressed(GLFW_KEY_F)) {
            --yr;
        }
        if (input.keyPressed(GLFW_KEY_G)) {
            ++yr;
        }
        if (input.keyPressed(GLFW_KEY_A)) {
            --zr;
        }
        if (input.keyPressed(GLFW_KEY_D)) {
            ++zr;
        }
        super.tick();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1000, 1000);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        super.resize(width, height);
    }

    @Override
    public void free() {
        glDeleteLists(sphere2DX, 1);
        glDeleteLists(sphere2DY, 1);
        glDeleteLists(sphere2DZ, 1);
    }

    public static void main(String[] args) {
        new GameApp(new SphereTest(), new GameConfig());
    }
}
