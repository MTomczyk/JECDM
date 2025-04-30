package y2025.ERS.common.indicators;

import problem.Problem;
import problem.moo.ReferencePointsFactory;
import random.IRandom;

import java.util.HashMap;

/**
 * Provides means for generating Pareto optimal reference points sets.
 *
 * @author MTomczyk
 */
public class ReferencePoints
{

    /**
     * Constructs reference points.
     *
     * @param no no points per each pareto
     * @param R  random number generator
     * @return reference points map (problem to objectives to points)
     */
    public static HashMap<String, HashMap<Integer, double[][]>> getReferencePoints(int no, IRandom R)
    {

        HashMap<String, HashMap<Integer, double[][]>> referencePoints = new HashMap<>(10);
        // for DTLZ1
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ1, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.DTLZ1.toString(), map);
        }
        // for DTLZ2-4
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.DTLZ2.toString(), map);
            referencePoints.put(Problem.DTLZ3.toString(), map);
            referencePoints.put(Problem.DTLZ4.toString(), map);
        }
        // for DTLZ5-6
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ5, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.DTLZ5.toString(), map);
            referencePoints.put(Problem.DTLZ6.toString(), map);
        }
        // for DTLZ7
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ7, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.DTLZ7.toString(), map);
        }
        // for WFG1
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.WFG1, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.WFG1.toString(), map);
            referencePoints.put(Problem.WFG1ALPHA02.toString(), map);
            referencePoints.put(Problem.WFG1ALPHA025.toString(), map);
            referencePoints.put(Problem.WFG1ALPHA05.toString(), map);
        }
        // for WFG2
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.WFG2, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.WFG2.toString(), map);
        }
        // for WFG3
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.WFG3, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.WFG3.toString(), map);
        }
        // for WFG4-9
        {
            HashMap<Integer, double[][]> map = new HashMap<>(5);
            for (int m = 2; m < 6; m++)
            {
                double[][] points = ReferencePointsFactory.getRandomReferencePoints(Problem.WFG4, no, m, R);
                map.put(m, points);
            }
            referencePoints.put(Problem.WFG4.toString(), map);
            referencePoints.put(Problem.WFG5.toString(), map);
            referencePoints.put(Problem.WFG6.toString(), map);
            referencePoints.put(Problem.WFG7.toString(), map);
            referencePoints.put(Problem.WFG8.toString(), map);
            referencePoints.put(Problem.WFG9.toString(), map);
        }
        return referencePoints;
    }
}
