package y2025.ERS.common.indicators;

import ea.EA;
import ea.IEA;
import exception.TrialException;
import indicator.AbstractIndicator;
import indicator.IIndicator;
import model.constructor.Report;
import y2025.ERS.common.EAWrapperIterableSampler;
/**
 * Returns sampler's current no. compatible models.
 *
 * @author MTomczyk
 */
public class CompatibleModels extends AbstractIndicator implements IIndicator
{
    /**
     * Default constructor.
     */
    public CompatibleModels()
    {
        super("CM", false, (scenario, trialID) -> new CompatibleModels());
    }

    /**
     * Performs the evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     * @throws TrialException the method's signature allows for exception throw (trial level)
     */
    @Override
    public double evaluate(IEA ea) throws TrialException
    {
        if (!(ea instanceof EAWrapperIterableSampler<?> eaWrapper)) throw new TrialException("Invalid EA type", null, (Class<?>) null, _scenario, _trialID);
        Report<?> report = eaWrapper.getReport();
        return report._models.size();
    }
}
