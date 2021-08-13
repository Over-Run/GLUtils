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
import org.overrun.glutils.*;
import org.overrun.glutils.mesh.Mesh3;

import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.overrun.glutest.GLUTest.TIMER;
import static org.overrun.glutils.ShaderReader.lines;
import static org.overrun.glutils.math.Transform.*;
import static org.overrun.glutils.mesh.MeshLoader.def;
import static org.overrun.glutils.mesh.MeshLoader.load3;

/**
 * @author squid233
 */
public class GameRenderer implements AutoCloseable {
    public static final float[] LOW_FPS_COLOR = {
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0
    };
    public static final float[] HIGH_FPS_COLOR = {
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0
    };
    public static final ClassLoader cl = GameRenderer.class.getClassLoader();
    public final Matrix4f proj = new Matrix4f();
    public final Matrix4f modelv = new Matrix4f();
    public final Matrix4f view = new Matrix4f();
    public final FontTexture utf8 = FontTextures.builder("Consolas-UTF_8-2")
            .font(Font.decode("Consolas"))
            .charset(StandardCharsets.UTF_8)
            .padding(2)
            .build();
    public final Map<Integer, Mesh3> textBgMap = new HashMap<>();
    public GLProgram program;
    public Mesh3 cube;
    public Mesh3 crossing;
    public Mesh3 text;

    public void init() throws Exception {
        program = new GLProgram();
        program.createVsh(lines(cl, "shaders/scene.vsh"));
        program.createFsh(lines(cl, "shaders/scene.fsh"));
        program.link();
        cube = load3(cl,
                "cube.mesh",
                m -> m.vertIdx(0).colorIdx(1).texIdx(2),
                def("size", 1.0f))
                .texture(Textures.loadAWT(cl, "face.png", GL_NEAREST));
        crossing = load3(cl,
                "crossing.mesh",
                m -> m.vertIdx(0).colorIdx(1).texIdx(2))
                .texture(Textures.loadAWT(cl, "crossing.png", GL_NEAREST));
        text = new Mesh3().unbindVao();
    }

    public void render(int w,
                       int h,
                       Player player) {
        float xRot = player.xRot;
        float yRot = player.yRot;
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        program.bind();
        program.setUniform("texSampler", 0);
        program.setUniform("textured", 1);
        program.setUniformMat4("proj",
                rotateY(rotateX(setPerspective(proj,
                        70,
                        w,
                        h,
                        0.05f,
                        1000.0f), -xRot), yRot));
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    renderMesh(player.x,
                            player.y,
                            player.z,
                            x,
                            y,
                            z);
                }
            }
        }
        program.unbind();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        renderGui(w, h);
        glDisable(GL_BLEND);
    }

    private void renderMesh(float cameraX,
                            float cameraY,
                            float cameraZ,
                            float x,
                            float y,
                            float z) {
        float fx = x == 0 ? -cameraX : -cameraX + (x * 0.9375f);
        float fy = y == 0 ? -cameraY : -cameraY + (y * 0.9375f);
        float fz = z == 0 ? -cameraZ : -cameraZ + (z * 0.9375f);
        program.setUniformMat4("modelv", modelv.translation(fx, fy, fz));
        cube.render();
    }

    public void renderGui(int w, int h) {
        glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA);
        program.bind();
        program.setUniformMat4("proj", view.setOrtho2D(0, w, h, 0));
        program.setUniformMat4("modelv", view.translation(w / 2f, h / 2f, 0));
        crossing.render();
        program.setUniformMat4("modelv", view.translation(2, 2, 0));
        String st = "FPS: " + TIMER.lastFps;
        int stl = st.length();
        DrawableText.build(utf8,
                st,
                (text, vertices) -> {
                    if (textBgMap.containsKey(stl)) {
                        return;
                    }
                    int cl = vertices.length;
                    // len / 2
                    int il = vertices.length >> 1;
                    float[] colors = new float[cl];
                    int[] indices = new int[il];
                    for (int i = 0; i < cl; i++) {
                        colors[i] = 1;
                    }
                    for (int i = 0; i < il; i += 6) {
                        // i / 6 * 4
                        int i0 = (int) (i * 0.6666666666666666);
                        int i1 = i0 + 1;
                        int i2 = i0 + 2;
                        int i3 = i0 + 3;
                        indices[i] = i0;
                        indices[i + 1] = i1;
                        indices[i + 2] = i2;
                        indices[i + 3] = i3;
                        indices[i + 4] = i0;
                        indices[i + 5] = i2;
                    }
                    textBgMap.put(stl, new Mesh3()
                            .vertUsage(GL_DYNAMIC_DRAW)
                            .vertIdx(0)
                            .vertices(vertices)
                            .colorIdx(1)
                            .colors(colors)
                            .indices(indices)
                            .unbindVao());
                },
                (c, index) -> {
                    if (index > 3) {
                        if (TIMER.lastFps < 30) {
                            return LOW_FPS_COLOR;
                        }
                        return HIGH_FPS_COLOR;
                    }
                    return DrawableText.DEFAULT_COLOR;
                },
                (vertices, colors, texCoord, tex, indices) ->
                        text.bindVao()
                                .vertUsage(GL_DYNAMIC_DRAW)
                                .vertIdx(0)
                                .vertices(vertices)
                                .colorIdx(1)
                                .colors(colors)
                                .texIdx(2)
                                .texCoords(texCoord)
                                .texture(tex)
                                .indices(indices)
                                .unbindVao()
        );
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        textBgMap.get(stl).render();
        glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_ALPHA);
        text.render();
        program.unbind();
    }

    @Override
    public void close() {
        if (cube != null) {
            cube.close();
        }
        if (program != null) {
            program.close();
        }
    }
}
