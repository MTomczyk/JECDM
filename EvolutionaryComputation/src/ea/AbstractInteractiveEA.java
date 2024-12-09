package ea;

import system.ds.DecisionSupportSystem;

/**
 * Abstract extension of {@link EA} that provides access to DSS-related functionalities
 * {@link system.ds.DecisionSupportSystem}.
 *
 * @author MTomczyk
 */
public abstract class AbstractInteractiveEA extends EA
{
    /**
     * Decision support system.
     */
    private final DecisionSupportSystem _dss;

    /**
     * Parameterized constructor.
     *
     * @param p   params container
     * @param dss instantiated decision support system
     */
    protected AbstractInteractiveEA(Params p, DecisionSupportSystem dss)
    {
        super(p);
        _dss = dss;
    }

    /**
     * Getter for the decision support system (returns null if a method does not employ any).
     *
     * @return decision support system (null, if not employed)
     */
    @Override
    public DecisionSupportSystem getDecisionSupportSystem()
    {
        return _dss;
    }
}
