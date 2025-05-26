package random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Wrapper for the XO_RO_SHI_RO_128_PP random number generator (Apache Commons RNG; 32 bit).
 * The generator is jumpable but not splittable.
 * @author MTomczyk
 */
public class XoRoShiRo128PP extends AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator (null, if not used)
     * @param rs   random source
     * @param urp  uniform random provider
     */
    protected XoRoShiRo128PP(long[] seed, RandomSource rs, UniformRandomProvider urp)
    {
        super(seed, rs, urp);
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed
     */
    public XoRoShiRo128PP(long seed)
    {
        this(new long[]{seed}, RandomSource.XO_RO_SHI_RO_128_PP, RandomSource.XO_RO_SHI_RO_128_PP.create(new long[]{seed}));
    }

    /**
     * Parameterized constructor.
     *
     * @param seed seed (native seed size: 2; the Apache Commons RNG documentation)
     */
    public XoRoShiRo128PP(long[] seed)
    {
        this(seed, RandomSource.XO_RO_SHI_RO_128_PP, RandomSource.XO_RO_SHI_RO_128_PP.create(seed));
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
        return new XoRoShiRo128PP(null, _rs, urp);
    }
}
