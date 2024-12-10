package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.Concave;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG4Final;
import problem.moo.wfg.transformations.sDeceptive;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG5 test problem.
 *
 * @author MTomczyk
 */


public class WFG5 extends WFGEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)

     */
    public WFG5(int M, int k, int l)
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
    public LinkedList<ITransformation> getTransformations()
    {
        LinkedList<ITransformation> transformations = new LinkedList<>();
        transformations.add(new sDeceptive(0.35d, 0.001d, 0.05d, 0, _n));
        transformations.add(new WFG4Final(_M, _k));
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
