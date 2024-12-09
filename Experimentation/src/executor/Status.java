package executor;

/**
 * Auxiliary enum representing the per-scenario processing status.
 *
 * @author MTomczyk
 */

public enum Status
{
    /**
     * Scenario processing is successfully completed.
     */
    COMPLETED,

    /**
     * Scenario processing is terminated (due to exception).
     */
    TERMINATED,

    /**
     * Auxiliary status for controlling the processing flow.
     */
    CONTINUED,
}
