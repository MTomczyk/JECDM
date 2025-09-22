package emo.aposteriori.nsga;

import criterion.Criteria;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link NSGABuilder}.
 *
 * @author MTomczyk
 */
class NSGABuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            NSGABuilder nsgaBuilder = new NSGABuilder(null);
            TestUtils.compare("The random number generator is not provided", nsgaBuilder::getInstance);
        }
        {
            NSGABuilder nsgaBuilder = new NSGABuilder(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", nsgaBuilder::getInstance);
            nsgaBuilder.setPopulationSize(100);
            TestUtils.compare("The criterion (or criteria) is (are) not provided", nsgaBuilder::getInstance);
            nsgaBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", nsgaBuilder::getInstance);
            nsgaBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", nsgaBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            nsgaBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", nsgaBuilder::getInstance);
            nsgaBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", nsgaBuilder::getInstance);
            nsgaBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", nsgaBuilder::getInstance);
            nsgaBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            // Implementation-specific:
            nsgaBuilder.setThreshold(-1.0d);
            TestUtils.compare("The niche-count distance threshold must not be negative (equals = -1.0)", nsgaBuilder::getInstance);
            nsgaBuilder.setThreshold(1.0d);
            TestUtils.compare(null, nsgaBuilder::getInstance);

        }
    }
}