/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Overrun Organization
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

package org.overrun.glutils.timer;

/**
 * @author squid233
 * @since 1.5.0
 */
public abstract class AbstractTimer implements ITimer {
    protected static final int MAX_TICKS_PER_UPDATE = 100;
    public float tps;
    public int ticks;
    public float delta;
    public float timeScale = 1;
    public float fps = 0;
    public float passedTime = 0;

    public AbstractTimer(float tps) {
        this.tps = tps;
    }

    @Override
    public float getTps() {
        return tps;
    }

    @Override
    public void setTps(float tps) {
        this.tps = tps;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public float getDelta() {
        return delta;
    }
}
