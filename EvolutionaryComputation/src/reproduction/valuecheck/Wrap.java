package reproduction.valuecheck;

/**
 * Absorb technique for repairing decision vector values (wrap around boundaries).
 *
 * @author MTomczyk
 */
public class Wrap extends AbstractValueCheck implements IValueCheck
{
    /**
     * Checks and corrects int values.
     * @param value value to be checked
     * @param lb domain left bound
     * @param rb domain right bound
     * @return corrected value
     */
    @Override
    public int checkAndCorrect(int value, int lb, int rb)
    {
        while ((value < lb) || (value > rb))
        {
            if (value < lb) value = 2 * lb - value;
            if (value > rb) value = 2 * rb - value;
        }
        return value;
    }

    /**
     * Checks and corrects double values.
     * @param value value to be checked
     * @param lb domain left bound
     * @param rb domain right bound
     * @return corrected value
     */
    @Override
    public double checkAndCorrect(double value, double lb, double rb)
    {
        while ((Double.compare(value, lb) < 0) ||(Double.compare(value, rb) > 0))
        {
            if (Double.compare(value, lb) < 0) value = 2.0d * lb - value;
            if (Double.compare(value, rb) > 0) value = 2.0d * rb - value;
        }
        return value;
    }

}
