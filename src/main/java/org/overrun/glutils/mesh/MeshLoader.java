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

package org.overrun.glutils.mesh;

import org.overrun.commonutils.MapStr2Str;
import org.overrun.glutils.CompileException;
import org.overrun.glutils.GLUtils;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.overrun.glutils.mesh.Assemblies.*;

/**
 * @author squid233
 * @since 0.7.0
 */
public class MeshLoader {
    private static final List<String> KEYWORDS = Arrays.asList(
            DEFINE,
            UNDEF,
            REPEAT,
            SET,
            OPT_VERT_DIM,
            OPT_COL_DIM,
            OPT_TEX_DIM,
            VERT,
            VERT_COL,
            VERT_TEX,
            FACE
    );

    /**
     * define a macro
     *
     * @param name  macro name
     * @param value macro value
     * @return obj
     * @since 1.0.0
     */
    public static MeshMacro def(String name,
                                String value) {
        return new MeshMacro(name, value);
    }

    /**
     * define a macro
     *
     * @param name  macro name
     * @param value macro value
     * @return obj
     * @since 1.0.0
     */
    public static MeshMacro def(String name,
                                float value) {
        return def(name, String.valueOf(value));
    }

    private static String[] replaceByMacro(String[] arr,
                                           Set<String> definedMacros,
                                           MapStr2Str macros) {
        return replaceByMacro(arr, definedMacros, 1, macros);
    }

    private static String[] replaceByMacro(String[] arr,
                                           Set<String> definedMacros,
                                           int offset,
                                           MapStr2Str macros) {
        boolean hasSpace = false;
        for (int i = offset; i < arr.length; i++) {
            // remove invalid chars and calculating signs
            String macro =
                    arr[i].replaceAll("[`~!@#%^&*()\\-=+\\[{}\\];'\\\\:\"|,./<>?]", "");
            if (definedMacros.contains(macro)) {
                String v = macros.get(macro);
                arr[i] = arr[i].replace(macro, v);
                if (v.matches(".+\\s.+")) {
                    hasSpace = true;
                }
            }
        }
        if (hasSpace) {
            int l = 0;
            for (String s : arr) {
                l += s.split("\\s+").length;
            }
            String[] arr1 = new String[l];
            int i = 0;
            for (String s : arr) {
                String[] arr2 = s.split("\\s+");
                for (String value : arr2) {
                    arr1[i] = value;
                    ++i;
                }
            }
            return arr1;
        }
        return arr;
    }

    private static boolean isIdValid(String id) {
        return id.matches("^[A-Za-z$_][^`~!@#%^&*()\\-=+\\[{}\\];'\\\\:\"|,./<>?]?[A-Za-z0-9$_]*");
    }

    private static boolean isIdInvalid(String id) {
        return !isIdValid(id);
    }

