package dataset.painter.glutils;

import dataset.painter.IDS3D;
import dataset.painter.style.MarkerStyle;
import gl.vboutils.OffsetStride;

/**
 * Provides an auxiliary method for creating VBO data that illustrates icospheres.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Icosphere
{
    /**
     * Fills VBO-related data (marker fill: icosphere)
     *
     * @param v         vertex array to be filled
     * @param ii        index array to be filled (integers) (either ii or is must be null)
     * @param is        index array to be filled (shorts) (either ii or is must be null)
     * @param c         color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha  true, the fourth channel is used
     * @param P         projection data
     * @param ms        marker style
     * @param polyLevel corresponds to the quality level (0, 1, 2)
     */
    public static void fillIcosphereFillData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms, int polyLevel)
    {
        if (polyLevel < 0) polyLevel = 0;
        if (polyLevel > 2) polyLevel = 2;
        float r = ms._size / 2.0f;

        OffsetStride os = new OffsetStride();
        os._vaStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 3;
        os._iaStride = gl.vboutils.Icosphere._NO_INDICES_FILL[polyLevel];
        os._iStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel];
        if (useAlpha) os._caStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 4;
        else os._caStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 3;

        int pnt = 0;
        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Icosphere.fillIcospehereFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        r, v, ii, is, null, os, null, useAlpha, ms._style, polyLevel);
                pnt += 3;
                os.applyStrides();
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Icosphere.fillIcospehereFillData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        r, v, ii, is, c, os, P._markerFillGradientColors[i], useAlpha, ms._style, polyLevel);
                pnt += 3;
                os.applyStrides();
            }
        }
    }

    /**
     * Fills VBO-related data (marker edges: icosphere)
     *
     * @param v         vertex array to be filled
     * @param ii        index array to be filled (integers) (either ii or is must be null)
     * @param is        index array to be filled (shorts) (either ii or is must be null)
     * @param c         color array to be filled (can be null -> not filled; only when the used color is not a gradient)
     * @param useAlpha  true, the fourth channel is used
     * @param P         projection data
     * @param ms        marker style
     * @param polyLevel corresponds to the quality level (0, 1, 2)
     */
    public static void fillIcosphereEdgesData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P, MarkerStyle ms, int polyLevel)
    {
        if (polyLevel < 0) polyLevel = 0;
        if (polyLevel > 2) polyLevel = 2;
        float r = ms._size / 2.0f;

        OffsetStride os = new OffsetStride();
        os._vaStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 3;
        os._iaStride = gl.vboutils.Icosphere._NO_INDICES_EDGES[polyLevel];
        os._iStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel];
        if (useAlpha) os._caStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 4;
        else os._caStride = gl.vboutils.Icosphere._NO_VERTICES[polyLevel] * 3;

        int pnt = 0;
        if (c == null)
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Icosphere.fillIcospehereEdgesData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        r, v, ii, is, null, os, null, useAlpha, ms._style, polyLevel);
                pnt += 3;
                os.applyStrides();
            }
        }
        else
        {
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                gl.vboutils.Icosphere.fillIcospehereEdgesData(P._projectedMarkers[pnt], P._projectedMarkers[pnt + 1], P._projectedMarkers[pnt + 2],
                        r, v, ii, is, c, os, P._markerEdgeGradientColors[i], useAlpha, ms._style, polyLevel);
                pnt += 3;
                os.applyStrides();
            }
        }
    }
}
