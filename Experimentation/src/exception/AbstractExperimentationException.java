package exception;

import scenario.CrossedScenarios;
import scenario.Scenario;
import utils.Log;

import java.util.LinkedList;

/**
 * Abstract, custom exception that allows to capture unwanted system behavior.
 *
 * @author MTomczyk
 */


public abstract class AbstractExperimentationException extends AbstractException
{
    /**
     * Exception level.
     */
    public final String _level;

    /**
     * Scenario that caused the exception (if null, then the exception happened at the GDC level).
     */
    public final Scenario _scenario;

    /**
     * Crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level).
     */
    public final CrossedScenarios _crossedScenarios;

    /**
     * Trial number that caused the exception (if null, the exception was triggered at a higher level).
     */
    public final Integer _trial;

    /**
     * Parameterized exception.
     *
     * @param msg              exception message
     * @param handler          class that handled the exception
     * @param cause            can be null; if provided, additional information can be retrieved
     * @param level            exception level
     * @param scenario         scenario that caused the exception (if null, then the exception happened at the GDC level)
     * @param crossedScenarios crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level)
     * @param trial            trial number that caused the exception (if null, the exception was triggered at a higher level)
     */
    public AbstractExperimentationException(String msg,
                                            Class<?> handler,
                                            Throwable cause,
                                            String level,
                                            Scenario scenario,
                                            CrossedScenarios crossedScenarios,
                                            Integer trial)
    {
        super(msg, handler, cause);
        _level = level;
        _scenario = scenario;
        _crossedScenarios = crossedScenarios;
        _trial = trial;
    }

    /**
     * Parameterized exception.
     *
     * @param msg              exception message
     * @param handler          class that handled the exception
     * @param source           source of exception
     * @param level            exception level
     * @param scenario         scenario that caused the exception (if null, then the exception happened at the GDC level)
     * @param crossedScenarios crossed scenarios processing that caused the exception (if null, then the exception happened at the GDC level)
     * @param trial            trial number that caused the exception (if null, the exception was triggered at a higher level)
     */
    public AbstractExperimentationException(String msg,
                                            Class<?> handler,
                                            Class<?> source,
                                            String level,
                                            Scenario scenario,
                                            CrossedScenarios crossedScenarios,
                                            Integer trial)
    {
        super(msg, handler, source);
        _level = level;
        _scenario = scenario;
        _crossedScenarios = crossedScenarios;
        _trial = trial;
    }

    /**
     * Creates a string (lines array) that represents the details on the exception.
     *
     * @return details on the exception (each line individually)
     */
    public String[] getDetails()
    {
        LinkedList<String> lines = new LinkedList<>();
        lines.add(Log.getLog("Message = " + getMessage(), 0, false));
        if (_handler != null) lines.add(Log.getLog("Handler = " + _handler.getName(), 0, false));
        if (_source != null) lines.add(Log.getLog("Source = " + _source.getName(), 0, false));
        if (_level != null) lines.add(Log.getLog("Level = " + _level, 0, false));
        if (_scenario != null) lines.add(Log.getLog("Scenario = " + _scenario, 0, false));
        if (_crossedScenarios != null) lines.add(Log.getLog("Crossed scenarios = " + _scenario, 0, false));
        if (_trial != null) lines.add(Log.getLog("Trial = " + _trial, 0, false));
        String[] r = new String[lines.size()];
        int idx = 0;
        for (String s : lines) r[idx++] = s;
        return r;
    }
}
