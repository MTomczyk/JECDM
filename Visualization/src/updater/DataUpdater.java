package updater;

import dataset.IDataSet;
import plot.AbstractPlot;
import plotswrapper.AbstractPlotsWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class can support automatic data update of plots wrapped by {@link AbstractPlotsWrapper}.
 * The class maintains several mappings that allow for minimizing data flow redundancy.
 * First, one may bind a data source ({@link IDataSource}) with several data processors {@link IDataProcessor}.
 * Data source is responsible for preparing raw data in the form of double[][]. The reference to this data
 * can be passed to different processors that can perform some extra operations (e.g., collect past data).
 * Note that the object is not cloned; only its reference is passed to processors. Hence, extra caution
 * has to be taken when implementing {@link IDataProcessor} as the given data should be considered read-only.
 * Important restriction: one source can be linked with multiple processors, but the inverse is not allowed.
 * Next, one may bind each processor with several plots. This way, the data created by the processor can be displayed on various
 * plots. As with the previous case, this data should be considered read-only.
 *
 * @author MTomczyk
 */


public class DataUpdater
{
    /**
     * Auxiliary class that represents a tuple (data processor; associated reference data set).
     */
    private static class ProcessorAndDataSet
    {
        /**
         * Data processor.
         */
        protected final IDataProcessor _dataProcessor;

        /**
         * Associated reference data set.
         */
        protected final IDataSet _referenceDataSet;

        /**
         * Parameterized constructor.
         *
         * @param dataProcessor    data processor
         * @param referenceDataSet associated reference data set
         */
        public ProcessorAndDataSet(IDataProcessor dataProcessor, IDataSet referenceDataSet)
        {
            _dataProcessor = dataProcessor;
            _referenceDataSet = referenceDataSet;
        }
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Data sources.
         */
        public IDataSource[] _dataSources;

        /**
         * Data processors.
         */
        public IDataProcessor[] _dataProcessors;

        /**
         * Data sources -> data processor mappings (elements map 1:1 with the elements in _dataSources).
         */
        public SourceToProcessors[] _sourcesToProcessors;

        /**
         * Data processors -> plots mappings (elements map 1:1 with the elements in _dataProcessors).
         */
        public ProcessorToPlots[] _processorToPlots;

        /**
         * Reference to the "plots wrapper".
         */
        protected final AbstractPlotsWrapper _plotsWrapper;

        /**
         * Auxiliary (can be null) map that binds a boolean flag with a plot.
         * The flag determines whether to request an update in display ranges when calling for
         * {@link plot.PlotModel#setDataSets(ArrayList, boolean)}. The default interpretation (e.g., when the flag is not
         * provided, is true). Nonetheless, the negative flags set in {@link drmanager.DisplayRangesManager.DisplayRange}
         * may negate the request.
         */
        public HashMap<AbstractPlot, Boolean> _callForUpdateDisplayRanges = null;


        /**
         * Parameterized constructor.
         *
         * @param plotsWrapper reference to the plots wrapper
         */
        public Params(AbstractPlotsWrapper plotsWrapper)
        {
            _plotsWrapper = plotsWrapper;
        }

        /**
         * Parameterized constructor (protected).
         * Assumes that there is just one data source and the output is explicitly passed as data set to the
         * only plot with id = 0.
         *
         * @param plotsWrapper     reference to the plots wrapper
         * @param dataSource       data source
         * @param referenceDataSet reference data set (provides info on market styles etc).
         */
        protected Params(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet referenceDataSet)
        {
            this(plotsWrapper, dataSource, new IDataSet[]{referenceDataSet});
        }

        /**
         * Parameterized constructor (protected).
         * Assumes that there is just one data source and the output is explicitly passed as data set to the
         * only plot with id = 0.
         *
         * @param plotsWrapper     reference to the plots wrapper
         * @param dataSource       data source
         * @param dataProcessor    data processor
         * @param referenceDataSet reference data set (provides info on market styles etc).
         */
        protected Params(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataProcessor dataProcessor, IDataSet referenceDataSet)
        {
            this(plotsWrapper, dataSource, dataProcessor, new IDataSet[]{referenceDataSet});
        }

        /**
         * Parameterized constructor (protected).
         * Assumes that there is just one data source and the output is explicitly passed as data set to the
         * several plots with IDs starting from 0.
         *
         * @param plotsWrapper      reference to the plots wrapper
         * @param dataSource        data source
         * @param referenceDataSets reference data set for each plot(provides info on market styles etc.); the array length determines the number of target plots
         */
        protected Params(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet[] referenceDataSets)
        {
            this(plotsWrapper, dataSource, new DataProcessor(), referenceDataSets);
        }

