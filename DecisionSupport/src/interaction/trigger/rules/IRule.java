package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.Postpone;

/**
 * Interface for classes that are rules for deciding whether the interaction should be triggered.
 *
 * @author MTomczyk
 */
public interface IRule
{
    /**
     * Signature for the main method for deciding whether to interact.
     *
     * @param dmContext current decision-making context
     * @return true, if the rule favors interaction, false otherwise
     * @throws TriggerException the exception can be thrown 
     */
    boolean shouldInteract(DMContext dmContext) throws TriggerException;

    /**
     * A call-back method that can be used to inform the object that the interaction, although triggered, is postponed
     * (e.g., due to the unavailability of alternatives).
     *
     * @param postpone object providing auxiliary data related to postponing (e.g., reason)
     */
    void postpone(Postpone postpone);


    /**
     * Getter for the number of successfully conducted interactions.
     *
     * @return  the number of successfully conducted interactions
     */
    int getNumberOfSuccessfullyConductedInteractions();

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process begins.
     *
     * @param dmContext current decision-making context
     */
    void notifyPreferenceElicitationBegins(DMContext dmContext);

    /**
     * Method for notifying that the preference elicitation failed.
     *
     * @param dmContext current decision-making context
     */
    void notifyPreferenceElicitationFailed(DMContext dmContext);

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process ends.
     *
     * @param dmContext current decision-making context
     */
    void notifyPreferenceElicitationEnds(DMContext dmContext);


    /**
     * Returns an extended string representation (may include, e.g., parameterization).
     *
     * @return extended string representation
     */
    String getDetailedStringRepresentation();

    /**
     * Returns the string representation
     *
     * @return string representation
     */
    String toString();
}

