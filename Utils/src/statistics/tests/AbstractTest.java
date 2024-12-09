package statistics.tests;

/**
 * Abstract implementation of {@link ITest}.
 * Provides common fields, functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractTest implements ITest
{
    /**
     * Unique name (in upper case).
     */
    protected final String _name;

    /**
     * If true, the test should be considered two-sided; one-sided otherwise.
     */
    protected final boolean _twoSided;

    /**
     * If true, the samples are considered paired; false otherwise.
     */
    protected final boolean _paired;

    /**
     * If true, it is assumed that the samples are taken from populations with equal variances; false otherwise.
     */
    protected final boolean _equalVariances;

    /**
     * Parameterized constructor.
     *
     * @param name     unique name (in upper case)
     * @param twoSided if true, the test should be considered two-sided; one-sided otherwise
     * @param paired   if true, the samples are considered paired; false otherwise
     * @param equalVariance if true, it is assumed that the samples are taken from populations with equal variances; false otherwise
     */
    protected AbstractTest(String name, boolean twoSided, boolean paired, boolean equalVariance)
    {
        _name = name.toUpperCase();
        _twoSided = twoSided;
        _paired = paired;
        _equalVariances = equalVariance;
    }

    /**
     * Should return a unique string representation of the test.
     *
     * @return string representation
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Should return a unique string representation of the test.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }
}
