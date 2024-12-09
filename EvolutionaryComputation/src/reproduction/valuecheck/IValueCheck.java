package reproduction.valuecheck;

/**
 * Supportive class for repairing gene values.
 */
public interface IValueCheck
{
    /**
     * Checks and corrects int values.
     *
     * @param value value to be checked
     * @param lb    domain left bound
     * @param rb    domain right bound
     * @return corrected value
     */
    int checkAndCorrect(int value, int lb, int rb);

    /**
     * Checks and corrects double values.
     *
     * @param value value to be checked
     * @param lb    domain left bound
     * @param rb    domain right bound
     * @return corrected value
     */
    double checkAndCorrect(double value, double lb, double rb);
}
