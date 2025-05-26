package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Wrapper for the PCG_XSH_RR_32 random number generator (Apache Commons RNG; 32 bit).
 * The generator is not jumpable and not splittable.
 *
 * @author MTomczyk
 */
public class PcgXshRR32 extends AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected PcgXshRR32(long[] seed, RandomSource rs, UniformRandomProvider urp)
    {
        super(seed, rs, urp);
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public PcgXshRR32(long seed)
    {
        this(new long[]{seed}, RandomSource.PCG_XSH_RR_32, RandomSource.PCG_XSH_RR_32.create(new long[]{seed}));
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed (native seed size: 2; the Apache Commons RNG documentation)
     */
    public PcgXshRR32(long[] seed)
    {
        this(seed, RandomSource.PCG_XSH_RR_32, RandomSource.PCG_XSH_RR_32.create(seed));
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
        return new PcgXshRR32(null, _rs, urp);
    }
}
