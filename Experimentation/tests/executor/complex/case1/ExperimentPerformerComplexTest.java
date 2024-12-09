package executor.complex.case1;

import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import ea.dummy.populations.EADummyPopulations;
import executor.ExperimentPerformer;
import indicator.Evaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.FileUtils;
import org.junit.jupiter.api.Test;
import scenario.Keys;
import statistics.IStatistic;
import statistics.Min;
import summary.Summary;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides a more complex test for the {@link ExperimentPerformer} class.
 *
 * @author MTomczyk
 */
class ExperimentPerformerComplexTest
{

    /**
     * Test.
     */
    @Test
    void test()
    {
        // IMPORTANT NOTE: this test assumes that the used CPU has at least 4 cores available

        DummyExperimentPerformer.Params pE = new DummyExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1"}, {"NSGAII"}};
        pGDC._noThreads = 4;
        pGDC._noTrials = 100;
        pGDC._monitorReportingInterval = 1000;

        String folderPath = "";
        String msg = null;
        try
        {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerComplexTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        pGDC._mainPath = folderPath + File.separatorChar + "executor_temporary_output";
        String mainPath = pGDC._mainPath;
        System.out.println(pGDC._mainPath);
        pE._GDC = new GlobalDataContainer(pGDC);
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._indicators = new IIndicator[]{new PerformanceIndicator(new Evaluation(new Min()))};
        pSDCF._statistics = new IStatistic[]{new Min()};
        pE._SDCF = new ScenarioDataContainerFactory(pSDCF);
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R, p) -> new EADummyPopulations(1, new double[][][]{{{1.0d}}});
        pE._TDCF = new TrialDataContainerFactory(pTDCF);
        pE._delay = 200;
        pE._throwException = new boolean[100];
        for (int i = 0; i < 100; i += 10) pE._throwException[i] = true;

        DummyExperimentPerformer executor = new DummyExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());

        assertEquals(1, s.getScenariosSummaries().length);
        assertFalse(s.getScenariosSummaries()[0].isSkipped());
        assertFalse(s.getScenariosSummaries()[0].isTerminatedDueToException());
        assertEquals(100, s.getScenariosSummaries()[0].getNoTrials());
        assertEquals(90, s.getScenariosSummaries()[0].getCompletedTrials());
        assertEquals(10, s.getScenariosSummaries()[0].getTerminatedTrials());
        assertEquals(1, s.getScenariosSummaries().length);
        assertEquals(1, s.getCompletedScenarios());
        assertEquals(0, s.getTerminatedScenarios());
        long millis = Duration.between(s.getScenariosSummaries()[0].getStartTimestamp(),
                s.getScenariosSummaries()[0].getStopTimestamp()).toMillis();
        assertTrue((millis > 4000) && (millis < 5000));

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(s);


        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{"PROBLEM_P1_ALGORITHM_NSGAII"};
        for (String f : subfolders)
        {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
        }


        // remove outputs
        try
        {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}