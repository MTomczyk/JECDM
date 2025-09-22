package y2025.ERS.common;

import criterion.Criteria;
import ea.AbstractEA;
import ea.IEA;
import model.constructor.value.rs.ers.IterableERS;
import model.constructor.value.rs.ers.SortedModel;
import dmcontext.DMContext;
import ea.EA;
import ea.EATimestamp;
import exception.EAException;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.constructor.value.rs.AbstractRejectionSampling;
import model.constructor.value.rs.frs.IterableFRS;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Wrapper for {@link AbstractRejectionSampling} methods (viewed as {@link EA}). It is used to wrap {@link IterableFRS}
 * or {@link IterableERS} (other classes are not supported).
 *
 * @author MTomczyk
 */
public class EAWrapperIterableSampler<T extends AbstractValueInternalModel> extends AbstractEA implements IEA
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEA.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name                  name of the EA
         * @param id,                   unique ID of the EA
         * @param R                     random number generator.
         * @param computeExecutionTimes flag indicating whether the total execution time (as well as other
         *                              implementation-dependent times) are measured or not (in ms)
         * @param criteria              considered criteria
         */
        protected Params(String name, int id, IRandom R, boolean computeExecutionTimes, Criteria criteria)
        {
            super(name, id, R, computeExecutionTimes, criteria);
        }
    }

    /**
     * Sampler
     */
    private final AbstractRejectionSampling<T> _sampler;

    /**
     * Sampler viewed as iterable FRS.
     */
    private final IterableFRS<T> _itFRS;

    /**
     * Sampler viewed as iterable ERS.
     */
    private final IterableERS<T> _itERS;

    /**
     * Feedback to be used.
     */
    private final LinkedList<PreferenceInformationWrapper> _feedback;

    /**
     * Report on the most recent model construction process.
     */
    private Report<T> _report = null;

    /**
     * Iterations per generation.
     */
    private final int _iterationsPerGeneration;

    /**
     * Parameterized constructor.
     *
     * @param name                    sampler name
     * @param sampler                 sample
     * @param feedback                feedback to be used
     * @param R                       random number generator
     * @param iterationsPerGeneration iterations per generation
     */
    public EAWrapperIterableSampler(String name,
                                    AbstractRejectionSampling<T> sampler,
                                    LinkedList<PreferenceInformationWrapper> feedback,
                                    IRandom R,
                                    int iterationsPerGeneration)
    {
        super(new Params(name, 0, R, false, null));

        _sampler = sampler;
        if (_sampler instanceof IterableFRS<T>) _itFRS = (IterableFRS<T>) _sampler;
        else _itFRS = null;
        if (_sampler instanceof IterableERS<T>) _itERS = (IterableERS<T>) _sampler;
        else _itERS = null;

        _iterationsPerGeneration = iterationsPerGeneration;

        _feedback = feedback;
        // set default values
        _criteria = null;
        setPopulationSize(1);
        setOffspringSize(1);
        setObjectiveSpaceManager(null);
    }

    /**
     * Getter for the report associated with the most recent model construction process.
     *
     * @return report
     */
    public Report<T> getReport()
    {
        return _report;
    }

    /**
     * Setter for the report associated with the most recent model construction process.
     *
     * @param report report
     */
    public void setReport(Report<T> report)
    {
        _report = report;
    }

    /**
     * Overwrites the init method.
     *
     * @throws EAException the exception can be thrown
     */
    @Override
    public void init() throws EAException
    {
        updateCurrentTimestamp(new EATimestamp(0, 0));
        DMContext dmContext = new DMContext(null, LocalDateTime.now(), null, null,
                false, 0, null, _R);

        if ((_itFRS == null) && (_itERS == null)) throw new EAException("No valid sampler is wrapped", this.getClass());

        try
        {
            _report = new Report<>(dmContext);
            _sampler.registerDecisionMakingContext(dmContext);

            long pT = System.nanoTime();
            if (_itFRS != null) _itFRS.initializeStep(_report, _feedback);
            else _itERS.initializeStep(_report, _feedback);
            setExecutionTime(getExecutionTime() + ((double) (System.nanoTime() - pT)) / 1000000.0d);

            // Important: Generation = 0 involves a step to make statistics based not on the initialization
            step(getCurrentTimestamp(), _iterationsPerGeneration);

        } catch (ConstructorException e)
        {
            throw new EAException("Error occurred in the init phase (reason = " + e.getMessage() + ")", this.getClass());
        }
    }

    /**
     * Overwrites the step method.
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException the exception can be thrown
     */
    @Override
    public void step(EATimestamp timestamp) throws EAException
    {
        step(timestamp, _iterationsPerGeneration);
    }

    /**
     * Executes a step.
     *
     * @param timestamp generation; steady-state repeat
     * @param repeats   no. repeats
     * @throws EAException the exception can be thrown
     */
    private void step(EATimestamp timestamp, int repeats) throws EAException
    {
        if ((_itFRS == null) && (_itERS == null)) throw new EAException("No valid sampled is wrapped", this.getClass());

        updateCurrentTimestamp(timestamp);
        try
        {
            for (int i = 0; i < repeats; i++)
            {
                long pT = System.nanoTime();
                if (_itFRS != null)
                {
                    _itFRS.executeStep(_report, _feedback);
                    setExecutionTime(getExecutionTime() + ((double) (System.nanoTime() - pT)) / 1000000.0d);
                } else
                {
                    _itERS.executeStep(_report, _feedback);
                    setExecutionTime(getExecutionTime() + ((double) (System.nanoTime() - pT)) / 1000000.0d);
                    if (i == repeats - 1)
                    {
                        if (_report._models == null) _report._models = new ArrayList<>();
                        _report._models.clear();
                        for (SortedModel<T> t : _itERS.getModelsQueue().getQueue())
                            if (t._isCompatible) _report._models.add(t._model);
                    }

                }
                if (i == _iterationsPerGeneration - 1)
                    _report._successRateInConstructing = (double) (_report._acceptedNewlyConstructedModels) /
                            (_report._acceptedNewlyConstructedModels + _report._rejectedNewlyConstructedModels);
            }


        } catch (ConstructorException e)
        {
            throw new EAException("Error occurred in the init phase (reason = " + e.getMessage() + ")", this.getClass());
        }
    }

    /**
     * Auxiliary method that tells if all models stored in the report are compatible.
     *
     * @return true, if all
     */
    public boolean isExpectedNoCompatibleModelsFound()
    {
        if (_itFRS != null) return _report._compatibleModelsToSample == _report._models.size();
        else if (_itERS != null) return _itERS.getModelsQueue().areAllSortedModelsCompatible();
        return false;
    }
}
