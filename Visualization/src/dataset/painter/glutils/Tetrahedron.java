package dataset.painter.glutils;

import dataset.painter.IDS3D;
import dataset.painter.style.MarkerStyle;
import gl.vboutils.OffsetStride;

/**
 * Provides an auxiliary method for creating VBO data that illustrates tetrahedrons.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tetrahedron
{
    /**
     * Fills VBO-related data (marker edge: tetrahedron)
     *
     * @param v        vertex array to be filled
     * @param ii       index array to be filled (integers) (either ii or is must be null)
     * @param is       index array to be filled (shorts) (either ii or is must be null)
     * @param c        color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha true, the fourth channel is used
     * @param P        projection data
     * @param ms       marker style
     */
    public static void fillTetrahedronEdgesData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms)
    {
        OffsetStride os = new OffsetStride();
        os._vaStride = 4 * 3;
        os._iaStride = 6 * 2;
        os._iStride = 4;
        if (useAlpha) os._caStride = 4 * 4;
        else os._caStride = 4 * 3;
        int pnt = 0;

        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronEdgesData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        ms._size, v, ii, is, null, os, null, useAlpha, ms._style);
                pnt += 3;
                os.applyStrides();
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronEdgesData(
                        P._projectedMarkers[pnt],
                        P._projectedMarkers[pnt + 1],
                        P._projectedMarkers[pnt + 2],
                        ms._size, v, ii, is, c, os,
                        P._markerEdgeGradientColors[i],
                        useAlpha, ms._style);
                pnt += 3;
                os.applyStrides();
            }
        }
    }


    /**
     * Fills VBO-related data (marker fill: tetrahedron)
     *
     * @param v        vertex array to be filled
     * @param ii       index array to be filled (integers) (either ii or is must be null)
     * @param is       index array to be filled (shorts) (either ii or is must be null)
     * @param c        color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha true, the fourth channel is used
     * @param P        projection data
     * @param ms       marker style
     */
    public static void fillTetrahedronFillData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms)
    {
        OffsetStride os = new OffsetStride();
        os._vaStride = 4 * 3;
        os._iaStride = 4 * 3;
        os._iStride = 4;
        if (useAlpha) os._caStride = 4 * 4;
        else os._caStride = 4 * 3;

        int pnt = 0;
        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        ms._size, v, ii, is, null, os, null, useAlpha, ms._style);
                pnt += 3;
                os.applyStrides();
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        ms._size, v, ii, is, c, os, P._markerFillGradientColors[i], useAlpha, ms._style);
                pnt += 3;
                os.applyStrides();
            }
        }
    }


}
