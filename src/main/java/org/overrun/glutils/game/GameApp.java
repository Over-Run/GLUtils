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

import org.lwjgl.opengl.GL;
import org.overrun.glutils.timer.SystemTimer;
import org.overrun.glutils.wnd.Framebuffer;
import org.overrun.glutils.wnd.GLFWindow;

import static org.lwjgl.glfw.GLFW.*;
import static org.overrun.glutils.game.GameEngine.*;

/**
 * @author squid233
 * @since 1.5.0
 */
public class GameApp {
    public final IGameLogic game;
    public final GameConfig config;

    /**
     * Create and run game
     *
     * @param game   Game logic
     * @param config Game config
     */
    public GameApp(final IGameLogic game,
                   final GameConfig config) {
        this.game = game;
        this.config = config;
        app = this;
        config.errorCallback.set();
        if (!glfwInit()) {
            throw new IllegalStateException("Can't init GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE,
            config.hintVisible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, (int) config.glVersion);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,
            (int) (config.glVersion * 10) - (int) config.glVersion * 10);
        glfwWindowHint(GLFW_OPENGL_PROFILE,
            config.coreProfile
                ? GLFW_OPENGL_CORE_PROFILE
                : GLFW_OPENGL_ANY_PROFILE);
        window = new GLFWindow(config.width,
            config.height,
            config.title);
        input = new Input();
        window.keyCb((hWnd, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                game.keyPressed(key, scancode, mods);
            } else if (action == GLFW_RELEASE) {
                game.keyReleased(key, scancode, mods);
            } else if (action == GLFW_REPEAT) {
                game.keyRepeated(key, scancode, mods);
            }
        });
        window.cursorPosCb((hWnd, xp, yp) -> {
            input.deltaMX = (int) xp - input.mouseX;
            input.deltaMY = (int) yp - input.mouseY;
            game.cursorPosCb(input.mouseX, input.mouseY);
            input.mouseX = (int) xp;
            input.mouseY = (int) yp;
        });
        framebuffer = new Framebuffer((hWnd, width, height) ->
            game.resize(width, height), window);
        timer = new SystemTimer(config.tps);
        window.makeCurr();
        GL.createCapabilities();
        glfwSwapInterval(config.vSync ? 1 : 0);
        graphics = new Graphics();
        game.create();
        window.show();
        long lastTime = System.currentTimeMillis();
        int frames = 0;
        while (!window.shouldClose()) {
            timer.advanceTime();
            for (int i = 0; i < timer.getTicks(); i++) {
                game.tick();
            }
            game.render();
            window.swapBuffers();
            glfwPollEvents();
            game.onUpdated();
            ++frames;
            while (System.currentTimeMillis() >= lastTime + 1000) {
                graphics.fps = frames;
                lastTime += 1000;
                frames = 0;
            }
        }
        game.free();
        window.free();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
