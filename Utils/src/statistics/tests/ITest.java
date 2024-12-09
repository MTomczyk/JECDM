package statistics.tests;

/**
 * Interface for classes responsible for performing statistical tests.
 *
 * @author MTomczyk
 */
public interface ITest
{
    /**
     * The main method for performing the test (significance-level; p-value; etc.).
     *
     * @param s1 the first sample
     * @param s2 the second sample
     * @return p-value
     * @throws Exception the signature supports throwing exceptions
     */
    Double getPValue(double[] s1, double[] s2) throws Exception;

    /**
     * Should return a unique string representation of the test.
     *
     * @return string representation
     */
    String getName();

    /**
     * Should return a unique string representation of the test.
     *
     * @return string representation
     */
    @Override
    String toString();
}
