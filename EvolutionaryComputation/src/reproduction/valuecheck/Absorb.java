package reproduction.valuecheck;

/**
 * Absorb technique for repairing gene values (absorb = set to lower or upper bound).
 *
 * @author MTomczyk
 */


public class Absorb implements IValueCheck
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
        if (value < lb) return lb;
        //noinspection ManualMinMaxCalculation
        if (value > rb) return rb;
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
        if (value < lb) return lb;
        //noinspection ManualMinMaxCalculation
        if (value > rb) return rb;
        return value;
    }
}
