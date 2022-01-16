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
public class SystemTimer extends AbstractTimer {
    public static final long NS_PER_SECOND = 1_000_000_000L;
    public static final long MAX_NS_PER_UPDATE = 1_000_000_000L;
    private long lastTime = System.nanoTime();

    /**
     * Construct with {@link #tps}
     *
     * @param tps {@link #tps}
     */
    public SystemTimer(double tps) {
        super(tps);
    }

    @Override
    public void advanceTime() {
        var now = System.nanoTime();
        var passedNs = now - lastTime;
        lastTime = now;
        if (passedNs < 0L) {
            passedNs = 0L;
        }
        if (passedNs > MAX_NS_PER_UPDATE) {
            passedNs = MAX_NS_PER_UPDATE;
        }
        fps = (double) MAX_NS_PER_UPDATE / passedNs;
        passedTime += passedNs * timeScale * tps / (double) NS_PER_SECOND;
        ticks = (int) passedTime;
        if (ticks > MAX_TICKS_PER_UPDATE) {
            ticks = MAX_TICKS_PER_UPDATE;
        }
        passedTime -= ticks;
        delta = passedTime;
    }

    @Override
    public double getCurrTime() {
        return lastTime / (double) NS_PER_SECOND;
    }
}
