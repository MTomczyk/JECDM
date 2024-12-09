package emo.utils.decomposition.goal;

import emo.utils.decomposition.goal.definitions.LNorm;
import emo.utils.decomposition.goal.definitions.PBI;
import emo.utils.decomposition.goal.definitions.PointLineProjection;
import space.normalization.INormalization;
import space.simplex.DasDennis;

import java.util.ArrayList;

/**
 * Supportive class for quickly constructing goals arrays.
 *
 * @author MTomczyk
 */

public class GoalsFactory
{
    /**
     * Generator of the family of LNorms whose weight vectors were generated using Das and Dennis's method (no normalizations).
     *
     * @param M     the number of objectives/dimensions
     * @param p     the number of cuts for the Das and Dennis's method
     * @param alpha compensation level alpha for L-norms
     * @return LNorms whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getLNormsDND(int M, int p, double alpha)
    {
        return getLNormsDND(M, p, alpha, null);
    }

    /**
     * Generator of the family of LNorms whose weight vectors were generated using Das and Dennis's method.
     *
     * @param M              the number of objectives/dimensions
     * @param p              the number of cuts for the Das and Dennis's method
     * @param alpha          compensation level alpha for L-norms
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return LNorms whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getLNormsDND(int M, int p, double alpha, INormalization[] normalizations)
    {
        ArrayList<double[]> w = DasDennis.getWeightVectors(M, p);
        IGoal[] G = new IGoal[w.size()];
        for (int i = 0; i < w.size(); i++)
            G[i] = new LNorm(new space.scalarfunction.LNorm(w.get(i), alpha, normalizations));
        return G;
    }

    /**
     * Generator of the family of LNorms whose weight vectors are set as provided (copied).
     *
     * @param w              weight vectors
     * @param alpha          compensation level alpha for L-norms
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return family consisting of LNorms whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getLNorms(double[][] w, double alpha, INormalization[] normalizations)
    {
        IGoal[] G = new IGoal[w.length];
        for (int i = 0; i < w.length; i++)
            G[i] = new LNorm(new space.scalarfunction.LNorm(w[i].clone(), alpha, normalizations));
        return G;
    }


    /**
     * Generator of the family of PBI functions whose reference points were generated using Das and Dennis's method (no normalizations).
     *
     * @param M     the number of objectives/dimensions
     * @param p     the number of cuts for the Das and Dennis's method
     * @param theta theta weight for controlling distribution (1 = convergence and distribution are equal; > 1; distribution is more important)
     * @return PBIs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPBIsDND(int M, int p, double theta)
    {
        return getPBIsDND(M, p, theta, null);
    }


    /**
     * Generator of the family of PBI whose weight vectors were generated using Das and Dennis's method.
     *
     * @param M              the number of objectives/dimensions
     * @param p              the number of cuts for the Das and Dennis's method
     * @param theta          theta weight for controlling distribution (1 = convergence and distribution are equal; > 1; distribution is more important)
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return PBIs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPBIsDND(int M, int p, double theta, INormalization[] normalizations)
    {
        ArrayList<double[]> w = DasDennis.getWeightVectors(M, p);
        IGoal[] G = new IGoal[w.size()];
        for (int i = 0; i < w.size(); i++)
            G[i] = new PBI(new space.scalarfunction.PBI(w.get(i), theta, normalizations));
        return G;
    }

    /**
     * Generator of the family of PBI whose weight vectors were generated using Das and Dennis's method.
     *
     * @param w weight vectors
     * @param theta          theta weight for controlling distribution (1 = convergence and distribution are equal; > 1; distribution is more important)
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return PBIs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPBIsDND(double[][] w, double theta, INormalization[] normalizations)
    {
        IGoal[] G = new IGoal[w.length];
        for (int i = 0; i < w.length; i++)
            G[i] = new PBI(new space.scalarfunction.PBI(w[i], theta, normalizations));
        return G;
    }

    /**
     * Generator of the family of PLP functions whose reference points were generated using Das and Dennis's method (no normalizations).
     *
     * @param M the number of objectives/dimensions
     * @param p the number of cuts for the Das and Dennis's method
     * @return PLPs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPointLineProjectionsDND(int M, int p)
    {
        return getPointLineProjectionsDND(M, p, null);
    }

    /**
     * Generator of the family of PLPs whose reference points were generated using Das and Dennis's method.
     *
     * @param M              the number of objectives/dimensions
     * @param p              the number of cuts for the Das and Dennis's method
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return PLPs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPointLineProjectionsDND(int M, int p, INormalization[] normalizations)
    {
        ArrayList<double[]> rp = DasDennis.getWeightVectors(M, p);
        IGoal[] G = new IGoal[rp.size()];
        for (int i = 0; i < rp.size(); i++)
            G[i] = new PointLineProjection(new space.scalarfunction.PointLineProjection(rp.get(i), normalizations));
        return G;
    }

    /**
     * Generator of the family of PLPs whose reference points were set as provided.
     *
     * @param rps reference points
     * @param normalizations normalizations used to rescale solutions' objective vectors
     * @return PLPs whose weight vectors were generated using Das and Dennis's method.
     */
    public static IGoal[] getPointLineProjectionsDND(double[][] rps, INormalization[] normalizations)
    {
        IGoal[] G = new IGoal[rps.length];
        for (int i = 0; i < rps.length; i++)
            G[i] = new PointLineProjection(new space.scalarfunction.PointLineProjection(rps[i], normalizations));
        return G;
    }
}
