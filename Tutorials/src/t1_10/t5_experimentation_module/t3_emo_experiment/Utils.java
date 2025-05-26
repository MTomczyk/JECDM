package t1_10.t5_experimentation_module.t3_emo_experiment;

import container.Containers;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.AbstractTrialDataContainer;
import container.trial.TrialDataContainerFactory;
import container.trial.initialziers.IEAInitializer;
import ea.EA;
import emo.aposteriori.moead.MOEAD;
import emo.aposteriori.nsga.NSGA;
import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaiii.NSGAIII;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import exception.TrialException;
import indicator.ExecutionTime;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import indicator.emo.GD;
import indicator.emo.HV;
import indicator.emo.IGD;
import io.cross.excel.ConvergenceXLSX;
import io.cross.excel.FinalRankerXLSX;
import io.cross.excel.FinalStatisticsXLSX;
import io.scenario.excel.SummarizerXLSX;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.ReferencePointsFactory;
import random.IRandom;
import random.MersenneTwister64;
import scenario.CrossedSetting;
import space.distance.Euclidean;
import space.normalization.INormalization;
import space.simplex.DasDennis;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.WilcoxonSignedRank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Provides some utility methods for the tutorial.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicateBranchesInSwitch", "DuplicatedCode"})
public class Utils
{
    /**
     * Creates global data container, scenario data container factory, and trial data container factory,
     * and returns them via {@link Containers}.
     *
     * @return created containers
     */
    public static Containers getContainers()
    {
        // Map containing data on the number of cuts for DND method given a number of objectives
        HashMap<Integer, Integer> cutsMap = Utils.getDNDCutsMap();
        System.out.println("Cuts map = " + cutsMap);

        // Map containing data on the population size given a number of objectives
        HashMap<Integer, Integer> psMap = Utils.getPSMap(cutsMap);
        System.out.println("Population size map = " + psMap);

        System.out.println("Creating reference points...");
        HashMap<Problem, HashMap<Integer, double[][]>> referencePoints = Utils.getRPs(psMap, new MersenneTwister64(0));
        System.out.println("Reference points are created...");

        // GLOBAL DATA CONTAINER =======================================================================================
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._mainPath = "E:\\emo_test";
        pGDC._noTrials = 100;
        pGDC._noThreads = 20;
        pGDC._useMonitorThread = true; // turn on (true by default)
        pGDC._monitorReportingInterval = 20000;

        pGDC._scenarioKeys = new String[]{
                "ALGORITHM", // 0
                "PROBLEM",  // 1
                "OBJECTIVES", // 2
        };

        // Define scenario values (1:1 mapping with keys):
        pGDC._scenarioValues = new String[][]{

                {
                        "NSGA", "NSGAII", "NSGAIII", "MOEAD"
                }, // 0 (note that special symbols, e.g., / in MOEAD, are not allowed)
                {
                        "DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4",
                        "WFG1ALPHA02", "WFG2", "WFG3", "WFG4",
                },
                {
                        "2", "3", "4", "5"
                }, // 2

        };

        // Use XLSX summarizer:
        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());

        // Create one default crossed scenario (all keys are used):
        pGDC._crossedSettings = new CrossedSetting[1];
        pGDC._crossedSettings[0] = new CrossedSetting(new String[]{"PROBLEM", "OBJECTIVES", "ALGORITHM"},
                new String[][]{
                        pGDC._scenarioValues[1].clone(),
                        pGDC._scenarioValues[2].clone(),
                        pGDC._scenarioValues[0].clone()
                });

