package emo.interactive.ktscone.cdemo;

import criterion.Criteria;
import ea.AbstractInteractiveEA;
import ea.EA;
import ea.IEA;
import emo.interactive.StandardDSSBuilder;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.definitions.KTSCone;
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
 * Provides means for creating an instance of CDEMO.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class CDEMO extends AbstractInteractiveEA implements IEA
{

    /**
     * Parameterized constructor (private).
     *
     * @param p   params container
     * @param dss instantiated decision support system
     */
    private CDEMO(EA.Params p, DecisionSupportSystem dss)
    {
        super(p, dss);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during
     * the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(0, populationSize, true, false, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during
     * the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params
     *                                container built when is set to updateOSDynamically (can be null; not used)
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(0, populationSize, true, false, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically (uses utopia incumbent during
     * the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and
     *                                reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getCDEMO(0, populationSize, true, false, R, problem,
                new Random(2), problem._construct, problem._evaluate, problem._reproduce,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
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
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
                                 int populationSize,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2),
                problem._construct, problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
                                 int populationSize,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2),
                problem._construct, problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
                                 int populationSize,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getCDEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2),
                problem._construct, problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
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
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(0, populationSize, true, false,
                R, MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider,
                osAdjuster, dssAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 IConstruct construct,
                                 IEvaluate evaluate,
                                 IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 IConstruct construct,
                                 IEvaluate evaluate,
                                 IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). Sets id to 0 and parameterizes the method to update
     * the OS dynamically (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria)
     * @param select                  parents selector
     * @param construct               specimens constructor
     * @param evaluate                specimens evaluator
     * @param reproduce               specimens reproducer
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 IConstruct construct,
                                 IEvaluate evaluate,
                                 IReproduce reproduce,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getCDEMO(0, populationSize, true, false,
                R, problem, select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
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
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
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
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, null);
    }


    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
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
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getCDEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct,
                evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster, null);
    }

    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
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
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        CDEMOBundle.Params pB = CDEMOBundle.Params.getDefault(problem._criteria,
                "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider, dssAdjuster);

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
            if (osAdjuster != null) osAdjuster.adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(problem);
        }

        pB._name = "CDEMO";

        CDEMOBundle bundle = new CDEMOBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, bundle);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = R;
        pEA._id = id;

        return new CDEMO(pEA, bundle.getDSS());
    }


    /**
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
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
     * @param osAdjuster              auxiliary object (can be null) responsible for customizing objective space manager
     *                                params container built when the method is expected to update its known bounds on
     *                                the objective space dynamically (otherwise, it is possible that the manager will
     *                                be null; the adjuster is not used).
     * @param dssAdjuster             an auxiliary object (can be null) responsible for decision support system params
     *                                container built when instantiating the algorithm; it is assumed that the
     *                                parameterization is done after the default parameterisation is completed
     * @param bundleAdjuster          if provided, it is used to adjust the {@link CDEMO.Params} instance being created
     *                                by this method to instantiate the IEMO/D algorithm; adjustment is  done  after the
     *                                default initialization
     * @param eaParamsAdjuster        if provided, it is used to adjust the {@link EA.Params} instance being created by
     *                                this method to instantiate the CDEMO algorithm; adjustment is done after the
     *                                default initialization
     * @return CDEMO algorithm
     */
    public static CDEMO getCDEMO(int id,
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
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster,
                                 CDEMOBundle.IParamsAdjuster bundleAdjuster,
                                 EA.IParamsAdjuster eaParamsAdjuster)
    {
        CDEMOBuilder cdemoBuilder = new CDEMOBuilder(R);
        cdemoBuilder.setCriteria(problem._criteria);
        cdemoBuilder.setStandardDSSBuilder(new StandardDSSBuilder<>());
        cdemoBuilder.getDSSBuilder().setInteractionRule(interactionRule);
        cdemoBuilder.getDSSBuilder().setReferenceSetConstructor(referenceSetConstructor);
        cdemoBuilder.getDSSBuilder().setDMFeedbackProvider(dmFeedbackProvider);
        cdemoBuilder.getDSSBuilder().setDSSParamsAdjuster(dssAdjuster);
        cdemoBuilder.getDSSBuilder().setPreferenceModel(new KTSCone());
        cdemoBuilder.getDSSBuilder().setModelConstructor(new model.constructor.value.KTSCone());

        cdemoBuilder.setInitialPopulationConstructor(construct);
        cdemoBuilder.setParentsReproducer(reproduce);
        cdemoBuilder.setSpecimensEvaluator(evaluate);
        cdemoBuilder.setParentsSelector(select);

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            cdemoBuilder.setDynamicOSBoundsLearningPolicy();
            cdemoBuilder.setOSMParamsAdjuster(osAdjuster);
            cdemoBuilder.setUseNadirIncumbent(useNadirIncumbent);
            cdemoBuilder.setUseUtopiaIncumbent(true);
        }
        else cdemoBuilder.setFixedOSBoundsLearningPolicy(problem);

        cdemoBuilder.setPopulationSize(populationSize);
        cdemoBuilder.setName("CDEMO");
        cdemoBuilder.setID(id);
        cdemoBuilder.setCDEMOParamsAdjuster(bundleAdjuster);
        cdemoBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getCDEMO(cdemoBuilder);
    }


    /**
     * Creates the CDEMO algorithm using {@link CDEMOBuilder}.
     *
     * @param cdemoBuilder CDEMO builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                     {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                     initialized as imposed by the specified  configuration; also note that the adjusters give
     *                     greater access to the data being instantiated and, thus, the validity of custom adjustments
     *                     is typically unchecked and may lead to errors
     * @return the CDEMO algorithm
     */
    public static CDEMO getCDEMO(CDEMOBuilder cdemoBuilder)
    {
        CDEMOBundle.Params pB = CDEMOBundle.Params.getDefault(
                cdemoBuilder.getCriteria(),
                "DM",
                cdemoBuilder.getDSSBuilder().getInteractionRule(),
                cdemoBuilder.getDSSBuilder().getReferenceSetConstructor(),
                cdemoBuilder.getDSSBuilder().getDMFeedbackProvider(),
                cdemoBuilder.getDSSBuilder().getDSSParamsAdjuster(),
                cdemoBuilder.getDSSBuilder().getPreferenceModel(),
                cdemoBuilder.getDSSBuilder().getModelConstructor());

        pB._construct = cdemoBuilder.getInitialPopulationConstructor();
        pB._reproduce = cdemoBuilder.getParentsReproducer();
        pB._evaluate = cdemoBuilder.getSpecimensEvaluator();
        pB._select = cdemoBuilder.getParentsSelector();

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (cdemoBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = cdemoBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = cdemoBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = cdemoBuilder.shouldUseNadirIncumbent();
            if ((cdemoBuilder.getUtopia() != null) && (cdemoBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(cdemoBuilder.getUtopia(), cdemoBuilder.getNadir());
            if (cdemoBuilder.getOSMParamsAdjuster() != null) cdemoBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = cdemoBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(cdemoBuilder.getUtopia(), cdemoBuilder.getNadir());
        }

        pB._name = "CDEMO";

        if (cdemoBuilder.getCDEMOParamsAdjuster() != null) cdemoBuilder.getCDEMOParamsAdjuster().adjust(pB);
        CDEMOBundle bundle = new CDEMOBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(cdemoBuilder.getCriteria(), bundle);
        pEA._populationSize = cdemoBuilder.getPopulationSize();
        pEA._offspringSize = cdemoBuilder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = cdemoBuilder.getR();
        pEA._id = cdemoBuilder.getID();
        if (cdemoBuilder.getEAParamsAdjuster() != null) cdemoBuilder.getEAParamsAdjuster().adjust(pEA);

        return new CDEMO(pEA, bundle.getDSS());
    }
}
