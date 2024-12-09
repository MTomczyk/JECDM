package container.trial;

import exception.TrialException;

/**
 * Default extension of {@link AbstractTrialDataContainer}.
 *
 * @author MTomczyk
 */
public class TrialDataContainer extends AbstractTrialDataContainer
{
    /**
     * Parameterized constructor that also instantiates the data.
     *
     * @param p params container
     * @throws TrialException the exception can be thrown and passed to the main executor via the scenario executor
     */
    public TrialDataContainer(Params p) throws TrialException
    {
        super(p);
    }

}
