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

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.overrun.glutils.game.GameEngine.window;

/**
 * @author squid233
 * @since 1.5.0
 */
public class Input {
    protected int mouseX, mouseY, deltaMX, deltaMY;

    /**
     * Is key pressed
     *
     * @param key The key
     * @return key pressed
     */
    public boolean keyPressed(final int key) {
        return window.key(key) == GLFW_PRESS;
    }

    /**
     * Is key released
     *
     * @param key The key
     * @return key released
     * @since 2.0.0
     */
    public boolean keyReleased(final int key) {
        return window.key(key) == GLFW_RELEASE;
    }

    /**
     * Is mouse button pressed
     *
     * @param button The mouse button
     * @return mouse button pressed
     * @since 2.0.0
     */
    public boolean mousePressed(final int button) {
        return window.mouse(button) == GLFW_PRESS;
    }

    /**
     * Is mouse button released
     *
     * @param button The mouse button
     * @return mouse button released
     * @since 2.0.0
     */
    public boolean mouseReleased(final int button) {
        return window.mouse(button) == GLFW_RELEASE;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getDeltaMX() {
        return deltaMX;
    }

    public int getDeltaMY() {
        return deltaMY;
    }
}
