package t1_10.t3_evolutionary_multiobjective_optimization.t1_normalizations.t1;

import print.PrintUtils;
import space.Range;
import space.normalization.INormalization;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

/**
 * Tutorial on the {@link ObjectiveSpace} class and {@link INormalizationBuilder} interface.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"ExtractMethodRecommender", "DuplicatedCode"})
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create known ranges for the OS manager (each associated with a different dimension, 1:1 mapping).
        // Dimension #1 = [0, 1]
        // Dimension #2 = [2, 4]
        Range[] ranges = new Range[]{
                Range.getNormalRange(),
                new Range(2.0d, 4.0)
        };

        // Criteria type = preference/optimization directions; false = to be minimized; true = to be maximized.
        boolean[] criteriaTypes = new boolean[]{false, false};

        // Consequently the utopia point = [0, 2], while the nadir point = [1, 4].

        // Create the initial objective space object
        ObjectiveSpace os = new ObjectiveSpace(ranges, criteriaTypes);

        System.out.println("Printing information on the objective space:");
        System.out.println(os);

        // Create the normalization builder object (standard min/max normalization)
        INormalizationBuilder normalizationBuilder = new StandardLinearBuilder();

        // Create the normalization objects (one per each dimension)
        INormalization[] normalizations = normalizationBuilder.getNormalizations(os);

        System.out.println("The number of normalization objects = " + normalizations.length);
        for (INormalization normalization : normalizations) System.out.println(normalization.toString());
        System.out.println();

        // Test data
        double[][] testData = new double[][]{
                {0.0d, 2.0d}, // utopia -> will map into 0.0d and 0.0d
                {0.25d, 2.5d}, // will map into 0.25d, 0.25d
                {0.5d, 3.0d}, // will map into 0.5d, 0.5d
                {0.75d, 3.5d}, // will map into 0.75d, 0.75d
                {1.0d, 4.0d}, // nadir -> will map into 1.0d, 1.0d
                {2.0d, 6.0d}, // outside the bounds -> will map into 2.0d, 2.0d
        };


        // Iterate and print normalization results
        for (double[] v : testData)
        {
            System.out.println("Case = " + PrintUtils.getVectorOfDoubles(v, 2));
            for (int n = 0; n < normalizations.length; n++)
            {
                double normalized = normalizations[n].getNormalized(v[n]);
                System.out.println("Normalization object #" + n + " mapped " + v[n] + " into " + normalized);
            }
        }

    }
}
