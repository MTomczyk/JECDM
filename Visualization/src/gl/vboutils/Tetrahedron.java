package gl.vboutils;

import color.Color;
import dataset.painter.style.enums.Marker;

/**
 * Provides methods for creating VBO data representing a tetrahedron.
 *
 * @author MTomczyk
 */
public class Tetrahedron
{
    /**
     * Constant field.
     */
    private static final float _r6d3 = (float) (Math.sqrt(6.0d) / 3.0d);


    /**
     * Fills tetrahedron (up) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronUpVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset] = x - l / 2.0f;
        v[vOffset + 1] = y - 1.0f / 3.0f * h;
        v[vOffset + 2] = z - 1.0f / 3.0f * h;

        v[vOffset + 3] = x + l / 2.0f;
        v[vOffset + 4] = y - 1.0f / 3.0f * h;
        v[vOffset + 5] = z - 1.0f / 3.0f * h;

        v[vOffset + 6] = x;
        v[vOffset + 7] = y - 1.0f / 3.0f * h;
        v[vOffset + 8] = z + 2.0f / 3.0f * h;

        v[vOffset + 9] = x;
        v[vOffset + 10] = y + 2.0f / 3.0f * h;
        v[vOffset + 11] = z;
    }

    /**
     * Fills tetrahedron (down) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronDownVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset] = x - l / 2.0f;
        v[vOffset + 1] = y + 1.0f / 3.0f * h;
        v[vOffset + 2] = z - 1.0f / 3.0f * h;

        v[vOffset + 3] = x + l / 2.0f;
        v[vOffset + 4] = y + 1.0f / 3.0f * h;
        v[vOffset + 5] = z - 1.0f / 3.0f * h;

        v[vOffset + 6] = x;
        v[vOffset + 7] = y + 1.0f / 3.0f * h;
        v[vOffset + 8] = z + 2.0f / 3.0f * h;

        v[vOffset + 9] = x;
        v[vOffset + 10] = y - 2.0f / 3.0f * h;
        v[vOffset + 11] = z;
    }

    /**
     * Fills tetrahedron (left) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronLeftVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset + 1] = y - l / 2.0f;
        v[vOffset] = x + 1.0f / 3.0f * h;
        v[vOffset + 2] = z - 1.0f / 3.0f * h;

        v[vOffset + 4] = y + l / 2.0f;
        v[vOffset + 3] = x + 1.0f / 3.0f * h;
        v[vOffset + 5] = z - 1.0f / 3.0f * h;

        v[vOffset + 7] = y;
        v[vOffset + 6] = x + 1.0f / 3.0f * h;
        v[vOffset + 8] = z + 2.0f / 3.0f * h;

        v[vOffset + 10] = y;
        v[vOffset + 9] = x - 2.0f / 3.0f * h;
        v[vOffset + 11] = z;
    }

    /**
     * Fills tetrahedron (right) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronRightVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset + 1] = y - l / 2.0f;
        v[vOffset] = x - 1.0f / 3.0f * h;
        v[vOffset + 2] = z - 1.0f / 3.0f * h;

        v[vOffset + 4] = y + l / 2.0f;
        v[vOffset + 3] = x - 1.0f / 3.0f * h;
        v[vOffset + 5] = z - 1.0f / 3.0f * h;

        v[vOffset + 7] = y;
        v[vOffset + 6] = x - 1.0f / 3.0f * h;
        v[vOffset + 8] = z + 2.0f / 3.0f * h;

        v[vOffset + 10] = y;
        v[vOffset + 9] = x + 2.0f / 3.0f * h;
        v[vOffset + 11] = z;
    }


    /**
     * Fills tetrahedron (front) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronFrontVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset] = x - l / 2.0f;
        v[vOffset + 2] = z - 1.0f / 3.0f * h;
        v[vOffset + 1] = y - 1.0f / 3.0f * h;

        v[vOffset + 3] = x + l / 2.0f;
        v[vOffset + 5] = z - 1.0f / 3.0f * h;
        v[vOffset + 4] = y - 1.0f / 3.0f * h;

        v[vOffset + 6] = x;
        v[vOffset + 8] = z - 1.0f / 3.0f * h;
        v[vOffset + 7] = y + 2.0f / 3.0f * h;

        v[vOffset + 9] = x;
        v[vOffset + 11] = z + 2.0f / 3.0f * h;
        v[vOffset + 10] = y;
    }

    /**
     * Fills tetrahedron (front) vertex data in the provided array (8 points).
     *
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param z       z-coordinate
     * @param l       tetrahedron side length
     * @param v       vertices array
     * @param vOffset offset in the vertices array (starting position for filling the data)
     */
    protected static void fillTetrahedronBackVertices(float[] v, int vOffset, float x, float y, float z, float l)
    {
        float h = l * _r6d3;

        v[vOffset] = x - l / 2.0f;
        v[vOffset + 2] = z + 1.0f / 3.0f * h;
        v[vOffset + 1] = y - 1.0f / 3.0f * h;

        v[vOffset + 3] = x + l / 2.0f;
        v[vOffset + 5] = z + 1.0f / 3.0f * h;
        v[vOffset + 4] = y - 1.0f / 3.0f * h;

        v[vOffset + 6] = x;
        v[vOffset + 8] = z + 1.0f / 3.0f * h;
        v[vOffset + 7] = y + 2.0f / 3.0f * h;

        v[vOffset + 9] = x;
        v[vOffset + 11] = z - 2.0f / 3.0f * h;
        v[vOffset + 10] = y;
    }

