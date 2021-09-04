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
public class DirectionalLight {
    private Vector3f color;
    private Vector3f direction;
    private float intensity;

    /**
     * construct
     *
     * @param color     light color
     * @param direction light direction
     * @param intensity light intensity
     */
    public DirectionalLight(Vector3f color,
                            Vector3f direction,
                            float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    /**
     * copy light
     *
     * @param light light for copying
     */
    @SuppressWarnings("CopyConstructorMissesField")
    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), light.getDirection(), light.getIntensity());
    }

    /**
     * get color
     *
     * @return {@link #color}
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * set color
     *
     * @param color light color
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * get direction
     *
     * @return {@link #direction}
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * set direction
     *
     * @param direction light direction
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    /**
     * get intensity
     *
     * @return {@link #intensity}
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * set intensity
     *
     * @param intensity light intensity
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
