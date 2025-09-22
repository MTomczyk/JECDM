package selection;

import ea.EA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import population.Parents;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.ReproductionStrategy;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Provides various tests for {@link Random}.
 *
 * @author MTomczyk
 */
class RandomTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ReproductionStrategy[] RS = new ReproductionStrategy[8];
        RS[1] = ReproductionStrategy.getDefaultStrategy();
        RS[2] = new ReproductionStrategy(new ReproductionStrategy.Params(2));
        RS[3] = new ReproductionStrategy(new ReproductionStrategy.Params(2));
        RS[4] = new ReproductionStrategy(new ReproductionStrategy.Params(3));
        RS[5] = new ReproductionStrategy(new ReproductionStrategy.Params(3));
        RS[6] = ReproductionStrategy.getDynamicStrategy((ea, counter, noExpectedOffspringGenerated) ->
                1 + ea.getR().nextInt(3));
        {
            ReproductionStrategy.Params pRS = new ReproductionStrategy.Params();
            pRS._enableOffspringThresholding = false;
            pRS._isReproductionStrategyConstant = false;
            pRS._noOffspringFromParentsGenerator = (ea, counter, noExpectedOffspringGenerated) -> {
                if (counter == 0) return 5;
                if (counter == 1) return 2;
                if (counter == 2) return 2;
                return 5;
            };
            RS[7] = new ReproductionStrategy(pRS);
        }


        int[] os = new int[]{5, 5, 5, 6, 8, 12, 10, 10};
        String[] msgs = new String[]{
                null,
                null,
                "It is expected to generate 2 offspring solutions from one Parents object, but it is not a " +
                        "divisor of the expected total offspring size of 5 (reproduction limit = 2147483647; capped to 5)," +
                        " and offspring thresholding is set to disabled.",
                null,
                "It is expected to generate 3 offspring solutions from one Parents object, but it is not a " +
                        "divisor of the expected total offspring size of 8 (reproduction limit = 2147483647; capped to 8), " +
                        "and offspring thresholding is set to disabled.",
                null,
                null,
                "The dynamic generation of the expected number of offspring to produce indicates the number of 5. When " +
                        "added to the already generated numbers (9), it would exceed the expected total offspring size " +
                        "of 10 (the offspring thresholding is set to disabled; the true offspring size is 10; " +
                        "reproduction limit = 2147483647; capped to 10)."
        };
        Integer[] ps = new Integer[]{5, 5, null, 3, null, 4, null, null};
        Integer[] total = new Integer[]{100000, 100000, null, 60000, null, 80000, null, null};

        for (int i = 0; i < RS.length; i++)
        {
            IRandom R = new MersenneTwister64(System.currentTimeMillis());
            EA.Params pEA = new EA.Params("", null);
            pEA._populationSize = 3;
            pEA._offspringSize = os[i];
            pEA._R = R;
            pEA._id = 0;
            pEA._reproductionStrategy = RS[i];
            EA ea = new EA(pEA);
            int T = 10000;
            ISelect S = new Random(2);
            ArrayList<Specimen> specimen = new ArrayList<>();
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 1)));
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 2)));
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setMatingPool(specimen);
            int[] hist = new int[3];

            String msg = null;
            for (int t = 0; t < T; t++)
            {
                ArrayList<Parents> parents = null;
                try
                {
                    parents = S.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }

                assertEquals(msgs[i], msg);

                if (msg == null)
                {
                    assertNotNull(parents);
                    if (ps[i] != null) assertEquals(ps[i], parents.size());
                    int ts = 0;
                    for (Parents p : parents)
                    {
                        assertNotNull(p._parents);
                        assertEquals(2, p._parents.size());
                        for (Specimen s : p._parents)
                            hist[s.getID()._no]++;

                        if (RS[i] == null) assertEquals(1, p._noOffspringToConstruct);
                        else if (RS[i].isReproductionStrategyConstant())
                            assertEquals(RS[i].getConstantNoOffspringFromParents(), p._noOffspringToConstruct);

                        ts += p._noOffspringToConstruct;
                    }
                    assertEquals(pEA._offspringSize, ts);
                } else break;
            }

            if (msg == null)
            {
                if ((RS[i] != null) && (RS[i].isReproductionStrategyConstant()))
                {
                    assertEquals(total[i], hist[0] + hist[1] + hist[2]);
                    assertEquals(1.0d / 3.0d, (double) hist[0] / total[i], 1.0E-2);
                    assertEquals(1.0d / 3.0d, (double) hist[1] / total[i], 1.0E-2);
                    assertEquals(1.0d / 3.0d, (double) hist[2] / total[i], 1.0E-2);
                }
            }
        }
    }
}