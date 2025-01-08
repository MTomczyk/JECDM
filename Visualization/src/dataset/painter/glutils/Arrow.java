package dataset.painter.glutils;

import dataset.painter.IDSArrows;
import gl.vboutils.OffsetStride;

/**
 * Provides an auxiliary method for creating VBO data that illustrates arrows (heads).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Arrow
{
    /**
     * Fills VBO-related data (arrow fill: triangular)
     *
     * @param v         vertex array to be filled
     * @param ii        index array to be filled (integers) (either ii or is must be null)
     * @param is        index array to be filled (shorts) (either ii or is must be null)
     * @param c         color array to be filled (can be null: not filled; only when the used color is not a gradient)
     * @param useAlpha  true, the fourth channel is used
     * @param noArrows  no. arrows
     * @param idsArrows projection data (arrows)
     */
    public static void fillTriangularArrowFillData(float[] v, int[] ii, short[] is, float[] c, boolean useAlpha,
                                                   int noArrows, IDSArrows idsArrows)
    {
        OffsetStride os = new OffsetStride();
        os._vaStride = 4 * 3;
        os._iaStride = 4 * 3;
        os._iStride = 4;
        if (useAlpha) os._caStride = 4 * 4;
        else os._caStride = 4 * 3;

        if (c == null)
        {
            for (int i = 0; i < noArrows; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronFillData(idsArrows._arrowProjectedData, v, ii, is, c, os, null, useAlpha);
                os.applyStrides();
            }
        }
        else
        {
            for (int i = 0; i < noArrows; i++)
            {
                gl.vboutils.Tetrahedron.fillTetrahedronFillData(idsArrows._arrowProjectedData, v, ii, is, c,
                        os, idsArrows._arrowFillColors[i], useAlpha);
                os.applyStrides();
            }
        }
    }
}
