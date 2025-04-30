package reproduction.operators.crossover;

import random.IRandom;
import reproduction.operators.mutation.Gaussian;
import reproduction.operators.mutation.IMutate;
import space.Vector;
import space.simplex.Simplex;

/**
 * Normalized points combination (works only with doubles). This operator assumes that the input double vectors are of
 * the same length and are on a normalized simplex plane ([0,1] components that sum up to 1). This crossover produces
 * the offspring vector by creating a combination of its parent: offspring = p1 + (p2 - p1) * weight (thus, the offspring
 * vector should also be normalized). The weight is derived by using an auxiliary IMutate object (preferably the unconstrained
 * Gaussian operator {@link reproduction.operators.mutation.Gaussian#getUnconstrained(double, double)}).
 * Since the weight may be generated randomly and be out of the [0, 1] bound, the resulting offspring may not be
 * normalized (its coordinates may be lower than 0 or exceed 1). If so, the procedure repeats this (random) construction
 * up to the desired number of times. If the process fails, the method returns a random parent (clone) as offspring).
 *
 * @author MTomczyk
 */
public class OnSimplexCombination extends AbstractCrossover implements ICrossover
{
    /**
     * Auxiliary object used for creating random weight (preferably {@link reproduction.operators.mutation.Gaussian}).
     */
    private final IMutate _mutationGenerator;

    /**
     * The upper limit for the number of attempts to create a normalized offspring.
     */
    private final int _whileAttempts;

    /**
     * Parameterized constructor. If assumes that the parents will be swapped randomly before crossing them over,
     * unconstrained Gaussian operator is used to generate the weight for the combination,
     * and the limit for the number of attempts is 10.
     *
     * @param std standard deviation for the gaussian operator
     */
    public OnSimplexCombination(double std)
    {
        this(Gaussian.getUnconstrained(1.0d, std), 10);
    }

    /**
     * Parameterized constructor. Parameters are passed via params container.
     *
     * @param mutationGenerator (unconstrained) mutation operator is used to generate the weight for the combination
     * @param whileAttempts     the limit for the number of attempts
     */
    public OnSimplexCombination(IMutate mutationGenerator,
                                int whileAttempts)
    {
        super(new Params(1.0d, true));
        _mutationGenerator = mutationGenerator;
        _whileAttempts = Math.max(whileAttempts, 1);
    }


    /**
     * This operator assumes that the input double vectors are of the same length and are on a normalized simplex plane
     * ([0,1] components that sum up to 1). This crossover produces the offspring vector by creating a combination of
     * its parent: offspring = p1 + (p2 - p1) * weight (thus, the offspring vector should also be normalized).
     * The weight is derived by using an auxiliary IMutate object (preferably the unconstrained Gaussian operator
     * {@link Gaussian#getUnconstrained(double, double)}). Since the weight may be generated randomly and be out of the
     * [0, 1] bound, the resulting offspring may not be normalized (its coordinates may be lower than 0 or exceed 1).
     * If so, the procedure repeats this (random) construction up to the desired number of times. If the process fails,
     * the method returns a random parent (clone) as offspring).
     *
     * @param p1 decision vector of the first parent (assumed to be normalized)
     * @param p2 decision vector of the second parent (assumed to be normalized)
     * @param R  random number generator
     * @return new decision vector (normalized)
     */
    @Override
    public double[] crossover(double[] p1, double[] p2, IRandom R)
    {
        double[][] p = doSwap(p1, p2, R);

        int attempt = _whileAttempts;
        double[] o;
        double[] aw = new double[]{0.0d};
        do
        {
            aw[0] = 0.0d;
            _mutationGenerator.mutate(aw, R);
            double w = aw[0];

            o = Vector.getCombination(p[0], p[1], w);
            // for safety (it should already be normalized; can produce minor numerical errors)
            boolean inc = false;
            for (double d : o)
                if ((Double.compare(d, 0.0d) < 0) || (Double.compare(d, 1.0d) > 0))
                {
                    inc = true;
                    break;
                }
            if (inc) continue;

            Vector.thresholdAtZeroFromBelow(o);
            Vector.thresholdAtOneFromAbove(o);
            Simplex.normalize(o);

            return o;

        } while (attempt-- > 0);


        return p[0].clone();
    }
}
