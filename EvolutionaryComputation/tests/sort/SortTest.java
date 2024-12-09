package sort;

import alternative.Alternative;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for the {@link sort.Sort} procedure.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class SortTest
{
    /**
     * Test 1.
     */
    @Test
    public void sortByAuxValue1()
    {
        double[] e = new double[]{2.0d, -1.0d, 5.0d, 7.0d, 22.0d, -1.5d, 1.5d};

        ArrayList<Specimen> S = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
        {
            Alternative A = new Alternative("A", 1);
            A.setAuxScore(e[i]);
            Specimen spec = new Specimen();
            spec.setID(new SpecimenID(0, 0, 0, i));
            spec.setAlternative(A);
            S.add(spec);
        }

        Sort.sortByAuxValue(S, true);
        for (int i = 0; i < 7; i++)
            System.out.println(S.get(i).getAlternative().getAuxScore());

        assertEquals(-1.5d, S.get(0).getAlternative().getAuxScore(), 0.001d);
        assertEquals(-1.0d, S.get(1).getAlternative().getAuxScore(), 0.001d);
        assertEquals(1.5d, S.get(2).getAlternative().getAuxScore(), 0.001d);
        assertEquals(2.0d, S.get(3).getAlternative().getAuxScore(), 0.001d);
        assertEquals(5.0d, S.get(4).getAlternative().getAuxScore(), 0.001d);
        assertEquals(7.0d, S.get(5).getAlternative().getAuxScore(), 0.001d);
        assertEquals(22.0d, S.get(6).getAlternative().getAuxScore(), 0.001d);

    }

    /**
     * Test 2.
     */
    @Test
    public void sortByAuxValue2()
    {
        double[] e = new double[]{2.0d, -1.0d, 5.0d, 7.0d, 22.0d, -1.5d, 1.5d};

        ArrayList<Specimen> S = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
        {
            Alternative A = new Alternative("A", 1);
            A.setAuxScore(e[i]);
            Specimen spec = new Specimen();
            spec.setID(new SpecimenID(0, 0, 0, i));
            spec.setAlternative(A);
            S.add(spec);
        }

        Sort.sortByAuxValue(S, false);
        for (int i = 0; i < 7; i++)
            System.out.println(S.get(i).getAlternative().getAuxScore());

        assertEquals(22.0d, S.get(0).getAlternative().getAuxScore(), 0.001d);
        assertEquals(7.0d, S.get(1).getAlternative().getAuxScore(), 0.001d);
        assertEquals(5.0d, S.get(2).getAlternative().getAuxScore(), 0.001d);
        assertEquals(2.0d, S.get(3).getAlternative().getAuxScore(), 0.001d);
        assertEquals(1.5d, S.get(4).getAlternative().getAuxScore(), 0.001d);
        assertEquals(-1.0d, S.get(5).getAlternative().getAuxScore(), 0.001d);
        assertEquals(-1.5d, S.get(6).getAlternative().getAuxScore(), 0.001d);

    }


    /**
     * Test 3.
     */
    @Test
    public void sortByAuxValue3()
    {
        double[] e = new double[]{2.0d, -1.0d, 5.0d, 7.0d, 22.0d, -1.5d, 1.5d};

        ArrayList<Specimen> S = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
        {
            Alternative A = new Alternative("A", 1);
            A.setPerformanceVector(new double[]{e[i]});
            Specimen spec = new Specimen();
            spec.setID(new SpecimenID(0, 0, 0, i));
            spec.setAlternative(A);
            S.add(spec);
        }

        Sort.sortByPerformanceValue(S, true);
        for (int i = 0; i < 7; i++)
            System.out.println(S.get(i).getAlternative().getPerformanceVector()[0]);

        assertEquals(-1.5d, S.get(0).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(-1.0d, S.get(1).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(1.5d, S.get(2).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(2.0d, S.get(3).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(5.0d, S.get(4).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(7.0d, S.get(5).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(22.0d, S.get(6).getAlternative().getPerformanceVector()[0], 0.001d);

    }

    /**
     * Test 4.
     */
    @Test
    public void sortByAuxValue4()
    {
        double[] e = new double[]{2.0d, -1.0d, 5.0d, 7.0d, 22.0d, -1.5d, 1.5d};

        ArrayList<Specimen> S = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
        {
            Alternative A = new Alternative("A", 1);
            A.setPerformanceVector(new double[]{e[i]});
            Specimen spec = new Specimen();
            spec.setID(new SpecimenID(0, 0, 0, i));
            spec.setAlternative(A);
            S.add(spec);
        }

        Sort.sortByPerformanceValue(S, false);
        for (int i = 0; i < 7; i++)
            System.out.println(S.get(i).getAlternative().getPerformanceVector()[0]);

        assertEquals(22.0d, S.get(0).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(7.0d, S.get(1).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(5.0d, S.get(2).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(2.0d, S.get(3).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(1.5d, S.get(4).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(-1.0d, S.get(5).getAlternative().getPerformanceVector()[0], 0.001d);
        assertEquals(-1.5d, S.get(6).getAlternative().getPerformanceVector()[0], 0.001d);

    }
}