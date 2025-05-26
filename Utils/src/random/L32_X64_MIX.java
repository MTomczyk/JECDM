package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Wrapper for the L32_X64_MIX random number generator (Apache Commons RNG; 32 bit).
 * The generator is jumpable and splittable.
 *
 * @author MTomczyk
 */
public class L32_X64_MIX extends AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected L32_X64_MIX(long[] seed, RandomSource rs, UniformRandomProvider urp)
    {
        super(seed, rs, urp);
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public L32_X64_MIX(long seed)
    {
        this(new long[]{seed}, RandomSource.L32_X64_MIX, RandomSource.L32_X64_MIX.create(new long[]{seed}));
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed (native seed size: 4; the Apache Commons RNG documentation)
     */
    public L32_X64_MIX(long[] seed)
    {
        this(seed, RandomSource.L32_X64_MIX, RandomSource.L32_X64_MIX.create(seed));
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
        return new L32_X64_MIX(null, _rs, urp);
    }
}
