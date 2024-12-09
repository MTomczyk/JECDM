package relation.equalevaluations;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import space.Vector;

import java.util.ArrayList;

/**
 * Contains different valuable methods for verifying the alternatives' equality (given their evaluations).
 *
 * @author MTomczyk
 */
public class EqualEvaluationsUtils
{
    /**
     * Auxiliary method that identifies unique vectors.
     *
     * @param alternatives wrapper for alternatives
     * @param tolerance    tolerance used when comparing doubles
     * @return indices list (contains indices that point to the original input matrix; showing which vectors are unique)
     */
    public static int[] pointUnique(AbstractAlternatives<?> alternatives, double tolerance)
    {
        boolean[] passed = new boolean[alternatives.size()];
        boolean[] checked = new boolean[alternatives.size()];

        EqualEvaluations EA = new EqualEvaluations(tolerance);

        int survived = 0;
        for (int i = 0; i < alternatives.size(); i++)
        {
            if ((checked[i]) && (!passed[i])) continue;
            else
            {
                passed[i] = true;
                survived++;
            }

            checked[i] = true;

            for (int j = i + 1; j < alternatives.size(); j++)
            {
                if ((checked[i]) && (!passed[i])) continue;

                if (EA.isHolding(alternatives.get(i), alternatives.get(j)))
                {
                    checked[j] = true;
                    passed[j] = false;
                }
            }
        }

        int[] indices = new int[survived];
        int idx = 0;
        for (int i = 0; i < alternatives.size(); i++)
            if (passed[i]) indices[idx++] = i;

        return indices;
    }

    /**
     * Auxiliary method that filters out duplicates.
     *
     * @param alternatives wrapper for alternatives
     * @param tolerance    tolerance used when comparing doubles
     * @return the input alternatives, but with filtered out duplicates (new array is instantiated)
     */
    public static AbstractAlternatives<?> removeDuplicates(AbstractAlternatives<?> alternatives, double tolerance)
    {
        int[] indices = pointUnique(alternatives, tolerance);
        ArrayList<Alternative> unique = new ArrayList<>(indices.length);
        for (int index : indices) unique.add(alternatives.get(index));
        return new Alternatives(unique);
    }


    /**
     * Auxiliary method that identifies unique vectors.
     *
     * @param alternatives alternatives matrix (alternatives X performances (criteria))
     * @param tolerance    tolerance used when comparing doubles
     * @return indices list (contains indices that point to the original input matrix; showing which vectors are unique)
     */
    public static int[] pointUnique(double[][] alternatives, double tolerance)
    {
        boolean[] passed = new boolean[alternatives.length];
        boolean[] checked = new boolean[alternatives.length];

        int survived = 0;
        for (int i = 0; i < alternatives.length; i++)
        {
            if ((checked[i]) && (!passed[i])) continue;
            else
            {
                passed[i] = true;
                survived++;
            }

            checked[i] = true;

            for (int j = i + 1; j < alternatives.length; j++)
            {
                if ((checked[i]) && (!passed[i])) continue;

                if (Vector.areVectorsEqual(alternatives[i], alternatives[j], tolerance))
                {
                    checked[j] = true;
                    passed[j] = false;
                }
            }
        }

        int[] indices = new int[survived];
        int idx = 0;
        for (int i = 0; i < alternatives.length; i++)
            if (passed[i]) indices[idx++] = i;

        return indices;
    }

    /**
     * Auxiliary method that filters out duplicates.
     *
     * @param alternatives alternatives matrix (alternatives X performances (criteria))
     * @param tolerance    tolerance used when comparing doubles
     * @return the input matrix, but with filtered out duplicates (new array is instantiated)
     */
    public static double[][] removeDuplicates(double[][] alternatives, double tolerance)
    {
        int[] indices = pointUnique(alternatives, tolerance);
        double[][] unique = new double[indices.length][];
        for (int i = 0; i < indices.length; i++)
            unique[i] = alternatives[indices[i]].clone();
        return unique;
    }

    /**
     * Checks if A equals B.
     *
     * @param A        the first performance vector
     * @param B        the second performance vector
     * @param criteria the number of criteria (dictates the number of first performance stored in the vector that will be compared)
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isEqualTo(double[] A,
                                    double[] B,
                                    int criteria)
    {
        for (int i = 0; i < criteria; i++) if (Double.compare(A[i], B[i]) != 0) return false;
        return true;
    }

    /**
     * Checks if A equals B.
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria the number of criteria (dictates the number of first performance stored in the vector that will be compared)
     * @return true, if both alternatives have equal performance vectors; false otherwise
     */
    public static boolean isEqualTo(Alternative A, Alternative B, int criteria)
    {
        return isEqualTo(A, B, criteria, null, 0.0d);
    }

    /**
     * Checks if A equals B.
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria the number of criteria (dictates the number of first performance stored in the vector that will be compared)
     * @param epsilon  epsilon-tolerance for comparing doubles (as long as the difference in performance is smaller/equal
     *                 than the epsilon, the performances are considered equal)
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isEqualTo(Alternative A, Alternative B, int criteria, double epsilon)
    {
        return isEqualTo(A, B, criteria, null, epsilon);
    }

    /**
     * Checks if A equals B.
     *
     * @param A        the first alternative
     * @param B        the second alternative
     * @param criteria the number of criteria (dictates the number of first performance stored in the vector that will be compared)
     * @param mask     optional (can be null) array for ignoring selected criteria (flag = true -> ignore the criterion)
     * @param epsilon  epsilon-tolerance for comparing doubles (as long as the difference in performance is smaller/equal
     *                 than the epsilon, the performances are considered equal)
     * @return true: A dominates B or equals B; false otherwise (mutually non-dominated, B dominates A)
     */
    public static boolean isEqualTo(Alternative A,
                                    Alternative B,
                                    int criteria,
                                    boolean[] mask,
                                    double epsilon)
    {
        for (int i = 0; i < criteria; i++)
        {
            if ((mask != null) && (!mask[i])) continue;
            if (((Double.compare(A.getPerformanceAt(i), B.getPerformanceAt(i) - epsilon) < 0))
                    || ((Double.compare(A.getPerformanceAt(i), B.getPerformanceAt(i) + epsilon) > 0)))
                return false;
        }

        return true;
    }
}
