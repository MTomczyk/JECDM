package interaction.trigger;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.rules.IRule;

import java.util.LinkedList;

/**
 * Main class for deciding whether to interact with the decision maker. The decision upon the interaction is made by
 * using a series of rules {@link IRule}. The interaction will be triggered if at least one rule favors interaction.
 * Note that all the rules objects are inspected when determining to interact (even if it is already known that the
 * interaction should be performed).
 *
 * @author MTomczyk
 */
public class InteractionTrigger
{
    /**
     * Series of rules for deciding whether to interact.
     */
    private final LinkedList<IRule> _rules;

    /**
     * Parameterized constructor (uses a single rule).
     *
     * @param rule rule
     */
    public InteractionTrigger(IRule rule)
    {
        if (rule != null)
        {
            _rules = new LinkedList<>();
            _rules.add(rule);
        }
        else _rules = null;
    }

    /**
     * Parameterized constructor.
     *
     * @param rules uses a series of rules
     */
    public InteractionTrigger(LinkedList<IRule> rules)
    {
        _rules = rules;
    }

    /**
     * Auxiliary method that can be called to validate some essential fields.
     *
     * @throws TriggerException the exception will be thrown if the validation fails
     */
    public void validate() throws TriggerException
    {
        if (_rules == null) throw new TriggerException("No rules are provided (the array is null)", this.getClass());
        if (_rules.isEmpty()) throw new TriggerException("No rules are provided (the array is empty)", this.getClass());
        for (IRule r : _rules)
            if (r == null) throw new TriggerException("One of the provided rules is null", this.getClass());
    }

    /**
     * Main method for deciding whether to interact with the decision maker. The decision upon the interaction is made
     * by using a series of rules {@link IRule}. The interaction will be triggered if at least one rule favors interaction.
     * Note that all the rules objects are inspected when determining to interact (even if it is already known that the
     * interaction should be performed).
     *
     * @param dmContext current decision-making context
     * @return the decision wrapped in a result class ({@link interaction.refine.Result})
     * @throws TriggerException the exception can be thrown and propagated higher
     */
    public Result shouldTriggerTheInteractions(DMContext dmContext) throws TriggerException
    {
        Result r = new Result(dmContext);
        long pT = System.nanoTime();

        for (IRule rule : _rules)
        {
            if (rule.shouldInteract(dmContext))
            {
                r._shouldInteract = true;
                if (r._callers == null) r._callers = new LinkedList<>();
                r._callers.add(rule.getDetailedStringRepresentation());
            }
        }
        r._processingTime = (System.nanoTime() - pT) / 1000000;
        return r;
    }

    /**
     * A call-back method that can be used to inform all the rules that the interaction, although triggered, is postponed
     * (e.g., due to the unavailability of alternatives)
     *
     * @param postpone object providing auxiliary data related to postponing (e.g., reason)
     */
    public void postpone(Postpone postpone)
    {
        for (IRule r : _rules) r.postpone(postpone);
    }

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process begins.
     *
     * @param dmContext current decision-making context
     */
    public void notifyPreferenceElicitationBegins(DMContext dmContext)
    {
        for (IRule r : _rules) r.notifyPreferenceElicitationBegins(dmContext);
    }

    /**
     * Method for notifying that the preference elicitation failed.
     *
     * @param dmContext current decision-making context
     */
    public void notifyPreferenceElicitationFailed(DMContext dmContext)
    {
        for (IRule r : _rules) r.notifyPreferenceElicitationFailed(dmContext);
    }

    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process ends.
     *
     * @param dmContext current decision-making context
     */
    public void notifyPreferenceElicitationEnds(DMContext dmContext)
    {
        for (IRule r : _rules) r.notifyPreferenceElicitationEnds(dmContext);
    }
}
