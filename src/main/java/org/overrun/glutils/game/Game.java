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

import org.overrun.glutils.timer.TimerID;

import static org.overrun.glutils.game.GameEngine.bufFrame;

/**
 * The game
 *
 * @author squid233
 * @since 1.5.0
 */
public class Game implements IGameLogic {
    /**
     * The screen
     */
    public Screen screen;

    /**
     * Open the screen
     * <p>
     * The steps of opening a new screen:
     * <ul>
     *     <li>{@link Screen#free Close} the old screen</li>
     *     <li>{@link Screen#resize Resize} the new screen</li>
     *     <li>{@link Screen#create Create} the new screen</li>
     * </ul>
     *
     * @param s The new screen
     */
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
    public void tick(TimerID timerID) {
        if (screen != null) {
            screen.tick(timerID);
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
    public void passedFrame() {
        if (screen != null) {
            screen.passedFrame();
        }
    }

    @Override
    public void cursorPosCb(final int x,
                            final int y) {
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
    public void mousePressed(final int button,
                             final int mods) {
        if (screen != null) {
            screen.mousePressed(button, mods);
        }
    }

    @Override
    public void mouseReleased(final int button,
                              final int mods) {
        if (screen != null) {
            screen.mouseReleased(button, mods);
        }
    }

    @Override
    public void mouseWheel(final double xo,
                           final double yo) {
        if (screen != null) {
            screen.mouseWheel(xo, yo);
        }
    }

    @Override
    public void inputChar(final int codepoint) {
        if (screen != null) {
            screen.inputChar(codepoint);
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
