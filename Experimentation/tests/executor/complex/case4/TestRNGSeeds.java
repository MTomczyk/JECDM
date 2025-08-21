package executor.complex.case4;

import condition.ScenarioDisablingConditions;
import condition.TrialDisablingConditions;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import ea.EA;
import executor.ExperimentPerformer;
import indicator.IIndicator;
import io.FileUtils;
import org.junit.jupiter.api.Test;
import phase.PhasesBundle;
import summary.ScenarioSummary;
import summary.Summary;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides a more complex test for the {@link ExperimentPerformer} class.
 * This test checks if RNGs' seeds are set correctly.
 *
 * @author MTomczyk
 */
class TestRNGSeeds
{

    /**
     * Test.
     */
    @Test
    void test()
    {

        int noTrials = 8;
        boolean[] disabledTrials = new boolean[]{true, false, true, false, false, false, true, false};

        for (int c = 0; c < 4; c++)
        {
            ExperimentPerformer.Params pE = new ExperimentPerformer.Params();

            GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
            pGDC._scenarioKeys = new String[]{"ALG", "PS", "GEN"};
            pGDC._scenarioValues = new String[][]{{"ALG1", "ALG2"}, {"2", "3"}, {"10", "20"}};
            pGDC._noThreads = 8;
            pGDC._noTrials = noTrials;

            if ((c == 2) || (c == 3))
            {
                pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[2];
                pGDC._scenarioDisablingConditions[0] = new ScenarioDisablingConditions("PS", "3");
                pGDC._scenarioDisablingConditions[1] = new ScenarioDisablingConditions("PS", "2");
            }

            if ((c == 1) || (c == 3))
            {
                pGDC._trialDisablingConditions = new TrialDisablingConditions(8);
                for (int t = 0; t < noTrials; t++) if (disabledTrials[t]) pGDC._trialDisablingConditions.disableTrial(t);
            }

            String folderPath = "";
            String msg = null;
            try
            {
                folderPath = FileUtils.getPathRelatedToClass(TestRNGSeeds.class,
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
            pSDCF._indicators = new IIndicator[]{new SeedIndicator()};
            pSDCF._generations = 1;
            pE._SDCF = new ScenarioDataContainerFactory(pSDCF);

            TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
            pTDCF._eaInitializer = (R, p) -> {
                EA.Params pEA = new EA.Params(R, null);
                PhasesBundle pb = PhasesBundle.getNulledInstance();
                pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(pb);
                return new EA(pEA);
            };
            pE._TDCF = new TrialDataContainerFactory(pTDCF);

            ExperimentPerformer executor = new ExperimentPerformer(pE);

            Summary s = executor.execute();

            for (ScenarioSummary scS : s.getScenariosSummaries())
            {
                System.out.println(scS.getScenario().toString());
                if (scS.isSkipped()) continue;
                for (int t = 0; t < noTrials; t++)
                {
                    if (disabledTrials[t]) continue;

                    int expSeed = scS.getScenario().getID() * noTrials + t;
                    String path = mainPath + File.separatorChar + scS.getScenario().toString() + File.separatorChar + "SEED_" + t + ".bin";
                    System.out.println(path);

                    try (FileInputStream fis = new FileInputStream(path);
                         DataInputStream dis = new DataInputStream(fis))
                    {
                        double readSeed = dis.readDouble(); // 42
                        assertEquals(expSeed, (int) (readSeed + 1.0E-6));
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
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
}