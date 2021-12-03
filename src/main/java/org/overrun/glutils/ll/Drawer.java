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

package org.overrun.glutils.ll;

import org.overrun.glutils.Direction;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.glVertex3d;

/**
 * @author squid233
 * @since 1.5.0
 */
public class Drawer {
    /**
     * Draw a circle with specified radius.
     *
     * @param r           The radius of the circle.
     * @param vertexCount The vertex count of the circle.
     * @param dir         The direction of the circle.
     */
    public static void drawCircle(final double r,
                                  final int vertexCount,
                                  final Direction dir) {
        final double C = 2 * PI;
        for (int i = 0; i < vertexCount; i++) {
            final double d = C * i / vertexCount;
            switch (dir) {
                case NORTH:
                case SOUTH:
                    glVertex3d(r * cos(d),
                        r * sin(d),
                        0);
                    break;
                case WEST:
                case EAST:
                    glVertex3d(0,
                        r * sin(d),
                        r * sin(d));
                    break;
                case UP:
                case DOWN:
                    glVertex3d(r * cos(d),
                        0,
                        r * cos(d));
                    break;
            }
        }
    }
}
