package utils.weights;

/**
 * Provides normalized weight vectors.
 *
 * @author MTomczyk
 */
public class WeightsGetter
{
    /**
     * Returns a weight vector (from the pre-defined ones).
     *
     * @param M vector length (dimensions)
     * @param t vector no. (e.g., trial ID)
     * @return weight vector (returns null if cannot find proper vector).
     */
    public static double[] getPredefinedVector(int M, int t)
    {
        double[][] w = null;
        if (M == 2) w = Weights2D._data;
        else if (M == 3) w = Weights3D._data;
        else if (M == 4) w = Weights4D._data;
        else if (M == 5) w = Weights5D._data;
        else if (M == 6) w = Weights6D._data;
        else if (M == 7) w = Weights7D._data;
        else if (M == 8) w = Weights8D._data;
        else if (M == 9) w = Weights9D._data;
        else if (M == 10) w = Weights10D._data;
        if ((w != null) && (w.length > t)) return w[t];
        else return null;
    }
}
