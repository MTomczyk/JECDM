package emo.interactive;

import exception.EAException;
import system.ds.DecisionSupportSystem;

/**
 * Abstract builder-like class for constructing a {@link DecisionSupportSystem}.
 * Its extensions are supposed to assist in constructing preference-based methods.
 *
 * @author MTomczyk
 */
public class AbstractDSSBuilder
{
    /**
     * An auxiliary object (can be null) responsible for decision support system params container built when
     * instantiating the algorithm. It is assumed that the parameterization is done after the default parameterization
     * is completed.
     */
    private DecisionSupportSystem.IParamsAdjuster _dssParamsAdjuster = null;

    /**
     * Setter for an auxiliary object (can be null) responsible for decision support system params container built when
     * instantiating the algorithm. It is assumed that the parameterization is done after the default parameterization
     * is completed.
     *
     * @param dssParamsAdjuster decision support system params adjuster
     */
    public void setDSSParamsAdjuster(DecisionSupportSystem.IParamsAdjuster dssParamsAdjuster)
    {
        _dssParamsAdjuster = dssParamsAdjuster;
    }

    /**
     * Getter for an auxiliary object responsible for decision support system params container built when
     * instantiating the algorithm. It is assumed that the parameterization is done after the default parameterization
     * is completed.
     *
     * @return decision support system params adjuster
     */
    public DecisionSupportSystem.IParamsAdjuster getDSSParamsAdjuster()
    {
        return _dssParamsAdjuster;
    }

    /**
     * Auxiliary method for performing a simple data validation.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    protected void validate() throws EAException
    {

    }
}
