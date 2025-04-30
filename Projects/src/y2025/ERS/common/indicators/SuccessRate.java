package y2025.ERS.common.indicators;

import ea.EA;
import exception.TrialException;
import indicator.AbstractIndicator;
import indicator.IIndicator;
import model.constructor.Report;
import y2025.ERS.common.EAWrapperIterableSampler;

/**
 * Returns sampler's current success rate.
 *
 * @author MTomczyk
 */
public class SuccessRate extends AbstractIndicator implements IIndicator
{
    /**
     * Default constructor.
     */
    public SuccessRate()
    {
        super("SR", false, (scenario, trialID) -> new SuccessRate());
    }

    /**
     * Performs the evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(EA ea) throws TrialException
    {
        if (!(ea instanceof EAWrapperIterableSampler<?> eaWrapper)) throw new TrialException("Invalid EA type", null, _scenario, _trialID);
        Report<?> report = eaWrapper.getReport();
        return report._successRateInConstructing;
    }
}
