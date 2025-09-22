package selection;

import population.Specimen;

/**
 * Default implementation of {@link IComparator}. It compares two presented specimens based on their first auxiliary
 * scores.
 *
 * @author MTomczyk
 */
public class ByAux implements IComparator
{
    /**
     * Aux score preference direction: true = gain (to be maximized); false = cost (to be minimized); used for
     * determining the comparison winner
     */
    private final boolean _preferenceDirection;

    /**
     * Parameterized constructor.
     *
     * @param preferenceDirection aux score preference direction: true = gain (to be maximized); false = cost (to be
     *                            minimized); used for determining the comparison winner
     */
    public ByAux(boolean preferenceDirection)
    {
        _preferenceDirection = preferenceDirection;
    }

    @Override
    public int compare(Specimen A, Specimen B)
    {
        int comp = Double.compare(A.getAlternative().getAuxScore(), B.getAlternative().getAuxScore());
        if (_preferenceDirection) return comp;
        else return -comp;
    }
}
