package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.Postpone;

/**
 * Abstract implementation of {@link IRule}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */


public class AbstractRule implements IRule
{
    /**
     * Postponing counter.
     */
    protected int _postponed = 0;

    /**
     * Represents the total number of successfully executed interactions.
     */
    protected int _numberOfSuccessfullyConductedInteractions = 0;

    /**
     * Can be used to limit the total number of interactions.
     */
    protected int _limit = Integer.MAX_VALUE;


    /**
     * Signature for the main method for deciding whether to interact (returns false).
     *
     * @param dmContext current decision-making context
     * @return false
     * @throws TriggerException the exception can be thrown 
     */
    @Override
    public boolean shouldInteract(DMContext dmContext) throws TriggerException
    {
        return false;
    }

    /**
     * Getter for the number of successfully conducted interactions.
     *
     * @return the number of successfully conducted interactions
     */
    @Override
    public int getNumberOfSuccessfullyConductedInteractions()
    {
        return _numberOfSuccessfullyConductedInteractions;
    }

    /**
     * A call-back method that can be used to inform the object that the interaction, although triggered, is postponed
     * (e.g., due to the unavailability of alternatives) (does nothing).
     *
     * @param postpone object providing auxiliary data related to postponing (e.g., reason)
     */
    @Override
    public void postpone(Postpone postpone)
    {

    }

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process begins (does nothing).
     *
     * @param dmContext current decision-making context
     */
    @Override
    public void notifyPreferenceElicitationBegins(DMContext dmContext)
    {
        _numberOfSuccessfullyConductedInteractions++;
    }

    /**
     * Method for notifying that the preference elicitation failed.
     */
    @Override
    public void notifyPreferenceElicitationFailed(DMContext dmContext)
    {
        _numberOfSuccessfullyConductedInteractions--;
    }

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process ends (does nothing).
     *
     * @param dmContext current decision-making context
     */
    @Override
    public void notifyPreferenceElicitationEnds(DMContext dmContext)
    {

    }

    /**
     * Returns empty string.
     *
     * @return empty string
     */
    public String getDetailedStringRepresentation()
    {
        return "";
    }
}
