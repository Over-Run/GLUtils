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

package org.overrun.glutils.game;

import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * @author squid233
 * @since 1.5.0
 */
public class GameConfig {
    /**
     * Default window width
     */
    public int width = 800;
    /**
     * Default window height
     */
    public int height = 600;
    /**
     * Default window title
     */
    public String title = "GLUtils Game";
    /**
     * Minimum required OpenGL version
     */
    public double glVersion = 1.1;
    /**
     * Option to enable core profile
     */
    public boolean coreProfile = false;
    /**
     * Set whether the window is visible before initialization.
     * <p>
     * If set to true, the window will be visible and display a white screen
     * without responding.
     * </p>
     */
    public boolean visibleBeforeInitialization = false;
    /**
     * Set ticks per seconds.
     */
    public float tps = 20;
    /**
     * Option to enable vertical synchronize.
     * <p>
     * <b>Note:</b> Don't confuse with {@link Graphics#vSync}
     * </p>
     */
    public boolean vSync = false;
    /**
     * Set GLFW error callback.
     */
    public GLFWErrorCallback errorCallback =
        GLFWErrorCallback.createPrint();
}
