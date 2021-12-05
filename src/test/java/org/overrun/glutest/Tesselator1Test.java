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

package org.overrun.glutest;

import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.ll.Tesselator;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 */
public class Tesselator1Test extends Game {
    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        Tesselator t = Tesselator.getInstance();
        t.init()
            .color(1, 0, 0).vertex(0, 0.5f, 0)
            .color(0, 1, 0).vertex(-0.5f, -0.5f, 0)
            .color(0, 0, 1).vertex(0.5f, -0.5f, 0)
            .draw(GL_TRIANGLES);
        super.render();
    }

    public static void main(String[] args) {
        new GameApp(new Tesselator1Test(), new GameConfig());
    }
}
