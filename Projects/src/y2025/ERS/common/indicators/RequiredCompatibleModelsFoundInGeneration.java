package y2025.ERS.common.indicators;

import ea.EA;
import ea.IEA;
import exception.TrialException;
import indicator.AbstractIndicator;
import indicator.IIndicator;
import y2025.ERS.common.EAWrapperIterableSampler;

/**
 * Returns the time required to find the expected number of compatible models.
 *
 * @author MTomczyk
 */
public class RequiredCompatibleModelsFoundInGeneration extends AbstractIndicator implements IIndicator
{
    /**
     * Flag indicating whether the condition was already triggered.
     */
    private boolean _triggered = false;

    /**
     * Field storing the result (-1 when found yet).
     */
    private int _result = -1;

    /**
     * Default constructor.
     */
    public RequiredCompatibleModelsFoundInGeneration()
    {
        super("REQ_COMP_MODELS_FOUND_GEN", true, (scenario, trialID) -> new RequiredCompatibleModelsFoundInGeneration());
    }

    /**
     * Performs the evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment (-1 when not found yet)
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(IEA ea) throws TrialException
    {
        if (!(ea instanceof EAWrapperIterableSampler<?> eaWrapper))
            throw new TrialException("Invalid EA type", null, (Class<?>) null, _scenario, _trialID);

        if ((!_triggered) && (eaWrapper.isExpectedNoCompatibleModelsFound()))
        {
            _triggered = true;
            _result = ea.getCurrentGeneration();
        }

        return _result;
    }
}
