package problem.moo.wfg.evaluate;

import problem.moo.wfg.transformations.ITransformation;

import java.util.LinkedList;

/**
 * Implementation of the WFG9 test problem (easy = minimal number of decision variables and no transformations).
 *
 * @author MTomczyk
 */


public class WFG9Easy extends WFG9
{
    /**
     * Parameterized constructor. Sets the number of position-related parameters to M - 1, while the number of distance-related
     * parameters to 1.
     *
     * @param M the number of objectives M
     */
    public WFG9Easy(int M)
    {
        super(M, M - 1, 1);
    }

    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public LinkedList<ITransformation> getTransformations()
    {
        return new LinkedList<>();
    }
}
