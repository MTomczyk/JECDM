package emo.interactive.ktscone.cdemo;

import criterion.Criteria;
import ea.AbstractInteractiveEA;
import ea.EA;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
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
public class CDEMO extends AbstractInteractiveEA
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically
     * (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and reproducer)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically
     * (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null; not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents. Sets id to 0 and parameterizes the method to update the OS dynamically
     * (uses utopia incumbent during the updates).
     *
     * @param populationSize          population size
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, specimen constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after
     *                                the default initialization
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
     * inconsistency handler = remove oldest; refiner = default). The method is also coupled with the random selection
     * of parents.
     *
     * @param id                      algorithm id
     * @param populationSize          population size
     * @param updateOSDynamically     if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent       if true, nadir incumbent will be used when updating OS
     * @param R                       the RGN
     * @param problem                 problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      artificial decision maker (feedback provider)
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
     * (model and feedback provider), single interaction rule, and single reference set constructor (representative model;
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
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
     * Creates the CDEMO algorithm. It employs a default decision support system that involves one decision maker
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
     * @param osAdjuster              auxiliary object responsible for customizing objective space manager params container
     *                                built when is set to updateOSDynamically (can be null, if not used)
     * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
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
        pEA._R = R;
        pEA._id = id;

        return new CDEMO(pEA, bundle.getDSS());
    }
}
