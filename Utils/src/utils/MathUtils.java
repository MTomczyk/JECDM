package utils;

/**
 * Provides simple mathematical functions.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class MathUtils
{
    /**
     * Calculates the sum of integer elements in an input array.
     *
     * @param a double array
     * @return sum of double (0 if the input array is null or empty).
     */
    public static double getSum(double[] a)
    {
        double s = 0;
        if (a == null) return 0.0d;
        for (double i : a) s += i;
        return s;
    }

    /**
     * Calculates the sum of integer elements in an input array that are indexed as [fromIndex, toIndex].
     * The method returns zero if the [fromIndex, toIndex] interval is invalid (toIndex lesser than fromIndex) or does
     * not point to any valid index in the input array. If the input indices point outside the array,
     * they are appropriately adjusted.
     *
     * @param a         double array
     * @param fromIndex starting index (inclusive)
     * @param toIndex   ending index (inclusive)
     * @return sum of double (0 if the input array is null or empty).
     */
    public static double getSum(double[] a, int fromIndex, int toIndex)
    {
        double s = 0;
        if (a == null) return 0.0d;
        if (fromIndex > toIndex) return 0.0d;
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex >= a.length) toIndex = a.length - 1;
        for (int i = fromIndex; i <= toIndex; i++) s += a[i];
        return s;
    }

    /**
     * Calculates the sum of integer elements in an input array.
     *
     * @param a integer array
     * @return sum of integers (0 if the input array is null or empty).
     */
    public static int getSum(int[] a)
    {
        int s = 0;
        if (a == null) return 0;
        for (int i : a) s += i;
        return s;
    }

    /**
     * Calculates the sum of double elements in an input array that are in indexed as [fromIndex, toIndex].
     * The method returns zero if the [fromIndex, toIndex] interval is invalid (toIndex lesser than fromIndex) or does
     * not point to any valid index in the input array. If the input indices point outside the array,
     * they are appropriately adjusted.
     *
     * @param a         double array
     * @param fromIndex starting index (inclusive)
     * @param toIndex   ending index (inclusive)
     * @return sum of double (0 if the input array is null or empty).
     */
    public static int getSum(int[] a, int fromIndex, int toIndex)
    {
        int s = 0;
        if (a == null) return 0;
        if (fromIndex > toIndex) return 0;
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex >= a.length) toIndex = a.length - 1;
        for (int i = fromIndex; i <= toIndex; i++) s += a[i];
        return s;
    }

    /**
     * Calculates the sum of boolean elements in an input array. Assumes that ``true'' is mapped into 1,
     * while ''false'' into 0.
     *
     * @param a boolean array
     * @return sum of integers (0 if the input array is null or empty).
     */
    public static int getSum(boolean[] a)
    {
        int s = 0;
        if (a == null) return 0;
        for (boolean b : a) if (b) s++;
        return s;
    }

    /**
     * Calculates the sum of boolean elements in an input array that are in indexed as [fromIndex, toIndex].
     * The method returns zero if the [fromIndex, toIndex] interval is invalid (toIndex lesser than fromIndex) or does
     * not point to any valid index in the input array. If the input indices point outside the array,
     * they are appropriately adjusted. Assumes that ``true'' is mapped into 1, while ''false'' into 0.
     *
     * @param a         double array
     * @param fromIndex starting index (inclusive)
     * @param toIndex   ending index (inclusive)
     * @return sum of double (0 if the input array is null or empty).
     */
    public static int getSum(boolean[] a, int fromIndex, int toIndex)
    {
        int s = 0;
        if (a == null) return 0;
        if (fromIndex > toIndex) return 0;
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex >= a.length) toIndex = a.length - 1;
        for (int i = fromIndex; i <= toIndex; i++) if (a[i]) s++;
        return s;
    }
}
