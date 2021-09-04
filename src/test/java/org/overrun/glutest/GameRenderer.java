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

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.overrun.glutils.*;
import org.overrun.glutils.light.DirectionalLight;
import org.overrun.glutils.light.PointLight;
import org.overrun.glutils.mesh.Mesh3;
import org.overrun.glutils.mesh.MeshLoader;
import org.overrun.glutils.mesh.obj.ObjLoader;
import org.overrun.glutils.mesh.obj.ObjModel3;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL15.*;
import static org.overrun.glutest.GLUTest.TIMER;
import static org.overrun.glutils.ShaderReader.lines;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class GameRenderer implements AutoCloseable {
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
    public static final ClassLoader cl = GameRenderer.class.getClassLoader();
    public final Matrix4f proj = new Matrix4f();
    public final Matrix4fStack modelv = new Matrix4fStack(32);
    public final FontTexture utf8 = FontTextures.builder("Consolas-UTF_8-2")
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

    public void init() throws Exception {
        program = new GLProgram();
        program.createVsh(lines(cl, "shaders/scene.vsh"));
        program.createFsh(lines(cl, "shaders/scene.fsh"));
        program.link();
        guiProgram = new GLProgram();
        guiProgram.createVsh(lines(cl, "shaders/gui.vsh"));
        guiProgram.createFsh(lines(cl, "shaders/gui.fsh"));
        guiProgram.link();
        cube = ObjLoader.load3(cl,
                "model/cube/cube.obj",
                (m, v, i) -> m.vertIdx(0)
                        .texIdx(1)
                        .normalIdx(2)
        );
        cube.setPreRender(m -> program.setUniform("material.ambient",
                "material.diffuse",
                "material.specular",
                "material.textured",
                "material.reflectance",
                m.getMaterial()));
        crossing = MeshLoader.load3(cl,
                        "crossing.mesh",
                        m -> m.vertIdx(0).colorIdx(1).texIdx(2))
                .texture(Textures.loadAWT(cl, "crossing.png", GL_NEAREST));
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

    public void render(int w,
                       int h,
                       Player player,
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

        modelv.mul(viewMatrix);
        program.setUniformMat4("proj",
                setPerspective(proj,
                        70,
                        w,
                        h,
                        0.05f,
                        1000.0f));

        DirectionalLight currDirLight = new DirectionalLight(light);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0)
                .mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        // Get a copy of the point light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
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
                    renderMesh(player, x, y, z);
                }
            }
        }
        program.unbind();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        renderGui(player, w, h, lightAngle);
        glDisable(GL_BLEND);
        modelv.popMatrix();
    }

    private void renderMesh(Player player,
                            float x,
                            float y,
                            float z) {
        float cameraX = player.x;
        float cameraY = player.y;
        float cameraZ = player.z;
        float fx = x == 0 ? -cameraX : -cameraX + (x * 0.9375f);
        float fy = y == 0 ? -cameraY : -cameraY + (y * 0.9375f);
        float fz = z == 0 ? -cameraZ : -cameraZ + (z * 0.9375f);
        modelv.pushMatrix();
        program.setUniformMat4("modelv", modelv.translate(fx, fy, fz));
        cube.render();
        modelv.popMatrix();
    }

    public void renderGui(Player player,
                          int w,
                          int h,
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
        guiProgram.setUniformMat4("proj", modelv.setOrtho2D(0, w, h, 0));
        guiProgram.setUniformMat4("modelv", modelv.translation(w / 2f, h / 2f, 0));
        crossing.render();
        guiProgram.setUniformMat4("modelv", modelv.translation(2, 2, 0));
        String fpsSt = "FPS: " + TIMER.fps;
        String st = fpsSt + "\nLight angle: " + lightAngle;
        st += "\nCamera pos: " + cameraX + ", " + cameraY + ", " + cameraZ;
        st += "\nCamera rotation: " + player.xRot + ", " + player.yRot;
        DrawableText.build(utf8,
                st,
                4,
                (c, index) -> BG_COLOR,
                (c, index) -> {
                    if (index > 3 && index < fpsSt.length()) {
                        if (TIMER.fps < 30) {
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

    @Override
    public void close() {
        if (cube != null) {
            cube.close();
        }
        if (crossing != null) {
            crossing.close();
        }
        if (text != null) {
            text.close();
        }
        if (program != null) {
            program.close();
        }
        if (guiProgram != null) {
            guiProgram.close();
        }
    }
}
