package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.ContinuousSampler;
import org.apache.commons.rng.sampling.distribution.GaussianSampler;
import org.apache.commons.rng.sampling.distribution.ZigguratSampler;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Wrapper for the Mersenne Twister random number generator. (Apache Commons; 64 bit).
 *
 * @author MTomczyk
 */


public class MersenneTwister64 implements IRandom
{
    /**
     * Mersenne Twister.
     */
    private final UniformRandomProvider _mt;

    /**
     * Gaussian sampler.
     */
    private final ContinuousSampler _gaussianSampler;

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public MersenneTwister64(long seed)
    {
        _mt = RandomSource.MT.create(new long[]{seed});
        _gaussianSampler = GaussianSampler.of(ZigguratSampler.NormalizedGaussian.of(_mt), 0, 1.0);
    }

    /**
     * Generates random integer (unbounded).
     *
     * @return generated integer
     */
    @Override
    public int nextInt()
    {
        return _mt.nextInt();
    }

    /**
     * Generates random integer from [0, n) bound.
     *
     * @param n right boundary
     * @return generated integer
     */
    @Override
    public int nextInt(int n)
    {
        return _mt.nextInt(n);
    }

    /**
     * Generates random double from a Gaussian distribution (exp. = 0, std. = 1).
     *
     * @return generated number
     */
    @Override
    public double nextGaussian()
    {
        return _gaussianSampler.sample();
    }

    /**
     * Generates random double from [0, 1] bound.
     *
     * @return generated number
     */
    @Override
    public double nextDouble()
    {
        return _mt.nextDouble();
    }

    /**
     * Generates random float from [0, 1] bound.
     *
     * @return generated number
     */
    @Override
    public float nextFloat()
    {
        return _mt.nextFloat();
    }

    /**
     * Generates true/false flag randomly.
     *
     * @return generated flag
     */
    @Override
    public boolean nextBoolean()
    {
        return _mt.nextBoolean();
    }


    /**
     * Selects one of the provided integers randomly with a given probability distribution.
     *
     * @param d array of integers
     * @param p probability distribution
     * @return selected integer
     */
    @Override
    public int getIntWithProbability(int[] d, double[] p)
    {
        double pnt = _mt.nextDouble();
        double acc = 0.0d;
        for (int i = 0; i < d.length; i++)
        {
            if (Double.compare(pnt, acc + p[i]) < 0) return d[i];
            acc += p[i];
        }
        return d[d.length - 1];
    }


    /**
     * Selects a random index with a given probability distribution (indexes are bounded from 0 to the length of the input array).
     *
     * @param p probability distribution
     * @return selected index
     */
    @Override
    public int getIdxWithProbability(double[] p)
    {
        double pnt = _mt.nextDouble();
        double acc = 0.0d;
        for (int i = 0; i < p.length; i++)
        {
            if (pnt < acc + p[i]) return i;
            acc += p[i];
        }
        return p.length - 1;
    }
}
