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

import org.overrun.glutils.IDrawable;
import org.overrun.glutils.mesh.IMesh;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjModel<T extends IMesh> implements IDrawable {
    private final T[] meshes;
    private MeshProcessor<T> processor;

    /**
     * construct
     *
     * @param meshes    meshes
     * @param processor mesh processor
     */
    public ObjModel(T[] meshes,
                    MeshProcessor<T> processor) {
        this.meshes = meshes;
        this.processor = processor;
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
     * Set mesh processor
     *
     * @param processor pre render
     * @since 2.0.0
     */
    public void setProcessor(MeshProcessor<T> processor) {
        this.processor = processor;
    }

    /**
     * get meshes
     *
     * @return {@link #meshes}
     * @since 2.0.0
     */
    public T[] getMeshes() {
        return meshes;
    }

    /**
     * Processing mesh
     * <p>
     * function: {@link MeshProcessor#process process}
     * </p>
     *
     * @author squid233
     * @since 2.0.0
     */
    @FunctionalInterface
    public interface MeshProcessor<T extends IMesh> {
        /**
         * Set uniforms before render.
         *
         * @param mesh Mesh to set
         */
        void process(T mesh);
    }

    @Override
    public void render() {
        for (T mesh : meshes) {
            if (processor != null) {
                processor.process(mesh);
            }
            mesh.render();
        }
    }

    public void free() {
        for (T mesh : meshes) {
            mesh.free();
        }
    }
}
