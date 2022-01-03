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

import static org.overrun.glutils.game.GameEngine.bufFrame;

/**
 * @author squid233
 * @since 1.5.0
 */
public class Game implements IGameLogic {
    public Screen screen;

    public void openScreen(final Screen s) {
        if (screen != null) {
            screen.free();
        }
        screen = s;
        if (s != null) {
            s.resize(bufFrame.width(), bufFrame.height());
            s.create();
        }
    }

    /**
     * Creating game.
     * <p>
     * This method call after creating OpenGL context.
     * </p>
     */
    @Override
    public void create() {
    }

    @Override
    public void render() {
        if (screen != null) {
            screen.render();
        }
    }

    @Override
    public void tick() {
        if (screen != null) {
            screen.tick();
        }
    }

    @Override
    public void resize(final int width,
                       final int height) {
        if (screen != null) {
            screen.resize(width, height);
        }
    }

    @Override
    public void onUpdated() {
        if (screen != null) {
            screen.onUpdated();
        }
    }

    @Override
    public void cursorPosCb(int x, int y) {
        if (screen != null) {
            screen.cursorPosCb(x, y);
        }
    }

    @Override
    public void keyPressed(final int key,
                           final int scancode,
                           final int mods) {
        if (screen != null) {
            screen.keyPressed(key, scancode, mods);
        }
    }

    @Override
    public void keyReleased(final int key,
                            final int scancode,
                            final int mods) {
        if (screen != null) {
            screen.keyReleased(key, scancode, mods);
        }
    }

    @Override
    public void keyRepeated(final int key,
                            final int scancode,
                            final int mods) {
        if (screen != null) {
            screen.keyRepeated(key, scancode, mods);
        }
    }

    @Override
    public void mousePressed(int button, int mods) {
        if (screen != null) {
            screen.mousePressed(button, mods);
        }
    }

    @Override
    public void mouseReleased(int button, int mods) {
        if (screen != null) {
            screen.mouseReleased(button, mods);
        }
    }

    /**
     * Freeing.
     * <p>
     * This method call on exiting the game engine.
     * </p>
     */
    @Override
    public void free() {
    }
}
