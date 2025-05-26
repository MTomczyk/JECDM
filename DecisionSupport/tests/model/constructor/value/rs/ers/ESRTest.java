package model.constructor.value.rs.ers;

import alternative.Alternative;
import dmcontext.DMContext;
import exeption.ConstructorException;
import exeption.HistoryException;
import history.History;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.iterationslimit.Constant;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link ERS}.
 *
 * @author MTomczyk
 */
class ESRTest
{

    /**
     * Test 1 (Chebyshev model).
     */
    @Test
    void mainConstructModels1()
    {
        int toGenerate = 100;
        int[] it = new int[]{0, toGenerate - 1, toGenerate};
        boolean[] inc = new boolean[]{false, false, true};
        IRandom R = new MersenneTwister64(0);

        for (int i = 0; i < 3; i++)
        {
            IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
            ERS.Params<LNorm> params = new ERS.Params<>(RM);
            params._similarity = new Euclidean();
            params._kMostSimilarNeighbors = 3;
            params._comparator = new MostSimilarWithTieResolving<>();
            params._feasibleSamplesToGenerate = toGenerate;
            params._iterationsLimit = new Constant(100000);
            params._inconsistencyThreshold = it[i];
            params._validateAlreadyExistingSamplesFirst = false;
            ERS<LNorm> frs = new ERS<>(params);

            String msg = null;
            try
            {
                frs.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0, null, R));
            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            LinkedList<IPreferenceInformation> PI = new LinkedList<>();
            PI.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{0.5d, 0.5d}),
                    new Alternative("A2", new double[]{0.0d, 1.0d})));
            PI.add(PairwiseComparison.getPreference(new Alternative("A3", new double[]{0.5d, 0.5d}),
                    new Alternative("A4", new double[]{1.0d, 0.0d})));

            try
            {
                History history = new History("H");
                history.registerPreferenceInformation(PI, 0, false);

                Report<LNorm> m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertEquals(toGenerate, m._models.size());
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.0d, m._successRateInPreserving, 1.0E-7);
                assertEquals(0, m._modelsPreservedBetweenIterations);
                assertEquals(0, m._modelsRejectedBetweenIterations);
                assertEquals(1.0d / 3.0d, m._successRateInConstructing, 1.0E-1);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-1);

                for (LNorm lNorm : m._models)
                {
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
                }

            } catch (HistoryException | ConstructorException e)
            {
                msg = e.toString();
            }
            assertNull(msg);
        }
    }


    /**
     * Test 2 (Chebyshev model).
     */
    @Test
    void mainConstructModels2()
    {
        int toGenerate = 100;
        int[] it = new int[]{0, toGenerate - 1, toGenerate};
        boolean[] inc = new boolean[]{false, false, true};

        IRandom R = new MersenneTwister64(0);

        for (int i = 0; i < 3; i++)
        {
            IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
            ERS.Params<LNorm> params = new ERS.Params<>(RM);
            params._similarity = new Euclidean();
            params._kMostSimilarNeighbors = 3;
            params._comparator = new MostSimilarWithTieResolving<>();
            params._feasibleSamplesToGenerate = toGenerate;
            params._iterationsLimit =  new Constant(100000);
            params._inconsistencyThreshold = it[i];
            params._validateAlreadyExistingSamplesFirst = false;
            ERS<LNorm> frs = new ERS<>(params);

            String msg = null;
            try
            {
                frs.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0, null, R));
            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            LinkedList<IPreferenceInformation> PI = new LinkedList<>();
            PI.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{0.5d, 0.5d}),
                    new Alternative("A2", new double[]{0.0d, 1.0d})));

            try
            {
                History history = new History("H");
                history.registerPreferenceInformation(PI, 0, false);

                Report<LNorm> m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertEquals(toGenerate, m._models.size());
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.0d, m._successRateInPreserving, 1.0E-7);
                assertEquals(0, m._modelsPreservedBetweenIterations);
                assertEquals(0, m._modelsRejectedBetweenIterations);
                assertEquals(2.0d / 3.0d, m._successRateInConstructing, 1.0E-1);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertEquals(2.0d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-1);


                for (LNorm lNorm : m._models)
                {
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0));
                }

                history.registerPreferenceInformation(PairwiseComparison.getPreference(new Alternative("A3", new double[]{0.5d, 0.5d}),
                        new Alternative("A4", new double[]{1.0d, 0.0d})), 0, false);

                m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertEquals(toGenerate, m._models.size());
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.0d, m._successRateInPreserving, 1.0E-7);
                assertEquals(0, m._modelsPreservedBetweenIterations);
                assertEquals(0, m._modelsRejectedBetweenIterations);
                assertEquals(1.0d / 3.0d, m._successRateInConstructing, 1.0E-2);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-2);


                for (LNorm lNorm : m._models)
                {
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
                }

            } catch (HistoryException | ConstructorException e)
            {
                msg = e.toString();
            }

            assertNull(msg);
        }
    }


    /**
     * Test 3  (Chebyshev model).
     */
    @Test
    void mainConstructModels3()
    {
        int toGenerate = 100;
        int[] it = new int[]{0, toGenerate - 1, toGenerate};
        boolean[] inc = new boolean[]{false, false, true};

        IRandom R = new MersenneTwister64(0);

        for (int i = 0; i < 3; i++)
        {
            IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
            ERS.Params<LNorm> params = new ERS.Params<>(RM);
            params._similarity = new Euclidean();
            params._kMostSimilarNeighbors = 3;
            params._comparator = new MostSimilarWithTieResolving<>();
            params._feasibleSamplesToGenerate = toGenerate;
            params._iterationsLimit =  new Constant(100000);
            params._inconsistencyThreshold = it[i];
            params._validateAlreadyExistingSamplesFirst = true;
            ERS<LNorm> frs = new ERS<>(params);

            String msg = null;
            try
            {
                frs.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0, null, R));
            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            LinkedList<IPreferenceInformation> PI = new LinkedList<>();
            PI.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{0.5d, 0.5d}),
                    new Alternative("A2", new double[]{0.0d, 1.0d})));

            try
            {
                History history = new History("H");
                history.registerPreferenceInformation(PI, 0, false);

                Report<LNorm> m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertEquals(toGenerate, m._models.size());
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.0d, m._successRateInPreserving, 1.0E-7);
                assertEquals(0, m._modelsPreservedBetweenIterations);
                assertEquals(0, m._modelsRejectedBetweenIterations);
                assertEquals(2.0d / 3.0d, m._successRateInConstructing, 1.0E-1);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertEquals(2.0d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-1);


                for (LNorm lNorm : m._models)
                {
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0));
                }

                history.registerPreferenceInformation(PairwiseComparison.getPreference(new Alternative("A3", new double[]{0.5d, 0.5d}),
                        new Alternative("A4", new double[]{1.0d, 0.0d})), 0, false);

                m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.5d, m._successRateInPreserving, 1.0E-1);
                assertEquals(0.5d, (double) m._modelsPreservedBetweenIterations / toGenerate, 1.0E-1);
                assertEquals(0.0d, (double) m._modelsRejectedBetweenIterations / toGenerate, 1.0E-1);

                assertEquals(1.0d / 3.0d, m._successRateInConstructing, 1.0E-1);
                assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-1);

                for (LNorm lNorm : m._models)
                {
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                    assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
                }


            } catch (HistoryException | ConstructorException e)
            {
                msg = e.toString();
            }
            assertNull(msg);
        }
    }


    /**
     * Test 5.
     */
    @Test
    void mainConstructModels5()
    {
        int toGenerate = 100;
        int[] it = new int[]{0, toGenerate - 1, toGenerate};
        boolean[] inc = new boolean[]{false, false, true};

        IRandom R = new MersenneTwister64(0);

        for (int i = 0; i < 3; i++)
        {

            IRandomModel<LNorm> RM = new LNormGenerator(2, 1.0d);
            ERS.Params<LNorm> params = new ERS.Params<>(RM);
            params._similarity = new Euclidean();
            params._kMostSimilarNeighbors = 3;
            params._comparator = new MostSimilarWithTieResolving<>();
            params._feasibleSamplesToGenerate = toGenerate;
            params._iterationsLimit =  new Constant(100000);
            params._inconsistencyThreshold = it[i];
            params._validateAlreadyExistingSamplesFirst = true;
            ERS<LNorm> frs = new ERS<>(params);

            String msg = null;
            try
            {
                frs.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0, null, R));
            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            LinkedList<IPreferenceInformation> PI = new LinkedList<>();
            PI.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{1.0d / 3.0d, 1.0d / 3.0d}),
                    new Alternative("A2", new double[]{0.0d, 1.0d})));
            PI.add(PairwiseComparison.getPreference(new Alternative("A3", new double[]{1.0d / 3.0d, 1.0d / 3.0d}),
                    new Alternative("A4", new double[]{1.0d, 0.0d})));

            try
            {
                History history = new History("H");
                history.registerPreferenceInformation(PI, 0, false);

                Report<LNorm> m = frs.constructModels(history.getPreferenceInformationCopy());
                assertEquals(inc[i], m._inconsistencyDetected);
                assertNotNull(m._models);
                assertEquals(toGenerate, m._models.size());
                assertTrue(m._constructionElapsedTime > 0);
                assertEquals(0.0d, m._successRateInPreserving, 1.0E-7);
                assertEquals(0, m._modelsPreservedBetweenIterations);
                assertEquals(0, m._modelsRejectedBetweenIterations);
                assertEquals(1.0d / 3.0d, m._successRateInConstructing, 1.0E-1);
                assertTrue(toGenerate <= m._acceptedNewlyConstructedModels);
                assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-1);

                for (LNorm lNorm : m._models)
                {
                    assertEquals(1.0d, lNorm.getLNorm().getAuxParam());
                    assertEquals(1.0d, lNorm.getAuxParam());

                    double[] w = lNorm.getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

                    w = lNorm.getLNorm().getWeights();
                    assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                    assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
                }

            } catch (HistoryException | ConstructorException e)
            {
                msg = e.toString();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 6 (Chebyshev model).
     */
    @Test
    void mainConstructModels6()
    {
        int toGenerate = 100;

        IRandom R = new MersenneTwister64(0);
        IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        FRS.Params<LNorm> params = new FRS.Params<>(RM);
        params._feasibleSamplesToGenerate = toGenerate;
        params._samplingLimit = 100000;
        params._inconsistencyThreshold = 1;
        params._validateAlreadyExistingSamplesFirst = false;
        FRS<LNorm> frs = new FRS<>(params);

        String msg = null;
        try
        {
            frs.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0, null, R));
        } catch (ConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        LinkedList<IPreferenceInformation> PI = new LinkedList<>();
        PI.add(PairwiseComparison.getPreference(new Alternative("A1", new double[]{0.5d, 0.5d}),
                new Alternative("A2", new double[]{0.0d, 1.0d})));
        PI.add(PairwiseComparison.getPreference(new Alternative("A3", new double[]{0.5d, 0.5d}),
                new Alternative("A4", new double[]{1.0d, 0.0d})));

        try
        {
            History history = new History("H");
            history.registerPreferenceInformation(PI, 0, false);

            frs.constructModels(null);

        } catch (HistoryException | ConstructorException e)
        {
            msg = e.toString();
        }
        assertEquals("Exception handled by: model.constructor.value.rs.frs.FRS, caused by: null, happened in line: null, message: The preference examples are not provided (the list is null)", msg);
    }
}