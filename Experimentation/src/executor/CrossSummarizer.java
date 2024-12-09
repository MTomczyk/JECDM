package executor;

import container.Containers;
import container.scenario.AbstractScenarioDataContainer;
import container.scenario.Validator;
import exception.CrossedScenariosException;
import exception.GlobalException;
import exception.ScenarioException;
import exception.TrialException;
import indicator.IIndicator;
import io.cross.CrossSavers;
import io.cross.ICrossSaver;
import io.trial.TLPITrialWrapper;
import io.trial.TLPerIndicator;
import io.utils.pusher.CrossPusher;
import io.utils.pusher.IPusher;
import scenario.CrossedScenarios;
import scenario.CrossedScenariosGenerator;
import scenario.CrossedSetting;
import scenario.Scenario;
import summary.CrossedExaminerSummary;
import summary.CrossedScenariosSummary;
import summary.Summary;
import unified.UnifiedIndicators;
import unified.UnifiedStatistics;
import utils.Level;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class can be considered an overlay to {@link ExperimentPerformer} and explicitly uses the data structures and
 * processing flow imposed by that class. This class can be run after the {@link ExperimentPerformer} completes
 * processing (successfully) and all the per-trial data are stored in files. It performs a cross-analysis and stores
 * the results.
 *
 * @author MTomczyk
 */
