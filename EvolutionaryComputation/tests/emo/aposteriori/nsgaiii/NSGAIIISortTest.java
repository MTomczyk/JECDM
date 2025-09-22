package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import ea.EA;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import phase.PhaseReport;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;
import space.normalization.INormalization;
import space.normalization.minmax.AbstractMinMaxNormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests related to NSGA-III.
 *
 * @author MTomczyk
 */
class NSGAIIISortTest
{
    /**
     * Test 1
     */
    @Test
    void test1()
    {
        double[][] rps = new double[][]{{1.0d, 0.0d}, {0.75d, 0.25d}, {0.5d, 0.5d}, {0.25d, 0.75d}, {0.0d, 1.0d}};
        INormalization[] normalizations = new INormalization[]{
                new Linear(1.0d, 2.0d),
                new Linear(0.0d, 2.0d)
        };

        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(rps, normalizations);
        Criteria criteria = Criteria.constructCriteria("C", 2, false);

        NSGAIIIGoalsManager.Params pGM = new NSGAIIIGoalsManager.Params(goals);
        NSGAIIIGoalsManager GM = new NSGAIIIGoalsManager(pGM);
        GM.updateNormalizations(normalizations);

        IAssignmentResolveTie assignmentResolveTie = (candidates, R) -> candidates.getFirst();
        ISpecimenResolveTie specimenResolveTie = (specimens, R) -> 0;
        NSGAIIISort nsgaiii = new NSGAIIISort(criteria, GM, assignmentResolveTie, specimenResolveTie);

        // Generation 0
        double[][] evals = new double[][]{
                {3.5d, 0.5d}, {1.5d, 2.0d}, {0.5d, 4.0d}, {7.0d, 0.5}, {5.75d, 1.5d},
                {5.5d, 2.0d}, {3.5d, 4.0d}, {2.5d, 5.0d}, {6.0d, 6.0d}, {8.0d, 4.5d},
        };
        for (int i = 0; i < evals.length; i++)
        {
            evals[i][0] = ((AbstractMinMaxNormalization) normalizations[0]).getUnnormalized(evals[i][0]);
            evals[i][1] = ((AbstractMinMaxNormalization) normalizations[1]).getUnnormalized(evals[i][1]);
        }

        ArrayList<Specimen> specimens = new ArrayList<>(evals.length);
        for (int i = 0; i < evals.length; i++) specimens.add(new Specimen(new SpecimenID(0, 0, 0, i), evals[i]));
        SpecimensContainer SC = new SpecimensContainer(specimens);

        EA.Params pEA = new EA.Params("EA", criteria);
        pEA._populationSize = 5;
        pEA._offspringSize = 5;
        EA ea = new EA(pEA);
        ea.setSpecimensContainer(SC);


        String msg = null;

        PhaseReport pR = new PhaseReport();
        try
        {
            nsgaiii.action(ea, pR);
        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertEquals(5, ea.getSpecimensContainer().getPopulation().size());
        HashSet<String> survived = new HashSet<>();
        survived.add("[EA id = 0, generation = 0, steady-state repeat = 0, no. = 0]");
        survived.add("[EA id = 0, generation = 0, steady-state repeat = 0, no. = 1]");
        survived.add("[EA id = 0, generation = 0, steady-state repeat = 0, no. = 2]");
        survived.add("[EA id = 0, generation = 0, steady-state repeat = 0, no. = 5]");
        survived.add("[EA id = 0, generation = 0, steady-state repeat = 0, no. = 7]");
        for (Specimen s : ea.getSpecimensContainer().getPopulation()) System.out.println(s.getID().toString());

        for (Specimen s : ea.getSpecimensContainer().getPopulation())
            assertTrue(survived.contains(s.getID().toString()));

        Assignment[] assignments = GM.getFamilies()[0].getAssignments();
        assertEquals(5, assignments.length);

        int[] expNo = new int[]{1, 1, 1, 0, 0};
        double[][] scores = new double[][]{{0.5d}, {0.3952847}, {Math.sqrt(2.0d) / 4.0d}, null, null};

        for (int i = 0; i < assignments.length; i++)
        {
            assertEquals(expNo[i], assignments[i].getSpecimens().size());
            for (int j = 0; j < expNo[i]; j++)
            {
                assertEquals(scores[i][j], assignments[i].getEvaluations().get(j), 0.0001d);
            }
        }

        // Generation 0
        evals = new double[][]{
                {3.5d, 0.5d}, {1.5d, 2.0d}, {0.5d, 4.0d}, {5.5d, 2.0d}, {2.5d, 5.0d}, // 0, 1, 2, 5, 7
                {4.0d, 0.0d}, {3.0d, 1.5d}, {2.0d, 1.75d}, {1.0d, 2.75d}, {0.0d, 4.5d} // 1.3, 1.4, 1.6, 1.8, 1.9
        };
        for (int i = 0; i < evals.length; i++)
        {
            evals[i][0] = ((AbstractMinMaxNormalization) normalizations[0]).getUnnormalized(evals[i][0]);
            evals[i][1] = ((AbstractMinMaxNormalization) normalizations[1]).getUnnormalized(evals[i][1]);
        }
        specimens = new ArrayList<>(evals.length);
        for (int i = 0; i < evals.length; i++) specimens.add(new Specimen(new SpecimenID(0, 1, 0, i), evals[i]));
        SC = new SpecimensContainer(specimens);
        ea.setSpecimensContainer(SC);

        pR = new PhaseReport();
        try
        {
            nsgaiii.action(ea, pR);
        } catch (PhaseException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);


        assertEquals(5, ea.getSpecimensContainer().getPopulation().size());
        survived = new HashSet<>();
        survived.add("[EA id = 0, generation = 1, steady-state repeat = 0, no. = 5]");
        survived.add("[EA id = 0, generation = 1, steady-state repeat = 0, no. = 6]");
        survived.add("[EA id = 0, generation = 1, steady-state repeat = 0, no. = 7]");
        survived.add("[EA id = 0, generation = 1, steady-state repeat = 0, no. = 8]");
        survived.add("[EA id = 0, generation = 1, steady-state repeat = 0, no. = 9]");
        for (Specimen s : ea.getSpecimensContainer().getPopulation()) System.out.println(s.getID().toString());

        for (Specimen s : ea.getSpecimensContainer().getPopulation())
            assertTrue(survived.contains(s.getID().toString()));

        assignments = GM.getFamilies()[0].getAssignments();
        assertEquals(5, assignments.length);

        expNo = new int[]{1, 0, 1, 0, 1};
        scores = new double[][]{{0.5d}, null, {Math.sqrt(2.0d) / 4.0d}, null, {0.5d}};

        for (int i = 0; i < assignments.length; i++)
        {
            assertEquals(expNo[i], assignments[i].getSpecimens().size());
            for (int j = 0; j < expNo[i]; j++)
            {
                assertEquals(scores[i][j], assignments[i].getEvaluations().get(j), 0.0001d);
            }
        }
    }
}