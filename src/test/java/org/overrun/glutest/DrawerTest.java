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

import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.Direction.*;
import static org.overrun.glutils.game.GameEngine.input;
import static org.overrun.glutils.ll.Drawer.drawCircle;
import static org.overrun.glutils.ll.Drawer.drawRect;

/**
 * @author squid233
 */
public class DrawerTest extends Game {
    private int sphere2DX;
    private int sphere2DY;
    private int sphere2DZ;
    private float xr, yr, zr;
    private float x, y, z;

    @Override
    public void create() {
        sphere2DX = glGenLists(1);
        sphere2DY = glGenLists(1);
        sphere2DZ = glGenLists(1);
        glNewList(sphere2DX, GL_COMPILE);
        glBegin(GL_POLYGON);
        drawCircle(32, 80, SOUTH);
        glEnd();
        glEndList();
        glNewList(sphere2DY, GL_COMPILE);
        glBegin(GL_POLYGON);
        drawCircle(32, 80, WEST);
        glEnd();
        glEndList();
        glNewList(sphere2DZ, GL_COMPILE);
        glBegin(GL_POLYGON);
        drawCircle(32, 80, UP);
        glEnd();
        glEndList();
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glTranslatef(x, y, z);
        glRotatef(xr, 1, 0, 0);
        glRotatef(yr, 0, 1, 0);
        glRotatef(zr, 0, 0, 1);
        glPushMatrix();
        glTranslatef(100, 100, 0);
        glCallList(sphere2DX);
        glTranslatef(100, 100, 0);
        glCallList(sphere2DY);
        glTranslatef(100, 100, 0);
        glCallList(sphere2DZ);
        glPopMatrix();
        drawRect(0, 0, 100, 100);
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
        if (input.keyPressed(GLFW_KEY_SPACE)) {
            --y;
        }
        if (input.keyPressed(GLFW_KEY_LEFT_SHIFT)) {
            ++y;
        }
        if (input.keyPressed(GLFW_KEY_LEFT)) {
            --x;
        }
        if (input.keyPressed(GLFW_KEY_RIGHT)) {
            ++x;
        }
        if (input.keyPressed(GLFW_KEY_UP)) {
            --z;
        }
        if (input.keyPressed(GLFW_KEY_DOWN)) {
            ++z;
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
        super.resize(width, height);
    }

    @Override
    public void free() {
        glDeleteLists(sphere2DX, 1);
        glDeleteLists(sphere2DY, 1);
        glDeleteLists(sphere2DZ, 1);
    }

    public static void main(String[] args) {
        new GameApp(new DrawerTest(), new GameConfig());
    }
}
