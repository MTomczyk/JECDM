package indicator;

import ea.EA;
import exception.TrialException;
import indicator.initializers.IIndicatorInitializer;
import population.Specimen;
import scenario.Scenario;

import java.util.ArrayList;

/**
 * Default, abstract implementation of {@link IIndicator}.
 * Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractIndicator implements IIndicator
{
    /**
     * Indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     */
    protected final String _name;

    /**
     * Scenario being currently processed.
     */
    protected Scenario _scenario;

    /**
     * ID of a trial being currently processed.
     */
    protected int _trialID;

    /**
     * Field specifying the preference direction (if true, less is preferred; if true, more is preferred).
     */
    protected final boolean _lessIsPreferred;

    /**
     * Object responsible for creating instances of an indicator (clones in initial state).
     */
    protected final IIndicatorInitializer _initializer;

    /**
     * Parameterized constructor.
     *
     * @param name            Indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     * @param lessIsPreferred field specifying the preference direction (if true, less is preferred; if true, more is preferred)
     * @param initializer     object responsible for creating instances of an indicator (clones in initial state)
     */
    public AbstractIndicator(String name, boolean lessIsPreferred, IIndicatorInitializer initializer)
    {
        _name = name;
        _lessIsPreferred = lessIsPreferred;
        _initializer = initializer;
    }

    /**
     * Each indicator must implement this factory-like method. After establishing the indicator at the scenario level,
     * each dispatched trial executor receives a copy. This allows each instance to maintain its own trial-dependent data.
     * Effectively, this method intends to clone the reference object in its initial state.
     *
     * @param scenario scenario being currently processed
     * @param trialID  ID of a trial being processed
     * @return new instance (clear, unprocessed) of the indicator
     * @throws TrialException trial-level exception can be thrown 
     */
    @Override
    public IIndicator getInstance(Scenario scenario, int trialID) throws TrialException
    {
        try {
            return _initializer.getInstance(scenario, trialID);
        } catch (Exception e) {
            throw new TrialException(e.getMessage(), this.getClass(), e, _scenario, _trialID);
        }
    }

    /**
     * Returns the indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     *
     * @return the indicator's name
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Returns the indicator's name. Note that the name should obey the same naming rules as the titles of the scenarios' keys and values.
     *
     * @return the indicator's name
     */
    @Override
    public String toString()
    {
        return _name;
    }

    /**
     * Method for identifying preference direction.
     *
     * @return preference direction (if true, less is preferred; if true, more is preferred)
     */
    public boolean isLessBetter()
    {
        return _lessIsPreferred;
    }

    /**
     * The main method for performance evaluation.
     * This default implementation gets the population form the EA's specimen container and delegates the evaluation
     * to a supportive method.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(EA ea) throws TrialException
    {
        return evaluate(ea.getSpecimensContainer().getPopulation());
    }


    /**
     * An auxiliary method for the performance evaluation.
     *
     * @param specimens the current population
     * @return performance evaluation
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    protected double evaluate(ArrayList<Specimen> specimens) throws TrialException
    {
        return 0.0d;
    }

    /**
     * The method for clearing the data.
     */
    @Override
    public void dispose()
    {

    }
}
