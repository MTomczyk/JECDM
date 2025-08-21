package visualization.updaters.sources;

import ea.EA;
import population.ISpecimenGetter;
import population.PopulationGetter;
import population.Specimen;
import statistics.IStatistic;
import updater.IDataSource;

import java.util.ArrayList;

/**
 * Implementation of {@link IDataSource}.
 * It creates new data that is defined as: [[generation, statistic1, statistic2,...statisticN]].
 * The statistics are derived from the i-th specimen performance values (parameter).
 * This implementation can be useful when generating convergence plots.
 *
 * @author MTomczyk
 */
public class GenerationAndStatistics extends AbstractEASource implements IDataSource
{

    /**
     * Statistic functions used.
     */
    private final IStatistic[] _statistics;

    /**
     * Performance value index;
     */
    private final int _index;

    /**
     * Parameterized constructor.
     *
     * @param ea         reference to the evolutionary algorithm
     * @param statistics statistic functions used
     * @param index      performance value index
     */
    public GenerationAndStatistics(EA ea, IStatistic[] statistics, int index)
    {
        this(ea, statistics, index, new PopulationGetter());
    }

    /**
     * Parameterized constructor.
     *
     * @param ea             reference to the evolutionary algorithm
     * @param statistics     statistic functions used
     * @param index          performance value index
     * @param specimenGetter object used to retrieve specimens for assessment
     */
    public GenerationAndStatistics(EA ea, IStatistic[] statistics, int index, ISpecimenGetter specimenGetter)
    {
        super(ea, specimenGetter, false);
        _statistics = statistics;
        _index = index;
    }

    /**
     * It creates new data that is defined as: [[generation, statistic1, statistic2,...statisticN]]. The statistics are
     * derived from the i-th specimen performance values (parameter). This implementation can be useful when generating
     * convergence plots.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        ArrayList<Specimen> P = getSpecimens();
        double[] sd = new double[P.size()];
        for (int i = 0; i < sd.length; i++) sd[i] = P.get(i).getEvaluations()[_index];
        double[] data = new double[1 + _statistics.length];
        data[0] = _ea.getCurrentGeneration();
        for (int j = 0; j < _statistics.length; j++)
            data[1 + j] = _statistics[j].calculate(sd);
        return new double[][]{data};
    }

}
