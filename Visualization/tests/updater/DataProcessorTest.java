package updater;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides various test for {@link DataProcessor} class.
 *
 * @author MTomczyk
 */
class DataProcessorTest
{
    /**
     * Test.
     */
    @Test
    void getProcessedData()
    {
        {
            IDataProcessor dp = new DataProcessor();
            dp.update(null);
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            assertNull(data.getFirst());
        }
        {
            IDataProcessor dp = new DataProcessor();
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(1.0d, d[0][0]);
            assertEquals(2.0d, d[0][1]);
            assertEquals(5.0d, d[1][0]);
            assertEquals(3.0d, d[1][1]);
        }
        {
            IDataProcessor dp = new DataProcessor();
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(1.0d, d[0][0]);
            assertEquals(2.0d, d[0][1]);
            assertEquals(5.0d, d[1][0]);
            assertEquals(3.0d, d[1][1]);
        }
        {
            IDataProcessor dp = new DataProcessor(true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(2, data.size());
            assertNull(data.getFirst());
            double[][] d = data.getLast();
            assertEquals(2, d.length);
            assertEquals(1.0d, d[0][0]);
            assertEquals(2.0d, d[0][1]);
            assertEquals(5.0d, d[1][0]);
            assertEquals(3.0d, d[1][1]);
        }

        {
            IDataProcessor dp = new DataProcessor(true, 0, false);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(0, data.size());
        }
        {
            IDataProcessor dp = new DataProcessor(true, 1, false);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(1, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(1.0d, d[0][0]);
            assertEquals(2.0d, d[0][1]);
            assertEquals(5.0d, d[1][0]);
            assertEquals(3.0d, d[1][1]);
        }
        {
            IDataProcessor dp = new DataProcessor(true, 1, true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(0, data.size());
        }

        {
            IDataProcessor dp = new DataProcessor(true, 2, true);
            dp.update(null);
            dp.update(new double[][]{{1.0d, 2.0d}, {5.0d, 3.0d}});
            LinkedList<double[][]> data = dp.getData();
            assertEquals(2, data.size());
            double[][] d = data.getFirst();
            assertEquals(2, d.length);
            assertEquals(1.0d, d[0][0]);
            assertEquals(2.0d, d[0][1]);
            assertEquals(5.0d, d[1][0]);
            assertEquals(3.0d, d[1][1]);
            assertNull(data.getLast());
        }
    }
}