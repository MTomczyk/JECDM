package executor.complex.case1;

import container.scenario.AbstractScenarioDataContainer;
import container.trial.TrialDataContainerFactory;
import executor.ExperimentPerformer;
import executor.ScenarioExecutor;
import summary.ScenarioSummary;

/**
 * Dummy extension of {@link ExperimentPerformer} (for testing).
 * Instantiates dummy scenario executor
 *
 * @author MTomczyk
 */
public class DummyExperimentPerformer extends ExperimentPerformer
{
    /**
     * Dummy params container.
     */
    public static class Params extends ExperimentPerformer.Params
    {
        /**
         * Delay between subsequent generations (in milliseconds).
         */
        protected int _delay = 500;

        /**
         * An array of flags indicating whether a trial should be forced to cast an exception (i-th object is assumed to be linked with a trial with id = i).
         */
        protected boolean[] _throwException = null;
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public DummyExperimentPerformer(Params p)
    {
        super(p);
        _delay = p._delay;
        _throwException = p._throwException;
    }


    /**
     * Delay between subsequent generations (in milliseconds).
     */
    private final int _delay;

    /**
     * An array of flags indicating whether a trial should be forced to cast an exception (i-th object is assumed to be linked with a trial with id = i).
     */
    private final boolean[] _throwException;

    /**
     * Auxiliary method that creates the scenario executor object.
     *
     * @param SDC      scenario data container that is to be linked with the executor
     * @param TDCF     trial data container factory
     * @param sSummary scenario summary object to be filled
     * @return scenario executor object
     */
    @Override
    protected ScenarioExecutor instantiateScenarioExecutor(AbstractScenarioDataContainer SDC, TrialDataContainerFactory TDCF, ScenarioSummary sSummary)
    {
        return new DummyScenarioExecutor(SDC, TDCF, sSummary, _log, _delay, _throwException);
    }

}
