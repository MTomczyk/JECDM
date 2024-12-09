package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.Convex;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.shapes.Mixed;
import problem.moo.wfg.transformations.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG1 test problem.
 *
 * @author MTomczyk
 */


public class WFG1 extends WFGEvaluate
{
    /**
     * Bias level.
     */
    private final double _alpha;

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @param alpha bias level alpha
     */
    public WFG1(int M, int k, int l, double alpha)
    {
        super(M, k, l);
        _alpha = alpha;
    }

    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    @Override
    protected LinkedList<ITransformation> getTransformations()
    {
        LinkedList<ITransformation> transformations = new LinkedList<>();
        transformations.add(new s_linear(0.35d, _k, _n));
        transformations.add(new b_flat(0.8d, 0.75d, 0.85d, _k, _n));
        transformations.add(new b_polynomial(_alpha, 0, _n));
        transformations.add(new WFG1_Final(_M, _k));
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
        for (int i = 0; i < _M - 1; i++) shapes.add(new Convex(i, _M));
        shapes.add(new Mixed(1.0d, 5));
        return shapes;
    }
}
