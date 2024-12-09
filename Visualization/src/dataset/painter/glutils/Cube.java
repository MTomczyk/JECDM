package dataset.painter.glutils;

import dataset.painter.IDS3D;
import dataset.painter.style.MarkerStyle;
import gl.vboutils.OffsetStride;

/**
 * Provides an auxiliary method for creating VBO data that illustrates cubes.
 *
 * @author MTomczyk
 */
public class Cube
{
    /**
     * Fills VBO-related data (marker edge: cube)
     *
     * @param v        vertex array to be filled
     * @param ii       index array to be filled (integers) (either ii or is must be null)
     * @param is       index array to be filled (shorts) (either ii or is must be null)
     * @param c        color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha true, the fourth channel is used
     * @param P        projection data
     * @param ms       marker style
     */
    public static void fillCubeEdgesData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms)
    {
        OffsetStride os = new OffsetStride();
        os._vaStride = 8 * 3;
        os._iaStride = 12 * 2;
        os._iStride = 8;
        if (useAlpha) os._caStride = 8 * 4;
        else os._caStride = 8 * 3;

        int pnt = 0;
        float hSize = ms._size / 2.0f;

        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Cube.fillCubeEdgesData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        hSize, v, ii, is, null, os, null, useAlpha);
                os.applyStrides();
                pnt += 3;
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Cube.fillCubeEdgesData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        hSize, v, ii, is, c, os, P._markerEdgeGradientColors[i], useAlpha);
                os.applyStrides();
                pnt += 3;
            }
        }
    }

    /**
     * Fills VBO-related data (marker fill: cube)
     *
     * @param v        vertex array to be filled
     * @param ii       index array to be filled (integers) (either ii or is must be null)
     * @param is       index array to be filled (shorts) (either ii or is must be null)
     * @param c        color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha true, the fourth channel is used
     * @param P        projection data
     * @param ms       marker style
     */
    public static void fillCubeFillData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms)
    {
        OffsetStride os = new OffsetStride();
        os._vaStride = 8 * 3;
        os._iaStride = 6 * 2 * 3;
        os._iStride = 8;
        if (useAlpha) os._caStride = 8 * 4;
        else os._caStride = 8 * 3;

        int pnt = 0;
        float hSize = ms._size / 2.0f;

        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Cube.fillCubeFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        hSize, v, ii, is, null, os, null, useAlpha);
                os.applyStrides();
                pnt += 3;
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Cube.fillCubeFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        hSize, v, ii, is, c, os, P._markerFillGradientColors[i], useAlpha);
                os.applyStrides();
                pnt += 3;
            }
        }
    }
}
