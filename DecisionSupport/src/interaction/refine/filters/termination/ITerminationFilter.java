package interaction.refine.filters.termination;

import dmcontext.DMContext;
import exeption.RefinerException;


/**
 * Interface for termination filters (decide whether to end the preference elicitation process prematurely).
 *
 * @author MTomczyk
 */
public interface ITerminationFilter
{
    /**
     * The method verifying whether the reference sets construction process should be terminated.
     *
     * @param dmContext current decision-making context
     * @return filter's indications
     * @throws RefinerException the exception can be thrown 
     */
    TerminationResult shouldTerminate(DMContext dmContext) throws RefinerException;
}
