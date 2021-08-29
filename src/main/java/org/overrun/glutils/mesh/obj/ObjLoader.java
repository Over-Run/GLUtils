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

import org.lwjgl.assimp.AIScene;
import org.overrun.commonutils.FloatArray;
import org.overrun.commonutils.IntArray;
import org.overrun.commonutils.MapStr2Obj;
import org.overrun.glutils.mesh.Mesh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.util.Objects.requireNonNull;
import static org.lwjgl.assimp.Assimp.*;
import static org.overrun.commonutils.ArrayHelper.toIArray;

/**
 * @author squid233
 * @since 1.2.0
 */
public class ObjLoader {
    private static File tempDir;

    /**
     * Load object file with default flags.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath.
     * @return Mesh.
     * @throws Exception IOE or RE
     */
    public static Mesh load2(ClassLoader cl,
                             String filename)
            throws Exception {
        return load2(cl,
                filename,
                aiProcess_JoinIdenticalVertices
                        | aiProcess_Triangulate
                        | aiProcess_FixInfacingNormals);
    }

    /**
     * Load object file.
     *
     * @param cl       Class loader
     * @param filename Object filename in classpath (in relative path).
     * @param flags    Assimp flags.
     * @return Mesh.
     * @throws Exception IOE or RE
     */
    public static Mesh load2(ClassLoader cl,
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
            bw.write(br.readLine());
        }
        AIScene scene = aiImportFile(ffn, flags);
        if (scene == null) {
            throw new RuntimeException("Error loading model");
            // TODO: 2021/8/28
        }
    }
}
