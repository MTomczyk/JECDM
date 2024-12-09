package runner.enums;

/**
 * Provides flags indicating when the data update for visualization should be executed.
 *
 * @author MTomczyk
 */
public enum UpdaterMode
{
    /**
     * If this flag is used, data updaters are run only at the end (run only for the last generation).
     */
    AT_THE_END,

    /**
     * If this flag is used, data updaters are run after a generation is completed.
     */
    AFTER_GENERATION,

    /**
     * If this flag is used, data updaters are run after a steady-state repeat is completed.
     */
    AFTER_STEADY_STATE_REPEAT,
}
