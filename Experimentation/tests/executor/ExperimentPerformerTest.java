package executor;

import condition.ScenarioDisablingConditions;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import ea.dummy.populations.EADummyPopulations;
import indicator.Evaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.FileUtils;
import org.junit.jupiter.api.Test;
import scenario.Keys;
import statistics.IStatistic;
import statistics.Min;
import statistics.Sum;
import summary.Summary;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ExperimentPerformer} class.
 *
 * @author MTomczyk
 */
class ExperimentPerformerTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        ExperimentPerformer e = new ExperimentPerformer(pE);
        Summary s = e.execute();
        assertTrue(s.isTerminatedDueToException());
        assertEquals("===", s.getExceptionMessage()[0].substring(0, 3));
        System.out.println(s);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        pE._GDC = new GlobalDataContainer(new GlobalDataContainer.Params());
        ExperimentPerformer e = new ExperimentPerformer(pE);
        Summary s = e.execute();
        assertTrue(s.isTerminatedDueToException());
        assertEquals("===", s.getExceptionMessage()[0].substring(0, 3));
        System.out.println(s);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        pE._GDC = new GlobalDataContainer(new GlobalDataContainer.Params());
        pE._SDCF = new ScenarioDataContainerFactory(new ScenarioDataContainerFactory.Params());
        ExperimentPerformer e = new ExperimentPerformer(pE);
        Summary s = e.execute();
        assertTrue(s.isTerminatedDueToException());
        assertEquals("===", s.getExceptionMessage()[0].substring(0, 3));
        System.out.println(s);
    }


    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};

        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
            msg = e.getMessage();
        }
        assertNull(msg);

        pGDC._mainPath = folderPath + File.separatorChar + "executor_temporary_output";
        System.out.println(pGDC._mainPath);
        pE._GDC = new GlobalDataContainer(pGDC);
        pE._SDCF = new ScenarioDataContainerFactory(new ScenarioDataContainerFactory.Params());
        ExperimentPerformer e = new ExperimentPerformer(pE);
        Summary s = e.execute();
        assertTrue(s.isTerminatedDueToException());
        assertEquals("===", s.getExceptionMessage()[0].substring(0, 3));
        System.out.println(s);
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        pGDC._noTrials = 10;
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
            msg = e.getMessage();
        }
        assertNull(msg);

        pGDC._mainPath = folderPath + File.separatorChar + "executor_temporary_output";
        String mainPath = pGDC._mainPath;
        System.out.println(pGDC._mainPath);
        pE._GDC = new GlobalDataContainer(pGDC);
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._indicators = new IIndicator[]{new PerformanceIndicator(new Evaluation(new Sum()))};
        pSDCF._statistics = new IStatistic[]{new Sum()};
        pE._SDCF = new ScenarioDataContainerFactory(pSDCF);

        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R, p) -> new EADummyPopulations(1, new double[][][]{{{1.0d}}});
        pE._TDCF = new TrialDataContainerFactory(pTDCF);


        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAIII",
                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P2_ALGORITHM_NSGAIII"
        };

        for (String f : subfolders) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
            for (int i = 0; i < pGDC._noTrials; i++) {
                File tf = new File(folder.toString() + File.separatorChar + f + File.separatorChar + "SUM0_" + i + ".bin");
                assertTrue(tf.exists());
            }
        }


        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }

        assertNull(msg);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
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
        pE._TDCF = new TrialDataContainerFactory(new TrialDataContainerFactory.Params());
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAIII",
                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P2_ALGORITHM_NSGAIII"
        };

        for (String f : subfolders) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
        }

        System.out.println(folder);

        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }

        assertNull(msg);
    }


    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM},
                        new String[]{"P1", "NSGAIII"})
        };
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
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
        pE._TDCF = new TrialDataContainerFactory(new TrialDataContainerFactory.Params());
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P2_ALGORITHM_NSGAIII"
        };

        String[] notexisting = new String[]{
                "PROBLEM_P1_ALGORITHM_NSGAIII",
        };

        for (String f : notexisting) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println("Not existing = " + sf);
            assertFalse(sf.exists());
        }

        for (String f : subfolders) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
        }

        System.out.println(folder);

        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }

        assertNull(msg);
    }


    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(Keys.KEY_PROBLEM, "P1")
        };
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
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
        pE._TDCF = new TrialDataContainerFactory(new TrialDataContainerFactory.Params());
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{

                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P2_ALGORITHM_NSGAIII"
        };

        String[] notexisting = new String[]{
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAIII",
        };

        for (String f : notexisting) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println("Not existing = " + sf);
            assertFalse(sf.exists());
        }

        for (String f : subfolders) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
        }

        System.out.println(folder);

        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }

        assertNull(msg);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(Keys.KEY_PROBLEM, "P1"),
                new ScenarioDisablingConditions(Keys.KEY_ALGORITHM, "NSGAII")
        };
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
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
        pE._TDCF = new TrialDataContainerFactory(new TrialDataContainerFactory.Params());
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] subfolders = new String[]{
                "PROBLEM_P2_ALGORITHM_NSGAIII"
        };

        String[] notexisting = new String[]{
                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAIII",
        };

        for (String f : notexisting) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println("Not existing = " + sf);
            assertFalse(sf.exists());
        }

        for (String f : subfolders) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println(sf);
            assertTrue(sf.exists());
        }

        System.out.println(folder);

        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{Keys.KEY_PROBLEM, Keys.KEY_ALGORITHM};
        pGDC._scenarioValues = new String[][]{{"P1", "P2",}, {"NSGAII", "NSGAIII"}};
        pGDC._noThreads = 20;
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(Keys.KEY_PROBLEM, "P1"),
                new ScenarioDisablingConditions(Keys.KEY_PROBLEM, "P2")
        };
        String folderPath = "";
        String msg = null;
        try {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerTest.class, "Experimentation", "tests", File.separatorChar).toString();
        } catch (IOException e) {
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
        pE._TDCF = new TrialDataContainerFactory(new TrialDataContainerFactory.Params());
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertFalse(s.isTerminatedDueToException());
        System.out.println(s);

        File folder = new File(mainPath);
        assertTrue(folder.isDirectory());
        assertTrue(folder.exists());

        String[] notexisting = new String[]{
                "PROBLEM_P2_ALGORITHM_NSGAIII",
                "PROBLEM_P2_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAII",
                "PROBLEM_P1_ALGORITHM_NSGAIII",
        };

        for (String f : notexisting) {
            File sf = new File(folder.toString() + File.separatorChar + f);
            System.out.println("Not existing = " + sf);
            assertFalse(sf.exists());
        }

        System.out.println(folder);

        // remove outputs
        try {
            FileUtils.removeFolderRecursively(mainPath, 5, 100, true);
        } catch (InterruptedException e) {
            msg = e.getMessage();
        }
        assertNull(msg);
    }
}