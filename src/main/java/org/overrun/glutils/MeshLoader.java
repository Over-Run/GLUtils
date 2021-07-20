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

package org.overrun.glutils;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static org.overrun.glutils.ArrayHelper.removeNull;

/**
 * @author squid233
 * @since 0.7.0
 */
public class MeshLoader {
    private static final List<String> KEYWORDS = Arrays.asList(
            "var",
            "set",
            "vertices",
            "color",
            "texCoords",
            "v",
            "vc",
            "vt",
            "f"
    );

    public static MeshVar var(String name,
                              String value) {
        return new MeshVar(name, value);
    }

    public static MeshVar var(String name,
                              float value) {
        return new MeshVar(name, String.valueOf(value));
    }

    public static MapStr2Str vars(MeshVar... vars) {
        MapStr2Str map = new MapStr2Str();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < vars.length; i++) {
            MeshVar var = vars[i];
            map.put(var.name, var.value);
        }
        return map;
    }

    private static void replaceByVar(String[] arr,
                                     List<String> definedVars,
                                     MapStr2Str vars) {
        for (int i = 1; i < arr.length; i++) {
            String var = arr[i].replaceAll("[`~!@#%^&*()\\-=+\\[{}\\];'\\\\:\"|,./<>?]", "");
            if (definedVars.contains(var)) {
                arr[i] = arr[i].replace(var, vars.get(var));
            }
        }
    }

    public static MeshFile loadf(ClassLoader cl,
                                 String file,
                                 MeshVar... vars)
            throws Exception {
        try (InputStream is = cl.getResourceAsStream(file);
             Scanner sc = new Scanner(Objects.requireNonNull(is))) {
            MeshFile mf = new MeshFile();
            List<String> definedVars = new ArrayList<>();
            MapStr2Str vmap = vars(vars);
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
                    // variables
                    case "var": {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        for (int i = 1; i < arr.length; i++) {
                            String var = arr[i];
                            if (!KEYWORDS.contains(var)) {
                                definedVars.add(var);
                            }
                        }
                        break;
                    }
                    // variables end
                    // settings
                    case "set": {
                        // check length
                        if (arr.length < 3) {
                            except("required 2 params but found " +
                                    (arr.length - 1) + " param", currLn);
                        }
                        replaceByVar(arr, definedVars, vmap);
                        // set vertDim
                        String p2 = arr[2];
                        try {
                            switch (arr[1]) {
                                case "vertices":
                                    mf.vertDim = parseInt(p2);
                                    break;
                                case "colors":
                                    mf.colorDim = parseInt(p2);
                                    break;
                                case "texCoords":
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
                    case "v": {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByVar(arr, definedVars, vmap);
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.vertices.add(parseFloat(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    case "vc": {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByVar(arr, definedVars, vmap);
                        for (int i = 1; i < arr.length; i++) {
                            try {
                                mf.colors.add(parseFloat(arr[i]));
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        break;
                    }
                    case "vt": {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByVar(arr, definedVars, vmap);
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
                    case "f": {
                        // check length
                        if (arr.length < 2) {
                            except("required 1 param at least but found 0",
                                    currLn);
                        }
                        replaceByVar(arr, definedVars, vmap);
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
           MeshVar... vars)
            throws Exception {
        MeshFile mf = loadf(cl, file, vars);
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
                            MeshVar... vars)
            throws Exception {
        return load(cl, file, null, Mesh.class, vars);
    }

    public static Mesh3 load3(ClassLoader cl,
                              String file,
                              Consumer<Mesh3> pre,
                              MeshVar... vars)
            throws Exception {
        return load(cl, file, pre, Mesh3.class, vars);
    }

    public static void except(String msg,
                              int currLn) {
        throw new CompileException("Error loading mesh at line " +
                currLn + ": " + msg);
    }
}
