package container.trial;

import container.AbstractDataContainer;
import container.scenario.AbstractScenarioDataContainer;
import container.trial.initialziers.*;
import exception.TrialException;
import io.trial.BinarySaver;
import io.trial.ITrialSaver;
import random.IRandom;

import java.util.LinkedList;

/**
 * Factory object for creating extensions of {@link AbstractTrialDataContainer}.
 * This class and its main method are intended to be overwritten.
 *
 * @author MTomczyk
 */
public class TrialDataContainerFactory extends AbstractDataContainer
{
    /**
     * Params container
     */
    public static class Params extends AbstractDataContainer.Params
    {
        /**
         * Specifies a list of objects for storing per-trial data (one file per each indicator and saver used). The list
         * will be accompanied by a default binary saver (so it should not contain it and can be empty as it should include
         * some additional/optional savers).
         */
        public LinkedList<ITrialSaver> _additionalTrialSavers;

        /**
         * Provides auxiliary methods for data validation (for trial-level).
         */
        public Validator _validator;

        /**
         * Object for retrieving instances of {@link AbstractScenarioDataContainer}.
         */
        public IInstanceGetter _instanceGetter;

        /**
         * Object responsible for generating per-trial instances of evolutionary algorithms.
         */
        public IEAInitializer _eaInitializer;

        /**
         * Object responsible for generating per-trial instances of runners responsible for managing the evolutionary process.
         */
        public IRunnerInitializer _runnerInitializer;

        /**
         * Object responsible for initializing the per-scenario problem to be solved.
         */
        public IProblemBundleInitializer _problemInitializer;
    }


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    @SuppressWarnings("ReplaceNullCheck")
    public TrialDataContainerFactory(Params p)
    {
        super(p);
        _trialSavers = new LinkedList<>();
        _trialSavers.add(new BinarySaver());
        if (p._additionalTrialSavers != null) _trialSavers.addAll(p._additionalTrialSavers);
        if (p._instanceGetter != null) _instanceGetter = p._instanceGetter;
        else _instanceGetter = new DefaultInstanceGetter();
        if (p._eaInitializer != null) _eaInitializer = p._eaInitializer;
        else _eaInitializer = new DefaultEAInitializer();
        if (p._runnerInitializer != null) _runnerInitializer = p._runnerInitializer;
        else _runnerInitializer = new DefaultRunnerInitializer();
        if (p._problemInitializer != null) _problemInitializer = p._problemInitializer;
        else _problemInitializer = new DefaultProblemBundleInitializer();
    }

    /**
     * Specifies a list of objects for storing per-trial data (one file per each indicator and saver used).
     * The list is instantiated by default with a binary saver, which is sufficient at this level (trial).
     * Note that upon object initialization, the list will be automatically supplied with a binary saver.
     */
    private final LinkedList<ITrialSaver> _trialSavers;

    /**
     * The object for retrieving instances of {@link AbstractScenarioDataContainer}.
     */
    private final IInstanceGetter _instanceGetter;

    /**
     * Object responsible for generating per-trial instances of evolutionary algorithms.
     */
    private final IEAInitializer _eaInitializer;

    /**
     * Object responsible for generating per-trial instances of runners responsible for managing the evolutionary process.
     */
    private final IRunnerInitializer _runnerInitializer;

    /**
     * Object responsible for initializing the per-scenario problem to be solved.
     */
    private final IProblemBundleInitializer _problemInitializer;

    /**
     * Default getter for the trial data container instance. This method instantiates the params container and
     * delegates the trial data getter creation to {@link TrialDataContainerFactory#getInstance(AbstractTrialDataContainer.Params)}.
     * The latter method is intended to be overwritten.
     *
     * @param SDC     the scenario data container
     * @param trialID the unique (in scenario) ID assigned to the test run
     * @param R       random number generator assigned to this trial
     * @return instance of the trial data container
     * @throws TrialException the exception can be thrown and passed to the main executor via the scenario executor
     */
    public AbstractTrialDataContainer getInstance(AbstractScenarioDataContainer SDC, int trialID, IRandom R) throws TrialException
    {
        try
        {
            Validator validator = new Validator(SDC.getScenario(), trialID);
            validator.validateTrialSavers(_trialSavers);
            AbstractTrialDataContainer.Params p = new AbstractTrialDataContainer.Params(SDC, trialID);
            passParams(p);
            p._trialSavers = _trialSavers;
            p._validator = validator;
            p._eaInitializer = _eaInitializer;
            p._runnerInitializer = _runnerInitializer;
            p._problemInitializer = _problemInitializer;
            p._R = R;
            return getInstance(p);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, SDC.getScenario(), trialID);
        }
    }


    /**
     * Getter for the trial data container instance (intended to be overwritten).
     *
     * @param p params container
     * @return instance of the trial data container
     * @throws TrialException the exception can be thrown and passed to the main executor via the scenario executor
     */
    private AbstractTrialDataContainer getInstance(AbstractTrialDataContainer.Params p) throws TrialException
    {
        return _instanceGetter.getInstance(p);
    }

    /**
     * Auxiliary method for data release.
     */
    public void dispose()
    {
        super.dispose();
    }
}