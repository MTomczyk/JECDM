package emo.utils.front;

import criterion.Criteria;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains various test for the {@link Front} class.
 *
 * @author MTomczyk
 */
class FrontTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        Specimen S = new Specimen(new double[]{1.0d, 1.0d});
        ArrayList<Specimen> specimens = new ArrayList<>();
        specimens.add(S);
        Dominance D = new Dominance(Criteria.constructCriteria("C", 2, false));
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        assertEquals(1, nds.size());
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        double[][] e = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Dominance D = new Dominance(Criteria.constructCriteria("C", 2, false));
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 3)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        double[][] e = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Dominance D = new Dominance(Criteria.constructCriteria("C", 2, true));
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 0)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        double[][] e = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Criteria C = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});
        Dominance D = new Dominance(C);
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 4)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        double[][] e = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Criteria C = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{false, true});
        Dominance D = new Dominance(C);
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 5)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        double[][] e = new double[][]
                {
                        {4.0d, 4.0d},
                        {4.0d, 4.0d},
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Criteria C = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{false, true});
        Dominance D = new Dominance(C);
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 1)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        double[][] e = new double[][]
                {
                        {1.0d, 4.0d},
                        {2.0d, 2.0d},
                        {4.0d, 1.0d},
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 1)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 2)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        double[][] e = new double[][]
                {
                        {2.0d, 2.0d, 2.0d},
                        {0.0d, 1.0d, 3.0d},
                        {3.0d, 1.0d, 0.0d},
                        {1.0d, 1.0d, 1.0d},
                };

        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < e.length; i++)
            specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), e[i]));
        Criteria C = Criteria.constructCriteria("C", 3, false);
        Dominance D = new Dominance(C);
        Front F = new Front(D);
        LinkedList<Specimen> nds = F.getNonDominatedSpecimens(specimens);
        LinkedList<Specimen> expected = new LinkedList<>();
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 1)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 2)));
        expected.add(new Specimen(new SpecimenID(0, 0, 0, 3)));

        assertEquals(expected.size(), nds.size());
        for (Specimen s : expected) assertTrue(nds.contains(s));
    }
}