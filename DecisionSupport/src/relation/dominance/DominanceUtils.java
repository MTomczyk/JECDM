package relation.dominance;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import relation.IBinaryRelation;
import relation.equalevaluations.EqualEvaluationsUtils;

import java.util.ArrayList;

/**
 * Contains different valuable methods for verifying the dominance relation.
 *
 * @author MTomczyk
 */
public class DominanceUtils
{
    /**
     * Identifies non-dominated alternatives.
     *
     * @param alternatives wrapper for alternatives
     * @param criteria     considered criteria
     * @return indices list (contains indices that point to the original input alternatives array; showing which vectors are non-dominated)
     */
    public static int[] pointNonDominatedVectors(AbstractAlternatives<?> alternatives, Criteria criteria)
    {
        int nds = 0;
        boolean[] nonDom = new boolean[alternatives.size()];
        for (int i = 0; i < alternatives.size(); i++)
        {
            boolean pass = true;
            for (int j = 0; j < alternatives.size(); j++)
            {
                if (i == j) continue;
                if (isDominating(alternatives.get(j).getPerformanceVector(), alternatives.get(i).getPerformanceVector(), criteria))
                {
                    pass = false;
                    break;
                }
            }
            if (pass)
            {
                nonDom[i] = true;
                nds++;
            }
        }

        int[] pnt = new int[nds];
        int idx = 0;
        for (int i = 0; i < nonDom.length; i++) if (nonDom[i]) pnt[idx++] = i;
        return pnt;
    }

    /**
     * Identifies and returns non-dominated alternatives (creates new array).
     *
     * @param alternatives wrapper for alternatives
     * @param criteria     considered criteria
     * @return non-dominated vectors (stored in a matrix; each row is a non-dominated performance vector)
     */
    public static ArrayList<Alternative> getNonDominatedAlternatives(AbstractAlternatives<?> alternatives, Criteria criteria)
    {
        int[] pnt = pointNonDominatedVectors(alternatives, criteria);

        ArrayList<Alternative> output = new ArrayList<>(pnt.length);
        for (int j : pnt) output.add(alternatives.get(j));
        return output;
    }

    /**
     * Identifies non-dominated vectors (stored in a matrix)
     *
     * @param input    input matrix (each row is one performance vector)
     * @param criteria considered criteria
     * @return indices list (contains indices that point to the original input matrix; showing which vectors are non-dominated)
     */
    public static int[] pointNonDominatedVectors(double[][] input, Criteria criteria)
    {
        int nds = 0;
        boolean[] nonDom = new boolean[input.length];
        for (int i = 0; i < input.length; i++)
        {
            boolean pass = true;
            for (int j = 0; j < input.length; j++)
            {
                if (i == j) continue;
                if (isDominating(input[j], input[i], criteria))
                {
                    pass = false;
                    break;
                }
            }
            if (pass)
            {
                nonDom[i] = true;
                nds++;
            }
        }

        int[] pnt = new int[nds];
        int idx = 0;
        for (int i = 0; i < nonDom.length; i++) if (nonDom[i]) pnt[idx++] = i;
        return pnt;
    }

    /**
     * Identifies and returns non-dominated vectors (stored in a matrix).
     *
     * @param input    input matrix (each row is one performance vector)
     * @param criteria considered criteria
     * @return non-dominated vectors (stored in a matrix; each row is a non-dominated performance vector)
     */
    public static double[][] getNonDominatedVectors(double[][] input, Criteria criteria)
    {
        int[] pnt = pointNonDominatedVectors(input, criteria);

        double[][] output = new double[pnt.length][criteria._no];
        for (int i = 0; i < pnt.length; i++)
            System.arraycopy(input[pnt[i]], 0, output[i], 0, criteria._no);
        return output;
    }

    /**
     * Checks if A dominates B (strongly).
     *
     * @param A        the first performance vector
     * @param B        the second performance vector
     * @param criteria considered criteria
     * @return true: A dominates B or equals B; false otherwise (equal, mutually non-dominated, B dominates A)
     */
    public static boolean isDominating(double[] A, double[] B, Criteria criteria)
    {
        return (isGoodAtLeastAs(A, B, criteria)) && (!EqualEvaluationsUtils.isEqualTo(A, B, criteria._no));
    }

    /**
     * Checks if A dominates B or equals B.
     *
     * @param A        the first performance vector
     * @param B        the second alternative
     * @param criteria considered criteria
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isGoodAtLeastAs(double[] A, double[] B, Criteria criteria)
    {
        for (int i = 0; i < criteria._no; i++)
        {
            if (criteria._c[i].isGain())
            {
                if (Double.compare(B[i], A[i]) > 0) return false;
            }
            else
            {
                if (Double.compare(B[i], A[i]) < 0) return false;
            }
        }
        return true;
    }

    /**
     * Creates the dominance matrix.
     *
     * @param alternatives wrapper for the array of alternatives
     * @param criteria     considered criteria
     * @return boolean matrix, where matrix[i][j] = true indicates that i-th alternative (strong) dominates j-th;
     * matrix[i][j] = false otherwise.
     */
    public static boolean[][] createDominanceMatrix(double[][] alternatives, Criteria criteria)
    {
        boolean[][] domination = new boolean[alternatives.length][alternatives.length];

        for (int i = 0; i < alternatives.length; i++)
        {
            for (int j = 0; j < alternatives.length; j++)
            {
                if (i == j) continue;
                if (DominanceUtils.isDominating(alternatives[i], alternatives[j], criteria)) domination[i][j] = true;
            }
        }
        return domination;
    }


