package statistics;

/**
 * General interface for statistic functions operating on double arrays.
 *
 * @author MTomczyk
 */

public interface IStatistic
{
    /**
     * Calculates the statistic.
     *
     * @param v input array
     * @return statistic
     */
    double calculate(double[] v);

    /**
     * Auxiliary method for retrieving the string representation.
     *
     * @return string representation
     */
    String getName();

    /**
     * The implementation must overwrite the toString() method.
     */
    @Override
    String toString();
}
