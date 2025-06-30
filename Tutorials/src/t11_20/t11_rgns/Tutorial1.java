package t11_20.t11_rgns;

import print.PrintUtils;
import random.IRandom;
import random.L32_X64_MIX;
import random.MersenneTwister64;

import java.util.stream.Stream;

/**
 * This tutorial briefly showcases new RNG-related functionalities
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        { // Using 64-bit Mersenne Twister generator
            System.out.println("MersenneTwister64");
            IRandom R = new MersenneTwister64(0); // Construct an RNG
            // R = new MersenneTwister64(new long[]{0, 0, 0}); // multiple seeds can be provided; see the java doc for the native seed size
            System.out.println("  Is splittable = " + R.isSplittable());
            System.out.println("  Is jumpable = " + R.isJumpable());

            Stream<IRandom> newRNGs = R.createSplitInstances(10); // creates instances via split (only if supported by the wrapped RNG)
            System.out.println("  " + newRNGs); // is null, as MersenneTwister64 is not splittable
            //IRandom newRNG = R.createSplitInstance(); // or create just one instance; still null in this case

            newRNGs = R.createSplitInstances(10); // creates instances via split (only if supported by the wrapped RNG)
            // IRandom newRNG = R.createSplitInstance(); // or create just one instance
            System.out.println("  " + newRNGs); // is null, as MersenneTwister64 is not splittable
            //IRandom newRNG = R.createInstanceViaJump(); // or create just one instance; still null in this case
        }
        {
            // Using L32_X64_MIX generator
            System.out.println("L32_X64_MIX");
            IRandom R = new L32_X64_MIX(0); // Construct an RNG
            System.out.println("  Is splittable = " + R.isSplittable());
            System.out.println("  Is jumpable = " + R.isJumpable());

            Stream<IRandom> newRNGs = R.createSplitInstances(10);
            int instance = 0;
            System.out.println("  Split case:");
            for (IRandom r : newRNGs.toList())
            {
                double[] d = r.nextDoubles(10);
                System.out.println("    Random doubles for instance = " + (++instance) + " : "
                        + PrintUtils.getVectorOfDoubles(d, 4));
            }
            newRNGs = R.createInstancesViaJumps(10);
            instance = 0;
            System.out.println("  Jump case:");
            for (IRandom r : newRNGs.toList())
            {
                double[] d = r.nextDoubles(10);
                System.out.println("    Random doubles for instance = " + (++instance) + " : "
                        + PrintUtils.getVectorOfDoubles(d, 4));
            }
        }
    }
}
