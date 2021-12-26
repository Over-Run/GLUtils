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

import org.joml.Vector3f;
import org.overrun.glutils.GLUtils;
import org.overrun.glutils.game.Game;
import org.overrun.glutils.game.GameApp;
import org.overrun.glutils.game.GameConfig;
import org.overrun.glutils.light.DirectionalLight;
import org.overrun.glutils.light.PointLight;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.overrun.glutils.game.GameEngine.input;
import static org.overrun.glutils.game.GameEngine.window;

/**
 * @author squid233
 */
public class GLUTest extends Game {
    public static final float SENSITIVITY = 0.05f;
    public final Player player = new Player();
    private float lightAngle;
    private final Vector3f ambientLight = new Vector3f(0.3f);
    private PointLight pointLight;
    private DirectionalLight directionalLight;
    public GameRenderer renderer;

    private void init() {
        (renderer = new GameRenderer()).init();
        float lightIntensity = 1.0f;
        var lightColor = new Vector3f(1);
        // player is the lighting source
        var lightPosition = new Vector3f(0);
        pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
        var att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        lightPosition = new Vector3f(-1, 0, 0);
        lightColor = new Vector3f(1);
        directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);
    }

    @Override
    public void create() {
        GLUtils.printLibInfo();
        window.setCursorPos(854 / 2, 480 / 2);
        var mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (mode != null) {
            window.setPos((mode.width() - window.width()) / 2,
                (mode.height() - window.height()) / 2);
        }
        glClearColor(0.4f, 0.6f, 0.9f, 1.0f);
        System.out.println("GL Renderer: " + glGetString(GL_RENDERER));
        System.out.println("GL Vendor: " + glGetString(GL_VENDOR));
        System.out.println("GL Extensions: " + glGetString(GL_EXTENSIONS));
        System.out.println("GL Version: " + glGetString(GL_VERSION));
        System.out.println("GL Shading language version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        init();
        window.setGrabbed(true);
    }

    @Override
    public void render() {
        renderer.render(player,
            ambientLight,
            pointLight,
            directionalLight,
            lightAngle);
        super.render();
    }

    @Override
    public void tick() {
        player.tick();
        // Update directional light direction, intensity and color
        lightAngle += 1f;
        if (lightAngle > 90f) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = max(factor, 0.9f);
            directionalLight.getColor().z = max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = toRadians(lightAngle);
        directionalLight.getDirection().x = (float) sin(angRad);
        directionalLight.getDirection().y = (float) cos(angRad);
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        super.resize(width, height);
    }

    @Override
    public void free() {
        renderer.free();
    }

    @Override
    public void keyPressed(int key, int scancode, int mods) {
        if (key == GLFW_KEY_ESCAPE) {
            window.close();
        }
        if (key == GLFW_KEY_GRAVE_ACCENT) {
            window.setGrabbed(!window.isGrabbed());
        }
        super.keyPressed(key, scancode, mods);
    }

    @Override
    public void cursorPosCb(int x, int y) {
        if (window.isGrabbed()) {
            double xOffset = input.getDeltaMX() * SENSITIVITY;
            double yOffset = input.getDeltaMY() * SENSITIVITY;
            player.turn(xOffset, yOffset);
        }
        super.cursorPosCb(x, y);
    }

    public static void main(String[] args) {
        var coreProfile = parseBoolean(System.getProperty(
            "GLUTest.coreProfile", "true"
        ));
        var cfg = new GameConfig();
        cfg.width = 854;
        cfg.height = 480;
        cfg.title = "Testing texture, lighting and HUD";
        if (coreProfile) {
            cfg.glVersion = 3.2;
            cfg.coreProfile = true;
        }
        System.out.println("Testing GLUtils " + GLUtils.version);
        new GameApp(new GLUTest(), cfg);
    }
}
