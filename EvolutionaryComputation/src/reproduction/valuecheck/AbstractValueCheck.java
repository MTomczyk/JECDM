package reproduction.valuecheck;

/**
 * Abstract implementation of {@link IValueCheck}
 *
 * @author MTomczyk
 */
public abstract class AbstractValueCheck implements IValueCheck
{
    /**
     * Checks and corrects int values in an array.
     *
     * @param values array whose values are to be checked and conditionally corrected
     * @param lb     domain left bound
     * @param rb     domain right bound
     * @return returns input array
     */
    public int[] checkAndCorrect(int[] values, int lb, int rb)
    {
        for (int i = 0; i < values.length; i++)
            values[i] = checkAndCorrect(values[i], lb, rb);
        return values;
    }

    /**
     * Checks and corrects double values in an array.
     *
     * @param values array whose values are to be checked and conditionally corrected
     * @param lb     domain left bound
     * @param rb     domain right bound
     * @return returns input array
     */
    public double[] checkAndCorrect(double[] values, double lb, double rb)
    {
        for (int i = 0; i < values.length; i++)
            values[i] = checkAndCorrect(values[i], lb, rb);
        return values;
    }
}
