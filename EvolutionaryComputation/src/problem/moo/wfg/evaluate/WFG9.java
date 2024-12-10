package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.Concave;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.transformations.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG9 test problem.
 *
 * @author MTomczyk
 */


public class WFG9 extends WFGEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     */
    public WFG9(int M, int k, int l)
    {
        super(M, k, l);
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
        transformations.add(new bParamWFG7(0.98 / 49.98, 0.02, 50, 0, _n - 1, _k, _l));
        transformations.add(new sDeceptive(0.35, 0.001, 0.05, 0, _k));
        transformations.add(new sMulti(30, 95, 0.35, _k, _n));
        transformations.add(new WFG6Final(_M, _k, _l));
        return transformations;
    }

    /**
     * Returns the problem-related shapes.
     *
     * @return shapes
     */
    @Override
    public ArrayList<IShape> getShapes()
    {
        ArrayList<IShape> shapes = new ArrayList<>(_M);
        for (int i = 0; i < _M; i++) shapes.add(new Concave(i, _M));
        return shapes;
    }
}
