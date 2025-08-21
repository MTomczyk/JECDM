package indicator.emo.interactive;

import ea.IEA;
import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import system.dm.DecisionMakerSystem;

/**
 * The indicator returns the size of the history of preference elicitation. It also returns 0.0d if the history cannot
 * be derived (e.g., because the input EA is not interactive, thus does not employ a decision support system).
 *
 * @author MTomczyk
 */
public class HistorySize extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * The DM's identifier (0, 1,...) in {@link system.ds.DecisionSupportSystem}.
     */
    private final int _dmID;

    /**
     * Default constructor (assumes that DM's identifier = 0; and smaller values are preferred)
     */
    public HistorySize()
    {
        this(0, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param dmID            the DM's identifier (0, 1,...) in {@link system.ds.DecisionSupportSystem}.
     * @param lessIsPreferred flag determining preference direction (true = less is preferred)
     */
    public HistorySize(int dmID, boolean lessIsPreferred)
    {
        super(lessIsPreferred);
        _dmID = dmID;
    }

    /**
     * The method returns the size of the preference elicitation history associated with chosen decision maker.
     * The method will return 0.0d if the history cannot be derived (e.g., because the input EA is not interactive,
     * thus does not employ a decision support system).
     *
     * @param ea evolutionary algorithm
     * @return performance value (0 if the specimen container is null)
     */
    @Override
    public double evaluate(IEA ea)
    {
        DecisionMakerSystem DMS = Utils.getDMS(ea, _dmID);
        if (DMS == null) return 0.0d;
        return DMS.getHistory().getNoPreferenceExamples();
    }

    /**
     * Returns string representation
     *
     * @return string representation: "HISTORY_SIZE_" + DM's id.
     */
    @Override
    public String toString()
    {
        return "HISTORY_SIZE_DM" + _dmID;
    }


    /**
     * Creates a cloned object in an initial state.
     * The parameters are not deep-copies (references are passed).
     *
     * @return cloning
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new HistorySize(_dmID, _lessIsPreferred);
    }
}
