package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.Convex;
import problem.moo.wfg.shapes.Disconnected;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG2Final;
import problem.moo.wfg.transformations.rNonseparability;
import problem.moo.wfg.transformations.sLinear;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG2 test problem.
 *
 * @author MTomczyk
 */


public class WFG2 extends WFGEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     */
    public WFG2(int M, int k, int l)
    {
        super(M, k, l);
        assert l % 2 == 0;
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
        transformations.add(new sLinear(0.35d, _k, _n));
        transformations.add(new rNonseparability(2, _k, _k, _k + _l / 2));
        transformations.add(new WFG2Final(_M, _k, _l));
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
        shapes.add(new Disconnected(5, 1.0d, 1.0d));
        return shapes;
    }
}
