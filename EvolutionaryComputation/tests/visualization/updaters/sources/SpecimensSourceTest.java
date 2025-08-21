package visualization.updaters.sources;

import org.junit.jupiter.api.Test;
import population.Specimen;
import utils.TestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides tests for {@link SpecimensSource}.
 *
 * @author MTomczyk
 */
class SpecimensSourceTest
{
    /**
     * Tests {@link SpecimensSource#createData()}.
     */
    @Test
    void createData()
    {
        {
            SpecimensSource specimensSource = new SpecimensSource(null);
            assertNull(specimensSource.createData());
        }
        {
            SpecimensSource specimensSource = new SpecimensSource(new ArrayList<>());
            TestUtils.assertEquals(new double[0][], specimensSource.createData(), 1.0E-6);
        }
        {
            ArrayList<Specimen> specimens = new ArrayList<>(2);
            specimens.add(new Specimen(new double[]{0.0d, 1.0d}));
            specimens.add(new Specimen(new double[]{3.0d, 2.0d, 4.0d}));
            SpecimensSource specimensSource = new SpecimensSource(specimens);
            double [][] data = specimensSource.createData();
            TestUtils.assertEquals(new double[][]{{0.0d, 1.0d}, {3.0d, 2.0d, 4.0d}}, data, 1.0E-6);
            assertNotEquals(data[0], specimens.get(0).getEvaluations()); // compares references
            assertNotEquals(data[1], specimens.get(1).getEvaluations()); // compares references
        }
    }
}