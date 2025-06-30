package tools.feedbackgenerators;

import scenario.Scenario;
import space.Range;

/**
 * Standard implementation of {@link IRangesProvider}.
 *
 * @author MTomczyk
 */
public class RangesProvider extends DimensionsDependent implements IRangesProvider
{
    /**
     * Auxiliary interface for objects responsible for providing ranges.
     */
    public interface IRanges
    {
        /**
         * The main method.
         *
         * @param M the number of criteria/objectives
         * @return constructed ranges spanned the considered alternatives space
         */
        Range[] getRanges(int M);
    }

    /**
     * Parameterized constructor.
     *
     * @param dimensionsKey key associated with the number of criteria/objectives
     * @param ranges        auxiliary object responsible for providing ranges
     */
    public RangesProvider(String dimensionsKey, IRanges ranges)
    {
        super(dimensionsKey);
        _ranges = ranges;
    }

    /**
     * Auxiliary object responsible for providing ranges.
     */
    private final IRanges _ranges;

    /**
     * The main method.
     *
     * @param scenario scenario being currently processed
     * @return ranges (one per each criterion/dimension).
     */
    @Override
    public Range[] getRanges(Scenario scenario)
    {
        return _ranges.getRanges(getM(scenario));
    }
}
