package utils;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Provides auxiliary array-related functionalities.
 *
 * @author MTomczyk
 */
public class ArrayUtils
{
    /**
     * Auxiliary method for creating an array filled with a fixed value (the same for each array element).
     *
     * @param n        array size
     * @param constant constant value
     * @param <T>      array element type
     * @return constructed array (null, if array size is less than 0)
     */
    public static <T> T[] getArray(int n, T constant)
    {
        if (n < 0) return null;
        @SuppressWarnings("unchecked")
        T[] a = (T[]) Array.newInstance(constant.getClass(), n);
        for (int i = 0; i < n; i++) a[i] = constant;
        return a;
    }

    /**
     * Auxiliary interface for classes responsible for constructing array values.
     *
     * @param <T> value type
     */
    public interface IValueConstructor<T>
    {
        /**
         * The main method. Constructs array element associated with i-th index.
         *
         * @param i i-th index
         * @return array element
         */

        T getValue(int i);
    }

    /**
     * Auxiliary method for creating an array filled with values specified via {@link IValueConstructor}.
     *
     * @param n                array size
     * @param valueConstructor value constructor
     * @param type             array element class
     * @param <T>              array element type
     * @return constructed array (null, if array size is less than 0 or the value constructor is not provided)
     */
    public static <T> T[] getArray(int n, Class<T> type, IValueConstructor<T> valueConstructor)
    {
        if (n < 0) return null;
        if (valueConstructor == null) return null;
        @SuppressWarnings("unchecked")
        T[] a = (T[]) Array.newInstance(type, n);
        for (int i = 0; i < n; i++) a[i] = valueConstructor.getValue(i);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with a fixed integer (the same for each array element).
     *
     * @param n        array size
     * @param constant constant value
     * @return constructed array (null, if array size is less than 0)
     */
    public static int[] getIntArray(int n, int constant)
    {
        if (n < 0) return null;
        int[] a = new int[n];
        Arrays.fill(a, constant);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with integer values specified via {@link IValueConstructor}.
     *
     * @param n                array size
     * @param valueConstructor value constructor
     * @return constructed array (null, if array size is less than 0 or the value constructor is not provided)
     */
    public static int[] getIntArray(int n, IValueConstructor<Integer> valueConstructor)
    {
        if (n < 0) return null;
        if (valueConstructor == null) return null;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = valueConstructor.getValue(i);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with a fixed float (the same for each array element).
     *
     * @param n        array size
     * @param constant constant value
     * @return constructed array (null, if array size is less than 0)
     */
    public static float[] getFloatArray(int n, float constant)
    {
        if (n < 0) return null;
        float[] a = new float[n];
        Arrays.fill(a, constant);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with float values specified via {@link IValueConstructor}.
     *
     * @param n                array size
     * @param valueConstructor value constructor
     * @return constructed array (null, if array size is less than 0 or the value constructor is not provided)
     */
    public static float[] getFloatArray(int n, IValueConstructor<Float> valueConstructor)
    {
        if (n < 0) return null;
        if (valueConstructor == null) return null;
        float[] a = new float[n];
        for (int i = 0; i < n; i++) a[i] = valueConstructor.getValue(i);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with a fixed double (the same for each array element).
     *
     * @param n        array size
     * @param constant constant value
     * @return constructed array (null, if array size is less than 0)
     */
    public static double[] getDoubleArray(int n, double constant)
    {
        if (n < 0) return null;
        double[] a = new double[n];
        Arrays.fill(a, constant);
        return a;
    }

    /**
     * Auxiliary method for creating an array filled with double values specified via {@link IValueConstructor}.
     *
     * @param n                array size
     * @param valueConstructor value constructor
     * @return constructed array (null, if array size is less than 0 or the value constructor is not provided)
     */
    public static double[] getDoubleArray(int n, IValueConstructor<Double> valueConstructor)
    {
        if (n < 0) return null;
        if (valueConstructor == null) return null;
        double[] a = new double[n];
        for (int i = 0; i < n; i++) a[i] = valueConstructor.getValue(i);
        return a;
    }
}
