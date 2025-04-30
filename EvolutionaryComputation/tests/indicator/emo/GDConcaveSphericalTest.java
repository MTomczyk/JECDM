package indicator.emo;

import org.junit.jupiter.api.Test;
import population.Specimen;
import space.distance.Euclidean;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link GDConcaveSpherical} class.
 *
 * @author MTomczyk
 */
class GDConcaveSphericalTest
{
    @Test
    void evaluate()
    {
        {
            GDConcaveSpherical GD = new GDConcaveSpherical(new Euclidean(), 2);
            ArrayList<Specimen> population = new ArrayList<>();
            population.add(new Specimen(0, new double[]{1.0d, 0.0d}));
            population.add(new Specimen(0, new double[]{0.0d, 1.0d}));
            double eval = GD.evaluate(population);
            assertEquals(0.0d, eval, 1.0E-6);
        }
        {
            GDConcaveSpherical GD = new GDConcaveSpherical(new Euclidean(), 2);
            ArrayList<Specimen> population = new ArrayList<>();
            population.add(new Specimen(0, new double[]{1.0d, 0.0d}));
            population.add(new Specimen(0, new double[]{2.0d, 2.0d}));
            double eval = GD.evaluate(population);
            assertEquals(((2.0d * Math.sqrt(2.0d)) - 1.0d) / 2.0d, eval, 1.0E-6);
        }
        {
            GDConcaveSpherical GD = new GDConcaveSpherical(new Euclidean(), 2);
            ArrayList<Specimen> population = new ArrayList<>();
            population.add(new Specimen(0, new double[]{2.0d, 0.0d}));
            population.add(new Specimen(0, new double[]{0.0d, 2.0d}));
            double eval = GD.evaluate(population);
            assertEquals(1.0d, eval, 1.0E-6);
        }
        {
            GDConcaveSpherical GD = new GDConcaveSpherical(new Euclidean(
                    new INormalization[]{
                        new Linear(0.0d, 2.0d),
                        new Linear(0.0d, 4.0d)
                    }
            ), 2);
            ArrayList<Specimen> population = new ArrayList<>();
            population.add(new Specimen(0, new double[]{2.0d, 0.0d}));
            population.add(new Specimen(0, new double[]{4.0d, 8.0d}));
            double eval = GD.evaluate(population);
            assertEquals(((2.0d * Math.sqrt(2.0d)) - 1.0d) / 2.0d, eval, 1.0E-6);
        }
        {
            GDConcaveSpherical GD = new GDConcaveSpherical(new Euclidean(
                    new INormalization[]{
                            new Linear(0.0d, 2.0d),
                            new Linear(0.0d, 4.0d)
                    }
            ), 2);
            ArrayList<Specimen> population = new ArrayList<>();
            population.add(new Specimen(0, new double[]{4.0d, 0.0d}));
            population.add(new Specimen(0, new double[]{0.0d, 8.0d}));
            double eval = GD.evaluate(population);
            assertEquals(1.0d, eval, 1.0E-6);
        }
    }
}