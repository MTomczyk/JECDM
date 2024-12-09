package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.Concave;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG4_Final;
import problem.moo.wfg.transformations.s_multi;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG4 test problem.
 *
 * @author MTomczyk
 */


public class WFG4 extends WFGEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)

     */
    public WFG4(int M, int k, int l)
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
        transformations.add(new s_multi(30, 10, 0.35, 0, _n));
        transformations.add(new WFG4_Final(_M, _k));
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
