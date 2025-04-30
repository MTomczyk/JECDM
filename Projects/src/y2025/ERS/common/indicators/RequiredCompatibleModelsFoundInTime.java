package y2025.ERS.common.indicators;

import ea.EA;
import exception.TrialException;
import indicator.AbstractIndicator;
import indicator.IIndicator;
import y2025.ERS.common.EAWrapperIterableSampler;

/**
 * Returns the time required to find the expected number of compatible models.
 *
 * @author MTomczyk
 */
public class RequiredCompatibleModelsFoundInTime extends AbstractIndicator implements IIndicator
{
    /**
     * Flag indicating whether the condition was already triggered.
     */
    private boolean _triggered = false;

    /**
     * Field storing the result (-1 when not found yet).
     */
    private double _result = -1.0d;

    /**
     * Default constructor.
     */
    public RequiredCompatibleModelsFoundInTime()
    {
        super("REQ_COMP_MODELS_FOUND_TIME", true, (scenario, trialID) -> new RequiredCompatibleModelsFoundInTime());
    }

    /**
     * Performs the evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment (-1 when not found yet)
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(EA ea) throws TrialException
    {
        if (!(ea instanceof EAWrapperIterableSampler<?> eaWrapper)) return -1.0d;
        if ((!_triggered) && (eaWrapper.isExpectedNoCompatibleModelsFound()))
        {
            _triggered = true;
            _result = ea.getExecutionTime();
        }
        return _result;
    }
}
