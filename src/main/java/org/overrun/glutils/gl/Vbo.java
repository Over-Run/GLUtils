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

package org.overrun.glutils.gl;

import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 1.4.0
 */
public class Vbo {
    private final int target;
    private final int id = glGenBuffers();

    /**
     * Gen buffer and set target.
     *
     * @param target the target to which the buffer object is bound. One of:<br>
     *               <table>
     *                  <caption>GL Buffer Target Constants</caption>
     *                   <tr>
     *                       <td>{@link GL15C#GL_ARRAY_BUFFER ARRAY_BUFFER}</td>
     *                       <td>{@link GL15C#GL_ELEMENT_ARRAY_BUFFER ELEMENT_ARRAY_BUFFER}</td>
     *                       <td>{@link GL21#GL_PIXEL_PACK_BUFFER PIXEL_PACK_BUFFER}</td>
     *                       <td>{@link GL21#GL_PIXEL_UNPACK_BUFFER PIXEL_UNPACK_BUFFER}</td>
     *                   </tr>
     *                   <tr>
     *                       <td>{@link GL30#GL_TRANSFORM_FEEDBACK_BUFFER TRANSFORM_FEEDBACK_BUFFER}</td>
     *                       <td>{@link GL31#GL_UNIFORM_BUFFER UNIFORM_BUFFER}</td>
     *                       <td>{@link GL31#GL_TEXTURE_BUFFER TEXTURE_BUFFER}</td>
     *                       <td>{@link GL31#GL_COPY_READ_BUFFER COPY_READ_BUFFER}</td>
     *                   </tr>
     *                   <tr>
     *                       <td>{@link GL31#GL_COPY_WRITE_BUFFER COPY_WRITE_BUFFER}</td>
     *                       <td>{@link GL40#GL_DRAW_INDIRECT_BUFFER DRAW_INDIRECT_BUFFER}</td>
     *                       <td>{@link GL42#GL_ATOMIC_COUNTER_BUFFER ATOMIC_COUNTER_BUFFER}</td>
     *                       <td>{@link GL43#GL_DISPATCH_INDIRECT_BUFFER DISPATCH_INDIRECT_BUFFER}</td>
     *                   </tr>
     *                   <tr>
     *                       <td>{@link GL43#GL_SHADER_STORAGE_BUFFER SHADER_STORAGE_BUFFER}</td>
     *                       <td>{@link ARBIndirectParameters#GL_PARAMETER_BUFFER_ARB PARAMETER_BUFFER_ARB}</td>
     *                   </tr>
     *               </table>
     */
    public Vbo(final int target) {
        this.target = target;
    }

    /**
     * Bind buffer.
     */
    public void bind() {
        glBindBuffer(target, id);
    }

    /**
     * Unbind buffer.
     *
     * @since 1.5.0
     */
    public void unbind() {
        glBindBuffer(target, 0);
    }

    /**
     * Set data.
     *
     * @param data  The data.
     * @param usage The usage of the data.
     */
    public void data(final float[] data,
                     final int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Set data.
     *
     * @param data  The data.
     * @param usage The usage of the data.
     * @since 1.6.0
     */
    public void data(final FloatBuffer data,
                     final int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Set data.
     *
     * @param data  The data.
     * @param usage The usage of the data.
     */
    public void data(final int[] data,
                     final int usage) {
        glBufferData(target, data, usage);
    }

    /**
     * Set data.
     *
     * @param data   The data.
     * @param offset The offset.
     * @since 1.5.0
     */
    public void subData(final float[] data,
                        final long offset) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Set data.
     *
     * @param data   The data.
     * @param offset The offset.
     * @since 1.5.0
     */
    public void subData(final int[] data,
                        final long offset) {
        glBufferSubData(target, offset, data);
    }

    /**
     * Get target for direct operations.
     *
     * @return The target of this vertex buffer object.
     */
    public int getTarget() {
        return target;
    }

    /**
     * Get id for direct operations.
     *
     * @return The id of this vertex buffer object.
     */
    public int getId() {
        return id;
    }

    /**
     * Free vertex buffer object.
     *
     * @since 1.5.0
     */
    public void free() {
        glDeleteBuffers(id);
    }
}
