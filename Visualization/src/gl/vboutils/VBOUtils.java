package gl.vboutils;

import space.Dimension;

/**
 * Provides some functionalities related to preparing VBOs.
 *
 * @author MTomczyk
 */
public class VBOUtils
{

    /**
     * Provides indices array for cuboid rendering.
     * It is assumed that vertices are obtained using {@link #getCuboidVertices(Dimension[])} method.
     */
    public static final short[] _cuboidIndices = new short[]
            {
                    0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7, 7, 4, 0, 4, 1, 5, 2, 6, 3, 7
            };

    /**
     * Prepares and returns vertices representing a cuboid.
     * To be rendered using GL_LINES.
     *
     * @param D cuboid dimensions
     * @return cuboid vertices
     */
    public static float[] getCuboidVertices(Dimension[] D)
    {
        assert D != null && D.length == 3;

        float lx = (float) D[0]._position;
        float rx = (float) (D[0].getRightPosition());

        float by = (float) D[1]._position;
        float ty = (float) (D[1].getRightPosition());

        float bz = (float) D[2]._position;
        float fz = (float) (D[2].getRightPosition());

        return new float[]
                {
                        lx, by, bz,
                        lx, by, fz,
                        lx, ty, fz,
                        lx, ty, bz,

                        rx, by, bz,
                        rx, by, fz,
                        rx, ty, fz,
                        rx, ty, bz,

                };
    }
}
