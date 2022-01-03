/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package org.overrun.glutils.gl.ll;

import org.lwjgl.BufferUtils;
import org.overrun.glutils.tex.Textures;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.overrun.glutils.gl.ll.GLU.GLU_INVALID_ENUM;
import static org.overrun.glutils.gl.ll.GLU.GLU_INVALID_VALUE;
import static org.overrun.glutils.gl.ll.Util.*;
import static org.overrun.glutils.util.math.Maths.ceilDiv;

/**
 * @author squid233
 * @since 2.0.0
 */
public class MipMap {
    public static int gluBuild2DMipmaps(
        int target,
        int components,
        int width,
        int height,
        int format,
        int type,
        ByteBuffer data) {
        if (width < 1 || height < 1)
            return GLU_INVALID_VALUE;

        final int bpp = bytesPerPixel(format, type);
        if (bpp == 0)
            return GLU_INVALID_ENUM;

        final int maxSize = Textures.getMaxSize();

        int w = nearestPower(width);
        if (w > maxSize)
            w = maxSize;

        int h = nearestPower(height);
        if (h > maxSize) {
            h = maxSize;
        }

        // Get current glPixelStore state
        PixelStoreState pss = new PixelStoreState();

        // set pixel packing
        glPixelStorei(GL_PACK_ROW_LENGTH, 0);
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glPixelStorei(GL_PACK_SKIP_ROWS, 0);
        glPixelStorei(GL_PACK_SKIP_PIXELS, 0);

        ByteBuffer image;
        int retVal = 0;
        boolean done = false;

        if (w != width || h != height) {
            // must rescale image to get "top" mipmap texture image
            image = BufferUtils.createByteBuffer((w + 4) * h * bpp);
            int error = gluScaleImage(format, width, height, type, data, w, h, type, image);
            if (error != 0) {
                retVal = error;
                done = true;
            }

            /* set pixel unpacking */
            glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
        } else {
            image = data;
        }

        ByteBuffer bufferA = null;
        ByteBuffer bufferB = null;

        int level = 0;
        while (!done) {
            if (image != data) {
                /* set pixel unpacking */
                glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
                glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
            }

            glTexImage2D(target, level, components, w, h, 0, format, type, image);

            if (w == 1 && h == 1)
                break;

            final int newW = (w < 2) ? 1 : w >> 1;
            final int newH = (h < 2) ? 1 : h >> 1;

            final ByteBuffer newImage;

            if (bufferA == null)
                newImage = (bufferA = BufferUtils.createByteBuffer((newW + 4) * newH * bpp));
            else if (bufferB == null)
                newImage = (bufferB = BufferUtils.createByteBuffer((newW + 4) * newH * bpp));
            else
                newImage = bufferB;

            int error = gluScaleImage(format, w, h, type, image, newW, newH, type, newImage);
            if (error != 0) {
                retVal = error;
                done = true;
            }

            image = newImage;
            if (bufferB != null)
                bufferB = bufferA;

            w = newW;
            h = newH;
            level++;
        }

        // Restore original glPixelStore state
        pss.save();

        return retVal;
    }

