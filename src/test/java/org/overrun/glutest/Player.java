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

import static org.overrun.glutils.math.Math.*;

/**
 * @author squid233
 */
public class Player {
    public float x = 1.5f;
    public float y = 1.5f;
    public float z = 4.5f;
    public float xRot, yRot;

    public void moveRelative(float xo, float yo, float zo) {
        if (isNotZero(xo)) {
            x -= sin(toRadians(yRot - 90)) * xo;
            z += cos(toRadians(yRot - 90)) * xo;
        }
        if (isNotZero(yo)) {
            y += yo;
        }
        if (isNotZero(zo)) {
            x += sin(toRadians(yRot)) * zo;
            z -= cos(toRadians(yRot)) * zo;
        }
    }

    public void rotate(double xOffset, double yOffset) {
        xRot += yOffset;
        yRot += xOffset;
        if (xRot > 90) {
            xRot = 90;
        }
        if (xRot < -90) {
            xRot = -90;
        }
    }
}
