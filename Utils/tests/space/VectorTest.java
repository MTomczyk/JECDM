package space;


import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;
import space.simplex.Simplex;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Several tests for the {@link Vector} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class VectorTest
{
    /**
     * Test 1.
     */
    @Test
    public void getTransposeTest()
    {
        {
            int[][] a = {{1, 2, 3, 4}};
            int[][] tr = Vector.getTransposed(a);
            assertEquals(1, tr[0][0]);
            assertEquals(2, tr[1][0]);
            assertEquals(3, tr[2][0]);
            assertEquals(4, tr[3][0]);
        }
        {
            int[][] a = {{1}, {2}, {3}, {4}};
            int[][] tr = Vector.getTransposed(a);
            assertEquals(1, tr[0][0]);
            assertEquals(2, tr[0][1]);
            assertEquals(3, tr[0][2]);
            assertEquals(4, tr[0][3]);
        }
        {
            int[][] a = {{1, 2}, {3, 4}};
            int[][] tr = Vector.getTransposed(a);
            assertEquals(1, tr[0][0]);
            assertEquals(2, tr[1][0]);
            assertEquals(3, tr[0][1]);
            assertEquals(4, tr[1][1]);
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void getIntersection2DTest()
    {
        { // SINGLE-POINTS CASE
            double[][] A = new double[][]{{0.0d, 1.0d}, {0.0d, 1.0d}};
            double[][] B = new double[][]{{0.0d, 1.0d}, {0.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(0.0d, i[0][0], 0.1d);
            assertEquals(1.0d, i[0][1], 0.1d);
        }

        { // HORIZONTAL BUT DO NOT INTERSECT #1
            double[][] A = new double[][]{{0.0d, 1.0d}, {2.0d, 1.0d}};
            double[][] B = new double[][]{{0.0d, 2.0d}, {4.0d, 2.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // HORIZONTAL BUT DO NOT INTERSECT #2
            double[][] A = new double[][]{{0.0d, 1.0d}, {2.0d, 1.0d}};
            double[][] B = new double[][]{{3.0d, 1.0d}, {4.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // HORIZONTAL BUT DO NOT INTERSECT #2
            double[][] A = new double[][]{{0.0d, 1.0d}, {2.0d, 1.0d}};
            double[][] B = new double[][]{{-3.0d, 1.0d}, {-4.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // HORIZONTAL WITH ONE-POINT INTERSECTION
            double[][] A = new double[][]{{0.0d, 1.0d}, {2.0d, 1.0d}};
            double[][] B = new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(1.0d, i[0][1], 0.1d);
        }
        { // HORIZONTAL WITH SEGMENT INTERSECTION
            double[][] A = new double[][]{{0.0d, 1.0d}, {3.0d, 1.0d}};
            double[][] B = new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2, i[1].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(1.0d, i[0][1], 0.1d);
            assertEquals(3.0d, i[1][0], 0.1d);
            assertEquals(1.0d, i[1][1], 0.1d);
        }

        { // VERTICAL BUT DO NOT INTERSECT #1
            double[][] A = new double[][]{{0.0d, 1.0d}, {0.0d, 2.0d}};
            double[][] B = new double[][]{{1.0d, 1.0d}, {1.0d, 2.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // VERTICAL BUT DO NOT INTERSECT #2
            double[][] A = new double[][]{{1.0d, 1.0d}, {1.0d, 2.0d}};
            double[][] B = new double[][]{{1.0d, 3.0d}, {1.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // VERTICAL BUT DO NOT INTERSECT #3
            double[][] A = new double[][]{{1.0d, 1.0d}, {1.0d, 2.0d}};
            double[][] B = new double[][]{{1.0d, -1.0d}, {1.0d, -2.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }
        { // VERTICAL WITH ONE-POINT INTERSECTION
            double[][] A = new double[][]{{2.0d, 1.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {2.0d, 3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(2.0d, i[0][1], 0.1d);
        }
        { // VERTICAL WITH SEGMENT INTERSECTION
            double[][] A = new double[][]{{2.0d, 1.0d}, {2.0d, 4.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {2.0d, 3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2, i[1].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(2.0d, i[0][1], 0.1d);
            assertEquals(2.0d, i[1][0], 0.1d);
            assertEquals(3.0d, i[1][1], 0.1d);
        }

        { // THE SAME ORIENTATION BUT DO NOT INTERSECT #1
            double[][] A = new double[][]{{1.0d, 1.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{3.0d, 5.0d}, {4.0d, 6.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // THE SAME ORIENTATION BUT DO NOT INTERSECT #2
            double[][] A = new double[][]{{1.0d, 1.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{3.0d, 3.0d}, {4.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // THE SAME ORIENTATION BUT DO NOT INTERSECT #2
            double[][] A = new double[][]{{1.0d, 1.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{-2.0d, -2.0d}, {-3.0d, -3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // THE SAME ORIENTATION ONE-POINT INTERSECTION (POSITIVE)
            double[][] A = new double[][]{{1.0d, 1.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {3.0d, 3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(2.0d, i[0][1], 0.1d);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(2.0d, i[0][1], 0.1d);
        }

        { // THE SAME ORIENTATION LINE INTERSECTION (POSITIVE)
            double[][] A = new double[][]{{1.0d, 1.0d}, {3.0d, 2.0d}};
            double[][] B = new double[][]{{2.0d, 1.5d}, {4.0d, 2.5d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(1.5d, i[0][1], 0.1d);
            assertEquals(3.0d, i[1][0], 0.1d);
            assertEquals(2.0d, i[1][1], 0.1d);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(1.5d, i[0][1], 0.1d);
            assertEquals(3.0d, i[1][0], 0.1d);
            assertEquals(2.0d, i[1][1], 0.1d);
        }

        { // THE SAME ORIENTATION ONE-POINT INTERSECTION (NEGATIVE)
            double[][] A = new double[][]{{1.0d, -1.0d}, {2.0d, -2.0d}};
            double[][] B = new double[][]{{2.0d, -2.0d}, {3.0d, -3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(-2.0d, i[0][1], 0.1d);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(-2.0d, i[0][1], 0.1d);
        }

        { // THE SAME ORIENTATION LINE INTERSECTION (POSITIVE)
            double[][] A = new double[][]{{1.0d, -1.0d}, {3.0d, -2.0d}};
            double[][] B = new double[][]{{2.0d, -1.5d}, {4.0d, -2.5d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(-1.5d, i[0][1], 0.1d);
            assertEquals(3.0d, i[1][0], 0.1d);
            assertEquals(-2.0d, i[1][1], 0.1d);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 0.1d);
            assertEquals(-1.5d, i[0][1], 0.1d);
            assertEquals(3.0d, i[1][0], 0.1d);
            assertEquals(-2.0d, i[1][1], 0.1d);
        }

        { // A - VERTICAL (NO INTERSECTION / ABOVE)
            double[][] A = new double[][]{{0.0d, 0.0d}, {0.0d, 1.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {1.0d, 3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }
        { // A - VERTICAL (NO INTERSECTION / BELOW)
            double[][] A = new double[][]{{0.0d, 0.0d}, {0.0d, 1.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {1.0d, 0.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }
        { // A - VERTICAL; INTERSECTION
            double[][] A = new double[][]{{0.0d, 0.0d}, {0.0d, 1.0d}};
            double[][] B = new double[][]{{0.0d, 0.0d}, {1.0d, 0.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(0.0d, i[0][0], 0.1d);
            assertEquals(0.0d, i[0][1], 0.1d);
        }

        { // A - VERTICAL; NO INTERSECTION
            double[][] A = new double[][]{{2.0d, 3.0d}, {2.0d, 5.0d}};
            double[][] B = new double[][]{{4.0d, 5.0d}, {3.0d, 4.5d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // B - VERTICAL (NO INTERSECTION / ABOVE)
            double[][] A = new double[][]{{1.0d, 5.0d}, {2.0d, 4.0d}};
            double[][] B = new double[][]{{1.0d, 2.0d}, {1.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // B - VERTICAL (NO INTERSECTION / BELOW)
            double[][] A = new double[][]{{2.0d, 1.0d}, {3.0d, 2.0d}};
            double[][] B = new double[][]{{1.0d, 2.0d}, {1.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }


        { // B - VERTICAL (INTERSECTION)
            double[][] A = new double[][]{{1.0d, 2.0d}, {2.0d, 3.0d}};
            double[][] B = new double[][]{{1.0d, 2.0d}, {1.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(1.0d, i[0][0], 0.1d);
            assertEquals(2.0d, i[0][1], 0.1d);
        }

        { // B - VERTICAL (NO INTERSECTION)
            double[][] A = new double[][]{{-2.0d, 0.0d}, {-1.0d, 1.0d}};
            double[][] B = new double[][]{{1.0d, 2.0d}, {1.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // B - NO VERTICALS (NO INTERSECTION)
            double[][] A = new double[][]{{1.0d, 1.0d}, {3.0d, 2.0d}};
            double[][] B = new double[][]{{7.0d, 1.0d}, {6.0d, 2.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNull(i);
        }

        { // B - NO VERTICALS (NO INTERSECTION)
            double[][] A = new double[][]{{1.0d, 3.0d}, {2.0d, 2.0d}};
            double[][] B = new double[][]{{1.0d, 2.0d}, {3.0d, 4.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(1.5d, i[0][0], 0.1d);
            assertEquals(2.5d, i[0][1], 0.1d);
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void getIntersection2DCase1Test()
    {
        double[][] lA = new double[][]
                {
                        {456.0d, 23.0d},
                        {456.0d, 411.0d}
                };

        double[][] lB = new double[][]
                {
                        {397.82, 411.65},
                        {401.70, 403.84}
                };
        double[][] i = Vector.getIntersection2D(lA, lB);
        assertNull(i);
    }

    /**
     * Test 4.
     */
    @Test
    public void getIntersection2DCase2Test()
    {
        double[][] lA = new double[][]
                {
                        {72.0d, 23.0d},
                        {456.0d, 23.0d}
                };

        double[][] lB = new double[][]
                {
                        {103.03, 411.81},
                        {106.91, 369.02}
                };
        double[][] i = Vector.getIntersection2D(lA, lB);
        assertNull(i);
    }

    /**
     * Test 5.
     */
    @Test
    public void getIntersection2DCase3Test()
    {
        double[][] lA = new double[][]
                {
                        {48.0d, 23.0d},
                        {256.0d, 23.0d}
                };

        double[][] lB = new double[][]
                {
                        {52.12, -110.44},
                        {56.24, 82.85}
                };
        double[][] i = Vector.getIntersection2D(lA, lB);
        assertNotNull(i);
        assertEquals(1, i.length);
        assertEquals(2, i[0].length);
        assertEquals(54.96428992705261, i[0][0], 0.1d);
        assertEquals(23.0d, i[0][1], 0.1d);
    }


    /**
     * Test 6.
     */
    @Test
    public void getCosineSimilarityTest()
    {
        {
            double[] a = new double[]{1.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.5d};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b), 0.0001d);
        }
        {
            double[] a = new double[]{0.0d, 1.0d};
            double[] b = new double[]{0.0d, 0.5d};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b), 0.0001d);
        }
        {
            double[] a = new double[]{1.0d, 0.0d};
            double[] b = new double[]{1.0d, 1.0d};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b), 0.0001d);
        }

        // COS TEST
        {
            double[] w1 = new double[]{1.0d, 0.0d};
            for (int i = 0; i < 101; i++)
            {
                double h = (float) i / 100.0d;
                double[] w2 = new double[]{1.0d, h};
                double l = (float) Math.sqrt(1 + h * h);
                double expected = 1.0d / l;
                double computed = Vector.getCosineSimilarity(w1, w2);
                assertEquals(expected, computed, 0.0001d);
            }
        }

        {
            double[] a = new double[]{5.0d, 1.0d, 0.0d, -5.0d};
            double[] b = new double[]{5.0d, 7.0d, 0.0d, 0.5d, -5.0d};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b, 1, 2, 2), 0.0001d);
        }
        {
            double[] a = new double[]{3.0d, 3.0d, 0.0d, 1.0d};
            double[] b = new double[]{0.0d, 0.5d, 3.0d, 3.0d};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b, 2, 0, 2), 0.0001d);
        }
        {
            double[] a = new double[]{2.0d, 2.0d, 2.0d, 1.0d, 0.0d, 2.0d};
            double[] b = new double[]{5.0d, 1.0d, 1.0d};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b, 3, 1, 2), 0.0001d);
        }

        // COS TEST
        {
            double[] w1 = new double[]{1.0d, 0.0d, 2.0d};
            for (int i = 0; i < 101; i++)
            {
                double h = (double) i / 100.0d;
                double[] w2 = new double[]{2.0d, 1.0d, h};
                double l = (float) Math.sqrt(1 + h * h);
                double expected = 1.0d / l;
                double computed = Vector.getCosineSimilarity(w1, w2, 0, 1, 2);
                assertEquals(expected, computed, 0.0001d);
            }
        }

        {
            float[] a = new float[]{1.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.5f};
            assertEquals(0.0f, Vector.getCosineSimilarity(a, b), 0.0001d);
        }
        {
            float[] a = new float[]{0.0f, 1.0f};
            float[] b = new float[]{0.0f, 0.5f};
            assertEquals(1.0f, Vector.getCosineSimilarity(a, b), 0.0001d);
        }
        {
            float[] a = new float[]{1.0f, 0.0f};
            float[] b = new float[]{1.0f, 1.0f};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b), 0.0001d);
        }

        // COS TEST
        {
            float[] w1 = new float[]{1.0f, 0.0f};
            for (int i = 0; i < 101; i++)
            {
                float h = (float) i / 100.0f;
                float[] w2 = new float[]{1.0f, h};
                float l = (float) Math.sqrt(1 + h * h);
                float expected = 1.0f / l;
                float computed = Vector.getCosineSimilarity(w1, w2);
                assertEquals(expected, computed, 0.0001d);
            }
        }

        {
            float[] a = new float[]{5.0f, 1.0f, 0.0f, -5.0f};
            float[] b = new float[]{5.0f, 7.0f, 0.0f, 0.5f, -5.0f};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b, 1, 2, 2), 0.0001d);
        }
        {
            float[] a = new float[]{3.0f, 3.0f, 0.0f, 1.0f};
            float[] b = new float[]{0.0f, 0.5f, 3.0f, 3.0f};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b, 2, 0, 2), 0.0001d);
        }
        {
            float[] a = new float[]{2.0f, 2.0f, 2.0f, 1.0f, 0.0f, 2.0f};
            float[] b = new float[]{5.0f, 1.0f, 1.0f};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b, 3, 1, 2), 0.0001d);
        }

        // COS TEST
        {
            float[] w1 = new float[]{1.0f, 0.0f, 2.0f};
            for (int i = 0; i < 101; i++)
            {
                float h = (float) i / 100.0f;
                float[] w2 = new float[]{2.0f, 1.0f, h};
                float l = (float) Math.sqrt(1 + h * h);
                float expected = 1.0f / l;
                float computed = Vector.getCosineSimilarity(w1, w2, 0, 1, 2);
                assertEquals(expected, computed, 0.0001d);
            }
        }
    }

    /**
     * Test 7 (getLineSegmentDivisions).
     */
    @Test
    public void getLineSegmentDivisions()
    {
        {
            double[] el = new double[]{0.4d, 0.5d, 0.6d, 0.7d, 1.0d, Math.sqrt(2.0d) + 0.0000000001d};
            int[] exp = new int[]{3, 2, 2, 2, 1, 0};

            for (int i = 0; i < el.length; i++)
            {
                double[] p = new double[]{0.0f, 0.0f};
                double[] n = new double[]{1.0f, 1.0f};
                int divs = Vector.getLineSegmentDivisions(p, n, el[i]);
                assertEquals(exp[i], divs);
            }
        }
        {
            float[] el = new float[]{0.4f, 0.5f, 0.6f, 0.7f, 1.0f, (float) (Math.sqrt(2.0d) + 0.000001f)};
            int[] exp = new int[]{3, 2, 2, 2, 1, 0};

            for (int i = 0; i < el.length; i++)
            {
                float[] a = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
                int divs = Vector.getLineSegmentDivisions(a, 0, 2, 2, el[i]);
                assertEquals(exp[i], divs);
            }
        }
    }

    /**
     * Test 8 (get cross product).
     */
    @Test
    public void getCrossProductDouble()
    {
        {
            double[] a = new double[]{2.0d, 3.0d, 4.0d};
            double[] b = new double[]{5.0d, 6.0d, 7.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{-3.0d, 6.0d, -3.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{0.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.0d, 1.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 1.0d};
            double[] b = new double[]{0.0d, 1.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{-1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 0.0d};
            double[] b = new double[]{0.0d, 1.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{0.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.00000001d);
        }
    }

    /**
     * Test 9 (get cross product).
     */
    @Test
    public void getCrossProductFloat()
    {
        {
            float[] a = new float[]{2.0f, 3.0f, 4.0f};
            float[] b = new float[]{5.0f, 6.0f, 7.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{-3.0f, 6.0f, -3.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.0000001f);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{0.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.0000001f);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.0f, 1.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.0000001f);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 1.0f};
            float[] b = new float[]{0.0f, 1.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{-1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.0000001f);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 0.0f};
            float[] b = new float[]{0.0f, 1.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{0.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 0.0000001f);
        }
    }

    /**
     * Test 10 (gets perpendicular vector).
     */
    @Test
    public void getPerpendicularVectorDouble()
    {
        {
            double[] a = new double[3];
            assertNull(Vector.getPerpendicularVector3D(a));
        }
        {
            assertNull(Vector.getPerpendicularVector3D((double[]) null));
        }
        {
            double[] a = new double[2];
            assertNull(Vector.getPerpendicularVector3D(a));
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 0.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{1.0d, 0.0d, 0.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{0.0d, -1.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{1.0d, 1.0d, 0.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d), 1.0d / Math.sqrt(3.0d)};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{1.0d, 0.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d)};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            double[] a = new double[]{1.0d, 1.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(2.0d), -1.0d / Math.sqrt(2.0d), 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00000001d);
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int i = 0; i < 1000000; i++)
            {
                double[] a = new double[]{R.nextDouble(), R.nextDouble(), R.nextDouble()};
                double[] p = Vector.getPerpendicularVector3D(a);
                double sc = a[0] * p[0] + a[1] * p[1] + a[2] * p[2];
                assertEquals(0.0d, sc, 0.0000001d);
            }
        }
    }

    /**
     * Test 11 (gets perpendicular vector).
     */
    @Test
    public void getPerpendicularVectorFloat()
    {
        {
            float[] a = new float[3];
            assertNull(Vector.getPerpendicularVector3D(a));
        }
        {
            assertNull(Vector.getPerpendicularVector3D((float[]) null));
        }
        {
            float[] a = new float[2];
            assertNull(Vector.getPerpendicularVector3D(a));
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 0.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{1.0f, 0.0f, 0.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{0.0f, -1.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{1.0f, 1.0f, 0.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(3.0d)), -(float) (1.0d / Math.sqrt(3.0d)), (float) (1.0d / Math.sqrt(3.0d))};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{1.0f, 0.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(3.0d)), (float) (-1.0d / Math.sqrt(3.0d)), (float) (-1.0d / Math.sqrt(3.0d))};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            float[] a = new float[]{1.0f, 1.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(2.0d)), (float) (-1.0d / Math.sqrt(2.0d)), 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 0.00001f);
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int i = 0; i < 1000000; i++)
            {
                float[] a = new float[]{R.nextFloat(), R.nextFloat(), R.nextFloat()};
                float[] p = Vector.getPerpendicularVector3D(a);
                float sc = a[0] * p[0] + a[1] * p[1] + a[2] * p[2];
                assertEquals(0.0d, sc, 0.00001f);
            }
        }
    }


    /**
     * Test 12 (get second input of the cross product 3D).
     */
    @Test
    public void getInverseCrossProduct3DDouble()
    {
        {
            double[] a = new double[3];
            double[] c = new double[3];
            double[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            assertNull(b);
        }
        {
            double[] a = new double[]{1.0d, 0.0d, 0.0d};
            double[] c = new double[]{0.0, 1.0d, 0.0d};
            double[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            assertNotNull(b);
            assertEquals(0.0d, b[0], 0.0000001d);
            assertEquals(0.0d, b[1], 0.0000001d);
            assertEquals(-1.0d, b[2], 0.0000001d);
        }
        IRandom R = new MersenneTwister64(0);
        for (int i = 0; i < 1000000; i++)
        {
            double[] a = new double[]{R.nextDouble(), R.nextDouble(), R.nextDouble()};
            double[] c = new double[]{R.nextDouble(), R.nextDouble(), R.nextDouble()};
            double dot = Vector.getDotProduct(a, c);
            double[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            if (Double.compare(Math.abs(dot), Float.MIN_NORMAL) > 0) assertNull(b);
            else if (Vector.isZeroVector(a)) assertNull(b);
            else if (Vector.areVectorsEqual(a, c)) assertTrue(Vector.isZeroVector(b));
            else
            {
                assertNotNull(b);
                double[] cc = Vector.getCrossProduct3D(a, b);
                double[] nCC = Vector.getUnitVector(cc);
                double[] nC = Vector.getUnitVector(c);
                assertTrue(Vector.areVectorsEqual(nCC, nC));
            }
        }

        for (int i = 0; i < 1000000; i++)
        {
            double[] a = new double[]{R.nextDouble(), R.nextDouble(), R.nextDouble()};
            double[] c = Vector.getPerpendicularVector3D(a);

            double dot = Vector.getDotProduct(a, c);
            double[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            if (Double.compare(Math.abs(dot), Float.MIN_NORMAL) > 0) assertNull(b);
            else if (Vector.isZeroVector(a)) assertNull(b);
            else if (Vector.areVectorsEqual(a, c)) assertTrue(Vector.isZeroVector(b));
            else
            {
                assertNotNull(b);
                double[] cc = Vector.getCrossProduct3D(a, b);
                double[] nCC = Vector.getUnitVector(cc);
                double[] nC = Vector.getUnitVector(c);
                assertTrue(Vector.areVectorsEqual(nCC, nC, 0.0000001d));
            }
        }
    }

    /**
     * Test 13 (get second input of the cross product 3D).
     */
    @Test
    public void getInverseCrossProduct3DFloat()
    {
        {
            float[] a = new float[3];
            float[] c = new float[3];
            float[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            assertNull(b);
        }
        {
            float[] a = new float[]{1.0f, 0.0f, 0.0f};
            float[] c = new float[]{0.0f, 1.0f, 0.0f};
            float[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            assertNotNull(b);
            assertEquals(0.0f, b[0], 0.0000001f);
            assertEquals(0.0f, b[1], 0.0000001f);
            assertEquals(-1.0f, b[2], 0.0000001f);
        }
        IRandom R = new MersenneTwister64(0);
        for (int i = 0; i < 1000000; i++)
        {
            float[] a = new float[]{R.nextFloat(), R.nextFloat(), R.nextFloat()};
            float[] c = new float[]{R.nextFloat(), R.nextFloat(), R.nextFloat()};
            float dot = Vector.getDotProduct(a, c);
            float[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            if (Float.compare(Math.abs(dot), Float.MIN_NORMAL) > 0) assertNull(b);
            else if (Vector.isZeroVector(a)) assertNull(b);
            else if (Vector.areVectorsEqual(a, c)) assertTrue(Vector.isZeroVector(b));
            else
            {
                assertNotNull(b);
                float[] cc = Vector.getCrossProduct3D(a, b);
                float[] nCC = Vector.getUnitVector(cc);
                float[] nC = Vector.getUnitVector(c);
                assertTrue(Vector.areVectorsEqual(nCC, nC));
            }
        }

        for (int i = 0; i < 1000000; i++)
        {
            float[] a = new float[]{R.nextFloat(), R.nextFloat(), R.nextFloat()};
            float[] c = Vector.getPerpendicularVector3D(a);

            float dot = Vector.getDotProduct(a, c);
            float[] b = Vector.getInverseCrossProduct3D(a, c, Float.MIN_NORMAL);
            if (Double.compare(Math.abs(dot), Float.MIN_NORMAL) > 0) assertNull(b);
            else if (Vector.isZeroVector(a)) assertNull(b);
            else if (Vector.areVectorsEqual(a, c)) assertTrue(Vector.isZeroVector(b));
            else
            {
                assertNotNull(b);
                float[] cc = Vector.getCrossProduct3D(a, b);
                float[] nCC = Vector.getUnitVector(cc);
                float[] nC = Vector.getUnitVector(c);
                assertTrue(Vector.areVectorsEqual(nCC, nC, 0.00001f));
            }
        }
    }


    /**
     * Test 14 (get point line orthogonal projection).
     */
    @Test
    public void getPointLineOrthogonalProjection()
    {
        {
            double[] p = new double[]{0.0d, 2.0d};
            double[] l = new double[]{1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(1.0d, r[0], 0.000000001d);
            assertEquals(1.0d, r[1], 0.000000001d);
        }

        {
            double[] p = new double[]{0.0d, 1.0d};
            double[] l = new double[]{1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(0.5d, r[0], 0.000000001d);
            assertEquals(0.5d, r[1], 0.000000001d);
        }

        {
            double[] p = new double[]{0.0d, 3.0d, 0.0d};
            double[] l = new double[]{1.0d, 1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(1.0d, r[0], 0.000000001d);
            assertEquals(1.0d, r[1], 0.000000001d);
            assertEquals(1.0d, r[2], 0.000000001d);
        }

        IRandom R = new MersenneTwister64(0);
        for (int M = 1; M < 5; M++)
        {
            for (int t = 0; t < 1000; t++)
            {
                double[] l = new double[M];
                double[] p = new double[M];
                for (int i = 0; i < M; i++)
                {
                    l[i] = R.nextDouble();
                    p[i] = R.nextDouble();
                }

                double[] r = Vector.getPointLineOrthogonalProjection(p, l);
                double[] delta = new double[M];
                for (int i = 0; i < M; i++) delta[i] = r[i] - p[i];
                double dot = Vector.getDotProduct(delta, l);
                double cos = Vector.getCosineSimilarity(r, l);
                assertEquals(0.0d, dot, 0.0000001d);
                assertEquals(1.0d, cos, 0.0000001d);
            }
        }
    }

    /**
     * Test 15 (get combination).
     */
    @Test
    public void getCombination()
    {
        {
            double[] b = new double[1];
            assertNull(Vector.getCombination(null, b, 1.0d));
        }
        {
            double[] a = new double[1];
            assertNull(Vector.getCombination(a, null, 1.0d));
        }
        {
            double[] a = new double[2];
            double[] b = new double[1];
            assertNull(Vector.getCombination(a, b, 1.0d));
        }
        {
            double[] a = new double[0];
            double[] b = new double[0];
            double[] c = Vector.getCombination(a, b, 1.0d);
            assertNotNull(c);
            assertEquals(0, c.length);
        }
        {
            double[] a = new double[]{0.0d, 1.0d};
            double[] b = new double[]{1.0d, 0.0d};
            double[] w = new double[]{-1.0d, 0.0d, 0.5d, 1.0d, 2.0d};
            double[][] exp = new double[][]{
                    {-1.0d, 2.0d},
                    {0.0d, 1.0d},
                    {0.5d, 0.5d},
                    {1.0d, 0.0d},
                    {2.0d, -1.0d}
            };
            for (int i = 0; i < exp.length; i++)
            {
                double[] c = Vector.getCombination(a, b, w[i]);
                assertNotNull(c);
                assertEquals(2, c.length);
                assertEquals(exp[i][0], c[0], 0.0000001d);
                assertEquals(exp[i][1], c[1], 0.0000001d);
            }
        }
        { // checks if produces normalized
            IRandom R = new MersenneTwister64(0);

            for (int m = 1; m < 5; m++) // dimensions
            {
                for (int i = 0; i < 1000; i++) // no. trials
                {
                    double[] w1 = WeightsGenerator.getNormalizedWeightVector(m, R);
                    double[] w2 = WeightsGenerator.getNormalizedWeightVector(m, R);
                    double[] w3 = Vector.getCombination(w1, w2, R.nextDouble());
                    assertTrue(Simplex.isOnSimplex(w3, 1.0E-12));
                }
            }
        }
    }

    /**
     * Test 16 (thresholdAtOneFromAbove).
     */
    @Test
    public void thresholdAtOneFromAbove()
    {
        double [] a = new double[]{1.1d, 1.00000d};
        Vector.thresholdAtOneFromAbove(a);
        assertEquals(1.0d, a[0]);
        assertEquals(1.0d, a[1]);
    }

    /**
     * Test 17 (thresholdAtZeroFromBelow).
     */
    @Test
    public void thresholdAtZeroFromBelow()
    {
        double [] a = new double[]{-0.00001d, 0.0d};
        Vector.thresholdAtZeroFromBelow(a);
        assertEquals(0.0d, a[0]);
        assertEquals(0.0d, a[1]);
    }
}