    /**
     * Checks if A dominates B (strongly).
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria considered criteria
     * @param epsilon  epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be considered "better")
     * @return true: A dominates B or equals B; false otherwise (equal, mutually non-dominated, B dominates A)
     */
    public static boolean isDominating(Alternative A, Alternative B, Criteria criteria, double epsilon)
    {
        return (isGoodAtLeastAs(A, B, criteria, epsilon)) && (!EqualEvaluationsUtils.isEqualTo(A, B, criteria._no, epsilon));
    }

    /**
     * Checks if A dominates B (strongly) or equals B.
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria considered criteria
     * @param epsilon  epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be considered "better")
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isGoodAtLeastAs(Alternative A, Alternative B, Criteria criteria, double epsilon)
    {
        return isGoodAtLeastAs(A, B, criteria, null, epsilon);
    }

    /**
     * Checks if A dominates B or equals B.
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria considered criteria
     * @param mask     optional (can be null) array for ignoring selected criteria (flag = true -> ignore the criterion)
     * @param epsilon  epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be considered "better")
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isGoodAtLeastAs(Alternative A, Alternative B, Criteria criteria, boolean[] mask, double epsilon)
    {
        for (int i = 0; i < criteria._no; i++)
        {
            if ((mask != null) && (!mask[i])) continue;

            if (criteria._c[i].isGain())
            {
                if (Double.compare(B.getPerformanceAt(i), A.getPerformanceAt(i) + epsilon) > 0) return false;
            }
            else
            {
                if (Double.compare(B.getPerformanceAt(i) + epsilon, A.getPerformanceAt(i)) < 0) return false;
            }
        }
        return true;
    }


    /**
     * Creates the dominance matrix.
     *
     * @param alternatives alternatives
     * @param criteria     considered criteria
     * @param epsilon      epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be
     *                     considered "better"; as long as the difference in performance is smaller/equal than the epsilon,
     *                     the performances are considered equal)
     * @return boolean matrix, where matrix[i][j] = true indicates that i-th alternative strongly dominates j-th;
     * matrix[i][j] = false otherwise.
     */
    public static boolean[][] createDominanceMatrix(ArrayList<Alternative> alternatives,
                                                    Criteria criteria,
                                                    double epsilon)
    {
        return createDominanceMatrix(new Alternatives(alternatives), criteria, epsilon);
    }

    /**
     * Creates the dominance matrix.
     *
     * @param alternatives alternatives
     * @param criteria     considered criteria
     * @param dominance    binary relation used for comparing alternatives
     * @param epsilon      epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be
     *                     considered "better"; as long as the difference in performance is smaller/equal than the epsilon,
     *                     the performances are considered equal)
     * @return boolean matrix, where matrix[i][j] = true indicates that i-th alternative strongly dominates j-th;
     * matrix[i][j] = false otherwise.
     */
    public static boolean[][] createDominanceMatrix(ArrayList<Alternative> alternatives,
                                                    Criteria criteria,
                                                    IBinaryRelation dominance,
                                                    double epsilon)
    {
        return createDominanceMatrix(new Alternatives(alternatives), criteria, dominance, epsilon);
    }

    /**
     * Creates the dominance matrix.
     *
     * @param alternatives wrapper for the array of alternatives
     * @param criteria     considered criteria
     * @param epsilon      epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be
     *                     considered "better"; as long as the difference in performance is smaller/equal than the epsilon,
     *                     the performances are considered equal)
     * @return boolean matrix, where matrix[i][j] = true indicates that i-th alternative strongly dominates j-th;
     * matrix[i][j] = false otherwise.
     */
    public static boolean[][] createDominanceMatrix(AbstractAlternatives<?> alternatives,
                                                    Criteria criteria,
                                                    double epsilon)
    {
        return createDominanceMatrix(alternatives, criteria, new Dominance(criteria, epsilon), epsilon);
    }

    /**
     * Creates the dominance matrix.
     *
     * @param alternatives wrapper for the array of alternatives
     * @param criteria     considered criteria
     * @param dominance    binary relation used for comparing alternatives
     * @param epsilon      epsilon-tolerance for comparing doubles (A's performance must be greater/equal than B's - epsilon to be
     *                     considered "better"; as long as the difference in performance is smaller/equal than the epsilon,
     *                     the performances are considered equal)
     * @return boolean matrix, where matrix[i][j] = true indicates that i-th alternative (strong) dominates j-th;
     * matrix[i][j] = false otherwise.
     */
    public static boolean[][] createDominanceMatrix(AbstractAlternatives<?> alternatives,
                                                    Criteria criteria,
                                                    IBinaryRelation dominance,
                                                    double epsilon)
    {
        boolean[][] domination = new boolean[alternatives.size()][alternatives.size()];

        for (int i = 0; i < alternatives.size(); i++)
        {
            for (int j = 0; j < alternatives.size(); j++)
            {
                if (i == j) continue;
                Alternative A = alternatives.get(i);
                Alternative B = alternatives.get(j);
                if (dominance != null) domination[i][j] = dominance.isHolding(A, B);
                else if (DominanceUtils.isDominating(A, B, criteria, epsilon)) domination[i][j] = true;
            }
        }
        return domination;
    }

}
