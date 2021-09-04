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

import static org.joml.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.overrun.commonutils.Math.isNotZero;

/**
 * @author squid233
 */
public class Player {
    //private static final int RATE = 8;
    public static final float STEP = 0.02f;
    public float x = 1.5f;
    public float y = 1.5f;
    public float z = 4.5f;
    public float xa;
    public float ya;
    public float za;
    /*public float xv;
    public float yv;
    public float zv;
    private int xvRate = RATE;
    private int yvRate = RATE;
    private int zvRate = RATE;
    private float lastSpeed;*/
    public float xRot, yRot;

    public boolean pressing(int key) {
        return glfwGetKey(glfwGetCurrentContext(), key) == GLFW_PRESS;
    }

    public void tick() {
        xa = 0;
        ya = 0;
        za = 0;
        if (pressing(GLFW_KEY_W)) {
            ++za;
        }
        if (pressing(GLFW_KEY_S)) {
            --za;
        }
        if (pressing(GLFW_KEY_A)) {
            --xa;
        }
        if (pressing(GLFW_KEY_D)) {
            ++xa;
        }
        if (pressing(GLFW_KEY_LEFT_SHIFT)) {
            --ya;
        }
        if (pressing(GLFW_KEY_SPACE)) {
            ++ya;
        }
        moveRelative(pressing(GLFW_KEY_LEFT_CONTROL) || pressing(GLFW_KEY_RIGHT_CONTROL)
                ? STEP * 2
                : STEP);
    }

    public void moveRelative(float speed) {
        /*if (speed != lastSpeed) {
            xv = 0;
            yv = 0;
            zv = 0;
            xvRate = RATE;
            yvRate = RATE;
            zvRate = RATE;
        }
        lastSpeed = speed;*/
        if (isNotZero(xa)) {
            /*if (--xvRate != 0 && xv < speed) {
                xv += speed / xvRate;
            }*/
            x -= sin(toRadians(yRot - 90)) * xa * speed /* xv*/;
            z += cos(toRadians(yRot - 90)) * xa * speed /* xv*/;
        }
        /*else {
            xv = 0;
            xvRate = RATE;
        }*/
        if (isNotZero(ya)) {
            /*if (--yvRate != 0 && yv < speed) {
                yv += speed / yvRate;
            }*/
            y += ya * speed /* yv*/;
        }
        /*else {
            yv = 0;
            yvRate = RATE;
        }*/
        if (isNotZero(za)) {
            /*if (--zvRate != 0 && zv < speed) {
                zv += speed / zvRate;
            }*/
            x += sin(toRadians(yRot)) * za * speed /* zv*/;
            z -= cos(toRadians(yRot)) * za * speed /* zv*/;
        }
        /*else {
            zv = 0;
            zvRate = RATE;
        }*/
    }

    public void rotate(double xOffset, double yOffset) {
        xRot += yOffset;
        yRot += xOffset;
        if (xRot > 90) {
            xRot = 90;
        } else if (xRot < -90) {
            xRot = -90;
        }
        if (yRot >= 360) {
            yRot = 0;
        } else if (yRot <= 0) {
            yRot = 360;
        }
    }
}
