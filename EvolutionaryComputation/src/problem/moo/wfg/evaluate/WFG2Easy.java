package problem.moo.wfg.evaluate;

import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG2_Final;

import java.util.LinkedList;

/**
 * Implementation of the WFG2 test problem (easy = minimal number of decision variables and no transformations).
 *
 * @author MTomczyk
 */


public class WFG2Easy extends WFG2
{

    /**
     * Parameterized constructor. Sets the number of position-related parameters to M - 1, while the number of distance-related
     * parameters to 2.
     *
     * @param M the number of objectives M
     */
    public WFG2Easy(int M)
    {
        super(M, M - 1, 2);
    }

    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    @Override
    public LinkedList<ITransformation> getTransformations()
    {
        LinkedList<ITransformation> transformations = new LinkedList<>();
        transformations.add(new WFG2_Final(_M, _k, _l));
        return transformations;
    }

}
