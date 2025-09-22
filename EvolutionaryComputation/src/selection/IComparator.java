package selection;

import population.Specimen;

/**
 * Auxiliary interface allowing customization of the in-tournament specimen comparisons.
 *
 * @author MTomczyk
 */
public interface IComparator
{
    /**
     * The main method for comparing two specimens.
     *
     * @param A the first specimen
     * @param B the second specimen
     * @return 1 if A is considered better, -1 if B is considered better, 0 in the case of a draw.
     */
    int compare(Specimen A, Specimen B);
}
