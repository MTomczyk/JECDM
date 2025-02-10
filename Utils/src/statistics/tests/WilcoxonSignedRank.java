package statistics.tests;

import org.apache.commons.math4.legacy.exception.*;
import org.apache.commons.math4.legacy.stat.inference.WilcoxonSignedRankTest;

/**
 * Class responsible for performing the Wilcoxon signed-rank test.
 * If the number of samples > 30, the main method approximates p-value ({@link WilcoxonSignedRank#getPValue(double[], double[])}).
 * @author MTomczyk
 */
public class WilcoxonSignedRank extends AbstractTest implements ITest
{
    /**
     * Object responsible for performing the test.
     */
    private final WilcoxonSignedRankTest _wsrTest;

    /**
     * Default constructor.
     *
     */
    public WilcoxonSignedRank()
    {
        super("WSR", false, true, false);
        _wsrTest = new WilcoxonSignedRankTest();
    }


    /**
     * The main method for performing the test (significance-level; p-value; etc.).
     * If the number of samples > 30, the method approximates p-value.
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
            if (s1.length > 30) return _wsrTest.wilcoxonSignedRankTest(s1, s2, false);
            return _wsrTest.wilcoxonSignedRankTest(s1, s2, true);
        } catch (NullArgumentException | NoDataException | DimensionMismatchException |
                 NumberIsTooLargeException | ConvergenceException | MaxCountExceededException e)
        {
            throw new Exception("WilcoxonSignerRank Exception (reason = " + e.getMessage() + ")");
        }
    }
}
