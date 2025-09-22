package emo.interactive.nemo.nemoii;

import criterion.Criteria;
import emo.interactive.StandardDSSBuilder;
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
 * Provides various tests for {@link NEMOIIBuilder}.
 *
 * @author MTomczyk
 */
class NEMOIIBuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            NEMOIIBuilder<LNorm> nemoiiBuilder = new NEMOIIBuilder<>(null);
            TestUtils.compare("The random number generator is not provided", nemoiiBuilder::getInstance);
        }
        {
            NEMOIIBuilder<model.internals.value.scalarizing.LNorm> nemoiiBuilder = new NEMOIIBuilder<>(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", nemoiiBuilder::getInstance);
            nemoiiBuilder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", nemoiiBuilder::getInstance);
            nemoiiBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", nemoiiBuilder::getInstance);
            nemoiiBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", nemoiiBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            nemoiiBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", nemoiiBuilder::getInstance);
            nemoiiBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", nemoiiBuilder::getInstance);
            nemoiiBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", nemoiiBuilder::getInstance);
            nemoiiBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            nemoiiBuilder.setPopulationSize(10);
            TestUtils.compare("The decision support system builder has not been provided", nemoiiBuilder::getInstance);
            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            nemoiiBuilder.setStandardDSSBuilder(DSSB);
            TestUtils.compare("No interaction rule has been provided", nemoiiBuilder::getInstance);
            DSSB.setInteractionRule(new IterationInterval(1000));
            TestUtils.compare("No reference set constructor has been provided", nemoiiBuilder::getInstance);
            DSSB.setReferenceSetConstructor(new RandomPairs());
            TestUtils.compare("No decision-maker's feedback provider has been provided", nemoiiBuilder::getInstance);
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            TestUtils.compare("No preference model has been provided", nemoiiBuilder::getInstance);
            DSSB.setPreferenceModel(new model.definitions.LNorm());
            TestUtils.compare("No model constructor has been provided", nemoiiBuilder::getInstance);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000;
            DSSB.setModelConstructor(new FRS<>(pFRS));
            TestUtils.compare(null, nemoiiBuilder::getInstance);
        }
    }

}