public class CrossSummarizer extends ExperimentPerformer
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
    public CrossSummarizer(Params p)
    {
        super(p);
    }


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public CrossSummarizer(ExperimentPerformer.Params p)
    {
        super(p);
    }

    /**
     * Additional field for keeping the summary object (helps to avoid casting).
     */
    private CrossedExaminerSummary _crossSummary = null;


    /**
     * Auxiliary method for printing the initial log.
     */
    @Override
    protected void doInitLog()
    {
        _log.log("Cross examination starts starts", Level.Global, _indent);
    }


    /**
     * Auxiliary method for contracting the main summary object.
     *
     * @return summary object
     */
    @Override
    protected Summary instantiateSummary()
    {
        _crossSummary = new CrossedExaminerSummary();
        return _crossSummary;
    }

    /**
     * Auxiliary method for instantiating scenario summary objects.
     *
     * @param summary the global summary object
     */
    @Override
    protected void instantiateScenarioSummaries(Summary summary)
    {
        summary.setScenariosSummaries(null);
    }

    /**
     * Auxiliary method for instantiating crossed scenarios summary objects.
     *
     * @param CSs crossed scenarios objects
     */
    protected void instantiateCrossedScenariosSummaries(CrossedScenarios[] CSs)
    {
        CrossedScenariosSummary[] css = new CrossedScenariosSummary[CSs.length];
        for (int i = 0; i < CSs.length; i++) css[i] = new CrossedScenariosSummary(CSs[i]);
        _crossSummary.setScenariosSummaries(css);
    }

    /**
     * The main method for processing cross scenarios.
     *
     * @param summary summary object to be filled throughout the execution
     * @throws GlobalException global exception can be thrown and propagated higher
     */
    @Override
    protected void processScenarios(Summary summary) throws GlobalException
    {
        _log.log("Beginning processing the crossed scenarios", Level.Global, _indent);

        validate();

        int completedScenarios = 0;
        int terminatedScenarios = 0;

        _log.log("Validating crossed settings", Level.Global, _indent);
        CrossedSetting[] crossedSettings = getValidCrossedSettings();

        _log.log("Getting crossed scenarios", Level.Global, _indent);
        CrossedScenarios[] CSs = getCrossedScenarios(crossedSettings);

        _log.log("Instantiating crossed scenarios summaries", Level.Global, _indent);
        instantiateCrossedScenariosSummaries(CSs);

        _log.log("Partitioning reference cross-savers", Level.Global, _indent);
        HashMap<Integer, LinkedList<ICrossSaver>> pRefCrossSavers = generatePartitionedReferenceCrossSavers();

        for (int cs = 0; cs < CSs.length; cs++) {
            CrossedScenariosSummary css = _crossSummary.getCrossedScenariosSummaries()[cs];
            try {
                LinkedList<ICrossSaver> referenceSavers = pRefCrossSavers.get(CSs[cs].getLevel());
                if ((referenceSavers == null) || (referenceSavers.isEmpty())) {
                    throw new GlobalException("Could not determine the reference cross-savers for crossed scenarios = " +
                            CSs[cs].getStringRepresentation() + " (level = " + CSs[cs].getLevel() + ")", this.getClass());
                }

                try {
                    processCrossedScenario(CSs[cs], referenceSavers, css);
                } catch (CrossedScenariosException e) {
                    throw new GlobalException(e.getMessage(), this.getClass(), e);
                }

            } catch (GlobalException e) {
                _log.log("Could not process crossed scenarios " + e.getDetailedReasonMessage(), Level.Global, _indent);
                terminatedScenarios++;
                css.setStartTimestamp(LocalDateTime.now());
                css.setExceptionMessage(_log.getTerminationMessage(e));
                css.setTerminationDueToException(true);
                css.setStopTimestamp(LocalDateTime.now());
                continue;
            }
            completedScenarios++;
        }

        summary.setCompletedScenarios(completedScenarios);
        summary.setTerminatedScenarios(terminatedScenarios);
        summary.setSkippedScenarios(0);
    }

    /**
     * The main method for processing a single crossed scenario.
     *
     * @param crossedScenarios        the crossed scenarios to be processed
     * @param referenceSavers         reference file savers
     * @param crossedScenariosSummary scenario summary object to be filled
     * @throws CrossedScenariosException the global-level exception can be thrown and propagated higher
     */
    private void processCrossedScenario(CrossedScenarios crossedScenarios,
                                        LinkedList<ICrossSaver> referenceSavers,
                                        CrossedScenariosSummary crossedScenariosSummary) throws CrossedScenariosException
    {
        _log.log("Processing crossed scenarios = " + crossedScenarios.getStringRepresentation(), Level.CrossedScenarios, _indent);
        crossedScenariosSummary.setStartTimestamp(LocalDateTime.now());
        String path = createCrossedScenarioFolder(crossedScenarios);

        _log.log("Instantiating scenario data containers", Level.CrossedScenarios, _indent);
        AbstractScenarioDataContainer[] SDCs = getInstantiatedScenarioDataContainers(crossedScenarios);

        _log.log("Getting cross-savers", Level.CrossedScenarios, _indent);
        CrossSavers savers = getCrossSavers(referenceSavers, path, crossedScenarios, crossedScenariosSummary);

        _log.log("Constructing unified indicators and statistics", Level.CrossedScenarios, _indent);
        UnifiedIndicators uIndicators = new UnifiedIndicators(SDCs, _GDC.getUnifiedIndicatorsNames(), crossedScenarios);
        UnifiedStatistics uStatistics = new UnifiedStatistics(SDCs, _GDC.getUnifiedStatisticFunctionsNames(), crossedScenarios);
        savers.notifyAboutUnifiedData(uIndicators, uStatistics);

        _log.log("Savers: creating files", Level.CrossedScenarios, _indent);
        savers.createFiles();

        _log.log("Savers: notifying processing begins", Level.CrossedScenarios, _indent);
        savers.notifyProcessingBegins();

        int completedScenarios = 0;
        int terminatedScenarios = 0;
        int skippedScenarios = 0;
        LinkedList<String[]> crossedScenariosExceptionMessages = new LinkedList<>();


        for (int s = 0; s < crossedScenarios.getReferenceScenariosSorted().length; s++) {

            try {
                Scenario scenario = crossedScenarios.getReferenceScenariosSorted()[s];
                _log.log("Processing scenario = " + scenario, Level.CrossedScenarios, _indent);

                if ((scenario.isDisabled()) || (checkScenarioDisablingConditions(scenario))) {
                    _log.log("Skipping scenario = " + scenario, Level.CrossedScenarios, _indent);
                    skippedScenarios++;
                    continue;
                }

                savers.notifyScenarioProcessingBegins(scenario, SDCs[s]);
                processScenario(savers, scenario, SDCs[s], crossedScenarios);
                savers.notifyScenarioProcessingEnds();
            } catch (CrossedScenariosException e) {
                terminatedScenarios++;
                crossedScenariosExceptionMessages.add(_log.getTerminationMessage(e));
                continue;
            }
            completedScenarios++;
        }

        crossedScenariosSummary.setCompletedScenarios(completedScenarios);
        crossedScenariosSummary.setTerminatedScenarios(terminatedScenarios);
        crossedScenariosSummary.setSkippedScenarios(skippedScenarios);
        crossedScenariosSummary.setCrossedScenariosExceptionMessages(crossedScenariosExceptionMessages);

        _log.log("Savers: notifying processing ends", Level.CrossedScenarios, _indent);
        savers.notifyProcessingEnds();

        _log.log("Savers: closing files", Level.CrossedScenarios, _indent);
        savers.closeFiles();
        crossedScenariosSummary.setStopTimestamp(LocalDateTime.now());
    }

    /**
     * The primary method for loading scenario-related binary files and pushing the data cross-savers.
     *
     * @param crossSavers      cross saver objects
     * @param scenario         scenario being processed
     * @param SDC              scenario data container linked to the scenario being processed
     * @param crossedScenarios crossed scenarios
     * @throws CrossedScenariosException crossed scenarios exception can be thrown and propagated higher
     */
    private void processScenario(CrossSavers crossSavers,
                                 Scenario scenario,
                                 AbstractScenarioDataContainer SDC,
                                 CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        IPusher pusher = new CrossPusher(crossSavers);

        _log.log("Loading binary files", Level.CrossedScenarios, _indent);
        TLPITrialWrapper loaders = getAndOpenBinaryLoaders(SDC);
        if (loaders == null) {
            closeBinaryLoaders(SDC.getBinaryLoaders());
            throw new CrossedScenariosException("Could not load binary files", this.getClass(), crossedScenarios);
        }

        int generations = SDC.getGenerations();

        for (int indicatorID = 0; indicatorID < SDC.getIndicators().length; indicatorID++) {
            IIndicator indicator = SDC.getIndicators()[indicatorID];
            if (!notifySaversIndicatorProcessingBegins(crossSavers, indicator)) break;

            try {
                Utils.loadAndPushBinaryData(_GDC, loaders, pusher, scenario, SDC, generations, indicatorID, indicator);

            } catch (ScenarioException e) {
                _log.log("Could not push data to files for indicator = " + indicator.getName() +
                        " " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
                break;
            }

            if (!notifySaversIndicatorProcessingEnds(crossSavers)) break;
        }

        _log.log("Closing binary files", Level.CrossedScenarios, _indent);
        if (!closeBinaryLoaders(loaders))
            throw new CrossedScenariosException("Could not close binary files", this.getClass(), crossedScenarios);
    }

    /**
     * Auxiliary method for notifying the savers that the per-indicator processing begins.
     *
     * @param crossSavers savers
     * @param indicator   indicator
     * @return true, if the notification was successful; false in the case of any exception
     */
    private boolean notifySaversIndicatorProcessingBegins(CrossSavers crossSavers, IIndicator indicator)
    {
        try {
            crossSavers.notifyIndicatorProcessingBegins(indicator);
        } catch (CrossedScenariosException e) {
            _log.log("Error occurred when notifying savers (indicator processing begins) = " + indicator.getName() +
                    " " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return false;
        }
        return true;
    }

    /**
     * Auxiliary method for notifying the savers that the per-indicator processing ends.
     *
     * @param crossSavers savers
     * @return true, if the notification was successful; false in the case of any exception
     */
    private boolean notifySaversIndicatorProcessingEnds(CrossSavers crossSavers)
    {
        try {
            crossSavers.notifyIndicatorProcessingEnds();
        } catch (CrossedScenariosException e) {
            _log.log("Error occurred when notifying savers (indicator processing ends)" +
                    " " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return false;
        }
        return true;
    }

    /**
     * Auxiliary method for getting trial-level binary loaders and opening them.
     *
     * @param SDC scenario data container
     * @return wrapper for {@link TLPerIndicator}; the method returns null if some exception occurs
     */
    private TLPITrialWrapper getAndOpenBinaryLoaders(AbstractScenarioDataContainer SDC)
    {
        TLPITrialWrapper loaders = SDC.getBinaryLoaders();
        try {
            _log.log("Opening per-trial binary files", Level.Scenario, _indent);
            loaders.openAllFiles();
        } catch (TrialException e) {
            _log.log("Binary loaders could not be opened " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return null;
        }
        return loaders;
    }

    /**
     * Auxiliary method for closing binary loaders.
     *
     * @param loaders binary loaders (wrapper)
     * @return true if the closing operation ends successfully, false otherwise
     */
    private boolean closeBinaryLoaders(TLPITrialWrapper loaders)
    {
        try {
            _log.log("Closing per-trial binary files", Level.Scenario, _indent);
            loaders.closeAllFiles();
        } catch (TrialException e) {
            _log.log("Binary loaders could not be closed " + e.getDetailedReasonMessage(), Level.Scenario, _indent);
            return false;
        }
        return true;
    }


    /**
     * Auxiliary method for creating instantiated scenario data containers organized in an array, where each element
     * explicitly corresponds to sorted scenarios maintained by {@link CrossedScenarios}.
     *
     * @param crossedScenarios input crossed scenarios
     * @return scenario data containers
     * @throws CrossedScenariosException crossed scenarios exception can be thrown and propagated higher
     */
    private AbstractScenarioDataContainer[] getInstantiatedScenarioDataContainers(CrossedScenarios crossedScenarios)
            throws CrossedScenariosException
    {
        AbstractScenarioDataContainer[] SDCs =
                new AbstractScenarioDataContainer[crossedScenarios.getReferenceScenariosSorted().length];
        for (int s = 0; s < crossedScenarios.getReferenceScenariosSorted().length; s++) {
            try {
                Scenario scenario = crossedScenarios.getReferenceScenariosSorted()[s];
                SDCs[s] = _SDCF.getInstance(_GDC, scenario, new Validator(scenario, true));
            } catch (ScenarioException e) {
                throw new CrossedScenariosException(e.getMessage(), this.getClass(), e, crossedScenarios);
            }
        }
        return SDCs;
    }

    /**
     * Auxiliary method for creating a wrapper for savers (cloned from the reference ones).
     *
     * @param referenceSavers         reference savers
     * @param path                    path to the result folder
     * @param crossedScenarios        crossed scenarios being currently processed
     * @param crossedScenariosSummary crossed scenarios summary
     * @return wrapper for the cross saves
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown and propagated higher
     */
    private CrossSavers getCrossSavers(LinkedList<ICrossSaver> referenceSavers,
                                       String path,
                                       CrossedScenarios crossedScenarios,
                                       CrossedScenariosSummary crossedScenariosSummary) throws CrossedScenariosException
    {
        LinkedList<ICrossSaver> savers = new LinkedList<>();
        LinkedList<String> skippedSavers = new LinkedList<>();
        crossedScenariosSummary.setSkippedSavers(skippedSavers);

        for (ICrossSaver cs : referenceSavers) {
            if (cs.shouldBeSkipped(crossedScenarios)) {
                _log.log("Skipping getting saver's instance (" + cs.getDefaultName() + ")", Level.CrossedScenarios, _indent);
                skippedSavers.add(cs.getDefaultName());
                continue;
            }

            try {
                ICrossSaver saver = cs.getInstance(path, crossedScenarios.getStringRepresentation(), crossedScenarios);
                savers.add(saver);
            } catch (CrossedScenariosException e) {
                _log.log("Was not able to instantiate saver (" + cs.getDefaultName() + ")", Level.CrossedScenarios, _indent);
                skippedSavers.add(cs.getDefaultName());
            }
        }
        return new CrossSavers(savers);
    }

    /**
     * Auxiliary method for partitioning the reference cross-savers based on their dedicated levels.
     *
     * @return partitioned cross-savers (associative array: key = level; value = list of matching savers)
     */
    private HashMap<Integer, LinkedList<ICrossSaver>> generatePartitionedReferenceCrossSavers()
    {
        HashMap<Integer, LinkedList<ICrossSaver>> p = new HashMap<>();
        for (ICrossSaver cs : _GDC.getReferenceCrossSavers()) {
            int lv = cs.getDedicatedLevel();
            if (!p.containsKey(lv)) p.put(lv, new LinkedList<>());
            p.get(lv).add(cs);
        }
        return p;
    }

    /**
     * Auxiliary method for creating a folder where the crossed results will be stored.
     *
     * @param crossedScenarios crossed scenarios being processed
     * @return the path leading to the folder where the results of the crossed scenarios should be stored
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown and propagated higher
     */
    private String createCrossedScenarioFolder(CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        String path = _GDC.getMainPath() + File.separatorChar + _GDC.getCrossedFolderName();
        File file = new File(path);
        if (!file.exists()) {
            boolean created = file.mkdir();
            if (!created)
                throw new CrossedScenariosException("Could not create the the folder = " + path, this.getClass(), crossedScenarios);
        }

        path = path + File.separatorChar + crossedScenarios.getStringRepresentation();
        file = new File(path);
        if (!file.exists()) {
            boolean created = file.mkdir();
            if (!created)
                throw new CrossedScenariosException("Could not create the the folder = " + path, this.getClass(), crossedScenarios);
        }

        _log.log("The folder for storing results of " + crossedScenarios.getStringRepresentation() +
                " was created successfully or already existed (" + path + ")", Level.Global, _indent);

        return path;
    }

    /**
     * Auxiliary method for creating and validating crossed scenarios to be processed.
     *
     * @param crossedSettings validated crossed settings
     * @return crossed scenarios to be processed
     * @throws GlobalException global-level exception can be thrown and propagated higher.
     */
    private CrossedScenarios[] getCrossedScenarios(CrossedSetting[] crossedSettings) throws GlobalException
    {
        CrossedScenariosGenerator generator = new CrossedScenariosGenerator();
        CrossedScenarios[] CSs;
        try {
            CSs = generator.generateCrossedScenarios(_GDC.getScenarios(), crossedSettings);
        } catch (GlobalException e) {
            throw new GlobalException("Crossed scenarios could not be generated " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        if (CSs == null)
            throw new GlobalException("There are no crossed scenarios to be processed (the array is null)", this.getClass());
        if (CSs.length == 0)
            throw new GlobalException("There are no crossed scenarios to be processed (the array is empty)", this.getClass());

        return CSs;
    }

    /**
     * Auxiliary method for retrieving valid cross settings.
     *
     * @return validated crossed settings
     * @throws GlobalException global-level exception can be thrown and propagated higher.
     */
    private CrossedSetting[] getValidCrossedSettings() throws GlobalException
    {
        LinkedList<CrossedSetting> validCrossedSettings = new LinkedList<>();
        for (CrossedSetting cs : _GDC.getCrossedSettings()) {
            try {
                cs.instantiateSetting(_GDC.getScenarios());
            } catch (GlobalException e) {
                _log.log("Could not instantiate crossed settings " + e.getDetailedReasonMessage(), Level.Global, _indent);
                continue;
            }
            validCrossedSettings.add(cs);
        }

        if (validCrossedSettings.isEmpty())
            throw new GlobalException("No valid crossed settings were identified", this.getClass());

        CrossedSetting[] crossedSettings = new CrossedSetting[validCrossedSettings.size()];
        int idx = 0;
        for (CrossedSetting cs : validCrossedSettings) crossedSettings[idx++] = cs;
        return crossedSettings;
    }

    /**
     * Auxiliary method for validating some of the settings.
     *
     * @throws GlobalException global-level exception can be thrown and propagated higher.
     */
    private void validate() throws GlobalException
    {
        if (_GDC.getCrossedFolderName() == null) throw new GlobalException("The name of the folder for storing the " +
                "results of cross-analysis is not provided (the string is null)", this.getClass());
        if (_GDC.getCrossedFolderName().isEmpty()) throw new GlobalException("The name of the folder for storing the " +
                "results of cross-analysis is not provided (the string is empty)", this.getClass());
        if (_GDC.getCrossedSettings() == null)
            throw new GlobalException("Crossed settings are not provided (the array is null)", this.getClass());
        if (_GDC.getCrossedSettings().length == 0)
            throw new GlobalException("Crossed settings are not provided (the array is empty)", this.getClass());
        for (CrossedSetting cs : _GDC.getCrossedSettings())
            if (cs == null) throw new GlobalException("One of the provided crossed settings is null", this.getClass());
        if (_GDC.getReferenceCrossSavers() == null)
            throw new GlobalException("Reference cross-savers are not provided (the array is null)", this.getClass());
        if (_GDC.getReferenceCrossSavers().isEmpty())
            throw new GlobalException("Reference cross-savers are not provided (the array is empty)", this.getClass());

        for (ICrossSaver s : _GDC.getReferenceCrossSavers())
            if (s == null)
                throw new GlobalException("One of the provided cross-savers is null", this.getClass());

        Set<String> names = new HashSet<>(_GDC.getReferenceCrossSavers().size());
        for (ICrossSaver s : _GDC.getReferenceCrossSavers()) {
            if (names.contains(s.getFileSuffix()))
                throw new GlobalException("Cross savers' suffixes are not unique (" + s.getFileSuffix() + ")",
                        this.getClass());
            names.add(s.getFileSuffix());
        }

    }


    /**
     * Auxiliary method that creates the main folder (the overwritten method does nothing).
     *
     * @throws GlobalException the exception will be thrown if the main folder cannot be created
     */
    @Override
    protected void createMainFolder() throws GlobalException
    {
        // do nothing
    }

    /**
     * Auxiliary method for validating the SDCF (scenario data container factory); (the overwritten method does nothing)
     *
     * @throws GlobalException global exception is thrown when the SDCF is not provided
     */
    @Override
    protected void validateTDCF() throws GlobalException
    {
        // do nothing
    }


    /**
     * Executes final operations.
     *
     * @param summary summary object
     */
    @Override
    protected void finalize(Summary summary)
    {
        super.finalize(summary);
        _crossSummary = null;
    }

}
