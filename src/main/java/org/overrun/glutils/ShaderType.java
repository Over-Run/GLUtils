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

package org.overrun.glutils;

import static org.lwjgl.opengl.GL32.*;

/**
 * @author squid233
 * @since 0.1.0
 */
public enum ShaderType {
    /**
     * fragmentShader
     */
    FRAGMENT_SHADER(GL_FRAGMENT_SHADER),
    /**
     * vertexShader
     */
    VERTEX_SHADER(GL_VERTEX_SHADER),
    /**
     * geometryShader
     */
    GEOMETRY_SHADER(GL_GEOMETRY_SHADER);

    /**
     * GL type constant
     */
    private final int type;

    ShaderType(int type) {
        this.type = type;
    }

    /**
     * get gl constant
     *
     * @return gl constant
     */
    public int getType() {
        return type;
    }

    /**
     * get name
     *
     * @return name
     */
    public String getName() {
        return name().toLowerCase().replace("_", " ");
    }

    @Override
    public String toString() {
        return getName();
    }
}
