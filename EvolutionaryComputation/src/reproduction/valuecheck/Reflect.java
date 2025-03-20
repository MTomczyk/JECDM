package reproduction.valuecheck;

/**
 * Absorb technique for repairing decision vector values (reflect from the boundary).
 *
 * @author MTomczyk
 */
public class Reflect extends AbstractValueCheck implements IValueCheck
{
    /**
     * Checks and corrects int values.
     *
     * @param value value to be checked
     * @param lb    domain left bound
     * @param rb    domain right bound
     * @return corrected value
     */
    @Override
    public int checkAndCorrect(int value, int lb, int rb)
    {
        if (value < lb) while (value < lb) value += (rb - lb);
        if (value > rb) while (value > rb) value -= (rb - lb);
        return value;
    }

    /**
     * Checks and corrects double values.
     *
     * @param value value to be checked
     * @param lb    domain left bound
     * @param rb    domain right bound
     * @return corrected value
     */
    @Override
    public double checkAndCorrect(double value, double lb, double rb)
    {
        if (value < lb) while (value < lb) value += (rb - lb);
        if (value > rb) while (value > rb) value -= (rb - lb);
        return value;
    }
}
