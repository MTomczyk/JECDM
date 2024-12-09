package executor.complex.case2;

import condition.ScenarioDisablingConditions;
import condition.TrialDisablingConditions;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import ea.dummy.populations.EADummyPopulations;
import executor.ExperimentPerformer;
import executor.ScenariosSummarizer;
import indicator.Evaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.FileUtils;
import io.scenario.SummarizerTXT;
import org.junit.jupiter.api.Test;
import statistics.*;
import summary.Summary;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides a more complex test for the {@link ExperimentPerformer} class.
 * This tests uses dummy inputs that mimic the solutions' evaluations generated throughout evolutionary processes.
 *
 * @author MTomczyk
 */
class ExperimentPerformerComplex2Test
{

    /**
     * Test.
     */
    @Test
    void test()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();

        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{"PS", "GEN"};
        pGDC._scenarioValues = new String[][]{{"3", "2"}, {"10", "5"}};
        pGDC._noThreads = 1;
        pGDC._noTrials = 8;
        pGDC._trialDisablingConditions = new TrialDisablingConditions(8);
        pGDC._trialDisablingConditions.disableTrials(4, 7);
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(new String[]{"PS", "GEN"}, new String[]{"2", "10"}),
                new ScenarioDisablingConditions(new String[]{"PS", "GEN"}, new String[]{"3", "5"}),
        };
        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerTXT());
        pGDC._referenceScenarioSavers.add(new DummySaver());

        String folderPath = "";
        String msg = null;
        try
        {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerComplex2Test.class,
                    "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        pGDC._mainPath = folderPath + File.separatorChar + "executor_temporary_output";
        String mainPath = pGDC._mainPath;
        pE._GDC = new GlobalDataContainer(pGDC);

        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._indicatorsInitializer = p ->
        {
            if (p._scenario.toString().equals("PS_3_GEN_10"))
            {
                return new IIndicator[]
                        {
                                new PerformanceIndicator(new Evaluation(new Min(), 0)),
                                new PerformanceIndicator(new Evaluation(new Sum(), 1))
                        };
            }
            else return new IIndicator[]{new PerformanceIndicator(new Evaluation(new Mean(), 2))};
        };
        pSDCF._numberOfGenerationsInitializer = p -> Integer.parseInt(p._scenario.getKeyValuesMap().get("GEN").toString());
        pSDCF._statistics = new IStatistic[]{new Min(), new Mean(), new Max()};
        pSDCF._dataLoadingInterval = Integer.MAX_VALUE;
        pSDCF._dataStoringInterval = Integer.MAX_VALUE;
        pE._SDCF = new ScenarioDataContainerFactory(pSDCF);


        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R, p) ->
        {
            if (p._SDC.getGenerations() == 10)
                return new EADummyPopulations(3, InputPopulations.getSolutionDataForT4PS3G10E3()[p._trialID]);
            else if (p._SDC.getGenerations() == 5)
                return new EADummyPopulations(3, InputPopulations.getSolutionDataForT4PS2G5E3()[p._trialID]);
            return null;
        };

        pE._TDCF = new TrialDataContainerFactory(pTDCF);
        ExperimentPerformer executor = new ExperimentPerformer(pE);

        // execute
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        assertEquals(2, s.getCompletedScenarios());
        assertEquals(0, s.getTerminatedScenarios());
        assertEquals(2, s.getSkippedScenarios());

        assertFalse(s.getScenariosSummaries()[0].isSkipped());
        assertFalse(s.getScenariosSummaries()[0].isTerminatedDueToException());
        assertEquals(4, s.getScenariosSummaries()[0].getNoTrials());
        assertEquals(4, s.getScenariosSummaries()[0].getCompletedTrials());
        assertEquals(0, s.getScenariosSummaries()[0].getTerminatedTrials());

        assertTrue(s.getScenariosSummaries()[1].isSkipped());
        assertFalse(s.getScenariosSummaries()[1].isTerminatedDueToException());
        assertEquals(0, s.getScenariosSummaries()[1].getNoTrials());
        assertEquals(0, s.getScenariosSummaries()[1].getCompletedTrials());
        assertEquals(0, s.getScenariosSummaries()[1].getTerminatedTrials());

        assertTrue(s.getScenariosSummaries()[2].isSkipped());
        assertFalse(s.getScenariosSummaries()[2].isTerminatedDueToException());
        assertEquals(0, s.getScenariosSummaries()[2].getNoTrials());
        assertEquals(0, s.getScenariosSummaries()[2].getCompletedTrials());
        assertEquals(0, s.getScenariosSummaries()[2].getTerminatedTrials());

        assertFalse(s.getScenariosSummaries()[3].isSkipped());
        assertFalse(s.getScenariosSummaries()[3].isTerminatedDueToException());
        assertEquals(4, s.getScenariosSummaries()[3].getNoTrials());
        assertEquals(4, s.getScenariosSummaries()[3].getCompletedTrials());
        assertEquals(0, s.getScenariosSummaries()[3].getTerminatedTrials());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println(s);

        // inspect files
        String[][] files = new String[][]
                {
                        {
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "MIN0_0.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "MIN0_1.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "MIN0_2.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "MIN0_3.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "SUM1_0.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "SUM1_1.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "SUM1_2.bin",
                                mainPath + File.separatorChar + "PS_3_GEN_10" + File.separatorChar + "SUM1_3.bin",
                        },
                        {
                                mainPath + File.separatorChar + "PS_2_GEN_5" + File.separatorChar + "MEAN2_0.bin",
                                mainPath + File.separatorChar + "PS_2_GEN_5" + File.separatorChar + "MEAN2_1.bin",
                                mainPath + File.separatorChar + "PS_2_GEN_5" + File.separatorChar + "MEAN2_2.bin",
                                mainPath + File.separatorChar + "PS_2_GEN_5" + File.separatorChar + "MEAN2_3.bin",
                        }
                };

        for (String[] pths : files)
            for (String pth : pths) assertTrue(new File(pth).exists());


        // create scenario level results
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("=== SCENARIO SUMMARIZER PHASE =====================================");
        ScenariosSummarizer TA = new ScenariosSummarizer(pE);
        s = TA.execute();

        assertFalse(s.isTerminatedDueToException());
        assertEquals(4, s.getScenariosSummaries().length);
        assertEquals(0, s.getCompletedScenarios());
        assertEquals(2, s.getTerminatedScenarios());
        assertEquals(2, s.getSkippedScenarios());

        assertTrue(s.getScenariosSummaries()[0].isTerminatedDueToException());
        assertEquals("Dummy exception (create)", s.getScenariosSummaries()[0].getExceptionMessage()[2].substring(10, 34));
        assertFalse(s.getScenariosSummaries()[0].isSkipped());

        assertFalse(s.getScenariosSummaries()[1].isTerminatedDueToException());
        assertTrue(s.getScenariosSummaries()[1].isSkipped());

        assertFalse(s.getScenariosSummaries()[2].isTerminatedDueToException());
        assertTrue(s.getScenariosSummaries()[2].isSkipped());

        assertTrue(s.getScenariosSummaries()[3].isTerminatedDueToException());
        assertEquals("Dummy exception (create)", s.getScenariosSummaries()[3].getExceptionMessage()[2].substring(10, 34));
        assertFalse(s.getScenariosSummaries()[3].isSkipped());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println(s);

        // remove outputs
        try
        {
            FileUtils.removeFolderRecursively(mainPath, 5, 200, true);
        } catch (InterruptedException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}