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

import org.joml.Vector4f;

/**
 * @author squid233
 * @since 1.2.0
 */
public class Material {
    public static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);
    private Vector4f ambientColor = DEFAULT_COLOR;
    private Vector4f diffuseColor = DEFAULT_COLOR;
    private Vector4f specularColor = DEFAULT_COLOR;
    private float reflectance = 0;
    private int texture = 0;

    /**
     * construct
     */
    public Material() {
    }

    public Material(Vector4f color,
                    float reflectance) {
        this(color, color, color, 0, reflectance);
    }

    public Material(int texture) {
        this.texture = texture;
    }

    public Material(int texture,
                    float reflectance) {
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Material(Vector4f ambientColor,
                    Vector4f diffuseColor,
                    Vector4f specularColor,
                    int texture,
                    float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    /**
     * get ambient color
     *
     * @return {@link #ambientColor}
     */
    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    /**
     * set ambient color
     */
    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    /**
     * get diffuse color
     *
     * @return {@link #diffuseColor}
     */
    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    /**
     * set diffuse color
     */
    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    /**
     * get specular color
     *
     * @return {@link #specularColor}
     */
    public Vector4f getSpecularColor() {
        return specularColor;
    }

    /**
     * set specular color
     */
    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    /**
     * get reflectance
     *
     * @return {@link #reflectance}
     */
    public float getReflectance() {
        return reflectance;
    }

    /**
     * set reflectance
     */
    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    /**
     * get texture
     *
     * @return {@link #texture}
     */
    public int getTexture() {
        return texture;
    }

    /**
     * set texture
     */
    public void setTexture(int texture) {
        this.texture = texture;
    }

    /**
     * is textured
     *
     * @return {@link #texture} != 0
     */
    public boolean isTextured() {
        return texture != 0;
    }
}
