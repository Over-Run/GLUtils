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

package org.overrun.glutils.timer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * @author squid233
 * @since 1.5.0
 */
public class GLFWTimer extends AbstractTimer {
    private static final double MAX_SECONDS_PER_UPDATE = 1.0;
    private static final int MAX_TICKS_PER_UPDATE = 100;
    private double lastTime = glfwGetTime();

    public GLFWTimer(float tps) {
        super(tps);
    }

    @Override
    public void advanceTime() {
        double now = glfwGetTime();
        double passedS = now - lastTime;
        lastTime = now;
        if (passedS < 0.0) {
            passedS = 0.0;
        }
        if (passedS > MAX_SECONDS_PER_UPDATE) {
            passedS = MAX_SECONDS_PER_UPDATE;
        }
        fps = (float) (MAX_SECONDS_PER_UPDATE / passedS);
        passedTime += (float) passedS * timeScale * tps;
        ticks = (int) passedTime;
        if (ticks > MAX_TICKS_PER_UPDATE) {
            ticks = MAX_TICKS_PER_UPDATE;
        }
        passedTime -= ticks;
        delta = passedTime;
    }

    @Override
    public double getCurrTime() {
        return lastTime;
    }
}
