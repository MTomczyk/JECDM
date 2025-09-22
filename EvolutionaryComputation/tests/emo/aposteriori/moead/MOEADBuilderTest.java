package emo.aposteriori.moead;

import criterion.Criteria;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import selection.Random;
import utils.TestUtils;


/**
 * Provides various tests for {@link MOEADBuilder}.
 *
 * @author MTomczyk
 */
class MOEADBuilderTest
{
    /**
     * Tests the validation procedure.
     */
    @Test
    void testValidate()
    {
        {
            MOEADBuilder moeadBuilder = new MOEADBuilder(null);
            TestUtils.compare("The random number generator is not provided", moeadBuilder::getInstance);
        }
        {
            MOEADBuilder moeadBuilder = new MOEADBuilder(new L32_X64_MIX(0));
            TestUtils.compare("The population size should not be less than 1 (equals = 0)", moeadBuilder::getInstance);
            moeadBuilder.setPopulationSize(100); // will be overwritten when setting goals
            TestUtils.compare("The criterion (or criteria) is (are) not provided", moeadBuilder::getInstance);
            moeadBuilder.setCriteria(Criteria.constructCriteria("C", 2, false));
            TestUtils.compare("The parents selector is not provided", moeadBuilder::getInstance);
            moeadBuilder.setParentsSelector(new Random(2));
            TestUtils.compare("The initial population constructor is not provided", moeadBuilder::getInstance);

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            moeadBuilder.setInitialPopulationConstructor(dtlzBundle._construct);
            TestUtils.compare("The specimen evaluator is not provided", moeadBuilder::getInstance);
            moeadBuilder.setSpecimensEvaluator(dtlzBundle._evaluate);
            TestUtils.compare("The parents reproducer is not provided", moeadBuilder::getInstance);
            moeadBuilder.setParentsReproducer(dtlzBundle._reproduce);
            TestUtils.compare("No OS bounds learning policy has been set", moeadBuilder::getInstance);
            moeadBuilder.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations);
            TestUtils.compare("The optimization goals are not provided (the array is null)", moeadBuilder::getInstance);
            moeadBuilder.setGoals(new IGoal[0]);
            moeadBuilder.setPopulationSize(10); // overwrite
            TestUtils.compare("The optimization goals are not provided (the array is empty)", moeadBuilder::getInstance);
            moeadBuilder.setGoals(GoalsFactory.getLNormsDND(2, 10, Double.POSITIVE_INFINITY));
            moeadBuilder.setPopulationSize(3); // overwrite
            TestUtils.compare("The number of optimization goals provided is different than the expected population size (11 vs 3)", moeadBuilder::getInstance);
            moeadBuilder.setPopulationSize(11); // overwrite
            moeadBuilder.setSimilarity(null);
            TestUtils.compare("The similarity measure is not provided", moeadBuilder::getInstance);
            moeadBuilder.setSimilarity(new Euclidean());
            moeadBuilder.setNeighborhoodSize(0);
            TestUtils.compare("The neighborhood size must not be less than 1 (equals = 0)", moeadBuilder::getInstance);
            moeadBuilder.setNeighborhoodSize(5);
            moeadBuilder.setAlloc(null);
            TestUtils.compare("The allocation procedure is not provided", moeadBuilder::getInstance);
            moeadBuilder.setAlloc(new Uniform());
            TestUtils.compare(null, moeadBuilder::getInstance);
        }
    }
}