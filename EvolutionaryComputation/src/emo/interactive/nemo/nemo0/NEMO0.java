package emo.interactive.nemo.nemo0;

import criterion.Criteria;
import ea.AbstractInteractiveEA;
import ea.EA;
import ea.IEA;
import emo.interactive.StandardDSSBuilder;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.value.rs.representative.RepresentativeModel;
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
 * Provides means for creating an instance of NEMO-0.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NEMO0 extends AbstractInteractiveEA implements IEA
{
    /**
     * Parameterized constructor (private).
     *
     * @param p   params container
     * @param dss instantiated decision support system
     */
    private NEMO0(EA.Params p, DecisionSupportSystem dss)
    {
        super(p, dss);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                       reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                       reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object responsible for customizing objective space manager params
     *                                       container built when is set to updateOSDynamically (can be null; not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia
     * incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                       reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                       constructor, evaluator, and reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
                                                                        int populationSize,
                                                                        boolean updateOSDynamically,
                                                                        boolean useNadirIncumbent,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, representativeModelConstructor);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                       constructor, evaluator, and reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
                                                                        int populationSize,
                                                                        boolean updateOSDynamically,
                                                                        boolean useNadirIncumbent,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random
     * selection of parents
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed), specimen
     *                                       constructor, evaluator, and reproducer)
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
                                                                        int populationSize,
                                                                        boolean updateOSDynamically,
                                                                        boolean useNadirIncumbent,
                                                                        IRandom R,
                                                                        AbstractMOOProblemBundle problem,
                                                                        IRule interactionRule,
                                                                        IReferenceSetConstructor referenceSetConstructor,
                                                                        IDMFeedbackProvider dmFeedbackProvider,
                                                                        IPreferenceModel<T> preferenceModel,
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param criteria                       criteria
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param criteria                       criteria
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param criteria                       criteria
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to
     * update the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize                 population size
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria)
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int populationSize,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(0, populationSize, true, false, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                preferenceModel, representativeModelConstructor, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed))
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
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
                                                                        RepresentativeModel<T> representativeModelConstructor)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct, evaluate,
                reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, null);
    }


    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed))
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor, osAdjuster, null);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed))
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    @Deprecated
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
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
                                                                        RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getNEMO0(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct,
                evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                representativeModelConstructor.getFutureInstance(),
                osAdjuster, dssAdjuster, null, null);
    }

    /**
     * Creates the NEMO-0 algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative
     * model; inconsistency handler = remove oldest; refiner = default).
     *
     * @param id                             algorithm id
     * @param populationSize                 population size
     * @param updateOSDynamically            if true, the data on the known Pareto front bounds will be updated
     *                                       dynamically; false: the data is assumed fixed (suitable normalization
     *                                       functions must be provided when instantiating the EA); if fixed, the
     *                                       objective space manager will not be instantiated by default, and the
     *                                       normalizations will be directly passed to interested components
     * @param useNadirIncumbent              field is in effect only when the method is set to dynamically update its
     *                                       known bounds of the objective space; if true, the
     *                                       {@link ObjectiveSpaceManager} used in {@link ea.EA} is supposed to be
     *                                       configured so that the objective space is updated based not only on the
     *                                       current population but the historical data as well (compare with the
     *                                       incumbent to determine the worst value for each objective ever found)
     * @param R                              the RGN
     * @param problem                        problem bundle (provides criteria, normalizations (when fixed))
     * @param select                         parents selector
     * @param construct                      specimens constructor
     * @param evaluate                       specimens evaluator
     * @param reproduce                      specimens reproducer
     * @param interactionRule                interaction rule
     * @param referenceSetConstructor        reference set constructor
     * @param dmFeedbackProvider             artificial decision maker (feedback provider)
     * @param representativeModelConstructor representative model constructor
     * @param preferenceModel                definition of the preference model
     * @param osAdjuster                     auxiliary object (can be null) responsible for customizing objective space
     *                                       manager params container built when the method is expected to update its
     *                                       known bounds on the objective space dynamically (otherwise, it is possible
     *                                       that the manager will be null; the adjuster is not used)
     * @param dssAdjuster                    aan auxiliary object (can be null) responsible for decision support system
     *                                       params container built when instantiating the algorithm; it is assumed that
     *                                       the parameterization is done after the default parameterisation is
     *                                       completed
     * @param bundleAdjuster                 if provided, it is used to adjust the {@link NEMO0Bundle.Params} instance
     *                                       being created by this method to instantiate the IEMO/D algorithm;
     *                                       adjustment is  done  after the default initialization
     * @param eaParamsAdjuster               if provided, it is used to adjust the {@link EA.Params} instance being
     *                                       created by this method to instantiate the NEMO-0 algorithm; adjustment is
     *                                       done after the default initialization
     * @param <T>                            form of the internal value model used to represent preferences
     * @return NEMO-0 algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(int id,
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
                                                                        model.constructor.value.representative.RepresentativeModel<T> representativeModelConstructor,
                                                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                                                        DecisionSupportSystem.IParamsAdjuster dssAdjuster,
                                                                        NEMO0Bundle.IParamsAdjuster bundleAdjuster,
                                                                        EA.IParamsAdjuster eaParamsAdjuster)
    {
        NEMO0Builder<T> nemo0Builder = new NEMO0Builder<>(R);
        nemo0Builder.setStandardDSSBuilder(new StandardDSSBuilder<>());
        nemo0Builder.getDSSBuilder().setInteractionRule(interactionRule);
        nemo0Builder.getDSSBuilder().setReferenceSetConstructor(referenceSetConstructor);
        nemo0Builder.getDSSBuilder().setDMFeedbackProvider(dmFeedbackProvider);
        nemo0Builder.getDSSBuilder().setPreferenceModel(preferenceModel);
        nemo0Builder.getDSSBuilder().setModelConstructor(representativeModelConstructor);
        nemo0Builder.getDSSBuilder().setDSSParamsAdjuster(dssAdjuster);
        nemo0Builder.setCriteria(problem._criteria);
        nemo0Builder.setInitialPopulationConstructor(construct);
        nemo0Builder.setParentsReproducer(reproduce);
        nemo0Builder.setSpecimensEvaluator(evaluate);
        nemo0Builder.setParentsSelector(select);

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            nemo0Builder.setDynamicOSBoundsLearningPolicy();
            nemo0Builder.setOSMParamsAdjuster(osAdjuster);
            nemo0Builder.setUseNadirIncumbent(useNadirIncumbent);
            nemo0Builder.setUseUtopiaIncumbent(true);
        }
        else nemo0Builder.setFixedOSBoundsLearningPolicy(problem);

        nemo0Builder.setPopulationSize(populationSize);
        nemo0Builder.setName("NEMO-0");
        nemo0Builder.setID(id);
        nemo0Builder.setNEMO0ParamsAdjuster(bundleAdjuster);
        nemo0Builder.setEAParamsAdjuster(eaParamsAdjuster);
        return getNEMO0(nemo0Builder);
    }


    /**
     * Creates the NEMO-0 algorithm using {@link NEMO0Builder}.
     *
     * @param nemo0Builder NEMO-0 builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                     {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                     initialized as imposed by the specified  configuration; also note that the adjusters give
     *                     greater access to the data being instantiated and, thus, the validity of custom adjustments
     *                     is typically unchecked and may lead to errors
     * @param <T>          type of the internal model used
     * @return the NEMO-0 algorithm
     */
    public static <T extends AbstractValueInternalModel> NEMO0 getNEMO0(NEMO0Builder<T> nemo0Builder)
    {
        model.constructor.value.representative.RepresentativeModel<T> representativeModel =
                (model.constructor.value.representative.RepresentativeModel<T>)
                        nemo0Builder.getDSSBuilder().getModelConstructor();
        NEMO0Bundle.Params pB = NEMO0Bundle.Params.getDefault(nemo0Builder.getCriteria(),
                "DM",
                nemo0Builder.getDSSBuilder().getInteractionRule(),
                nemo0Builder.getDSSBuilder().getReferenceSetConstructor(),
                nemo0Builder.getDSSBuilder().getDMFeedbackProvider(),
                nemo0Builder.getDSSBuilder().getPreferenceModel(),
                representativeModel,
                nemo0Builder.getDSSBuilder().getDSSParamsAdjuster());

        pB._construct = nemo0Builder.getInitialPopulationConstructor();
        pB._reproduce = nemo0Builder.getParentsReproducer();
        pB._evaluate = nemo0Builder.getSpecimensEvaluator();
        pB._select = nemo0Builder.getParentsSelector();

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (nemo0Builder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = nemo0Builder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = nemo0Builder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = nemo0Builder.shouldUseNadirIncumbent();
            if ((nemo0Builder.getUtopia() != null) && (nemo0Builder.getNadir() != null))
                pOS._os = new ObjectiveSpace(nemo0Builder.getUtopia(), nemo0Builder.getNadir());
            if (nemo0Builder.getOSMParamsAdjuster() != null) nemo0Builder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = nemo0Builder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(nemo0Builder.getUtopia(), nemo0Builder.getNadir());
        }
        pB._name = "NEMO-0";

        if (nemo0Builder.getNEMO0ParamsAdjuster() != null)
            nemo0Builder.getNEMO0ParamsAdjuster().adjust(pB);

        NEMO0Bundle bundle = new NEMO0Bundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(nemo0Builder.getCriteria(), bundle);
        pEA._populationSize = nemo0Builder.getPopulationSize();
        pEA._offspringSize = nemo0Builder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = nemo0Builder.getR();
        pEA._id = nemo0Builder.getID();
        if (nemo0Builder.getEAParamsAdjuster() != null)
            nemo0Builder.getEAParamsAdjuster().adjust(pEA);

        return new NEMO0(pEA, bundle.getDSS());
    }
}
