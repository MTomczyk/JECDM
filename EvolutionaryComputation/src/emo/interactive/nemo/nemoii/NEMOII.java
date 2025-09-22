package emo.interactive.nemo.nemoii;

import criterion.Criteria;
import ea.AbstractInteractiveEA;
import ea.EA;
import ea.IEA;
import emo.interactive.StandardDSSBuilder;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.value.AbstractValueInternalModel;
import os.ObjectiveSpace;
import os.ObjectiveSpaceManager;
import phase.*;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.MOOProblemBundle;
import random.IRandom;
import reproduction.DoubleReproduce;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;
import system.ds.DecisionSupportSystem;

/**
 * Provides means for creating an instance of NEMO-II.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NEMOII extends AbstractInteractiveEA implements IEA
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
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor,
                osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
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
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          Criteria criteria,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor)
    {
        return getNEMOII(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          Criteria criteria,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param criteria                criteria
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          Criteria criteria,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
                                                                          IRandom R,
                                                                          AbstractMOOProblemBundle problem,
                                                                          ISelect select,
                                                                          DoubleConstruct.IConstruct construct,
                                                                          DoubleEvaluate.IEvaluate evaluate,
                                                                          DoubleReproduce.IReproduce reproduce,
                                                                          IRule interactionRule,
                                                                          IReferenceSetConstructor referenceSetConstructor,
                                                                          IDMFeedbackProvider dmFeedbackProvider,
                                                                          IPreferenceModel<T> preferenceModel,
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
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
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster);
    }


    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param <T>                     form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(int populationSize,
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
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
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, modelConstructor, null);
    }

    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct,
                evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                modelConstructor, osAdjuster, null);
    }


    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMOII(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct,
                evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                modelConstructor, osAdjuster, dssAdjuster, null, null);
    }


    /**
     * Creates the NEMO-II algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                      algorithm id
     * @param populationSize          population size
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
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed))
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param modelConstructor        model constructor (the number of goals it constructs should be greater/equal to
     *                                the number of initial goals
     * @param preferenceModel         definition of the preference model
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used)
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param bundleAdjuster          if provided, it is used to adjust the {@link NEMOIIBundle.Params} instance being
     *                                created by this method to instantiate the NEMO-II algorithm; adjustment is done
     *                                after the default initialization
     * @param eaParamsAdjuster        if provided, it is used to adjust the {@link EA.Params} instance being created by
     *                                this method to instantiate the NEMO-II algorithm; adjustment is done after the
     *                                default initialization
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
                                                                          IConstructor<T> modelConstructor,
                                                                          ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                          DecisionSupportSystem.IParamsAdjuster dssAdjuster,
                                                                          NEMOIIBundle.IParamsAdjuster bundleAdjuster,
                                                                          EA.IParamsAdjuster eaParamsAdjuster)
    {
        NEMOIIBuilder<T> nemoiiBuilder = new NEMOIIBuilder<>(R);
        nemoiiBuilder.setCriteria(problem._criteria);
        nemoiiBuilder.setStandardDSSBuilder(new StandardDSSBuilder<>());
        nemoiiBuilder.getDSSBuilder().setInteractionRule(interactionRule);
        nemoiiBuilder.getDSSBuilder().setReferenceSetConstructor(referenceSetConstructor);
        nemoiiBuilder.getDSSBuilder().setDMFeedbackProvider(dmFeedbackProvider);
        nemoiiBuilder.getDSSBuilder().setPreferenceModel(preferenceModel);
        nemoiiBuilder.getDSSBuilder().setModelConstructor(modelConstructor);
        nemoiiBuilder.getDSSBuilder().setDSSParamsAdjuster(dssAdjuster);
        nemoiiBuilder.setInitialPopulationConstructor(construct);
        nemoiiBuilder.setParentsReproducer(reproduce);
        nemoiiBuilder.setSpecimensEvaluator(evaluate);
        nemoiiBuilder.setParentsSelector(select);

        if (updateOSDynamically)
        {
            nemoiiBuilder.setDynamicOSBoundsLearningPolicy();
            nemoiiBuilder.setOSMParamsAdjuster(osAdjuster);
            nemoiiBuilder.setUseNadirIncumbent(useNadirIncumbent);
            nemoiiBuilder.setUseUtopiaIncumbent(true);
        }
        else nemoiiBuilder.setFixedOSBoundsLearningPolicy(problem);

        nemoiiBuilder.setName("NEMO-II");
        nemoiiBuilder.setPopulationSize(populationSize);
        nemoiiBuilder.setID(id);
        nemoiiBuilder.setNEMOIIParamsAdjuster(bundleAdjuster);
        nemoiiBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getNEMOII(nemoiiBuilder);
    }


    /**
     * Creates the NEMO-II algorithm using {@link NEMOIIBuilder}.
     *
     * @param nemoiiBuilder NEMO-II builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                      {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects
     *                      are initialized as imposed by the specified  configuration; also note that the adjusters
     *                      give greater access to the data being instantiated and, thus, the validity of custom
     *                      adjustments is typically unchecked and may lead to errors
     * @param <T>           form of the internal value model used to represent preferences
     * @return NEMO-II algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMOII getNEMOII(NEMOIIBuilder<T> nemoiiBuilder)
    {
        NEMOIIBundle.Params pB = NEMOIIBundle.Params.getDefault(nemoiiBuilder.getCriteria(),
                "DM",
                nemoiiBuilder.getDSSBuilder().getInteractionRule(),
                nemoiiBuilder.getDSSBuilder().getReferenceSetConstructor(),
                nemoiiBuilder.getDSSBuilder().getDMFeedbackProvider(),
                nemoiiBuilder.getDSSBuilder().getPreferenceModel(),
                nemoiiBuilder.getDSSBuilder().getModelConstructor(),
                nemoiiBuilder.getDSSBuilder().getDSSParamsAdjuster());

        pB._construct = nemoiiBuilder.getInitialPopulationConstructor();
        pB._reproduce = nemoiiBuilder.getParentsReproducer();
        pB._evaluate = nemoiiBuilder.getSpecimensEvaluator();
        pB._select = nemoiiBuilder.getParentsSelector();

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (nemoiiBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = nemoiiBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = nemoiiBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = nemoiiBuilder.shouldUseNadirIncumbent();
            if ((nemoiiBuilder.getUtopia() != null) && (nemoiiBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(nemoiiBuilder.getUtopia(), nemoiiBuilder.getNadir());
            if (nemoiiBuilder.getOSMParamsAdjuster() != null) nemoiiBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = nemoiiBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(nemoiiBuilder.getUtopia(), nemoiiBuilder.getNadir());
        }

        pB._name = "NEMO-II";
        if (nemoiiBuilder.getNEMOIIParamsAdjuster() != null) nemoiiBuilder.getNEMOIIParamsAdjuster().adjust(pB);
        NEMOIIBundle bundle = new NEMOIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(nemoiiBuilder.getCriteria(), bundle);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(bundle._phasesBundle);
        pEA._populationSize = nemoiiBuilder.getPopulationSize();
        pEA._offspringSize = nemoiiBuilder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = nemoiiBuilder.getR();
        pEA._id = nemoiiBuilder.getID();
        if (nemoiiBuilder.getEAParamsAdjuster() != null) nemoiiBuilder.getEAParamsAdjuster().adjust(pEA);
        return new NEMOII(pEA, bundle.getDSS());
    }
}
