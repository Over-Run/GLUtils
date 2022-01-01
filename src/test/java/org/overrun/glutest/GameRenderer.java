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

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.overrun.glutils.gl.GLProgram;
import org.overrun.glutils.gui.DrawableText;
import org.overrun.glutils.gui.FontTexture;
import org.overrun.glutils.light.DirectionalLight;
import org.overrun.glutils.light.PointLight;
import org.overrun.glutils.mesh.Mesh3;
import org.overrun.glutils.mesh.MeshLoader;
import org.overrun.glutils.mesh.obj.ObjLoader;
import org.overrun.glutils.mesh.obj.ObjModel3;
import org.overrun.glutils.tex.TexParam;
import org.overrun.glutils.tex.Textures;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL15.*;
import static org.overrun.glutils.FilesReader.lines;
import static org.overrun.glutils.game.GameEngine.*;
import static org.overrun.glutils.util.math.Transform.*;

/**
 * @author squid233
 */
public class GameRenderer {
    public static final float[] LOW_FPS_COLOR = {
        1, 0, 0, 1,
        1, 0, 0, 1,
        1, 0, 0, 1,
        1, 0, 0, 1
    };
    public static final float[] HIGH_FPS_COLOR = {
        0, 1, 0, 1,
        0, 1, 0, 1,
        0, 1, 0, 1,
        0, 1, 0, 1
    };
    public static final float[] BG_COLOR = {
        0.0f, 0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.0f, 0.5f
    };
    public static final Class<GameRenderer> clazz = GameRenderer.class;
    public final Matrix4f proj = new Matrix4f();
    public final Matrix4fStack modelv = new Matrix4fStack(32);
    public final FontTexture utf8 = FontTexture.builder("Consolas-UTF_8-2")
        .font(Font.decode("Consolas"))
        .charset(StandardCharsets.UTF_8)
        .padding(2)
        .build();
    public static final float SPECULAR_POWER = 10;
    public GLProgram program;
    public GLProgram guiProgram;
    public ObjModel3 cube;
    public Mesh3 crossing;
    public Mesh3 text;
    public Mesh3 textBg;

    public void init() {
        program = new GLProgram();
        program.createVsh(lines(clazz, "shaders/scene.vsh"));
        program.createFsh(lines(clazz, "shaders/scene.fsh"));
        program.link();
        guiProgram = new GLProgram();
        guiProgram.createVsh(lines(clazz, "shaders/gui.vsh"));
        guiProgram.createFsh(lines(clazz, "shaders/gui.fsh"));
        guiProgram.link();
        cube = ObjLoader.load3(clazz,
            "model/cube/cube.obj",
            (m, v, i) -> m.vertIdx(0)
                .texIdx(1)
                .normalIdx(2)
        );
        cube.setConsumer(m -> program.setUniform("material.ambient",
            "material.diffuse",
            "material.specular",
            "material.textured",
            "material.reflectance",
            m.getMaterial()));
        crossing = MeshLoader.load3(clazz,
                "crossing.mesh",
                m -> m.vertIdx(0).colorIdx(1).texIdx(2))
            .texture(Textures.loadAWT(clazz,
                "crossing.png",
                TexParam.glNearest()));
        text = new Mesh3()
            .vertUsage(GL_DYNAMIC_DRAW)
            .vertIdx(0)
            .colorIdx(1)
            .colorDim(4)
            .texIdx(2)
            .unbindVao();
        textBg = new Mesh3()
            .vertUsage(GL_DYNAMIC_DRAW)
            .vertIdx(0)
            .colorIdx(1)
            .colorDim(4)
            .unbindVao();
    }

