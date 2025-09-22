package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;

/**
 * Provides various tests for {@link NSGAIIIBuilder}.
 *
 * @author MTomczyk
 */
class NSGAIIIBuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(null);
            TestUtils.compare("The random number generator is not provided", nsgaiiiBuilder::getInstance);
        }
        {
            NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", nsgaiiiBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            nsgaiiiBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            TestUtils.compare("The optimization goals are not provided (the array is null)", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setGoals(new IGoal[0]);
            nsgaiiiBuilder.setPopulationSize(10); // overwrite
            TestUtils.compare("The optimization goals are not provided (the array is empty)", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setGoals(GoalsFactory.getLNormsDND(2, 10, Double.POSITIVE_INFINITY));
            nsgaiiiBuilder.setPopulationSize(3); // overwrite
            TestUtils.compare("The number of optimization goals provided is different than the expected population size (11 vs 3)", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setPopulationSize(11); // overwrite
            nsgaiiiBuilder.setAssigmentResolveTie(null);
            TestUtils.compare("The procedure for resolving assignment selection ties is not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setAssigmentResolveTie(new RandomAssignment());
            nsgaiiiBuilder.setSpecimenResolveTie(null);
            TestUtils.compare("The procedure for resolving specimen selection ties is not provided", nsgaiiiBuilder::getInstance);
            nsgaiiiBuilder.setSpecimenResolveTie(new RandomSpecimen());
            TestUtils.compare(null, nsgaiiiBuilder::getInstance);
        }
    }

}