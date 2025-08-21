package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import ea.EA;
import population.PopulationGetter;
import population.Specimen;
import updater.IDataSource;
import visualization.updaters.sources.AbstractEASource;

import java.util.ArrayList;

/**
 * Dedicated data source. Creates data point in the following form: [specimen first decision variable (x-coordinate);
 * specimen performance value; specimen second decision variable (y-coordinate)].
 *
 * @author MTomczyk
 */
public class ContEASource extends AbstractEASource implements IDataSource
{
    /**
     * If true, the generation number will be added as the fourth attribute.
     */
    private final boolean _addGeneration;

    /**
     * If true (and the ``add generation'' flag is true), the specimen individual timestamp will be used instead of the
     * current timestamp's generation number.
     */
    private final boolean _generationWhenConstructed;

    /**
     * Parameterized constructor.
     *
     * @param ea                        reference to the associated EA
     * @param addGeneration             if true, the generation number will be added as the fourth attribute
     * @param generationWhenConstructed if true (and the ``add generation'' flag is true), the specimen individual
     *                                  timestamp will be used instead of the current timestamp's generation number.
     */
    public ContEASource(EA ea, boolean addGeneration, boolean generationWhenConstructed)
    {
        super(ea, new PopulationGetter(), false);
        _addGeneration = addGeneration;
        _generationWhenConstructed = generationWhenConstructed;
    }

    /**
     * Creates data point in the following form: [specimen first decision variable (x-coordinate); specimen performance
     * value; specimen second decision variable (y-coordinate).
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> specimens = getSpecimens();
        double[][] data;
        if (_addGeneration) data = new double[specimens.size()][4];
        else data = new double[specimens.size()][3];

        Specimen s;
        for (int i = 0; i < specimens.size(); i++)
        {
            s = specimens.get(i);
            data[i][0] = s.getDoubleDecisionVector()[0];
            data[i][1] = s.getEvaluations()[0];
            data[i][2] = s.getDoubleDecisionVector()[1];
            if (_addGeneration)
            {
                if (_generationWhenConstructed) data[i][3] = s.getID()._generation; // generation when constructed
                else data[i][3] = _ea.getCurrentGeneration(); // current timestamp's generation no.
            }
        }
        return data;
    }
}