    public void render(Player player,
                       Vector3f ambientLight,
                       PointLight pointLight,
                       DirectionalLight light,
                       float lightAngle) {
        modelv.pushMatrix();
        float xRot = player.xRot;
        float yRot = player.yRot;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        program.bind();

        modelv.pushMatrix();
        Matrix4f viewMatrix = new Matrix4f(rotateY(rotateX(modelv, -xRot), yRot));
        modelv.popMatrix();
        {
            final float x = player.x,
                y = player.y,
                z = player.z,
                xo = player.xo,
                yo = player.yo,
                zo = player.zo;
            float delta = timer.getDelta();
            float tx = xo + (x - xo) * delta;
            float ty = yo + (y - yo) * delta;
            float tz = zo + (z - zo) * delta;
            viewMatrix.translate(-tx, -ty, -tz);
        }

        modelv.mul(viewMatrix);
        program.setUniformMat4("proj",
            setPerspective(proj,
                90,
                bufFrame,
                0.05f,
                1000.0f));

        var currDirLight = new DirectionalLight(light);
        var dir = new Vector4f(currDirLight.getDirection(), 0)
            .mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        // Get a copy of the point light object and transform its position to view coordinates
        var currPointLight = new PointLight(pointLight);
        var lightPos = currPointLight.getPosition();
        var aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;

        // Update Light Uniforms
        program.setUniform("ambientLight", ambientLight);
        program.setUniform("specularPower", SPECULAR_POWER);
        program.setUniform(
            "pointLight.color",
            "pointLight.position",
            "pointLight.intensity",
            "pointLight.att.constant",
            "pointLight.att.linear",
            "pointLight.att.exponent",
            currPointLight);
        program.setUniform(
            "directionalLight.color",
            "directionalLight.direction",
            "directionalLight.intensity",
            currDirLight);

        program.setUniform("texSampler", 0);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    renderMesh(x, y, z);
                }
            }
        }
        program.unbind();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        renderGui(player, lightAngle);
        glDisable(GL_BLEND);
        modelv.popMatrix();
    }

    private void renderMesh(float x,
                            float y,
                            float z) {
        float fx = x * 0.9375f;
        float fy = y * 0.9375f;
        float fz = z * 0.9375f;
        modelv.pushMatrix();
        program.setUniformMat4("modelv", modelv.translate(fx, fy, fz));
        cube.render();
        modelv.popMatrix();
    }

    public void renderGui(Player player,
                          float lightAngle) {
        float cameraX = player.x;
        float cameraY = player.y;
        float cameraZ = player.z;
        modelv.pushMatrix();
        glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA);
        guiProgram.bind();
        guiProgram.setUniform("texSampler", 0);
        guiProgram.setUniform("textured", true);
        //todo scale
        guiProgram.setUniformMat4("proj",
            modelv.setOrtho2D(0,
                bufFrame.width(),
                bufFrame.height(),
                0));
        guiProgram.setUniformMat4("modelv",
            modelv.translation(bufFrame.width() / 2f,
                bufFrame.height() / 2f,
                0));
        crossing.render();
        guiProgram.setUniformMat4("modelv", modelv.translation(2, 2, 0));
        int fps = graphics.getFps();
        String fpsSt = "FPS: " + fps;
        String st = fpsSt + "\nLight angle: " + lightAngle;
        st += "\nCamera pos: " + cameraX + ", " + cameraY + ", " + cameraZ;
        st += "\nCamera rotation: " + player.xRot + ", " + player.yRot;
        DrawableText.build(utf8,
            st,
            4,
            (c, index) -> BG_COLOR,
            (c, index) -> {
                if (index > 3 && index < fpsSt.length()) {
                    if (fps < 30) {
                        return LOW_FPS_COLOR;
                    }
                    return HIGH_FPS_COLOR;
                }
                return DrawableText.DEFAULT_COLOR_ALPHA;
            },
            (vertices,
             colors,
             texCoord,
             tex,
             indices,
             bc) -> {
                text.bindVao()
                    .vertices(vertices)
                    .colors(colors)
                    .texCoords(texCoord)
                    .texture(tex)
                    .indices(indices)
                    .unbindVao();
                textBg.bindVao()
                    .vertices(vertices)
                    .colors(bc)
                    .indices(indices)
                    .unbindVao();
            }
        );
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        guiProgram.setUniform("textured", false);
        textBg.render();
        guiProgram.setUniform("textured", true);
        text.render();
        guiProgram.unbind();
        modelv.popMatrix();
    }

    public void free() {
        if (cube != null) {
            cube.free();
        }
        if (crossing != null) {
            crossing.free();
        }
        if (text != null) {
            text.free();
        }
        if (program != null) {
            program.free();
        }
        if (guiProgram != null) {
            guiProgram.free();
        }
    }
}
