package interaction;

/**
 * Provides various statuses related to creating reference sets of alternatives (alternatives to be evaluated
 * by the decision maker).
 *
 * @author MTomczyk
 */
public enum Status
{
    /**
     * Status related to not-triggering the interaction.
     */
    INTERACTION_WAS_NOT_TRIGGERED,

    /**
     * Status related to termination that was due to having not enough alternatives after the reduction step.
     */
    TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES,

    /**
     * Status related to termination that was caused by a termination filter.
     */
    TERMINATED_DUE_TO_TERMINATION_FILTER,

    /**
     * Status indicating that the process ended but no valid sets were constructed.
     */
    PROCESS_ENDED_BUT_NO_REFERENCE_SETS_WERE_CONSTRUCTED,

    /**
     * Status indicating that the process ended successfully (at least one reference set was constructed).
     */
    PROCESS_ENDED_SUCCESSFULLY,




}
