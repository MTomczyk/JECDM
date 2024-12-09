package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import interaction.trigger.Postpone;

/**
 * This rule object decides whether to interact based on analyzing the current interaction number. The pattern uses two
 * parameters: startingIteration (minimum 0) and interval (minimum 1). The main method will return false if the current
 * iteration &lt; startingIteration. Next, after the current iteration exceeds the startingIteration, the interaction
 * requests are triggered every "interval" iterations, i.e., interact = (current iteration - startingIteration) modulo
 * interval == 0. This implementation also supports postponing interactions. Every call for postponing will result in
 * incrementing a postponing counter. Then, the main method will return true if the counter is positive. If it returns
 * true either way, i.e., due to satisfying the interval/startingIteration pattern, the counter is not decreased (it is
 * decreased otherwise). This way, the postponing calls can be stacked.
 *
 * @author MTomczyk
 */


public class IterationInterval extends AbstractRule implements IRule
{
    /**
     * Constant string representation.
     */
    public static final String _NAME = "ITERATION_INTERVAL";

    /**
     * Starting iteration (minimum 0).
     */
    private final int _startingIteration;

    /**
     * Iteration interval (minimum 1).
     */
    private final int _interval;


    /**
     * Parameterized constructor (sets the startingInteraction to 0).
     *
     * @param interval iteration interval
     */
    public IterationInterval(int interval)
    {
        this(0, interval, Integer.MAX_VALUE);
    }


    /**
     * Parameterized constructor.
     *
     * @param startingIteration starting iteration
     * @param interval iteration interval
     */
    public IterationInterval(int startingIteration, int interval)
    {
        this(startingIteration, interval, Integer.MAX_VALUE);
    }

    /**
     * Parameterized constructor.
     *
     * @param startingIteration starting iteration
     * @param interval iteration interval
     * @param limit determines the limit for the number of interactions
     */
    public IterationInterval(int startingIteration, int interval, int limit)
    {
        _interval = Math.max(interval, 1);
        _startingIteration = Math.max(startingIteration, 0);
        _limit = limit;
    }


    /**
     * Signature for the main method for deciding whether to interact.
     *
     * @param dmContext current decision-making context
     * @return true, if the rule favors interaction, false otherwise
     * @throws TriggerException exception can be thrown and propagated higher
     */
    @Override
    public boolean shouldInteract(DMContext dmContext) throws TriggerException
    {
        if (_numberOfSuccessfullyConductedInteractions >= _limit) return false;

        int ci = dmContext.getCurrentIteration();
        if (ci < _startingIteration) return false;

        boolean interact = (ci - _startingIteration) % _interval == 0;

        if ((_postponed > 0) && (interact))
        {
            return true;
        }
        else if ((_postponed == 0) && (!interact)) return false;
        else if (_postponed > 0)
        {
            _postponed--;
            return true;
        }

        return true;
    }

    /**
     * A call-back method that can be used to inform the object that the interaction, although triggered, is postponed
     * (e.g., due to the unavailability of alternatives). This implementation increases the postponing counter.
     *
     * @param postpone object providing auxiliary data related to postponing (e.g., reason)
     */
    @Override
    public void postpone(Postpone postpone)
    {
        _postponed++;
    }

    /**
     * Returns an extended string representation ("INTERACTION_INTERVAL_" + startingIteration + "_" + interval).
     *
     * @return extended string representation
     */
    @Override
    public String getDetailedStringRepresentation()
    {
       return IterationInterval._NAME + "_" + _startingIteration + "_" + _interval;
    }

    /**
     * Returns the string representation ("INTERACTION_INTERVAL").
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return IterationInterval._NAME;
    }
}
