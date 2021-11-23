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

import static org.overrun.glutils.game.GameEngine.framebuffer;

/**
 * @author squid233
 * @since 1.5.0
 */
public class Game implements GameLogic {
    public Screen screen;

    public void openScreen(Screen s) {
        if (screen != null) {
            screen.close();
        }
        screen = s;
        if (s != null) {
            s.resize(framebuffer.width(), framebuffer.height());
            s.create();
        }
    }

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
    public void onUpdated() {
        if (screen != null) {
            screen.onUpdated();
        }
    }

    @Override
    public void tick() {
        if (screen != null) {
            screen.tick();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (screen != null) {
            screen.resize(width, height);
        }
    }

    @Override
    public void free() {
    }
}
