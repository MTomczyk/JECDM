package emo.interactive.iemod;

import ea.AbstractInteractiveEA;
import ea.EA;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.similarity.ISimilarity;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.value.AbstractValueInternalModel;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;

/**
 * Provides means for creating an instance of IEMO/D.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class IEMOD extends AbstractInteractiveEA
{
    /**
     * Reference to goals manager.
     */
    private final MOEADGoalsManager _goalsManager;

    /**
     * Parameterized constructor (private).
     *
     * @param p      params container
     * @param bundle IEMOD bundle
     */
    private IEMOD(EA.Params p, IEMODBundle.Params<?> bundle)
    {
        super(p, bundle._DSS);
        _goalsManager = bundle._goalsManager;
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with a random selection.
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param goals                   optimization goals
     * @param problem                 problem bundle
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(int id,
                                                                        boolean updateOSDynamically,
                                                                        boolean useNadirIncumbent,
                                                                        IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor)
    {
        // IMPORTANT: create just 1 offspring (1 offspring in one steady-state repeat)
        ISelect select = new Random(2, 1);
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default)
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(int id,
                                                                        boolean updateOSDynamically,
                                                                        boolean useNadirIncumbent,
                                                                        IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISelect select,
                                                                        IConstruct construct,
                                                                        IEvaluate evaluate,
                                                                        IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor)
    {
        MOEADGoalsManager.Params pGM = new MOEADGoalsManager.Params(goals, similarity, neighborhoodSize);
        pGM._alloc = new Uniform();
        MOEADGoalsManager manager = new MOEADGoalsManager(pGM);

        IEMODBundle.Params<T> pB = IEMODBundle.Params.getDefault(problem._criteria, manager, "DM",
                interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
        pB._construct = construct;
        pB._reproduce = reproduce;
        pB._evaluate = evaluate;
        pB._select = select;

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = problem._criteria;
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = true;
            pOS._updateNadirUsingIncumbent = useNadirIncumbent;
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations (will be delivered to the object responsible for calculating crowding distances):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = null; // no os manager needed
            manager.updateNormalizations(pB._initialNormalizations); // update normalizations
        }

        pB._name = "IEMO/D";

        IEMODBundle bundle = new IEMODBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, bundle);
        pEA._populationSize = goals.length;
        pEA._offspringSize = 1; // Important: offspring size = 1
        pEA._R = R;
        pEA._id = id;

        return new IEMOD(pEA, pB);
    }

    /**
     * Getter for the goals manager.
     *
     * @return goals manager.
     */
    public MOEADGoalsManager getGoalsManager()
    {
        return _goalsManager;
    }

}
