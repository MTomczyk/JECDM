package y2025.SoftwareX_JECDM;

import container.Containers;
import container.global.GlobalDataContainer;
import container.global.initializers.FromStreamsInitializer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import emo.aposteriori.nsgaii.NSGAII;
import indicator.ExecutionTime;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.cross.excel.ConvergenceXLSX;
import io.cross.excel.FinalRankerXLSX;
import io.cross.excel.FinalStatisticsXLSX;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import scenario.CrossedSetting;
import selection.Random;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.TStudent;

import java.io.File;
import java.util.LinkedList;

/**
 * Provides the data containers for this experiment.
 *
 * @author MTomczyk
 */
public class ContainersGetter
{
    /**
     * Tester.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        try
        {
            getContainers();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Main method for creating the containers.
     *
     * @return containers
     * @throws Exception exception can be thrown
     */
    public static Containers getContainers() throws Exception
    {
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._mainPath = "D:" + File.separator + "experiments" + File.separator + "2025"
                + File.separator + "SoftwareX_JECDM" + File.separatorChar + "e1_time_experiment"; // my path
        pGDC._noTrials = 20; // no trials (test runs pe scenario)
        pGDC._noThreads = 10; // run using 10 threads
        pGDC._useMonitorThread = true; // monitor what is going on
        pGDC._monitorReportingInterval = 10000; // 10000; // monitor every 10 s
        pGDC._scenarioKeys = new String[]{ // define scenario keys
                "FRAMEWORK", // framework used to instantiate NSGA-II
                "M", // the number of objectives
                "PS",  // the population size
                "GEN", // the number of generations
        };

        pGDC._scenarioValues = new String[][]{
                {
                        "JECDM", "JMETAL", "PYMOO",
                },
                {
                        "2", "3", "4", "5"
                },
                {
                        "50", "100", "150", "200",
                },
                {
                        "200", "300", "400", "500"
                }
        };

        pGDC._extraAllowedCharacters = new Character[]{'_', '.', '-'}; // add extra allowed characters for processing

        // Define the RNG:
        pGDC._RNGI = new FromStreamsInitializer(FromStreamsInitializer.Mode.SPLITTABLE,
                FromStreamsInitializer.InitializationStage.SCENARIO, () -> new L32_X64_MIX(0));

        // Define settings for the cross-summarizer:
        pGDC._crossedSettings = new CrossedSetting[1];
        pGDC._crossedSettings[0] = new CrossedSetting(new String[]{
                "M", "PS", "FRAMEWORK", "GEN"},
                new String[][]{pGDC._scenarioValues[1],
                        pGDC._scenarioValues[2],
                        pGDC._scenarioValues[0],
                        pGDC._scenarioValues[3]});

        pGDC._referenceCrossSavers = new LinkedList<>();
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(4));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("FRAMEWORK", new ITest[]{
                TStudent.getPairedTest(true),
        }, 4, 1.0E-9));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(4));
        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SDCF params container
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._statistics = new IStatistic[]
                {
                        new Min(),
                        new Mean(),
                        new Max(),
                        new StandardDeviation(),
                };

        pSDCF._numberOfGenerationsInitializer = p ->
                Integer.parseInt(p._scenario.getKeyValuesMap().get("GEN").getValue());

        pSDCF._indicators = new IIndicator[1];
        pSDCF._indicators[0] = new PerformanceIndicator(new ExecutionTime());

        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // Define EA initializer:
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R, p) -> {
            String name = p._SDC.getScenario().getKeyValuesMap().get("FRAMEWORK").getValue();
            int M = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("M").getValue());
            int PS = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("PS").getValue());
            int GEN = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("GEN").getValue());

            DTLZBundle dtlzBundle = DTLZBundle.getBundle(Problem.DTLZ2, M,
                    DTLZBundle.getRecommendedNODistanceRelatedParameters(Problem.DTLZ2, M));

            if (name.equals("JECDM")) // NSGA-II from the JECDM library
            {
                return NSGAII.getNSGAII(0, true, PS, p._R,
                        dtlzBundle, new Random(2), dtlzBundle._construct,
                        dtlzBundle._evaluate, dtlzBundle._reproduce);
            } else if (name.equals("JMETAL"))
            {
                return new JMETAL(M, PS, GEN, p._R);
            } else if (name.equals("PYMOO"))
            {
                return new PYMOO(M, PS, GEN, p._R);
            }

            return null;
        };

        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);
        return new Containers(GDC, SDCF, TDCF);
    }
}
