package updater;

import dataset.IDataSet;
import plotswrapper.AbstractPlotsWrapper;

/**
 * Provides practical means of instantiating {@link DataUpdater}.
 *
 * @author MTomczyk
 */
public class DataUpdaterFactory
{
    /**
     * Builder for the simple data updater. Assumes that there is just one data source and the output is explicitly
     * passed as data set to the several plots with IDs starting from 0.
     *
     * @param plotsWrapper      reference to the plots wrapper
     * @param dataSource        data source
     * @param referenceDataSets reference data set for each plot(provides info on market styles etc.); the array length determines the number of target plots
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getDataUpdaterLinkedWithMultiplePlots(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet[] referenceDataSets) throws Exception
    {
        return new DataUpdater(new DataUpdater.Params(plotsWrapper, dataSource, referenceDataSets));
    }

    /**
     * Builder for the simple data updater. Assumes that there is just one data source and the output is explicitly
     * passed as data set to the only plot with id = 0.
     *
     * @param plotsWrapper     reference to the plots wrapper
     * @param dataSource       data source
     * @param referenceDataSet reference data set (provides info on market styles etc)
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getSimpleDataUpdater(AbstractPlotsWrapper plotsWrapper, IDataSource dataSource, IDataSet referenceDataSet) throws Exception
    {
        return new DataUpdater(new DataUpdater.Params(plotsWrapper, dataSource, referenceDataSet));
    }

    /**
     * Builder for the simple data updater. Assumes that there is just one data source and the output is explicitly
     * passed through the given processor as data set to the only plot with id = 0.
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
        return new DataUpdater(new DataUpdater.Params(plotsWrapper, dataSource, dataProcessor, referenceDataSet));
    }

    /**
     * Builder for the simple data updater. Allows linking multiple data sources with their regular data processors and
     * provided reference data sets (by index mapping). The data sets are passed to with id = 0.
     *
     * @param plotsWrapper      reference to the plots wrapper
     * @param dataSources       data sources
     * @param referenceDataSets reference data sets (provides info on market styles etc).
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getSimpleDataUpdater(AbstractPlotsWrapper plotsWrapper,
                                                   IDataSource[] dataSources,
                                                   IDataSet[] referenceDataSets) throws Exception
    {
        if (dataSources == null) throw new Exception("The data sources are not provided (the input is null)");
        for (IDataSource ds : dataSources)
            if (ds == null) throw new Exception("One of the provided data sources is null");
        IDataProcessor[] dataProcessors = new IDataProcessor[dataSources.length];
        for (int i = 0; i < dataSources.length; i++) dataProcessors[i] = new DataProcessor(false);
        return getSimpleDataUpdater(plotsWrapper, dataSources, dataProcessors, referenceDataSets);
    }

    /**
     * Builder for the simple data updater. Allows linking multiple data sources with their corresponding processors and
     * provided reference data sets (by index mapping). The data sets are passed to with id = 0.
     *
     * @param plotsWrapper      reference to the plots wrapper
     * @param dataSources       data sources
     * @param dataProcessors    data processor
     * @param referenceDataSets reference data sets (provides info on market styles etc).
     * @return data updater object
     * @throws Exception exception will be thrown if the input data is invalid
     */
    public static DataUpdater getSimpleDataUpdater(AbstractPlotsWrapper plotsWrapper,
                                                   IDataSource[] dataSources,
                                                   IDataProcessor[] dataProcessors,
                                                   IDataSet[] referenceDataSets) throws Exception
    {
        if (dataSources == null) throw new Exception("The data sources are not provided (the input is null)");
        if (dataProcessors == null) throw new Exception("The data processors are not provided (the input is null)");
        if (referenceDataSets == null)
            throw new Exception("The reference data sets are not provided (the input is null)");
        for (IDataSource ds : dataSources)
            if (ds == null) throw new Exception("One of the provided data sources is null");
        for (IDataProcessor ds : dataProcessors)
            if (ds == null) throw new Exception("One of the provided data processors is null");
        for (IDataSet ds : referenceDataSets)
            if (ds == null) throw new Exception("One of the provided reference data sets is null");
        if ((dataSources.length != dataProcessors.length) || (dataSources.length != referenceDataSets.length))
            throw new Exception("The input array lengths are not equal");

        SourceToProcessors[] stp = new SourceToProcessors[dataProcessors.length];
        ProcessorToPlots[] ptp = new ProcessorToPlots[dataSources.length];
        for (int i = 0; i < dataSources.length; i++)
        {
            stp[i] = new SourceToProcessors(i);
            ptp[i] = new ProcessorToPlots(0, referenceDataSets[i]);
        }

        DataUpdater.Params pDU = new DataUpdater.Params(plotsWrapper);
        pDU._dataSources = dataSources;
        pDU._dataProcessors = dataProcessors;
        pDU._sourcesToProcessors = stp;
        pDU._processorToPlots = ptp;
        return new DataUpdater(pDU);
    }

}
