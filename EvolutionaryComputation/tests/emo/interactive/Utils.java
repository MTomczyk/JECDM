package emo.interactive;

import criterion.Criteria;
import ea.EA;
import emo.interactive.iemod.IEMODBundle;
import emo.interactive.ktscone.cdemo.CDEMOBundle;
import emo.interactive.ktscone.dcemo.DCEMOBundle;
import emo.interactive.nemo.nemo0.NEMO0Bundle;
import emo.interactive.nemo.nemoii.NEMOIIBundle;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.similarity.ISimilarity;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.frs.FRS;
import model.constructor.value.representative.MDVF;
import model.constructor.value.frs.representative.RepresentativeModel;
import model.definitions.LNorm;
import model.internals.value.AbstractValueInternalModel;
import os.ObjectiveSpaceManager;
import phase.PhasesBundle;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import selection.Random;
import selection.Tournament;
import space.normalization.INormalization;

/**
 * Utils class that provides a common code for all test cases.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Returns a model constructor (FRS {@link FRS}, L-norms with alpha = infinity).
     *
     * @param noSamples     the number of samples to generate
     * @param samplingLimit sampling limit for FRS
     * @param dimensions    dimensionality (the number of weights)
     * @return model constructor
     */
    public static IConstructor<model.internals.value.scalarizing.LNorm> getModelConstructor(int noSamples, int samplingLimit, int dimensions)
    {
        IRandomModel<model.internals.value.scalarizing.LNorm> RM = new LNormGenerator(dimensions, Double.POSITIVE_INFINITY);
        FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(RM);
        pFRS._feasibleSamplesToGenerate = noSamples;
        pFRS._samplingLimit = samplingLimit;
        pFRS._inconsistencyThreshold = noSamples - 1;
        return new FRS<>(pFRS);
    }

    /**
     * Returns a representative model constructor (MDVF {@link MDVF}, founded on FRS {@link FRS}, L-norms with alpha = infinity).
     *
     * @param noSamples     the number of samples to generate
     * @param samplingLimit sampling limit for FRS
     * @param dimensions    dimensionality (the number of weights)
     * @return model constructor
     */
    public static RepresentativeModel<model.internals.value.scalarizing.LNorm> getRepresentativeModelConstructor(int noSamples, int samplingLimit, int dimensions)
    {
        IRandomModel<model.internals.value.scalarizing.LNorm> RM = new LNormGenerator(dimensions, Double.POSITIVE_INFINITY);
        RepresentativeModel.Params<model.internals.value.scalarizing.LNorm> pRMC = new RepresentativeModel.Params<>(RM, new MDVF<>());
        pRMC._feasibleSamplesToGenerate = noSamples;
        pRMC._samplingLimit = samplingLimit;
        pRMC._inconsistencyThreshold = noSamples - 1;
        return new RepresentativeModel<>(pRMC);
    }

    /**
     * Returns a default feedback provider for 2D case (artificial value-based model base on an L-norm with [0.3, 0.7]
     * weights and alpha of infinity (Chebyshev function).
     *
     * @param normalizations normalizations
     * @return DM-based feedback provider
     */
    public static IDMFeedbackProvider getDefaultDMFeedbackProvider2D(INormalization[] normalizations)
    {
        return new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new space.scalarfunction.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY, normalizations))));
    }

    /**
     * Returns a default feedback provider for 3D case (artificial value-based model base on an L-norm with [0.3, 0.2, 0.5]
     * weights and alpha of infinity (Chebyshev function).
     *
     * @param normalizations normalization functions
     * @return DM-based feedback provider
     */
    public static IDMFeedbackProvider getDefaultDMFeedbackProvider3D(INormalization[] normalizations)
    {
        return new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new space.scalarfunction.LNorm(new double[]{0.3d, 0.2d, 0.5d}, Double.POSITIVE_INFINITY, normalizations))));
    }


    /**
     * Creates IEMO/D instance.
     *
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveRanges  true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param goals                   optimization goals used
     * @param similarity              similarity function used
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model instance constructor
     * @param <T>                     internal preference model definition
     * @return IEMO/D instance
     */
    public static <T extends AbstractValueInternalModel> EA getIEMOD(Criteria criteria,
                                                                     AbstractMOOProblemBundle problemBundle,
                                                                     boolean dynamicObjectiveRanges,
                                                                     IRandom R,
                                                                     IGoal[] goals,
                                                                     ISimilarity similarity,
                                                                     IRule interactionRule,
                                                                     IReferenceSetConstructor referenceSetConstructor,
                                                                     IDMFeedbackProvider dmFeedbackProvider,
                                                                     IPreferenceModel<T> preferenceModel,
                                                                     IConstructor<T> modelConstructor)
    {
        return getIEMOD(0, criteria, problemBundle, dynamicObjectiveRanges, R, goals, similarity,
                interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates IEMO/D instance.
     *
     * @param id                      EA's id
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param goals                   optimization goals used
     * @param similarity              similarity function used
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model instance constructor
     * @param <T>                     internal preference model definition
     * @return IEMO/D instance
     */
    public static <T extends AbstractValueInternalModel> EA getIEMOD(int id,
                                                                     Criteria criteria,
                                                                     AbstractMOOProblemBundle problemBundle,
                                                                     boolean dynamicObjectiveSpace,
                                                                     IRandom R,
                                                                     IGoal[] goals,
                                                                     ISimilarity similarity,
                                                                     IRule interactionRule,
                                                                     IReferenceSetConstructor referenceSetConstructor,
                                                                     IDMFeedbackProvider dmFeedbackProvider,
                                                                     IPreferenceModel<T> preferenceModel,
                                                                     IConstructor<T> modelConstructor)
    {
        MOEADGoalsManager.Params pGM = new MOEADGoalsManager.Params(goals, similarity, 10);
        pGM._alloc = new Uniform();
        MOEADGoalsManager GM = new MOEADGoalsManager(pGM);

        IEMODBundle.Params<T> pAB = IEMODBundle.Params.getDefault(criteria, GM, "DM", interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, modelConstructor);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;
        pAB._select = new Random(2, 1);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, true, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        IEMODBundle algorithmBundle = new IEMODBundle(pAB);

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
     * Creates DCEMO instance.
     *
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @return DCEMO instance
     */
    public static EA getDCEMO(Criteria criteria,
                              int populationSize,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveSpace,
                              IRandom R,
                              IRule interactionRule,
                              IReferenceSetConstructor referenceSetConstructor,
                              IDMFeedbackProvider dmFeedbackProvider)
    {
        return getDCEMO(0, criteria, populationSize, problemBundle, dynamicObjectiveSpace,
                R, interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates DCEMO instance.
     *
     * @param id                      EA's id
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @return DCEMO instance
     */
    public static EA getDCEMO(int id,
                              Criteria criteria,
                              int populationSize,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveSpace,
                              IRandom R,
                              IRule interactionRule,
                              IReferenceSetConstructor referenceSetConstructor,
                              IDMFeedbackProvider dmFeedbackProvider)
    {
        DCEMOBundle.Params pAB = DCEMOBundle.Params.getDefault(criteria, "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;
        Tournament.Params pT = new Tournament.Params();
        pT._size = 5;
        pT._preferenceDirection = false;
        pT._noOffspring = populationSize;
        pT._noParentsPerOffspring = 2;
        pAB._select = new Tournament(pT);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, true, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        DCEMOBundle algorithmBundle = new DCEMOBundle(pAB);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);

    }

    /**
     * Creates CDEMO instance.
     *
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @return CDEMO instance
     */
    public static EA getCDEMO(Criteria criteria,
                              int populationSize,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveSpace,
                              IRandom R,
                              IRule interactionRule,
                              IReferenceSetConstructor referenceSetConstructor,
                              IDMFeedbackProvider dmFeedbackProvider)
    {
        return getCDEMO(0, criteria, populationSize, problemBundle, dynamicObjectiveSpace,
                R, interactionRule, referenceSetConstructor, dmFeedbackProvider);
    }

    /**
     * Creates CDEMO instance.
     *
     * @param id                      EA's id
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @return CDEMO instance
     */
    public static EA getCDEMO(int id,
                              Criteria criteria,
                              int populationSize,
                              AbstractMOOProblemBundle problemBundle,
                              boolean dynamicObjectiveSpace,
                              IRandom R,
                              IRule interactionRule,
                              IReferenceSetConstructor referenceSetConstructor,
                              IDMFeedbackProvider dmFeedbackProvider)
    {
        CDEMOBundle.Params pAB = CDEMOBundle.Params.getDefault(criteria, "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider);
        pAB._construct = problemBundle._construct;
        pAB._reproduce = problemBundle._reproduce;
        pAB._evaluate = problemBundle._evaluate;
        Tournament.Params pT = new Tournament.Params();
        pT._size = 5;
        pT._preferenceDirection = false;
        pT._noOffspring = populationSize;
        pT._noParentsPerOffspring = 2;
        pAB._select = new Tournament(pT);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, true, criteria);
        pAB._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pAB._initialNormalizations = problemBundle._normalizations;
        else pAB._initialNormalizations = null;

        CDEMOBundle algorithmBundle = new CDEMOBundle(pAB);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._osManager = pAB._osManager;
        return new EA(pEA);
    }

    /**
     * Creates NEMO-0 instance.
     *
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model instance constructor
     * @param <T>                     internal preference model definition
     * @return NEMO-0 instance
     */
    public static <T extends AbstractValueInternalModel> EA getNEMO0(Criteria criteria,
                                                                     int populationSize,
                                                                     AbstractMOOProblemBundle problemBundle,
                                                                     boolean dynamicObjectiveSpace,
                                                                     IRandom R,
                                                                     IRule interactionRule,
                                                                     IReferenceSetConstructor referenceSetConstructor,
                                                                     IDMFeedbackProvider dmFeedbackProvider,
                                                                     IPreferenceModel<T> preferenceModel,
                                                                     RepresentativeModel<T> modelConstructor)
    {
        return getNEMO0(0, criteria, populationSize, problemBundle, dynamicObjectiveSpace,
                R, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates NEMO-0 instance.
     *
     * @param id                      EA's id
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model instance constructor
     * @param <T>                     internal preference model definition
     * @return NEMO-0 instance
     */
    public static <T extends AbstractValueInternalModel> EA getNEMO0(int id,
                                                                     Criteria criteria,
                                                                     int populationSize,
                                                                     AbstractMOOProblemBundle problemBundle,
                                                                     boolean dynamicObjectiveSpace,
                                                                     IRandom R,
                                                                     IRule interactionRule,
                                                                     IReferenceSetConstructor referenceSetConstructor,
                                                                     IDMFeedbackProvider dmFeedbackProvider,
                                                                     IPreferenceModel<T> preferenceModel,
                                                                     RepresentativeModel<T> modelConstructor)
    {
        NEMO0Bundle.Params pNEMO0 = NEMO0Bundle.Params.getDefault(criteria,
                "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);

        pNEMO0._construct = problemBundle._construct;
        pNEMO0._reproduce = problemBundle._reproduce;
        pNEMO0._evaluate = problemBundle._evaluate;
        Tournament.Params pT = new Tournament.Params();
        pT._size = 5;
        pT._preferenceDirection = false;
        pT._noOffspring = populationSize;
        pT._noParentsPerOffspring = 2;
        pNEMO0._select = new Tournament(pT);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, true, criteria);
        pNEMO0._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pNEMO0._initialNormalizations = problemBundle._normalizations;
        else pNEMO0._initialNormalizations = null;

        NEMO0Bundle algorithmBundle = new NEMO0Bundle(pNEMO0);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._osManager = pNEMO0._osManager;
        return new EA(pEA);
    }


    /**
     * Creates NEMO-II instance.
     *
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model constructor that is supposed to generate a plurality of compatible preference model instances (e.g. {@link model.constructor.value.frs.FRS})
     * @param <T>                     internal preference model definition
     * @return NEMO-II instance
     */
    public static <T extends AbstractValueInternalModel> EA getNEMOII(Criteria criteria,
                                                                      int populationSize,
                                                                      AbstractMOOProblemBundle problemBundle,
                                                                      boolean dynamicObjectiveSpace,
                                                                      IRandom R,
                                                                      IRule interactionRule,
                                                                      IReferenceSetConstructor referenceSetConstructor,
                                                                      IDMFeedbackProvider dmFeedbackProvider,
                                                                      IPreferenceModel<T> preferenceModel,
                                                                      IConstructor<T> modelConstructor)
    {
        return getNEMOII(0, criteria, populationSize, problemBundle, dynamicObjectiveSpace,
                R, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
    }

    /**
     * Creates NEMO-0 instance.
     *
     * @param id                      EA's id
     * @param populationSize          populationSize
     * @param criteria                considered criteria
     * @param problemBundle           problem bundle
     * @param dynamicObjectiveSpace   true = dynamic objective ranges mode is on
     * @param R                       random number generator
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model constructor that is supposed to generate a plurality of compatible preference model instances (e.g. {@link model.constructor.value.frs.FRS})
     * @param <T>                     internal preference model definition
     * @return NEMO-II instance
     */
    public static <T extends AbstractValueInternalModel> EA getNEMOII(int id,
                                                                      Criteria criteria,
                                                                      int populationSize,
                                                                      AbstractMOOProblemBundle problemBundle,
                                                                      boolean dynamicObjectiveSpace,
                                                                      IRandom R,
                                                                      IRule interactionRule,
                                                                      IReferenceSetConstructor referenceSetConstructor,
                                                                      IDMFeedbackProvider dmFeedbackProvider,
                                                                      IPreferenceModel<T> preferenceModel,
                                                                      IConstructor<T> modelConstructor)
    {
        NEMOIIBundle.Params pNEMOII = NEMOIIBundle.Params.getDefault(criteria,
                "DM", interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);

        pNEMOII._construct = problemBundle._construct;
        pNEMOII._reproduce = problemBundle._reproduce;
        pNEMOII._evaluate = problemBundle._evaluate;
        Tournament.Params pT = new Tournament.Params();
        pT._size = 5;
        pT._preferenceDirection = false;
        pT._noOffspring = populationSize;
        pT._noParentsPerOffspring = 2;
        pNEMOII._select = new Tournament(pT);

        ObjectiveSpaceManager.Params pOS = ObjectiveSpaceManager.getInstantiatedParams(problemBundle, !dynamicObjectiveSpace,
                dynamicObjectiveSpace, true, true, criteria);
        pNEMOII._osManager = new ObjectiveSpaceManager(pOS);
        if (!dynamicObjectiveSpace) pNEMOII._initialNormalizations = problemBundle._normalizations;
        else pNEMOII._initialNormalizations = null;

        NEMOIIBundle algorithmBundle = new NEMOIIBundle(pNEMOII);

        // create ea instance
        EA.Params pEA = new EA.Params(algorithmBundle._name, criteria);
        PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
        pEA._id = id;
        pEA._R = R;
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._osManager = pNEMOII._osManager;
        return new EA(pEA);
    }
}
