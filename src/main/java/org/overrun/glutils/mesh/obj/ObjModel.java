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

package org.overrun.glutils.mesh.obj;

import org.overrun.glutils.Drawable;
import org.overrun.glutils.mesh.IMesh;

import java.util.function.Consumer;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjModel<T extends IMesh> implements Drawable, AutoCloseable {
    private final T[] meshes;
    private PreRender<T> preRender;

    /**
     * construct
     *
     * @param meshes    meshes
     * @param preRender pre render
     */
    public ObjModel(T[] meshes, PreRender<T> preRender) {
        this.meshes = meshes;
        this.preRender = preRender;
    }

    /**
     * construct
     *
     * @param meshes meshes
     */
    public ObjModel(T[] meshes) {
        this.meshes = meshes;
    }

    /**
     * set pre render
     *
     * @param preRender pre render
     */
    public void setPreRender(PreRender<T> preRender) {
        this.preRender = preRender;
    }

    /**
     * get meshes
     *
     * @return {@link #meshes}
     */
    public T[] getMeshes() {
        return meshes;
    }

    /**
     * pre render
     * <p>
     * function: {@link PreRender#accept accept}
     *
     * @author squid233
     */
    @FunctionalInterface
    public interface PreRender<T extends IMesh>
            extends Consumer<T> {
        /**
         * Set uniforms before render.
         *
         * @param mesh Mesh to set
         */
        void accept(T mesh);
    }

    @Override
    public void render() {
        for (T mesh : meshes) {
            if (preRender != null) {
                preRender.accept(mesh);
            }
            mesh.render();
        }
    }

    @Override
    public void close() {
        for (T mesh : meshes) {
            mesh.close();
        }
    }
}
