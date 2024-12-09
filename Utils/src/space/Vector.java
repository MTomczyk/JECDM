package space;

import sort.InsertionSortDouble;

import java.util.Arrays;

/**
 * A set of auxiliary functions for processing vectors.
 *
 * @author MTomczyk
 */

public class Vector
{
    /**
     * Creates and returns a vector of length n whose components are set to the same provided value.
     * @param n array length
     * @param value value to be set
     * @return created vector
     */
    public static double [] getVectorWithEqualComponents(int n, double value)
    {
        double [] v = new double[n];
        Arrays.fill(v, value);
        return v;
    }

    /**
     * Calculates the expected number a line segment should be divided so that each resulting segment is approximately
     * equal to the provided expected length. It is assumed that input data points are normalized.
     *
     * @param array       float array containing line data points
     * @param offset1     index pointing to the first line point
     * @param offset2     index pointing to the second line point
     * @param stride      determines the number of attributes per one line data point (e.g., dimensionality, stride)
     * @param expectedLen expected one line segment length.
     * @return expected no. times a line segment should be divided (0 = no divisions; 1 -> divide one time, i.e., produce two segments from one, etc.)
     */
    public static int getLineSegmentDivisions(float[] array, int offset1, int offset2, int stride, float expectedLen)
    {
        double len = 0.0f;
        for (int i = 0; i < stride; i++)
            len += Math.pow(array[offset2 + i] - array[offset1 + i], 2.0f);
        len = Math.sqrt(len);
        if (Double.compare(len, expectedLen) <= 0) return 0;
        else return (int) (Math.floor(len / expectedLen) + 0.5f);
    }

    /**
     * Calculates the expected number a line segment should be divided so that each resulting segment is approximately
     * equal to the provided expected length. It is assumed that input data points are normalized.
     *
     * @param pLinePoint  previous line point
     * @param nLinePoint  next line point
     * @param expectedLen expected one line segment length.
     * @return expected no. times a line segment should be divided (0 = no divisions; 1 -> divide one time, i.e., produce two segments from one, etc.)
     */
    public static int getLineSegmentDivisions(double[] pLinePoint, double[] nLinePoint, double expectedLen)
    {
        double len = 0.0f;
        for (int i = 0; i < pLinePoint.length; i++)
            len += Math.pow(nLinePoint[i] - pLinePoint[i], 2.0d);
        len = Math.sqrt(len);
        if (Double.compare(len, expectedLen) <= 0) return 0;
        else return (int) (Math.floor(len / expectedLen) + 0.5f);
    }

    /**
     * Compares if two double vectors are equal.
     *
     * @param a         the first vector
     * @param b         the second vector
     * @param precision precision (|a - b| should be &lt; precision) to neglect equality
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(double[] a, double[] b, double precision)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
            if (Double.compare(Math.abs(a[i] - b[i]), precision) > 0) return false;
        return true;
    }

    /**
     * Compares if two double vectors are equal.
     *
     * @param a the first vector
     * @param b the second vector
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(double[] a, double[] b)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
            if (Double.compare(a[i], b[i]) != 0) return false;
        return true;
    }

    /**
     * Compares if two double vectors are equal.
     *
     * @param a the first vector
     * @param b the second vector
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(float[] a, float[] b)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
            if (Float.compare(a[i], b[i]) != 0) return false;
        return true;
    }


    /**
     * Compares if two integer vectors are equal.
     *
     * @param a the first vector
     * @param b the second vector
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(int[] a, int[] b)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;
        return true;
    }

    /**
     * Compares if two boolean vectors are equal.
     *
     * @param a the first vector
     * @param b the second vector
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(boolean[] a, boolean[] b)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;
        return true;
    }

    /**
     * Compares if two double vectors are equal.
     *
     * @param a         the first vector
     * @param b         the second vector
     * @param precision precision (|a - b| should be &lt; precision)
     * @return true = vectors are equal; false otherwise
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
    public static boolean areVectorsEqual(float[] a, float[] b, float precision)
    {
        if ((a != null) && (b == null)) return false;
        if ((a == null) && (b != null)) return false;
        if ((a == null)) return true;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
            if (Float.compare(Math.abs(a[i] - b[i]), precision) > 0) return false;
        return true;
    }

    /**
     * Returns direction vector coordinates (not normalized) given the alpha and beta angles (degrees) in the coordinate system.
     *
     * @param alpha alpha angle
     * @param beta  beta angle
     * @return direction vector coordinates (not normalized)
     */
    public static double[] getDirectionVectorUn(double alpha, double beta)
    {
        double aX = Math.toRadians(alpha);
        double aY = Math.toRadians(beta);
        return new double[]{Math.sin(aY) * Math.cos(aX), Math.sin(aX), Math.cos(aY) * Math.cos(aX)};
    }

