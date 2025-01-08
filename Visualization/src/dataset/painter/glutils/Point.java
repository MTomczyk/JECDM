package dataset.painter.glutils;

import dataset.painter.IDS3D;

/**
 * Provides an auxiliary method for creating VBO data that illustrates points.
 *
 * @author MTomczyk
 */
public class Point
{
    /**
     * Fills VBO-related data (marker fill: points)
     *
     * @param v        vertex array to be filled
     * @param ii       index array to be filled (integers) (either ii or is must be null)
     * @param is       index array to be filled (shorts) (either ii or is must be null)
     * @param c        color array to be filled (can be null: not filled; only when the used color is not a gradient)
     * @param useAlpha true, the fourth channel is used
     * @param P        projection data
     */
    public static void fillPointFillData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha, IDS3D P)
    {
        System.arraycopy(P._projectedMarkers, 0, v, 0, P._projectedMarkers.length);

        // indices
        if (ii != null) for (int i = 0; i < P._noMarkerPoints; i++) ii[i] = i;
        else for (short i = 0; i < P._noMarkerPoints; i++) is[i] = i;

        // colors
        if (c != null)
        {
            int pnt = 0;
            for (int i = 0; i < P._noMarkerPoints; i++)
            {
                c[pnt] = P._markerFillGradientColors[i]._r;
                c[pnt + 1] = P._markerFillGradientColors[i]._g;
                c[pnt + 2] = P._markerFillGradientColors[i]._b;
                if (useAlpha)
                {
                    c[pnt + 3] = P._markerFillGradientColors[i]._a;
                    pnt += 4;
                }
                else pnt += 3;
            }
        }
    }
}
