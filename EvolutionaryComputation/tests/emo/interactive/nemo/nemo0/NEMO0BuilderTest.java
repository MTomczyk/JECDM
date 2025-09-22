package emo.interactive.nemo.nemo0;

import criterion.Criteria;
import emo.interactive.StandardDSSBuilder;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.representative.MDVF;
import model.constructor.value.representative.RepresentativeModel;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link NEMO0Builder}.
 *
 * @author MTomczyk
 */
class NEMO0BuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            NEMO0Builder<LNorm> nemo0Builder = new NEMO0Builder<>(null);
            TestUtils.compare("The random number generator is not provided", nemo0Builder::getInstance);
        }
        {
            NEMO0Builder<model.internals.value.scalarizing.LNorm> nemo0Builder = new NEMO0Builder<>(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", nemo0Builder::getInstance);
            nemo0Builder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", nemo0Builder::getInstance);
            nemo0Builder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", nemo0Builder::getInstance);
            nemo0Builder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", nemo0Builder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            nemo0Builder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", nemo0Builder::getInstance);
            nemo0Builder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", nemo0Builder::getInstance);
            nemo0Builder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", nemo0Builder::getInstance);
            nemo0Builder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            nemo0Builder.setPopulationSize(10);
            TestUtils.compare("The decision support system builder has not been provided", nemo0Builder::getInstance);
            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            nemo0Builder.setStandardDSSBuilder(DSSB);
            TestUtils.compare("No interaction rule has been provided", nemo0Builder::getInstance);
            DSSB.setInteractionRule(new IterationInterval(1000));
            TestUtils.compare("No reference set constructor has been provided", nemo0Builder::getInstance);
            DSSB.setReferenceSetConstructor(new RandomPairs());
            TestUtils.compare("No decision-maker's feedback provider has been provided", nemo0Builder::getInstance);
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            TestUtils.compare("No preference model has been provided", nemo0Builder::getInstance);
            DSSB.setPreferenceModel(new model.definitions.LNorm());
            TestUtils.compare("No model constructor has been provided", nemo0Builder::getInstance);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000;
            DSSB.setModelConstructor(new FRS<>(pFRS));
            TestUtils.compare("The provided model constructor (model.constructor.value.rs.frs.FRS) is not an instance " +
                    "of model.constructor.value.representative.RepresentativeModel", nemo0Builder::getInstance);
            DSSB.setModelConstructor(new RepresentativeModel<>(
                    new RepresentativeModel.Params<>(new FRS<>(pFRS), new MDVF<>())));
            TestUtils.compare(null, nemo0Builder::getInstance);
        }
    }
}