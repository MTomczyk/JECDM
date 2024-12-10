package problem.moo.wfg.evaluate;

import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG2Final;

import java.util.LinkedList;

/**
 * Implementation of the WFG3 test problem (easy = minimal number of decision variables and no transformations).
 *
 * @author MTomczyk
 */


public class WFG3Easy extends WFG3
{
    /**
     * Parameterized constructor. Sets the number of position-related parameters to M - 1, while the number of distance-related
     * parameters to 2.
     *
     * @param M          the number of objectives M
     */
    public WFG3Easy(int M)
    {
        this(M, true);
    }

    /**
     * Parameterized constructor. Sets the number of position-related parameters to M - 1, while the number of distance-related
     * parameters to 2.
     *
     * @param M          the number of objectives M
     * @param degeneracy if false, the degeneracy is disabled
     */
    public WFG3Easy(int M, boolean degeneracy)
    {
        super(M, M - 1, 2, degeneracy);
    }

    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    protected LinkedList<ITransformation> getTransformations()
    {
        LinkedList<ITransformation> transformations = new LinkedList<>();
        transformations.add(new WFG2Final(_M, _k, _l));
        return transformations;
    }
}
