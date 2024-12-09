package utils;

import exception.AbstractExperimentationException;

/**
 * Main object printing notifications.
 *
 * @author MTomczyk
 */
public class Log
{

    /**
     * If true, the notifications are expected to be printed to the console.
     */
    private final boolean _notify;

    /**
     * If true, the notifications related to the top-level (global) processing are expected to be printed to the console.
     */
    private final boolean _notifyGlobalLevel;

    /**
     * If true, the notifications related to the middle-level (crossed-scenarios) processing are expected to be printed to the console.
     */
    private final boolean _notifyCrossedScenariosLevel;

    /**
     * If true, the notifications related to the middle-level (scenario) processing are expected to be printed to the console.
     */
    private final boolean _notifyScenarioLevel;

    /**
     * If true, the notifications related to the bottom-level (trial) processing are expected to be printed to the console.
     */
    private final boolean _notifyTrialLevel;

    /**
     * If true,  a timestamp accompanies the notifications printed into the console.
     */
    private final boolean _timestamp;

    /**
     * Parameterized constructor.
     *
     * @param notify              if true, the notifications are expected to be printed to the console
     * @param notifyGlobalLevel   if true, the notifications related to the top-level (global) processing are expected to be printed to the console
     * @param notifyCrossedScenariosLevel if true, the notifications related to the middle-level (crossed-scenarios) processing are expected to be printed to the console
     * @param notifyScenarioLevel if true, the notifications related to the middle-level (scenario) processing are expected to be printed to the console
     * @param notifyTrialLevel    if true, the notifications related to the bottom-level (trial) processing are expected to be printed to the console
     * @param timestamp           if true,  a timestamp accompanies the notifications printed into the console
     */
    public Log(boolean notify, boolean notifyGlobalLevel,  boolean notifyCrossedScenariosLevel,
               boolean notifyScenarioLevel, boolean notifyTrialLevel, boolean timestamp)
    {
        _notify = notify;
        _notifyGlobalLevel = notifyGlobalLevel;
        _notifyCrossedScenariosLevel = notifyCrossedScenariosLevel;
        _notifyScenarioLevel = notifyScenarioLevel;
        _notifyTrialLevel = notifyTrialLevel;
        _timestamp = timestamp;
    }

    /**
     * Auxiliary method for printing the input into the console.
     *
     * @param msg    message to be displayed
     * @param level  processing level
     * @param indent the number of spaces to be put in front of each line to be printed
     */
    public void log(String msg, Level level, int indent)
    {
        if (!_notify) return;
        if ((!_notifyGlobalLevel) && (level.equals(Level.Global))) return;
        if ((!_notifyScenarioLevel) && (level.equals(Level.Scenario))) return;
        if ((!_notifyCrossedScenariosLevel) && (level.equals(Level.CrossedScenarios))) return;
        if ((!_notifyTrialLevel) && (level.equals(Level.Trial))) return;
        String message = getLog(msg, indent, _timestamp);
        System.out.print(message);
    }

    /**
     * Auxiliary method for printing the input exception into the console.
     *
     * @param e exception that caused the termination
     * @return prints the input exception into the console
     */
    public String[] printTerminationMessage(AbstractExperimentationException e)
    {
        if ((!_notify) || (!_notifyGlobalLevel)) return null;
        String[] message = getTerminationMessage(e);
        for (String l : message) System.out.print(l);
        return message;
    }


    /**
     * Auxiliary method for getting the termination message to be printed into the console.
     * The method returns each line individually (array).
     *
     * @param e         the exception
     * @return termination message (each line individually)
     */
    public String[] getTerminationMessage(AbstractExperimentationException e)
    {
        String[] details = e.getDetails();
        String[] lines = new String[3 + details.length];
        lines[0] = getLog("============================================================", 0, false);
        lines[1] = getLog("TERMINATION DUE TO EXCEPTION", 0, _timestamp);
        System.arraycopy(details, 0, lines, 2, details.length);
        lines[2 + details.length] = getLog("============================================================", 0, false);
        return lines;
    }


    /**
     * Auxiliary method for creating the log message to be printed.
     *
     * @param msg       message to be displayed
     * @param indent    the number of spaces to be put in front of each line to be printed
     * @param timestamp if true, the message begins with a timestamp
     * @return message printed
     */
    public static String getLog(String msg, int indent, boolean timestamp)
    {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();

        String[] lines = msg.split("\\r?\\n");
        String sInd = StringUtils.getIndent(indent);

        if (timestamp)
        {
            sb.append(sInd);
            String time = StringUtils.getTimestamp(java.time.LocalDateTime.now());
            sb.append(time).append(": ");
        }

        for (int i = 0; i < lines.length; i++)
        {
            if ((i == 0) && (timestamp)) sb.append(lines[i]).append(ls);
            else sb.append(sInd).append(lines[i]).append(ls);
        }
        return sb.toString();
    }

}
