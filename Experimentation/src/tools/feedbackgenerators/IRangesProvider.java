package tools.feedbackgenerators;

import scenario.Scenario;
import space.Range;

/**
 * Auxiliary interface for objects responsible for providing ranges (bounds) encapsulating all alternatives.
 * These are used to produce normalization objects employed, e.g., by the artificial DM's model.
 *
 * @author MTomczyk
 */
public interface IRangesProvider
{
    /**
     * The main method.
     *
     * @param scenario scenario being currently processed
     * @return ranges (one per each criterion/dimension).
     */
    Range[] getRanges(Scenario scenario);
}
