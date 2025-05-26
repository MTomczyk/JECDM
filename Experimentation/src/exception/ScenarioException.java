package exception;

import scenario.Scenario;

/**
 * Custom exception that captures unwanted system behavior that happens on the scenario level.
 *
 * @author MTomczyk
 */


public class ScenarioException extends AbstractExperimentationException
{
    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     */
    public ScenarioException(String msg, Class<?> handler, Scenario scenario)
    {
        this(msg, handler, (Throwable) null, scenario);
    }

    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param source   source of exception (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     */
    public ScenarioException(String msg, Class<?> handler, Class<?> source, Scenario scenario)
    {
        super(msg, handler, source, null, scenario, null, null);
    }

    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param cause    can be null; if provided, additional information can be retrieved (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     */
    public ScenarioException(String msg, Class<?> handler, Throwable cause, Scenario scenario)
    {
        super(msg, handler, cause, null, scenario, null, null);
    }
}
