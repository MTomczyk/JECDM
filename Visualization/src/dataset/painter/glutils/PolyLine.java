package dataset.painter.glutils;

import space.Vector;

/**
 * Provides an auxiliary method for creating VBO data that illustrates polygon-based lines.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class PolyLine
{
    /**
     * Auxiliary method for getting the reference (average) vector.
     *
     * @param D1 first delta when moving along vertices
     * @param D2 second delta when moving along vertices
     * @return reference vector
     */
    protected static float[] getReferenceVector(float[] D1, float[] D2)
    {
        float[] refVector;
        if (D1 != null)
        {
            refVector = Vector.getUnitVector(D1);
            if (D2 != null)
            {
                float[] d2 = Vector.getUnitVector(D2.clone());
                refVector[0] += d2[0];
                refVector[1] += d2[1];
                refVector[2] += d2[2];
            }
        }
        else if (D2 != null) refVector = Vector.getUnitVector(D2.clone());
        else refVector = null;

        if ((Vector.isZeroVector(refVector))) refVector = null;

        return refVector;
    }

    /**
     * Auxiliary method that adjusts vertices order for plane.
     *
     * @param v         vertices
     * @param cO        current vertex offset
     * @param pO        previous vertex offset
     * @param D1        first delta
     * @param rotations no. rotations
     * @param vS        plane vertex stride
     */
    protected static void adjustVertices(float[] v, int cO, int pO, float[] D1, int rotations, int vS)
    {
        if (pO < 0) return;
        if (D1 == null) return;

        int idx = 0;
        float ba;

        float a = Vector.getCosineSimilarity(new float[]{v[cO] - v[pO], v[cO + 1] - v[pO + 1], v[cO + 2] - v[pO + 2]}, D1);
        ba = a;
        int aO = cO + 3;
        for (int rot = 1; rot < rotations; rot++)
        {
            a = Vector.getCosineSimilarity(new float[]{v[aO] - v[pO], v[aO + 1] - v[pO + 1], v[aO + 2] - v[pO + 2]}, D1);
            aO += 3;

            if (Double.compare(Math.abs(a) , Math.abs(ba)) > 0)
            {
                ba = a;
                idx = rot;
            }
        }
        if (idx > 0)
        {
            float[] tmp = new float[vS];
            System.arraycopy(v, cO, tmp, 0, vS);
            System.arraycopy(tmp, 3 * idx, v, cO, vS - 3 * idx);
            System.arraycopy(tmp, 0, v, cO + vS - 3 * idx, 3 * idx);
        }

    }
}
