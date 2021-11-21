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
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.overrun.commonutils.FloatArray;
import org.overrun.commonutils.IntArray;
import org.overrun.glutils.Textures;
import org.overrun.glutils.light.Material;
import org.overrun.glutils.mesh.Mesh;
import org.overrun.glutils.mesh.Mesh3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjLoader {
    /**
     *
     */
    public static final int DEFAULT_FLAGS = aiProcess_JoinIdenticalVertices
        | aiProcess_Triangulate
        | aiProcess_FixInfacingNormals;
    private static final File TMP = new File("tmp");

    static {
        TMP.mkdirs();
        TMP.deleteOnExit();
    }

    /**
     * pre return
     *
     * @author squid233
     */
    @FunctionalInterface
    public interface PreReturn {
        /**
         * Set attribute index before return.
         *
         * @param mesh      Mesh to set
         * @param vertices  Vertices
         * @param meshIndex The order of the index of the mesh
         */
        void accept(Mesh3 mesh,
                    float[] vertices,
                    int meshIndex);
    }

    private static AIScene load(ClassLoader cl,
                                String filename,
                                int flags) {
        File parent = new File(filename).getParentFile();
        String parentPath = parent.getPath().replaceAll("\\\\", "/");
        new File(TMP + "/" + parentPath).mkdirs();
        try {
            Enumeration<URL> resources = cl.getResources(parentPath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if (protocol.equals("jar")) {
                    // TODO: 2021/8/31 0031 test
                    JarURLConnection conn = (JarURLConnection) url.openConnection();
                    JarFile jar = conn.getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (!entry.isDirectory() && name.startsWith(parentPath)) {
                            try (InputStream in = cl.getResourceAsStream(name)) {
                                Files.copy(requireNonNull(in),
                                    Paths.get(TMP + "/" + name),
                                    StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
                } else if (protocol.equals("file")) {
                    URL resource = requireNonNull(cl.getResource(parentPath));
                    String[] files = new File(resource.getPath()).list();
                    if (files != null) {
                        for (String file : files) {
                            String path = parentPath + "/" + file;
                            try (InputStream in = cl.getResourceAsStream(path)) {
                                Files.copy(requireNonNull(in),
                                    Paths.get(TMP + "/" + path),
                                    StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AIScene scene = aiImportFile(TMP + "/" + filename, flags);
        if (scene == null) {
            throw new RuntimeException(
                "Error loading model: " +
                    aiGetErrorString());
        }
        return scene;
    }

    private static List<Material> createMaterials(ClassLoader cl,
                                                  AIScene scene,
                                                  String filename) {
        int numMaterials = scene.mNumMaterials();
        PointerBuffer aiMaterials = scene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(requireNonNull(aiMaterials).get(i));
            processMaterial(cl, aiMaterial, materials, filename);
        }
        return materials;
    }

    private static void processMaterial(ClassLoader cl,
                                        AIMaterial aiMaterial,
                                        List<Material> materials,
                                        String filename) {
        AIColor4D color = AIColor4D.create();
        AIString path = AIString.calloc();
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
        String texPath = path.dataString();
        int texture = 0;
        if (!texPath.isEmpty()) {
            texture = Textures.loadAWT(cl, filename + "/../" + texPath, GL_NEAREST);
        }
        path.close();

        Vector4f ambient = Material.DEFAULT_COLOR;
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
        if (result == 0) {
            ambient = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        Vector4f diffuse = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
        if (result == 0) {
            diffuse = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        Vector4f specular = Material.DEFAULT_COLOR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
        if (result == 0) {
            specular = new Vector4f(color.r(), color.g(), color.b(), color.a());
        }

        materials.add(new Material(ambient, diffuse, specular, texture, 1.0f));
    }

    private static void processVertices(AIMesh mesh,
                                        FloatArray vertices) {
        AIVector3D.Buffer buffer = mesh.mVertices();
        while (buffer.remaining() > 0) {
            AIVector3D vertex = buffer.get();
            vertices.addAll(vertex.x(), vertex.y(), vertex.z());
        }
    }

    private static void processColors(AIMesh mesh,
                                      FloatArray colors) {
        AIColor4D.Buffer buffer = mesh.mColors(0);
        if (buffer != null) {
            while (buffer.remaining() > 0) {
                AIColor4D color = buffer.get();
                colors.addAll(color.r(), color.g(), color.b(), color.a());
            }
        }
    }

    private static void processNormals(AIMesh mesh,
                                       FloatArray normals) {
        AIVector3D.Buffer buffer = mesh.mNormals();
        if (buffer != null) {
            while (buffer.remaining() > 0) {
                AIVector3D normal = buffer.get();
                normals.addAll(normal.x(), normal.y(), normal.z());
            }
        }
    }

    private static void processTexCoords(AIMesh mesh,
                                         FloatArray textures) {
        AIVector3D.Buffer buffer = mesh.mTextureCoords(0);
        int numTexCoords = buffer != null ? buffer.remaining() : 0;
        for (int i = 0; i < numTexCoords; i++) {
            AIVector3D texCoord = buffer.get();
            textures.addAll(texCoord.x(), 1 - texCoord.y());
        }
    }

    private static void processIndices(AIMesh mesh,
                                       IntArray indices) {
        int numFaces = mesh.mNumFaces();
        AIFace.Buffer buffer = mesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace face = buffer.get(i);
            IntBuffer ib = face.mIndices();
            while (ib.remaining() > 0) {
                indices.add(ib.get());
            }
        }
    }

    private static Mesh processMesh(AIMesh aiMesh,
                                    List<Material> materials) {
        FloatArray vertices = new FloatArray();
        FloatArray textures = new FloatArray();
        FloatArray normals = new FloatArray();
        IntArray indices = new IntArray();

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
                                     @Nullable PreReturn preReturn,
                                     int index) {
        FloatArray vertices = new FloatArray();
        FloatArray colors = new FloatArray();
        FloatArray textures = new FloatArray();
        FloatArray normals = new FloatArray();
        IntArray indices = new IntArray();

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

        Mesh3 mesh = new Mesh3();
        float[] v = vertices.toFArray();
        if (preReturn != null) {
            preReturn.accept(mesh, v, index);
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
        AIScene scene = load(cl, filename, flags);
        List<Material> materials = createMaterials(cl, scene, filename);
        int numMeshes = scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(requireNonNull(aiMeshes).get(i));
            Mesh mesh = processMesh(aiMesh, materials);
            meshes[i] = mesh;
        }
        aiReleaseImport(scene);
        return new ObjModel2(meshes);
    }

    /**
     * Load object file with default flags.
     *
     * @param cl        Class loader
     * @param filename  Object filename in classpath.
     * @param preReturn Set attribute index before return.
     * @return Meshes v3.
     */
    public static ObjModel3 load3(ClassLoader cl,
                                  String filename,
                                  @Nullable PreReturn preReturn) {
        return load3(cl, filename, DEFAULT_FLAGS, preReturn);
    }

    /**
     * Load object file.
     *
     * @param cl        Class loader
     * @param filename  Object filename in classpath (in relative path).
     * @param flags     Assimp flags.
     * @param preReturn Set attribute index before return.
     * @return Meshes v3.
     */
    public static ObjModel3 load3(ClassLoader cl,
                                  String filename,
                                  int flags,
                                  @Nullable PreReturn preReturn) {
        AIScene scene = load(cl, filename, flags);
        List<Material> materials = createMaterials(cl, scene, filename);
        int numMeshes = scene.mNumMeshes();
        PointerBuffer aiMeshes = scene.mMeshes();
        Mesh3[] meshes = new Mesh3[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(requireNonNull(aiMeshes).get(i));
            Mesh3 mesh = processMesh(aiMesh, materials, preReturn, i)
                .unbindVao();
            meshes[i] = mesh;
        }
        aiReleaseImport(scene);
        return new ObjModel3(meshes);
    }
}
