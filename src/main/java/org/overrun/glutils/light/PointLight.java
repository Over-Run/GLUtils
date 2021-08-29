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
public class PointLight {
    private Vector3f color;
    private Vector3f position;
    private float intensity;
    private Attenuation attenuation;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()),
                new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(),
                pointLight.getAttenuation());
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
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * get position
     *
     * @return {@link #position}
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * set position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
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
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
     * get attenuation
     *
     * @return {@link #attenuation}
     */
    public Attenuation getAttenuation() {
        return attenuation;
    }

    /**
     * set attenuation
     */
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {
        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        /**
         * get constant
         *
         * @return {@link #constant}
         */
        public float getConstant() {
            return constant;
        }

        /**
         * set constant
         */
        public void setConstant(float constant) {
            this.constant = constant;
        }

        /**
         * get linear
         *
         * @return {@link #linear}
         */
        public float getLinear() {
            return linear;
        }

        /**
         * set linear
         */
        public void setLinear(float linear) {
            this.linear = linear;
        }

        /**
         * get exponent
         *
         * @return {@link #exponent}
         */
        public float getExponent() {
            return exponent;
        }

        /**
         * set exponent
         */
        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }
}
