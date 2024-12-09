package statistics.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Various tests for {@link WilcoxonSignedRank}.
 *
 * @author MTomczyk
 */
class WilcoxonSignedRankTest
{
    /**
     * Test 1.
     */
    @Test
    void getPValue1()
    {
        double[] s1 = new double[]{0.153415253, 0.214934719, 0.169926208, 0.234490156, 0.23965537, 0.247242628, 0.15828538,
                0.221218186, 0.228464248, 0.213042082, 0.178867297, 0.172182979, 0.238604015, 0.179642265, 0.17148834,
                0.189501979, 0.173639861, 0.151070549, 0.23707857, 0.245244439};
        double[] s2 = new double[]{0.284431091, 0.271350993, 0.265707327, 0.346758381, 0.282287947, 0.332209103,
                0.328606204, 0.26919478, 0.272899762, 0.314398961, 0.263304461, 0.262570387, 0.297359228, 0.2613516,
                0.256002195, 0.323122826, 0.305590275, 0.251718667, 0.257706622, 0.341509224};
        WilcoxonSignedRank wsr = new WilcoxonSignedRank();

        double p = 0.0d;
        String msg = null;
        try
        {
            p = wsr.getPValue(s1, s2);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertEquals(0.000001907, p, 0.000000001d);
    }

    /**
     * Test 2.
     */
    @Test
    void getPValue2()
    {
        double[] s1 = new double[]{0.198613017, 0.204282464, 0.199446808, 0.197252895, 0.20126544, 0.201581007, 0.203928873,
                0.204691185, 0.203320859, 0.200615365, 0.196842942, 0.197217156, 0.197455586, 0.203721739, 0.202046142,
                0.199048457, 0.196960256, 0.19574971, 0.196086433, 0.201476089};
        double[] s2 = new double[]{0.199771866, 0.197064631, 0.20077194, 0.200151473, 0.199020734, 0.196228427,
                0.203748041, 0.198882944, 0.203480831, 0.201027393, 0.2030431, 0.198680239, 0.201906471, 0.201927378,
                0.204772414, 0.196347685, 0.204715805, 0.200218794, 0.198970053, 0.201401787};
        WilcoxonSignedRank wsr = new WilcoxonSignedRank();

        double p = 0.0d;
        String msg = null;
        try
        {
            p = wsr.getPValue(s1, s2);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertEquals(0.4304, p, 0.0001d);
    }
}