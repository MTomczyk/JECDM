package visualization.updaters.sources;

import ea.IEA;
import population.ISpecimenGetter;
import population.Specimen;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Provides common fields/functionalities for EA-related implementations of {@link updater.IDataSource}.
 *
 * @author MTomczyk
 */
public abstract class AbstractEASource extends AbstractSpecimensSource implements IDataSource
{
    /**
     * Reference to the associated EA.
     */
    protected final IEA _ea;

    /**
     * Allows retrieving specimen arrays (main input for data creation).
     */
    protected final ISpecimenGetter _specimenGetter;

    /**
     * If true, the specimens' entries contributing to the final data (double [][]) are extended by two additional
     * doubles: [generation number, steady-state repeat].
     */
    protected final boolean _addTimestamp;

    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the associated EA
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     * @param addTimestamp   if true, the specimens' entries contributing to the final data (double [][]) are extended
     *                       by two additional * doubles: [generation number, steady-state repeat]
     */
    protected AbstractEASource(IEA ea, ISpecimenGetter specimenGetter, boolean addTimestamp)
    {
        super(null);
        _addTimestamp = addTimestamp;
        _ea = ea;
        _specimenGetter = specimenGetter;
    }

    /**
     * Returns a specimen array obtained by using the specimen getter implementation.
     *
     * @return specimen array (main input for data creation)
     */
    @Override
    protected ArrayList<Specimen> getSpecimens()
    {
        return _specimenGetter.getSpecimens(_ea.getSpecimensContainer());
    }

    /**
     * Creates new data and returns it. The data is defined as a 2D matrix: [population members x solutions'
     * evaluations] (the entries are clones from specimens' evaluations; i.e., new objects are created). Additionally,
     * if the _addTimestamp flag is true, the specimens' entries contributing to the final data (double [][]) are
     * extended will be extended by two additional doubles [generation number, steady-state repeat].
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> P = getSpecimens();
        double[][] data = new double[P.size()][];
        for (int i = 0; i < P.size(); i++)
        {
            if (_addTimestamp)
            {
                double[] e = P.get(i).getEvaluations(); // do not clone, use array copy below
                data[i] = new double[e.length + 2];
                System.arraycopy(P.get(i).getEvaluations(), 0, data[i], 0, e.length);
                data[i][e.length] = _ea.getCurrentGeneration();
                data[i][e.length + 1] = _ea.getCurrentSteadyStateRepeat();
            } else data[i] = P.get(i).getEvaluations().clone();
        }
        return data;
    }
}
