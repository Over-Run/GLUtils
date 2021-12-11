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

import org.overrun.glutils.gui.FontTexture;
import org.overrun.glutils.gui.FontTextures;
import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.gl.ll.TextRenderer;

import java.awt.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.game.GLStateManager.enableBlend;
import static org.overrun.glutils.game.GLStateManager.enableTexture2D;
import static org.overrun.glutils.game.GameEngine.window;

/**
 * @author squid233
 */
public class TextRendererTest extends Game {
    private static final boolean btt = false;
    private int cx = 0, cy = 0;
    private FontTexture unifont;

    @Override
    public void create() {
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        enableTexture2D();
        enableBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        unifont = FontTextures.builder("unifont")
            .font(Font.decode("Unifont"))
            .charset(UTF_8)
            .padding(2)
            .build(false);
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPushMatrix();
        glTranslatef(cx, cy, 0);
        TextRenderer.drawText("Testing\nMulti lines", unifont, null, btt);
        glPopMatrix();
        super.render();
    }

    @Override
    public void tick() {
        super.tick();
        if (window.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            --cy;
        }
        if (window.getKey(GLFW_KEY_S) == GLFW_PRESS) {
            ++cy;
        }
        if (window.getKey(GLFW_KEY_A) == GLFW_PRESS) {
            --cx;
        }
        if (window.getKey(GLFW_KEY_D) == GLFW_PRESS) {
            ++cx;
        }
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, btt ? 0 : height, btt ? height : 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        super.resize(width, height);
    }

    @Override
    public void keyReleased(int key, int scancode, int mods) {
        if (key == GLFW_RELEASE) {
            window.close();
        }
        super.keyReleased(key, scancode, mods);
    }

    public static void main(String[] args) {
        GameConfig config = new GameConfig();
        config.title = "TextRenderer";
        config.tps = 100;
        new GameApp(new TextRendererTest(), config);
    }
}
