package statistics.tests;

import org.apache.commons.math4.legacy.exception.MaxCountExceededException;
import org.apache.commons.math4.legacy.exception.NullArgumentException;
import org.apache.commons.math4.legacy.exception.NumberIsTooSmallException;
import org.apache.commons.math4.legacy.stat.inference.TTest;

/**
 * Class responsible for performing the t-Student test.
 *
 * @author MTomczyk
 */
public class TStudent extends AbstractTest implements ITest
{
    /**
     * Object responsible for performing the test.
     */
    private final TTest _tTest;

    /**
     * Parameterized constructor.
     *
     * @param twoSided       if true, the test should be considered two-sided; one-sided otherwise
     * @param paired         if true, the samples are considered paired; false otherwise (if paired, the equalVariances flag is not used)
     * @param equalVariances if true, it is assumed that the samples are taken from populations with equal variances; false otherwise
     */
    protected TStudent(boolean twoSided, boolean paired, boolean equalVariances)
    {
        super("TStudent", twoSided, paired, equalVariances);
        _tTest = new TTest();
    }

    /**
     * Creates the TStudent instance for performing paired tests.
     *
     * @param twoSided if true, the test should be considered two-sided; one-sided otherwise
     * @return object instance
     */
    public static TStudent getPairedTest(boolean twoSided)
    {
        return new TStudent(twoSided, true, false);
    }

    /**
     * Creates the TStudent instance for performing unpaired tests (homoscedastic or heteroscedastic).
     *
     * @param twoSided       if true, the test should be considered two-sided; one-sided otherwise
     * @param equalVariances if true, it is assumed that the samples are taken from populations with equal variances; false otherwise
     * @return object instance
     */
    public static TStudent getUnpairedTest(boolean twoSided, boolean equalVariances)
    {
        return new TStudent(twoSided, false, equalVariances);
    }

    /**
     * The main method for performing the test (significance-level; p-value; etc.).
     *
     * @param s1 the first sample
     * @param s2 the second sample
     * @return p-value (returns null if the input data is invalid)
     * @throws Exception the exception will be thrown in the case of invalid inputs
     */
    @Override
    public Double getPValue(double[] s1, double[] s2) throws Exception
    {
        double v;
        try
        {
            if (_paired) v = _tTest.pairedTTest(s1, s2);
            else
            {
                if (_equalVariances) v = _tTest.homoscedasticTTest(s1, s2);
                else v = _tTest.tTest(s1, s2);
            }


        } catch (NullArgumentException | NumberIsTooSmallException | MaxCountExceededException e)
        {
            throw new Exception("TStudent Exception (reason = " + e.getMessage() + ")");
        }

        if (_twoSided) return v;
        else return v / 2.0d;
    }
}
