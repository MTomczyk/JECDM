package random;

/**
 * Abstract class implementing {@link IRandom}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
abstract public class AbstractRandomNumberGenerator implements IRandom
{
    /**
     * Seed used to initialize the generator.
     */
    private final long _seed;

    /**
     * Parameterized constructor.
     *
     * @param seed seed used to initialize the generator
     */
    protected AbstractRandomNumberGenerator(long seed)
    {
        _seed = seed;
    }


    /**
     * Returns the seed used to initialize the object.
     *
     * @return the seed
     */
    @Override
    public long getSeed()
    {
        return _seed;
    }
}
