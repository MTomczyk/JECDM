package statistics;

/**
 * Contains some useful functions.
 *
 * @author MTomczyk
 */
public class Statistics
{
    /**
     * Calculates the relative difference.
     *
     * @param base base for the calculations (difference relative to this value)
     * @param v    the other value (relative difference from v to base is to be calculated).
     * @return relative difference of v from the base.
     */
    public static double getRelativeDifference(double base, double v)
    {
        return Math.abs(base - v) / base;
    }
}
