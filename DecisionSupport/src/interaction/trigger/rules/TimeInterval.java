package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;
import utils.DateTimeUtils;

import java.time.LocalDateTime;

/**
 * This rule decides upon the interaction by following a time-based pattern. Specifically, the main method
 * ({@link TimeInterval#shouldInteract(DMContext)}) returns true the time since the last call exceeds a given
 * threshold. Additionally, the initial delay (for the first interaction) can be specified).
 *
 * @author MTomczyk
 */


public class TimeInterval extends AbstractRule implements IRule
{
    /**
     * Constant string representation.
     */
    public static final String _NAME = "TIME_INTERVAL";


    /**
     * Represents the delay (waiting for the first interaction; in ms).
     */
    private final long _delay;

    /**
     * Represents the interval between subsequent interactions (in ms).
     */
    private final long _interval;

    /**
     * Auxiliary timestamp representing when the last interaction was triggered.
     */
    private LocalDateTime _lastTrigger;

    /**
     * Flag indicating whether the delay interval has come to past.
     */
    private boolean _delayPassed;

    /**
     * Auxiliary flag. The class sets it to true anytime the current timestamp needs to be stored at the end of the
     * preference elicitation phase.
     */
    private boolean _storeCurrentTimestamp;

    /**
     * Parameterized constructor (sets the delay to 0).
     *
     * @param interval represents the interval between subsequent interactions (in ms)
     */
    public TimeInterval(long interval)
    {
        this(0, interval);
    }

    /**
     * Parameterized constructor.
     *
     * @param delay    represents the delay (waiting for the first interaction; in ms)
     * @param interval represents the interval between subsequent interactions (in ms)
     */
    public TimeInterval(long delay, long interval)
    {
        this(delay, interval, Integer.MAX_VALUE);
    }


    /**
     * Parameterized constructor.
     *
     * @param delay    represents the delay (waiting for the first interaction; in ms)
     * @param interval represents the interval between subsequent interactions (in ms)
     * @param limit determines the limit for the number of interactions
     */
    public TimeInterval(long delay, long interval, int limit)
    {
        _limit = limit;
        _delay = delay;
        _interval = interval;
        _lastTrigger = null;
        _delayPassed = false;
        _storeCurrentTimestamp = false;
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

        if (!_delayPassed)
        {
            if (dmContext.getSystemStartingTimestamp() == null)
                throw new TriggerException("The system starting timestamp is not provided", this.getClass());
            long passed = DateTimeUtils.getDeltaTimeInMilliseconds(dmContext.getSystemStartingTimestamp(), dmContext.getCurrentDateTime());

            if (passed >= _delay)
            {
                _delayPassed = true;
                _storeCurrentTimestamp = true;
                return true;
            }
            else return false;
        }

        if (_lastTrigger == null)
            throw new TriggerException("The most recent timestamp is not stored (call notifyPreferenceElicitationEnds)", this.getClass());

        long passed = DateTimeUtils.getDeltaTimeInMilliseconds(_lastTrigger, dmContext.getCurrentDateTime());
        if (passed >= _interval)
        {
            _storeCurrentTimestamp = true;
            return true;
        }

        _storeCurrentTimestamp = false;
        return false;
    }


    /**
     * Auxiliary method that can be implemented to notify that the preference elicitation process ends.
     * This implementation reports the current timestamp (date and time).
     *
     * @param dmContext current decision-making context
     */
    @Override
    public void notifyPreferenceElicitationEnds(DMContext dmContext)
    {
        if (_storeCurrentTimestamp)
        {
            _lastTrigger = LocalDateTime.now();
            _storeCurrentTimestamp = false;
        }
    }

    /**
     * Returns an extended string representation ("TIME_INTERVAL" + delay + "_" + interval).
     *
     * @return extended string representation
     */
    @Override
    public String getDetailedStringRepresentation()
    {
        return TimeInterval._NAME + "_" + _delay + "_" + _interval;
    }

    /**
     * Returns the string representation ("TIME_INTERVAL").
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return TimeInterval._NAME;
    }
}
