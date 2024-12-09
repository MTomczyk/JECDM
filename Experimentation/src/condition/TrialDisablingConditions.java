package condition;

/**
 * Class that provides conditions for excluding a trial from processing.
 *
 * @author MTomczyk
 */
public class TrialDisablingConditions
{
    /**
     * Determines the upper number for trial IDs, i.e., [0; _noTrials - 1].
     */
    private final int _noTrials;

    /**
     * Flags representing which trials should be excluded, i.e., if _disabled[trial id] = true, the corresponding trial
     * run should be skipped.
     */
    private final boolean[] _disabled;

    /**
     * Parameterized constructor. Enables all trials in the provided interval b default.
     *
     * @param noTrials determines the upper number for trial IDs, i.e., [0; noTrials - 1] (should be > 0)
     */
    public TrialDisablingConditions(int noTrials)
    {
        if (noTrials < 1) noTrials = 1;
        _noTrials = noTrials;
        _disabled = new boolean[noTrials];
    }

    /**
     * Can be called to check if a trial with a specified ID is disabled.
     *
     * @param id trial ID
     * @return true = trial is disabled; false otherwise
     */
    public boolean isTrialDisabled(int id)
    {
        if (id < 0) return false;
        if (id >= _disabled.length) return false;
        return _disabled[id];
    }

    /**
     * Auxiliary method that disables (set flags to true) trials with IDs in the specified (closed) interval, i.e., [from; to].
     * If the [from; to] interval is not entirely enclosed by [0, _noTrials -1] interval, it will be properly truncated.
     *
     * @param from starting index
     * @param to   ending index
     */
    public void disableTrials(int from, int to)
    {
        if (from < 0) from = 0;
        if (from >= _noTrials) return;
        if (to >= _noTrials) to = _noTrials - 1;
        if (to < from) return;
        for (int i = from; i <= to; i++) _disabled[i] = true;
    }

    /**
     * Auxiliary method that disables (set a flag to true) a trial with a specified ID.
     * The method terminates if the ID is not within the [0, _noTrials -1] interval.
     *
     * @param id trial ID
     */
    public void disableTrial(int id)
    {
        if (id < 0) return;
        if (id >= _noTrials) return;
        _disabled[id] = true;
    }

    /**
     * Auxiliary method that enables (set flags to false) trials with IDs in the specified (closed) interval, i.e., [from; to].
     * If the [from; to] interval is not entirely enclosed by [0, _noTrials -1] interval, it will be properly truncated.
     *
     * @param from starting index
     * @param to   ending index
     */
    public void enableTrials(int from, int to)
    {
        if (from < 0) from = 0;
        if (from >= _noTrials) return;
        if (to >= _noTrials) to = _noTrials - 1;
        if (to < from) return;
        for (int i = from; i <= to; i++) _disabled[i] = false;
    }


    /**
     * Auxiliary method that enables (set a flag to false) a trial with a specified ID.
     * The method terminates if the ID is not within the [0, _noTrials -1] interval.
     *
     * @param id trial ID
     */
    public void enableTrial(int id)
    {
        if (id < 0) return;
        if (id >= _noTrials) return;
        _disabled[id] = false;
    }
}
