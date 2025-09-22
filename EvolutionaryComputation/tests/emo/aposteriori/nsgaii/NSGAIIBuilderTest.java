package emo.aposteriori.nsgaii;

import criterion.Criteria;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link NSGAIIBuilder}.
 *
 * @author MTomczyk
 */
class NSGAIIBuilderTest
{

    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(null);
            TestUtils.compare("The random number generator is not provided", nsgaiiBuilder::getInstance);
        }
        {
            NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setPopulationSize(100);
            TestUtils.compare("The criterion (or criteria) is (are) not provided", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", nsgaiiBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            nsgaiiBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", nsgaiiBuilder::getInstance);
            nsgaiiBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);

            TestUtils.compare(null, nsgaiiBuilder::getInstance);
        }
    }
}