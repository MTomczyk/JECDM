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
     * @param handler  class that handled the exception
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     */
    public ScenarioException(String msg, Class<?> handler, Scenario scenario)
    {
        this(msg, handler, null, scenario);
    }

    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception
     * @param cause    can be null; if provided, additional information can be retrieved
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     */
    public ScenarioException(String msg, Class<?> handler, Throwable cause, Scenario scenario)
    {
        super(msg, handler, cause, null, scenario, null, null);
    }
}
