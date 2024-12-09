package phase;

import ea.EA;

/**
 * This class represents a report on the action performed by the calling {@link phase.IPhase#perform(EA)}
 *
 * @author MTomczyk
 */
public class PhaseReport
{
    /**
     * Supportive field used for measuring the execution time (in ns).
     */
    protected long _startTime = 0;

    /**
     * Supportive field used for measuring the execution time (in ns).
     */
    protected long _stopTime = 0;

    /**
     * Time elapsed when executing an action (in ms)
     */
    public Double _elapsedTime = null;
}
