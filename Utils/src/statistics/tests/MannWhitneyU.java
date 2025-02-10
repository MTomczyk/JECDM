package statistics.tests;


import org.apache.commons.math4.legacy.exception.ConvergenceException;
import org.apache.commons.math4.legacy.exception.MaxCountExceededException;
import org.apache.commons.math4.legacy.exception.NoDataException;
import org.apache.commons.math4.legacy.exception.NullArgumentException;
import org.apache.commons.math4.legacy.stat.inference.MannWhitneyUTest;

/**
 * Class responsible for performing the Wilcoxon signed-rank test.
 *
 * @author MTomczyk
 */
public class MannWhitneyU extends AbstractTest implements ITest
{
    /**
     * Object responsible for performing the test.
     */
    private final MannWhitneyUTest _mwuTest;

    /**
     * Default constructor.
     *
     */
    public MannWhitneyU()
    {
        super("MWU", false, false ,false);
        _mwuTest = new MannWhitneyUTest();
    }


    /**
     * The main method for performing the test (significance-level; p-value; etc.).
     *
     * @param s1 the first sample
     * @param s2 the second sample
     * @return p-value (returns null if the input data is invalid)
     */
    @Override
    public Double getPValue(double[] s1, double[] s2) throws Exception
    {
        try
        {
            return _mwuTest.mannWhitneyUTest(s1, s2);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e)
        {
            throw new Exception("MannWhitneyU Exception (reason = " + e.getMessage() + ")");
        }
    }
}
