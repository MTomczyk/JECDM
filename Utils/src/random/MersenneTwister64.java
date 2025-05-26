package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import java.util.Objects;

/**
 * Wrapper for the Mersenne Twister random number generator (Apache Commons RNG; 64 bit).
 * The generator is not jumpable and not splittable.
 *
 * @author MTomczyk
 */
public class MersenneTwister64 extends AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected MersenneTwister64(long[] seed, RandomSource rs, UniformRandomProvider urp)
    {
        super(seed, rs, urp);
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public MersenneTwister64(long seed)
    {
        this(new long[]{seed}, RandomSource.MT_64, RandomSource.MT_64.create(new long[]{seed}));
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed (native seed size: 312; the Apache Commons RNG documentation)
     */
    public MersenneTwister64(long[] seed)
    {
        this(seed, RandomSource.MT_64, RandomSource.MT_64.create(seed));
    }

    /**
     * Protected method. Creates wrapper's instance.
     *
     * @param urp uniform random provider
     * @return object instance.
     */
    @Override
    protected IRandom getInstance(UniformRandomProvider urp)
    {
        return new MersenneTwister64(null, _rs, urp);
    }
}
