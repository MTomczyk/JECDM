package reproduction.valuecheck;

/**
 * Absorb technique for repairing gene values (wrap around boundaries).
 *
 * @author MTomczyk
 */


public class Wrap implements IValueCheck
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
        while ((value < lb) || (value > rb))
        {
            if (value < lb) value = 2.0d * lb - value;
            if (value > rb) value = 2.0d * rb - value;
        }
        return value;
    }

}
