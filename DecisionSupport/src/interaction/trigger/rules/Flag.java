package interaction.trigger.rules;

import dmcontext.DMContext;
import exeption.TriggerException;

/**
 * This rule decides upon the interaction by using an internal flag (volatile) that can be set by any external source
 * (e.g., a human decision maker or the optimization algorithm).
 *
 * @author MTomczyk
 */


public class Flag extends AbstractRule implements IRule
{
    /**
     * Constant string representation.
     */
    public static final String _NAME = "FLAG";

    /**
     * Flag that determines whether to interact (true = interact; false otherwise).
     */
    private volatile boolean _flag;

    /**
     * Default constructor (sets the flag to false).
     */
    public Flag()
    {
        this(false);
    }

    /**
     * Parameterized constructor (sets the flag as provided).
     *
     * @param flag new flag value
     */
    public Flag(boolean flag)
    {
        setFlag(flag);
    }

    /**
     * Sets the internal flag as provided.
     *
     * @param flag new flag value
     */
    public void setFlag(boolean flag)
    {
        _flag = flag;
    }

    /**
     * Signature for the main method for deciding whether to interact
     *
     * @param dmContext current decision-making context
     * @return true, if the rule favors interaction, false otherwise
     * @throws TriggerException the exception can be thrown 
     */
    @Override
    public boolean shouldInteract(DMContext dmContext) throws TriggerException
    {
        return _flag;
    }

    /**
     * Returns an extended string representation ("FLAG_" + flag value).
     *
     * @return extended string representation
     */
    @Override
    public String getDetailedStringRepresentation()
    {
        if (_flag) return Flag._NAME + "_TRUE";
        else return Flag._NAME + "_FALSE";
    }

    /**
     * Returns the string representation ("FLAG").
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return Flag._NAME;
    }
}
