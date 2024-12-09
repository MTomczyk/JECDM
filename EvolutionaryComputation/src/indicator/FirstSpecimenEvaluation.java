package indicator;

import population.Specimen;

import java.util.ArrayList;

/**
 * This indicator returns the i-th performance value of the first specimen stored in the population.
 *
 * @author MTomczyk
 */
public class FirstSpecimenEvaluation extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Parameterized constructor (smaller values are preferred).
     *
     * @param index index of the specimens' evaluations
     */
    public FirstSpecimenEvaluation(int index)
    {
        this(index, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param index           index of the specimens' evaluations
     * @param lessIsPreferred field specifying the preference direction (if true, less is preferred; if true, more is preferred)
     */
    public FirstSpecimenEvaluation(int index, boolean lessIsPreferred)
    {
        super(lessIsPreferred);
        _index = index;
    }

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
        return population.get(0).getEvaluations()[_index];
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
        return new FirstSpecimenEvaluation(_index, _lessIsPreferred);
    }


    /**
     * The implementation must overwrite the toString() method.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "FSE" + _index;
    }
}
