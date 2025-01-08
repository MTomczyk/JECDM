package dataset.painter.glutils;

import color.Color;
import gl.vboutils.BufferData;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Provides an auxiliary method for creating VBO data that illustrates regular lines.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class RegularLine
{
    /**
     * Creates the buffer data for a contiguous line (drawn using GL_LINE_STRIP)
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
     * @return buffer data
     */
    public static BufferData getRegularLineData(float[] cLine,
                                                ListIterator<Integer> noPointsIt,
                                                ListIterator<Color[]> colorsIterator,
                                                ListIterator<int[]> noAuxPointsIt,
                                                ListIterator<float[]> auxLinesIt,
                                                ListIterator<Color[]> auxColorsIt,
                                                boolean useGradient, int colorStride, boolean useAlpha)
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

        return getRegularLineData(cLine, noPoints, colors, noAuxPoints, auxLines, auxColors, useGradient, colorStride, useAlpha);
    }


    /**
     * Creates the buffer data for a contiguous line (drawn using GL_LINE_STRIP)
     *
     * @param cLine       currently processed contiguous line (coordinates)
     * @param noPoints    the number of regular points for the contiguous line
     * @param colors      color data for the regular points in the contiguous line
     * @param noAuxPoints data on the number of auxiliary points for the contiguous line
     * @param auxLines    data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColors   data on the colors of auxiliary points of the contiguous line
     * @param useGradient if true, then the gradient mode is used
     * @param colorStride color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha    if true, then alpha channel is used
     * @return buffer data
     */
    private static BufferData getRegularLineData(float[] cLine,
                                                 int noPoints,
                                                 Color[] colors,
                                                 int[] noAuxPoints,
                                                 float[] auxLines,
                                                 Color[] auxColors,
                                                 boolean useGradient,
                                                 int colorStride,
                                                 boolean useAlpha)
    {

        int noPointsWithAux = noPoints;
        if (noAuxPoints != null) for (int ap : noAuxPoints) noPointsWithAux += ap;

        int cLineOffset = 0;
        int vAuxLinesOffset = 0;
        int vAuxColorsOffset = 0;

        return getBD(0, noPoints, noPointsWithAux, useGradient, colorStride, useAlpha, colors, cLine,
                auxLines, noAuxPoints, 0, 1, auxColors, cLineOffset, vAuxLinesOffset, vAuxColorsOffset);
    }

    /**
     * Creates the buffer data object
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
                                    int vAuxColorsOffset)
    {
        float[] v = new float[noPointsWithAux * 3];
        boolean intIndices = noPointsWithAux > Short.MAX_VALUE;
        int[] ii = null;
        short[] is = null;
        if (intIndices) ii = new int[noPointsWithAux];
        else is = new short[noPointsWithAux];
        float[] c = null;
        if (useGradient) c = new float[noPointsWithAux * colorStride];

        int vOffset = 0;
        int cOffset = 0;
        int indexNo = 0;

        for (int i = pointsOffset; i < pointsOffset + noPoints; i++)
        {
            v[vOffset] = cLine[cLineOffset];
            v[vOffset + 1] = cLine[cLineOffset + 1];
            v[vOffset + 2] = cLine[cLineOffset + 2];
            cLineOffset += 3;
            vOffset += 3;

            if (ii != null) ii[indexNo] = indexNo;
            else is[indexNo] = (short) indexNo;
            indexNo++;

            if (useGradient)
            {
                c[cOffset] = colors[i]._r;
                c[cOffset + 1] = colors[i]._g;
                c[cOffset + 2] = colors[i]._b;
                if (useAlpha) c[cOffset + 3] = colors[i]._a;
                cOffset += colorStride;
            }

            // aux after adding the point, and if the point is not last
            if ((noAuxPoints != null) && (i < pointsOffset + noPoints - 1))
            {
                for (int j = 0; j < noAuxPoints[noAuxPointsOffset]; j++) // indeksowanie tego jest inne przy innej interpretacji
                {
                    v[vOffset] = auxLines[vAuxLinesOffset];
                    v[vOffset + 1] = auxLines[vAuxLinesOffset + 1];
                    v[vOffset + 2] = auxLines[vAuxLinesOffset + 2];
                    vAuxLinesOffset += 3;
                    vOffset += 3;

                    if (ii != null) ii[indexNo] = indexNo;
                    else is[indexNo] = (short) indexNo;
                    indexNo++;

                    assert c != null;
                    c[cOffset] = auxColors[vAuxColorsOffset + j]._r;
                    c[cOffset + 1] = auxColors[vAuxColorsOffset + j]._g;
                    c[cOffset + 2] = auxColors[vAuxColorsOffset + j]._b;
                    if (useAlpha) c[cOffset + 3] = auxColors[vAuxColorsOffset + j]._a;
                    cOffset += colorStride;
                }

                vAuxColorsOffset += noAuxPoints[noAuxPointsOffset];
                noAuxPointsOffset += noAuxPointsStride;
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
     * @param cLine       currently processed contiguous line (coordinates)
     * @param noPoints    the number of regular points for the contiguous line
     * @param colors      color data for the regular points in the contiguous line
     * @param noAuxPoints data on the number of auxiliary points for the contiguous line
     * @param auxLines    data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColors   data on the colors of auxiliary points of the contiguous line
     * @param colorStride color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha    if true, then alpha channel is used
     * @return buffer data
     */
    public static LinkedList<BufferData> getRegularLineDataForNonContiguousModeWithGradient(float[] cLine,
                                                                                            int noPoints,
                                                                                            Color[] colors,
                                                                                            int[] noAuxPoints,
                                                                                            float[] auxLines,
                                                                                            Color[] auxColors,
                                                                                            int colorStride,
                                                                                            boolean useAlpha)
    {
        LinkedList<BufferData> lBD = new LinkedList<>();
        int auxIdx = 0;
        int cLineOffset = 0;
        int vAuxLinesOffset = 0;
        int vAuxColorsOffset = 0;

        for (int i = 0; i < noPoints; i += 2)
        {
            int noPointsWithAux = 2;
            noPointsWithAux += noAuxPoints[auxIdx];

            lBD.add(getBD(i, 2, noPointsWithAux, true, colorStride, useAlpha, colors, cLine, auxLines,
                    noAuxPoints, auxIdx, 0, auxColors, cLineOffset, vAuxLinesOffset, vAuxColorsOffset));

            cLineOffset += 6;
            vAuxLinesOffset += 3 * noAuxPoints[auxIdx];
            vAuxColorsOffset += noAuxPoints[auxIdx];
            auxIdx++;
        }
        return lBD;
    }


}
