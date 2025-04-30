package reproduction.operators.mutation;

import random.IRandom;
import random.WeightsGenerator;
import space.Vector;
import space.simplex.Simplex;

/**
 * This mutation operator assumes that the input is a double vector on a normalized simplex plane ([0,1] components that
 * sum up to 1). It performs its random perturbation. For this reason, a random vector is drawn from the normalized
 * simplex first (uniform distribution). It dictates the mutation direction. For this reason, a central normalized
 * vector is first subtracted from this random vector (consisting of equal components that sum to 1). Then, a mutation
 * direction vector is rescaled using a scaling factor (fixed parameter) multiplied by a random number from [0,1]. The
 * scaling factor controls the overall spread of the mutation (it strictly limits it), while the random components
 * randomize the overall distance from the center. Since the unprocessed direction vector is also drawn randomly from
 * a uniform distribution (e.g., it can already be close to the central vector), the overall probability distribution
 * of attaining vectors in various points in the normalized simplex hyperplane will favor being closer to the central
 * vector. Next, the final direction vector is applied to the input offspring vector (mutation). Note that some of
 * the resulting weight may be out of the [0, 1] bound. If so, the procedure repeats this (random) construction up
 * to the desired number of times. If the process fails, the method returns the offspring (clone).
 *
 * @author MTomczyk
 */
public class OnSimplexSimplexMutation extends AbstractMutation implements IMutate
{

    /**
     * The upper limit for the number of attempts to create a normalized offspring.
     */
    private final int _whileAttempts;

    /**
     * Scale used when determining random deviation from the original point.
     */
    private final double _scale;

    /**
     * Parameterized constructor. If assumes that the parents will be swapped randomly before crossing them over,
     * and the limit for the number of attempts is 10.
     *
     * @param scale scale used when determining random deviation from the original point
     */
    public OnSimplexSimplexMutation(double scale)
    {
        this(scale, 10);
    }

    /**
     * Parameterized constructor. Parameters are passed via params container.
     *
     * @param scale         scale used when determining random deviation from the original point
     * @param whileAttempts the limit for the number of attempts
     */
    public OnSimplexSimplexMutation(double scale, int whileAttempts)
    {
        super(new Params(1.0d, null));
        _scale = scale;
        _whileAttempts = Math.max(whileAttempts, 1);
    }

    /**
     * Executes the mutation (doubles only).
     *
     * @param o decision vector to be mutated (assumed to be on a normalized simplex plane)
     * @param R random number generator
     * @return returns the input array
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public double[] mutate(double[] o, IRandom R)
    {
        int attempt = _whileAttempts;
        double sub = 1.0d / (double) o.length;
        double spread;
        double[] dw;
        do
        {
            dw = WeightsGenerator.getNormalizedWeightVector(o.length, R);
            spread = R.nextDouble() * _scale;
            for (int i = 0; i < dw.length; i++)
            {
                dw[i] -= sub;
                dw[i] *= spread;
                dw[i] += o[i];
            }

            boolean cont = false;
            for (double v : dw)
            {
                if ((Double.compare(v, 0.0d) < 0) || (Double.compare(v, 1.0d) > 0))
                {
                    cont = true;
                    break;
                }
            }
            if (cont) continue;

            System.arraycopy(dw, 0, o, 0, o.length);

            // for safety (it should already be normalized; can produce minor numerical errors)
            Vector.thresholdAtZeroFromBelow(o);
            Vector.thresholdAtOneFromAbove(o);
            Simplex.normalize(o);
            // ===============
            return o;

        } while (attempt-- > 0);

        // avoid mutation
        return o;
    }

}
