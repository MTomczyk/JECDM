package random;

import org.apache.commons.rng.JumpableUniformRandomProvider;
import org.apache.commons.rng.SplittableUniformRandomProvider;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.ContinuousSampler;
import org.apache.commons.rng.sampling.distribution.GaussianSampler;
import org.apache.commons.rng.sampling.distribution.ZigguratSampler;
import org.apache.commons.rng.simple.RandomSource;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Abstract class implementing {@link IRandom}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
abstract public class AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Seed used to initialize the initial generator (null, if not used); new generators derived via, e.g.,
     * {@link IRandom#createSplitInstance()} are not passed this reference.
     */
    protected final Object _seed;

    /**
     * Uniform random provider.
     */
    protected final UniformRandomProvider _urp;

    /**
     * Uniform random provider viewed as jumpable (if possible).
     */
    protected final JumpableUniformRandomProvider _jurp;

    /**
     * Uniform random provider viewed as splittable (if possible).
     */
    protected final SplittableUniformRandomProvider _surp;

    /**
     * Random source.
     */
    protected final RandomSource _rs;

    /**
     * Continuous sampler.
     */
    protected ContinuousSampler _cs;

    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected AbstractRandomNumberGenerator(Object seed, RandomSource rs, UniformRandomProvider urp)
    {
        _seed = seed;
        _rs = rs;
        _urp = urp;
        _cs = GaussianSampler.of(ZigguratSampler.NormalizedGaussian.of(_urp), 0, 1.0);
        if (_urp instanceof JumpableUniformRandomProvider) _jurp = (JumpableUniformRandomProvider) _urp;
        else _jurp = null;
        if (_urp instanceof SplittableUniformRandomProvider) _surp = (SplittableUniformRandomProvider) _urp;
        else _surp = null;
    }

    /**
     * Protected method. Creates wrapper's instance. To be overwritten.
     *
     * @param urp uniform random provider
     * @return object instance.
     */
    protected IRandom getInstance(UniformRandomProvider urp)
    {
        return null;
    }

    /**
     * Returns the seed used to initialize the object (null, if not used).
     *
     * @return the seed
     */
    public Object getSeed()
    {
        return _seed;
    }

    /**
     * Generates random integer (unbounded).
     *
     * @return generated integer
     */
    @Override
    public int nextInt()
    {
        return _urp.nextInt();
    }

    /**
     * Generates array of random integer (unbounded).
     *
     * @param n array length
     * @return generated integer
     */
    @Override
    public int[] nextInts(int n)
    {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = nextInt();
        return a;
    }


    /**
     * Generates random integer from [0, b) bound.
     *
     * @param b right bound
     * @return generated integer
     */
    @Override
    public int nextInt(int b)
    {
        return _urp.nextInt(b);
    }

    /**
     * Generates array of random integers from [0, b) bound.
     *
     * @param n array length
     * @param b right (upper) bound
     * @return generated integer
     */
    @Override
    public int[] nextInts(int n, int b)
    {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = nextInt(b);
        return a;
    }

    /**
     * Generates random double from a Gaussian distribution (exp. = 0, std. = 1).
     *
     * @return generated number
     */
    @Override
    public double nextGaussian()
    {
        return _cs.sample();
    }

    /**
     * Generates array of random doubles derived from a Gaussian distribution (exp. = 0, std. = 1).
     *
     * @param n array length
     * @return array of generated numbers
     */
    @Override
    public double[] nextGaussians(int n)
    {
        double[] a = new double[n];
        for (int i = 0; i < n; i++) a[i] = nextGaussian();
        return a;
    }

    /**
     * Generates random double from [0, 1] bound.
     *
     * @return generated number
     */
    @Override
    public double nextDouble()
    {
        return _urp.nextDouble();
    }

    /**
     * Generates array of random double from [0, 1] bound.
     *
     * @param n array length
     * @return generated number
     */
    @Override
    public double[] nextDoubles(int n)
    {
        double[] a = new double[n];
        for (int i = 0; i < a.length; i++) a[i] = nextDouble();
        return a;
    }

    /**
     * Generates random float from [0, 1] bound.
     *
     * @return generated number
     */
    @Override
    public float nextFloat()
    {
        return _urp.nextFloat();
    }

    /**
     * Generates array of random float from [0, 1] bound.
     *
     * @param n array length
     * @return array of generated floats
     */
    @Override
    public float[] nextFloats(int n)
    {
        float[] a = new float[n];
        for (int i = 0; i < n; i++) a[i] = nextFloat();
        return a;
    }

    /**
     * Generates true/false flag randomly.
     *
     * @return generated flag
     */
    @Override
    public boolean nextBoolean()
    {
        return _urp.nextBoolean();
    }

    /**
     * Generates array of random true/false flags.
     *
     * @param n array length
     * @return array of generated flags
     */
    @Override
    public boolean[] nextBooleans(int n)
    {
        boolean[] a = new boolean[n];
        for (int i = 0; i < n; i++) a[i] = nextBoolean();
        return a;
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
        double pnt = _urp.nextDouble();
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
        double pnt = _urp.nextDouble();
        double acc = 0.0d;
        for (int i = 0; i < p.length; i++)
        {
            if (pnt < acc + p[i]) return i;
            acc += p[i];
        }
        return p.length - 1;
    }

    /**
     * Method for indicating whether the concrete implementation supports performing jumps; for parallel computations
     * (see {@link IRandom#createInstanceViaJump()}).
     *
     * @return true, if object is jumble; false otherwise
     */
    @Override
    public boolean isJumpable()
    {
        return _rs.isJumpable();
    }

    /**
     * The method is supposed create object instance with advanced state. The method should return null
     * if {@link IRandom#isJumpable()} returns false.
     *
     * @return object clone with advanced state (null, if the object is not jumpable)
     */
    @Override
    public IRandom createInstanceViaJump()
    {
        if (!isJumpable()) return null;
        return getInstance(_jurp.jump());
    }


    /**
     * The method is supposed create object instance with advanced state. The method should return null
     * if {@link IRandom#isJumpable()} returns false.
     *
     * @param streamSize stream size (the number of clones to construct)
     * @return object clones with advanced state (null, if the object is not jumpable)
     */
    @Override
    public Stream<IRandom> createInstancesViaJumps(int streamSize)
    {
        if (!isJumpable()) return null;
        Stream<UniformRandomProvider> stream = _jurp.jumps(streamSize);
        ArrayList<IRandom> wrapped = new ArrayList<>(streamSize);
        for (UniformRandomProvider urp : stream.toList()) wrapped.add(getInstance(urp));
        return wrapped.stream();
    }


    /**
     * Method for indicating whether the concrete implementation supports performing the split of the random number
     * generator; for parallel computations (see {@link IRandom#createSplitInstance()}).
     *
     * @return true, if object is splittable; false otherwise
     */
    @Override
    public boolean isSplittable()
    {
        return _rs.isSplittable();
    }

    /**
     * The method is supposed create split instance of the object. The method should return null
     * if {@link IRandom#isSplittable()} returns false.
     *
     * @return split object (null, if the object is not splittable)
     */
    @Override
    public IRandom createSplitInstance()
    {
        if (!isSplittable()) return null;
        return getInstance(_surp.split());
    }

    /**
     * The method is supposed create split instance of the object. The method should return null
     * if {@link IRandom#isSplittable()} returns false.
     *
     * @param streamSize stream size (the number of clones to construct)
     * @return split object (null, if the object is not splittable)
     */
    @Override
    public Stream<IRandom> createSplitInstances(int streamSize)
    {
        if (!isSplittable()) return null;
        Stream<SplittableUniformRandomProvider> stream = _surp.splits(streamSize);
        ArrayList<IRandom> wrapped = new ArrayList<>(streamSize);
        for (SplittableUniformRandomProvider surp : stream.toList()) wrapped.add(getInstance(surp));
        return wrapped.stream();
    }

    /**
     * Returns string representation of the random number generator.
     *
     * @return string representation of the random number generator
     */
    @Override
    public String toString()
    {
        return _rs.toString();
    }
}
