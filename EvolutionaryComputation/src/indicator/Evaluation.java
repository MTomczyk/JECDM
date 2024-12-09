package indicator;

import population.Specimen;
import statistics.IStatistic;

import java.util.ArrayList;

/**
 * This indicator calculates a statistic (e.g., min/mean/max) from the i-th specimens' evaluations.
 *
 * @author MTomczyk
 */
public class Evaluation extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Parameterized constructor Sets the index to 0 (the first evaluation in the vector is used and smaller values are preferred).
     *
     * @param statistic statistic function used
     */
    public Evaluation(IStatistic statistic)
    {
        this(statistic, 0, true);
    }

    /**
     * Parameterized constructor (smaller values are preferred).
     *
     * @param statistic statistic function used
     * @param index     index of the specimens' evaluations
     */
    public Evaluation(IStatistic statistic, int index)
    {
        this(statistic, index, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param statistic       statistic function used
     * @param index           index of the specimens' evaluations
     * @param lessIsPreferred field specifying the preference direction (if true, less is preferred; if true, more is preferred)
     */
    public Evaluation(IStatistic statistic, int index, boolean lessIsPreferred)
    {
        super(lessIsPreferred);
        _index = index;
        _statistic = statistic;
    }

    /**
     * Statistic function used.
     */
    private final IStatistic _statistic;


    /**
     * Index of the specimens' evaluations
     */
    private final int _index;


    /**
     * Auxiliary method for assessing the performance of EA based on its population.
     * The method is to be overwritten.
     *
     * @param population input population
     * @return performance value
     */
    @Override
    protected double evaluate(ArrayList<Specimen> population)
    {
        double[] e = new double[population.size()];
        for (int i = 0; i < population.size(); i++) e[i] = population.get(i).getEvaluations()[_index];
        return _statistic.calculate(e);
    }

    /**
     * For cloning: must be implemented to work properly with the experimentation module.
     * Deep copy must be created, but it should consider object's INITIAL STATE.
     *
     * @return cloned instance
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new Evaluation(_statistic, _index, _lessIsPreferred);
    }


    /**
     * The implementation must overwrite the toString() method.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _statistic.getName() + _index;
    }
}