        /**
         * Parameterized constructor (protected).
         * Assumes that there is just one data source and the output is explicitly passed as data set to the
         * several plots with IDs starting from 0.
         *
         * @param plotsWrapper      reference to the plots wrapper
         * @param dataSource        data source
         * @param dataProcessor     data processor
         * @param referenceDataSets reference data set for each plot(provides info on market styles etc.); the array length determines the number of target plots
         */
        protected Params(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataProcessor dataProcessor, IDataSet[] referenceDataSets)
        {
            _plotsWrapper = plotsWrapper;
            _dataSources = new IDataSource[]{dataSource};
            _dataProcessors = new IDataProcessor[]{dataProcessor};
            _sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
            _processorToPlots = new ProcessorToPlots[1];
            int[] ids = new int[referenceDataSets.length];
            for (int i = 0; i < referenceDataSets.length; i++) ids[i] = i;
            _processorToPlots[0] = new ProcessorToPlots(ids, referenceDataSets);
        }

    }

    /**
     * Data sources.
     */
    private final IDataSource[] _dataSources;

    /**
     * Data processors.
     */
    private final IDataProcessor[] _dataProcessors;

    /**
     * Data sources -> data processor mappings.
     */
    private final SourceToProcessors[] _sourcesToProcessors;

    /**
     * Data processors -> plots mappings.
     */
    private final ProcessorToPlots[] _processorToPlots;

    /**
     * Reference to the "plots wrapper".
     */
    private final AbstractPlotsWrapper _plotsWrapper;

    /**
     * Auxiliary field that stores the mapping: plot -> array (processor; data set).
     * It helps quickly construct a new array of data sets to be provided to the plot.
     */
    private final HashMap<AbstractPlot, ArrayList<ProcessorAndDataSet>> _plotsToDSs;

    /**
     * Auxiliary (can be null) map that binds a boolean flag with a plot.
     * The flag determines whether to request an update in display ranges when calling for
     * {@link plot.PlotModel#setDataSets(ArrayList, boolean)}. The default interpretation (e.g., when the flag is not
     * provided, is true). Nonetheless, the negative flags set in {@link drmanager.DisplayRangesManager.DisplayRange}
     * may negate the request.
     */
    private final HashMap<AbstractPlot, Boolean> _callForUpdateDisplayRanges;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws Exception exception that is thrown when there is an error in mapping
     */
    public DataUpdater(Params p) throws Exception
    {
        _dataSources = p._dataSources;
        _dataProcessors = p._dataProcessors;
        _sourcesToProcessors = p._sourcesToProcessors;
        _processorToPlots = p._processorToPlots;
        _plotsWrapper = p._plotsWrapper;
        _callForUpdateDisplayRanges = p._callForUpdateDisplayRanges;

        String validity = checkValidity();
        if (validity != null) throw new Exception(validity);

        _plotsToDSs = new HashMap<>();
        for (int pID = 0; pID < _processorToPlots.length; pID++)
        {
            for (int i = 0; i < _processorToPlots[pID]._plotIDs.length; i++)
            {
                int plotID = _processorToPlots[pID]._plotIDs[i];
                AbstractPlot plot = _plotsWrapper.getModel().getPlot(plotID);
                IDataProcessor processor = _dataProcessors[pID];
                IDataSet reference = _processorToPlots[pID]._referenceDataSets[i];
                if (!_plotsToDSs.containsKey(plot)) _plotsToDSs.put(plot, new ArrayList<>(_processorToPlots.length));
                _plotsToDSs.get(plot).add(new ProcessorAndDataSet(processor, reference));
            }
        }
    }

