package emo.aposteriori;

import criterion.Criteria;
import ea.EA;
import emo.aposteriori.moead.MOEADBundle;
import emo.aposteriori.nsga.NSGABundle;
import emo.aposteriori.nsgaii.NSGAIIBundle;
import emo.aposteriori.nsgaiii.NSGAIIIBundle;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import emo.utils.decomposition.similarity.ISimilarity;
import os.ObjectiveSpaceManager;
import phase.PhasesBundle;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import selection.Random;
import selection.Tournament;
import space.distance.Euclidean;

/**
 * Utils class that provides a common code for all test cases.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Creates MOEAD instance.
     *
     * @param criteria               considered criteria
     * @param problemBundle          problem bundle
     * @param dynamicObjectiveRanges true = dynamic objective ranges mode is on
     * @param R                      random number generator
     * @param goals                  optimization goals used
     * @param similarity             similarity function used
     * @return NSGA instance
     */
    public static EA getMOEAD(Criteria criteria,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveRanges,
                              IRandom R,
                              IGoal[] goals,
                              ISimilarity similarity)
    {
        return getMOEAD(0, criteria, problemBundle, dynamicObjectiveRanges, R, goals, similarity);
    }

    /**
     * Creates MOEAD instance.
     *
     * @param id                    EA's id
     * @param criteria              considered criteria
     * @param problemBundle         problem bundle
     * @param dynamicObjectiveSpace true = dynamic objective ranges mode is on
     * @param R                     random number generator
     * @param goals                 optimization goals used
     * @param similarity            similarity function used
     * @return NSGA instance
     */
    public static EA getMOEAD(int id,
                              Criteria criteria,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveSpace,
                              IRandom R,
                              IGoal[] goals,
                              ISimilarity similarity)
    {
        MOEADGoalsManager.Params pGM = new MOEADGoalsManager.Params(goals, similarity, 10);
        pGM._alloc = new Uniform();
        MOEADGoalsManager GM = new MOEADGoalsManager(pGM);

        MOEADBundle.Params pAB = new MOEADBundle.Params(criteria, GM);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;
        pAB._select = new Random(2, 1);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, false, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        MOEADBundle algorithmBundle = new MOEADBundle(pAB);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = goals.length;
        pEA._offspringSize = 1;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);

    }


    /**
     * Creates NSGA-II instance.
     *
     * @param criteria               considered criteria
     * @param problemBundle          problem bundle
     * @param dynamicObjectiveRanges true = dynamic objective ranges mode is on
     * @param R                      random number generator
     * @param populationSize         population size
     * @param offspringSize          offspring size
     * @return NSGA instance
     */
    public static EA getNSGAII(Criteria criteria,
                               AbstractMOOProblemBundle problemBundle,
                               boolean dynamicObjectiveRanges,
                               IRandom R,
                               int populationSize,
                               int offspringSize)
    {
        return getNSGAII(0, criteria, problemBundle, dynamicObjectiveRanges, R, populationSize, offspringSize);
    }

    /**
     * Creates NSGA-II instance.
     *
     * @param id                    EA's id
     * @param criteria              considered criteria
     * @param problemBundle         problem bundle
     * @param dynamicObjectiveSpace true = dynamic objective ranges mode is on
     * @param R                     random number generator
     * @param populationSize        population size
     * @param offspringSize         offspring size
     * @return NSGA instance
     */
    public static EA getNSGAII(int id,
                               Criteria criteria,
                               AbstractMOOProblemBundle problemBundle,
                               boolean dynamicObjectiveSpace,
                               IRandom R,
                               int populationSize,
                               int offspringSize)
    {
        // create algorithm bundle
        NSGAIIBundle.Params pAB = new NSGAIIBundle.Params(criteria);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;

        pAB._select = new Tournament(2, offspringSize);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true,false, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        NSGAIIBundle algorithmBundle = new NSGAIIBundle(pAB);

        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = offspringSize;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);
    }

    /**
     * Creates NSGA-III instance.
     *
     * @param criteria               considered criteria
     * @param problemBundle          problem bundle
     * @param dynamicObjectiveRanges true = dynamic objective ranges mode is on
     * @param R                      random number generator
     * @param goals                  optimization goals
     * @return NSGA instance
     */
    public static EA getNSGAIII(Criteria criteria,
                                AbstractMOOProblemBundle problemBundle,
                                boolean dynamicObjectiveRanges,
                                IRandom R,
                                IGoal[] goals)
    {
        return getNSGAIII(0, criteria, problemBundle, dynamicObjectiveRanges, R, goals);
    }

    /**
     * Creates NSGA-III instance.
     *
     * @param id                    EA's id
     * @param criteria              considered criteria
     * @param problemBundle         problem bundle
     * @param dynamicObjectiveSpace true = dynamic objective ranges mode is on
     * @param R                     random number generator
     * @param goals                 optimization goals
     * @return NSGA instance
     */
    public static EA getNSGAIII(int id, Criteria criteria,
                                AbstractMOOProblemBundle problemBundle,
                                boolean dynamicObjectiveSpace,
                                IRandom R,
                                IGoal[] goals)
    {
        NSGAIIIGoalsManager goalsManager = new NSGAIIIGoalsManager(new NSGAIIIGoalsManager.Params(goals));
        NSGAIIIBundle.Params pAB = new NSGAIIIBundle.Params(criteria, goalsManager, new RandomAssignment(), new RandomSpecimen());
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, false, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        pAB._select = new Random(2, goals.length);

        NSGAIIIBundle algorithmBundle = new NSGAIIIBundle(pAB);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = goals.length;
        pEA._offspringSize = goals.length;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);
    }

    /**
     * Creates NSGA instance.
     *
     * @param criteria               considered criteria
     * @param problemBundle          problem bundle
     * @param dynamicObjectiveRanges true = dynamic objective ranges mode is on
     * @param R                      random number generator
     * @param populationSize         population size
     * @param offspringSize          offspring size
     * @param th                     threshold for the niching procedure
     * @return NSGA instance
     */
    public static EA getNSGA(Criteria criteria,
                             AbstractMOOProblemBundle problemBundle,
                             boolean dynamicObjectiveRanges,
                             IRandom R,
                             int populationSize,
                             int offspringSize,
                             double th)
    {
        return getNSGA(0, criteria, problemBundle, dynamicObjectiveRanges, R, populationSize, offspringSize, th);
    }

    /**
     * Creates NSGA instance.
     *
     * @param id                    EA's id
     * @param criteria              considered criteria
     * @param problemBundle         problem bundle
     * @param dynamicObjectiveSpace true = dynamic objective ranges mode is on
     * @param R                     random number generator
     * @param populationSize        population size
     * @param offspringSize         offspring size
     * @param th                    threshold for the niching procedure
     * @return NSGA instance
     */
    public static EA getNSGA(int id,
                             Criteria criteria,
                             AbstractMOOProblemBundle problemBundle,
                             boolean dynamicObjectiveSpace,
                             IRandom R,
                             int populationSize,
                             int offspringSize,
                             double th)
    {
        // create algorithm bundle
        NSGABundle.Params pAB = new NSGABundle.Params(criteria);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;
        pAB._distance = new Euclidean();
        pAB._th = th;

        pAB._select = new Tournament(2, offspringSize);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, false, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        NSGABundle algorithmBundle = new NSGABundle(pAB);

        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = offspringSize;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);
    }

}
