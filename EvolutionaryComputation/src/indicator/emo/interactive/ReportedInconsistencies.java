package indicator.emo.interactive;

import ea.EA;
import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;
import system.model.ModelSystem;

/**
 * The indicator returns the number of reported inconsistencies associated with a model updater.
 * It also returns 0.0d if the model updater cannot be derived (e.g., because the input EA is not interactive,
 * thus does not employ a decision support system).
 *
 * @author MTomczyk
 */
public class ReportedInconsistencies extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * The DM's identifier (0, 1,...) in {@link DecisionSupportSystem}.
     */
    private final int _dmID;

    /**
     * The model system identifier (0, 1,...) in {@link DecisionMakerSystem}.
     */
    private final int _modelID;

    /**
     * Default constructor (assumes that DM's identifier = 0; model system identifier = 0; and smaller values are preferred)
     */
    public ReportedInconsistencies()
    {
        this(0, 0, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param dmID            the DM's identifier (0, 1,...) in {@link DecisionSupportSystem}
     * @param modelID         the model system identifier (0, 1,...) in {@link DecisionMakerSystem}
     * @param lessIsPreferred flag determining preference direction (true = less is preferred)
     */
    public ReportedInconsistencies(int dmID, int modelID, boolean lessIsPreferred)
    {
        super(lessIsPreferred);
        _dmID = dmID;
        _modelID = modelID;
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
    public double evaluate(EA ea)
    {
        ModelSystem<?> MS = Utils.getMS(ea, _dmID, _modelID);
        if (MS == null) return 0.0d;
        return MS.getNoFailedAttemptsDueToInconsistency();
    }

    /**
     * Returns string representation
     *
     * @return string representation: "REPORTED_INCONSISTENCIES_" + DM's id + "_" + model's ID..
     */
    @Override
    public String toString()
    {
        return "REPORTED_INCONSISTENCIES_DM" + _dmID + "_MODEL_" + _modelID;
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
        return new ReportedInconsistencies(_dmID, _modelID, _lessIsPreferred);
    }
}
