/*
 * MIT License
 *
 * Copyright (c) 2021 OverRun Organization
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 0.3.0
 */
public abstract class AtlasLoom<T> {
    protected final Map<String, T> imageMap = new HashMap<>();
    protected final Map<String, UV> uvMap = new HashMap<>();
    protected int width, maxH;
    protected int atlasId;

    public static AtlasLoomAWT getAWT() {
        return new AtlasLoomAWT();
    }

    public abstract void load(String name,
                     ClassLoader loader,
                     int defaultW,
                     int defaultH,
                     int mode,
                     String... images);

    protected void addImg(String img) {
        if (img != null) {
            imageMap.put(img, null);
        }
    }

    public int getAtlasId() {
        return atlasId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return maxH;
    }

    public float getU0(String id) {
        return (float) uvMap.get(id).u0 / (float) width;
    }

    public float getU1(String id) {
        return (float) uvMap.get(id).u1 / (float) width;
    }

    public float getV0(String id) {
        return (float) uvMap.get(id).v0 / (float) maxH;
    }

    public float getV1(String id) {
        return (float) uvMap.get(id).v1 / (float) maxH;
    }

    public static class UV {
        public final int u0, v0 = 0;
        public final int u1, v1;

        public UV(int u0,
                  int u1, int v1) {
            this.u0 = u0;
            this.u1 = u1;
            this.v1 = v1;
        }
    }
}