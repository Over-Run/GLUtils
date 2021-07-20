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
import static org.overrun.glutils.MeshLoader.load3;
import static org.overrun.glutils.MeshLoader.var;
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

    public void init() throws Exception {
        texture = Textures.loadAWT(cl, "face.png", GL_NEAREST);
        program = new GLProgram();
        program.createVsh(lines(cl, "shaders/scene.vsh"));
        program.createFsh(lines(cl, "shaders/scene.fsh"));
        program.link();
        program2d = new GLProgram();
        program2d.createVsh(lines(cl, "shaders/scene.vsh"));
        program2d.createFsh(lines(cl, "shaders/scene.fsh"));
        program2d.link();
        mesh = load3(cl,
                "cube.mesh",
                m -> m.vertIdx(0).colorIdx(1).texIdx(2),
                var("size", 1.0f)).texture(texture);
        float[] vert = {
                -1, -9, 0, //0
                1, -9, 0, //1
                -1, 9, 0, //2
                1, 9, 0, //3
                -9, -1, 0, //4
                -9, 1, 0, //5
                9, -1, 0, //6
                9, 1, 0 //7
        };
        float[] col = {
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };
        int[] idx = {
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
