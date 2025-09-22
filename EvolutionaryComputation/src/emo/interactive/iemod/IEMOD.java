package emo.interactive.iemod;

import criterion.Criteria;
import ea.AbstractInteractiveEA;
import ea.EA;
import ea.IEA;
import emo.interactive.StandardDSSBuilder;
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
import os.ObjectiveSpace;
import os.ObjectiveSpaceManager;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.MOOProblemBundle;
import random.IRandom;
import reproduction.DoubleReproduce;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;
import system.ds.DecisionSupportSystem;

/**
 * Provides means for creating an instance of IEMO/D. Important note: the population size parameter has no meaning as
 * the population size is explicitly determined from the number of optimization goals supplied. IMPORTANT NOTE ON THE
 * PARENTS SELECTION PROCESS: In contrast to other implementations, e.g., NSGA-II, this implementation does not assign
 * any auxiliary scores for constructed specimens. Thus, selection procedures relying on these scores are ineffective.
 * Nonetheless, IEMO/D was primarily designed to be coupled with a random selection procedure due to employing a
 * restricted mating pool.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class IEMOD extends AbstractInteractiveEA implements IEA
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
     * Getter for the goals manager.
     *
     * @return goals manager.
     */
    public MOEADGoalsManager getGoalsManager()
    {
        return _goalsManager;
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     * Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during the
     * updates).
     *
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
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
        ISelect select = new Random(2);
        return getIEMOD(0, true, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     * Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during the
     * updates).
     *
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getIEMOD(0, true, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     * Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during the
     * updates).
     *
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        ISelect select = new Random(2);
        return getIEMOD(0, true, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor,
                osAdjuster, dssAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     * Sets id to 0
     *
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(boolean updateOSDynamically,
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
        ISelect select = new Random(2);
        return getIEMOD(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
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
        ISelect select = new Random(2);
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (inconsistency
     * handler = remove oldest; refiner = default). The method is also coupled with the random selection of parents.
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
     * @param R                       the RGN
     * @param goals                   optimization goals used to steer the evolution; they also determine the population
     *                                size
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        ISelect select = new Random(2);
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor,
                osAdjuster, dssAdjuster);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        Criteria criteria,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor)
    {
        return getIEMOD(0, true, false, R, goals,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        Criteria criteria,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getIEMOD(0, true, false, R, goals,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        Criteria criteria,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getIEMOD(0, true, false, R, goals,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor)
    {
        return getIEMOD(0, true, false, R, goals, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getIEMOD(0, true, false, R, goals, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
     * @param R                       the RGN
     * @param goals                   initial optimization goals
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param similarity              object used to quantify similarity between two optimization goals
     * @param neighborhoodSize        neighborhood size for IEMO/D
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
                                                                        IGoal[] goals,
                                                                        AbstractMOOProblemBundle problem,
                                                                        ISelect select,
                                                                        DoubleConstruct.IConstruct construct,
                                                                        DoubleEvaluate.IEvaluate evaluate,
                                                                        DoubleReproduce.IReproduce reproduce,
                                                                        ISimilarity similarity,
                                                                        int neighborhoodSize,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getIEMOD(0, true, false, R, goals, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
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
        return getIEMOD(0, true, false, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getIEMOD(0, true, false, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update the OS
     * dynamically (uses utopia incumbent during the updates).
     *
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param <T>                     form of the internal value model used to represent preferences
     * @return IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IRandom R,
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getIEMOD(0, true, false, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
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
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, null);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, null, null);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster, null);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param bundleAdjuster          if provided, it is used to adjust the {@link IEMODBundle.Params} instance being
     *                                created by this method to instantiate the IEMO/D algorithm; adjustment is done
     *                                after the default initialization
     * @return the IEMO/D algorithm
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster,
                                                                        IEMODBundle.IParamsAdjuster<T> bundleAdjuster)
    {
        return getIEMOD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster, bundleAdjuster,
                null);
    }

    /**
     * Creates the IEMO/D algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor. (inconsistency
     * handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param updateOSDynamically     if true, the data on the known Pareto front bounds will be updated dynamically;
     *                                false: the data is assumed fixed (suitable normalization functions must be
     *                                provided when instantiating the EA); if fixed, the objective space manager will
     *                                not be instantiated by default, and the normalizations will be directly passed to
     *                                interested components
     * @param useNadirIncumbent       field is in effect only when the method is set to dynamically update its known
     *                                bounds of the objective space; if true, the {@link ObjectiveSpaceManager} used in
     *                                {@link ea.EA} is supposed to be configured so that the objective space is updated
     *                                based not only on the current population but the historical data as well (compare
     *                                with the incumbent to determine the worst value for each objective ever found)
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
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed.
     * @param bundleAdjuster          if provided, it is used to adjust the {@link IEMODBundle.Params} instance being
     *                                created by this method to instantiate the IEMO/D algorithm; adjustment is done
     *                                after the default initialization
     * @param eaParamsAdjuster        if provided, it is used to adjust the {@link EA.Params} instance being created by
     *                                this method to instantiate the IEMO/D algorithm; adjustment is done after the
     *                                default initialization
     * @return the IEMO/D algorithm
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
                                                                        IConstructor<T> modelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster,
                                                                        IEMODBundle.IParamsAdjuster<T> bundleAdjuster,
                                                                        EA.IParamsAdjuster eaParamsAdjuster)
    {
        StandardDSSBuilder<T> dssBuilder = new StandardDSSBuilder<>();
        dssBuilder.setReferenceSetConstructor(referenceSetConstructor);
        dssBuilder.setInteractionRule(interactionRule);
        dssBuilder.setDMFeedbackProvider(dmFeedbackProvider);
        dssBuilder.setModelConstructor(modelConstructor);
        dssBuilder.setPreferenceModel(preferenceModel);
        dssBuilder.setDSSParamsAdjuster(dssAdjuster);

        IEMODBuilder<T> iemodBuilder = new IEMODBuilder<>(R);
        iemodBuilder.setGoals(goals);
        iemodBuilder.setSimilarity(similarity);
        iemodBuilder.setNeighborhoodSize(neighborhoodSize);
        iemodBuilder.setAlloc(new Uniform());
        iemodBuilder.setCriteria(problem._criteria);

        if (updateOSDynamically)
        {
            iemodBuilder.setDynamicOSBoundsLearningPolicy();
            iemodBuilder.setOSMParamsAdjuster(osAdjuster);
            iemodBuilder.setUseNadirIncumbent(useNadirIncumbent);
        }
        else iemodBuilder.setFixedOSBoundsLearningPolicy(problem);

        iemodBuilder.setParentsSelector(select);
        iemodBuilder.setInitialPopulationConstructor(construct);
        iemodBuilder.setParentsReproducer(reproduce);
        iemodBuilder.setSpecimensEvaluator(evaluate);
        iemodBuilder.setName("IEMO/D");
        iemodBuilder.setID(id);
        iemodBuilder.setIEMODParamsAdjuster(bundleAdjuster);
        iemodBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        iemodBuilder.setStandardDSSBuilder(dssBuilder);
        return getIEMOD(iemodBuilder);
    }

    /**
     * Creates the IEMO/D algorithm using {@link IEMODBuilder}.
     *
     * @param iemodBuilder IEMO/D builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                     {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                     initialized as imposed by the specified  configuration; also note that the adjusters give
     *                     greater access to the data being instantiated and, thus, the validity of custom adjustments
     *                     is typically unchecked and may lead to errors
     * @param <T>          type of the internal model used
     * @return the IEMO/D algorithm
     */
    public static <T extends AbstractValueInternalModel> IEMOD getIEMOD(IEMODBuilder<T> iemodBuilder)
    {
        MOEADGoalsManager.Params pGM = new MOEADGoalsManager.Params(iemodBuilder.getGoals(),
                iemodBuilder.getSimilarity(), iemodBuilder.getNeighborhoodSize());
        pGM._alloc = new Uniform();
        MOEADGoalsManager manager = new MOEADGoalsManager(pGM);

        IEMODBundle.Params<T> pB = IEMODBundle.Params.getDefault(
                iemodBuilder.getCriteria(),
                manager,
                "DM",
                iemodBuilder.getDSSBuilder().getInteractionRule(),
                iemodBuilder.getDSSBuilder().getReferenceSetConstructor(),
                iemodBuilder.getDSSBuilder().getDMFeedbackProvider(),
                iemodBuilder.getDSSBuilder().getPreferenceModel(),
                iemodBuilder.getDSSBuilder().getModelConstructor(),
                iemodBuilder.getDSSBuilder().getDSSParamsAdjuster(),
                iemodBuilder.getReassignmentStrategy());
        pB._construct = iemodBuilder.getInitialPopulationConstructor();
        pB._reproduce = iemodBuilder.getParentsReproducer();
        pB._evaluate = iemodBuilder.getSpecimensEvaluator();
        pB._select = iemodBuilder.getParentsSelector();

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (iemodBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = iemodBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = iemodBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = iemodBuilder.shouldUseNadirIncumbent();
            if ((iemodBuilder.getUtopia() != null) && (iemodBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(iemodBuilder.getUtopia(), iemodBuilder.getNadir());
            if (iemodBuilder.getOSMParamsAdjuster() != null) iemodBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = iemodBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(iemodBuilder.getUtopia(), iemodBuilder.getNadir());
            manager.updateNormalizations(pB._initialNormalizations); // update normalizations
        }
        pB._name = "IEMO/D";

        if (iemodBuilder.getIEMODParamsAdjuster() != null) iemodBuilder.getIEMODParamsAdjuster().adjust(pB);
        IEMODBundle bundle = new IEMODBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(iemodBuilder.getCriteria(), bundle);
        pEA._populationSize = iemodBuilder.getGoals().length;
        pEA._offspringSize = 1; // Important: offspring size = 1
        pEA._expectedNumberOfSteadyStateRepeats = pEA._populationSize;
        pEA._R = iemodBuilder.getR();
        pEA._id = iemodBuilder.getID();
        if (iemodBuilder.getEAParamsAdjuster() != null) iemodBuilder.getEAParamsAdjuster().adjust(pEA);
        return new IEMOD(pEA, pB);
    }
}
