package problem.moo.zdt.evaluate;

import population.Specimen;
import reproduction.operators.ReproductionUtils;

import java.util.ArrayList;

/**
 * Evaluates specimens as imposed by the ZDT5 benchmark. Important note: the decision variables for
 * this problem are bit sequences. They are modeled as boolean arrays and stored within separate genes
 * ({@link population.Gene} within the top-level specimen's chromosome). The genes are crossed and mutated independently
 * using the specified Boolean-dedicated operators.
 *
 * @author MTomczyk
 */
public class ZDT5 extends AbstractZDTEvaluate
{
    /**
     * Default constructor (sets the number of decision variables to 30).
     */
    public ZDT5()
    {
        super(30 + 50);
    }

    /**
     * Evaluates specimens.
     *
     * @param specimens array of specimens to be evaluated
     */
    @Override
    public void evaluateSpecimens(ArrayList<Specimen> specimens)
    {
        for (Specimen specimen : specimens)
        {
            double f1 = 1.0d + ReproductionUtils.countTrues(specimen.getChromosome()._genes[0]._bv);
            double g = 0.0d;
            for (int i = 1; i < 11; i++)
            {
                double u = ReproductionUtils.countTrues(specimen.getChromosome()._genes[i]._bv);
                if (u < 5) g += (2.0d + u);
                else g += 1.0d;
            }
            double h = 1.0d / f1;
            specimen.setEvaluations(new double[]{f1, h * g});
        }
    }
}
