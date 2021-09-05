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

package org.overrun.glutils.mesh;

import org.overrun.commonutils.FloatArray;
import org.overrun.glutils.IVertexBuilder;

/**
 * @author squid233
 * @since 1.2.0
 */
public abstract class BaseMeshVertexBuilder implements IVertexBuilder, AutoCloseable {
    /**
     * vertices
     */
    protected final FloatArray vertices = new FloatArray();
    /**
     * vertex colors
     */
    protected final FloatArray colors = new FloatArray();
    /**
     * vertex textures
     */
    protected final FloatArray textureCoord = new FloatArray();
    /**
     * vertex normals
     */
    protected final FloatArray normals = new FloatArray();
    /**
     * Is changed vertex
     */
    protected boolean isDirty = false;
    /**
     * Is colored
     */
    protected boolean colored = false;
    /**
     * Is textured
     */
    protected boolean textured = false;
    /**
     * Is normalized
     */
    protected boolean normalized = false;

    /**
     * Render this mesh
     * <p>
     * <b>Note:</b> use {@link #getMesh()} to set mesh attrib index before call
     * this method.</p>
     *
     * @see #render(int)
     */
    @Override
    public abstract void render();

    /**
     * Render this mesh with specified geometry mode
     * <p>
     * <b>Note:</b> use {@link #getMesh()} to set mesh attrib index before call
     * this method.</p>
     *
     * @param mode rendering mode
     * @see #render()
     */
    @Override
    public abstract void render(int mode);

    /**
     * Get mesh object
     *
     * @return built mesh object
     */
    public abstract BaseMesh<?> getMesh();

    /**
     * Mark as changed
     */
    protected void markDirty() {
        isDirty = true;
    }

    @Override
    public BaseMeshVertexBuilder vertex(float x, float y, float z) {
        markDirty();
        vertices.addAll(x, y, z);
        return this;
    }

    @Override
    public BaseMeshVertexBuilder color(float r, float g, float b, float a) {
        markDirty();
        colored = true;
        colors.addAll(r, g, b, a);
        return this;
    }

    @Override
    public BaseMeshVertexBuilder texture(float x, float y) {
        markDirty();
        textured = true;
        textureCoord.addAll(x, y);
        return this;
    }

    @Override
    public BaseMeshVertexBuilder normal(float x, float y, float z) {
        markDirty();
        normalized = true;
        normals.addAll(x, y, z);
        return this;
    }

    @Override
    public abstract void close();
}