    /**
     * Fills tetrahedron vertex + index + color (optional) data in provided arrays.
     * It is assumed that a cube is represented as 4 triangles, i.e., to be rendered using GL_TRIANGLES option.
     *
     * @param x        x-coordinate
     * @param y        y-coordinate
     * @param z        z-coordinate
     * @param l        tetrahedron side length
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    provides color components (not used is c is null)
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     * @param marker   marker style
     */
    public static void fillTetrahedronEdgesData(float x, float y, float z, float l,
                                                float[] v, int[] ii, short[] is, float[] c,
                                                OffsetStride os, Color color,
                                                boolean useAlpha, Marker marker)
    {
        fillVertices(x, y, z, l, v, c, os, color, useAlpha, marker);

        if (ii != null)
        {
            ii[os._iaOffset] = os._iOffset;
            ii[os._iaOffset + 1] = os._iOffset + 1;
            ii[os._iaOffset + 2] = os._iOffset + 1;
            ii[os._iaOffset + 3] = os._iOffset + 2;
            ii[os._iaOffset + 4] = os._iOffset + 2;
            ii[os._iaOffset + 5] = os._iOffset;
            ii[os._iaOffset + 6] = os._iOffset;
            ii[os._iaOffset + 7] = os._iOffset + 3;
            ii[os._iaOffset + 8] = os._iOffset + 1;
            ii[os._iaOffset + 9] = os._iOffset + 3;
            ii[os._iaOffset + 10] = os._iOffset + 2;
            ii[os._iaOffset + 11] = os._iOffset + 3;
        }
        else
        {
            is[os._iaOffset] = (short) os._iOffset;
            is[os._iaOffset + 1] = (short) (os._iOffset + 1);
            is[os._iaOffset + 2] = (short) (os._iOffset + 1);
            is[os._iaOffset + 3] = (short) (os._iOffset + 2);
            is[os._iaOffset + 4] = (short) (os._iOffset + 2);
            is[os._iaOffset + 5] = (short) os._iOffset;
            is[os._iaOffset + 6] = (short) os._iOffset;
            is[os._iaOffset + 7] = (short) (os._iOffset + 3);
            is[os._iaOffset + 8] = (short) (os._iOffset + 1);
            is[os._iaOffset + 9] = (short) (os._iOffset + 3);
            is[os._iaOffset + 10] = (short) (os._iOffset + 2);
            is[os._iaOffset + 11] = (short) (os._iOffset + 3);
        }
    }

    /**
     * Auxiliary method for filling tetrahedron data.
     *
     * @param x        x-coordinate
     * @param y        y-coordinate
     * @param z        z-coordinate
     * @param l        tetrahedron side length
     * @param v        vertices array
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    provides color components (not used is c is null)
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     * @param marker   marker style
     */
    private static void fillVertices(float x, float y, float z, float l, float[] v, float[] c, OffsetStride os, Color color, boolean useAlpha, Marker marker)
    {
        if (marker.equals(Marker.TETRAHEDRON_UP_3D)) fillTetrahedronUpVertices(v, os._vaOffset, x, y, z, l);
        else if (marker.equals(Marker.TETRAHEDRON_DOWN_3D)) fillTetrahedronDownVertices(v, os._vaOffset, x, y, z, l);
        else if (marker.equals(Marker.TETRAHEDRON_LEFT_3D)) fillTetrahedronLeftVertices(v, os._vaOffset, x, y, z, l);
        else if (marker.equals(Marker.TETRAHEDRON_RIGHT_3D)) fillTetrahedronRightVertices(v, os._vaOffset, x, y, z, l);
        else if (marker.equals(Marker.TETRAHEDRON_FRONT_3D)) fillTetrahedronFrontVertices(v, os._vaOffset, x, y, z, l);
        else if (marker.equals(Marker.TETRAHEDRON_BACK_3D)) fillTetrahedronBackVertices(v, os._vaOffset, x, y, z, l);
        if (c != null) fillTetrahedronVerticesColors(c, os._caOffset, color, useAlpha);
    }

