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

package org.overrun.glutils.game;

import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.overrun.glutils.game.GameEngine.app;

/**
 * @author squid233
 * @since 1.5.0
 */
public class Graphics {
    /**
     * Current frames per seconds
     */
    protected int fps;
    /**
     * State of vertical synchronizing.
     * <p>
     * <b>Note:</b> Don't confuse with {@link GameConfig#vSync}
     * </p>
     */
    protected boolean vSync = app.config.vSync;

    /**
     * Get current frames per seconds
     *
     * @return {@link #fps}
     */
    public int getFps() {
        return fps;
    }

    public boolean isVSync() {
        return vSync;
    }

    public Graphics setVSync(boolean vSync) {
        this.vSync = vSync;
        glfwSwapInterval(vSync ? 1 : 0);
        return this;
    }
}
