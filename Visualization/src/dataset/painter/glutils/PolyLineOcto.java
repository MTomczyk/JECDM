package dataset.painter.glutils;

import color.Color;
import gl.vboutils.BufferData;
import space.Vector;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Provides an auxiliary method for creating VBO data that illustrates polygon-based lines.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class PolyLineOcto
{
    /**
     * Creates the buffer data for a contiguous line (drawn using a style = POLY_OCTO)
     *
     * @param cLine          currently processed contiguous line (coordinates)
     * @param noPointsIt     stores the number of regular points for the contiguous line
     * @param colorsIterator stores the color data for the regular points in the contiguous line
     * @param noAuxPointsIt  stores the number of auxiliary points for the contiguous line
     * @param auxLinesIt     stores the data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColorsIt    stores the data on the colors of auxiliary points of the contiguous line
     * @param useGradient    if true, then gradient mode is used
     * @param colorStride    color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha       if true, then alpha channel is used
     * @param r              line radius
     * @return buffer data
     */
    public static BufferData getPolyLineOctoData(float[] cLine,
                                                 ListIterator<Integer> noPointsIt,
                                                 ListIterator<Color[]> colorsIterator,
                                                 ListIterator<int[]> noAuxPointsIt,
                                                 ListIterator<float[]> auxLinesIt,
                                                 ListIterator<Color[]> auxColorsIt,
                                                 boolean useGradient,
                                                 int colorStride,
                                                 boolean useAlpha,
                                                 float r)
    {
        int noPoints = noPointsIt.next();
        int[] noAuxPoints = null;
        float[] auxLines = null;
        Color[] auxColors = null;
        Color[] colors = null;

        if (useGradient)
        {
            colors = colorsIterator.next();
            noAuxPoints = noAuxPointsIt.next();
            auxLines = auxLinesIt.next();
            auxColors = auxColorsIt.next();
        }

        return getPolyLineOctoData(cLine, noPoints, colors, noAuxPoints, auxLines,
                auxColors, useGradient, colorStride, useAlpha, r);
    }


    /**
     * Creates the buffer data for a contiguous line (drawn using a style = POLY_OCTO)
     *
     * @param cLine       currently processed contiguous line (coordinates)
     * @param noPoints    the number of regular points for the contiguous line
     * @param colors      color data for the regular points in the contiguous line
     * @param noAuxPoints data on the number of auxiliary points for the contiguous line
     * @param auxLines    data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColors   data on the colors of auxiliary points of the contiguous line
     * @param useGradient if true, then gradient mode is used
     * @param colorStride color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha    if true, then alpha channel is used
     * @param r           line radius
     * @return buffer data
     */
    private static BufferData getPolyLineOctoData(float[] cLine,
                                                  int noPoints,
                                                  Color[] colors,
                                                  int[] noAuxPoints,
                                                  float[] auxLines,
                                                  Color[] auxColors,
                                                  boolean useGradient,
                                                  int colorStride,
                                                  boolean useAlpha,
                                                  float r)
    {
        int noPointsWithAux = noPoints;
        if (noAuxPoints != null) for (int ap : noAuxPoints) noPointsWithAux += ap;

        int cLineOffset = 0;
        int vAuxLinesOffset = 0;
        int vAuxColorsOffset = 0;

        return getBD(0, noPoints, noPointsWithAux, useGradient, colorStride, useAlpha, colors, cLine,
                auxLines, noAuxPoints, 0, 1, auxColors, cLineOffset, vAuxLinesOffset, vAuxColorsOffset, r);
    }

    /**
     * Creates the buffer data for a contiguous line (drawn using a style = POLY_OCTO)
     *
     * @param pointsOffset      points offset (original data points)
     * @param noPoints          no points to process (starting from the offset)
     * @param noPointsWithAux   no points to process (starting from the offset; original + aux)
     * @param useGradient       if true, then the gradient mode is used
     * @param colorStride       color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha          if true, then alpha channel is used
     * @param colors            color data for the regular points in the contiguous line
     * @param cLine             currently processed contiguous line (coordinates)
     * @param auxLines          data on the coordinates of the auxiliary points of the contiguous line
     * @param noAuxPoints       data on the number of auxiliary points for the contiguous line
     * @param noAuxPointsOffset offset in noAuxPoints,
     * @param noAuxPointsStride stride in noAuxPoints
     * @param auxColors         data on the colors of auxiliary points of the contiguous line
     * @param cLineOffset       offset in cLine
     * @param vAuxLinesOffset   offset in auxLines
     * @param vAuxColorsOffset  offset in auxColors
     * @param r                 line radius
     * @return buffer data
     */
    private static BufferData getBD(int pointsOffset,
                                    int noPoints,
                                    int noPointsWithAux,
                                    boolean useGradient,
                                    int colorStride,
                                    boolean useAlpha,
                                    Color[] colors,
                                    float[] cLine,
                                    float[] auxLines,
                                    int[] noAuxPoints,
                                    int noAuxPointsOffset,
                                    int noAuxPointsStride,
                                    Color[] auxColors,
                                    int cLineOffset,
                                    int vAuxLinesOffset,
                                    int vAuxColorsOffset,
                                    float r)
    {
        int noPointsQ = noPointsWithAux * 8; // 8 satellite vertices per original vertex
        float[] v = new float[noPointsQ * 3];
        boolean intIndices = noPointsQ > Short.MAX_VALUE;
        // no segments * no walls * no points in the triangle + ends = 2 * 6 * 3
        int noIndices = (noPointsWithAux - 1) * 8 * 2 * 3 + 36; // in original point space
        int[] ii = null;
        short[] is = null;
        if (intIndices) ii = new int[noIndices];
        else is = new short[noIndices];
        float[] c = null;
        if (useGradient) c = new float[noPointsQ * colorStride];


        int vOffset = 0;
        int cOffset = 0;
        int caStride = colorStride * 8;
        int iaOffset = 0;
        int iOffset = 0;

        // fill first end indices!
        {
            if (ii != null)
            {
                ii[0] = 0;
                ii[1] = 1;
                ii[2] = 2;
                ii[3] = 0;
                ii[4] = 2;
                ii[5] = 3;
                ii[6] = 0;
                ii[7] = 3;
                ii[8] = 4;
                ii[9] = 0;
                ii[10] = 4;
                ii[11] = 5;
                ii[12] = 0;
                ii[13] = 5;
                ii[14] = 6;
                ii[15] = 0;
                ii[16] = 6;
                ii[17] = 7;
            }
            else
            {
                is[0] = 0;
                is[1] = 1;
                is[2] = 2;
                is[3] = 0;
                is[4] = 2;
                is[5] = 3;
                is[6] = 0;
                is[7] = 3;
                is[8] = 4;
                is[9] = 0;
                is[10] = 4;
                is[11] = 5;
                is[12] = 0;
                is[13] = 5;
                is[14] = 6;
                is[15] = 0;
                is[16] = 6;
                is[17] = 7;
            }
            iaOffset += 18;
        }

        float[] Dp;
        float[] D = null;

        for (int i = pointsOffset; i < pointsOffset + noPoints; i++)
        {
            Dp = D;
            if (i < pointsOffset + noPoints - 1)
            {
                D = new float[3];

                if ((noAuxPoints != null) && (noAuxPoints[noAuxPointsOffset] > 0))
                {
                    D[0] = auxLines[vAuxLinesOffset] - cLine[cLineOffset];
                    D[1] = auxLines[vAuxLinesOffset + 1] - cLine[cLineOffset + 1];
                    D[2] = auxLines[vAuxLinesOffset + 2] - cLine[cLineOffset + 2];
                }
                else
                {
                    D[0] = cLine[cLineOffset + 3] - cLine[cLineOffset];
                    D[1] = cLine[cLineOffset + 4] - cLine[cLineOffset + 1];
                    D[2] = cLine[cLineOffset + 5] - cLine[cLineOffset + 2];
                }

                if ((Double.compare(D[0], 0.0d) == 0) && (Double.compare(D[1], 0.0d) == 0) &&
                        (Double.compare(D[2], 0.0d) == 0)) D = null;
            }
            else D = null;

            fillPolyLineOctoVertices(v, vOffset, vOffset - 24, cLine, cLineOffset, Dp, D, r);
            cLineOffset += 3;
            vOffset += 24;

            if (i < pointsOffset + noPoints - 1) // only if not last
            {
                fillPolyLineOctoIndices(ii, is, iaOffset, iOffset);
                iaOffset += 48;
                iOffset += 8;
            }

            if (useGradient)
            {
                fillPolyLineOctoColors(useAlpha, c, cOffset, colors[i]);
                cOffset += caStride;
            }

            // aux after adding the point, and if the point is not last
            if ((noAuxPoints != null) && (i < pointsOffset + noPoints - 1))
            {
                for (int j = 0; j < noAuxPoints[noAuxPointsOffset]; j++)
                {
                    Dp = D;
                    D = new float[3];
                    if (j == noAuxPoints[noAuxPointsOffset] - 1) // last
                    {
                        D[0] = cLine[cLineOffset] - auxLines[vAuxLinesOffset];
                        D[1] = cLine[cLineOffset + 1] - auxLines[vAuxLinesOffset + 1];
                        D[2] = cLine[cLineOffset + 2] - auxLines[vAuxLinesOffset + 2];
                    }
                    else
                    {
                        D[0] = auxLines[vAuxLinesOffset + 3] - auxLines[vAuxLinesOffset];
                        D[1] = auxLines[vAuxLinesOffset + 4] - auxLines[vAuxLinesOffset + 1];
                        D[2] = auxLines[vAuxLinesOffset + 5] - auxLines[vAuxLinesOffset + 2];
                    }

                    if ((Double.compare(D[0], 0.0d) == 0) && (Double.compare(D[1], 0.0d) == 0) &&
                            (Double.compare(D[2], 0.0d) == 0)) D = null;

                    fillPolyLineOctoVertices(v, vOffset, vOffset - 24, auxLines, vAuxLinesOffset, Dp, D, r);
                    vAuxLinesOffset += 3;
                    vOffset += 24;

                    fillPolyLineOctoIndices(ii, is, iaOffset, iOffset);
                    iaOffset += 48;
                    iOffset += 8;

                    fillPolyLineOctoColors(useAlpha, c, cOffset, auxColors[vAuxColorsOffset + j]);
                    cOffset += caStride;
                }
                vAuxColorsOffset += noAuxPoints[noAuxPointsOffset];
                noAuxPointsOffset += noAuxPointsStride;
            }
        }

        // fill last end indices!
        {
            if (ii != null)
            {
                ii[iaOffset] = iOffset;
                ii[iaOffset + 1] = iOffset + 1;
                ii[iaOffset + 2] = iOffset + 2;
                ii[iaOffset + 3] = iOffset;
                ii[iaOffset + 4] = iOffset + 2;
                ii[iaOffset + 5] = iOffset + 3;
                ii[iaOffset + 6] = iOffset;
                ii[iaOffset + 7] = iOffset + 3;
                ii[iaOffset + 8] = iOffset + 4;
                ii[iaOffset + 9] = iOffset;
                ii[iaOffset + 10] = iOffset + 4;
                ii[iaOffset + 11] = iOffset + 5;
                ii[iaOffset + 12] = iOffset;
                ii[iaOffset + 13] = iOffset + 5;
                ii[iaOffset + 14] = iOffset + 6;
                ii[iaOffset + 15] = iOffset;
                ii[iaOffset + 16] = iOffset + 6;
                ii[iaOffset + 17] = iOffset + 7;
            }
            else
            {
                is[iaOffset] = (short) (iOffset);
                is[iaOffset + 1] = (short) (iOffset + 1);
                is[iaOffset + 2] = (short) (iOffset + 2);
                is[iaOffset + 3] = (short) (iOffset);
                is[iaOffset + 4] = (short) (iOffset + 2);
                is[iaOffset + 5] = (short) (iOffset + 3);
                is[iaOffset + 6] = (short) (iOffset);
                is[iaOffset + 7] = (short) (iOffset + 3);
                is[iaOffset + 8] = (short) (iOffset + 4);
                is[iaOffset + 9] = (short) (iOffset);
                is[iaOffset + 10] = (short) (iOffset + 4);
                is[iaOffset + 11] = (short) (iOffset + 5);
                is[iaOffset + 12] = (short) (iOffset);
                is[iaOffset + 13] = (short) (iOffset + 5);
                is[iaOffset + 14] = (short) (iOffset + 6);
                is[iaOffset + 15] = (short) (iOffset);
                is[iaOffset + 16] = (short) (iOffset + 6);
                is[iaOffset + 17] = (short) (iOffset + 7);
            }
        }

        BufferData bd;
        if (ii != null) bd = new BufferData(v, ii, c);
        else bd = new BufferData(v, is, c);
        return bd;
    }


    /**
     * Creates the buffer data for a line (interprets two consecutive data points as one line).
     *
     * @param cLine          currently processed contiguous line (coordinates)
     * @param noPointsIt     stores the number of regular points for the contiguous line
     * @param colorsIterator stores the color data for the regular points in the contiguous line
     * @param noAuxPointsIt  stores the number of auxiliary points for the contiguous line
     * @param auxLinesIt     stores the data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColorsIt    stores the data on the colors of auxiliary points of the contiguous line
     * @param useGradient    if true, then gradient mode is used
     * @param colorStride    color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha       if true, then alpha channel is used
     * @param r              line radius
     * @return buffer data
     */
    public static LinkedList<BufferData> getPolyLineOctoDataForNonContiguousModeWithGradient(float[] cLine,
                                                                                             ListIterator<Integer> noPointsIt,
                                                                                             ListIterator<Color[]> colorsIterator,
                                                                                             ListIterator<int[]> noAuxPointsIt,
                                                                                             ListIterator<float[]> auxLinesIt,
                                                                                             ListIterator<Color[]> auxColorsIt,
                                                                                             boolean useGradient,
                                                                                             int colorStride,
                                                                                             boolean useAlpha,
                                                                                             float r)
    {
        int noPoints = noPointsIt.next();
        int[] noAuxPoints = null;
        float[] auxLines = null;
        Color[] auxColors = null;
        Color[] colors = null;

        if (useGradient)
        {
            colors = colorsIterator.next();
            noAuxPoints = noAuxPointsIt.next();
            auxLines = auxLinesIt.next();
            auxColors = auxColorsIt.next();
        }

        return getPolyLineOctoDataForNonContiguousModeWithGradient(cLine, noPoints, colors, noAuxPoints, auxLines,
                auxColors, useGradient, colorStride, useAlpha, r);
    }

    /**
     * Creates the buffer data for a line (interprets two consecutive data points as one line).
     *
     * @param cLine       currently processed contiguous line (coordinates)
     * @param noPoints    the number of regular points for the contiguous line
     * @param colors      color data for the regular points in the contiguous line
     * @param noAuxPoints data on the number of auxiliary points for the contiguous line
     * @param auxLines    data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColors   data on the colors of auxiliary points of the contiguous line
     * @param useGradient if true, then gradient mode is used
     * @param colorStride color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha    if true, then alpha channel is used
     * @param r           line radius
     * @return buffer data
     */
    public static LinkedList<BufferData> getPolyLineOctoDataForNonContiguousModeWithGradient(float[] cLine,
                                                                                             int noPoints,
                                                                                             Color[] colors,
                                                                                             int[] noAuxPoints,
                                                                                             float[] auxLines,
                                                                                             Color[] auxColors,
                                                                                             boolean useGradient,
                                                                                             int colorStride,
                                                                                             boolean useAlpha,
                                                                                             float r)
    {
        LinkedList<BufferData> lBD = new LinkedList<>();
        int auxIdx = 0;
        int cLineOffset = 0;
        int vAuxLinesOffset = 0;
        int vAuxColorsOffset = 0;

        for (int i = 0; i < noPoints; i += 2)
        {
            int noPointsWithAux = 2;
            if (useGradient) noPointsWithAux += noAuxPoints[auxIdx];

            lBD.add(getBD(i, 2, noPointsWithAux, useGradient, colorStride, useAlpha, colors, cLine, auxLines,
                    noAuxPoints, auxIdx, 0, auxColors, cLineOffset, vAuxLinesOffset, vAuxColorsOffset, r));

            cLineOffset += 6;

            if (useGradient)
            {
                vAuxLinesOffset += 3 * noAuxPoints[auxIdx];
                vAuxColorsOffset += noAuxPoints[auxIdx];
            }
            auxIdx++;
        }
        return lBD;
    }


    /**
     * Fills plane coordinates modifiers for the poly octo lines.
     *
     * @param v  vertex array to be filled (8 points x 3 dimensions)
     * @param cO current offset in v
     * @param pO previous offset (pointing to the already filled data
     * @param s  source data
     * @param sO source offset
     * @param D1 delta coordinates when moving from a point to a point (point - 1: point)
     * @param D2 delta coordinates when moving from a point to a point (point: point + 1)
     * @param r  line radius
     */
    private static void fillPolyLineOctoVertices(float[] v, int cO, int pO, float[] s, int sO, float[] D1, float[] D2, float r)
    {
        float[][] m;

        float[] refVector = PolyLine.getReferenceVector(D1, D2);

        if (refVector != null)
        {
            m = new float[8][];

            m[0] = Vector.getPerpendicularVector3D(refVector);
            m[2] = Vector.getInverseCrossProduct3D(m[0], refVector, 0.0001f);
            if (m[2] != null)
            {
                m[4] = new float[]{-m[0][0], -m[0][1], -m[0][2]};
                m[6] = new float[]{-m[2][0], -m[2][1], -m[2][2]};

                //Vector.normalize(m[0]);
                Vector.normalize(m[2]);
                Vector.normalize(m[4]);
                Vector.normalize(m[6]);

                m[1] = new float[3];
                m[1][0] = m[0][0] + m[2][0];
                m[1][1] = m[0][1] + m[2][1];
                m[1][2] = m[0][2] + m[2][2];
                Vector.normalize(m[1]);
                m[3] = new float[3];
                m[3][0] = m[2][0] + m[4][0];
                m[3][1] = m[2][1] + m[4][1];
                m[3][2] = m[2][2] + m[4][2];
                Vector.normalize(m[3]);
                m[5] = new float[3];
                m[5][0] = m[4][0] + m[6][0];
                m[5][1] = m[4][1] + m[6][1];
                m[5][2] = m[4][2] + m[6][2];
                Vector.normalize(m[5]);
                m[7] = new float[3];
                m[7][0] = m[6][0] + m[0][0];
                m[7][1] = m[6][1] + m[0][1];
                m[7][2] = m[6][2] + m[0][2];
                Vector.normalize(m[7]);
            }
            else m = new float[8][3];
        }
        else m = new float[8][3];

        v[cO] = s[sO] + m[0][0] * r;
        v[cO + 1] = s[sO + 1] + m[0][1] * r;
        v[cO + 2] = s[sO + 2] + m[0][2] * r;
        v[cO + 3] = s[sO] + m[1][0] * r;
        v[cO + 4] = s[sO + 1] + m[1][1] * r;
        v[cO + 5] = s[sO + 2] + m[1][2] * r;
        v[cO + 6] = s[sO] + m[2][0] * r;
        v[cO + 7] = s[sO + 1] + m[2][1] * r;
        v[cO + 8] = s[sO + 2] + m[2][2] * r;
        v[cO + 9] = s[sO] + m[3][0] * r;
        v[cO + 10] = s[sO + 1] + m[3][1] * r;
        v[cO + 11] = s[sO + 2] + m[3][2] * r;
        v[cO + 12] = s[sO] + m[4][0] * r;
        v[cO + 13] = s[sO + 1] + m[4][1] * r;
        v[cO + 14] = s[sO + 2] + m[4][2] * r;
        v[cO + 15] = s[sO] + m[5][0] * r;
        v[cO + 16] = s[sO + 1] + m[5][1] * r;
        v[cO + 17] = s[sO + 2] + m[5][2] * r;
        v[cO + 18] = s[sO] + m[6][0] * r;
        v[cO + 19] = s[sO + 1] + m[6][1] * r;
        v[cO + 20] = s[sO + 2] + m[6][2] * r;
        v[cO + 21] = s[sO] + m[7][0] * r;
        v[cO + 22] = s[sO + 1] + m[7][1] * r;
        v[cO + 23] = s[sO + 2] + m[7][2] * r;

        PolyLine.adjustVertices(v, cO, pO, D1, 8, 24);
    }

    /**
     * Fills color array for the poly octo line.
     *
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @param c        color array
     * @param cOffset  offset in the color array
     * @param color    vertex color
     */
    private static void fillPolyLineOctoColors(boolean useAlpha, float[] c, int cOffset, Color color)
    {
        if (useAlpha)
        {
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            c[cOffset + 3] = color._a;
            c[cOffset + 4] = color._r;
            c[cOffset + 5] = color._g;
            c[cOffset + 6] = color._b;
            c[cOffset + 7] = color._a;
            c[cOffset + 8] = color._r;
            c[cOffset + 9] = color._g;
            c[cOffset + 10] = color._b;
            c[cOffset + 11] = color._a;
            c[cOffset + 12] = color._r;
            c[cOffset + 13] = color._g;
            c[cOffset + 14] = color._b;
            c[cOffset + 15] = color._a;
            c[cOffset + 16] = color._r;
            c[cOffset + 17] = color._g;
            c[cOffset + 18] = color._b;
            c[cOffset + 19] = color._a;
            c[cOffset + 20] = color._r;
            c[cOffset + 21] = color._g;
            c[cOffset + 22] = color._b;
            c[cOffset + 23] = color._a;
            c[cOffset + 24] = color._r;
            c[cOffset + 25] = color._g;
            c[cOffset + 26] = color._b;
            c[cOffset + 27] = color._a;
            c[cOffset + 28] = color._r;
            c[cOffset + 29] = color._g;
            c[cOffset + 30] = color._b;
            c[cOffset + 31] = color._a;
        }
        else
        {
            c[cOffset] = color._r;
            c[cOffset + 1] = color._g;
            c[cOffset + 2] = color._b;
            c[cOffset + 3] = color._r;
            c[cOffset + 4] = color._g;
            c[cOffset + 5] = color._b;
            c[cOffset + 6] = color._r;
            c[cOffset + 7] = color._g;
            c[cOffset + 8] = color._b;
            c[cOffset + 9] = color._r;
            c[cOffset + 10] = color._g;
            c[cOffset + 11] = color._b;
            c[cOffset + 12] = color._r;
            c[cOffset + 13] = color._g;
            c[cOffset + 14] = color._b;
            c[cOffset + 15] = color._r;
            c[cOffset + 16] = color._g;
            c[cOffset + 17] = color._b;
            c[cOffset + 18] = color._r;
            c[cOffset + 19] = color._g;
            c[cOffset + 20] = color._b;
            c[cOffset + 21] = color._r;
            c[cOffset + 22] = color._g;
            c[cOffset + 23] = color._b;
        }
    }

    /**
     * Fills indices array for the poly octo line.
     *
     * @param ii       indices array (ints)
     * @param is       indices array (shorts
     * @param iaOffset indices array offset
     * @param iOffset  pointed vertex offset
     */
    private static void fillPolyLineOctoIndices(int[] ii, short[] is, int iaOffset, int iOffset)
    {
        if (ii != null)
        {
            ii[iaOffset] = iOffset;
            ii[iaOffset + 1] = iOffset + 8;
            ii[iaOffset + 2] = iOffset + 9;
            ii[iaOffset + 3] = iOffset;
            ii[iaOffset + 4] = iOffset + 1;
            ii[iaOffset + 5] = iOffset + 9;
            ii[iaOffset + 6] = iOffset + 1;
            ii[iaOffset + 7] = iOffset + 9;
            ii[iaOffset + 8] = iOffset + 10;
            ii[iaOffset + 9] = iOffset + 1;
            ii[iaOffset + 10] = iOffset + 2;
            ii[iaOffset + 11] = iOffset + 10;
            ii[iaOffset + 12] = iOffset + 2;
            ii[iaOffset + 13] = iOffset + 10;
            ii[iaOffset + 14] = iOffset + 11;
            ii[iaOffset + 15] = iOffset + 2;
            ii[iaOffset + 16] = iOffset + 3;
            ii[iaOffset + 17] = iOffset + 11;
            ii[iaOffset + 18] = iOffset + 3;
            ii[iaOffset + 19] = iOffset + 11;
            ii[iaOffset + 20] = iOffset + 12;
            ii[iaOffset + 21] = iOffset + 3;
            ii[iaOffset + 22] = iOffset + 4;
            ii[iaOffset + 23] = iOffset + 12;
            ii[iaOffset + 24] = iOffset + 4;
            ii[iaOffset + 25] = iOffset + 12;
            ii[iaOffset + 26] = iOffset + 13;
            ii[iaOffset + 27] = iOffset + 4;
            ii[iaOffset + 28] = iOffset + 5;
            ii[iaOffset + 29] = iOffset + 13;
            ii[iaOffset + 30] = iOffset + 5;
            ii[iaOffset + 31] = iOffset + 13;
            ii[iaOffset + 32] = iOffset + 14;
            ii[iaOffset + 33] = iOffset + 5;
            ii[iaOffset + 34] = iOffset + 6;
            ii[iaOffset + 35] = iOffset + 14;
            ii[iaOffset + 36] = iOffset + 6;
            ii[iaOffset + 37] = iOffset + 14;
            ii[iaOffset + 38] = iOffset + 15;
            ii[iaOffset + 39] = iOffset + 6;
            ii[iaOffset + 40] = iOffset + 7;
            ii[iaOffset + 41] = iOffset + 15;
            ii[iaOffset + 42] = iOffset + 7;
            ii[iaOffset + 43] = iOffset + 15;
            ii[iaOffset + 44] = iOffset + 8;
            ii[iaOffset + 45] = iOffset + 7;
            ii[iaOffset + 46] = iOffset;
            ii[iaOffset + 47] = iOffset + 8;
        }
        else
        {
            is[iaOffset] = (short) iOffset;
            is[iaOffset + 1] = (short) (iOffset + 8);
            is[iaOffset + 2] = (short) (iOffset + 9);
            is[iaOffset + 3] = (short) iOffset;
            is[iaOffset + 4] = (short) (iOffset + 1);
            is[iaOffset + 5] = (short) (iOffset + 9);
            is[iaOffset + 6] = (short) (iOffset + 1);
            is[iaOffset + 7] = (short) (iOffset + 9);
            is[iaOffset + 8] = (short) (iOffset + 10);
            is[iaOffset + 9] = (short) (iOffset + 1);
            is[iaOffset + 10] = (short) (iOffset + 2);
            is[iaOffset + 11] = (short) (iOffset + 10);
            is[iaOffset + 12] = (short) (iOffset + 2);
            is[iaOffset + 13] = (short) (iOffset + 10);
            is[iaOffset + 14] = (short) (iOffset + 11);
            is[iaOffset + 15] = (short) (iOffset + 2);
            is[iaOffset + 16] = (short) (iOffset + 3);
            is[iaOffset + 17] = (short) (iOffset + 11);
            is[iaOffset + 18] = (short) (iOffset + 3);
            is[iaOffset + 19] = (short) (iOffset + 11);
            is[iaOffset + 20] = (short) (iOffset + 12);
            is[iaOffset + 21] = (short) (iOffset + 3);
            is[iaOffset + 22] = (short) (iOffset + 4);
            is[iaOffset + 23] = (short) (iOffset + 12);
            is[iaOffset + 24] = (short) (iOffset + 4);
            is[iaOffset + 25] = (short) (iOffset + 12);
            is[iaOffset + 26] = (short) (iOffset + 13);
            is[iaOffset + 27] = (short) (iOffset + 4);
            is[iaOffset + 28] = (short) (iOffset + 5);
            is[iaOffset + 29] = (short) (iOffset + 13);
            is[iaOffset + 30] = (short) (iOffset + 5);
            is[iaOffset + 31] = (short) (iOffset + 13);
            is[iaOffset + 32] = (short) (iOffset + 14);
            is[iaOffset + 33] = (short) (iOffset + 5);
            is[iaOffset + 34] = (short) (iOffset + 6);
            is[iaOffset + 35] = (short) (iOffset + 14);
            is[iaOffset + 36] = (short) (iOffset + 6);
            is[iaOffset + 37] = (short) (iOffset + 14);
            is[iaOffset + 38] = (short) (iOffset + 15);
            is[iaOffset + 39] = (short) (iOffset + 6);
            is[iaOffset + 40] = (short) (iOffset + 7);
            is[iaOffset + 41] = (short) (iOffset + 15);
            is[iaOffset + 42] = (short) (iOffset + 7);
            is[iaOffset + 43] = (short) (iOffset + 15);
            is[iaOffset + 44] = (short) (iOffset + 8);
            is[iaOffset + 45] = (short) (iOffset + 7);
            is[iaOffset + 46] = (short) iOffset;
            is[iaOffset + 47] = (short) (iOffset + 8);
        }
    }
}
