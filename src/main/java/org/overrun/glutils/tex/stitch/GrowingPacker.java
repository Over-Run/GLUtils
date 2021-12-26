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

package org.overrun.glutils.tex.stitch;

import org.jetbrains.annotations.Nullable;

/**
 * Thanks for <a href="https://codeincomplete.com/articles/bin-packing/">this
 * article</a>.
 * <p>
 * Java port of <a href=
 * "https://github.com/jakesgordon/bin-packing/blob/master/js/packer.growing.js"
 * ><code>GrowingPacker</code></a>.
 * </p>
 *
 * @author squid233
 * @since 2.0.0
 */
public class GrowingPacker {
    public Node root;

    public void fit(Node... blocks) {
        Node node;
        int len = blocks.length;
        int w, h;
        if (len > 0) {
            var b0 = blocks[0];
            w = b0.w;
            h = b0.h;
        } else {
            w = 0;
            h = 0;
        }
        root = new Node(0, 0, w, h);
        for (var block : blocks) {
            if ((node = findNode(root, block.w, block.h)) != null) {
                block.fit = splitNode(node, block.w, block.h);
            } else {
                block.fit = growNode(block.w, block.h);
            }
        }
    }

    @Nullable
    public Node findNode(Node root,
                         int w,
                         int h) {
        if (root.used) {
            var r = findNode(root.right, w, h);
            return r != null ? r : findNode(root.down, w, h);
        }
        if ((w <= root.w) && (h <= root.h)) {
            return root;
        }
        return null;
    }

    public Node splitNode(Node node,
                          int w,
                          int h) {
        node.used = true;
        node.down = new Node(node.x, node.y + h, node.w, node.h - h);
        node.right = new Node(node.x + w, node.y, node.w - w, h);
        return node;
    }

    @Nullable
    public Node growNode(int w,
                         int h) {
        var canGrowDown = w <= root.w;
        var canGrowRight = h <= root.h;
        // attempt to keep square-ish by growing right when height is much greater than width
        var shouldGrowRight = canGrowRight && (root.h >= (root.w + w));
        // attempt to keep square-ish by growing down  when width  is much greater than height
        var shouldGrowDown = canGrowDown && (root.w >= (root.h + h));
        if (shouldGrowRight) {
            return growRight(w, h);
        }
        if (shouldGrowDown) {
            return growDown(w, h);
        }
        if (canGrowRight) {
            return growRight(w, h);
        }
        if (canGrowDown) {
            return growDown(w, h);
        }
        // need to ensure sensible root starting size to avoid this happening
        return null;
    }

    @Nullable
    public Node growRight(int w,
                          int h) {
        root = new Node(true,
            0,
            0,
            root.w + w,
            root.h,
            root,
            new Node(root.w, 0, w, root.h));
        Node node;
        if ((node = findNode(root, w, h)) != null) {
            return splitNode(node, w, h);
        }
        return null;
    }

    @Nullable
    public Node growDown(int w,
                         int h) {
        root = new Node(
            true,
            0,
            0,
            root.w,
            root.h + h,
            new Node(0, root.h, root.w, h),
            root
        );
        Node node;
        if ((node = findNode(root, w, h)) != null) {
            return splitNode(node, w, h);
        }
        return null;
    }
}
