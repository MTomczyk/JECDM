package problem.moo.wfg.evaluate;

import problem.moo.wfg.transformations.ITransformation;

import java.util.LinkedList;

/**
 * Implementation of the WFG1 test problem (easy = minimal number of decision variables and no transformations).
 *
 * @author MTomczyk
 */


public class WFG1Easy extends WFG1
{
    /**
     * Parameterized constructor. Sets the number of position-related parameters to M - 1, while the number of distance-related
     * parameters to 1.
     *
     * @param M the number of objectives M
     */
    public WFG1Easy(int M)
    {
        super(M, M - 1, 1, 0.0d);
    }

    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    @Override
    public LinkedList<ITransformation> getTransformations()
    {
        return new LinkedList<>();
    }
}
