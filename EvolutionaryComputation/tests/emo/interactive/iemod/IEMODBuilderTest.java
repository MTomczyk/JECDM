package emo.interactive.iemod;

import criterion.Criteria;
import emo.interactive.StandardDSSBuilder;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link IEMODBuilderTest}.
 *
 * @author MTomczyk
 */
class IEMODBuilderTest
{

    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            IEMODBuilder<model.internals.value.scalarizing.LNorm> iemodBuilder = new IEMODBuilder<>(null);
            TestUtils.compare("The random number generator is not provided", iemodBuilder::getInstance);
        }
        {
            IEMODBuilder<model.internals.value.scalarizing.LNorm> iemodBuilder = new IEMODBuilder<>(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", iemodBuilder::getInstance);
            iemodBuilder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", iemodBuilder::getInstance);
            iemodBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", iemodBuilder::getInstance);
            iemodBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", iemodBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            iemodBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", iemodBuilder::getInstance);
            iemodBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", iemodBuilder::getInstance);
            iemodBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", iemodBuilder::getInstance);
            iemodBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            TestUtils.compare("The optimization goals are not provided (the array is null)", iemodBuilder::getInstance);
            iemodBuilder.setGoals(new IGoal[0]);
            iemodBuilder.setPopulationSize(10); // overwrite
            TestUtils.compare("The optimization goals are not provided (the array is empty)", iemodBuilder::getInstance);
            iemodBuilder.setGoals(GoalsFactory.getLNormsDND(2, 10, Double.POSITIVE_INFINITY));
            iemodBuilder.setPopulationSize(3); // overwrite
            TestUtils.compare("The number of optimization goals provided is different than the expected population size (11 vs 3)", iemodBuilder::getInstance);
            iemodBuilder.setPopulationSize(11); // overwrite
            iemodBuilder.setSimilarity(null);
            TestUtils.compare("The similarity measure is not provided", iemodBuilder::getInstance);
            iemodBuilder.setSimilarity(new Euclidean());
            iemodBuilder.setNeighborhoodSize(0);
            TestUtils.compare("The neighborhood size must not be less than 1 (equals = 0)", iemodBuilder::getInstance);
            iemodBuilder.setNeighborhoodSize(5);
            iemodBuilder.setAlloc(null);
            TestUtils.compare("The allocation procedure is not provided", iemodBuilder::getInstance);
            iemodBuilder.setAlloc(new Uniform());
            iemodBuilder.setReassignmentStrategy(null);
            TestUtils.compare("The assignment strategy has not been provided", iemodBuilder::getInstance);
            iemodBuilder.setReassignmentStrategy(new BestReassignments());
            TestUtils.compare("The decision support system builder has not been provided", iemodBuilder::getInstance);
            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            iemodBuilder.setStandardDSSBuilder(DSSB);
            TestUtils.compare("No interaction rule has been provided", iemodBuilder::getInstance);
            DSSB.setInteractionRule(new IterationInterval(1000));
            TestUtils.compare("No reference set constructor has been provided", iemodBuilder::getInstance);
            DSSB.setReferenceSetConstructor(new RandomPairs());
            TestUtils.compare("No decision-maker's feedback provider has been provided", iemodBuilder::getInstance);
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            TestUtils.compare("No preference model has been provided", iemodBuilder::getInstance);
            DSSB.setPreferenceModel(new model.definitions.LNorm());
            TestUtils.compare("No model constructor has been provided", iemodBuilder::getInstance);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000;
            DSSB.setModelConstructor(new FRS<>(pFRS));
            TestUtils.compare(null, iemodBuilder::getInstance);
        }
    }
}