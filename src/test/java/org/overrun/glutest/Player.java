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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 */
public class Player {
    public static final float SPEED = 0.1f;
    public float
        x = 1.5f,
        y = 1.5f,
        z = 4.5f,
        xo,
        yo,
        zo,
        xd,
        yd,
        zd;
    public float xRot, yRot;

    public boolean pressing(int key) {
        return glfwGetKey(glfwGetCurrentContext(), key) == GLFW_PRESS;
    }

    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        float xa = 0, ya = 0, za = 0;
        if (pressing(GLFW_KEY_W)) {
            --za;
        }
        if (pressing(GLFW_KEY_S)) {
            ++za;
        }
        if (pressing(GLFW_KEY_A)) {
            --xa;
        }
        if (pressing(GLFW_KEY_D)) {
            ++xa;
        }
        if (pressing(GLFW_KEY_LEFT_SHIFT)
            || pressing(GLFW_KEY_RIGHT_SHIFT)) {
            --ya;
        }
        if (pressing(GLFW_KEY_SPACE)) {
            ++ya;
        }
        moveRelative(pressing(GLFW_KEY_LEFT_CONTROL)
                || pressing(GLFW_KEY_RIGHT_CONTROL)
                ? SPEED * 2
                : SPEED,
            xa,
            ya,
            za);
        x += xd;
        y += yd;
        z += zd;
        xd *= 0.91 * 0.7;
        yd *= 0.98 * 0.65;
        zd *= 0.91 * 0.7;
    }

    public void moveRelative(float speed,
                             float xa,
                             float ya,
                             float za) {
        float dist = xa * xa + ya * ya + za * za;
        if (dist >= 0.01) {
            dist = (float) (speed / sqrt(dist));
            xa *= dist;
            ya *= dist;
            za *= dist;
            float sin = (float) sin(toRadians(yRot));
            float cos = (float) cos(toRadians(yRot));
            xd += xa * cos - za * sin;
            yd += ya;
            zd += za * cos + xa * sin;
        }
    }

    public void turn(double xOffset, double yOffset) {
        xRot -= yOffset;
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
