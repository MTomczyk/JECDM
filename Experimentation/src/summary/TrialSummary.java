package summary;

/**
 * Each instance represents a summary of the trial execution.
 *
 * @author MTomczyk
 */
public class TrialSummary extends AbstractSummary
{
    /**
     * Unique trial ID.
     */
    protected final int _trialID;

    /**
     * Parameterized constructor.
     *
     * @param trialID unique trialID
     */
    public TrialSummary(int trialID)
    {
        super(6);
        _trialID = trialID;
    }

    /**
     * Method that generates the summary (string).
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();
        sb.append("SUMMARY OF TRIAL = ").append(_trialID).append(ls);
        applyBasicStatistics(sb, _indent);
        return sb.toString();
    }
}
