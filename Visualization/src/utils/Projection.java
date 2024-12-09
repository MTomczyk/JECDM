package utils;

/**
 * Auxiliary class providing different projection-related operations.
 *
 * @author MTomczyk
 */

public class Projection
{
    /**
     * Additional offset used when casting floats into integers.
     */
    private static final float _fA = 0.5f;

    /**
     * Casts float to int. Note: fA offset is added. If fA = 0.5, it effectively rounds float to the nearest integer.
     *
     * @param v input float
     * @return integer value
     */
    public static int getP(float v)
    {
        return (int) (v + _fA);
    }

    /**
     * Casts float by float product (v1 * v2) to int. Note: fA offset is added. If fA = 0.5, it effectively rounds float to the nearest integer.
     *
     * @param v1 input float
     * @param v2 input float
     * @return integer value
     */
    public static int getP_mul(float v1, float v2)
    {
        return (int) (v1 * v2 + _fA);
    }

    /**
     * Casts the sum of two floats (v1 + v2) to int. Note: fA offset is added. If fA = 0.5, it effectively rounds float to the nearest integer.
     *
     * @param v1 input float
     * @param v2 input float
     * @return integer value
     */
    public static int getP_sum(float v1, float v2)
    {
        return (int) (v1 + v2 + _fA);
    }

    /**
     * Casts the subtraction of two floats (v1 - v2) to int. Note: fA offset is added. If fA = 0.5, it effectively rounds float to the nearest integer.
     *
     * @param v1 input float
     * @param v2 input float
     * @return integer value
     */
    public static int getP_sub(float v1, float v2)
    {
        return (int) (v1 - v2 + _fA);
    }
}
