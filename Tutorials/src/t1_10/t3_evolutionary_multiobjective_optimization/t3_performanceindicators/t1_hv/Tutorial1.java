package t1_10.t3_evolutionary_multiobjective_optimization.t3_performanceindicators.t1_hv;


import criterion.Criteria;
import ea.dummy.populations.EADummyPopulations;
import exception.EAException;
import indicator.emo.HV;
import space.Range;
import space.normalization.INormalization;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

/**
 * Tutorial on the {@link indicator.emo.HV} class.
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Criteria criteria = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});

        // Bound for the first objective (to be maximized): [1, 2].
        Range b1 = new Range(1.0d, 2.0d);
        boolean d1 = true;

        // Bound for the second objective (to be minimized): [0, 4].
        Range b2 = new Range(0.0d, 4.0d);
        boolean d2 = false;

        // Objective space
        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{b1, b2}, new boolean[]{d1, d2});
        // Construct normalization builder:
        INormalizationBuilder nb = new StandardLinearBuilder();
        // Construct normalizations:
        INormalization[] normalizations = nb.getNormalizations(OS);

        // Print normalization data:
        for (INormalization n : normalizations) System.out.println(n.toString());

        // Create the data (in the original space)
        double[][] data = new double[][]
                {
                        //{2.0d, 0.0d}, //utopia, adding this point will set the HV to 100%
                        {1.5d, 2.0d},
                        {1.0d, 0.0d},
                        {2.0d, 4.0d},
                        {1.0d, 4.0d}, // dominated point
                };

        // Reference point used for HV calculation (nadir + normalized).
        double[] rp = new double[]{1.0d, 1.0d};
        //double [] rp = new double[] {1.1d, 1.1d};  // the RP can be extended to ensure that the extreme points will contribute to HV

        // Create the HV instance:
        HV hv = new HV(new HV.Params(2, normalizations, rp));

        // The indicator accepts an EA for the input (not double[][]). Hence, we can create a "dummy"
        // EA that wraps predefined populations (expressed as double[][][]).
        EADummyPopulations eaDummyPopulations = new EADummyPopulations(2, new double[][][]{data});

        // The init method is called to set the initial population:
        try
        {
            eaDummyPopulations.init();
        } catch (EAException e)
        {
            throw new RuntimeException(e);
        }

        // Calculate HV and print it:
        System.out.println("HV = " + hv.evaluate(eaDummyPopulations));
    }
}
