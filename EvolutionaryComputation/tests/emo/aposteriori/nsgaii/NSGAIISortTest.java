package emo.aposteriori.nsgaii;

import alternative.Alternative;
import criterion.Criteria;
import ea.EA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import phase.PhaseReport;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Various tests for the {@link NSGAIISort} class.
 */
@SuppressWarnings("ExtractMethodRecommender")
class NSGAIISortTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);

        EA.Params pEA = new EA.Params("EA", C);
        pEA._populationSize = 6;
        pEA._offspringSize = 6;
        EA EA = new EA(pEA);

        double[][] e = new double[][]
                {
                        {4.0d, 2.0d},
                        {1.5d, 5.4d}, //2 + 10.0/10.0
                        {7.0d, 7.0d},
                        {1.0d, 1.0d}, //0.0
                        {5.0d, 5.0d},
                        {3.0d, 1.0d}, //1.0
                        {4.0d, 4.0d},
                        {1.0d, 3.0d}, //1.0
                        {6.0d, 6.0d},
                        {3.0d, 3.0d}, //2.0 + (1 -  2.0/3.0)
                        {5.4d, 1.5d}, //2 + 10.0 / 10.0
                        {2.0d, 4.0d},


                };

        ArrayList<Specimen> S = new ArrayList<>(12);
        for (int i = 0; i < 12; i++)
        {
            Specimen s = new Specimen(C._no, new SpecimenID(0, 0, 0, i));
            Alternative A = new Alternative("A" + i, e[i]);
            s.setAlternative(A);
            S.add(s);
        }

        EA.setSpecimensContainer(new SpecimensContainer(S));

        NSGAIISort sort = new NSGAIISort(C);
        String msg = null;
        try
        {
            sort.action(EA, new PhaseReport());
        } catch (PhaseException ex)
        {
            msg = ex.getMessage();
        }
        assertNull(msg);

        Set<Specimen> included = new HashSet<>();
        included.add(S.get(1));
        included.add(S.get(3));
        included.add(S.get(5));
        included.add(S.get(7));
        included.add(S.get(9));
        included.add(S.get(10));

        Set<Specimen> notincluded = new HashSet<>();
        notincluded.add(S.get(0));
        notincluded.add(S.get(2));
        notincluded.add(S.get(4));
        notincluded.add(S.get(6));
        notincluded.add(S.get(8));
        notincluded.add(S.get(11));

        assertEquals(6, EA.getSpecimensContainer().getPopulation().size());
        double[] scores = new double[]
                {0.0d, 1.0d, 1.0d, 2.0d, 2.0d, 2.0d + 1.0d / 3.0d};


        for (int s = 0; s < 6; s++)
        {
            Specimen spec = EA.getSpecimensContainer().getPopulation().get(s);
            assertTrue(included.contains(spec));
            assertEquals(scores[s], spec.getAlternative().getAuxScore(), 0.0001d);
        }
        for (Specimen s : notincluded) assertFalse(EA.getSpecimensContainer().getPopulation().contains(s));


    }
}