package population;

import org.junit.jupiter.api.Test;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Several tests for the {@link population.Specimen} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class SpecimenTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        Specimen s1 = new Specimen();
        {
            Chromosome c = new Chromosome();
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }
            s1.setChromosome(c);
            s1.setAlternative(null);
            s1.setID(new SpecimenID(0, 1, 2, 3));
        }

        Specimen s2 = s1.getClone();
        assertEquals(s1, s2);
        assertTrue(s1.isChromosomeEqual(s2, Constants.EPSILON));
    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        Specimen s1 = new Specimen();
        {
            Chromosome c = new Chromosome();
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }
            s1.setChromosome(c);
            s1.setAlternative(null);
            s1.setID(new SpecimenID(0, 1, 2, 3));
        }

        Specimen s2 = s1.getClone();
        s2.setID(new SpecimenID(60, 7, 7 , 9));
        assertNotEquals(s1, s2);
        assertTrue(s1.isChromosomeEqual(s2, Constants.EPSILON));
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        Specimen s1 = new Specimen();
        {
            Chromosome c = new Chromosome();
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }
            s1.setChromosome(c);
            s1.setAlternative(null);
            s1.setID(new SpecimenID(0, 1, 2, 3));
        }

        Specimen s2 = s1.getClone();
        s2.getChromosome()._genes[0]._dv = new double[]{99.0d,88.0d};

        assertEquals(s1, s2);
        assertFalse(s1.isChromosomeEqual(s2, Constants.EPSILON));
    }
}