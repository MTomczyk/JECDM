package tools.feedbackgenerators;

import scenario.Scenario;

/**
 * Implementation of {@link INoInteractionsProvider} for providing constant number of interactions.
 *
 * @author MTomczyk
 */
public class Constant implements INoInteractionsProvider
{
    /**
     * Parameterized constructor.
     *
     * @param no the number of interactions (constant; at least 1).
     */
    public Constant(int no)
    {
        _no = Math.max(no, 0);
    }

    /**
     * The number of interactions (constant).
     */
    private final int _no;

    @Override
    public int getNoInteractions(Scenario scenario)
    {
        return _no;
    }
}
