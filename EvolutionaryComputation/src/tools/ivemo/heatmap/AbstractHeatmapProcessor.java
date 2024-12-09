package tools.ivemo.heatmap;

import ea.EA;
import ea.factory.IEAFactory;
import exception.RunnerException;
import plot.heatmap.utils.Coords;
import plot.heatmap.utils.HeatmapDataProcessor;
import statistics.IStatistic;
import statistics.distribution.bucket.AbstractBucketCoords;
import tools.ivemo.heatmap.feature.IFeatureGetter;
import tools.ivemo.heatmap.runners.AbstractUpdateRunner;
import tools.ivemo.heatmap.runners.GenerationalUpdateRunner;
import tools.ivemo.heatmap.runners.SteadyStateUpdateRunner;
import tools.ivemo.heatmap.utils.BucketData;

import java.util.ArrayList;

/**
 * Abstract class representing IVEMO.Heatmap module for generating and saving data for visualization.
 *
 * @author MTomczyk
 */

public abstract class AbstractHeatmapProcessor extends AbstractHeatmap
{
    /**
     * Params container
     */
    public static class Params extends AbstractHeatmap.Params
    {
        /**
         * Title associated with this data (e.g., name of the EA used).
         */
        public String _title = null;

        /**
         * Used to construct instances of an EA to be tested.
         */
        public IEAFactory _eaFactory;

        /**
         * Limit for the number of generations.
         */
        public int _generations = 100;

        /**
         * The number of steady state repeats.
         */
        public int _steadyStateRepeats = 1;

        /**
         * If true, heatmap data is updated after every generation is completed. If false,
         * data update is done after every steady-state repeat.
         */
        public boolean _generationalDataUpdate = true;

        /**
         * The number of test runs whose results are to be aggregated.
         */
        public int _trials = 100;

        /**
         * Used to calculate feature values linked to particular specimens (in particular trial run).
         */
        public IFeatureGetter _featureGetter;

        /**
         * Statistics function used to aggregate in-bucket values per one trial.
         */
        public IStatistic _trialStatistics;

        /**
         * Statistics function used to aggregate in-bucket values from final data.
         */
        public IStatistic _finalStatistics;

        /**
         * If true, intermediate data structures are cleared after processing. Use false to preserve them
         * so that they can be accessed (for testing).
         */
        public boolean _clearIntermediateData = true;
    }

    /**
     * Used to construct instances of an EA to be tested.
     */
    protected IEAFactory _eaFactory;

    /**
     * Limit for the number of generations.
     */
    protected int _generations;

    /**
     * The number of steady state repeats.
     */
    protected int _steadyStateRepeats;

    /**
     * If true, heatmap data is updated after every generation is completed. If false,
     * data update is done after every steady-state repeat.
     */
    protected boolean _generationalDataUpdate;

    /**
     * The number of test runs whose results are to be aggregated.
     */
    protected int _trials;

    /**
     * Bucket coords object responsible for translating input points into bucket coordinates (ids).
     */
    protected AbstractBucketCoords _BC = null;

    /**
     * Used to calculate feature values linked to particular specimens (in particular trial run).
     */
    protected IFeatureGetter _featureGetter;

    /**
     * Statistics function used to aggregate in-bucket values per one trial.
     */
    protected IStatistic _trialStatistics;

    /**
     * Statistics function used to aggregate in-bucket values from final data.
     */
    protected IStatistic _finalStatistics;

    /**
     * If true, intermediate data structures are cleared after processing. Use false to preserve them
     * so that they can be accessed (for testing).
     */
    protected boolean _clearIntermediateData;

    /**
     * Raw data structure for calculating final heatmap data.
     * 1. ArrayList -> one element per trial run;
     * 2. [][][] matrix -> one element per bucket; note that dimensions order is (according to index no) Z, Y, X
     * 3. BucketTrialData used to store samples obtained when processing an EA.
     */
    protected ArrayList<BucketData[][][]> _trialData = null;

    /**
     * Bucket data to be aggregated (obtained from various trials).
     * 1. [][][] matrix -> one element per bucket; note that dimensions order is (according to index no) Z, Y, X
     * 2. BucketTrialData used to store samples obtained from trials.
     */
    protected BucketData[][][] _dataForAggregation = null;

    /**
     * Heatmap data processor. Used to preprocess data before saving it on a disk (it mainly sorts buckets according to their values to allow efficient data filtering).
     */
    protected HeatmapDataProcessor _HDP = new HeatmapDataProcessor();

    /**
     * Auxiliary field storing sorted coords (generated using HeatmapDataProcessor).
     */
    protected Coords[] _SC;

    /**
     * Auxiliary field storing sorted bucket values (generated using HeatmapDataProcessor).
     */
    protected HeatmapDataProcessor.SortedValues _SV;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractHeatmapProcessor(Params p)
    {
        super(p);
        _eaFactory = p._eaFactory;
        _generations = p._generations;
        _steadyStateRepeats = p._steadyStateRepeats;
        _generationalDataUpdate = p._generationalDataUpdate;
        _trials = p._trials;
        _featureGetter = p._featureGetter;
        _trialStatistics = p._trialStatistics;
        _finalStatistics = p._finalStatistics;
        _clearIntermediateData = p._clearIntermediateData;
        instantiateBucketCoords(p);
    }


