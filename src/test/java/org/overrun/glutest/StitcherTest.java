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

package org.overrun.glutest;

import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.gl.ll.Tesselator;
import org.overrun.glutils.tex.TexParam;
import org.overrun.glutils.tex.stitch.Stitcher;
import org.overrun.glutils.tex.stitch.StrSpriteAtlas;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GameEngine.bufFrame;

/**
 * @author squid233
 */
public class StitcherTest extends Game {
    private StrSpriteAtlas atlas;

    @Override
    public void create() {
        atlas = Stitcher.stitch(this,
            TexParam.glNearest(),
            "stitch/grassblock.png",
            "stitch/stone.png",
            "stitch/dirt.png",
            "missingo");
        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void render() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, bufFrame.width(), bufFrame.height(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glClear(GL_COLOR_BUFFER_BIT);
        var t = Tesselator.getInstance();
        atlas.bind();
        t.init(GL_QUADS)
            .vertexUV(0, 0, 0, 0, 0)
            .vertexUV(0, atlas.height(), 0, 0, 1)
            .vertexUV(atlas.width(), atlas.height(), 0, 1, 1)
            .vertexUV(atlas.width(), 0, 0, 1, 0)
            .draw();
        atlas.unbind();
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        super.resize(width, height);
    }

    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        new GameApp(new StitcherTest(), config);
    }
}