    /**
     * Returns the transposed 2D matrix.
     *
     * @param m 2D matrix
     * @return transposed 2D matrix
     */
    public static int[][] getTransposed(int[][] m)
    {
        int[][] r = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++) r[j][i] = m[i][j];
        return r;
    }

    /**
     * Calculates the cosine distance between two m-dimensional vectors.
     *
     * @param a m-dimensional vector
     * @param b m-dimensional vector
     * @return cosine distance
     */
    public static double getCosineSimilarity(double[] a, double[] b)
    {
        double d = getDotProduct(a, b);
        double lA = getLength(a);
        double lB = getLength(b);
        if (Double.compare(lA, 0.0d) == 0) return 1.0d;
        if (Double.compare(lB, 0.0d) == 0) return 1.0d;
        return d / (lA * lB);
    }


    /**
     * Calculates the cosine distance between two m-dimensional vectors.
     *
     * @param a  m-dimensional vector
     * @param b  m-dimensional vector
     * @param ao offset in the a-array
     * @param bo offset in the b-array
     * @param s  stride
     * @return cosine distance
     */
    public static double getCosineSimilarity(double[] a, double[] b, int ao, int bo, int s)
    {
        double d = getDotProduct(a, b, ao, bo, s);
        double lA = getLength(a, ao, s);
        double lB = getLength(b, bo, s);
        if (Double.compare(lA, 0.0d) == 0) return 1.0d;
        if (Double.compare(lB, 0.0d) == 0) return 1.0d;
        return d / (lA * lB);
    }

    /**
     * Calculates the cosine distance between two m-dimensional vectors.
     *
     * @param a m-dimensional vector
     * @param b m-dimensional vector
     * @return cosine distance
     */
    public static float getCosineSimilarity(float[] a, float[] b)
    {
        float d = getDotProduct(a, b);
        float lA = getLength(a);
        float lB = getLength(b);
        if (Float.compare(lA, 0.0f) == 0) return 1.0f;
        if (Float.compare(lB, 0.0f) == 0) return 1.0f;
        return d / (lA * lB);
    }

    /**
     * Calculates the cosine distance between two m-dimensional vectors.
     *
     * @param a  m-dimensional vector
     * @param b  m-dimensional vector
     * @param ao offset in the a-array
     * @param bo offset in the b-array
     * @param s  stride
     * @return cosine distance
     */
    public static float getCosineSimilarity(float[] a, float[] b, int ao, int bo, int s)
    {
        float d = getDotProduct(a, b, ao, bo, s);
        float lA = getLength(a, ao, s);
        float lB = getLength(b, bo, s);
        if (Float.compare(lA, 0.0f) == 0) return 1.0f;
        if (Float.compare(lB, 0.0f) == 0) return 1.0f;
        return d / (lA * lB);
    }

    /**
     * Returns a vector (3D) b that satisfies a x b = c.
     * The returned vector is of arbitrary length (usually, there is no unique b that satisfies the above condition).
     * If b cannot be derived (||a|| = 0 or a and c are not orthogonal &lt;- important!), the method returns null.
     *
     * @param a         3d array of doubles
     * @param c         3d array of doubles
     * @param precision precision used when determining orthogonality (dot product of a and c should be smaller than precision)
     * @return the b vector that satisfies a x b = c.
     */
    public static float[] getInverseCrossProduct3D(float[] a, float[] c, float precision)
    {
        float l = Vector.getLength(a);
        if (Float.compare(l, 0.0f) == 0) return null;
        float dot = getDotProduct(a, c);
        if (Float.compare(Math.abs(dot), precision) > 0) return null;
        l = l * l;
        float[] nom = getCrossProduct3D(c, a);
        //double t = 0.0d;
        return new float[]{
                nom[0] / l,// + t * a[0],
                nom[1] / l,// + t * a[1],
                nom[2] / l,// + t * a[2],

        };
    }

    /**
     * Returns a vector (3D) b that satisfies a x b = c.
     * The returned vector is of arbitrary length (usually, there is no unique b that satisfies the above condition).
     * If b cannot be derived (||a|| = 0 or a and c are not orthogonal &lt;- important!), the method returns null.
     *
     * @param a         3d array of doubles
     * @param c         3d array of doubles
     * @param precision precision used when determining orthogonality (dot product of a and c should be smaller than precision)
     * @return the b vector that satisfies a x b = c.
     */
    public static double[] getInverseCrossProduct3D(double[] a, double[] c, double precision)
    {
        double l = Vector.getLength(a);
        if (Double.compare(l, 0.0d) == 0) return null;
        double dot = getDotProduct(a, c);
        if (Double.compare(Math.abs(dot), precision) > 0) return null;
        l = l * l;
        double[] nom = getCrossProduct3D(c, a);
        //double t = 0.0d;
        return new double[]{
                nom[0] / l,// + t * a[0],
                nom[1] / l,// + t * a[1],
                nom[2] / l,// + t * a[2],

        };
    }

    /**
     * Returns the cross-product of two 3D vectors
     *
     * @param a 3d array of doubles
     * @param b 3d array of doubles
     * @return cross product of A (first) x B (second).
     */
    public static double[] getCrossProduct3D(double[] a, double[] b)
    {
        return new double[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    /**
     * Returns the cross-product of two 3D vectors
     *
     * @param a 3d array of doubles
     * @param b 3d array of doubles
     * @return cross product of A (first) x B (second).
     */
    public static float[] getCrossProduct3D(float[] a, float[] b)
    {
        return new float[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    /**
     * Returns the normal vector perpendicular to the input one (in 3D spaces).
     *
     * @param a 3d input vector
     * @return perpendicular normal vector; note that if the input vector is a zero vector or its length is not 3, null is returned
     */
    public static double[] getPerpendicularVector3D(double[] a)
    {
        if (a == null) return null;
        if (a.length != 3) return null;
        if (isZeroVector(a)) return null;

        double[] q1 = new double[]{a[0], a[2], -a[1]};
        double[] d1 = Vector.getCrossProduct3D(q1, a);
        double l1 = Vector.getLength(d1);

        if (Double.compare(l1, 0.0d) > 0)
        {
            d1[0] /= l1;
            d1[1] /= l1;
            d1[2] /= l1;
            return d1;
        }

        double[] q2 = new double[]{a[2], a[1], -a[0]};
        double[] d2 = Vector.getCrossProduct3D(q2, a);
        double l2 = Vector.getLength(d2);

        d2[0] /= l2;
        d2[1] /= l2;
        d2[2] /= l2;
        return d2;
    }

    /**
     * Returns the normal vector perpendicular to the input one (in 3D spaces).
     *
     * @param a 3d input vector
     * @return perpendicular normal vector; note that if the input vector is a zero vector or its length is not 3, null is returned
     */
    public static float[] getPerpendicularVector3D(float[] a)
    {
        if (a == null) return null;
        if (a.length != 3) return null;
        if (isZeroVector(a)) return null;

        float[] q1 = new float[]{a[0], a[2], -a[1]};
        float[] d1 = Vector.getCrossProduct3D(q1, a);
        float l1 = Vector.getLength(d1);

        if (Float.compare(l1, 0.0f) > 0)
        {
            d1[0] /= l1;
            d1[1] /= l1;
            d1[2] /= l1;
            return d1;
        }

        float[] q2 = new float[]{a[2], a[1], -a[0]};
        float[] d2 = Vector.getCrossProduct3D(q2, a);
        float l2 = Vector.getLength(d2);

        d2[0] /= l2;
        d2[1] /= l2;
        d2[2] /= l2;
        return d2;
    }

    /**
     * Checks if the input vector is a zero vector.
     *
     * @param a input vector
     * @return true, if the input vector is a zero vector; false otherwise
     */
    public static boolean isZeroVector(double[] a)
    {
        if (a == null) return false;
        for (double d : a) if (Double.compare(d, 0.0d) != 0) return false;
        return true;
    }

    /**
     * Checks if the input vector is a zero vector.
     *
     * @param a input vector
     * @return true, if the input vector is a zero vector; false otherwise
     */
    public static boolean isZeroVector(float[] a)
    {
        if (a == null) return false;
        for (float d : a) if (Float.compare(d, 0.0f) != 0) return false;
        return true;
    }


    /**
     * Calculates the dot-product of two m-dimensional vectors.
     *
     * @param a m-dimensional vector
     * @param b m-dimensional vector
     * @return dot product
     */
    public static float getDotProduct(float[] a, float[] b)
    {
        float result = 0.0f;
        for (int i = 0; i < a.length; i++)
            result += a[i] * b[i];
        return result;
    }

    /**
     * Calculates the dot-product of two m-dimensional vectors.
     *
     * @param a  m-dimensional vector
     * @param b  m-dimensional vector
     * @param ao offset in the a-vector
     * @param bo offset in the b-vector
     * @param s  stride
     * @return dot product
     */
    public static float getDotProduct(float[] a, float[] b, int ao, int bo, int s)
    {
        float result = 0.0f;
        for (int i = 0; i < s; i++)
            result += a[ao + i] * b[bo + i];
        return result;
    }


    /**
     * Calculates the dot-product of two m-dimensional vectors.
     *
     * @param a m-dimensional vector
     * @param b m-dimensional vector
     * @return dot product
     */
    public static double getDotProduct(double[] a, double[] b)
    {
        double result = 0.0d;
        for (int i = 0; i < a.length; i++)
            result += a[i] * b[i];
        return result;
    }

    /**
     * Calculates the dot-product of two m-dimensional vectors.
     *
     * @param a  m-dimensional vector
     * @param b  m-dimensional vector
     * @param ao offset in the a-vector
     * @param bo offset in the b-vector
     * @param s  stride
     * @return dot product
     */
    public static double getDotProduct(double[] a, double[] b, int ao, int bo, int s)
    {
        double result = 0.0d;
        for (int i = 0; i < s; i++)
            result += a[ao + i] * b[bo + i];
        return result;
    }

    /**
     * Calculates the length (Euclidean) of an m-dimensional vector.
     *
     * @param a m-dimensional vector
     * @return length
     */
    public static double getLength(double[] a)
    {
        double result = 0.0d;
        for (double aV : a) result += (aV * aV);
        return Math.sqrt(result);
    }

    /**
     * Calculates the length (Euclidean) of an m-dimensional vector.
     *
     * @param a m-dimensional vector
     * @param o offset
     * @param s stride
     * @return length
     */
    public static double getLength(double[] a, int o, int s)
    {
        double result = 0.0d;
        for (int i = o; i < o + s; i++) result += (a[i] * a[i]);
        return Math.sqrt(result);
    }


    /**
     * Calculates the length (Euclidean) of an m-dimensional vector.
     *
     * @param b m-dimensional vector
     * @return length
     */
    public static float getLength(float[] b)
    {
        float result = 0.0f;
        for (float aV : b) result += (aV * aV);
        return (float) Math.sqrt(result);
    }

    /**
     * Calculates the length (Euclidean) of an m-dimensional vector.
     *
     * @param a m-dimensional vector
     * @param o offset
     * @param s stride
     * @return length
     */
    public static float getLength(float[] a, int o, int s)
    {
        float result = 0.0f;
        for (int i = o; i < o + s; i++) result += (a[i] * a[i]);
        return (float) Math.sqrt(result);
    }


    /**
     * Normalizes the input vector into a unit vector  (creates a new object).
     *
     * @param v m-dimensional vector
     * @return unit vector with the same orientation as the input vector
     */
    public static double[] getUnitVector(double[] v)
    {
        return getMultiplication(v, 1.0d / getLength(v));
    }

    /**
     * Normalizes the input vector into a unit vector  (creates a new object).
     *
     * @param v m-dimensional vector
     * @return unit vector with the same orientation as the input vector
     */
    public static float[] getUnitVector(float[] v)
    {
        return getMultiplication(v, 1.0f / getLength(v));
    }

    /**
     * Normalizes the input vector (does not create a new object).
     *
     * @param v m-dimensional vector
     */
    public static void normalize(double[] v)
    {
        if (isZeroVector(v)) return;
        double l = getLength(v);
        for (int i = 0; i < v.length; i++) v[i] /= l;
    }

    /**
     * Normalizes the input vector (does not create a new object).
     *
     * @param v m-dimensional vector
     */
    public static void normalize(float[] v)
    {
        if (isZeroVector(v)) return;
        float l = getLength(v);
        for (int i = 0; i < v.length; i++) v[i] /= l;
    }

    /**
     * Calculates the difference of two m-dimensional vectors.
     *
     * @param A m-dimensional vector
     * @param B m-dimensional vector
     * @return difference between A and B (vector)
     */
    public static double[] getDifference(double[] A, double[] B)
    {
        double[] result = A.clone();
        for (int i = 0; i < result.length; i++)
            result[i] -= B[i];
        return result;
    }

    /**
     * Calculates the sum of two m-dimensional vectors (creates a new object)
     *
     * @param a m-dimensional vector
     * @param b m-dimensional vector
     * @return sum of A and B (new vector)
     */
    public static double[] getSum(double[] a, double[] b)
    {
        double[] result = a.clone();
        for (int i = 0; i < result.length; i++)
            result[i] += b[i];
        return result;
    }

    /**
     * Multiplies a vector by a scalar (rescale) (creates a new object).
     *
     * @param A m-dimensional vector
     * @param a multiplier
     * @return rescaled vector
     */
    public static double[] getMultiplication(double[] A, double a)
    {
        double[] result = A.clone();
        for (int i = 0; i < result.length; i++)
            result[i] *= a;
        return result;
    }

    /**
     * Multiplies an input vector by a scalar (rescale) (does not create a new object).
     *
     * @param A m-dimensional vector
     * @param a multiplier
     */
    public static void multiply(double[] A, double a)
    {
        for (int i = 0; i < A.length; i++) A[i] *= a;
    }


    /**
     * Multiplies a vector by a scalar (rescale) (creates a new object).
     *
     * @param A m-dimensional vector
     * @param a multiplier
     * @return rescaled vector
     */
    public static float[] getMultiplication(float[] A, float a)
    {
        float[] result = A.clone();
        for (int i = 0; i < result.length; i++)
            result[i] *= a;
        return result;
    }

    /**
     * Multiplies an input vector by a scalar (rescale) (does not create a new object).
     *
     * @param A m-dimensional vector
     * @param a multiplier
     */
    public static void multiply(float[] A, float a)
    {
        for (int i = 0; i < A.length; i++) A[i] *= a;
    }

    /**
     * Calculates intersection point(s) of two 2D lines (not segments).
     *
     * @param a 2D line (determined by two points)
     * @param b 2D line (determined by two points)
     * @return 2D intersection point(s). Null if lines do not intersect.
     * When lines overlap and the product of intersection is a line segment with length > 0, two extreme points of this segment are returned, i.e., [[x1,y1],[x2,x2]].
     * Otherwise, a single point stored in the array is returned, i.e., [[x,y]].
     */
    @SuppressWarnings("DuplicatedCode")
    public static double[][] getIntersection2D(double[][] a, double[][] b)
    {
        double[] dA = new double[]{a[1][0] - a[0][0], a[1][1] - a[0][1]};
        double[] dB = new double[]{b[1][0] - b[0][0], b[1][1] - b[0][1]};

        // SPECIAL CASE 1. TWO SINGLE POINTS THAT OVERLAP
        if ((Double.compare(dA[0], 0.0d) == 0) && (Double.compare(dA[1], 0.0d) == 0) &&
                (Double.compare(dB[0], 0.0d) == 0) && (Double.compare(dB[1], 0.0d) == 0))
            return new double[][]{a[0].clone()};


        // TWO HORIZONTAL LINES
        if ((Double.compare(dA[1], 0.0d) == 0) && (Double.compare(dB[1], 0.0d) == 0))
        {
            if (Double.compare(a[0][1] - b[0][1], 0.0d) != 0) return null; // DO NOT INTERSECT #1
            if ((a[0][0] < b[0][0]) && (a[1][0] < b[0][0]) && (a[0][0] < b[1][0]) && (a[1][0] < b[1][0]))
                return null; // DO NOT INTERSECT #2
            if ((a[0][0] > b[0][0]) && (a[1][0] > b[0][0]) && (a[0][0] > b[1][0]) && (a[1][0] > b[1][0]))
                return null; // DO NOT INTERSECT #3

            InsertionSortDouble ISD = new InsertionSortDouble();
            ISD.init(4, true);
            ISD.add(new double[]{a[0][0], a[1][0], b[0][0], b[1][0]});
            double[] n1 = new double[]{ISD._data[1], a[0][1]};
            double[] n2 = new double[]{ISD._data[2], a[0][1]};
            if (Double.compare(n1[0], n2[0]) == 0) return new double[][]{n1}; // INTERSECT WITH ONE POINT
            else return new double[][]{n1, n2}; // LINE SEGMENT INTERSECTION
        }

        // TWO VERTICAL LINES
        if ((Double.compare(dA[0], 0.0d) == 0) && (Double.compare(dB[0], 0.0d) == 0))
        {

            if (Double.compare(a[0][0] - b[0][0], 0.0d) != 0) return null; // DO NOT INTERSECT #1
            if ((a[0][1] < b[0][1]) && (a[1][1] < b[0][1]) && (a[0][1] < b[1][1]) && (a[1][1] < b[1][1]))
                return null; // DO NOT INTERSECT #2
            if ((a[0][1] > b[0][1]) && (a[1][1] > b[0][1]) && (a[0][1] > b[1][1]) && (a[1][1] > b[1][1]))
                return null; // DO NOT INTERSECT #3

            InsertionSortDouble ISD = new InsertionSortDouble();
            ISD.init(4, true);
            ISD.add(new double[]{a[0][1], a[1][1], b[0][1], b[1][1]});
            double[] n1 = new double[]{a[0][0], ISD._data[1]};
            double[] n2 = new double[]{a[0][0], ISD._data[2]};
            if (Double.compare(n1[1], n2[1]) == 0) return new double[][]{n1}; // INTERSECT WITH ONE POINT
            else return new double[][]{n1, n2}; // LINE SEGMENT INTERSECTION
        }

        Double aA = null;
        if (Double.compare(dA[0], 0.0d) != 0) aA = dA[1] / dA[0];
        Double aB = null;
        if (Double.compare(dB[0], 0.0d) != 0) aB = dB[1] / dB[0];
        Double bA = null;
        if (aA != null) bA = a[0][1] - aA * a[0][0];
        Double bB = null;
        if (aB != null) bB = b[0][1] - aB * b[0][0];


        // BOTH LINES CANNOT BE VERTICAL AT THIS MOMENT
        if (!((aA == null) || (aB == null)) && (Double.compare(aA, aB) == 0)) // THE SAME ORIENTATION BUT DO NOT INTERSECT
        {

            if (Double.compare(bA, bB) != 0) return null;
            if ((a[0][0] < b[0][0]) && (a[1][0] < b[0][0]) && (a[0][0] < b[1][0]) && (a[1][0] < b[1][0]))
                return null; // DO NOT INTERSECT #2
            if ((a[0][0] > b[0][0]) && (a[1][0] > b[0][0]) && (a[0][0] > b[1][0]) && (a[1][0] > b[1][0]))
                return null; // DO NOT INTERSECT #3

            InsertionSortDouble ISDX = new InsertionSortDouble();
            InsertionSortDouble ISDY = new InsertionSortDouble();
            ISDX.init(4, true);
            ISDY.init(4, true);
            ISDX.add(new double[]{a[0][0], a[1][0], b[0][0], b[1][0]});
            ISDY.add(new double[]{a[0][1], a[1][1], b[0][1], b[1][1]});

            double[] n1 = new double[]{ISDX._data[1], ISDY._data[1]};
            double[] n2 = new double[]{ISDX._data[2], ISDY._data[2]};
            if (aA < 0.0d) // NEGATIVE COR
            {
                n1 = new double[]{ISDX._data[1], ISDY._data[2]};
                n2 = new double[]{ISDX._data[2], ISDY._data[1]};
            }
            if (Double.compare(n1[1], n2[1]) == 0) return new double[][]{n1}; // INTERSECT WITH ONE POINT
            else return new double[][]{n1, n2}; // LINE SEGMENT INTERSECTION
        }

        // === VECTORS HAVE DIFFERENT ORIENTATION
        // CALCULATE INTERSECTION POINT
        if (aA == null) // A is vertical
        {
            //noinspection DataFlowIssue (B line cannot be vertical at this moment
            double[] p = new double[]{a[0][0], aB * a[0][0] + bB};
            if (((p[1] >= a[0][1] && p[1] <= a[1][1]) || (p[1] <= a[0][1] && p[1] >= a[1][1]))
                    && ((p[0] >= b[0][0] && p[0] <= b[1][0]) || (p[0] <= b[0][0] && p[0] >= b[1][0])))
                return new double[][]{p};
            else return null;

        }
        else if (aB == null) // B is vertical
        {
            double[] p = new double[]{b[0][0], aA * b[0][0] + bA};
            if (((p[1] >= b[0][1] && p[1] <= b[1][1]) || (p[1] <= b[0][1] && p[1] >= b[1][1])) &&
                    ((p[0] >= a[0][0] && p[0] <= a[1][0]) || (p[0] <= a[0][0] && p[0] >= a[1][0])))
                return new double[][]{p};
            else return null;
        }
        else // NO VERTICALS
        {
            double x = (bB - bA) / (aA - aB);
            double y = aA * x + bA;
            if (
                    (((x >= a[0][0]) && (x <= a[1][0])) || ((x <= a[0][0]) && (x >= a[1][0]))) &&
                            (((y >= a[0][1]) && (y <= a[1][1])) || ((y <= a[0][1]) && (y >= a[1][1]))) &&

                            (((x >= b[0][0]) && (x <= b[1][0])) || ((x <= b[0][0]) && (x >= b[1][0]))) &&
                            (((y >= b[0][1]) && (y <= b[1][1])) || ((y <= b[0][1]) && (y >= b[1][1])))
            )
                return new double[][]{new double[]{x, y}};
            else return null;
        }
    }

    /**
     * Projects a point onto a line using the shortest path (orthogonal).
     * It is assumed that the input line parameter is a direction vector
     * (zero vector is by default treated as the origin; the same is true for the input point).
     *
     * @param p M-dimensional point to be projected
     * @param l M-dimensional direction vector.
     * @return projected M-dimensional point
     */
    public static double[] getPointLineOrthogonalProjection(double[] p, double[] l)
    {
        double c = Vector.getDotProduct(l, p) / Vector.getDotProduct(l, l);
        double[] r = l.clone();
        multiply(r, c);
        return r;
    }
}
