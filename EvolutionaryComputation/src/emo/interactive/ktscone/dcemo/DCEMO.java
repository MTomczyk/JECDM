package emo.interactive.ktscone.dcemo;

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
 * Provides means for creating an instance of DCEMO.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class DCEMO extends AbstractInteractiveEA implements IEA
{
    /**
     * Parameterized constructor (private).
     *
     * @param p   params container
     * @param dss instantiated decision support system
     */
    private DCEMO(EA.Params p, DecisionSupportSystem dss)
    {
        super(p, dss);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getDCEMO(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getDCEMO(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 DecisionSupportSystem.IParamsAdjuster dssAdjuster)
    {
        return getDCEMO(0, populationSize, true, false, R, problem,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
                                 int populationSize,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 AbstractMOOProblemBundle problem,
                                 IRule interactionRule,
                                 IReferenceSetConstructor referenceSetConstructor,
                                 IDMFeedbackProvider dmFeedbackProvider)
    {
        return getDCEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
        return getDCEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
        return getDCEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, new Random(2), problem._construct,
                problem._evaluate, problem._reproduce, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider, osAdjuster, dssAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), interactionRule,
                referenceSetConstructor, dmFeedbackProvider, osAdjuster, dssAdjuster);
    }


    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }


    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int populationSize,
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
        return getDCEMO(0, populationSize, true, false, R, problem,
                select, construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                osAdjuster, dssAdjuster);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
        return getDCEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select,
                construct, evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, null);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
        return getDCEMO(id, populationSize, updateOSDynamically, useNadirIncumbent, R, problem, select, construct,
                evaluate, reproduce, interactionRule, referenceSetConstructor, dmFeedbackProvider, osAdjuster, null);
    }

    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
        DCEMOBundle.Params pB = DCEMOBundle.Params.getDefault(problem._criteria,
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
            // Set the initial normalizations (will be delivered to the object responsible for calculating crowding distances):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(problem);
        }

        pB._name = "DCEMO";

        DCEMOBundle bundle = new DCEMOBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, bundle);
        pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(bundle._phasesBundle);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = R;
        pEA._id = id;

        return new DCEMO(pEA, bundle.getDSS());
    }


    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
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
     * @param bundleAdjuster          if provided, it is used to adjust the {@link DCEMO.Params} instance being created
     *                                by this method to instantiate the IEMO/D algorithm; adjustment is  done  after the
     *                                default initialization
     * @param eaParamsAdjuster        if provided, it is used to adjust the {@link EA.Params} instance being created by
     *                                this method to instantiate the DCEMO algorithm; adjustment is done after the
     *                                default initialization
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(int id,
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
                                 DCEMOBundle.IParamsAdjuster bundleAdjuster,
                                 EA.IParamsAdjuster eaParamsAdjuster)
    {
        DCEMOBuilder dcemoBuilder = new DCEMOBuilder(R);
        dcemoBuilder.setCriteria(problem._criteria);
        dcemoBuilder.setStandardDSSBuilder(new StandardDSSBuilder<>());
        dcemoBuilder.getDSSBuilder().setInteractionRule(interactionRule);
        dcemoBuilder.getDSSBuilder().setReferenceSetConstructor(referenceSetConstructor);
        dcemoBuilder.getDSSBuilder().setDMFeedbackProvider(dmFeedbackProvider);
        dcemoBuilder.getDSSBuilder().setDSSParamsAdjuster(dssAdjuster);
        dcemoBuilder.getDSSBuilder().setPreferenceModel(new KTSCone());
        dcemoBuilder.getDSSBuilder().setModelConstructor(new model.constructor.value.KTSCone());

        dcemoBuilder.setInitialPopulationConstructor(construct);
        dcemoBuilder.setParentsReproducer(reproduce);
        dcemoBuilder.setSpecimensEvaluator(evaluate);
        dcemoBuilder.setParentsSelector(select);

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            dcemoBuilder.setDynamicOSBoundsLearningPolicy();
            dcemoBuilder.setOSMParamsAdjuster(osAdjuster);
            dcemoBuilder.setUseNadirIncumbent(useNadirIncumbent);
            dcemoBuilder.setUseUtopiaIncumbent(true);
        }
        else dcemoBuilder.setFixedOSBoundsLearningPolicy(problem);

        dcemoBuilder.setPopulationSize(populationSize);
        dcemoBuilder.setName("DCEMO");
        dcemoBuilder.setID(id);
        dcemoBuilder.setDCEMOParamsAdjuster(bundleAdjuster);
        dcemoBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getDCEMO(dcemoBuilder);
    }


    /**
     * Creates the DCEMO algorithm. It employs a default decision support system that involves one decision maker (model
     * and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default).
     *
     * @param dcemoBuilder DCEMO builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                     {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                     initialized as imposed by the specified  configuration; also note that the adjusters give
     *                     greater access to the data being instantiated and, thus, the validity of custom adjustments
     *                     is typically unchecked and may lead to errors
     * @return DCEMO algorithm
     */
    public static DCEMO getDCEMO(DCEMOBuilder dcemoBuilder)
    {
        DCEMOBundle.Params pB = DCEMOBundle.Params.getDefault(
                dcemoBuilder.getCriteria(),
                "DM",
                dcemoBuilder.getDSSBuilder().getInteractionRule(),
                dcemoBuilder.getDSSBuilder().getReferenceSetConstructor(),
                dcemoBuilder.getDSSBuilder().getDMFeedbackProvider(),
                dcemoBuilder.getDSSBuilder().getDSSParamsAdjuster(),
                dcemoBuilder.getDSSBuilder().getPreferenceModel(),
                dcemoBuilder.getDSSBuilder().getModelConstructor());

        pB._construct = dcemoBuilder.getInitialPopulationConstructor();
        pB._reproduce = dcemoBuilder.getParentsReproducer();
        pB._evaluate = dcemoBuilder.getSpecimensEvaluator();
        pB._select = dcemoBuilder.getParentsSelector();

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (dcemoBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = dcemoBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = dcemoBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = dcemoBuilder.shouldUseNadirIncumbent();
            if ((dcemoBuilder.getUtopia() != null) && (dcemoBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(dcemoBuilder.getUtopia(), dcemoBuilder.getNadir());
            if (dcemoBuilder.getOSMParamsAdjuster() != null) dcemoBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = dcemoBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(dcemoBuilder.getUtopia(), dcemoBuilder.getNadir());
        }

        pB._name = "DCEMO";

        if (dcemoBuilder.getDCEMOParamsAdjuster() != null) dcemoBuilder.getDCEMOParamsAdjuster().adjust(pB);
        DCEMOBundle bundle = new DCEMOBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(dcemoBuilder.getCriteria(), bundle);
        pEA._populationSize = dcemoBuilder.getPopulationSize();
        pEA._offspringSize = dcemoBuilder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = dcemoBuilder.getR();
        pEA._id = dcemoBuilder.getID();
        if (dcemoBuilder.getEAParamsAdjuster() != null) dcemoBuilder.getEAParamsAdjuster().adjust(pEA);

        return new DCEMO(pEA, bundle.getDSS());
    }
}
