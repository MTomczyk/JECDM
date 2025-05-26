package executor;

import container.Containers;
import container.scenario.AbstractScenarioDataContainer;
import container.scenario.Validator;
import exception.AbstractExperimentationException;
import exception.GlobalException;
import exception.ScenarioException;
import exception.TrialException;
import indicator.IIndicator;
import io.scenario.IScenarioSaver;
import io.scenario.ScenarioSavers;
import io.trial.TLPITrialWrapper;
import io.trial.TLPerIndicator;
import io.utils.pusher.IPusher;
import io.utils.pusher.ScenarioPusher;
import scenario.Scenario;
import summary.ScenarioSummary;
import summary.Summary;
import summary.TrialAggregatorScenarioSummary;
import summary.TrialAggregatorSummary;
import utils.Level;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class can be considered an overlay to {@link ExperimentPerformer} and explicitly uses the data structures and
 * processing flow imposed by that class. This class can be run after the {@link ExperimentPerformer} completes
 * processing (successfully) and all the per-trial data are stored in files. It generates scenario-level files that
 * aggregate trial outcomes.
 *
 * @author MTomczyk
 */
public class ScenariosSummarizer extends ExperimentPerformer
{
    /**
     * Params container.
     */
    public static class Params extends ExperimentPerformer.Params
    {
        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor. Instantiates the containers/factories as imposed by the input {@link Containers} object.
         *
         * @param containers provides containers/factories
         */
        public Params(Containers containers)
        {
            super(containers);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ScenariosSummarizer(Params p)
    {
        super(p);
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ScenariosSummarizer(ExperimentPerformer.Params p)
    {
        super(p);
    }


    /**
     * Auxiliary method for printing the initial log.
     */
    @Override
    protected void doInitLog()
    {
        _log.log("Trial aggregation starts", Level.Global, _indent);
    }


    /**
     * Auxiliary method for contracting the main summary object.
     *
     * @return summary object
     */
    @Override
    protected Summary instantiateSummary()
    {
        return new TrialAggregatorSummary();
    }

    /**
     * Auxiliary method for instantiating scenario summary objects.
     *
     * @param summary the global summary object
     */
    @Override
    protected void instantiateScenarioSummaries(Summary summary)
    {
        summary.setScenariosSummaries(new ScenarioSummary[_GDC.getScenarios().getScenarios().length]);
        for (int sc = 0; sc < _GDC.getScenarios().getScenarios().length; sc++)
            summary.getScenariosSummaries()[sc] = new TrialAggregatorScenarioSummary(_GDC.getScenarios().getScenarios()[sc]);
    }

    /**
     * The main method for processing a single scenario (post-stage).
     *
     * @param status          current processing status
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    @Override
    protected Status postProcessScenario(Status status, Scenario scenario, ScenarioSummary scenarioSummary)
    {
        scenarioSummary.setStopTimestamp(LocalDateTime.now());
        return status;
    }

    /**
     * The main method for processing a single scenario (main-stage).
     * This overwriting method attempts to produce scenario-level summarizing files.
     *
     * @param status          current processing status
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    @Override
    protected Status mainProcessScenario(Status status, Scenario scenario, ScenarioSummary scenarioSummary)
    {
        AbstractScenarioDataContainer SDC = getScenarioDataContainerInstance(scenario, scenarioSummary);
        if (SDC == null) return Status.TERMINATED;
        _log.log("Experimental scenario = " + SDC + " begins processing", Level.Scenario, _indent);

        LinkedList<IScenarioSaver> scenarioSavers = getScenarioSavers(SDC, scenarioSummary);
        if (scenarioSavers == null) return Status.TERMINATED;

        TLPITrialWrapper loaders = getAndOpenBinaryLoaders(SDC, scenarioSummary);
        if (loaders == null)
        {
            closeBinaryLoaders(SDC.getBinaryLoaders(), scenarioSummary);
            return Status.TERMINATED;
        }

        //SAVER FILES CREATE FILES
        ScenarioSavers savers = new ScenarioSavers(scenarioSavers);
        IPusher pusher = new ScenarioPusher(savers);

        if (!createSaversFiles(savers, scenarioSummary))
        {
            closeBinaryLoaders(loaders, scenarioSummary);
            closeSavers(savers, scenarioSummary);
            return Status.TERMINATED;
        }

        // BEGIN PROCESSING
        int generations = SDC.getGenerations();

        for (int indicatorID = 0; indicatorID < SDC.getIndicators().length; indicatorID++)
        {
            IIndicator indicator = SDC.getIndicators()[indicatorID];
            if (!notifySaversIndicatorProcessingBegins(savers, indicator, generations)) break;

            try
            {
                Utils.loadAndPushBinaryData(_GDC, loaders, pusher, scenario, SDC, generations, indicatorID, indicator);

            } catch (ScenarioException e)
            {
                _log.log(e.getMessage(), Level.Scenario, _indent);
                break;
            }

            if (!notifySaversIndicatorProcessingEnds(savers, indicator)) break;
        }

        if (!closeSavers(savers, scenarioSummary))
        {
            closeBinaryLoaders(loaders, scenarioSummary);
            return Status.TERMINATED;
        }
        if (!closeBinaryLoaders(loaders, scenarioSummary)) return Status.TERMINATED;

        _log.log("Experimental scenario = " + SDC + " ends processing", Level.Scenario, _indent);
        SDC.dispose();

        return Status.COMPLETED;
    }


    /**
     * Auxiliary method for crating an instance of a scenario data container
     *
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled
     * @return instance of a scenario data container (null if an exception occurred)
     */
    private AbstractScenarioDataContainer getScenarioDataContainerInstance(Scenario scenario, ScenarioSummary scenarioSummary)
    {
        try
        {
            return _SDCF.getInstance(_GDC, scenario, new Validator(scenario, true));
        } catch (ScenarioException e)
        {
            processTermination("Scenario data container could not be instantiated " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return null;
        }
    }

    /**
     * Auxiliary method for setting some relevant summary fields when terminating.
     *
     * @param msg             message to be printed via {@link utils.Log}
     * @param e               exception that caused the termination
     * @param scenarioSummary scenario summary being filled
     */
    private void processTermination(String msg, AbstractExperimentationException e, ScenarioSummary scenarioSummary)
    {
        scenarioSummary.setExceptionMessage(_log.getTerminationMessage(e));
        scenarioSummary.setTerminationDueToException(true);
        _log.log(msg, Level.Scenario, _indent);
    }

    /**
     * Auxiliary method for creating scenario savers (copied/cloned from the reference ones).
     *
     * @param SDC             scenario data container
     * @param scenarioSummary scenario summary object to be filled
     * @return list of scenario savers (cloned from the reference ones and adjusted to the scenario being currently
     * processed); returns null if some exception occurs
     */
    private LinkedList<IScenarioSaver> getScenarioSavers(AbstractScenarioDataContainer SDC, ScenarioSummary scenarioSummary)
    {
        LinkedList<IScenarioSaver> scenarioSavers = new LinkedList<>();
        try
        {
            if (_GDC.getReferenceScenarioSavers() != null)
            {
                for (IScenarioSaver s : _GDC.getReferenceScenarioSavers())
                {
                    scenarioSavers.add(s.getInstance(SDC.getMainPath(), SDC.toString(), SDC.getScenario(),
                            _GDC.getTrialIDs(), SDC.getIndicators(), SDC.getStatisticFunctions()));
                }
            }

            validateReferenceScenarioSavers(SDC.getScenario(), scenarioSavers);

        } catch (ScenarioException e)
        {
            processTermination("Could not create scenario saver instances " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return null;
        }
        return scenarioSavers;
    }

    /**
     * Auxiliary method for getting trial-level binary loaders and opening them.
     *
     * @param SDC             scenario data container
     * @param scenarioSummary scenario summary to be filled
     * @return wrapper for {@link TLPerIndicator}; the method returns null if some exception occurs
     */
    private TLPITrialWrapper getAndOpenBinaryLoaders(AbstractScenarioDataContainer SDC, ScenarioSummary scenarioSummary)
    {
        TLPITrialWrapper loaders = SDC.getBinaryLoaders();
        try
        {
            _log.log("Opening per-trial binary files", Level.Scenario, _indent);
            loaders.openAllFiles();
        } catch (TrialException e)
        {
            processTermination("Binary loaders could not be opened " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return null;
        }
        return loaders;
    }

    /**
     * Auxiliary method for creating savers' files.
     *
     * @param savers          scenario savers (wrapped by {@link ScenarioSavers})
     * @param scenarioSummary scenario summary object to be filled
     * @return true if the operation ends successfully, false otherwise
     */
    private boolean createSaversFiles(ScenarioSavers savers, ScenarioSummary scenarioSummary)
    {
        try
        {
            _log.log("Creating scenario result files", Level.Scenario, _indent);
            savers.createFiles();
        } catch (ScenarioException e)
        {
            processTermination("Could not create scenario result files " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return false;
        }
        return true;
    }

    /**
     * Auxiliary method for closing scenario savers.
     *
     * @param savers          scenario savers (wrapper)
     * @param scenarioSummary scenario summary object to be filled
     * @return true if the closing operation ends successfully, false otherwise
     */
    private boolean closeSavers(ScenarioSavers savers, ScenarioSummary scenarioSummary)
    {
        try
        {
            _log.log("Closing scenario result files", Level.Scenario, _indent);
            savers.closeFiles();
        } catch (ScenarioException e)
        {
            processTermination("Could not close scenario result files " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return false;
        }
        return true;
    }

    /**
     * Auxiliary method for closing binary loaders.
     *
     * @param loaders         binary loaders (wrapper)
     * @param scenarioSummary scenario summary object to be filled
     * @return true if the closing operation ends successfully, false otherwise
     */
    private boolean closeBinaryLoaders(TLPITrialWrapper loaders, ScenarioSummary scenarioSummary)
    {
        try
        {
            _log.log("Closing per-trial binary files", Level.Scenario, _indent);
            loaders.closeAllFiles();
        } catch (TrialException e)
        {
            processTermination("Binary loaders could not be closed " + e.getDetailedReasonMessage(), e, scenarioSummary);
            return false;
        }
        return true;
    }


    /**
     * Auxiliary method for notifying the savers that the per-indicator processing begins.
     *
     * @param savers      savers
     * @param indicator   indicator
     * @param generations the total number of generations throughout which the input data was collected
     * @return true, if the notification was successful; false in the case of any exception
     */
    private boolean notifySaversIndicatorProcessingBegins(ScenarioSavers savers, IIndicator indicator, int generations)
    {
        try
        {
            savers.notifyIndicatorProcessingBegins(indicator, generations);
        } catch (ScenarioException e)
        {
            _log.log("Error occurred when notifying savers (indicator processing begins) = " + indicator.getName() +
                    " " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return false;
        }
        return true;
    }

    /**
     * Auxiliary method for notifying the savers that the per-indicator processing ends.
     *
     * @param savers    savers
     * @param indicator indicator
     * @return true, if the notification was successful; false in the case of any exception
     */
    private boolean notifySaversIndicatorProcessingEnds(ScenarioSavers savers, IIndicator indicator)
    {
        try
        {
            savers.notifyIndicatorProcessingEnds();
        } catch (ScenarioException e)
        {
            _log.log("Error occurred when notifying savers (indicator processing ends) = " + indicator.getName() +
                    " " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return false;
        }
        return true;
    }


    /**
     * Auxiliary method for validating the reference scenario savers.
     *
     * @param scenario currently processed scenario
     * @param savers   savers being examined
     * @throws ScenarioException the exception will be thrown if the savers are not valid
     */
    private void validateReferenceScenarioSavers(Scenario scenario, LinkedList<IScenarioSaver> savers) throws ScenarioException
    {
        if (savers == null)
        {
            throw new ScenarioException("Reference scenario savers are not provided (the array is null)", null, this.getClass(), scenario);
        }
        if (savers.isEmpty())
            throw new ScenarioException("Reference scenario savers are not provided (the array is empty)", null, this.getClass(), scenario);
        for (IScenarioSaver s : savers)
            if (s == null)
                throw new ScenarioException("One of the provided scenario savers is null", null, this.getClass(), scenario);
        Set<String> names = new HashSet<>(savers.size());
        for (IScenarioSaver s : savers)
        {
            if (names.contains(s.getFileSuffix()))
                throw new ScenarioException("Scenario savers' suffixes are not unique (" + s.getFileSuffix() + ")", null, this.getClass(), scenario);
            names.add(s.getFileSuffix());
        }
    }


    /**
     * Auxiliary method that creates the main folder (the overwritten method does nothing).
     *
     * @throws GlobalException the exception will be thrown if the main folder cannot be created
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    protected void createMainFolder() throws GlobalException
    {
        // do nothing
    }

    /**
     * Auxiliary method for validating the TDCF (scenario data container factory); (the overwritten method does nothing)
     *
     * @throws GlobalException global exception is thrown when the TDCF is not provided
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    protected void validateTDCF() throws GlobalException
    {
        // do nothing
    }
}