    /**
     * Builder for the simple data updater.
     * Assumes that there is just one data source and the output is explicitly passed as data set to the
     * several plots with IDs starting from 0.
     *
     * @param plotsWrapper      reference to the plots wrapper
     * @param dataSource        data source
     * @param referenceDataSets reference data set for each plot(provides info on market styles etc.); the array length determines the number of target plots
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getDataUpdaterLinkedWithMultiplePlots(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet[] referenceDataSets) throws Exception
    {
        return new DataUpdater(new Params(plotsWrapper, dataSource, referenceDataSets));
    }

    /**
     * Builder for the simple data updater.
     * Assumes that there is just one data source and the output is explicitly passed as data set to the
     * only plot with id = 0.
     *
     * @param plotsWrapper     reference to the plots wrapper
     * @param dataSource       data source
     * @param referenceDataSet reference data set (provides info on market styles etc)
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getSimpleDataUpdater(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet referenceDataSet) throws Exception
    {
        return new DataUpdater(new Params(plotsWrapper, dataSource, referenceDataSet));
    }

    /**
     * Builder for the simple data updater.
     * Assumes that there is just one data source and the output is explicitly passed through the given processor
     * as data set to the only plot with id = 0.
     *
     * @param plotsWrapper     reference to the plots wrapper
     * @param dataSource       data source
     * @param dataProcessor    data processor
     * @param referenceDataSet reference data set (provides info on market styles etc).
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getSimpleDataUpdater(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataProcessor dataProcessor, IDataSet referenceDataSet) throws Exception
    {
        return new DataUpdater(new Params(plotsWrapper, dataSource, dataProcessor, referenceDataSet));
    }


    /**
     * Checks whether the mappings are valid.
     * If not, the main constructor will throw the exception.
     *
     * @return 0 = mapping is valid; positive values imply various reasons for validity violation.
     */
    private String checkValidity()
    {
        if (_dataSources == null) return "Data sources not provided";
        if (_dataSources.length == 0) return "Data sources not provided";
        for (int i = 0; i < _dataSources.length; i++)
            if (_dataSources[i] == null) return "Data source no. " + i + " is null";

        if (_dataProcessors == null) return "Data processors not provided";
        if (_dataProcessors.length == 0) return "Data processors not provided";
        for (int i = 0; i < _dataProcessors.length; i++)
            if (_dataProcessors[i] == null) return "Data processor no. " + i + " is null";

        if (_sourcesToProcessors == null) return "Sources->processors: mapping not provided";
        if (_sourcesToProcessors.length == 0) return "Sources->processors: mapping not provided";
        for (int i = 0; i < _sourcesToProcessors.length; i++)
        {
            if (_sourcesToProcessors[i] == null) return "Sources->processors: mapping no. " + i + " is null";
            if (_sourcesToProcessors[i]._processorID == null)
                return "Sources->processors: mapping no. " + i + " has no mapping";
        }

        if (_processorToPlots == null) return "Processors->plots: mapping not provided";
        if (_processorToPlots.length == 0) return "Processors->plots: mapping not provided";
        for (int i = 0; i < _processorToPlots.length; i++)
        {
            if (_processorToPlots[i] == null) return "Processors->plots: mapping no. " + i + " is null";
            if (_processorToPlots[i]._plotIDs == null)
                return "Processors->plots: mapping no. " + i + " has no mapping";
            if (_processorToPlots[i]._referenceDataSets == null)
                return "Processors->plots: mapping no. " + i + " has no reference data set";
            if (_processorToPlots[i]._referenceDataSets.length == 0)
                return "Processors->plots: mapping no. " + i + " has no reference data set";
            for (IDataSet ds : _processorToPlots[i]._referenceDataSets)
                if (ds == null)
                    return "Processors->plots: mapping no. " + i + ": one of the reference data sets is null";

            if (_processorToPlots[i]._plotIDs.length != _processorToPlots[i]._referenceDataSets.length)
                return "Processors->plots: no. " + i + ": plots length != reference data sets length";
        }

        if (_processorToPlots.length != _dataProcessors.length)
            return "Provided processors length != processors -> plots length";
        if (_sourcesToProcessors.length != _dataSources.length)
            return "Provided sources length != sources -> processors length";

        if (_plotsWrapper == null) return "Plots wrapper not provided";
        for (ProcessorToPlots pp : _processorToPlots)
            for (int id : pp._plotIDs)
                if (_plotsWrapper.getModel().getPlot(id) == null) return "There is no plot of id = " + id;

        // processors cannot be linked with multiple sources
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int sourceID = 0; sourceID < _sourcesToProcessors.length; sourceID++)
        {
            for (int processorID : _sourcesToProcessors[sourceID]._processorID)
            {
                if (map.containsKey(processorID))
                    return "Processor no. " + processorID + " is assigned multiple sources";
                map.put(processorID, sourceID);
            }
        }

        return null;
    }

    /**
     * Can be called to update data. This method first updates all internal data of data processors {@link IDataProcessor}
     * by collecting data from data sources {@link IDataSource}. Then, new data sets are constructed and passed to plots.
     */
    public void update()
    {
        // first -> feed processors with sources
        for (int s = 0; s < _dataSources.length; s++)
        {
            double[][] sourceData = _dataSources[s].createData();
            for (int u : _sourcesToProcessors[s]._processorID)
                _dataProcessors[u].update(sourceData);
        }


        // second -> for each plot, create new data sets
        if (_plotsToDSs != null)
        {
            _plotsToDSs.forEach((key, value) -> {
                if (_plotsWrapper == null) return;
                if (_plotsWrapper.getModel() == null) return;
                if (_plotsWrapper.getModel().isTerminating()) return;

                ArrayList<IDataSet> newDSs = new ArrayList<>(value.size());
                for (ProcessorAndDataSet pds : value)
                {
                    LinkedList<double[][]> data = pds._dataProcessor.getData();
                    IDataSet ds = pds._referenceDataSet.wrapAround(data);
                    newDSs.add(ds);
                }

                boolean request = true;
                if ((_callForUpdateDisplayRanges != null) && (_callForUpdateDisplayRanges.containsKey(key)))
                    request = _callForUpdateDisplayRanges.get(key);
                key.getModel().setDataSets(newDSs, request, true);
            });
        }
    }

    /**
     * Clears data.
     */
    public void dispose()
    {
        if (_dataSources != null) for (IDataSource s : _dataSources) s.dispose();
        if (_dataProcessors != null) for (IDataProcessor p : _dataProcessors) p.dispose();
    }

}
