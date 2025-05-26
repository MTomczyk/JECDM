package utils;

import exception.AbstractException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various test utils.
 *
 * @author MTomczyk
 */
public class TestUtils
{
    /**
     * Auxiliary interface for methods intending to trigger a runtime exception (or not; as desired)
     */
    public interface IExceptionMessageTrigger
    {
        /**
         * The main method that is supposed to wrap an operation that can trigger an exception.
         *
         * @throws AbstractException an exception that can be thrown
         */
        void execute() throws AbstractException;
    }

    /**
     * This auxiliary method intends to check if a method wrapped by
     * {@link IExceptionMessageTrigger} triggers an exception.
     * The returned error message is compared against the expected one.
     *
     * @param exp            the expected message (can be null)
     * @param messageTrigger a trigger that can produce an exception message
     */
    public static
    void compare(String exp, IExceptionMessageTrigger messageTrigger)
    {
        String msg = null;
        try
        {
            messageTrigger.execute();
        } catch (AbstractException e)
        {
            msg = e.getMessage();
        }
        assertEquals(exp, msg);
    }

    /**
     * Performs simple matrix comparison.
     *
     * @param exp       expected matrix
     * @param res       outcome matrix
     * @param tolerance tolerance level
     */
    public static void compare(double[][] exp, double[][] res, double tolerance)
    {
        if (exp == null)
        {
            assertNull(res);
            return;
        }
        else assertNotNull(res);
        assertEquals(exp.length, res.length);
        for (int i = 0; i < exp.length; i++)
        {
            if (exp[i] == null)
                assertNull(res[i]);
            else
            {
                assertEquals(exp[i].length, res[i].length);
                for (int j = 0; j < exp[i].length; j++)
                    assertEquals(exp[i][j], res[i][j], tolerance);
            }
        }
    }
}