    /**
     * define macros
     *
     * @param macros macros
     * @return map
     * @since 1.0.0
     */
    public static MapStr2Str def(MeshMacro... macros) {
        MapStr2Str map = new MapStr2Str();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < macros.length; i++) {
            MeshMacro macro = macros[i];
            map.put(macro.name, macro.value);
        }
        return map;
    }

    /**
     * load to file
     *
     * @param cl     class loader
     * @param file   filename
     * @param macros macros
     * @return mesh file
     * @throws Exception occurred exception on compiling
     */
    public static MeshFile loadf(ClassLoader cl,
                                 String file,
                                 MeshMacro... macros)
            throws Exception {
        try (InputStream is = cl.getResourceAsStream(file);
             Scanner sc = new Scanner(Objects.requireNonNull(is),
                     UTF_8.name())) {
            MeshFile mf = new MeshFile();
            Set<String> definedMacros = new HashSet<>();
            MapStr2Str mmap = def(macros);
            int currLn = 0;
            while (sc.hasNextLine()) {
                ++currLn;
                String ln = sc.nextLine().trim();
                // ignore comment
                if (ln.startsWith("#") || ln.startsWith("//")) {
                    continue;
                }
                String[] arr = ln.split("\\s+");
                // ignore empty line
                if (arr.length == 0) {
                    continue;
                }
                switch (arr[0]) {
                    // macros
                    case DEFINE: {
                        // check length
                        if (arr.length < 2) {
                            except("Required 1 param at least but found 0",
                                    currLn);
                        }
                        // check identifier whether valid
                        if (isIdInvalid(arr[1])) {
                            except("Invalid identifier",
                                    currLn);
                        }
                        String[] arr2 = ln.split("\\s+", 3);
                        String macro = arr2[1];
                        if (!KEYWORDS.contains(macro)) {
                            definedMacros.add(macro);
                        }
                        if (arr2.length > 2) {
                            mmap.put(macro, arr2[2]);
                        }
                        break;
                    }
                    case UNDEF: {
                        // check length
                        if (arr.length != 2) {
                            except("Required 1 param but found " + (arr.length - 2),
                                    currLn);
                        }
                        String macro = arr[1];
                        // check whether defined
                        if (!definedMacros.contains(macro)) {
                            except("Macro " + macro + " not defined.",
                                    currLn);
                        }
                        definedMacros.remove(macro);
                        mmap.remove(macro);
                        break;
                    }
                    case REPEAT: {
                        // check length
                        if (arr.length < 4) {
                            except("Required 3 param at least but found " + (arr.length - 1),
                                    currLn);
                        }
                        // check identifier whether valid
                        if (isIdInvalid(arr[1])) {
                            except("Invalid identifier",
                                    currLn);
                        }
                        String[] arr2 = ln.split("\\s+", 4);
                        String macro = arr2[1];
                        if (!KEYWORDS.contains(macro)) {
                            definedMacros.add(macro);
                        }
                        String p2 = arr[2];
                        int count = 0;
                        try {
                            count = parseInt(p2);
                        } catch (NumberFormatException e) {
                            except(p2 + " isn't a integer", currLn);
                        }
                        String p3 = arr2[3];
                        StringBuilder result = new StringBuilder(p3);
                        for (int i = 1; i < count; i++) {
                            result.append(" ").append(p3);
                        }
                        mmap.put(macro, result.toString());
                        break;
                    }
                    // macros end
                    // settings
                    case SET: {
                        // check length
                        if (arr.length != 3) {
                            except("Required 2 params but found " +
                                    (arr.length - 1) + " param", currLn);
                        }
                        arr = replaceByMacro(arr, definedMacros, 2, mmap);
                        // set vertDim
                        String p2 = arr[2];
                        try {
                            switch (arr[1]) {
                                case OPT_VERT_DIM:
                                    mf.vertDim = parseInt(p2);
                                    break;
                                case OPT_COL_DIM:
                                    mf.colorDim = parseInt(p2);
                                    break;
                                case OPT_TEX_DIM:
                                    mf.texDim = parseInt(p2);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            except(p2 + " isn't a number", currLn);
                        }
                        break;
                    }
                    // settings end
                    // vertices
                    case VERT: {
                        // check length
                        if (arr.length < 2) {
                            except("Required 1 param at least but found 0",
                                    currLn);
                        }
                        arr = replaceByMacro(arr, definedMacros, mmap);
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.vertices.add(parseFloat(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    case VERT_COL: {
                        // check length
                        if (arr.length < 2) {
                            except("Required 1 param at least but found 0",
                                    currLn);
                        }
                        arr = replaceByMacro(arr, definedMacros, mmap);
                        mf.colored = true;
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.colors.add(parseFloat(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    case VERT_TEX: {
                        // check length
                        if (arr.length < 2) {
                            except("Required 1 param at least but found 0",
                                    currLn);
                        }
                        arr = replaceByMacro(arr, definedMacros, mmap);
                        mf.textured = true;
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.texCoords.add(parseFloat(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    // vertices end
                    // face
                    case FACE: {
                        // check length
                        if (arr.length < 2) {
                            except("Required 1 param at least but found 0",
                                    currLn);
                        }
                        arr = replaceByMacro(arr, definedMacros, mmap);
                        mf.indexed = true;
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.indices.add(parseInt(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    // face end
                }
            }
            return mf;
        }
    }

    /**
     * load
     *
     * @param cl     class loader
     * @param file   filename
     * @param pre    pre-operations
     * @param clazz  target class
     * @param macros macros
     * @param <T>    mesh type
     * @return mesh
     * @throws Exception occurred exception on compiling
     */
    public static <T extends BaseMesh<T>>
    T load(ClassLoader cl,
           String file,
           Consumer<T> pre,
           Class<T> clazz,
           MeshMacro... macros)
            throws Exception {
        MeshFile mf = loadf(cl, file, macros);
        T mesh = GLUtils.newClass(clazz);
        if (pre != null) {
            pre.accept(mesh);
        }
        mesh.vertDim(mf.vertDim).vertices(mf.vertices.toFArray());
        if (mf.colored) {
            mesh.colorDim(mf.colorDim).colors(mf.colors.toFArray());
        }
        if (mf.textured) {
            mesh.texDim(mf.texDim).texCoords(mf.texCoords.toFArray());
        }
        if (mf.indexed) {
            mesh.indices(mf.indices.toIArray());
        }
        return mesh;
    }

    /**
     * load
     *
     * @param cl     class loader
     * @param file   filename
     * @param macros macros
     * @return mesh
     * @throws Exception occurred exception on compiling
     */
    public static Mesh load(ClassLoader cl,
                            String file,
                            MeshMacro... macros)
            throws Exception {
        return load(cl, file, null, Mesh.class, macros);
    }

    /**
     * load
     *
     * @param cl     class loader
     * @param file   filename
     * @param pre    pre-operations
     * @param macros macros
     * @return mesh
     * @throws Exception occurred exception on compiling
     */
    public static Mesh3 load3(ClassLoader cl,
                              String file,
                              Consumer<Mesh3> pre,
                              MeshMacro... macros)
            throws Exception {
        return load(cl, file, pre, Mesh3.class, macros).unbindVao();
    }

    /**
     * exception
     *
     * @param msg    message
     * @param currLn current line
     */
    public static void except(String msg,
                              int currLn)
            throws CompileException {
        throw new CompileException("Error loading mesh at line " +
                currLn + ": " + msg);
    }
}
