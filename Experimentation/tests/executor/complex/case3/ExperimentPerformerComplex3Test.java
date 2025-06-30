package executor.complex.case3;

import condition.ScenarioDisablingConditions;
import condition.TrialDisablingConditions;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import ea.dummy.populations.EADummyPopulations;
import executor.CrossSummarizer;
import executor.ExperimentPerformer;
import executor.ScenariosSummarizer;
import indicator.Evaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.FileUtils;
import io.cross.excel.*;
import io.scenario.SummarizerTXT;
import io.scenario.excel.SummarizerXLSX;
import io.utils.excel.Style;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import scenario.CrossedSetting;
import statistics.IStatistic;
import statistics.Max;
import statistics.Mean;
import statistics.Min;
import statistics.tests.ITest;
import statistics.tests.MannWhitneyU;
import statistics.tests.TStudent;
import statistics.tests.WilcoxonSignedRank;
import summary.CrossedExaminerSummary;
import summary.Summary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides a more complex test for the {@link ExperimentPerformer} class.
 * This tests uses dummy inputs that mimic the solutions' evaluations generated throughout evolutionary processes.
 *
 * @author MTomczyk
 */
class ExperimentPerformerComplex3Test
{

    /**
     * Test.
     */
    @Test
    void test()
    {
        ExperimentPerformer.Params pE = new ExperimentPerformer.Params();

        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._scenarioKeys = new String[]{"ALG", "PS", "GEN"};
        pGDC._scenarioValues = new String[][]{{"ALG1", "ALG2"}, {"2", "3"}, {"10", "20"}};
        pGDC._noThreads = 8;
        pGDC._noTrials = 8;
        pGDC._trialDisablingConditions = new TrialDisablingConditions(8);
        pGDC._trialDisablingConditions.disableTrials(4, 7);
        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]{
                new ScenarioDisablingConditions(new String[]{"PS", "GEN"}, new String[]{"2", "20"}),
                new ScenarioDisablingConditions(new String[]{"PS", "GEN"}, new String[]{"3", "10"}),
        };

        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerTXT());
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());

        pGDC._crossedSettings = new CrossedSetting[]
                {
                        new CrossedSetting(new String[]{"ALG", "PS", "GEN"},
                                new String[][]{{"ALG2", "ALG1"}, {"3", "2"}, {"20", "10"}},
                                null),
                        new CrossedSetting(new String[]{"ALG", "PS"},
                                new String[][]{{"ALG2", "ALG1"}, {"3", "2"}},
                                null),
                        new CrossedSetting(new String[]{"ALG"},
                                new String[][]{{"ALG2", "ALG1"}},
                                null)
                };
        pGDC._referenceCrossSavers = new LinkedList<>();
        Style style = new Style();
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLS(2, style));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(2, style));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLS(3, style));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(3, style));

        pGDC._referenceCrossSavers.add(new ConvergenceXLS(1, style));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(1, style));
        pGDC._referenceCrossSavers.add(new ConvergenceXLS(2, style));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(2, style));
        pGDC._referenceCrossSavers.add(new ConvergenceXLS(3, style));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(3, style));

        ITest[] Ts = new ITest[]{
                TStudent.getUnpairedTest(true, true),
                new WilcoxonSignedRank(),
                new MannWhitneyU()};
        pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALG", Ts, 3));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALG", Ts, 3));
        pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALG", Ts, 2));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALG", Ts, 2));
        pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALG", Ts, 1));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALG", Ts, 1));

        pGDC._unifiedStatisticFunctionsNames = new String[]{Min._name, Mean._name, Max._name};

        String folderPath = "";
        String msg = null;
        try
        {
            folderPath = FileUtils.getPathRelatedToClass(ExperimentPerformerComplex3Test.class,
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
            if (p._scenario.getKeyValuesMap().get("PS").getValue().equals("3")) {
                return new IIndicator[]{
                        new PerformanceIndicator(new Evaluation(new Mean(), 0)),
                        new PerformanceIndicator(new Evaluation(new Mean(), 1)),
                        new PerformanceIndicator(new Evaluation(new Mean(), 2))};
            }
            else return new IIndicator[]{new PerformanceIndicator(new Evaluation(new Mean(), 0)),
                    new PerformanceIndicator(new Evaluation(new Mean(), 1))};
        };

        pSDCF._numberOfGenerationsInitializer = p -> Integer.parseInt(p._scenario.getKeyValuesMap().get("GEN").toString());
        pSDCF._statisticFunctionsInitializer = p ->
        {
            if (p._scenario.getKeyValuesMap().get("PS").getValue().equals("3"))
            {
                return new IStatistic[]{new Min(), new Mean(), new Max()};
            }
            else return new IStatistic[]{new Max(), new Min()};
        };

        pSDCF._dataLoadingInterval = 4;
        pSDCF._dataStoringInterval = 4;
        pE._SDCF = new ScenarioDataContainerFactory(pSDCF);

        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R, p) ->
        {
            String alg = p._SDC.getScenario().getKeyValuesMap().get("ALG").getValue();
            int gen = p._SDC.getGenerations();

            if (gen == 20)
            {
                if (alg.equals("ALG1"))
                    return new EADummyPopulations(3, InputPopulations.getSolutionDataForT4ALG1PS3G20E3()[p._trialID]);
                if (alg.equals("ALG2"))
                    return new EADummyPopulations(3, InputPopulations.getSolutionDataForT4ALG2PS3G20E3()[p._trialID]);
            }
            else if (gen == 10)
            {
                if (alg.equals("ALG1"))
                    return new EADummyPopulations(2, InputPopulations.getSolutionDataForT4ALG1PS2G10E2()[p._trialID]);
                if (alg.equals("ALG2"))
                    return new EADummyPopulations(2, InputPopulations.getSolutionDataForT4ALG2PS2G10E2()[p._trialID]);
            }

            return null;
        };

        pE._TDCF = new TrialDataContainerFactory(pTDCF);
        ExperimentPerformer executor = new ExperimentPerformer(pE);
        Summary s = executor.execute();
        assertEquals(8, s.getScenariosSummaries().length);
        assertEquals(4, s.getCompletedScenarios());
        assertEquals(0, s.getTerminatedScenarios());
        assertEquals(4, s.getSkippedScenarios());
        assertFalse(s.isTerminatedDueToException());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(s);


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("=== SCENARIO SUMMARIZER PHASE =====================================");
        ScenariosSummarizer TA = new ScenariosSummarizer(pE);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        s = TA.execute();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        assertFalse(s.isTerminatedDueToException());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("=== CROSS SUMMARIZER PHASE =====================================");
        CrossSummarizer CE = new CrossSummarizer(pE);
        s = CE.execute();
        assertTrue(s instanceof CrossedExaminerSummary);
        CrossedExaminerSummary cs = (CrossedExaminerSummary) s;
        System.out.println(s);

        // EXCEL LOADING load data and compare the results
        {
            String[] excelResults = new String[]{
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_NONE_COMPARED_ALG_PS_GEN" + File.separatorChar + "FIXED_NONE_COMPARED_ALG_PS_GEN_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_3D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_NONE_COMPARED_ALG_PS_GEN" + File.separatorChar + "FIXED_NONE_COMPARED_ALG_PS_GEN_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_3D.xlsx",
            };

            for (int f = 0; f < 2; f++)
            {
                FileInputStream file = null;
                try
                {
                    file = new FileInputStream(excelResults[f]);
                } catch (FileNotFoundException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(file);

                Workbook workbook = null;
                try
                {
                    if (f == 0) workbook = new HSSFWorkbook(file);
                    else workbook = new XSSFWorkbook(file);
                } catch (IOException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(workbook);

                for (int sheet = 0; sheet < 3; sheet++)
                {
                    Sheet sh = workbook.getSheetAt(sheet);
                    assertEquals(ExpectedResults34._sheets3[sheet], sh.getSheetName());

                    for (int row = 0; row < 8; row++)
                    {
                        Row r = sh.getRow(style._tableMarginY + 2 + row);
                        for (int column = 0; column < 13; column++)
                        {
                            Cell cell = r.getCell(style._tableMarginX + 6 + column);
                            if (ExpectedResults34._expectedRanks3[sheet][row][column] == null)
                            {
                                assertSame(cell.getCellType(), CellType.BLANK);
                            }
                            else
                                assertEquals(ExpectedResults34._expectedRanks3[sheet][row][column],
                                        cell.getNumericCellValue(), 0.0000001d);
                        }
                    }
                }
            }
        }

        // EXCEL LOADING load data and compare the results
        {
            String[] excelResults = new String[]{
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_GEN_10_COMPARED_ALG_PS" + File.separatorChar + "FIXED_GEN_10_COMPARED_ALG_PS_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_2D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_GEN_10_COMPARED_ALG_PS" + File.separatorChar + "FIXED_GEN_10_COMPARED_ALG_PS_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_2D.xlsx",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_GEN_20_COMPARED_ALG_PS" + File.separatorChar + "FIXED_GEN_20_COMPARED_ALG_PS_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_2D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_GEN_20_COMPARED_ALG_PS" + File.separatorChar + "FIXED_GEN_20_COMPARED_ALG_PS_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_2D.xlsx",
            };

            for (int f = 0; f < 4; f++)
            {
                FileInputStream file = null;
                try
                {
                    file = new FileInputStream(excelResults[f]);
                } catch (FileNotFoundException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(file);

                Workbook workbook = null;
                try
                {
                    if ((f == 0) || (f == 2)) workbook = new HSSFWorkbook(file);
                    else workbook = new XSSFWorkbook(file);
                } catch (IOException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(workbook);

                int num;
                if (f < 2) num = ExpectedResults34._sheetsFixedGen10.length;
                else num = ExpectedResults34._sheetsFixedGen20.length;
                assertEquals(num, workbook.getNumberOfSheets());

                for (int sheet = 0; sheet < num; sheet++)
                {
                    Sheet sh = workbook.getSheetAt(sheet);
                    if (f < 2) assertEquals(ExpectedResults34._sheetsFixedGen10[sheet], sh.getSheetName());
                    else assertEquals(ExpectedResults34._sheetsFixedGen20[sheet], sh.getSheetName());

                    Double[][] ER;
                    if (f < 2) ER = ExpectedResults34._expectedRanksFixedGen10[sheet];
                    else ER = ExpectedResults34._expectedRanksFixedGen20[sheet];

                    for (int row = 0; row < 4; row++)
                    {
                        Row r = sh.getRow(style._tableMarginY + 2 + row);
                        for (int column = 0; column < 13; column++)
                        {
                            Cell cell = r.getCell(style._tableMarginX + 4 + column);
                            if (ER[row][column] == null) assertSame(cell.getCellType(), CellType.BLANK);
                            else assertEquals(ER[row][column], cell.getNumericCellValue(), 0.0000001d);
                        }
                    }
                }
            }
        }


        // EXCEL LOADING load data and compare the results
        {
            String[] excelResults = new String[]{
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_2_GEN_10_COMPARED_ALG" + File.separatorChar + "FIXED_PS_2_GEN_10_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_2_GEN_10_COMPARED_ALG" + File.separatorChar + "FIXED_PS_2_GEN_10_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xlsx",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_2_GEN_20_COMPARED_ALG" + File.separatorChar + "FIXED_PS_2_GEN_20_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_2_GEN_20_COMPARED_ALG" + File.separatorChar + "FIXED_PS_2_GEN_20_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xlsx",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_3_GEN_10_COMPARED_ALG" + File.separatorChar + "FIXED_PS_3_GEN_10_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_3_GEN_10_COMPARED_ALG" + File.separatorChar + "FIXED_PS_3_GEN_10_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xlsx",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_3_GEN_20_COMPARED_ALG" + File.separatorChar + "FIXED_PS_3_GEN_20_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xls",
                    mainPath + File.separatorChar + "CROSSED_RESULTS" + File.separatorChar +
                            "FIXED_PS_3_GEN_20_COMPARED_ALG" + File.separatorChar + "FIXED_PS_3_GEN_20_COMPARED_ALG_generation_final_ranker_key_ALG_TSTUDENT_WSR_MWU_1D.xlsx",
            };

            for (int f = 0; f < 8; f++)
            {
                FileInputStream file = null;
                try
                {
                    file = new FileInputStream(excelResults[f]);
                } catch (FileNotFoundException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(file);

                Workbook workbook = null;
                try
                {
                    if ((f == 0) || (f == 2) || (f == 4) || (f == 6)) workbook = new HSSFWorkbook(file);
                    else workbook = new XSSFWorkbook(file);
                } catch (IOException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(workbook);

                int num;
                if (f < 2) num = ExpectedResults34._sheetsFixedPS2Gen10.length;
                else if (f < 4) num = ExpectedResults34._sheetsFixedPS2Gen20.length;
                else if (f < 6) num = ExpectedResults34._sheetsFixedPS3Gen10.length;
                else num = ExpectedResults34._sheetsFixedPS3Gen20.length;
                assertEquals(num, workbook.getNumberOfSheets());

                for (int sheet = 0; sheet < num; sheet++)
                {
                    Sheet sh = workbook.getSheetAt(sheet);
                    if (f < 2) assertEquals(ExpectedResults34._sheetsFixedPS2Gen10[sheet], sh.getSheetName());
                    else if (f < 4) assertEquals(ExpectedResults34._sheetsFixedPS2Gen20[sheet], sh.getSheetName());
                    else if (f < 6) assertEquals(ExpectedResults34._sheetsFixedPS3Gen10[sheet], sh.getSheetName());
                    else assertEquals(ExpectedResults34._sheetsFixedPS3Gen20[sheet], sh.getSheetName());

                    Double[][] ER;
                    if (f < 2) ER = ExpectedResults34._expectedRanksFixedPS2Gen10[sheet];
                    else if (f < 4) ER = ExpectedResults34._expectedRanksFixedPS2Gen20[sheet];
                    else if (f < 6) ER = ExpectedResults34._expectedRanksFixedPS3Gen10[sheet];
                    else ER = ExpectedResults34._expectedRanksFixedPS3Gen20[sheet];

                    for (int row = 0; row < 2; row++)
                    {
                        Row r = sh.getRow(style._tableMarginY + 2 + row);
                        for (int column = 0; column < 13; column++)
                        {
                            Cell cell = r.getCell(style._tableMarginX + 2 + column);
                            if (ER[row][column] == null) assertSame(cell.getCellType(), CellType.BLANK);
                            else assertEquals(ER[row][column], cell.getNumericCellValue(), 0.0000001d);
                        }
                    }
                }
            }
        }

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