    public static int gluScaleImage(
        int format,
        int widthIn,
        int heightIn,
        int typeIn,
        ByteBuffer dataIn,
        int widthOut,
        int heightOut,
        int typeOut,
        ByteBuffer dataOut) {
        final int components = compPerPix(format);
        if (components == -1)
            return GLU_INVALID_ENUM;

        int i, j, k;
        float[] tempIn, tempOut;
        float sx, sy;
        int sizein, sizeout;
        int rowstride, rowlen;

        // temp image data
        tempIn = new float[widthIn * heightIn * components];
        tempOut = new float[widthOut * heightOut * components];

        // Determine bytes per input type
        switch (typeIn) {
            case GL_UNSIGNED_BYTE:
                sizein = 1;
                break;
            case GL_FLOAT:
                sizein = 4;
                break;
            default:
                return GL_INVALID_ENUM;
        }

        // Determine bytes per output type
        switch (typeOut) {
            case GL_UNSIGNED_BYTE:
                sizeout = 1;
                break;
            case GL_FLOAT:
                sizeout = 4;
                break;
            default:
                return GL_INVALID_ENUM;
        }

        // Get glPixelStore state
        PixelStoreState pss = new PixelStoreState();

        //Unpack the pixel data and convert to floating point
        if (pss.unpackRowLength > 0)
            rowlen = pss.unpackRowLength;
        else
            rowlen = widthIn;

        if (sizein >= pss.unpackAlignment)
            rowstride = components * rowlen;
        else
            rowstride = pss.unpackAlignment / sizein * ceilDiv(components * rowlen * sizein, pss.unpackAlignment);

        switch (typeIn) {
            case GL_UNSIGNED_BYTE:
                k = 0;
                dataIn.rewind();
                for (i = 0; i < heightIn; i++) {
                    int ubptr = i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components;
                    for (j = 0; j < widthIn * components; j++) {
                        tempIn[k++] = dataIn.get(ubptr++) & 0xff;
                    }
                }
                break;
            case GL_FLOAT:
                k = 0;
                dataIn.rewind();
                for (i = 0; i < heightIn; i++) {
                    int fptr = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);
                    for (j = 0; j < widthIn * components; j++) {
                        tempIn[k++] = dataIn.getFloat(fptr);
                        fptr += 4;
                    }
                }
                break;
            default:
                return GLU_INVALID_ENUM;
        }

        // Do scaling
        sx = (float) widthIn / (float) widthOut;
        sy = (float) heightIn / (float) heightOut;

        float[] c = new float[components];
        int src, dst;

        for (int iy = 0; iy < heightOut; iy++) {
            for (int ix = 0; ix < widthOut; ix++) {
                int x0 = (int) (ix * sx);
                int x1 = (int) ((ix + 1) * sx);
                int y0 = (int) (iy * sy);
                int y1 = (int) ((iy + 1) * sy);

                int readPix = 0;

                // reset weighted pixel
                for (int ic = 0; ic < components; ic++) {
                    c[ic] = 0;
                }

                // create weighted pixel
                for (int ix0 = x0; ix0 < x1; ix0++) {
                    for (int iy0 = y0; iy0 < y1; iy0++) {

                        src = (iy0 * widthIn + ix0) * components;

                        for (int ic = 0; ic < components; ic++) {
                            c[ic] += tempIn[src + ic];
                        }

                        readPix++;
                    }
                }

                // store weighted pixel
                dst = (iy * widthOut + ix) * components;

                if (readPix == 0) {
                    // Image is sized up, caused by non power of two texture as input
                    src = (y0 * widthIn + x0) * components;
                    for (int ic = 0; ic < components; ic++) {
                        tempOut[dst++] = tempIn[src + ic];
                    }
                } else {
                    // sized down
                    for (k = 0; k < components; k++) {
                        tempOut[dst++] = c[k] / readPix;
                    }
                }
            }
        }


        // Convert temp output
        if (pss.packRowLength > 0)
            rowlen = pss.packRowLength;
        else
            rowlen = widthOut;

        if (sizeout >= pss.packAlignment)
            rowstride = components * rowlen;
        else
            rowstride = pss.packAlignment / sizeout * ceilDiv(components * rowlen * sizeout, pss.packAlignment);

        switch (typeOut) {
            case GL_UNSIGNED_BYTE:
                k = 0;
                for (i = 0; i < heightOut; i++) {
                    int ubptr = i * rowstride + pss.packSkipRows * rowstride + pss.packSkipPixels * components;

                    for (j = 0; j < widthOut * components; j++) {
                        dataOut.put(ubptr++, (byte) tempOut[k++]);
                    }
                }
                break;
            case GL_FLOAT:
                k = 0;
                for (i = 0; i < heightOut; i++) {
                    int fptr = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);

                    for (j = 0; j < widthOut * components; j++) {
                        dataOut.putFloat(fptr, tempOut[k++]);
                        fptr += 4;
                    }
                }
                break;
            default:
                return GLU_INVALID_ENUM;
        }

        return 0;
    }
}
