/*
 * MIT License
 *
 * Copyright (c) 2021 OverRun Organization
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
import org.joml.Vector3f;
import org.overrun.glutils.GLProgram;
import org.overrun.glutils.Mesh3;
import org.overrun.glutils.Textures;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.ShaderReader.lines;
import static org.overrun.glutils.math.Transform.*;

/**
 * @author squid233
 */
public class GameRenderer implements AutoCloseable {
    public static final ClassLoader cl = GameRenderer.class.getClassLoader();
    public final Matrix4f proj = new Matrix4f();
    public final Matrix4f modelv = new Matrix4f();
    public final Matrix4f view = new Matrix4f();
    public int texture;
    public GLProgram program;
    public GLProgram program2d;
    public Mesh3 mesh;
    public Mesh3 crossing;

    public void init() {
        texture = Textures.loadAWT(cl, "face.png", GL_NEAREST);
        program = new GLProgram();
        program.createVsh(lines(cl, "shaders/scene.vsh"));
        program.createFsh(lines(cl, "shaders/scene.fsh"));
        program.link();
        program2d = new GLProgram();
        program2d.createVsh(lines(cl, "shaders/scene.vsh"));
        program2d.createFsh(lines(cl, "shaders/scene.fsh"));
        program2d.link();
        float size = 1.0f;
        float[] vert = {
                // front
                0.0f, size, 0.0f, //0
                0.0f, 0.0f, 0.0f, //1
                size, 0.0f, 0.0f, //2
                size, size, 0.0f, //3
                // back
                size, size, -size, // 4
                size, 0.0f, -size, // 5
                0.0f, 0.0f, -size, // 6
                0.0f, size, -size, // 7
                // left
                0.0f, size, -size, // 8
                0.0f, 0.0f, -size, // 9
                0.0f, 0.0f, 0.0f, // 10
                0.0f, size, 0.0f, // 11
                // right
                size, size, 0.0f, // 12
                size, 0.0f, 0.0f, // 13
                size, 0.0f, -size, // 14
                size, size, -size, // 15
                // up
                0.0f, size, -size, // 16
                0.0f, size, 0.0f, // 17
                size, size, 0.0f, // 18
                size, size, -size, // 19
                // down
                size, 0.0f, -size, // 20
                size, 0.0f, 0.0f, // 21
                0.0f, 0.0f, 0.0f, // 22
                0.0f, 0.0f, -size // 23
        };
        float[] col = {
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,
                0.4f, 0.8f, 1.0f,

                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,
                0.5f, 1.0f, 0.5f,

                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,
                1.0f, 0.5f, 0.0f,

                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,
                1.0f, 0.1f, 0.0f,

                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,

                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f
        };
        float[] tex = {
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 0
        };
        int[] idx = {
                // south
                0, 1, 3, 3, 1, 2,
                // north
                4, 5, 7, 7, 5, 6,
                // west
                8, 9, 11, 11, 9, 10,
                // east
                12, 13, 15, 15, 13, 14,
                // up
                16, 17, 19, 19, 17, 18,
                // down
                20, 21, 23, 23, 21, 22
        };
        mesh = Mesh3.of(vert, 0, col, 1, tex, texture, 2, idx);
        vert = new float[]{
                -1, -9, 0, //0
                1, -9, 0, //1
                -1, 9, 0, //2
                1, 9, 0, //3
                -9, -1, 0, //4
                -9, 1, 0, //5
                9, -1, 0, //6
                9, 1, 0 //7
        };
        col = new float[]{
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };
        idx = new int[]{
                0, 2, 1, 1, 2, 3,
                4, 5, 6, 6, 5, 7
        };
        crossing = Mesh3.of(vert, 0, col, 1, idx);
    }

    public void render(int w,
                       int h,
                       float xRot,
                       float yRot,
                       Vector3f pos) {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        program.bind();
        program.setUniform("texSampler", 0);
        program.setUniformMat4("proj", rotateY(rotateX(setPerspective(proj, 70, w, h, 0.05f, 1000.0f), -xRot), yRot));
        program.setUniform("textured", 1);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    renderMesh(pos, x, y, z);
                }
            }
        }
        program.unbind();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        renderGui(w, h);
    }

    private void renderMesh(Vector3f pos, float x, float y, float z) {
        float fx = x == 0 ? -pos.x : -pos.x + (x * 0.9375f);
        float fy = y == 0 ? -pos.y : -pos.y + (y * 0.9375f);
        float fz = z == 0 ? -pos.z : -pos.z + (z * 0.9375f);
        program.setUniformMat4("modelv", modelv.translation(fx, fy, fz));
        mesh.render();
    }

    public void renderGui(int w, int h) {
        program2d.bind();
        program2d.setUniform("texSampler", 0);
        program2d.setUniformMat4("proj", view.setOrtho2D(0, w, h, 0));
        program2d.setUniformMat4("modelv", view.translation(w / 2f, h / 2f, 0));
        crossing.render();
        program2d.unbind();
    }

    @Override
    public void close() {
        if (mesh != null) {
            mesh.close();
        }
        if (program != null) {
            program.close();
        }
    }
}