    /**
     * Fills tetrahedron vertex + index + color (optional) data in provided arrays (the input data is precalculated
     * for TRIANGULAR_3D arrows (and similar).
     *
     * @param data     pre-calculated data (each tetrahedron data occupies 12 elements; 4 walls * 3 coordinates)
     * @param v        vertices array; the data organization should match the data array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    provides color components (not used is c is null)
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    public static void fillTetrahedronFillData(float[] data, float[] v, int[] ii, short[] is, float[] c,
                                               OffsetStride os, Color color,
                                               boolean useAlpha)
    {
        System.arraycopy(data, os._vaOffset, v, os._vaOffset, 12);
        if (c != null) fillTetrahedronVerticesColors(c, os._caOffset, color, useAlpha);
        fillTetrahedronFillIndices(ii, is, os);
    }

    /**
     * Fills tetrahedron vertex + index + color (optional) data in provided arrays.
     *
     * @param x        x-coordinate
     * @param y        y-coordinate
     * @param z        z-coordinate
     * @param l        tetrahedron side length
     * @param v        vertices array
     * @param ii       indices array (ints) (either ii or is must be null)
     * @param is       indices array (shorts) (either ii or is must be null)
     * @param c        color array (can be null -> not filled)
     * @param os       offset/stride data
     * @param color    provides color components (not used is c is null)
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     * @param marker   marker style
     */
    public static void fillTetrahedronFillData(float x, float y, float z, float l,
                                               float[] v, int[] ii, short[] is, float[] c,
                                               OffsetStride os, Color color,
                                               boolean useAlpha, Marker marker)
    {
        fillVertices(x, y, z, l, v, c, os, color, useAlpha, marker);
        fillTetrahedronFillIndices(ii, is, os);
    }

    /**
     * Fills tetrahedron fills indices data
     *
     * @param ii indices array (ints) (either ii or is must be null)
     * @param is indices array (shorts) (either ii or is must be null)
     * @param os offset/stride data
     */
    protected static void fillTetrahedronFillIndices(int[] ii, short[] is, OffsetStride os)
    {
        if (ii != null)
        {
            // front triangle
            ii[os._iaOffset] = os._iOffset;
            ii[os._iaOffset + 1] = os._iOffset + 3;
            ii[os._iaOffset + 2] = os._iOffset + 1;
            // left triangle
            ii[os._iaOffset + 3] = os._iOffset;
            ii[os._iaOffset + 4] = os._iOffset + 2;
            ii[os._iaOffset + 5] = os._iOffset + 3;
            // right triangle
            ii[os._iaOffset + 6] = os._iOffset + 1;
            ii[os._iaOffset + 7] = os._iOffset + 3;
            ii[os._iaOffset + 8] = os._iOffset + 2;
            // bottom triangle
            ii[os._iaOffset + 9] = os._iOffset;
            ii[os._iaOffset + 10] = os._iOffset + 1;
            ii[os._iaOffset + 11] = os._iOffset + 2;
        }
        else
        {
            // front triangle
            is[os._iaOffset] = (short) os._iOffset;
            is[os._iaOffset + 1] = (short) (os._iOffset + 3);
            is[os._iaOffset + 2] = (short) (os._iOffset + 1);
            // left triangle
            is[os._iaOffset + 3] = (short) os._iOffset;
            is[os._iaOffset + 4] = (short) (os._iOffset + 2);
            is[os._iaOffset + 5] = (short) (os._iOffset + 3);
            // right triangle
            is[os._iaOffset + 6] = (short) (os._iOffset + 1);
            is[os._iaOffset + 7] = (short) (os._iOffset + 3);
            is[os._iaOffset + 8] = (short) (os._iOffset + 2);
            // bottom triangle
            is[os._iaOffset + 9] = (short) os._iOffset;
            is[os._iaOffset + 10] = (short) (os._iOffset + 1);
            is[os._iaOffset + 11] = (short) (os._iOffset + 2);
        }
    }

    /**
     * Fills tetrahedron vertex color in the provided array (8 points).
     *
     * @param c        color array (can be null -> not filled)
     * @param cOffset  offset in the color array (starting position for filling the data)
     * @param color    color data
     * @param useAlpha if true, alpha channel is considered, therefore the stride for c-array is assumed 4 (not 3)
     */
    @SuppressWarnings("DuplicatedCode")
    protected static void fillTetrahedronVerticesColors(float[] c, int cOffset, Color color, boolean useAlpha)
    {
        if (!useAlpha)
        {
            // front
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            // left
            c[cOffset + 3] = color._r;
            c[cOffset + 4] = color._g;
            c[cOffset + 5] = color._b;
            // right
            c[cOffset + 6] = color._r;
            c[cOffset + 7] = color._g;
            c[cOffset + 8] = color._b;
            // bottom
            c[cOffset + 9] = color._r;
            c[cOffset + 10] = color._g;
            c[cOffset + 11] = color._b;
        }
        else
        {
            // front
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            c[cOffset + 3] = color._a;
            // left
            c[cOffset + 4] = color._r;
            c[cOffset + 5] = color._g;
            c[cOffset + 6] = color._b;
            c[cOffset + 7] = color._a;
            // right
            c[cOffset + 8] = color._r;
            c[cOffset + 9] = color._g;
            c[cOffset + 10] = color._b;
            c[cOffset + 11] = color._a;
            // bottom
            c[cOffset + 12] = color._r;
            c[cOffset + 13] = color._g;
            c[cOffset + 14] = color._b;
            c[cOffset + 15] = color._a;
        }
    }


}
