package gl.vboutils;

import color.Color;

/**
 * Provides methods for creating VBO data representing a cube.
 *
 * @author MTomczyk
 */
public class Cube
{
    /**
     * Fills cube vertex + index + color (optional) data in provided arrays.
     * It is assumed that a cube is represented as 8 edges, i.e., to be rendered using GL_LINES option.
     *
     * @param x        cube x-coordinate
     * @param y        cube y-coordinate
     * @param z        cube z-coordinate
     * @param hSize    cube size (edge length by two)
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    color data
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    public static void fillCubeEdgesData(float x, float y, float z, float hSize,
                                         float[] v, int[] ii, short[] is, float[] c,
                                         OffsetStride os, Color color,
                                         boolean useAlpha)
    {
        fillCubeEdgesData(x, y, z, hSize, hSize, hSize, v, ii, is, c, os, color, useAlpha);
    }


    /**
     * Fills cube vertex + index + color (optional) data in provided arrays.
     * It is assumed that a cube is represented as 8 edges, i.e., to be rendered using GL_LINES option.
     *
     * @param x        cube x-coordinate
     * @param y        cube y-coordinate
     * @param z        cube z-coordinate
     * @param hWidth   cube size along the x dimension (edge length by two)
     * @param hHeight  cube size along the y dimension (edge length by two)
     * @param hDepth   cube size along the z dimension (edge length by two)
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    color data
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    public static void fillCubeEdgesData(float x, float y, float z,
                                         float hWidth, float hHeight, float hDepth,
                                         float[] v, int[] ii, short[] is, float[] c,
                                         OffsetStride os, Color color,
                                         boolean useAlpha)
    {
        fillCubeVertices(v, os._vaOffset, x, y, z, hWidth, hHeight, hDepth);
        if (c != null) fillCubeVerticesColors(c, os._caOffset, color, useAlpha);

        if (ii != null)
        {
            //front
            ii[os._iaOffset] = os._iOffset;
            ii[os._iaOffset + 1] = os._iOffset + 1;
            ii[os._iaOffset + 2] = os._iOffset + 1;
            ii[os._iaOffset + 3] = os._iOffset + 2;
            ii[os._iaOffset + 4] = os._iOffset + 2;
            ii[os._iaOffset + 5] = os._iOffset + 3;
            ii[os._iaOffset + 6] = os._iOffset + 3;
            ii[os._iaOffset + 7] = os._iOffset;
            // middle
            ii[os._iaOffset + 8] = os._iOffset;
            ii[os._iaOffset + 9] = os._iOffset + 4;
            ii[os._iaOffset + 10] = os._iOffset + 1;
            ii[os._iaOffset + 11] = os._iOffset + 5;
            ii[os._iaOffset + 12] = os._iOffset + 2;
            ii[os._iaOffset + 13] = os._iOffset + 6;
            ii[os._iaOffset + 14] = os._iOffset + 3;
            ii[os._iaOffset + 15] = os._iOffset + 7;
            //back
            ii[os._iaOffset + 16] = os._iOffset + 4;
            ii[os._iaOffset + 17] = os._iOffset + 5;
            ii[os._iaOffset + 18] = os._iOffset + 5;
            ii[os._iaOffset + 19] = os._iOffset + 6;
            ii[os._iaOffset + 20] = os._iOffset + 6;
            ii[os._iaOffset + 21] = os._iOffset + 7;
            ii[os._iaOffset + 22] = os._iOffset + 7;
            ii[os._iaOffset + 23] = os._iOffset + 4;
        }
        else
        {
            //front
            is[os._iaOffset] = (short) os._iOffset;
            is[os._iaOffset + 1] = (short) (os._iOffset + 1);
            is[os._iaOffset + 2] = (short) (os._iOffset + 1);
            is[os._iaOffset + 3] = (short) (os._iOffset + 2);
            is[os._iaOffset + 4] = (short) (os._iOffset + 2);
            is[os._iaOffset + 5] = (short) (os._iOffset + 3);
            is[os._iaOffset + 6] = (short) (os._iOffset + 3);
            is[os._iaOffset + 7] = (short) os._iOffset;
            // middle
            is[os._iaOffset + 8] = (short) os._iOffset;
            is[os._iaOffset + 9] = (short) (os._iOffset + 4);
            is[os._iaOffset + 10] = (short) (os._iOffset + 1);
            is[os._iaOffset + 11] = (short) (os._iOffset + 5);
            is[os._iaOffset + 12] = (short) (os._iOffset + 2);
            is[os._iaOffset + 13] = (short) (os._iOffset + 6);
            is[os._iaOffset + 14] = (short) (os._iOffset + 3);
            is[os._iaOffset + 15] = (short) (os._iOffset + 7);
            //back
            is[os._iaOffset + 16] = (short) (os._iOffset + 4);
            is[os._iaOffset + 17] = (short) (os._iOffset + 5);
            is[os._iaOffset + 18] = (short) (os._iOffset + 5);
            is[os._iaOffset + 19] = (short) (os._iOffset + 6);
            is[os._iaOffset + 20] = (short) (os._iOffset + 6);
            is[os._iaOffset + 21] = (short) (os._iOffset + 7);
            is[os._iaOffset + 22] = (short) (os._iOffset + 7);
            is[os._iaOffset + 23] = (short) (os._iOffset + 4);
        }
    }

    /**
     * Fills cube vertex + index + color (optional) data in provided arrays.
     * It is assumed that a cube is represented as 6 (walls) x 2 (triangles), i.e., to be rendered using GL_TRIANGLES option.
     *
     * @param x        cube x-coordinate
     * @param y        cube y-coordinate
     * @param z        cube z-coordinate
     * @param hSize    cube size (edge length by two)
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    color data
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    public static void fillCubeFillData(float x, float y, float z, float hSize,
                                        float[] v, int[] ii, short[] is, float[] c,
                                        OffsetStride os, Color color,
                                        boolean useAlpha)
    {
        fillCubeFillData(x, y, z, hSize, hSize, hSize, v, ii, is, c, os, color, useAlpha);
    }

    /**
     * Fills cube vertex + index + color (optional) data in provided arrays.
     * It is assumed that a cube is represented as 6 (walls) x 2 (triangles), i.e., to be rendered using GL_TRIANGLES option.
     *
     * @param x        cube x-coordinate
     * @param y        cube y-coordinate
     * @param z        cube z-coordinate
     * @param hWidth   cube size along the x dimension (edge length by two)
     * @param hHeight  cube size along the y dimension (edge length by two)
     * @param hDepth   cube size along the z dimension (edge length by two)
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    color data
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    public static void fillCubeFillData(float x, float y, float z,
                                        float hWidth, float hHeight, float hDepth,
                                        float[] v, int[] ii, short[] is, float[] c,
                                        OffsetStride os, Color color,
                                        boolean useAlpha)
    {
        fillCubeVertices(v, os._vaOffset, x, y, z, hWidth, hHeight, hDepth);
        if (c != null) fillCubeVerticesColors(c, os._caOffset, color, useAlpha);

        if (ii != null)
        {
            // front triangles
            ii[os._iaOffset] = os._iOffset + 3;
            ii[os._iaOffset + 1] = os._iOffset;
            ii[os._iaOffset + 2] = os._iOffset + 1;
            ii[os._iaOffset + 3] = os._iOffset + 3;
            ii[os._iaOffset + 4] = os._iOffset + 1;
            ii[os._iaOffset + 5] = os._iOffset + 2;
            // left triangles
            ii[os._iaOffset + 6] = os._iOffset;
            ii[os._iaOffset + 7] = os._iOffset + 1;
            ii[os._iaOffset + 8] = os._iOffset + 5;
            ii[os._iaOffset + 9] = os._iOffset;
            ii[os._iaOffset + 10] = os._iOffset + 5;
            ii[os._iaOffset + 11] = os._iOffset + 4;
            // back triangles
            ii[os._iaOffset + 12] = os._iOffset + 4;
            ii[os._iaOffset + 13] = os._iOffset + 5;
            ii[os._iaOffset + 14] = os._iOffset + 6;
            ii[os._iaOffset + 15] = os._iOffset + 4;
            ii[os._iaOffset + 16] = os._iOffset + 6;
            ii[os._iaOffset + 17] = os._iOffset + 7;
            // right triangles
            ii[os._iaOffset + 18] = os._iOffset + 7;
            ii[os._iaOffset + 19] = os._iOffset + 3;
            ii[os._iaOffset + 20] = os._iOffset + 2;
            ii[os._iaOffset + 21] = os._iOffset + 7;
            ii[os._iaOffset + 22] = os._iOffset + 2;
            ii[os._iaOffset + 23] = os._iOffset + 6;
            // top triangles
            ii[os._iaOffset + 24] = os._iOffset + 2;
            ii[os._iaOffset + 25] = os._iOffset + 1;
            ii[os._iaOffset + 26] = os._iOffset + 5;
            ii[os._iaOffset + 27] = os._iOffset + 2;
            ii[os._iaOffset + 28] = os._iOffset + 5;
            ii[os._iaOffset + 29] = os._iOffset + 6;
            // bottom triangles
            ii[os._iaOffset + 30] = os._iOffset + 3;
            ii[os._iaOffset + 31] = os._iOffset;
            ii[os._iaOffset + 32] = os._iOffset + 4;
            ii[os._iaOffset + 33] = os._iOffset + 3;
            ii[os._iaOffset + 34] = os._iOffset + 4;
            ii[os._iaOffset + 35] = os._iOffset + 7;
        }
        else
        {
            // front triangles
            is[os._iaOffset] = (short) (os._iOffset + 3);
            is[os._iaOffset + 1] = (short) os._iOffset;
            is[os._iaOffset + 2] = (short) (os._iOffset + 1);
            is[os._iaOffset + 3] = (short) (os._iOffset + 3);
            is[os._iaOffset + 4] = (short) (os._iOffset + 1);
            is[os._iaOffset + 5] = (short) (os._iOffset + 2);
            // left triangles
            is[os._iaOffset + 6] = (short) os._iOffset;
            is[os._iaOffset + 7] = (short) (os._iOffset + 1);
            is[os._iaOffset + 8] = (short) (os._iOffset + 5);
            is[os._iaOffset + 9] = (short) os._iOffset;
            is[os._iaOffset + 10] = (short) (os._iOffset + 5);
            is[os._iaOffset + 11] = (short) (os._iOffset + 4);
            // back triangles
            is[os._iaOffset + 12] = (short) (os._iOffset + 4);
            is[os._iaOffset + 13] = (short) (os._iOffset + 5);
            is[os._iaOffset + 14] = (short) (os._iOffset + 6);
            is[os._iaOffset + 15] = (short) (os._iOffset + 4);
            is[os._iaOffset + 16] = (short) (os._iOffset + 6);
            is[os._iaOffset + 17] = (short) (os._iOffset + 7);
            // right triangles
            is[os._iaOffset + 18] = (short) (os._iOffset + 7);
            is[os._iaOffset + 19] = (short) (os._iOffset + 3);
            is[os._iaOffset + 20] = (short) (os._iOffset + 2);
            is[os._iaOffset + 21] = (short) (os._iOffset + 7);
            is[os._iaOffset + 22] = (short) (os._iOffset + 2);
            is[os._iaOffset + 23] = (short) (os._iOffset + 6);
            // top triangles
            is[os._iaOffset + 24] = (short) (os._iOffset + 2);
            is[os._iaOffset + 25] = (short) (os._iOffset + 1);
            is[os._iaOffset + 26] = (short) (os._iOffset + 5);
            is[os._iaOffset + 27] = (short) (os._iOffset + 2);
            is[os._iaOffset + 28] = (short) (os._iOffset + 5);
            is[os._iaOffset + 29] = (short) (os._iOffset + 6);
            // bottom triangles
            is[os._iaOffset + 30] = (short) (os._iOffset + 3);
            is[os._iaOffset + 31] = (short) os._iOffset;
            is[os._iaOffset + 32] = (short) (os._iOffset + 4);
            is[os._iaOffset + 33] = (short) (os._iOffset + 3);
            is[os._iaOffset + 34] = (short) (os._iOffset + 4);
            is[os._iaOffset + 35] = (short) (os._iOffset + 7);
        }
    }

    /**
     * Fills cube vertex data in the provided array (8 points).
     *
     * @param x       cube x-coordinate
     * @param y       cube y-coordinate
     * @param z       cube z-coordinate
     * @param hWidth  cube size along the x dimension (edge length by two)
     * @param hHeight cube size along the y dimension (edge length by two)
     * @param hDepth  cube size along the z dimension (edge length by two)
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillCubeVertices(float[] v, int vOffset, float x, float y, float z, float hWidth, float hHeight, float hDepth)
    {
        // left/bottom/front
        v[vOffset] = x - hWidth;
        v[vOffset + 1] = y - hHeight;
        v[vOffset + 2] = z - hDepth;
        // left/top/front
        v[vOffset + 3] = x - hWidth;
        v[vOffset + 4] = y + hHeight;
        v[vOffset + 5] = z - hDepth;
        // right/top/front
        v[vOffset + 6] = x + hWidth;
        v[vOffset + 7] = y + hHeight;
        v[vOffset + 8] = z - hDepth;
        // right/bottom/front
        v[vOffset + 9] = x + hWidth;
        v[vOffset + 10] = y - hHeight;
        v[vOffset + 11] = z - hDepth;
        // left/bottom/back
        v[vOffset + 12] = x - hWidth;
        v[vOffset + 13] = y - hHeight;
        v[vOffset + 14] = z + hDepth;
        // left/top/back
        v[vOffset + 15] = x - hWidth;
        v[vOffset + 16] = y + hHeight;
        v[vOffset + 17] = z + hDepth;
        // right/top/back
        v[vOffset + 18] = x + hWidth;
        v[vOffset + 19] = y + hHeight;
        v[vOffset + 20] = z + hDepth;
        // right/bottom/back
        v[vOffset + 21] = x + hWidth;
        v[vOffset + 22] = y - hHeight;
        v[vOffset + 23] = z + hDepth;
    }

    /**
     * Fills cube vertex color in the provided array (8 points).
     *
     * @param c        color array (can be null -> not filled)
     * @param cOffset  offset in the color array (shorts) (starting position for filling the data)
     * @param color    color data,
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    protected static void fillCubeVerticesColors(float[] c, int cOffset, Color color, boolean useAlpha)
    {
        if (!useAlpha)
        {
            // left/bottom/front
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            // left/top/front
            c[cOffset + 3] = color._r;
            c[cOffset + 4] = color._g;
            c[cOffset + 5] = color._b;
            // right/top/front
            c[cOffset + 6] = color._r;
            c[cOffset + 7] = color._g;
            c[cOffset + 8] = color._b;
            // right/bottom/front
            c[cOffset + 9] = color._r;
            c[cOffset + 10] = color._g;
            c[cOffset + 11] = color._b;
            // left/bottom/back
            c[cOffset + 12] = color._r;
            c[cOffset + 13] = color._g;
            c[cOffset + 14] = color._b;
            // left/top/back
            c[cOffset + 15] = color._r;
            c[cOffset + 16] = color._g;
            c[cOffset + 17] = color._b;
            // right/top/back
            c[cOffset + 18] = color._r;
            c[cOffset + 19] = color._g;
            c[cOffset + 20] = color._b;
            // right/bottom/back
            c[cOffset + 21] = color._r;
            c[cOffset + 22] = color._g;
            c[cOffset + 23] = color._b;
        }
        else
        {
            // left/bottom/front
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            c[cOffset + 3] = color._a;
            // left/top/front
            c[cOffset + 4] = color._r;
            c[cOffset + 5] = color._g;
            c[cOffset + 6] = color._b;
            c[cOffset + 7] = color._a;
            // right/top/front
            c[cOffset + 8] = color._r;
            c[cOffset + 9] = color._g;
            c[cOffset + 10] = color._b;
            c[cOffset + 11] = color._a;
            // right/bottom/front
            c[cOffset + 12] = color._r;
            c[cOffset + 13] = color._g;
            c[cOffset + 14] = color._b;
            c[cOffset + 15] = color._a;
            // left/bottom/back
            c[cOffset + 16] = color._r;
            c[cOffset + 17] = color._g;
            c[cOffset + 18] = color._b;
            c[cOffset + 19] = color._a;
            // left/top/back
            c[cOffset + 20] = color._r;
            c[cOffset + 21] = color._g;
            c[cOffset + 22] = color._b;
            c[cOffset + 23] = color._a;
            // right/top/back
            c[cOffset + 24] = color._r;
            c[cOffset + 25] = color._g;
            c[cOffset + 26] = color._b;
            c[cOffset + 27] = color._a;
            // right/bottom/back
            c[cOffset + 28] = color._r;
            c[cOffset + 29] = color._g;
            c[cOffset + 30] = color._b;
            c[cOffset + 31] = color._a;
        }
    }
}
