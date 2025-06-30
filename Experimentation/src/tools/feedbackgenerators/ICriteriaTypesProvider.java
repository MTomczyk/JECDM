package tools.feedbackgenerators;

import scenario.Scenario;

/**
 * Auxiliary interface for objects responsible for returning data on criteria types (gain/cost) given
 * a scenario being processed.
 *
 * @author MTomczyk
 */
public interface ICriteriaTypesProvider
{
    /**
     * The main method.
     *
     * @param scenario scenario being processed
     * @return criteria types (one per each criterion/dimension;
     * true, if a criterion is of a gain type, i.e., to be maximized; false, if otherwise).
     */
    boolean[] getTypes(Scenario scenario);
}
