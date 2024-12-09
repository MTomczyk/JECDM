package emo.interactive.nemo.nemoii;

import ea.AbstractInteractiveEA;
import ea.EA;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.value.AbstractValueInternalModel;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import phase.PhasesBundle;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Tournament;
import system.ds.DecisionSupportSystem;

/**
 * Provides means for creating an instance of NEMO-II.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NEMOII extends AbstractInteractiveEA
{
    /**
     * Parameterized constructor (private).
     *
     * @param p   params container
     * @param dss instantiated decision support system
     */
    private NEMOII(EA.Params p, DecisionSupportSystem dss)
    {
        super(p, dss);
    }


    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with a random selection
     * (of size two).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int id,
                                                                          int populationSize,
                                                                          boolean updateOSDynamically,
                                                                          boolean useNadirIncumbent,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor)
    {
        Tournament.Params pT = new Tournament.Params();
        pT._size = 5;
        pT._preferenceDirection = false;
        pT._noOffspring = populationSize;
        pT._noParentsPerOffspring = 2;

        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Tournament(pT), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
    }


    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int id,
                                                                          int populationSize,
                                                                          boolean updateOSDynamically,
                                                                          boolean useNadirIncumbent,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          ISelect select,
                                                                          IConstruct construct,
                                                                          IEvaluate evaluate,
                                                                          IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor)
    {
        NEMOIIBundle.Params pB = NEMOIIBundle.Params.getDefault(problem._criteria,
                "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor);

        pB._construct = construct;
        pB._reproduce = reproduce;
        pB._evaluate = evaluate;
        pB._select = select;

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically) {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = problem._criteria;
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = true;
            pOS._updateNadirUsingIncumbent = useNadirIncumbent;
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else {
            // Set the initial normalizations (will be delivered to the object responsible for calculating crowding distances):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = null; // no os manager needed
        }

        pB._name = "NEMO-II";

        NEMOIIBundle bundle = new NEMOIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, bundle);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, bundle._phasesBundle);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._R = R;
        pEA._id = id;

        return new NEMOII(pEA, bundle.getDSS());
    }
}
