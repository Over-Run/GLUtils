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

package org.overrun.glutils.tex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 0.3.0
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public abstract class AtlasLoom<T> {
    /**
     * string id to images obj
     */
    protected final Map<String, T> imageMap = new HashMap<>();
    /**
     * texture UVs
     */
    protected final Map<String, UV> uvMap = new HashMap<>();
    /**
     * atlas name
     */
    protected final String name;
    /**
     * atlas width
     */
    protected int width;
    /**
     * atlas height
     */
    protected int height;
    /**
     * texture id of atlas
     */
    protected int atlasId;

    /**
     * constructor
     *
     * @param name target id
     * @since 0.4.0
     */
    public AtlasLoom(String name) {
        this.name = name;
    }

    /**
     * gen by AWT
     *
     * @param name target id
     * @return {@link AtlasLoomAWT}
     * @since 0.4.0
     */
    public static AtlasLoomAWT awt(String name) {
        return new AtlasLoomAWT(name);
    }

    /**
     * gen by STB
     *
     * @param name target id
     * @return {@link AtlasLoomSTB}
     * @since 0.4.0
     */
    public static AtlasLoomSTB stb(String name) {
        return new AtlasLoomSTB(name);
    }

    /**
     * load
     *
     * @param loader   class loader
     * @param defaultW default width
     * @param defaultH default height
     * @param param    Texture parameters
     * @param images   images
     * @return {@link #atlasId}
     * @since 2.0.0
     */
    public abstract int load(ClassLoader loader,
                             int defaultW,
                             int defaultH,
                             TexParam param,
                             String... images);

    /**
     * add image
     *
     * @param img image
     */
    protected void addImg(String img) {
        if (img != null) {
            imageMap.put(img, null);
        }
    }

    /**
     * get atlas id
     *
     * @return {@link #atlasId}
     */
    public int getAtlasId() {
        return atlasId;
    }

    /**
     * get atlas width
     *
     * @return {@link #width}
     */
    public int getWidth() {
        return width;
    }

    /**
     * get atlas height
     *
     * @return {@link #height}
     */
    public int getHeight() {
        return height;
    }

    /**
     * get texture u0
     *
     * @param id sprite id
     * @return left-top texture coordinate x
     */
    public float getU0(String id) {
        return (float) uvMap.get(id).u0 / (float) width;
    }

    /**
     * get texture u1
     *
     * @param id sprite id
     * @return right-bottom texture coordinate x
     */
    public float getU1(String id) {
        return (float) uvMap.get(id).u1 / (float) width;
    }

    /**
     * get texture v0
     *
     * @param id sprite id
     * @return left-top texture coordinate y
     */
    public float getV0(String id) {
        return (float) uvMap.get(id).v0 / (float) height;
    }

    /**
     * get texture v1
     *
     * @param id sprite id
     * @return right-bottom texture coordinate y
     */
    public float getV1(String id) {
        return (float) uvMap.get(id).v1 / (float) height;
    }

    /**
     * texture uv
     *
     * @author squid233
     * @since 0.3.0
     */
    public static class UV {
        /**
         * origin point
         */
        public final int u0, v0;
        /**
         * end point
         */
        public final int u1, v1;

        /**
         * construct
         *
         * @param u0 u0
         * @param v0 v0
         * @param u1 u1
         * @param v1 v1
         */
        public UV(int u0, int v0,
                  int u1, int v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
        }
    }
}
