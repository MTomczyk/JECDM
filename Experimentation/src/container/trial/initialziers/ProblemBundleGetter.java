package container.trial.initialziers;

import problem.AbstractProblemBundle;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import problem.moo.wfg.WFGBundle;
import random.IRandom;

/**
 * Auxiliary method that aids in constructing a problem bundle.
 *
 * @author MTomczyk
 */


class ProblemBundleGetter
{
    /**
     * Auxiliary method that selects a proper problem bundle and returns it.
     *
     * @param problem    problem ID
     * @param objectives the number of objectives
     * @return problem bundle matching the problem ID and the number of objectives; returns null if no matching bundle can be found
     */
    protected static AbstractProblemBundle getProblemBundle(String problem, Integer objectives)
    {
        if (problem == null) return null;
        Problem pID = AbstractProblemBundle.getProblemFromString(problem);
        if (pID == null) return null;
        if (AbstractProblemBundle.isDTLZ(pID)) return getDTLZBundle(pID, objectives);
        else if (AbstractProblemBundle.isWFG(pID)) return getWFGBundle(pID, objectives);
        return null;
    }

    /**
     * Method for retrieving a DTLZ problem bundle.
     *
     * @param problem    problem id
     * @param objectives the number of objectives (should not be null)
     * @return DTLZ problem bundle
     */
    protected static DTLZBundle getDTLZBundle(Problem problem, Integer objectives)
    {
        if (objectives == null) return null;
        if (problem == null) return null;
        int l = DTLZBundle.getRecommendedNODistanceRelatedParameters(problem, objectives);
        return DTLZBundle.getBundle(problem, objectives, l);
    }


    /**
     * Method for retrieving a WFG problem bundle.
     *
     * @param problem    problem id
     * @param objectives the number of objectives (should not be null)
     * @return WFG problem bundle
     */
    protected static WFGBundle getWFGBundle(Problem problem, Integer objectives)
    {
        if (objectives == null) return null;
        if (problem == null) return null;
        int k = WFGBundle.getRecommendedNOPositionRelatedParameters(problem, objectives);
        int l = WFGBundle.getRecommendedNODistanceRelatedParameters(problem, objectives);
        return WFGBundle.getBundle(problem, objectives, k, l);
    }
}
