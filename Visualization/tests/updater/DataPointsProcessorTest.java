package updater;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various test for {@link DataPointsProcessor} class.
 *
 * @author MTomczyk
 */
class DataPointsProcessorTest
{
    /**
     * Test.
     */
    @Test
    void getProcessedData()
    {
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]});
            dp.update(null);
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            assertNull(data.getFirst());
        }
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]});
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(2.0d, d[0][0]);
            assertEquals(0.0d, d[0][1]);
            assertEquals(1.0d, d[0][2]);
            assertEquals(3.0d, d[1][0]);
            assertEquals(0.0d, d[1][1]);
            assertEquals(5.0d, d[1][2]);
        }
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]});
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(2.0d, d[0][0]);
            assertEquals(0.0d, d[0][1]);
            assertEquals(1.0d, d[0][2]);
            assertEquals(3.0d, d[1][0]);
            assertEquals(0.0d, d[1][1]);
            assertEquals(5.0d, d[1][2]);
        }
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]}, true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(2, data.size());
            assertNull(data.getFirst());
            double[][] d = data.getLast();
            assertEquals(2, d.length);
            assertEquals(2.0d, d[0][0]);
            assertEquals(0.0d, d[0][1]);
            assertEquals(1.0d, d[0][2]);
            assertEquals(3.0d, d[1][0]);
            assertEquals(0.0d, d[1][1]);
            assertEquals(5.0d, d[1][2]);
        }

        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]}, true, 0, false);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(0, data.size());
        }
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]}, true, 1, false);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(2.0d, d[0][0]);
            assertEquals(0.0d, d[0][1]);
            assertEquals(1.0d, d[0][2]);
            assertEquals(3.0d, d[1][0]);
            assertEquals(0.0d, d[1][1]);
            assertEquals(5.0d, d[1][2]);
        }
        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]}, true, 1, true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(0, data.size());
        }

        {
            IDataProcessor dp = new DataPointsProcessor(p -> new double[]{p[1], 0.0f, p[0]}, true, 2, true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(2, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(2.0d, d[0][0]);
            assertEquals(0.0d, d[0][1]);
            assertEquals(1.0d, d[0][2]);
            assertEquals(3.0d, d[1][0]);
            assertEquals(0.0d, d[1][1]);
            assertEquals(5.0d, d[1][2]);
            assertNull(data.getLast());
        }
    }
}