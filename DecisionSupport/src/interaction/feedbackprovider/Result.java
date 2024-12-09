package interaction.feedbackprovider;

import dmcontext.DMContext;
import interaction.feedbackprovider.common.CommonResult;
import system.dm.DM;

/**
 * Wrapper for the received feedback.
 *
 * @author MTomczyk
 */


public class Result extends CommonResult
{
    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     * @param DMs decision makers' identifiers
     */
    public Result(DMContext dmContext, DM[] DMs)
    {
        super(dmContext, DMs);
    }
}
