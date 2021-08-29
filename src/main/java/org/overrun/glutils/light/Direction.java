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

package org.overrun.glutils.light;

import org.joml.Vector3f;

/**
 * @author squid233
 * @since 1.2.0
 */
public enum Direction {
    UP(0, 1, 0, 1, 0),
    DOWN(1, 0, 0, -1, 0),
    NORTH(2, 3, 0, 0, -1),
    SOUTH(3, 2, 0, 0, 1),
    WEST(4, 5, -1, 0, 0),
    EAST(5, 4, 1, 0, 0);

    private final int id;
    private final int oppositeId;
    private final int axisX;
    private final int axisY;
    private final int axisZ;

    Direction(int id,
              int oppositeId,
              int axisX,
              int axisY,
              int axisZ) {
        this.id = id;
        this.oppositeId = oppositeId;
        this.axisX = axisX;
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    /**
     * get direction by id
     *
     * @param id direction id
     * @return a direction
     */
    public static Direction getById(int id) {
        return values()[id];
    }

    /**
     * get id
     *
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * get opposite id
     *
     * @return {@link #oppositeId}
     */
    public int getOppositeId() {
        return oppositeId;
    }

    /**
     * get opposite direction
     *
     * @return opposite direction
     */
    public Direction opposite() {
        return getById(oppositeId);
    }

    /**
     * get axisX
     *
     * @return {@link #axisX}
     */
    public int getAxisX() {
        return axisX;
    }

    /**
     * get axisY
     *
     * @return {@link #axisY}
     */
    public int getAxisY() {
        return axisY;
    }

    /**
     * get axisZ
     *
     * @return {@link #axisZ}
     */
    public int getAxisZ() {
        return axisZ;
    }

    /**
     * convert to vector
     *
     * @return Vector3f contains vec xyz
     */
    public Vector3f toVector() {
        return new Vector3f(axisX, axisY, axisZ);
    }
}
