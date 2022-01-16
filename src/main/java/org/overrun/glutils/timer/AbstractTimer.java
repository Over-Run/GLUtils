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
 * The abstract layer of {@link ITimer}
 *
 * @author squid233
 * @since 1.5.0
 */
public abstract class AbstractTimer implements ITimer {
    /**
     * Max tick count per update
     */
    protected static final int MAX_TICKS_PER_UPDATE = 100;
    /**
     * Timer ticks per seconds.
     */
    public final double tps;
    /**
     * The tick count that should tick.
     */
    public int ticks;
    /**
     * The time since the last time.
     */
    public double delta;
    /**
     * The timer speed scale
     */
    public double timeScale = 1.0;
    /**
     * Frames per seconds
     */
    public double fps = 0.0;
    /**
     * The time since the last time.
     */
    public double passedTime = 0.0;

    /**
     * Construct with {@link #tps}
     *
     * @param tps {@link #tps}
     */
    public AbstractTimer(double tps) {
        this.tps = tps;
    }

    @Override
    public double getTps() {
        return tps;
    }

    @Override
    public double getTimeScale() {
        return timeScale;
    }

    @Override
    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public double getDelta() {
        return delta;
    }
}
