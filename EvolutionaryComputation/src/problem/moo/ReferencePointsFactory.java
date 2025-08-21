package problem.moo;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import population.Specimen;
import population.SpecimenID;
import problem.Problem;
import problem.moo.wfg.WFGBundle;
import problem.moo.wfg.evaluate.WFG1Easy;
import problem.moo.wfg.evaluate.WFG2Easy;
import problem.moo.wfg.evaluate.WFG3Easy;
import problem.moo.wfg.evaluate.WFG4Easy;
import problem.moo.wfg.shapes.Concave;
import problem.moo.wfg.shapes.IShape;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;

import java.util.ArrayList;

/**
 * This class aids in generating reference Pareto optimal solutions.
 *
 * @author MTomczyk
 */


public class ReferencePointsFactory
{
    /**
     * Allows inspecting the constructed reference points constructed.
     *
     * @param args not used
     */
    @SuppressWarnings("ConstantValue")
    public static void main(String[] args)
    {
        int M = 2;
        int n = 100000;
        IRandom R = new MersenneTwister64(0);

        Problem problem = Problem.WFG9;
        AbstractMOOProblemBundle bundle = WFGBundle.getBundle(problem, M, 6, 2);

        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(M, 10, bundle._normalizations);
        // IGoal [] goals = FamilyFactory.getLNorms(0, M, 10, 4.0d, bundle._normalizations).getGoals();
        //double[][] rps = ReferencePointsFactory.getRandomReferencePoints(problem, n, M, R);
        //double[][] rps = ReferencePointsFactory.getFilteredReferencePoints(problem, n, M, R, goals);
        double[][] rps = ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(2.0d, n, M, R);

        IDataSet ds;
        if (M == 2) ds = DataSet.getFor2D("RPs", rps, new MarkerStyle(1.0f, Color.RED, Marker.CIRCLE));
        else if (M == 3)
            ds = DataSet.getFor3D("RPs", rps, new MarkerStyle(0.02f, Color.RED, Marker.SPHERE_LOW_POLY_3D));
        else
            ds = DataSet.getForParallelCoordinatePlot2D("RPs", M, rps, null, new LineStyle(0.5f, Gradient.getViridisGradient(), 0));

        AbstractPlot plot;

        if (M == 2)
        {
            Plot2D.Params pP = new Plot2D.Params();
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(bundle._paretoFrontBounds[0], bundle._paretoFrontBounds[1]);
            pP._xAxisTitle = "f1";
            pP._yAxisTitle = "f2";
            plot = new Plot2D(pP);

        }
        else if (M == 3)
        {
            Plot3D.Params pP = new Plot3D.Params();
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(bundle._paretoFrontBounds[0], bundle._paretoFrontBounds[1],
                    bundle._paretoFrontBounds[2]);
            pP._xAxisTitle = "f1";
            pP._yAxisTitle = "f2";
            pP._zAxisTitle = "f3";
            plot = new Plot3D(pP);
        }
        else
        {
            ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(M);
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(M, bundle._paretoFrontBounds, new boolean[M], new boolean[M]);
            pP._axesTitles = new String[M];
            for (int m = 0; m < M; m++) pP._axesTitles[m] = "f" + (m + 1);
            plot = new ParallelCoordinatePlot2D(pP);
        }

        Frame frame = new Frame(plot, 0.5f);
        plot.getModel().setDataSet(ds, true, false);

        frame.setVisible(true);
    }


    /**
     * This method generates random reference points on a concave and spherical Pareto front. The sphere is located in
     * the center of the coordinate space, and points belonging to its first quarter are sampled only.
     * Important note: This method generates points that are (randomly) uniformly distributed on the sphere.
     *
     * @param n the number of points to sample
     * @param M the number of objectives
     * @param R Random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution); null, if the
     * input is invalid
     */
    public static double[][] getUniformRandomRPsOnConcaveSphere(int n, int M, IRandom R)
    {
        return getUniformRandomRPsOnConcaveSphere(1.0d, n, M, R);
    }

