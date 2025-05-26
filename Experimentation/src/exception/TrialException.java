package exception;

import scenario.Scenario;

/**
 * Custom exception that captures unwanted system behavior that happens on the trial level.
 *
 * @author MTomczyk
 */
public class TrialException extends AbstractExperimentationException
{
    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     * @param trial    trial number that caused the exception (if null, the exception was triggered at a higher level)
     */
    public TrialException(String msg, Class<?> handler, Scenario scenario, int trial)
    {
        this(msg, handler, (Throwable) null, scenario, trial);
    }

    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param source   source of exception (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     * @param trial    trial number that caused the exception (if null, the exception was triggered at a higher level)
     */
    public TrialException(String msg, Class<?> handler, Class<?> source, Scenario scenario, int trial)
    {
        super(msg, handler, source, null, scenario, null, trial);
    }

    /**
     * Parameterized exception.
     *
     * @param msg      exception message
     * @param handler  class that handled the exception (can be null)
     * @param cause    can be null; if provided, additional information can be retrieved (can be null)
     * @param scenario scenario that caused the exception (if null, then the exception happened at the GDC level)
     * @param trial    trial number that caused the exception (if null, the exception was triggered at a higher level)
     */
    public TrialException(String msg, Class<?> handler, Throwable cause, Scenario scenario, int trial)
    {
        super(msg, handler, cause, null, scenario, null, trial);
    }
}
