package t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common;

import population.ISpecimenGetter;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Returns a dummy/fixed population that is created using an input performance matrix (rows = specimens; columns = evaluations).
 * Multiple data sets can be specified, and they will be returned cyclically.
 *
 * @author MTomczyk
 */
public class DummyGetter implements ISpecimenGetter
{
    /**
     * Dummy specimens.
     */
    private final ArrayList<ArrayList<Specimen>> _dummySpecimens;

    /**
     * Current index.
     */
    private int _index = -1;

    /**
     * Parameterized constructor (uses one data set).
     *
     * @param performances performance matrix.
     */
    public DummyGetter(double[][] performances)
    {
        this(new double[][][]{performances});
    }

    /**
     * Parameterized constructor (uses multiple data sets).
     *
     * @param performances performance matrix (multiple data sets).
     */
    public DummyGetter(double[][][] performances)
    {
        _dummySpecimens = new ArrayList<>(performances.length);
        for (double[][] pm : performances)
        {
            ArrayList<Specimen> specimens = new ArrayList<>(pm.length);
            for (double[] v : pm) specimens.add(new Specimen(v));
            _dummySpecimens.add(specimens);
        }
    }

    /**
     * Retrieves the dummy specimens array.
     *
     * @param container container
     * @return specimen array
     */
    @Override
    public ArrayList<Specimen> getSpecimens(SpecimensContainer container)
    {
        _index++;
        if (_index == _dummySpecimens.size()) _index = 0;
        return _dummySpecimens.get(_index);
    }
}
