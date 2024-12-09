package plot.heatmap.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Some test for the {@link HeatmapDataProcessor} class.
 *
 * @author MTomczyk
 */
class HeatmapDataProcessorTest
{
    /**
     * Test #1.
     */
    @Test
    void getCoords3D1()
    {
        HeatmapDataProcessor HDP = new HeatmapDataProcessor();

        HeatmapDataProcessor.SortedValues SV = HDP.getSortedValues(null, 3, true);
        assertNull(SV._sortedValues);
        assertNull(SV._sortedX_debug);
        assertNull(SV._sortedY_debug);
        assertNull(SV._sortedZ_debug);

        double[][][] data = new double[][][]
                {
                        {{0.12d, 0.25d, 1.0d}, {0.52d, 0.44d, 0.21d}, {0.67d, 0.42d, 1.0d}},
                        {{0.72d, 0.43d, 0.34d}, {0.51d, 0.43d, 0.55d}, {0.23d, 0.25d, 0.76d}},
                        {{0.76d, 0.53d, 0.32d}, {0.92d, 0.97d, 0.95d}, {0.24d, 0.26d, 0.27d}}
                };

        Coords [] SC = HDP.getCoords3D(3, 3, 3, data);
        SV = HDP.getSortedValues(SC, 3, false);

        double[] sV = SV._sortedValues;
        double[] expSorted = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1, 1};

        assertEquals(expSorted.length, sV.length);
        for (int i = 0; i < sV.length; i++) assertEquals(expSorted[i], sV[i], 0.0001d);

        assertNull(SV._sortedX_debug);
        assertNull(SV._sortedY_debug);
        assertNull(SV._sortedZ_debug);
        assertEquals(27, SC.length);
    }

    /**
     * Test #2.
     */
    @Test
    void getCoords3D2()
    {
        HeatmapDataProcessor HDP = new HeatmapDataProcessor();

        HeatmapDataProcessor.SortedValues SV = HDP.getSortedValues(null, 3, true);
        assertNull(SV._sortedValues);
        assertNull(SV._sortedX_debug);
        assertNull(SV._sortedY_debug);
        assertNull(SV._sortedZ_debug);

        double[][][] data = new double[][][]
                {
                        {{0.12d, 0.25d, Double.NEGATIVE_INFINITY}, {0.52d, 0.44d, 0.21d}, {0.67d, 0.42d, 1.0d}},
                        {{0.72d, 0.43d, 0.34d}, {0.51d, 0.43d, 0.55d}, {0.23d, 0.25d, 0.76d}},
                        {{0.76d, 0.53d, 0.32d}, {0.92d, 0.97d, 0.95d}, {0.24d, 0.26d, 0.27d}}
                };

        Coords [] SC = HDP.getCoords3D(3, 3, 3, data);
        SV = HDP.getSortedValues(SC, 3, true);

        double[] expSorted = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1};

        assertEquals(expSorted.length, SV._sortedValues.length);
        for (int i = 0; i < SV._sortedValues.length; i++) assertEquals(expSorted[i], SV._sortedValues[i], 0.0001d);

        int[] expX = new int[]{0, 2, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 0, 0, 1, 2, 0, 0, 2, 0, 0, 2, 1, 2};
        assertEquals(expX.length, SV._sortedX_debug.length);
        for (int i = 0; i < expX.length; i++) assertEquals(expX[i], SV._sortedX_debug[i]);

        int[] expY = new int[]{0, 1, 2, 2, 0, 2, 2, 2, 0, 0, 2, 0, 1, 1, 1, 1, 0, 1, 2, 0, 2, 0, 1, 1, 1, 2};
        assertEquals(expY.length, SV._sortedY_debug.length);
        for (int i = 0; i < expY.length; i++) assertEquals(expY[i], SV._sortedY_debug[i]);

        int[] expZ = new int[]{0, 0, 1, 2, 0, 1, 2, 2, 2, 1, 0, 1, 1, 0, 1, 0, 2, 1, 0, 1, 1, 2, 2, 2, 2, 0};
        assertEquals(expZ.length, SV._sortedZ_debug.length);
        for (int i = 0; i < expZ.length; i++) assertEquals(expZ[i], SV._sortedZ_debug[i]);
    }


    /**
     * Test #3.
     */
    @Test
    void getCoords3D3()
    {
        HeatmapDataProcessor HDP = new HeatmapDataProcessor();

        HeatmapDataProcessor.SortedValues SV = HDP.getSortedValues(null, 3, true);
        assertNull(SV._sortedValues);
        assertNull(SV._sortedX_debug);
        assertNull(SV._sortedY_debug);
        assertNull(SV._sortedZ_debug);

        double[][][] data = new double[][][]
                {
                        {{0.12d, 0.25d, Double.NEGATIVE_INFINITY}, {0.52d, 0.44d, 0.21d}, {0.67d, 0.42d, 1.0d}},
                        {{0.72d, 0.43d, 0.34d}, {0.51d, 0.43d, 0.55d}, {0.23d, 0.25d, 0.76d}},
                        {{0.76d, 0.53d, 0.32d}, {0.92d, 0.97d, 0.95d}, {0.24d, 0.26d, 0.27d}}
                };

        Coords [] SC = HDP.getCoords3D(3, 3, 3, data);
        SV = HDP.getSortedValues(SC, 3, true);

        double[] expSorted = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1};

        assertEquals(expSorted.length, SV._sortedValues.length);
        for (int i = 0; i < SV._sortedValues.length; i++) assertEquals(expSorted[i], SV._sortedValues[i], 0.0001d);

        int[] expX = new int[]{0, 2, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 0, 0, 1, 2, 0, 0, 2, 0, 0, 2, 1, 2};
        assertEquals(expX.length, SV._sortedX_debug.length);
        for (int i = 0; i < expX.length; i++) assertEquals(expX[i], SV._sortedX_debug[i]);

        int[] expY = new int[]{0, 1, 2, 2, 0, 2, 2, 2, 0, 0, 2, 0, 1, 1, 1, 1, 0, 1, 2, 0, 2, 0, 1, 1, 1, 2};
        assertEquals(expY.length, SV._sortedY_debug.length);
        for (int i = 0; i < expY.length; i++) assertEquals(expY[i], SV._sortedY_debug[i]);

        int[] expZ = new int[]{0, 0, 1, 2, 0, 1, 2, 2, 2, 1, 0, 1, 1, 0, 1, 0, 2, 1, 0, 1, 1, 2, 2, 2, 2, 0};
        assertEquals(expZ.length, SV._sortedZ_debug.length);
        for (int i = 0; i < expZ.length; i++) assertEquals(expZ[i], SV._sortedZ_debug[i]);
    }
}