    /**
     * Executes processing, i.e., generates heatmap-specific data and stores it in internal fields.
     */
    public void executeProcessing()
    {
        if (_notify) notify("Processing started...");

        if (_notify) notify("Constructing empty buckets (reserving memory)...");

        instantiateData();
        clearData(); // sets all final bucket values to NEGATIVE_INFINITY

        _trialData = new ArrayList<>(_trials);

        for (int t = 0; t < _trials; t++)
        {
            if (_notify) notify("Processing trial no = " + t);

            EA ea = _eaFactory.getInstance(); // get ea instance
            BucketData[][][] btd = getBucketTrialData(ea);
            _trialData.add(btd);

            AbstractUpdateRunner runner = getRunner(ea); // constructs runner responsible for performing ea
            runner.setProcessedSample(btd); // setter for the processed sample

            try
            {
                runner.executeEvolution(_generations); // executes whole evolution (updates are done in runners)
            } catch (RunnerException e)
            {
                throw new RuntimeException(e);
            }
            processTrialData(btd);


            if (_clearIntermediateData)
            {
                clearBucketTrialAuxiliaryData(btd); // clears BTD's aux data
                System.gc();
            }

        }

        if (_notify) notify("Calculating final data...");

        if (_clearIntermediateData) _trialData = null;
        processData();
        if (_clearIntermediateData)
        {
            _dataForAggregation = null;
            System.gc();
        }

        if (_notify) notify("Finished");
    }

    /**
     * Can be called after {@link #executeProcessing} to pre-process data to be stored.
     */
    public void generateSortedInputData()
    {
        if (_data == null) return;
        _SC = _HDP.getCoords2D(_xAxisDivisions, _yAxisDivisions, _data[0]);
        _SV = _HDP.getSortedValues(_SC, 2, false);
    }


    /**
     * Processes data to obtain final results
     */
    protected void processData()
    {
        for (int z = 0; z < _dataForAggregation.length; z++)
        {
            for (int y = 0; y < _dataForAggregation[0].length; y++)
            {
                for (int x = 0; x < _dataForAggregation[0][0].length; x++)
                {
                    double[] arr = _dataForAggregation[z][y][x]._LA.getTransformedToArray();
                    if ((arr == null) || (arr.length == 0))
                        _dataForAggregation[z][y][x]._value = Double.NEGATIVE_INFINITY;
                    else
                    {
                        _dataForAggregation[z][y][x]._value = _finalStatistics.calculate(arr);
                        _data[z][y][x] = _dataForAggregation[z][y][x]._value;
                    }
                }
            }
        }
    }

    /**
     * Method for processing trial data (determines final bucket values).
     *
     * @param btd bucket trial data
     */
    protected void processTrialData(BucketData[][][] btd)
    {
        for (int z = 0; z < btd.length; z++)
        {
            for (int y = 0; y < btd[0].length; y++)
            {
                for (int x = 0; x < btd[0][0].length; x++)
                {
                    double[] arr = btd[z][y][x]._LA.getTransformedToArray();
                    if ((arr == null) || (arr.length == 0)) btd[z][y][x]._value = Double.NEGATIVE_INFINITY;
                    else
                    {
                        btd[z][y][x]._value = _trialStatistics.calculate(arr);
                        _dataForAggregation[z][y][x]._LA.addValue(btd[z][y][x]._value);
                    }
                }
            }
        }
    }

    /**
     * Constructs runner object (responsible for running EMOAs).
     *
     * @param ea input EA
     * @return runner object
     */
    protected AbstractUpdateRunner getRunner(EA ea)
    {
        AbstractUpdateRunner.Params pR = new AbstractUpdateRunner.Params(ea, _steadyStateRepeats);
        pR._featureGetter = _featureGetter;
        pR._BC = _BC;
        pR._dimensions = _dimensions;
        if (_generationalDataUpdate) return new GenerationalUpdateRunner(pR);
        else return new SteadyStateUpdateRunner(pR);
    }


    /**
     * Returns the raw (processed but not prepared for visualization) data.
     *
     * @return raw heatmap data
     */
    public double[][][] getRawData()
    {
        return _data;
    }

    /**
     * Auxiliary method instantiating bucket coordinate object (responsible for translating input data points into bucket coordinates).
     *
     * @param p params container.
     */
    protected void instantiateBucketCoords(Params p)
    {
        notify("Invalid method call!");
    }

    /**
     * Auxiliary method for instantiating data (to be overwritten).
     */
    protected void instantiateData()
    {
        notify("Invalid method call!");
    }

    /**
     * Auxiliary method for instantiating (getter) trial data (to be overwritten).
     *
     * @param ea evolutionary algorithm
     * @return trial bucket data
     */
    protected BucketData[][][] getBucketTrialData(EA ea)
    {
        notify("Invalid method call!");
        return null;
    }

    /**
     * Clears auxiliary per-bucket data samples captured when executing an EA.
     *
     * @param m bucket trial data
     */
    protected void clearBucketTrialAuxiliaryData(BucketData[][][] m)
    {
        for (BucketData[][] zd : m)
            for (BucketData[] yd : zd)
                for (BucketData xd : yd)
                {
                    xd._LA.clearData();
                    xd._LA = null;
                }
    }

    /**
     * Getter for testing: Raw data structure for calculating final heatmap data.
     * 1. ArrayList -> one element per trial run;
     * 2. [][][] matrix -> one element per bucket; note that dimensions order is (according to index no) Z, Y, X
     * 3. BucketTrialData used to store samples obtained when processing an EA.
     *
     * @return trial data
     */
    public ArrayList<BucketData[][][]> getTrialData()
    {
        return _trialData;
    }

    /**
     * Returns auxiliary field storing sorted coords (generated using HeatmapDataProcessor).
     *
     * @return auxiliary field storing sorted coords (generated using HeatmapDataProcessor)
     */
    public Coords[] getSortedCoords()
    {
        return _SC;
    }

    /**
     * Returns auxiliary field storing sorted bucket values (generated using HeatmapDataProcessor).
     *
     * @return auxiliary field storing sorted bucket values (generated using HeatmapDataProcessor).
     */
    public HeatmapDataProcessor.SortedValues getSortedValues()
    {
        return _SV;
    }


}

