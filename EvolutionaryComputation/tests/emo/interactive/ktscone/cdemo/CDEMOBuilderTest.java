package emo.interactive.ktscone.cdemo;

import criterion.Criteria;
import emo.interactive.StandardDSSBuilder;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import model.internals.value.scalarizing.KTSCone;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link CDEMOBuilder}.
 *
 * @author MTomczyk
 */
class CDEMOBuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            CDEMOBuilder cdemoBuilder = new CDEMOBuilder(null);
            TestUtils.compare("The random number generator is not provided", cdemoBuilder::getInstance);
        }
        {
            CDEMOBuilder cdemoBuilder = new CDEMOBuilder(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", cdemoBuilder::getInstance);
            cdemoBuilder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", cdemoBuilder::getInstance);
            cdemoBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", cdemoBuilder::getInstance);
            cdemoBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", cdemoBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            cdemoBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", cdemoBuilder::getInstance);
            cdemoBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", cdemoBuilder::getInstance);
            cdemoBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", cdemoBuilder::getInstance);
            cdemoBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            cdemoBuilder.setPopulationSize(10);
            TestUtils.compare("The decision support system builder has not been provided", cdemoBuilder::getInstance);
            StandardDSSBuilder<KTSCone> DSSB = new StandardDSSBuilder<>();
            cdemoBuilder.setStandardDSSBuilder(DSSB);
            TestUtils.compare("No interaction rule has been provided", cdemoBuilder::getInstance);
            DSSB.setInteractionRule(new IterationInterval(1000));
            TestUtils.compare("No reference set constructor has been provided", cdemoBuilder::getInstance);
            DSSB.setReferenceSetConstructor(new RandomPairs());
            TestUtils.compare("No decision-maker's feedback provider has been provided", cdemoBuilder::getInstance);
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            TestUtils.compare("No preference model has been provided", cdemoBuilder::getInstance);
            DSSB.setPreferenceModel(new model.definitions.KTSCone());
            TestUtils.compare("No model constructor has been provided", cdemoBuilder::getInstance);
            DSSB.setModelConstructor(new model.constructor.value.KTSCone());
            TestUtils.compare(null, cdemoBuilder::getInstance);
        }
    }
}