package visualization.updaters.sources;

import ea.EA;
import population.ISpecimenGetter;
import population.PopulationGetter;
import population.Specimen;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Implementation of {@link updater.IDataSource}.
 * It is a default implementation that creates new data that is defined as a 2D matrix:
 * [population members x solutions' evaluations].
 *
 * @author MTomczyk
 */
public class EASource extends AbstractEASource implements IDataSource
{

    /**
     * If true, the specimens' entries contributing to the final data (double [][]) are extended by two
     * additional doubles: [generation number, steady-state repeat].
     */
    private final boolean _addTimestamp;


    /**
     * Parameterized constructor.
     *
     * @param ea reference to the evolutionary algorithm.
     */
    public EASource(EA ea)
    {
        this(ea, new PopulationGetter(), false);
    }

    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the evolutionary algorithm.
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     */
    public EASource(EA ea, ISpecimenGetter specimenGetter)
    {
        this(ea, specimenGetter, false);
    }


    /**
     * Parameterized constructor.
     *
     * @param ea           reference to the evolutionary algorithm.
     * @param addTimeStamp if true, the specimens' entries contributing to the final data (double [][]) are extended by
     *                     two additional doubles: [current (EA) generation number, current (EA) steady-state repeat]
     */
    public EASource(EA ea, boolean addTimeStamp)
    {
        this(ea, new PopulationGetter(), addTimeStamp);
    }


    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the evolutionary algorithm.
     * @param specimenGetter allows retrieving specimen arrays (main input for data creation)
     * @param addTimeStamp   if true, the specimens' entries contributing to the final data (double [][]) are extended by
     *                       two additional doubles: [current (EA) generation number, current (EA) steady-state repeat]
     */
    public EASource(EA ea, ISpecimenGetter specimenGetter, boolean addTimeStamp)
    {
        super(ea, specimenGetter);
        _addTimestamp = addTimeStamp;
    }

    /**
     * Creates new data and returns it. The data is defined as a 2D matrix: [population members x solutions' evaluations].
     * Additionally, if the _addTimestamp flag is true, the specimens' entries contributing to the final data (double [][])
     * are extended will be extended by two additional doubles [generation number, steady-state repeat].
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> P = getDefaultSpecimens();
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
            }
            else data[i] = P.get(i).getEvaluations().clone();
        }
        return data;
    }

}
