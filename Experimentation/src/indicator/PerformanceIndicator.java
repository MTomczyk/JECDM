package indicator;

import ea.IEA;
import exception.TrialException;
import indicator.initializers.IIndicatorInitializer;

/**
 * This indicator wraps {@link IPerformanceIndicator}.
 *
 * @author MTomczyk
 */
public class PerformanceIndicator extends AbstractIndicator implements IIndicator
{
    /**
     * Parameterized constructor.
     *
     * @param indicator performance indicator wrapped
     */
    public PerformanceIndicator(IPerformanceIndicator indicator)
    {
        this(indicator.toString(), indicator);
    }

    /**
     * Parameterized constructor.
     *
     * @param surpassedName surpasses the name of the original indicator
     * @param indicator     performance indicator wrapped
     */
    public PerformanceIndicator(String surpassedName, IPerformanceIndicator indicator)
    {
        this(surpassedName, indicator, (scenario, trialID) -> new PerformanceIndicator(indicator.getInstanceInInitialState()));
    }

    /**
     * Parameterized constructor.
     *
     * @param indicator   performance indicator wrapped
     * @param initializer object responsible for creating instances of an indicator (clones in initial state)
     */
    public PerformanceIndicator(IPerformanceIndicator indicator, IIndicatorInitializer initializer)
    {
        this(indicator.toString(), indicator, initializer);
    }

    /**
     * Parameterized constructor.
     *
     * @param surpassedName surpasses the name of the original indicator
     * @param indicator     performance indicator wrapped
     * @param initializer   object responsible for creating instances of an indicator (clones in initial state)
     */
    public PerformanceIndicator(String surpassedName, IPerformanceIndicator indicator, IIndicatorInitializer initializer)
    {
        super(surpassedName, indicator.isLessPreferred(), initializer);
        _indicator = indicator;
    }


    /**
     * Wrapped performance indicator.
     */
    private final IPerformanceIndicator _indicator;

    /**
     * The main method for performance evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(IEA ea) throws TrialException
    {
        try {
            return _indicator.evaluate(ea);
        } catch (Exception e) {
            throw new TrialException(e.getMessage(), this.getClass(), e, _scenario, _trialID);
        }
    }

}
