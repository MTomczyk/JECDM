package dataset.painter.glutils;

import color.Color;
import gl.vboutils.BufferData;

import java.util.ListIterator;

/**
 * Provides an auxiliary method for creating VBO data that illustrates regular lines.
 *
 * @author MTomczyk
 */
public class RegularLine
{
    /**
     * Creates the buffer data for a contiguous line (drawn using GL_LINES)
     *
     * @param cLine          currently processed contiguous line (coordinates)
     * @param noPointsIt     stores the number of regular points for the contiguous line
     * @param colorsIterator stores the color data for the regular points in the contiguous line
     * @param noAuxPointsIt  stores the number of auxiliary points for the contiguous line
     * @param auxLinesIt     stores the data on the coordinates of the auxiliary points of the contiguous line
     * @param auxColorsIt    stores the data on the colors of auxiliary points of the contiguous line
     * @param useGradient    true -> gradient mode is used
     * @param colorStride    color stride (3 or 4 in the case alpha channel is used)
     * @param useAlpha       true -> alpha channel is used
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
        int originalNoPoints = noPointsIt.next();
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

        int noPoints = originalNoPoints;
        if (noAuxPoints != null) for (int ap : noAuxPoints) noPoints += ap;
        float[] v = new float[noPoints * 3];
        boolean intIndices = noPoints > Short.MAX_VALUE;
        int[] ii = null;
        short[] is = null;
        if (intIndices) ii = new int[noPoints];
        else is = new short[noPoints];
        float[] c = null;
        if (useGradient) c = new float[noPoints * colorStride];


        int cLineOffset = 0;
        int vOffset = 0;
        int cOffset = 0;
        int vAuxOffset = 0;
        int auxPassed = 0;
        int indexNo = 0;

        for (int i = 0; i < originalNoPoints; i++)
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
            if ((noAuxPoints != null) && (i < originalNoPoints - 1))
            {
                for (int j = 0; j < noAuxPoints[i]; j++)
                {
                    v[vOffset] = auxLines[vAuxOffset];
                    v[vOffset + 1] = auxLines[vAuxOffset + 1];
                    v[vOffset + 2] = auxLines[vAuxOffset + 2];
                    vAuxOffset += 3;
                    vOffset += 3;

                    if (ii != null) ii[indexNo] = indexNo;
                    else is[indexNo] = (short) indexNo;
                    indexNo++;

                    c[cOffset] = auxColors[auxPassed + j]._r;
                    c[cOffset + 1] = auxColors[auxPassed + j]._g;
                    c[cOffset + 2] = auxColors[auxPassed + j]._b;
                    if (useAlpha) c[cOffset + 3] = colors[auxPassed + j]._a;
                    cOffset += colorStride;
                }
                auxPassed += noAuxPoints[i];

            }
        }

        BufferData bd;
        if (ii != null) bd = new BufferData(v, ii, c);
        else bd = new BufferData(v, is, c);
        return bd;
    }
}
