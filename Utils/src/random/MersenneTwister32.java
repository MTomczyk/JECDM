package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Wrapper for the Mersenne Twister random number generator (Apache Commons RNG; 32 bit).
 * The generator is not jumpable and not splittable.
 *
 * @author MTomczyk
 */
public class MersenneTwister32 extends AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected MersenneTwister32(long[] seed, RandomSource rs, UniformRandomProvider urp)
    {
        super(seed, rs, urp);
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public MersenneTwister32(long seed)
    {
        this(new long[]{seed}, RandomSource.MT, RandomSource.MT.create(new long[]{seed}));
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed (native seed size: 624; the Apache Commons RNG documentation)
     */
    public MersenneTwister32(long[] seed)
    {
        this(seed, RandomSource.MT, RandomSource.MT.create(seed));
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
        return new MersenneTwister32(null, _rs, urp);
    }
}
