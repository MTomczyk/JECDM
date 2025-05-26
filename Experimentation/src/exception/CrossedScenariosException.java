package exception;

import executor.CrossSummarizer;
import scenario.CrossedScenarios;

/**
 * Custom exception that captures unwanted system behavior that happens when processing crossed scenarios using
 * {@link CrossSummarizer}.
 *
 * @author MTomczyk
 */


public class CrossedScenariosException extends AbstractExperimentationException
{
    /**
     * Parameterized exception.
     *
     * @param msg              exception message
     * @param handler          class that handled the exception (can be null)
     * @param crossedScenarios crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level)
     */
    public CrossedScenariosException(String msg, Class<?> handler, CrossedScenarios crossedScenarios)
    {
        this(msg, handler, (Throwable) null, crossedScenarios);
    }

    /**
     * Parameterized exception.
     *
     * @param msg              exception message
     * @param handler          class that handled the exception (can be null)
     * @param source           source of exception (can be null)
     * @param crossedScenarios crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level)
     */
    public CrossedScenariosException(String msg, Class<?> handler, Class<?> source, CrossedScenarios crossedScenarios)
    {
        super(msg, handler, source, null, null, crossedScenarios, null);
    }

    /**
     * Parameterized exception.
     *
     * @param msg              exception message
     * @param handler          class that handled the exception (can be null)
     * @param cause            can be null; if provided, additional information can be retrieved (can be null)
     * @param crossedScenarios crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level)
     */
    public CrossedScenariosException(String msg, Class<?> handler, Throwable cause, CrossedScenarios crossedScenarios)
    {
        super(msg, handler, cause, null, null, crossedScenarios, null);
    }
}
