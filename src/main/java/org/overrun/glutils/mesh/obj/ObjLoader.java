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

import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.lwjgl.assimp.*;
import org.overrun.commonutils.FloatArray;
import org.overrun.commonutils.IntArray;
import org.overrun.glutils.gl.TexParam;
import org.overrun.glutils.gl.Textures;
import org.overrun.glutils.light.Material;
import org.overrun.glutils.mesh.Mesh;
import org.overrun.glutils.mesh.Mesh3;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.assimp.Assimp.*;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjLoader {
    /**
     * The default flags for Assimp.
     */
    public static final int DEFAULT_FLAGS = aiProcess_JoinIdenticalVertices
        | aiProcess_Triangulate
        | aiProcess_FixInfacingNormals;
    private static final File TMP = new File("glutils_obj_tmp");

    /**
     * Vertices processor
     *
     * @author squid233
     * @since 2.0.0
     */
    @FunctionalInterface
    public interface VertProcessor {
        /**
         * Set attribute index before return.
         *
         * @param mesh      Mesh to set
         * @param vertices  Vertices
         * @param meshIndex The order of the index of the mesh
         */
        void process(Mesh3 mesh,
                     float[] vertices,
                     int meshIndex);
    }

    private static AIScene load(ClassLoader cl,
                                String filename,
                                int flags) {
        var fn = filename.replaceAll("\\\\", "/");
        var parentPath = fn.substring(0, fn.lastIndexOf('/') + 1);
        var f = new File(TMP + "/" + parentPath);
        f.mkdirs();
        try {
            var resources = cl.getResources(parentPath);
            while (resources.hasMoreElements()) {
                var url = resources.nextElement();
                var protocol = url.getProtocol();
                if (protocol.equals("jar")) {
                    // TODO: 2021/8/31 0031 test
                    var conn = (JarURLConnection) url.openConnection();
                    var jar = conn.getJarFile();
                    var entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        var entry = entries.nextElement();
                        var name = entry.getName();
                        if (!entry.isDirectory() && name.startsWith(parentPath)) {
                            copyToFS(cl, name);
                        }
                    }
                } else if (protocol.equals("file")) {
                    var resource = requireNonNull(cl.getResource(parentPath));
                    var files = new File(resource.getPath()).list();
                    if (files != null) {
                        for (var file : files) {
                            copyToFS(cl, parentPath + "/" + file);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AIScene scene = aiImportFile(TMP + "/" + fn, flags);
        if (scene == null) {
            throw new RuntimeException(
                "Error loading model: " +
                    aiGetErrorString());
        }
        deleteTmpFiles(TMP);
        return scene;
    }

    private static void copyToFS(ClassLoader cl,
                                 String p)
        throws IOException {
        try (var in = cl.getResourceAsStream(p)) {
            Files.copy(requireNonNull(in),
                Paths.get(TMP + "/" + p),
                StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void deleteTmpFiles(File file) {
        var files = file.listFiles();
        if (files != null) {
            for (var f1 : files) {
                deleteTmpFiles(f1);
            }
        }
        file.delete();
    }

    private static List<Material> createMaterials(ClassLoader cl,
                                                  AIScene scene,
                                                  String filename) {
        int numMaterials = scene.mNumMaterials();
        var aiMaterials = scene.mMaterials();
        var materials = new ArrayList<Material>();
        for (int i = 0; i < numMaterials; i++) {
            var aiMaterial = AIMaterial.create(requireNonNull(aiMaterials).get(i));
            processMaterial(cl, aiMaterial, materials, filename);
        }
        return materials;
    }

    private static void processMaterial(ClassLoader cl,
                                        AIMaterial aiMaterial,
                                        List<Material> materials,
                                        String filename) {
        var color = AIColor4D.create();
        var path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial,
            aiTextureType_DIFFUSE,
            0,
            path,
            (IntBuffer) null,
            null,
            null,
            null,
            null,
            null);
        var texPath = path.dataString();
        int texture = 0;
        if (!texPath.isEmpty()) {
            texture = Textures.loadAWT(cl,
                filename + "/../" + texPath,
                TexParam.glNearest());
        }
        path.close();

        var ambient = Material.DEFAULT_COLOR;
        int result = aiGetMaterialColor(aiMaterial,
            AI_MATKEY_COLOR_AMBIENT,
            aiTextureType_NONE,
            0,
            color);
        if (result == 0) {
            ambient = new Vector4f(color.r(),
                color.g(),
                color.b(),
                color.a());
        }

        var diffuse = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial,
            AI_MATKEY_COLOR_DIFFUSE,
            aiTextureType_NONE,
            0,
            color);
        if (result == 0) {
            diffuse = new Vector4f(color.r(),
                color.g(),
                color.b(),
                color.a());
        }

        var specular = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial,
            AI_MATKEY_COLOR_SPECULAR,
            aiTextureType_NONE,
            0,
            color);
        if (result == 0) {
            specular = new Vector4f(color.r(),
                color.g(),
                color.b(),
                color.a());
        }

        materials.add(new Material(ambient,
            diffuse,
            specular,
            texture,
            1.0f));
    }

    private static void processVertices(AIMesh mesh,
                                        FloatArray vertices) {
        var buffer = mesh.mVertices();
        while (buffer.remaining() > 0) {
            var vertex = buffer.get();
            vertices.addAll(vertex.x(), vertex.y(), vertex.z());
        }
    }

    private static void processColors(AIMesh mesh,
                                      FloatArray colors) {
        var buffer = mesh.mColors(0);
        if (buffer != null) {
            while (buffer.remaining() > 0) {
                var color = buffer.get();
                colors.addAll(color.r(), color.g(), color.b(), color.a());
            }
        }
    }

    private static void processNormals(AIMesh mesh,
                                       FloatArray normals) {
        var buffer = mesh.mNormals();
        if (buffer != null) {
            while (buffer.remaining() > 0) {
                var normal = buffer.get();
                normals.addAll(normal.x(), normal.y(), normal.z());
            }
        }
    }

    private static void processTexCoords(AIMesh mesh,
                                         FloatArray textures) {
        var buffer = mesh.mTextureCoords(0);
        int numTexCoords = buffer != null ? buffer.remaining() : 0;
        for (int i = 0; i < numTexCoords; i++) {
            var texCoord = buffer.get();
            textures.addAll(texCoord.x(), 1 - texCoord.y());
        }
    }

    private static void processIndices(AIMesh mesh,
                                       IntArray indices) {
        int numFaces = mesh.mNumFaces();
        var buffer = mesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            var face = buffer.get(i);
            var ib = face.mIndices();
            while (ib.remaining() > 0) {
                indices.add(ib.get());
            }
        }
    }

    private static Mesh processMesh(AIMesh aiMesh,
                                    List<Material> materials) {
        var vertices = new FloatArray();
        var textures = new FloatArray();
        var normals = new FloatArray();
        var indices = new IntArray();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTexCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }

        return new Mesh()
            .vertices(vertices.toFArray())
            .texCoords(textures.toFArray())
            .normalVert(normals.toFArray())
            .indices(indices.toIArray())
            .material(material);
    }

    private static Mesh3 processMesh(AIMesh aiMesh,
                                     List<Material> materials,
                                     @Nullable VertProcessor vertProcessor,
                                     int index) {
        var vertices = new FloatArray();
        var colors = new FloatArray();
        var textures = new FloatArray();
        var normals = new FloatArray();
        var indices = new IntArray();

        processVertices(aiMesh, vertices);
        processColors(aiMesh, colors);
        processNormals(aiMesh, normals);
        processTexCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }

        var mesh = new Mesh3();
        var v = vertices.toFArray();
        if (vertProcessor != null) {
            vertProcessor.process(mesh, v, index);
        }
        if (!colors.isEmpty()) {
            mesh.colors(colors.toFArray());
        }
        return mesh.vertices(v)
            .texCoords(textures.toFArray())
            .normalVert(normals.toFArray())
            .indices(indices.toIArray())
            .material(material);
    }

    /**
     * Load object file with default flags.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath.
     * @return Meshes.
     */
    public static ObjModel2 load2(ClassLoader cl,
                                  String filename) {
        return load2(cl, filename, DEFAULT_FLAGS);
    }

    /**
     * Load object file.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath (in relative path).
     * @param flags    Assimp flags.
     * @return Meshes.
     */
    public static ObjModel2 load2(ClassLoader cl,
                                  String filename,
                                  int flags) {
        var scene = load(cl, filename, flags);
        var materials = createMaterials(cl, scene, filename);
        int numMeshes = scene.mNumMeshes();
        var aiMeshes = scene.mMeshes();
        var meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            var aiMesh = AIMesh.create(requireNonNull(aiMeshes).get(i));
            var mesh = processMesh(aiMesh, materials);
            meshes[i] = mesh;
        }
        aiReleaseImport(scene);
        return new ObjModel2(meshes);
    }

    /**
     * Load object file with default flags.
     *
     * @param cl            Class loader
     * @param filename      Object filename in classpath.
     * @param vertProcessor Set attribute index before return.
     * @return Meshes v3.
     */
    public static ObjModel3 load3(ClassLoader cl,
                                  String filename,
                                  @Nullable VertProcessor vertProcessor) {
        return load3(cl, filename, DEFAULT_FLAGS, vertProcessor);
    }

    /**
     * Load object file.
     *
     * @param cl            Class loader
     * @param filename      Object filename in classpath (in relative path).
     * @param flags         Assimp flags.
     * @param vertProcessor Set attribute index before return.
     * @return Meshes v3.
     */
    public static ObjModel3 load3(ClassLoader cl,
                                  String filename,
                                  int flags,
                                  @Nullable VertProcessor vertProcessor) {
        var scene = load(cl, filename, flags);
        var materials = createMaterials(cl, scene, filename);
        int numMeshes = scene.mNumMeshes();
        var aiMeshes = scene.mMeshes();
        var meshes = new Mesh3[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            var aiMesh = AIMesh.create(requireNonNull(aiMeshes).get(i));
            Mesh3 mesh = processMesh(aiMesh, materials, vertProcessor, i)
                .unbindVao();
            meshes[i] = mesh;
        }
        aiReleaseImport(scene);
        return new ObjModel3(meshes);
    }
}
