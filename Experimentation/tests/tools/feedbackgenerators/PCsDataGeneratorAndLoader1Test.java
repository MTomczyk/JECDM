package tools.feedbackgenerators;

import exception.Exception;
import io.FileUtils;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import problem.moo.ReferencePointsFactory;
import random.MersenneTwister64;
import random.WeightsGenerator;
import space.Range;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Provides various tests for {@link PCsDataGenerator}.
 *
 * @author MTomczyk
 */
class PCsDataGeneratorAndLoader1Test
{
    /**
     * Validates the generator
     */
    @Test
    void test1()
    {
        {
            TestUtils.compare("No keys are provided (the array is null)", () -> new PCsDataGenerator(new PCsDataGenerator.Params()));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[0];
            TestUtils.compare("No keys are provided (the array is empty)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{null};
            TestUtils.compare("One of the provided keys is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "A"};
            TestUtils.compare("The key = A is not unique", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            TestUtils.compare("No values are provided (the array is null)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{};
            TestUtils.compare("The number of value-arrays does not equal the number of keys (0 vs 2)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1"}, null};
            TestUtils.compare("One of the provided value-arrays for a key = B is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1"}, new String[0]};
            TestUtils.compare("One of the provided value-arrays for a key = B is empty", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "1"}, new String[]{"X", "Y"}};
            TestUtils.compare("The value = 1 is not unique for a key = A", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            TestUtils.compare("The number of trials must be at least 1 (but equals 0)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            TestUtils.compare("The no. interactions provider is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            TestUtils.compare("The file path is null", () -> new PCsDataGenerator(p));
        }

        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._keysCommonForAlternatives = new String[]{"A"};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("Key (common to built alternatives) does not match a key at corresponding index = 2 (A vs C)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("The random number generator is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{null};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("One of the provided keys that are common to built alternatives is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"Z"};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("One of the provided keys that are common to built alternatives is does not exist in the primary keys array (requested key = Z)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"A", "A"};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("The key = A that is common to built alternatives is not unique", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"A"};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("Key (common to built alternatives) does not match a key at corresponding index = 1 (A vs B)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("The alternatives provider is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._filePath = "";
            TestUtils.compare("The artificial DM provider is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            TestUtils.compare("The artificial DM's model writer is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._modelWriter = new LNormWriterParser();
            p._filePath = "";
            TestUtils.compare("The reference alternatives selector is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            p._filePath = "";
            TestUtils.compare("The alternative writer is null", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativeWriter = new AlternativeWriterParser();
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            p._filePath = "";
            TestUtils.compare(null, () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"B"};
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._alternativeWriter = new AlternativeWriterParser();
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            TestUtils.compare(null, () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A_", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"B"};
            p._extraAllowedCharacters = null;
            p._alternativeWriter = new AlternativeWriterParser();
            p._R = new MersenneTwister64(0);
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            TestUtils.compare("Exception occurred when instantiating scenarios (message = The key = A_ contains forbidden characters)", () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A_", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"B"};
            p._extraAllowedCharacters = new Character[]{'_'};
            p._R = new MersenneTwister64(0);
            p._alternativeWriter = new AlternativeWriterParser();
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            TestUtils.compare(null, () -> new PCsDataGenerator(p));
        }
        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A_", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"B"};
            p._extraAllowedCharacters = new Character[]{'_'};
            p._R = new MersenneTwister64(0);
            p._alternativeWriter = new AlternativeWriterParser();
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            p._rangesProvider = new RangesProvider("M", Range::getDefaultRanges);
            TestUtils.compare("The ranges provider is set but the criteria provider is not (or vice versa)", () -> new PCsDataGenerator(p));
        }

        {
            PCsDataGenerator.Params p = new PCsDataGenerator.Params();
            p._keys = new String[]{"A_", "B"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}};
            p._keysCommonForAlternatives = new String[]{"B"};
            p._extraAllowedCharacters = new Character[]{'_'};
            p._R = new MersenneTwister64(0);
            p._alternativeWriter = new AlternativeWriterParser();
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._dmModelProvider = (scenario, t, normalizations, R) -> null;
            p._filePath = "";
            p._alternativesProvider = (scenario, noInteractions, R) -> new ArrayList<>();
            p._modelWriter = new LNormWriterParser();
            p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
            p._criteriaTypesProvider = new Cost("M");
            TestUtils.compare("The ranges provider is set but the criteria provider is not (or vice versa)", () -> new PCsDataGenerator(p));
        }
    }

    /**
     * Validates the generator
     */
    @Test
    void test2()
    {
        {
            TestUtils.compare("No keys are provided (the array is null)", () -> new PCsDataLoader(new PCsDataLoader.Params()));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[0];
            TestUtils.compare("No keys are provided (the array is empty)", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{null};
            TestUtils.compare("One of the provided keys is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "A"};
            TestUtils.compare("The key = A is not unique", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B"};
            TestUtils.compare("No values are provided (the array is null)", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{};
            TestUtils.compare("The number of value-arrays does not equal the number of keys (0 vs 2)", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1"}, null};
            TestUtils.compare("One of the provided value-arrays for a key = B is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1"}, new String[0]};
            TestUtils.compare("One of the provided value-arrays for a key = B is empty", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B"};
            p._values = new String[][]{{"1", "1"}, new String[]{"X", "Y"}};
            TestUtils.compare("The value = 1 is not unique for a key = A", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            TestUtils.compare("The number of trials must be at least 1 (but equals 0)", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            TestUtils.compare("The no. interactions provider is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            TestUtils.compare("The file path is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            TestUtils.compare("The artificial DM's model reader is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            p._modelReader = new LNormReaderParser();
            TestUtils.compare("The alternative reader is null", () -> new PCsDataLoader(p));
        }
        {
            PCsDataLoader.Params p = new PCsDataLoader.Params();
            p._keys = new String[]{"A", "B", "C"};
            p._values = new String[][]{{"1", "2"}, new String[]{"X", "Y"}, new String[]{"a", "b"}};
            p._trials = 1;
            p._noInteractionsProvider = new Constant(1);
            p._filePath = "";
            p._modelReader = new LNormReaderParser();
            p._alternativeReader = new AlternativeReaderParser();
            TestUtils.compare(null, () -> new PCsDataLoader(p));
        }
    }


    /**
     * Validates the model generation and loading procedure.
     */
    @Test
    void test3()
    {
        PCsDataGenerator.Params pG = new PCsDataGenerator.Params();
        pG._keys = new String[]{"SHAPE", "M", "DM",};
        pG._values = new String[][]
                {
                        {"convex", "CONCAVE"}, // will be converted to uppercase
                        {"2", "3", "4", "5"},
                        {"LINEAR", "CHEBYSHEV"},
                };
        pG._keysCommonForAlternatives = new String[]{"DM"};
        pG._trials = 5;
        //pG._notify = false;
        pG._noInteractionsProvider = new Constant(5);
        pG._R = new MersenneTwister64(0);
        pG._alternativesProvider = new FromShapeProvider("M", 100,
                (scenario, n, m, R) -> ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(n, m, R),
                (scenario, interaction, noInteractions, e) -> {
                    double rM = 1.5d - 0.5d * (interaction / (double) (noInteractions - 1));
                    for (double[] a : e)
                        for (int i = 0; i < a.length; i++) a[i] *= rM;
                });
        pG._rangesProvider = new RangesProvider("M", M -> Range.getDefaultRanges(M, 1.5d));
        pG._criteriaTypesProvider = new Cost("M");
        pG._dmModelProvider = new DMModelProvider<>("M",
                (scenario, t, M, normalizations, R) -> new LNorm(WeightsGenerator.getNormalizedWeightVector(M, R),
                        Double.POSITIVE_INFINITY, normalizations));
        pG._modelWriter = new LNormWriterParser();
        pG._refAlternativesSelector = new GammaIndicesForPCs(2.0d);
        pG._alternativeWriter = new AlternativeWriterParser();

        try
        {
            Path path = FileUtils.getPathRelatedToClass(this.getClass(), "Experimentation", "tests", File.separatorChar);
            pG._filePath = path.toString() + File.separatorChar + "results.txt";

            PCsDataGenerator DG = new PCsDataGenerator(pG);
            DG.process();
        } catch (Exception | IOException e)
        {
            throw new RuntimeException(e);
        }

        PCsDataLoader.Params pL = new PCsDataLoader.Params(pG);

        try
        {
            pL._modelReader = new LNormReaderParser();
            pL._alternativeReader = new AlternativeReaderParser();
            PCsDataLoader DL = new PCsDataLoader(pL);
            FeedbackData FD = DL.process();
            assertNotNull(FD);
            assertEquals(16, FD._scenariosData.size());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}