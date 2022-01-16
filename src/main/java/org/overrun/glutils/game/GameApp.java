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

import org.lwjgl.opengl.GL;
import org.lwjgl.system.Callback;
import org.overrun.glutils.timer.TimerMgrImpl;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.glutils.wnd.GLFWindow;

import static java.lang.Math.floor;
import static java.util.Objects.requireNonNullElse;
import static org.lwjgl.glfw.GLFW.*;
import static org.overrun.glutils.GLUtils.getLogger;
import static org.overrun.glutils.game.GameEngine.*;

/**
 * @author squid233
 * @since 1.5.0
 */
public class GameApp {
    public final IGameLogic game;
    public final GameConfig config;

    /**
     * Create game
     *
     * @param game   Game logic
     * @param config Game config
     */
    public GameApp(final IGameLogic game,
                   final GameConfig config) {
        this.game = game;
        this.config = config;
    }

    /**
     * Start the game
     *
     * @since 2.0.0
     */
    public void start() {
        app = this;
        config.errorCallback.set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE,
            config.hintVisible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, (int) config.glVersion);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,
            ((int) (config.glVersion * 10)) - (((int) config.glVersion) * 10));
        glfwWindowHint(GLFW_OPENGL_PROFILE,
            config.coreProfile
                ? GLFW_OPENGL_CORE_PROFILE
                : GLFW_OPENGL_ANY_PROFILE);
        final int cw = config.width;
        final int ch = config.height;
        window = new GLFWindow(cw,
            ch,
            config.title);
        input = new Input();
        Callback cb = window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                game.keyPressed(key, scancode, mods);
            } else if (action == GLFW_RELEASE) {
                game.keyReleased(key, scancode, mods);
            } else if (action == GLFW_REPEAT) {
                game.keyRepeated(key, scancode, mods);
            }
        });
        if (cb != null) {
            cb.free();
        }
        cb = window.mouseButtonCb((hWnd, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                game.mousePressed(button, mods);
            } else if (action == GLFW_RELEASE) {
                game.mouseReleased(button, mods);
            }
        });
        if (cb != null) {
            cb.free();
        }
        cb = window.cursorPosCb((hWnd, xp, yp) -> {
            var nxp = (int) floor(xp);
            var nyp = (int) floor(yp);
            input.deltaMX = nxp - input.mouseX;
            input.deltaMY = nyp - input.mouseY;
            game.cursorPosCb(nxp, nyp);
            input.mouseX = nxp;
            input.mouseY = nyp;
        });
        if (cb != null) {
            cb.free();
        }
        cb = window.scrollCb((hWnd, xo, yo) -> game.mouseWheel(xo, yo));
        if (cb != null) {
            cb.free();
        }
        cb = window.charCb((hWnd, codepoint) -> game.inputChar(codepoint));
        if (cb != null) {
            cb.free();
        }
        bufFrame = framebuffer = new Framebuffer((hWnd, width, height) ->
            game.resize(width, height), window)
            .setWidth(cw)
            .setHeight(ch);
        timerMgr = requireNonNullElse(config.timerMgr, new TimerMgrImpl());
        window.makeCurr();
        glfwSwapInterval(config.vSync ? 1 : 0);
        GL.createCapabilities();
        graphics = new Graphics();
        try {
            game.create();
            game.resize(cw, ch);
            window.show();
            long lastTime = System.currentTimeMillis();
            int frames = 0;
            while (!window.shouldClose()) {
                for (int i = 0;
                     i < timerMgr.getIDCount();
                     i++) {
                    var timerID = timerMgr.getID(i);
                    var timer = timerID.get();
                    timer.advanceTime();
                    for (int j = 0; j < timer.getTicks(); j++) {
                        game.tick(timerID);
                    }
                }
                game.render();
                window.swapBuffers();
                glfwPollEvents();
                game.onUpdated();
                ++frames;
                while (System.currentTimeMillis() >= lastTime + 1000) {
                    graphics.fps = frames;
                    game.passedFrame();
                    lastTime += 1000;
                    frames = 0;
                }
            }
        } catch (Throwable t) {
            getLogger().catching(t);
        } finally {
            game.free();
            GL.setCapabilities(null);
            window.free();
            glfwTerminate();
            cb = glfwSetErrorCallback(null);
            if (cb != null) {
                cb.free();
            }
        }
    }
}
