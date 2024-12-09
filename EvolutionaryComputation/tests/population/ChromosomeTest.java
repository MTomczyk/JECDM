package population;

import org.junit.jupiter.api.Test;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several tests for the {@link population.Chromosome} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class ChromosomeTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        Chromosome c = new Chromosome();
        Gene G1 = new Gene();
        G1._dv = new double[]{0.12d, 0.56d, 0.43d};
        c._genes = new Gene[]{G1};
        c.print();
    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        Chromosome c1 = new Chromosome();

        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c1._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }
        }

        c1.print();

        Chromosome c2 = new Chromosome();

        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c2._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }

        }

        assertTrue(c1.isEqual(c2, Constants.EPSILON));
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        Chromosome c1 = new Chromosome();

        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c1._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }
        }

        c1.print();

        Chromosome c2 = new Chromosome();

        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.44d};
            c2._genes = new Gene[]{G1};

            {
                Gene G2 = new Gene();
                G2._dv = new double[]{0.1d, 0.2d};
                G2._level = 1;
                Gene G3 = new Gene();
                G3._iv = new int[]{1, 2, 3, 60};
                G3._level = 1;
                G1._gene = new Gene[]{G2, G3};
            }

        }

        assertFalse(c1.isEqual(c2, Constants.EPSILON));
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();

        assertTrue(c1.isEqual(c2, Constants.EPSILON));
    }

    /**
     * Test 4.
     */
    @Test
    public void test5()
    {
        Chromosome c1 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c1._genes = new Gene[]{G1};
        }
        Chromosome c2 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c2._genes = new Gene[]{G1};
        }
        assertTrue(c1.isEqual(c2, Constants.EPSILON));
        assertTrue(c2.isEqual(c1, Constants.EPSILON));
    }

    /**
     * Test 5.
     */
    @Test
    public void test6()
    {
        Chromosome c1 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.431d};
            c1._genes = new Gene[]{G1};
        }
        Chromosome c2 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{0.12d, 0.56d, 0.43d};
            c2._genes = new Gene[]{G1};
        }
        assertFalse(c1.isEqual(c2, Constants.EPSILON));
        assertFalse(c2.isEqual(c1, Constants.EPSILON));
    }

    /**
     * Test 6.
     */
    @Test
    public void test7()
    {
        Chromosome c1 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{};
            c1._genes = new Gene[]{G1};
        }
        Chromosome c2 = new Chromosome();
        {
            Gene G1 = new Gene();
            G1._dv = new double[]{};
            c2._genes = new Gene[]{G1};
        }
        assertTrue(c1.isEqual(c2, Constants.EPSILON));
        assertTrue(c2.isEqual(c1, Constants.EPSILON));
    }
}