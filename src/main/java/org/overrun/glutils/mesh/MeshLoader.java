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

package org.overrun.glutils.mesh;

import org.overrun.glutils.CompileException;
import org.overrun.glutils.MapStr2Str;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static org.overrun.glutils.ArrayHelper.removeNull;
import static org.overrun.glutils.mesh.Assemblies.*;

/**
 * @author squid233
 * @since 0.7.0
 */
public class MeshLoader {
    private static final List<String> KEYWORDS = Arrays.asList(
            DEFINE,
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

    private static void replaceByMacro(String[] arr,
                                       List<String> definedMacros,
                                       MapStr2Str macros) {
        for (int i = 1; i < arr.length; i++) {
            String macro =
                    arr[i].replaceAll("[`~!@#%^&*()\\-=+\\[{}\\];'\\\\:\"|,./<>?]", "");
            if (definedMacros.contains(macro)) {
                arr[i] = arr[i].replace(macro, macros.get(macro));
            }
        }
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

    public static MeshFile loadf(ClassLoader cl,
                                 String file,
                                 MeshMacro... macros)
            throws Exception {
        try (InputStream is = cl.getResourceAsStream(file);
             Scanner sc = new Scanner(Objects.requireNonNull(is))) {
            MeshFile mf = new MeshFile();
            List<String> definedMacros = new ArrayList<>();
            MapStr2Str mmap = def(macros);
            int currLn = 0;
            while (sc.hasNextLine()) {
                ++currLn;
                String ln = sc.nextLine().trim();
                // ignore comment
                if (ln.startsWith("#") || ln.startsWith("||")) {
                    continue;
                }
                String[] arr = removeNull(ln.split("\\s"));
                // ignore empty line
                if (arr.length == 0) {
                    continue;
                }
                switch (arr[0]) {
                    // macros
                    case DEFINE: {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        for (int i = 1; i < arr.length; i++) {
                            String macro = arr[i];
                            if (!KEYWORDS.contains(macro)) {
                                definedMacros.add(macro);
                            }
                        }
                        break;
                    }
                    // macros end
                    // settings
                    case SET: {
                        // check length
                        if (arr.length < 3) {
                            except("required 2 params but found " +
                                    (arr.length - 1) + " param", currLn);
                        }
                        replaceByMacro(arr, definedMacros, mmap);
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
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByMacro(arr, definedMacros, mmap);
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
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByMacro(arr, definedMacros, mmap);
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
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByMacro(arr, definedMacros, mmap);
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
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByMacro(arr, definedMacros, mmap);
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

    public static <T extends BaseMesh<T>>
    T load(ClassLoader cl,
           String file,
           Consumer<T> pre,
           Class<T> tClass,
           MeshMacro... macros)
            throws Exception {
        MeshFile mf = loadf(cl, file, macros);
        T mesh = tClass.getDeclaredConstructor().newInstance();
        if (pre != null) {
            pre.accept(mesh);
        }
        mesh.vertDim(mf.vertDim)
                .vertices(mf.vertices.toFArray())
                .colorDim(mf.colorDim)
                .colors(mf.colors.toFArray());
        if (mf.textured) {
            mesh.texDim(mf.texDim)
                    .texCoords(mf.texCoords.toFArray());
        }
        if (mf.indexed) {
            mesh.indices(mf.indices.toIArray());
        }
        return mesh;
    }

    public static Mesh load(ClassLoader cl,
                            String file,
                            MeshMacro... macros)
            throws Exception {
        return load(cl, file, null, Mesh.class, macros);
    }

    public static Mesh3 load3(ClassLoader cl,
                              String file,
                              Consumer<Mesh3> pre,
                              MeshMacro... macros)
            throws Exception {
        return load(cl, file, pre, Mesh3.class, macros);
    }

    public static void except(String msg,
                              int currLn) {
        throw new CompileException("Error loading mesh at line " +
                currLn + ": " + msg);
    }
}
