/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package org.overrun.glutest;

import org.overrun.glutils.timer.ITimer;
import org.overrun.glutils.timer.ITimerMgr;
import org.overrun.glutils.timer.SystemTimer;
import org.overrun.glutils.timer.TimerID;

/**
 * @author squid233
 */
public enum GLUTestTimers implements ITimerMgr, TimerID {
    PLAYER(0, new SystemTimer(20)),
    LIGHTING(1, new SystemTimer(20)),
    INSTANCE;

    public final int id;
    private SystemTimer timer;

    GLUTestTimers(int id,
                  SystemTimer timer) {
        this.id = id;
        this.timer = timer;
    }

    GLUTestTimers() {
        id = -1;
    }

    @Override
    public TimerID getID(int index) {
        return values()[index];
    }

    @Override
    public int getIDCount() {
        return 2;
    }

    @Override
    public ITimer get() {
        return timer;
    }
}
