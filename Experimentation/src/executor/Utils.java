package executor;

import container.global.AbstractGlobalDataContainer;
import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import exception.TrialException;
import indicator.IIndicator;
import io.trial.ITrialLoader;
import io.trial.TLPITrialWrapper;
import io.trial.TLPerIndicator;
import io.utils.pusher.IPusher;
import scenario.Scenario;

/**
 * Provides some auxiliary functions for {@link ScenariosSummarizer} and {@link CrossSummarizer}.
 *
 * @author MTomczyk
 */
class Utils
{
    /**
     * Auxiliary method for loading binary data and pushing to savers
     *
     * @param GDC         global data container
     * @param loaders     contains binary files loaders
     * @param pusher      object responsible for pushing the loaded data to data savers
     * @param scenario    scenario being currently processed
     * @param SDC         scenario data container
     * @param generations the total number of generations the methods were run for
     * @param indicatorID id (array index) of the indicator being currently processed
     * @param indicator   indicator being currently processed
     * @throws ScenarioException the scenario-level exception can be thrown and passed higher
     */
    protected static void loadAndPushBinaryData(AbstractGlobalDataContainer GDC,
                                                TLPITrialWrapper loaders,
                                                IPusher pusher,
                                                Scenario scenario,
                                                AbstractScenarioDataContainer SDC,
                                                int generations,
                                                int indicatorID,
                                                IIndicator indicator) throws ScenarioException
    {
        int loadingCap = SDC.getDataLoadingInterval();
        if (loadingCap > generations) loadingCap = generations;

        for (int generation = 0; generation < generations; generation += loadingCap)
        {
            if (generation + loadingCap >= generations) loadingCap = generations - generation;

            double[][] data;
            try
            {
                data = getData(GDC, loaders, scenario, indicatorID, loadingCap);
            } catch (ScenarioException e)
            {
                throw new ScenarioException("Could not load data for indicator = " + indicator.getName() +
                        " " + e.getDetailedReasonMessage(), Utils.class, e, scenario);
            }

            try
            {
                for (int g = 0; g < loadingCap; g++)
                    pusher.pushData(data[g], SDC.getStatisticFunctions(), generation + g);
            } catch (ScenarioException e)
            {
                throw new ScenarioException("Could not push data to files for indicator = " + indicator.getName() +
                        " " + e.getDetailedReasonMessage(), Utils.class, e, scenario);
            }
        }

    }

    /**
     * Auxiliary method for retrieving data matrices from trial-level files to be passed to scenario savers.
     *
     * @param GDC               global data container
     * @param loaders           contains binary files loaders
     * @param scenario          scenario being currently processed
     * @param indicatorID       id (array index) of the indicator being currently processed
     * @param generationsToLoad generationsToLoad the number of generations to load
     * @return data matrix
     * @throws ScenarioException scenario-level exception can be called and passed higher
     */
    private static double[][] getData(AbstractGlobalDataContainer GDC, TLPITrialWrapper loaders,
                                      Scenario scenario, int indicatorID, int generationsToLoad) throws ScenarioException
    {
        double[][] data = new double[generationsToLoad][GDC.getNoEnabledTrials()]; // (generations x trials)
        int tIdx = 0;
        for (Integer trial : GDC.getTrialIDs())
        {
            try
            {
                TLPerIndicator tl = loaders.getTLPerIndicator(scenario, trial);
                ITrialLoader l = tl.getTrialLoaderForIndicator(indicatorID);
                double[] vs = l.retrieve(generationsToLoad);
                for (int g = 0; g < generationsToLoad; g++) data[g][tIdx] = vs[g];
                tIdx++;

            } catch (ScenarioException | TrialException e)
            {
                throw new ScenarioException("Could not load trial data " + e.getDetailedReasonMessage(), Utils.class, e, scenario);
            }
        }

        return data;
    }

}