        // Use three cross-savers:
        pGDC._referenceCrossSavers = new LinkedList<>();
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(3));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(3));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM", new ITest[]{new WilcoxonSignedRank()}, 3));

        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);


        // SCENARIO DATA CONTAINER =======================================================================================
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();

        // Conditional number of generations (different problems are more or less challenging, the limit is suitably adjusted)
        pSDCF._numberOfGenerationsInitializer = p -> {
            int mul = p._scenario.getObjectives() - 1;
            String problem = p._scenario.getProblem();
            switch (problem)
            {
                case "DTLZ1" ->
                {
                    return 400 + 100 * mul;
                }
                case "DTLZ2", "DTLZ4" ->
                {
                    return 200 + 50 * mul;
                }
                case "DTLZ3" ->
                {
                    return 1000 + 250 * mul;
                }
                case "WFG1ALPHA02" ->
                {
                    return 1000 + 200 * mul;
                }
                default ->
                {
                    return 400 + 100 * mul;
                }
            }
        };

        // Conditional number of SSR (different for MOEA/D, SSR = PS)
        pSDCF._numberOfSteadyStateRepeatsInitializer = p -> {
            if (p._scenario.getAlgorithm().equals("MOEAD"))
            {
                return psMap.get(p._scenario.getObjectives());
            }
            return 1;
        };

        // We will use IGD and GD, and HV, and measure the execution time. The former two have to be parameterized
        // conditionally (reference points depend on the problem).
        pSDCF._indicatorsInitializer = p -> {
            IIndicator[] indicators = new IIndicator[4]; // create four indicators
            Problem problem = AbstractMOOProblemBundle.getProblemFromString(p._scenario.getProblem()); // get problem ID
            int M = p._scenario.getObjectives(); // get the number of objectives
            INormalization[] normalizations = AbstractMOOProblemBundle.getNormalizations(problem, M); // derive proper normalizations
            double[][] rps = referencePoints.get(problem).get(M); // get the pre-calculated reference points
            indicators[0] = new PerformanceIndicator(new IGD(new Euclidean(normalizations), rps)); // store IGD (performance indicator = simple wrapper)
            indicators[1] = new PerformanceIndicator(new GD(new Euclidean(normalizations), rps)); // store GD (performance indicator = simple wrapper)
            double[] rp = new double[M];
            // shift the nadir to 1.1 (all objectives are to be minimized)
            Arrays.fill(rp, 1.1d);
            HV.Params pHV = new HV.Params(M, normalizations, rp);
            pHV._toleranceDuplicates = 0.00001d;
            pHV._deriveNonDominatedSet = true;
            indicators[2] = new PerformanceIndicator(new HV(pHV)); // HV
            indicators[3] = new PerformanceIndicator(new ExecutionTime()); // Execution time (ms)
            return indicators;
        };

        // Use standard statistics:
        pSDCF._statistics = new IStatistic[4];
        pSDCF._statistics[0] = new Min();
        pSDCF._statistics[1] = new Mean();
        pSDCF._statistics[2] = new Max();
        pSDCF._statistics[3] = new StandardDeviation();

        pSDCF._dataStoringInterval = 500;

        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TRIAL DATA CONTAINER =======================================================================================
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();

        // Create EA initializer (all methods are set to dynamically update OS:
        pTDCF._eaInitializer = new IEAInitializer()
        {
            @Override
            public EA instantiateEA(IRandom R, AbstractTrialDataContainer.Params p) throws TrialException
            {
                // Problem bundle should be available:
                if (p._problemBundle == null)
                    throw new TrialException("Problem is unavailable", null, this.getClass(), p._SDC.getScenario(), p._trialID);
                if (!(p._problemBundle instanceof AbstractMOOProblemBundle problemBundle))
                    throw new TrialException("Problem is not MOO", null, this.getClass(), p._SDC.getScenario(), p._trialID);

                String algorithm = p._SDC.getScenario().getAlgorithm();
                int objectives = p._SDC.getObjectives();
                int PS = psMap.get(objectives);
                int cuts = cutsMap.get(objectives);

                switch (algorithm)
                {
                    case "NSGA" ->
                    {
                        return NSGA.getNSGA(0, true, 0.1d, PS, R, problemBundle);
                    }
                    case "NSGAII" ->
                    {
                        return NSGAII.getNSGAII(0, true, PS, R, problemBundle);
                    }
                    case "NSGAIII" ->
                    {
                        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(objectives, cuts);
                        return NSGAIII.getNSGAIII(0, true, true, R,
                                goals, problemBundle, new RandomAssignment(), new RandomSpecimen());
                    }
                    case "MOEAD" ->
                    {
                        IGoal[] goals = GoalsFactory.getPBIsDND(objectives, cuts, 5.0d);
                        return MOEAD.getMOEAD(0, true, true, R, goals, problemBundle,
                                new emo.utils.decomposition.similarity.lnorm.Euclidean(), 10);
                    }
                    default ->
                    {
                        return null;
                    }
                }
            }
        };

        // Create the factory:
        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        return new Containers(GDC, SDCF, TDCF);
    }


    /**
     * Provides a map containing data on the number of cuts for the DND method given a number of objectives.
     *
     * @return map  (key = objectives; value = no. cuts)
     */
    public static HashMap<Integer, Integer> getDNDCutsMap()
    {
        HashMap<Integer, Integer> cutsMap = new HashMap<>(5);
        cutsMap.put(2, 29);
        cutsMap.put(3, 10);
        cutsMap.put(4, 7);
        cutsMap.put(5, 6);
        return cutsMap;
    }

    /**
     * Provides a map containing data on the number population size method given the input number of cuts for the DND method.
     *
     * @param cutsMap map obtained via {@link Utils#getDNDCutsMap()}
     * @return map (key = objectives; value = ps)
     */
    public static HashMap<Integer, Integer> getPSMap(HashMap<Integer, Integer> cutsMap)
    {
        HashMap<Integer, Integer> psMap = new HashMap<>(5);
        for (int i : new int[]{2, 3, 4, 5})
        {
            int cuts = cutsMap.get(i);
            int ps = DasDennis.getWeightVectors(i, cuts).size();
            psMap.put(i, ps);
        }
        return psMap;
    }

    /**
     * Creates the reference points (read-only data set). The points are stored in a 2-level associative array.
     * The first key is the problem ID, while the second is the number of objectives. The reference points are random
     * Pareto optimal solutions to the problem. The number is set to the respective population size * 4  (arbitrary
     * decision; one should adjust to one's own needs).
     *
     * @param psMap ma obtained via {@link Utils#getPSMap(HashMap)}
     * @param R     random number generator
     * @return reference points (given a problem and the number of objectives)
     */
    public static HashMap<Problem, HashMap<Integer, double[][]>> getRPs(HashMap<Integer, Integer> psMap,
                                                                        IRandom R)
    {
        // Important note: some of the problems have the same PF. Thus, the whole result could be simplified.
        HashMap<Problem, HashMap<Integer, double[][]>> rpsMap = new HashMap<>(5 * 8);
        for (Problem problem : new Problem[]{Problem.DTLZ1, Problem.DTLZ2, Problem.DTLZ3, Problem.DTLZ4,
                Problem.WFG1ALPHA02, Problem.WFG2, Problem.WFG3, Problem.WFG4})
        {
            HashMap<Integer, double[][]> rps = new HashMap<>();
            for (Integer o : new Integer[]{2, 3, 4, 5})
            {
                int ps = psMap.get(o);
                double[][] pfPoints = ReferencePointsFactory.getRandomReferencePoints(problem, ps * 2, o, R);
                rps.put(o, pfPoints);
            }
            rpsMap.put(problem, rps);
        }
        return rpsMap;
    }
}
