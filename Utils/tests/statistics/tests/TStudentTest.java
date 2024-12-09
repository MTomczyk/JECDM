package statistics.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link TStudent}.
 *
 * @author MTomczyk
 */
class TStudentTest
{

    /**
     * Test 1.
     */
    @Test
    void getPValue1()
    {
        double[] s1 = new double[]{0.153415253, 0.214934719, 0.169926208, 0.234490156, 0.23965537, 0.247242628, 0.15828538,
                0.221218186, 0.228464248, 0.213042082, 0.178867297, 0.172182979, 0.238604015, 0.179642265, 0.17148834, 0.189501979, 0.173639861, 0.151070549, 0.23707857, 0.245244439};
        double[] s2 = new double[]{0.284431091, 0.271350993, 0.265707327, 0.346758381, 0.282287947, 0.332209103,
                0.328606204, 0.26919478, 0.272899762, 0.314398961, 0.263304461, 0.262570387, 0.297359228, 0.2613516,
                0.256002195, 0.323122826, 0.305590275, 0.251718667, 0.257706622, 0.341509224};

        TStudent pairedTwo = TStudent.getPairedTest(true);
        TStudent pairedOne = TStudent.getPairedTest(false);
        TStudent unpairedTwoEqual = TStudent.getUnpairedTest(true, true);
        TStudent unpairedOneEqual = TStudent.getUnpairedTest(false, true);
        TStudent unpairedTwoUnequal = TStudent.getUnpairedTest(true, false);
        TStudent unpairedOneUnequal = TStudent.getUnpairedTest(false, false);

        String msg = null;
        try
        {
            double v1 = pairedTwo.getPValue(s1, s2);
            double v2 = pairedOne.getPValue(s1, s2);
            double v3 = unpairedTwoEqual.getPValue(s1, s2);
            double v4 = unpairedOneEqual.getPValue(s1, s2);
            double v5 = unpairedTwoUnequal.getPValue(s1, s2);
            double v6 = unpairedOneUnequal.getPValue(s1, s2);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println(v3);
            System.out.println(v4);
            System.out.println(v5);
            System.out.println(v6);
            assertEquals(0.00000000160260, v1, 0.00000000001);
            assertEquals(0.00000000080130, v2, 0.00000000001);
            assertEquals(0.00000000022803, v3, 0.00000000001);
            assertEquals(0.00000000011402, v4, 0.00000000001);
            assertEquals(0.00000000023954, v5, 0.00000000001);
            assertEquals(0.00000000011977, v6, 0.00000000001);


        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

    }


    /**
     * Test 2.
     */
    @Test
    void getPValue2()
    {
        double[] s1 = new double[]{0.24157182, 0.238874246, 0.195166885, 0.172774662, 0.233691101, 0.173422893, 0.246827389,
                0.172860177, 0.182346259, 0.246749476, 0.196688911, 0.217113134, 0.193776589, 0.243271455, 0.154336246,
                0.208164828, 0.204374802, 0.21416444, 0.238181309, 0.216586808};
        double[] s2 = new double[]{0.160551389, 0.212129969, 0.18746761, 0.210289901, 0.190559763, 0.175623911,
                0.213320004, 0.204174393, 0.160435301, 0.241567298, 0.224546093, 0.196016691, 0.192507511,
                0.185943597, 0.191401711, 0.19008169, 0.224171936, 0.20805987, 0.195873043, 0.17434052};

        TStudent pairedTwo = TStudent.getPairedTest(true);
        TStudent pairedOne = TStudent.getPairedTest(false);
        TStudent unpairedTwoEqual = TStudent.getUnpairedTest(true, true);
        TStudent unpairedOneEqual = TStudent.getUnpairedTest(false, true);
        TStudent unpairedTwoUnequal = TStudent.getUnpairedTest(true, false);
        TStudent unpairedOneUnequal = TStudent.getUnpairedTest(false, false);

        String msg = null;
        try
        {
            double v1 = pairedTwo.getPValue(s1, s2);
            double v2 = pairedOne.getPValue(s1, s2);
            double v3 = unpairedTwoEqual.getPValue(s1, s2);
            double v4 = unpairedOneEqual.getPValue(s1, s2);
            double v5 = unpairedTwoUnequal.getPValue(s1, s2);
            double v6 = unpairedOneUnequal.getPValue(s1, s2);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println(v3);
            System.out.println(v4);
            System.out.println(v5);
            System.out.println(v6);

            assertEquals(0.100821267, v1, 0.000000001);
            assertEquals(0.050410634, v2, 0.000000001);
            assertEquals(0.121991082, v3, 0.000000001);
            assertEquals(0.060995541, v4, 0.000000001);
            assertEquals(0.122779589, v5, 0.000000001);
            assertEquals(0.061389795, v6, 0.000000001);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 3.
     */
    @Test
    void getPValue3()
    {
        double[] s1 = new double[]{0.198613017, 0.204282464, 0.199446808, 0.197252895, 0.20126544, 0.201581007, 0.203928873,
                0.204691185, 0.203320859, 0.200615365, 0.196842942, 0.197217156, 0.197455586, 0.203721739, 0.202046142,
                0.199048457, 0.196960256, 0.19574971, 0.196086433, 0.201476089};
        double[] s2 = new double[]{0.199771866, 0.197064631, 0.20077194, 0.200151473, 0.199020734, 0.196228427,
                0.203748041, 0.198882944, 0.203480831, 0.201027393, 0.2030431, 0.198680239, 0.201906471, 0.201927378,
                0.204772414, 0.196347685, 0.204715805, 0.200218794, 0.198970053, 0.201401787};

        TStudent pairedTwo = TStudent.getPairedTest(true);
        TStudent pairedOne = TStudent.getPairedTest(false);
        TStudent unpairedTwoEqual = TStudent.getUnpairedTest(true, true);
        TStudent unpairedOneEqual = TStudent.getUnpairedTest(false, true);
        TStudent unpairedTwoUnequal = TStudent.getUnpairedTest(true, false);
        TStudent unpairedOneUnequal = TStudent.getUnpairedTest(false, false);

        String msg = null;
        try
        {
            double v1 = pairedTwo.getPValue(s1, s2);
            double v2 = pairedOne.getPValue(s1, s2);
            double v3 = unpairedTwoEqual.getPValue(s1, s2);
            double v4 = unpairedOneEqual.getPValue(s1, s2);
            double v5 = unpairedTwoUnequal.getPValue(s1, s2);
            double v6 = unpairedOneUnequal.getPValue(s1, s2);
            System.out.println(v1);
            System.out.println(v2);
            System.out.println(v3);
            System.out.println(v4);
            System.out.println(v5);
            System.out.println(v6);

            assertEquals(0.555534408, v1, 0.0000001);
            assertEquals(0.277767204, v2, 0.0000001);
            assertEquals(0.553176909, v3, 0.0000001);
            assertEquals(0.276588454, v4, 0.0000001);
            assertEquals(0.553254163, v5, 0.0000001);
            assertEquals(0.276627081, v6, 0.0000001);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Test 4.
     */
    @Test
    void getPValue4()
    {
        double[] s1 = new double[]{0.198613017};
        double[] s2 = new double[]{0.199771866, 0.197064631, 0.20077194, 0.200151473, 0.199020734, 0.196228427,
                0.203748041, 0.198882944, 0.203480831, 0.201027393, 0.2030431, 0.198680239, 0.201906471, 0.201927378,
                0.204772414, 0.196347685, 0.204715805, 0.200218794, 0.198970053, 0.201401787};

        TStudent pairedTwo = TStudent.getPairedTest(true);
        TStudent pairedOne = TStudent.getPairedTest(false);
        TStudent unpairedTwoEqual = TStudent.getUnpairedTest(true, true);
        TStudent unpairedOneEqual = TStudent.getUnpairedTest(false, true);
        TStudent unpairedTwoUnequal = TStudent.getUnpairedTest(true, false);
        TStudent unpairedOneUnequal = TStudent.getUnpairedTest(false, false);

        String msg = null;
        try
        {
            pairedTwo.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        msg = null;
        try
        {
            pairedOne.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        msg = null;
        try
        {
            unpairedTwoEqual.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        msg = null;
        try
        {
            unpairedOneEqual.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        msg = null;
        try
        {
            unpairedTwoUnequal.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);

        msg = null;
        try
        {
            unpairedOneUnequal.getPValue(s1, s2);

        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNotNull(msg);
    }

}