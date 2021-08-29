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

package org.overrun.glutils.mesh.obj;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.overrun.glutils.light.Material;
import org.overrun.glutils.mesh.Mesh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.assimp.Assimp.*;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjLoader {
    public static final int DEFAULT_FLAGS = aiProcess_JoinIdenticalVertices
            | aiProcess_Triangulate
            | aiProcess_FixInfacingNormals;
    private static File tempDir;

    private static AIScene load(ClassLoader cl,
                                String filename,
                                int flags)
            throws Exception {
        if (tempDir == null) {
            tempDir = File.createTempFile("overrun_glutils", "obj_loader");
            tempDir.deleteOnExit();
        }
        String ffn = tempDir + filename;
        try (InputStream is = cl.getResourceAsStream(filename);
             Reader isr = new InputStreamReader(requireNonNull(is),
                     StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr);
             Writer w = new FileWriter(ffn);
             Writer bw = new BufferedWriter(w)) {
            for (String s : br.lines().collect(Collectors.toList())) {
                bw.write(s + "\n");
            }
        }
        AIScene scene = aiImportFile(ffn, flags);
        if (scene == null) {
            throw new RuntimeException(
                    "Error loading model" +
                            aiGetErrorString());
        }
        return scene;
    }

    private static Mesh processMesh(AIMesh aiMesh,
                                    Material material) {}

    /**
     * Load object file with default flags.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath.
     * @return Meshes.
     * @throws Exception IOE or RE
     */
    public static Mesh[] load2(ClassLoader cl,
                               String filename)
            throws Exception {
        return load2(cl, filename, DEFAULT_FLAGS);
    }

    /**
     * Load object file.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath (in relative path).
     * @param flags    Assimp flags.
     * @return Meshes.
     * @throws Exception IOE or RE
     */
    public static Mesh[] load2(ClassLoader cl,
                               String filename,
                               int flags)
            throws Exception {
        AIScene scene = load(cl, filename, flags);
        int numMeshes = scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(requireNonNull(aiMeshes).get(i));
            Mesh mesh = processMesh(aiMesh, material);
            meshes[i] = mesh;
        }
        return meshes;
    }
}
