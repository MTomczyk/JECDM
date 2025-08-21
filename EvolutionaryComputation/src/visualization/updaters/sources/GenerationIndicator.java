package visualization.updaters.sources;

import ea.EA;
import indicator.IPerformanceIndicator;
import updater.IDataSource;

/**
 * Implementation of {@link IDataSource}.
 * It is an indicator-based implementation that creates new data that is defined as:
 * [[generation, indicator value]]. The indicator is used to assess the population.
 * This implementation can be useful when generating convergence plots.
 *
 * @author MTomczyk
 */
public class GenerationIndicator extends AbstractEASource implements IDataSource
{
    /**
     * Indicator used to assess the population.
     */
    private final IPerformanceIndicator _indicator;

    /**
     * Parameterized constructor.
     *
     * @param ea reference to the evolutionary algorithm
     * @param indicator indicator used to assess the population
     */
    public GenerationIndicator(EA ea, IPerformanceIndicator indicator)
    {
        super(ea, null, false);
        _indicator = indicator;
    }

    /**
     * Creates new data and returns it. The data is defined as: [[generation, indicator value]]. The indicator is used
     * to comprehensively assess the population. This implementation can be useful when generating convergence plots.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        double performance = _indicator.evaluate(_ea);
        return new double[][]{{_ea.getCurrentGeneration(), performance}};
    }

}
