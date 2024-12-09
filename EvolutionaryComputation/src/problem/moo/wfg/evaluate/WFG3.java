package problem.moo.wfg.evaluate;

import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.shapes.Linear;
import problem.moo.wfg.transformations.ITransformation;
import problem.moo.wfg.transformations.WFG2_Final;
import problem.moo.wfg.transformations.r_nonseparability;
import problem.moo.wfg.transformations.s_linear;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the WFG3 test problem.
 *
 * @author MTomczyk
 */


public class WFG3 extends WFGEvaluate
{
    /**
     * If false, the degeneracy is disabled.
     */
    private final boolean _degeneracy;

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     */
    public WFG3(int M, int k, int l)
    {
        this(M, k, l, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters (should be divisible by M - 1)
     * @param l the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @param degeneracy if false, the degeneracy is disabled
     */
    public WFG3(int M, int k, int l, boolean degeneracy)
    {
        super(M, k, l);
        assert l % 2 == 0;
        _degeneracy = degeneracy;
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
        transformations.add(new s_linear(0.35d, _k, _n));
        transformations.add(new r_nonseparability(2, _k, _k, _k + _l / 2));
        transformations.add(new WFG2_Final(_M, _k, _l));
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
        for (int i = 0; i < _M; i++) shapes.add(new Linear(i, _M));
        return shapes;
    }

    /**
     * Returns the A vector.
     *
     * @return the A vector.
     */
    @Override
    protected double[] getA()
    {
        double[] A = new double[_M - 1];

        if (_degeneracy) A[0] = 1.0d;
        else for (int i = 0; i < _M - 1; i++) A[i] = 1.0d;

        return A;
    }

}
