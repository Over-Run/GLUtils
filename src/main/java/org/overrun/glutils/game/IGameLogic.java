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

/**
 * @author squid233
 * @since 1.5.0
 */
public interface IGameLogic {
    void create();

    /**
     * Rendering.
     * <p>
     * This method call per frames.
     * </p>
     */
    void render();

    /**
     * Ticking.
     * <p>
     * This method call per ticks.
     * </p>
     *
     * @see GameConfig#tps
     */
    default void tick() {
    }

    /**
     * Resizing framebuffer.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    default void resize(final int width,
                        final int height) {
    }

    /**
     * Called after polling events.
     */
    default void onUpdated() {
    }

    /**
     * Called on cursor pos callback.
     *
     * @param x New pos x.
     * @param y New pos y.
     */
    default void cursorPosCb(final int x,
                             final int y) {
    }

    /**
     * Called on key pressed.
     *
     * @param key      The key.
     * @param scancode The scancode.
     * @param mods     The modifiers.
     */
    default void keyPressed(final int key,
                            final int scancode,
                            final int mods) {
    }

    /**
     * Called on key released.
     *
     * @param key      The key.
     * @param scancode The scancode.
     * @param mods     The modifiers.
     */
    default void keyReleased(final int key,
                             final int scancode,
                             final int mods) {
    }

    /**
     * Called on key repeated.
     *
     * @param key      The key.
     * @param scancode The scancode.
     * @param mods     The modifiers.
     */
    default void keyRepeated(final int key,
                             final int scancode,
                             final int mods) {
    }

    void free();
}
