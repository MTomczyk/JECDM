package reproduction.valuecheck;

/**
 * Supportive class for repairing decision vector values.
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
     * Checks and corrects int values in an array.
     *
     * @param values array whose values are to be checked and conditionally corrected
     * @param lb     domain left bound
     * @param rb     domain right bound
     * @return returns input array
     */
    int[] checkAndCorrect(int[] values, int lb, int rb);

    /**
     * Checks and corrects double values.
     *
     * @param value value to be checked
     * @param lb    domain left bound
     * @param rb    domain right bound
     * @return corrected value
     */
    double checkAndCorrect(double value, double lb, double rb);

    /**
     * Checks and corrects double values in an array.
     *
     * @param values array whose values are to be checked and conditionally corrected
     * @param lb     domain left bound
     * @param rb     domain right bound
     * @return returns input array
     */
    double[] checkAndCorrect(double[] values, double lb, double rb);
}
