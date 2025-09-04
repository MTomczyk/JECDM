package t11_20.t12_generating_reference_pcs;

import condition.ScenarioDisablingConditions;
import exception.Exception;
import io.FileUtils;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import problem.moo.ReferencePointsFactory;
import random.MersenneTwister64;
import random.WeightsGenerator;
import space.Range;
import tools.feedbackgenerators.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This tutorial showcases how to employ {@link tools.feedbackgenerators.PCsDataGenerator} tool to pre-construct data
 * on artificial DMs and their feedback, contextually to specified experimental scenarios. This can be viewed as
 * a supplement to the publication on <a href="https://papers.ssrn.com/sol3/papers.cfm?abstract_id=5415565">this
 * paper</a> (preprint; TODO to be updated hen published).
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // First, let's instantiate the params container:
        PCsDataGenerator.Params p = new PCsDataGenerator.Params();

        // Now, let's specify the context (equivalent to defining experimental scenarios when designing experiments);
        p._keys = new String[]{
                "SHAPE", // Assume that the reference alternatives for comparisons will be drawn from various Pareto fronts (shapes)
                "M", // Also, assume that the dimensionality may vary (denoted by M).
                "DM" // Finally, assume that the DM's artificial model may vary
        };

        // Below, example realizations of the above keys can be found:
        p._values = new String[][]
                {
                        {"CONVEX", "concave"}, // note that the key and value will be converted to uppercase
                        {"2", "3", "4", "5"},
                        {"WSM", "CHEBYSHEV"} // WSM = weighted linear model; Chebyshev = weighted Chebyshev model
                };

        // The tool allows specifying keys that will share the same alternative sets constructed and processed by the
        // tool. For instance, the "DM" key does not affect the solution space anyway; thus, the alternatives can be
        // shared for all realizations of this key. It will boost the processing efficiency. IMPORTANT NOTE:
        // the "common" keys must be specified as the last ones in the "_keys" param.
        p._keysCommonForAlternatives = new String[]{"DM"};

        // Similarly to the experimentation module (designing experiments), the Cartesian product of all key values
        // defines all experimental settings. One may, however, easily disable (exclude) some of them. In the context
        // of this tutorial, there is no reason to apply a "DM=WSM" to concave Pareto fronts (as such DM should always
        // point to extreme solutions). The below line disables all such settings:
        p._scenarioDisablingConditions = new ScenarioDisablingConditions(new String[]{"SHAPE", "DM"},
                new String[]{"CONCAVE", "CHEBYSHEV"});

        p._trials = 100; // Set number of test repeats within a scenario to 100
        int interactions = 10; // Consider the number of interactions equal 10

        // The below field allows specifying the number of interactions associated with a scenario being processed
        // (see the "INoInteractionsProvider" interface). This example assumes a constant number of interactions for
        // each scenario (and its test run).
        p._noInteractionsProvider = new Constant(interactions);

        // As with the experimentation module, not all characters are allowed in the key/values labels. The below field
        // allows specifying additional allowed characters.
        p._extraAllowedCharacters = new Character[]{'_', '-'};

        // Let's specify a random number generator to be used by this tool:
        p._R = new MersenneTwister64(0);

        // The below parameter is expected to be supplied with the implementation of "IAlternativesProvider," which is
        // supposed to return alternatives sets (array of array of alternatives) contextually to the (i) scenario being
        // processed. The first dimension (array) refers to different interaction numbers (index of 0 refers to the first
        // interaction, of 1 to the second, etc.); the second dimension represents associated alternatives. In this
        // example, a straightforward implementation named "FromShapeProvider" is used. It allows for defining
        // the reference shape (alternatives, defined as a matrix of their performances: double [][]) and a rescaling
        // function that allows for rescaling the reference shape given the interaction number, which may be employed
        // to account for convergence towards the PF in subsequent interactions.
        p._alternativesProvider = new FromShapeProvider("M", 100000,
                (scenario, n, m, R) -> { // Should return a reference performance matrix given the "SHAPE" of
                    // the scenario being currently processed
                    if (scenario.getKeyValuesMap().get("SHAPE").getValue().equals("CONVEX"))
                        return ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(n, m, R);
                    else return ReferencePointsFactory.getUniformRandomRPsOnConcaveSphere(n, m, R);
                },
                // The below lines define how the reference shape is rescaled given the interaction number
                // (here, a simple linear interpolation from 2.0 (the first interaction) to 1.0 (the last interaction)
                // is employed).
                (scenario, interaction, noInteractions, e) -> {
                    double rM = 2.0d - (interaction / (double) (noInteractions - 1));
                    if (scenario.getKeyValuesMap().get("SHAPE").getValue().equals("CONVEX"))
                        for (double[] a : e) for (int i = 0; i < a.length; i++) a[i] *= rM;
                    else
                        for (double[] a : e)
                            for (int i = 0; i < a.length; i++) a[i] = ((a[i] - 1.0d) * rM) + rM;
                });

        // The range provider may be specified to define how a solution space is bounded for a scenario being processed.
        // Here, it is specified that the solution space is bounded by the [0, 2]^M hypercube. These ranges are employed
        // to define the normalization functions that can be used, e.g., to normalize alternatives' performance vectors
        // when being evaluated by the artificial DM.
        p._rangesProvider = new RangesProvider("M", M -> Range.getDefaultRanges(M, 2.0d));

        // The field below should be instantiated with an object responsible for delivering criteria (objectives) types.
        // Here, a boolean["M"] vector is returned (instantiated with false, by default, which indicates criteria to be
        // minimized).
        p._criteriaTypesProvider = new Cost("M");

        // // The below field must be instantiated with an implementation of the "IDMModelProvider" interface,
        // responsible for defining the internal model of an artificial DM, given the scenario being currently processed.
        // The tutorial uses a default "DMModelProvider" implementation.
        p._dmModelProvider = new DMModelProvider<>("M", (DMModelProvider.IInternalModel<AbstractValueInternalModel>)
                (scenario, t, M, normalizations, R) -> {
                    // Let's set the alpha value for the L-norm (contextual to the "DM" key).
                    double alpha = 1.0d; // Linear model
                    if (scenario.getKeyValuesMap().get("DM").getValue().equals("CHEBYSHEV"))
                        alpha = Double.POSITIVE_INFINITY; // Chebyshev model

                    // Return a suitably parameterized L-norm
                    return new LNorm(WeightsGenerator.getNormalizedWeightVector(M, R),
                            alpha, normalizations);
                });

        // The model writer (parser) must be specified. The code uses a default LNormWriterParser, dedicated to L-norms.
        p._modelWriter = new LNormWriterParser();

        // The below field must be supplied with an implementation of "IReferenceAlternativesSelector," which is supposed
        // to return indices pointing to alternatives that the DM must compare to form pairwise comparisons. This tutorial
        // uses GammaIndicesForPCs, which favors better alternatives (in the view of the oracle, i.e., artificial DM)
        // the later the interaction is (see the associated article for details).
        p._refAlternativesSelector = new GammaIndicesForPCs(2.0d);

        // The alternatives writer (parser) must be specified. The code uses a default AlternativeWriterParser.
        p._alternativeWriter = new AlternativeWriterParser();

        try
        {
            // Let's specify that the resulting data will be stored in this package in the "PCs.txt" file.
            Path path = FileUtils.getPathRelatedToClass(Tutorial1.class, "Tutorials", "src", File.separatorChar);
            p._filePath = path.toString() + File.separatorChar + "PCs.txt";
            // Let's create the tool instance and run it.
            PCsDataGenerator DG = new PCsDataGenerator(p);
            DG.process();
        } catch (Exception | IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
