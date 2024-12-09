package problem.moo.wfg.evaluate;

import org.apache.commons.math4.legacy.stat.StatUtils;
import phase.IEvaluate;
import population.Specimen;
import print.PrintUtils;
import problem.moo.wfg.shapes.IShape;
import problem.moo.wfg.transformations.ITransformation;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Abstract class that supports evaluation of specimens as imposed by WFG benchmarks.
 *
 * @author MTomczyk
 */


public abstract class WFGEvaluate implements IEvaluate
{
    /**
     * Flag that aids debugging (if true).
     */
    protected boolean _debug = false;

    /**
     * Series of transformations.
     */
    protected LinkedList<ITransformation> _transformations;

    /**
     * Series of shapes.
     */
    protected ArrayList<IShape> _shapes;

    /**
     * Technical parameters #1 (bounds for decision variables).
     */
    protected double[] _Z_MAX;

    /**
     * Technical parameters #2.
     */
    protected double[] _A;

    /**
     * Technical parameters #3.
     */
    protected double[] _S;

    /**
     * Technical parameters #4.
     */
    protected double _D;

    /**
     * The number of position-related variables.
     */
    protected int _k;

    /**
     * The number of distance-related variables.
     */
    protected int _l;

    /**
     * The number of decision variables (position + distance).
     */
    protected int _n;

    /**
     * The number of objectives.
     */
    protected int _M;

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives M
     * @param k the number of position-related parameters
     * @param l the number of distance-related parameters
     */
    public WFGEvaluate(int M, int k, int l)
    {
        _M = M;
        _k = k;
        _l = l;
        _n = k + l;
    }

    /**
     * Should be called after constructing object instance to instantiate all WFG-related fields.
     */
    public void instantiateEvaluator()
    {
        _A = getA();
        _D = getD();
        _S = getS();
        _Z_MAX = getZMax();
        _transformations = getTransformations();
        _shapes = getShapes();

        assert _A.length == _M - 1;
        assert _S.length == _M;
        assert _Z_MAX.length == (_k + _l);
        assert _k % (_M - 1) == 0;
        assert _shapes.size() == _M;
    }

    /**
     * Evaluates specimens.
     *
     * @param specimens array of specimens to be evaluated
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens)
    {
        for (Specimen specimen : specimens)
        {
            double[] x = specimen.getDoubleDecisionVector();
            double[] nx = getX(x);
            double[] sx = new double[nx.length - 1];
            System.arraycopy(nx, 0, sx, 0, nx.length - 1);
            double[] f = new double[_M];
            for (int i = 0; i < _M; i++) f[i] = _D * nx[_M - 1] + _S[i] * _shapes.get(i).getShape(sx);
            specimen.setEvaluations(f);
        }
    }


    /**
     * Returns X variables
     *
     * @param e input decision vector.
     * @return X variables
     */
    private double[] getX(double[] e)
    {
        if (_debug)
        {
            System.out.println("Input : ");
            PrintUtils.printVectorOfDoubles(e, 4);
        }

        assert e.length == _n;
        double[] ce = e.clone();

        // normalizations
        {
            assert ce.length == _Z_MAX.length;
            for (int i = 0; i < e.length; i++) ce[i] /= _Z_MAX[i];
        }

        if (_debug)
        {
            System.out.println("Norms : ");
            PrintUtils.printVectorOfDoubles(ce, 4);
        }

        // transformations
        {
            double[] t = ce.clone();
            for (ITransformation T : _transformations)
            {
                t = T.applyTransformation(t.clone());
                if (_debug) PrintUtils.printVectorOfDoubles(t, 4);
            }
            assert t.length == _M;
            ce = t.clone();
        }

        // COMPUTE X
        return calculate_x(ce, _M, _A);
    }

    /**
     * Auxiliary method calculating X variables.
     *
     * @param vars input variables
     * @param M    the number of objectives
     * @param A    technical parameters #2
     * @return X variables
     */
    private double[] calculate_x(double[] vars, int M, double[] A)
    {
        assert A.length == M - 1;
        assert vars.length == M;
        double[] r = new double[vars.length];

        for (int i = 0; i < M; i++)
        {
            if (i != M - 1)
            {
                double[] tc = {vars[M - 1], A[i]};
                r[i] = 0.5d + (vars[i] - 0.5d) * StatUtils.max(tc);
            }
            else r[i] = vars[i];
        }
        return r;
    }


    /**
     * Returns the problem-related transformations.
     *
     * @return transformations
     */
    protected LinkedList<ITransformation> getTransformations()
    {
        return new LinkedList<>();
    }

    /**
     * Returns the problem-related shapes.
     *
     * @return shapes
     */
    public ArrayList<IShape> getShapes()
    {
        return new ArrayList<>();
    }

    /**
     * Returns the A vector.
     *
     * @return the A vector.
     */
    protected double[] getA()
    {
        double[] A = new double[_M - 1];
        for (int i = 0; i < _M - 1; i++) A[i] = 1.0d;
        return A;
    }

    /**
     * Returns the D value.
     *
     * @return the D value.
     */
    protected static double getD()
    {
        return 1.0d;
    }

    /**
     * Returns the S vector.
     *
     * @return the S vector.
     */
    protected double[] getS()
    {
        double[] S = new double[_M];
        for (int i = 0; i < _M; i++) S[i] = 2.0d * (i + 1);
        return S;
    }

    /**
     * Returns the Z vector.
     *
     * @return the Z vector.
     */
    protected double[] getZMax()
    {
        double[] Zmax = new double[_n];
        for (int i = 0; i < _n; i++) Zmax[i] = 2.0d * (i + 1);
        return Zmax;
    }


}
