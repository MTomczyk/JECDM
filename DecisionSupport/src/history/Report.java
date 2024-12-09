package history;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Report-like class summarizing the update of the history of preference elicitation
 * (see {@link History#updateHistoryWithASubset(LinkedList, int, LocalDateTime)}).
 *
 * @author MTomczyk
 */
public class Report
{
    /**
     * Reports the number of previously stored preference examples (before the update).
     */
    public int _numberOfPreferenceExamplesBeforeUpdate = 0;

    /**
     * Reports the current number of stored preference examples (after the update).
     */
    public int _numberOfPreferenceExamplesAfterUpdate = 0;

    /**
     * Reports the number of removed preference examples (during the update).
     */
    public int _numberOfPreferenceExamplesRemovedDuringUpdate = 0;

    /**
     * Reports the removed preference examples (during the update).
     */
    public LinkedList<PreferenceInformationWrapper> _removedPreferenceExamples = null;

    /**
     * Reports the timestamp (iteration) during which the update was conducted.
     */
    public int _iteration = 0;

    /**
     * Reports the timestamp (date and time) during which the update was conducted.
     */
    public LocalDateTime _dateTime = null;
}
