package reproduction.operators;

/**
 * Provides various auxiliary functions.
 *
 * @author MTomczyk
 */
public class ReproductionUtils
{
    /**
     * Calculates the number of 'true' flags in the input boolean vector.
     *
     * @param v input vector
     * @return the number of 'true' flags in the input vector (0, if the input is null or empty)
     */
    public static int countTrues(boolean[] v)
    {
        if (v == null) return 0;
        if (v.length == 0) return 0;
        int c = 0;
        for (boolean b : v) if (b) c++;
        return c;
    }
}
