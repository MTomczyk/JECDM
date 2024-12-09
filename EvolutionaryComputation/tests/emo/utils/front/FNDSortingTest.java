package emo.utils.front;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.Specimens;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Several tests for the {@link FNDSorting} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class FNDSortingTest
{
    /**
     * Test 1.
     */
    @Test
    void testFNDSorting1()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{7.0d, 1.0d}, {4.0d, 4.0d}, {3.0d, 3.0d}, {2.0d, 5.0d}, {1.0d, 4.0d}, {2.0d, 2.0d}, {6.0d, 2.0d}};

        ArrayList<Alternative> A = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            A.add(new Alternative("a" + i, e[i]));

        FNDSorting F = new FNDSorting(D);
        LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Alternatives(A));

        assertEquals(FA.size(), 3);

        assertTrue(FA.get(0).contains(0));
        assertTrue(FA.get(0).contains(4));
        assertTrue(FA.get(0).contains(5));

        assertTrue(FA.get(1).contains(2));
        assertTrue(FA.get(1).contains(3));
        assertTrue(FA.get(1).contains(6));

        assertTrue(FA.get(2).contains(1));

        // CHECK FRONT LEVEL
        Integer[] lv = FNDSorting.getFrontLevels(new Alternatives(A), FA);
        int[] exp = new int[]{0, 2, 1, 1, 0, 0, 1};
        assertEquals(exp.length, lv.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], lv[i]);
    }

    /**
     * Test 2.
     */
    @Test
    void testFNDSorting2()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{7.0d, 1.0d}, {4.0d, 4.0d}, {3.0d, 3.0d}, {2.0d, 5.0d}, {1.0d, 4.0d}, {2.0d, 2.0d}, {6.0d, 2.0d}};

        ArrayList<Alternative> A = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            A.add(new Alternative("a" + i, e[i]));

        FNDSorting F = new FNDSorting(D);
        LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Alternatives(A), 0);

        assertEquals(FA.size(), 0);

        /*assertEquals(3, FA.get(0).size());
        assertTrue(FA.get(0).contains(0));
        assertTrue(FA.get(0).contains(4));
        assertTrue(FA.get(0).contains(5));


        // CHECK FRONT LEVEL
        Integer[] lv = FNDSorting.getFrontLevels(new Alternatives(A), FA);
        Integer[] exp = new Integer[]{0, null, null, null, 0, 0, null};
        assertEquals(exp.length, lv.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], lv[i]);*/
    }

    /**
     * Test 3.
     */
    @Test
    void testFNDSorting3()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{7.0d, 1.0d}, {4.0d, 4.0d}, {3.0d, 3.0d}, {2.0d, 5.0d}, {1.0d, 4.0d}, {2.0d, 2.0d}, {6.0d, 2.0d}};

        ArrayList<Alternative> A = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            A.add(new Alternative("a" + i, e[i]));

        FNDSorting F = new FNDSorting(D);
        LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Alternatives(A), 4);

        assertEquals(FA.size(), 2);

        assertEquals(3, FA.get(0).size());
        assertTrue(FA.get(0).contains(0));
        assertTrue(FA.get(0).contains(4));
        assertTrue(FA.get(0).contains(5));

        assertEquals(3, FA.get(1).size());
        assertTrue(FA.get(1).contains(2));
        assertTrue(FA.get(1).contains(3));
        assertTrue(FA.get(1).contains(6));


        // CHECK FRONT LEVEL
        Integer[] lv = FNDSorting.getFrontLevels(new Alternatives(A), FA);
        Integer[] exp = new Integer[]{0, null, 1, 1, 0, 0, 1};
        assertEquals(exp.length, lv.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], lv[i]);
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{7.0d, 1.0d}, {4.0d, 4.0d}, {3.0d, 3.0d}, {2.0d, 5.0d}, {1.0d, 4.0d}, {2.0d, 2.0d}, {6.0d, 2.0d}};

        ArrayList<Alternative> A = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            A.add(new Alternative("a" + i, e[i]));

        FNDSorting F = new FNDSorting(D);
        LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Alternatives(A));

        assertEquals(FA.size(), 3);

        assertTrue(FA.get(0).contains(0));
        assertTrue(FA.get(0).contains(4));
        assertTrue(FA.get(0).contains(5));

        assertTrue(FA.get(1).contains(2));
        assertTrue(FA.get(1).contains(3));
        assertTrue(FA.get(1).contains(6));

        assertTrue(FA.get(2).contains(1));

        // CHECK FRONT LEVEL
        Integer[] lv = FNDSorting.getFrontLevels(new Alternatives(A), FA);
        int[] exp = new int[]{0, 2, 1, 1, 0, 0, 1};
        assertEquals(exp.length, lv.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], lv[i]);
    }

    /**
     * Test 5.
     */
    @Test
    public void test5()
    {
        Criteria C = Criteria.constructCriteria("C", 2, true);
        Dominance D = new Dominance(C);

        double[][] e = {{10.0d - 7.0d, 10.0d - 1.0d},
                {10.0d - 4.0d, 10.0d - 4.0d},
                {10.0d - 3.0d, 10.0d - 3.0d},
                {10.0d - 2.0d, 10.0d - 5.0d},
                {10.0d - 1.0d, 10.0d - 4.0d},
                {10.0d - 2.0d, 10.0d - 2.0d},
                {10.0d - 6.0d, 10.0d - 2.0d}};

        ArrayList<Alternative> A = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            A.add(new Alternative("a" + i, e[i]));

        FNDSorting F = new FNDSorting(D);
        LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Alternatives(A));

        assertEquals(FA.size(), 3);

        assertTrue(FA.get(0).contains(0));
        assertTrue(FA.get(0).contains(4));
        assertTrue(FA.get(0).contains(5));

        assertTrue(FA.get(1).contains(2));
        assertTrue(FA.get(1).contains(3));
        assertTrue(FA.get(1).contains(6));

        assertTrue(FA.get(2).contains(1));

        // CHECK FRONT LEVEL
        Integer[] lv = FNDSorting.getFrontLevels(new Alternatives(A), FA);
        int[] exp = new int[]{0, 2, 1, 1, 0, 0, 1};
        assertEquals(exp.length, lv.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], lv[i]);
    }

    /**
     * Test 6.
     */
    @Test
    public void test6()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{0.0d, 0.0d}, {1.0d, 0.0d}, {2.0d, 0.0d},
                {0.0d, 1.0d}, {1.0d, 1.0d}, {2.0d, 1.0d},
                {0.0d, 2.0d}, {1.0d, 2.0d}, {2.0d, 2.0d}};

        boolean[] nulled = new boolean[]{
                true, // 0
                true, // 1
                false, true, // 2 3
                false, false, true,
                false, true,
                true};

        int[] passedFrontsNo = new int[]{0, 1, 1, 2, 2, 2, 3, 3, 4, 5};
        int[] passedMembers = new int[]{0, 1, 1, 3, 3, 3, 6, 6, 8, 9};
        int[][] ambiguousFront = new int[][]{
                null,
                null,
                {1, 2},
                null,
                {3, 4, 5},
                {3, 4, 5},
                null,
                {6, 7},
                null,
                null,
        };

        for (int ps = 0; ps <= e.length; ps++)
        {
            ArrayList<Specimen> specimens = new ArrayList<>(e.length);
            for (double[] doubles : e) specimens.add(new Specimen(doubles));
            ArrayList<Specimen> newPopulation = new ArrayList<>(specimens.size());

            FNDSorting F = new FNDSorting(D);
            LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Specimens(specimens));
            assertEquals(5, FA.size());
            assertEquals(1, FA.get(0).size());
            assertEquals(2, FA.get(1).size());
            assertEquals(3, FA.get(2).size());
            assertEquals(2, FA.get(3).size());
            assertEquals(1, FA.get(4).size());

            FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation, specimens, FA, ps);
            assertEquals(nulled[ps], aFront == null);
            if (aFront != null)
            {
                assertEquals(passedFrontsNo[ps], aFront._passedFronts);
                assertEquals(passedMembers[ps], aFront._passedMembers);
                assertNotNull(ambiguousFront[ps]);
                assert ambiguousFront[ps] != null;
                assertEquals(ambiguousFront[ps].length, aFront._front.size());
            }
        }
    }


    /**
     * Test 7.
     */
    @Test
    public void test7()
    {
        Criteria C = Criteria.constructCriteria("C", 2, false);
        Dominance D = new Dominance(C);

        double[][] e = {{0.0d, 0.0d}, {1.0d, 0.0d}, {2.0d, 0.0d},
                {0.0d, 1.0d}, {1.0d, 1.0d}, {2.0d, 1.0d},
                {0.0d, 2.0d}, {1.0d, 2.0d}, {2.0d, 2.0d}};

        int[][][] passedMembers = new int[][][]
                {
                        {},// 0
                        {{0}},// 1
                        {{0}, {1, 3}}, //2
                        {{0}, {1, 3}}, //3

                        {{0}, {1, 3}, {6, 4, 2}}, //4
                        {{0}, {1, 3}, {6, 4, 2}}, //5
                        {{0}, {1, 3}, {6, 4, 2}}, //6

                        {{0}, {1, 3}, {2, 4, 6}, {5, 7}}, //7
                        {{0}, {1, 3}, {2, 4, 6}, {5, 7}}, //8
                        {{0}, {1, 3}, {2, 4, 6}, {5, 7}, {8}}, //9
                };


        for (int ps = 0; ps <= e.length; ps++)
        {
            System.out.println("PS = " + ps);
            ArrayList<Specimen> specimens = new ArrayList<>(e.length);
            for (double[] doubles : e) specimens.add(new Specimen(doubles));

            FNDSorting F = new FNDSorting(D);
            LinkedList<LinkedList<Integer>> FA = F.getFrontAssignments(new Specimens(specimens), ps);

            int[][] ref = passedMembers[ps];
            assertEquals(ref.length, FA.size());
            for (int i = 0; i < ref.length; i++)
            {
                HashSet<Integer> returned = new HashSet<>(FA.get(i));
                for (Integer j : ref[i]) assertTrue(returned.contains(j));
            }
        }
    }
}