    /**
     * This method generates random reference points on a concave and spherical Pareto front. The sphere is located in
     * the center of the coordinate space, and points belonging to its first quarter are sampled only.
     * Important note: This method generates points that are (randomly) uniformly distributed on the sphere.
     *
     * @param r sphere radius
     * @param n the number of points to sample
     * @param M the number of objectives
     * @param R Random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution); null, if the
     * input is invalid
     */
    public static double[][] getUniformRandomRPsOnConcaveSphere(double r, int n, int M, IRandom R)
    {
        if (R == null) return null;
        if (n < 1) return null;
        if (M < 2) return null;
        if (Double.compare(r, 0.0) <= 0) return null;

        IShape[] shapes = new IShape[M];
        for (int m = 0; m < M; m++) shapes[m] = new Concave(m, M);
        double div = Math.PI / 2.0d;

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] x = new double[M - 1];
            x[M - 2] = R.nextDouble();
            for (int j = 0; j < M - 2; j++)
            {
                double ct = R.nextDouble();
                x[j] = Math.acos(ct);
                x[j] /= (div);
            }
            for (int j = 0; j < M; j++) p[i][j] = r * shapes[j].getShape(x);
        }
        return p;
    }

    /**
     * This method generates random reference points on a convex and spherical Pareto front. The sphere is located in
     * the [radius,...,radius]-vector of the coordinate space, and points belonging to its first quarter are sampled
     * only.
     * Important note: This method generates points that are (randomly) uniformly distributed on the sphere.
     *
     * @param n the number of points to sample
     * @param M the number of objectives
     * @param R Random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution); null, if the
     * input is invalid
     */
    public static double[][] getUniformRandomRPsOnConvexSphere(int n, int M, IRandom R)
    {
        return getUniformRandomRPsOnConvexSphere(1.0d, n, M, R);
    }

    /**
     * This method generates random reference points on a convex and spherical Pareto front. The sphere is located in
     * the [radius,...,radius]-vector of the coordinate space, and points belonging to its first quarter are sampled
     * only.
     * Important note: This method generates points that are (randomly) uniformly distributed on the sphere.
     *
     * @param r sphere radius
     * @param n the number of points to sample
     * @param M the number of objectives
     * @param R Random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution); null, if the
     * input is invalid
     */
    public static double[][] getUniformRandomRPsOnConvexSphere(double r, int n, int M, IRandom R)
    {
        double[][] p = getUniformRandomRPsOnConcaveSphere(r, n, M, R);
        if (p == null) return null;
        for (double[] v : p)
            for (int j = 0; j < v.length; j++) v[j] = r - v[j];
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * The points are drawn randomly, by generating uniformly distributed decision vectors (which may not lead to a
     * uniform
     * distribution in the objective space).
     *
     * @param problem problem ID
     * @param n       number of solutions to generate
     * @param M       problem dimensionality
     * @param R       random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution); null if the
     * method does not support a requested problem
     */
    public static double[][] getRandomReferencePoints(Problem problem, int n, int M, IRandom R)
    {
        switch (problem)
        {
            case DTLZ1 ->
            {
                return getForDTLZ1(n, M, R);
            }
            case DTLZ2, DTLZ3, DTLZ4 ->
            {
                return getForDTLZ2_4(n, M, R);
            }
            case DTLZ5, DTLZ6 ->
            {
                return getForDTLZ5_6(n, M, R);
            }
            case DTLZ7 ->
            {
                return getForDTLZ7(n, M, R);
            }
            case WFG1, WFG1EASY, WFG1ALPHA02, WFG1ALPHA025, WFG1ALPHA05 ->
            {
                return getForWFG1(n, M, R);
            }
            case WFG2, WFG2EASY ->
            {
                return getForWFG2(n, M, R);
            }
            case WFG3, WFG3EASY ->
            {
                return getForWFG3(n, M, R);
            }
            case WFG4, WFG4EASY, WFG5, WFG5EASY, WFG6, WFG6EASY, WFG7, WFG7EASY, WFG8, WFG8EASY, WFG9, WFG9EASY ->
            {
                return getForWFG4_9(n, M, R);
            }
        }

        return null;
    }


    /**
     * This method constructs the desired number of Pareto optimal solutions. The method requires providing a series of
     * scalar optimization goals (wrapped by {@link emo.utils.decomposition.goal.IGoal}) used to control distribution.
     * It first constructs n points randomly. Then, per each goal, it identifies the best performer. Thus, the size of
     * the returned set equals the number of goals. Note that the quality of the filtering highly depends on the type
     * of goals/scalar functions used. For instance, solutions located in concave areas will not be found if the
     * functions are in the form of weighted sums. Note that the method does not filter out the duplicates. Note that
     * also the goals should have properly set normalization functions.
     *
     * @param problem problem ID
     * @param n       number of random solutions to generate (input for filtering)
     * @param M       problem dimensionality
     * @param R       random number generator
     * @param goals   input goals
     * @return solutions represented as 'number of goals' x M matrix (each row corresponds to M-dimensional solution);
     * null if the method does not support a requested problem
     */
    public static double[][] getFilteredReferencePoints(Problem problem, int n, int M, IRandom R, IGoal[] goals)
    {
        double[][] rS = getRandomReferencePoints(problem, n, M, R);
        if (rS == null) return null;
        return getFilteredReferencePoints(rS, M, goals);
    }

    /**
     * This method constructs the desired number of Pareto optimal solutions. The method requires providing a series of
     * scalar optimization goals (wrapped by {@link emo.utils.decomposition.goal.IGoal}) used to control distribution.
     * It accepts as an input a series of points. Then, per each goal, it identifies the best performer among these
     * points. Thus, the size of the returned set equals the number of goals. Note that the quality of the filtering
     * highly depends on the type of goals/scalar functions used. For instance, solutions located in concave areas will
     * not be found if the functions are in the form of weighted sums. Note that the method does not filter out the
     * duplicates. Note that also the goals should have properly set normalization functions.
     *
     * @param points input random points (source for filtering)
     * @param M      problem dimensionality
     * @param goals  input goals
     * @return solutions represented as 'number of goals' x M matrix (each row corresponds to M-dimensional solution);
     * null if the method does not support a requested problem
     */
    public static double[][] getFilteredReferencePoints(double[][] points, int M, IGoal[] goals)
    {
        double[][] r = new double[goals.length][M];

        // for each goal
        for (int g = 0; g < goals.length; g++)
        {
            // find the best performer
            double bestEval = Double.NEGATIVE_INFINITY;
            if (goals[g].isLessPreferred()) bestEval = Double.POSITIVE_INFINITY;
            int bestIndex = 0;

            for (int s = 0; s < points.length; s++)
            {
                double e = goals[g].evaluate(new Specimen(new SpecimenID(0), points[s]));

                if (((goals[g].isLessPreferred()) && (Double.compare(e, bestEval) < 0)) ||
                                ((!goals[g].isLessPreferred()) && (Double.compare(e, bestEval) > 0)))
                {
                    bestEval = e;
                    bestIndex = s;
                }
            }

            r[g] = points[bestIndex].clone();
        }
        return r;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the DTLZ1 problem.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForDTLZ1(int n, int M, IRandom R)
    {
        double[][] p = new double[n][];
        for (int i = 0; i < n; i++)
        {
            p[i] = WeightsGenerator.getNormalizedWeightVector(M, R);
            for (int j = 0; j < M; j++) p[i][j] *= 0.5d;
        }
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the DTLZ2, DTLZ3, and DTLZ4 problems.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForDTLZ2_4(int n, int M, IRandom R)
    {
        IShape[] shapes = new IShape[M];
        for (int m = 0; m < M; m++) shapes[m] = new Concave(m, M);

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            for (int j = 0; j < M - 1; j++) a[j] = R.nextDouble();
            for (int j = 0; j < M; j++) p[i][j] = shapes[j].getShape(a);
        }
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the DTLZ5 and DTLZ6 problems.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForDTLZ5_6(int n, int M, IRandom R)
    {
        IShape[] shapes = new IShape[M];
        for (int m = 0; m < M; m++) shapes[m] = new Concave(m, M);
        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            a[0] = R.nextDouble();
            for (int j = 1; j < M - 1; j++) a[j] = 0.5d;
            for (int j = 0; j < M; j++) p[i][j] = shapes[j].getShape(a);
        }
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the DTLZ7 problem.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForDTLZ7(int n, int M, IRandom R)
    {
        double firstZero = 0.251411836088917;
        double intermediate = 0.631626530700061;
        double secondZero = 0.859400856644724;
        double d = secondZero - intermediate;
        double sum = firstZero + d;
        double p1 = firstZero / sum;

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < M - 1; j++)
            {
                if (Double.compare(R.nextDouble(), p1) <= 0) p[i][j] = R.nextDouble() * firstZero;
                else p[i][j] = intermediate + R.nextDouble() * d;
            }
            double h = M;
            for (int j = 0; j < M - 1; j++) h -= (p[i][j] / 2.0d) * (1.0d + Math.sin(3.0d * Math.PI * p[i][j]));
            p[i][M - 1] = 2.0d * h;
        }
        return p;
    }


    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the WFG1 problem.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    @SuppressWarnings("DuplicatedCode")
    private static double[][] getForWFG1(int n, int M, IRandom R)
    {
        WFG1Easy wfg1 = new WFG1Easy(M);
        ArrayList<IShape> shapes = wfg1.getShapes();

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            for (int j = 0; j < M - 1; j++) a[j] = R.nextDouble();
            for (int j = 0; j < M; j++) p[i][j] = shapes.get(j).getShape(a) * (2 * (j + 1));
        }
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the WFG2 problem.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForWFG2(int n, int M, IRandom R)
    {
        WFG2Easy wfg2 = new WFG2Easy(M);
        ArrayList<IShape> shapes = wfg2.getShapes();

        double e0 = 0.0d;
        double m1 = 0.04158853544223386;
        double e1 = 0.129692467222130;
        double m2 = 0.20959496563121088;
        double e2 = 0.354934714585981;
        double m3 = 0.4049933325885807;
        double e3 = 0.564050091147755;
        double m4 = 0.6033554792401994;
        double e4 = 0.769098470661788;
        double m5 = 0.8025237409357284;
        double e5 = 0.972437273874140;
        double m6 = 1.0d;

        double d1 = m1 - e0;
        double d2 = m2 - e1;
        double d3 = m3 - e2;
        double d4 = m4 - e3;
        double d5 = m5 - e4;
        double d6 = m6 - e5;

        double sum = d1 + d2 + d3 + d4 + d5 + d6;
        double p1 = d1 / sum;
        double p2 = d2 / sum;
        double p3 = d3 / sum;
        double p4 = d4 / sum;
        double p5 = d5 / sum;
        double p6 = d6 / sum;

        int[] choice = new int[]{0, 1, 2, 3, 4, 5};
        double[] prob = new double[]{p1, p2, p3, p4, p5, p6};
        double[] base = new double[]{e0, e1, e2, e3, e4, e5};
        double[] span = new double[]{d1, d2, d3, d4, d5, d6};

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            int c = R.getIntWithProbability(choice, prob);
            a[0] = base[c] + span[c] * R.nextDouble();
            for (int j = 1; j < M - 1; j++) a[j] = R.nextDouble();
            for (int j = 0; j < M; j++) p[i][j] = shapes.get(j).getShape(a) * (2 * (j + 1));
        }
        return p;
    }


    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the WFG3 problem.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    private static double[][] getForWFG3(int n, int M, IRandom R)
    {
        WFG3Easy wfg3 = new WFG3Easy(M);
        ArrayList<IShape> shapes = wfg3.getShapes();

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            a[0] = R.nextDouble();
            for (int j = 1; j < M - 1; j++) a[j] = 0.5d;
            for (int j = 0; j < M; j++) p[i][j] = shapes.get(j).getShape(a) * (2 * (j + 1));
        }
        return p;
    }

    /**
     * This method constructs the desired number of random Pareto optimal solutions (reference points in the objective
     * space)
     * to the WFG4-9 problems.
     *
     * @param n number of solutions to generate
     * @param M problem dimensionality
     * @param R random number generator
     * @return solutions represented as n x M matrix (each row corresponds to M-dimensional solution)
     */
    @SuppressWarnings("DuplicatedCode")
    private static double[][] getForWFG4_9(int n, int M, IRandom R)
    {
        WFG4Easy wfg4 = new WFG4Easy(M);
        ArrayList<IShape> shapes = wfg4.getShapes();

        double[][] p = new double[n][M];
        for (int i = 0; i < n; i++)
        {
            double[] a = new double[M - 1];
            for (int j = 0; j < M - 1; j++) a[j] = R.nextDouble();
            for (int j = 0; j < M; j++) p[i][j] = shapes.get(j).getShape(a) * (2 * (j + 1));
        }
        return p;
    }
}
