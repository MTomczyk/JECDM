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
     * Test "getTransposed".
     */
    @SuppressWarnings("ConstantValue")
    @Test
    public void areVectorsEqualTest()
    {
        // double
        {
            assertTrue(Vector.areVectorsEqual(null, (double[]) null));
            assertFalse(Vector.areVectorsEqual(new double[]{}, null));
            assertFalse(Vector.areVectorsEqual(null, new double[]{}));
            assertFalse(Vector.areVectorsEqual(new double[]{1.0d, 2.0d}, new double[]{1.0d}));
            assertTrue(Vector.areVectorsEqual(new double[]{1.0d, 2.0d}, new double[]{1.0d, 2.0d}));
            assertFalse(Vector.areVectorsEqual(new double[]{1.0d, 2.0d}, new double[]{1.0d, 1.5d}));
            assertFalse(Vector.areVectorsEqual(new double[]{1.0d, 2.0d}, new double[]{1.0d, 1.5d}, 0.49999d));
            assertTrue(Vector.areVectorsEqual(new double[]{1.0d, 2.0d}, new double[]{1.0d, 1.5d}, 0.50001d));
        }
        // float
        {
            assertTrue(Vector.areVectorsEqual(null, (float[]) null));
            assertFalse(Vector.areVectorsEqual(new float[]{}, null));
            assertFalse(Vector.areVectorsEqual(null, new float[]{}));
            assertFalse(Vector.areVectorsEqual(new float[]{1.0f, 2.0f}, new float[]{1.0f}));
            assertTrue(Vector.areVectorsEqual(new float[]{1.0f, 2.0f}, new float[]{1.0f, 2.0f}));
            assertFalse(Vector.areVectorsEqual(new float[]{1.0f, 2.0f}, new float[]{1.0f, 1.5f}));
            assertFalse(Vector.areVectorsEqual(new float[]{1.0f, 2.0f}, new float[]{1.0f, 1.5f}, 0.49999f));
            assertTrue(Vector.areVectorsEqual(new float[]{1.0f, 2.0f}, new float[]{1.0f, 1.5f}, 0.50001f));
        }
        // int
        {
            assertTrue(Vector.areVectorsEqual(null, (int[]) null));
            assertFalse(Vector.areVectorsEqual(new int[]{}, null));
            assertFalse(Vector.areVectorsEqual(null, new int[]{}));
            assertFalse(Vector.areVectorsEqual(new int[]{1, 2}, new int[]{1}));
            assertTrue(Vector.areVectorsEqual(new int[]{1, 2}, new int[]{1, 2}));
            assertFalse(Vector.areVectorsEqual(new int[]{1, 2}, new int[]{1, 3}));
        }
        // boolean
        {
            assertTrue(Vector.areVectorsEqual(null, (boolean[]) null));
            assertFalse(Vector.areVectorsEqual(new boolean[]{}, null));
            assertFalse(Vector.areVectorsEqual(null, new boolean[]{}));
            assertFalse(Vector.areVectorsEqual(new boolean[]{true, false}, new boolean[]{true}));
            assertTrue(Vector.areVectorsEqual(new boolean[]{true, false}, new boolean[]{true, false}));
            assertFalse(Vector.areVectorsEqual(new boolean[]{true, false}, new boolean[]{true, true}));
        }
    }

    /**
     * Test "multiply".
     */
    @SuppressWarnings("ConstantValue")
    @Test
    public void multiplyTest()
    {
        // double
        {
            {
                double[] v = null;
                Vector.multiply(v, 0.0d);
            }
            {
                double[] v = new double[]{1.0d, -3.0d};
                Vector.multiply(v, 3);
                assertEquals(3.0d, v[0], 1.0E-6);
                assertEquals(-9.0d, v[1], 1.0E-6);
            }
        }
        // float
        {
            {
                float[] v = null;
                Vector.multiply(v, 0.0f);
            }
            {
                float[] v = new float[]{1.0f, -3.0f};
                Vector.multiply(v, 3);
                assertEquals(3.0f, v[0], 1.0E-6);
                assertEquals(-9.0f, v[1], 1.0E-6);
            }
        }
    }

    /**
     * Test "getMultiplication".
     */
    @Test
    public void getMultiplicationTest()
    {
        // double
        {
            {
                assertNull(Vector.getMultiplication(null, 1.0d));
            }
            {
                double[] v = new double[]{1.0d, -3.0d};
                double[] nv = Vector.getMultiplication(v, 3.0d);
                assertEquals(1.0d, v[0], 1.0E-6);
                assertEquals(-3.0d, v[1], 1.0E-6);
                assertEquals(3.0d, nv[0], 1.0E-6);
                assertEquals(-9.0d, nv[1], 1.0E-6);
            }
        }
        // float
        {
            {
                assertNull(Vector.getMultiplication((float[]) null, 1.0f));
            }
            {
                float[] v = new float[]{1.0f, -3.0f};
                float[] nv = Vector.getMultiplication(v, 3.0f);
                assertEquals(1.0f, v[0], 1.0E-6f);
                assertEquals(-3.0f, v[1], 1.0E-6f);
                assertEquals(3.0f, nv[0], 1.0E-6f);
                assertEquals(-9.0f, nv[1], 1.0E-6f);
            }
        }
    }

    /**
     * Test "getTransposed".
     */
    @Test
    public void getTransposedTest()
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
     * Test "getIntersection2D".
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
            assertEquals(0.0d, i[0][0], 1.0E-6);
            assertEquals(1.0d, i[0][1], 1.0E-6);
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
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(1.0d, i[0][1], 1.0E-6);
        }
        { // HORIZONTAL WITH SEGMENT INTERSECTION
            double[][] A = new double[][]{{0.0d, 1.0d}, {3.0d, 1.0d}};
            double[][] B = new double[][]{{2.0d, 1.0d}, {3.0d, 1.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2, i[1].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(1.0d, i[0][1], 1.0E-6);
            assertEquals(3.0d, i[1][0], 1.0E-6);
            assertEquals(1.0d, i[1][1], 1.0E-6);
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
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(2.0d, i[0][1], 1.0E-6);
        }
        { // VERTICAL WITH SEGMENT INTERSECTION
            double[][] A = new double[][]{{2.0d, 1.0d}, {2.0d, 4.0d}};
            double[][] B = new double[][]{{2.0d, 2.0d}, {2.0d, 3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2, i[1].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(2.0d, i[0][1], 1.0E-6);
            assertEquals(2.0d, i[1][0], 1.0E-6);
            assertEquals(3.0d, i[1][1], 1.0E-6);
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
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(2.0d, i[0][1], 1.0E-6);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(2.0d, i[0][1], 1.0E-6);
        }

        { // THE SAME ORIENTATION LINE INTERSECTION (POSITIVE)
            double[][] A = new double[][]{{1.0d, 1.0d}, {3.0d, 2.0d}};
            double[][] B = new double[][]{{2.0d, 1.5d}, {4.0d, 2.5d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(1.5d, i[0][1], 1.0E-6);
            assertEquals(3.0d, i[1][0], 1.0E-6);
            assertEquals(2.0d, i[1][1], 1.0E-6);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(1.5d, i[0][1], 1.0E-6);
            assertEquals(3.0d, i[1][0], 1.0E-6);
            assertEquals(2.0d, i[1][1], 1.0E-6);
        }

        { // THE SAME ORIENTATION ONE-POINT INTERSECTION (NEGATIVE)
            double[][] A = new double[][]{{1.0d, -1.0d}, {2.0d, -2.0d}};
            double[][] B = new double[][]{{2.0d, -2.0d}, {3.0d, -3.0d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(-2.0d, i[0][1], 1.0E-6);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(1, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(-2.0d, i[0][1], 1.0E-6);
        }

        { // THE SAME ORIENTATION LINE INTERSECTION (POSITIVE)
            double[][] A = new double[][]{{1.0d, -1.0d}, {3.0d, -2.0d}};
            double[][] B = new double[][]{{2.0d, -1.5d}, {4.0d, -2.5d}};
            double[][] i = Vector.getIntersection2D(A, B);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(-1.5d, i[0][1], 1.0E-6);
            assertEquals(3.0d, i[1][0], 1.0E-6);
            assertEquals(-2.0d, i[1][1], 1.0E-6);

            i = Vector.getIntersection2D(B, A);
            assertNotNull(i);
            assertEquals(2, i.length);
            assertEquals(2, i[0].length);
            assertEquals(2.0d, i[0][0], 1.0E-6);
            assertEquals(-1.5d, i[0][1], 1.0E-6);
            assertEquals(3.0d, i[1][0], 1.0E-6);
            assertEquals(-2.0d, i[1][1], 1.0E-6);
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
            assertEquals(0.0d, i[0][0], 1.0E-6);
            assertEquals(0.0d, i[0][1], 1.0E-6);
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
            assertEquals(1.0d, i[0][0], 1.0E-6);
            assertEquals(2.0d, i[0][1], 1.0E-6);
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
            assertEquals(1.5d, i[0][0], 1.0E-6);
            assertEquals(2.5d, i[0][1], 1.0E-6);
        }
    }

    /**
     * Test "getIntersection2D".
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
     * Test "getIntersection2D".
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
     * Test "getIntersection2D".
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
        assertEquals(54.96428992705261, i[0][0], 1.0E-6);
        assertEquals(23.0d, i[0][1], 1.0E-6);
    }


    /**
     * Test "getCosineSimilarity".
     */
    @Test
    public void getCosineSimilarityTest()
    {
        {
            double[] a = new double[]{1.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.5d};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b), 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 1.0d};
            double[] b = new double[]{0.0d, 0.5d};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b), 1.0E-6);
        }
        {
            double[] a = new double[]{1.0d, 0.0d};
            double[] b = new double[]{1.0d, 1.0d};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b), 1.0E-6);
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
                assertEquals(expected, computed, 1.0E-6);
            }
        }

        {
            double[] a = new double[]{5.0d, 1.0d, 0.0d, -5.0d};
            double[] b = new double[]{5.0d, 7.0d, 0.0d, 0.5d, -5.0d};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b, 1, 2, 2), 1.0E-6);
        }
        {
            double[] a = new double[]{3.0d, 3.0d, 0.0d, 1.0d};
            double[] b = new double[]{0.0d, 0.5d, 3.0d, 3.0d};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b, 2, 0, 2), 1.0E-6);
        }
        {
            double[] a = new double[]{2.0d, 2.0d, 2.0d, 1.0d, 0.0d, 2.0d};
            double[] b = new double[]{5.0d, 1.0d, 1.0d};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b, 3, 1, 2), 1.0E-6);
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
                assertEquals(expected, computed, 1.0E-6);
            }
        }

        {
            float[] a = new float[]{1.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.5f};
            assertEquals(0.0f, Vector.getCosineSimilarity(a, b), 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 1.0f};
            float[] b = new float[]{0.0f, 0.5f};
            assertEquals(1.0f, Vector.getCosineSimilarity(a, b), 1.0E-6);
        }
        {
            float[] a = new float[]{1.0f, 0.0f};
            float[] b = new float[]{1.0f, 1.0f};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b), 1.0E-6);
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
                assertEquals(expected, computed, 1.0E-6);
            }
        }

        {
            float[] a = new float[]{5.0f, 1.0f, 0.0f, -5.0f};
            float[] b = new float[]{5.0f, 7.0f, 0.0f, 0.5f, -5.0f};
            assertEquals(0.0d, Vector.getCosineSimilarity(a, b, 1, 2, 2), 1.0E-6);
        }
        {
            float[] a = new float[]{3.0f, 3.0f, 0.0f, 1.0f};
            float[] b = new float[]{0.0f, 0.5f, 3.0f, 3.0f};
            assertEquals(1.0d, Vector.getCosineSimilarity(a, b, 2, 0, 2), 1.0E-6);
        }
        {
            float[] a = new float[]{2.0f, 2.0f, 2.0f, 1.0f, 0.0f, 2.0f};
            float[] b = new float[]{5.0f, 1.0f, 1.0f};
            assertEquals(Math.sqrt(2.0d) / 2.0d, Vector.getCosineSimilarity(a, b, 3, 1, 2), 1.0E-6);
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
                assertEquals(expected, computed, 1.0E-6);
            }
        }
    }

    /**
     * Test "getLineSegmentDivisions".
     */
    @Test
    public void getLineSegmentDivisions()
    {
        {
            double[] el = new double[]{0.4d, 0.5d, 0.6d, 0.7d, 1.0d, Math.sqrt(2.0d) + 1.0E-6};
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
            float[] el = new float[]{0.4f, 0.5f, 0.6f, 0.7f, 1.0f, (float) (Math.sqrt(2.0d) + 1.0E-6)};
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
     * Test "getCrossProduct3D" (double).
     */
    @Test
    public void getCrossProduct3DDouble()
    {
        {
            double[] a = new double[]{2.0d, 3.0d, 4.0d};
            double[] b = new double[]{5.0d, 6.0d, 7.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{-3.0d, 6.0d, -3.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{0.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 0.0d};
            double[] b = new double[]{0.0d, 0.0d, 1.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 1.0d};
            double[] b = new double[]{0.0d, 1.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{-1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 0.0d};
            double[] b = new double[]{0.0d, 1.0d, 0.0d};
            double[] c = Vector.getCrossProduct3D(a, b);
            double[] e = new double[]{0.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
    }

    /**
     * Test "getCrossProduct3D" (float).
     */
    @Test
    public void getCrossProduct3DFloat()
    {
        {
            float[] a = new float[]{2.0f, 3.0f, 4.0f};
            float[] b = new float[]{5.0f, 6.0f, 7.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{-3.0f, 6.0f, -3.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{0.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 0.0f};
            float[] b = new float[]{0.0f, 0.0f, 1.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 1.0f};
            float[] b = new float[]{0.0f, 1.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{-1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 0.0f};
            float[] b = new float[]{0.0f, 1.0f, 0.0f};
            float[] c = Vector.getCrossProduct3D(a, b);
            float[] e = new float[]{0.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(c[i], e[i], 1.0E-6);
        }
    }

    /**
     * Test "getPerpendicularVector2D (double)".
     */
    @Test
    public void getPerpendicularVector2DDouble()
    {
        {
            assertNull(Vector.getPerpendicularVector2D((double[]) null));
            assertNull(Vector.getPerpendicularVector2D(new double[1]));
            assertNull(Vector.getPerpendicularVector2D(new double[2]));
        }

        {
            double[] v = Vector.getPerpendicularVector2D(new double[]{1.0d, 2.0d});
            assertNotNull(v);
            assertEquals(2.0d, v[0], 1.0E-6);
            assertEquals(-1.0d, v[1], 1.0E-6);
        }

        {
            double[] v = Vector.getPerpendicularVector2D(new double[]{1.0d, 0.0d});
            assertNotNull(v);
            assertEquals(0.0d, v[0], 1.0E-6);
            assertEquals(-1.0d, v[1], 1.0E-6);
        }

        {
            double[] v = Vector.getPerpendicularVector2D(new double[]{0.0d, -1.0d});
            assertNotNull(v);
            assertEquals(-1.0d, v[0], 1.0E-6);
            assertEquals(0.0d, v[1], 1.0E-6);
        }

        {
            double[] v = Vector.getPerpendicularVector2D(new double[]{0.0d, 1.0d});
            assertNotNull(v);
            assertEquals(1.0d, v[0], 1.0E-6);
            assertEquals(0.0d, v[1], 1.0E-6);
        }

        {
            IRandom R = new MersenneTwister64(0);
            for (int t = 0; t < 10000; t++)
            {
                double[] w = WeightsGenerator.getNormalizedWeightVector(2, R);
                double[] pw = Vector.getPerpendicularVector2D(w);
                double sp = Vector.getDotProduct(w, pw);
                assertEquals(0.0d, sp, 1.0E-5);
            }
        }
    }

    /**
     * Test "getPerpendicularVector2D (float)".
     */
    @Test
    public void getPerpendicularVector2DFloat()
    {
        {
            assertNull(Vector.getPerpendicularVector2D((float[]) null));
            assertNull(Vector.getPerpendicularVector2D(new float[1]));
            assertNull(Vector.getPerpendicularVector2D(new float[2]));
        }

        {
            float[] v = Vector.getPerpendicularVector2D(new float[]{1.0f, 2.0f});
            assertNotNull(v);
            assertEquals(2.0f, v[0], 1.0E-6);
            assertEquals(-1.0f, v[1], 1.0E-6);
        }

        {
            float[] v = Vector.getPerpendicularVector2D(new float[]{1.0f, 0.0f});
            assertNotNull(v);
            assertEquals(0.0f, v[0], 1.0E-6);
            assertEquals(-1.0f, v[1], 1.0E-6);
        }

        {
            float[] v = Vector.getPerpendicularVector2D(new float[]{0.0f, -1.0f});
            assertNotNull(v);
            assertEquals(-1.0f, v[0], 1.0E-6);
            assertEquals(0.0f, v[1], 1.0E-6);
        }

        {
            float[] v = Vector.getPerpendicularVector2D(new float[]{0.0f, 1.0f});
            assertNotNull(v);
            assertEquals(1.0f, v[0], 1.0E-6);
            assertEquals(0.0f, v[1], 1.0E-6);
        }


        {
            IRandom R = new MersenneTwister64(0);
            for (int t = 0; t < 10000; t++)
            {
                double[] w = WeightsGenerator.getNormalizedWeightVector(2, R);
                float[] fw = new float[]{(float) w[0], (float) w[1]};
                float[] pw = Vector.getPerpendicularVector2D(fw);
                float sp = Vector.getDotProduct(fw, pw);
                assertEquals(0.0d, sp, 1.0E-5);
            }
        }
    }

    /**
     * Test "getPerpendicularVector3D (double)".
     */
    @Test
    public void getPerpendicularVector3DDouble()
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
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 0.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{1.0d, 0.0d, 0.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{0.0d, -1.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{1.0d, 1.0d, 0.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d), 1.0d / Math.sqrt(3.0d)};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{0.0d, 1.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d, 0.0d, 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{1.0d, 0.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d), -1.0d / Math.sqrt(3.0d)};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            double[] a = new double[]{1.0d, 1.0d, 1.0d};
            double[] p = Vector.getPerpendicularVector3D(a);
            double[] e = new double[]{1.0d / Math.sqrt(2.0d), -1.0d / Math.sqrt(2.0d), 0.0d};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int i = 0; i < 1000000; i++)
            {
                double[] a = new double[]{R.nextDouble(), R.nextDouble(), R.nextDouble()};
                double[] p = Vector.getPerpendicularVector3D(a);
                double sc = a[0] * p[0] + a[1] * p[1] + a[2] * p[2];
                assertEquals(0.0d, sc, 1.0E-6);
            }
        }
    }

    /**
     * Test "getPerpendicularVector3D (float)".
     */
    @Test
    public void getPerpendicularVector3DFloat()
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
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 0.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{1.0f, 0.0f, 0.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{0.0f, -1.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{1.0f, 1.0f, 0.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(3.0d)), -(float) (1.0d / Math.sqrt(3.0d)), (float) (1.0d / Math.sqrt(3.0d))};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{0.0f, 1.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{1.0f, 0.0f, 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{1.0f, 0.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(3.0d)), (float) (-1.0d / Math.sqrt(3.0d)), (float) (-1.0d / Math.sqrt(3.0d))};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            float[] a = new float[]{1.0f, 1.0f, 1.0f};
            float[] p = Vector.getPerpendicularVector3D(a);
            float[] e = new float[]{(float) (1.0d / Math.sqrt(2.0d)), (float) (-1.0d / Math.sqrt(2.0d)), 0.0f};
            for (int i = 0; i < 3; i++) assertEquals(e[i], p[i], 1.0E-6);
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int i = 0; i < 1000000; i++)
            {
                float[] a = new float[]{R.nextFloat(), R.nextFloat(), R.nextFloat()};
                float[] p = Vector.getPerpendicularVector3D(a);
                float sc = a[0] * p[0] + a[1] * p[1] + a[2] * p[2];
                assertEquals(0.0d, sc, 1.0E-6);
            }
        }
    }

    /**
     * Test "getInverseCrossProduct3D (double)".
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
            assertEquals(0.0d, b[0], 1.0E-6);
            assertEquals(0.0d, b[1], 1.0E-6);
            assertEquals(-1.0d, b[2], 1.0E-6);
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
                assertTrue(Vector.areVectorsEqual(nCC, nC, 1.0E-6));
            }
        }
    }

    /**
     * Test "getInverseCrossProduct3D (float)".
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
            assertEquals(0.0f, b[0], 1.0E-6);
            assertEquals(0.0f, b[1], 1.0E-6);
            assertEquals(-1.0f, b[2], 1.0E-6);
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
                assertTrue(Vector.areVectorsEqual(nCC, nC, 1.0E-6f));
            }
        }
    }


    /**
     * Test "getPointLineOrthogonalProjection".
     */
    @Test
    public void getPointLineOrthogonalProjection()
    {
        {
            double[] p = new double[]{0.0d, 2.0d};
            double[] l = new double[]{1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(1.0d, r[0], 1.0E-6);
            assertEquals(1.0d, r[1], 1.0E-6);
        }

        {
            double[] p = new double[]{0.0d, 1.0d};
            double[] l = new double[]{1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(0.5d, r[0], 1.0E-6);
            assertEquals(0.5d, r[1], 1.0E-6);
        }

        {
            double[] p = new double[]{0.0d, 3.0d, 0.0d};
            double[] l = new double[]{1.0d, 1.0d, 1.0d};
            double[] r = Vector.getPointLineOrthogonalProjection(p, l);
            assertEquals(1.0d, r[0], 1.0E-6);
            assertEquals(1.0d, r[1], 1.0E-6);
            assertEquals(1.0d, r[2], 1.0E-6);
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
                assertEquals(0.0d, dot, 1.0E-6);
                assertEquals(1.0d, cos, 1.0E-6);
            }
        }
    }

    /**
     * Test "getCombination".
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
                assertEquals(exp[i][0], c[0], 1.0E-6);
                assertEquals(exp[i][1], c[1], 1.0E-6);
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
                    assertTrue(Simplex.isOnSimplex(w3, 1.0E-6));
                }
            }
        }
    }

    /**
     * Test "thresholdAtOneFromAbove".
     */
    @Test
    public void thresholdAtOneFromAbove()
    {
        double[] a = new double[]{1.1d, 1.00000d};
        Vector.thresholdAtOneFromAbove(a);
        assertEquals(1.0d, a[0]);
        assertEquals(1.0d, a[1]);
    }

    /**
     * Test "thresholdAtZeroFromBelow".
     */
    @Test
    public void thresholdAtZeroFromBelow()
    {
        double[] a = new double[]{-0.00001d, 0.0d};
        Vector.thresholdAtZeroFromBelow(a);
        assertEquals(0.0d, a[0]);
        assertEquals(0.0d, a[1]);
    }


    /**
     * Test "getOrientation (2D; double)".
     */
    @Test
    public void getOrientation2DDouble()
    {
        assertEquals(0.0d, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d), 1.0E-6);
        assertEquals(Math.PI / 2.0d, Vector.getOrientation(0.0d, 0.0d, 0.0d, 1.0d), 1.0E-6);
        assertEquals(Math.PI, Vector.getOrientation(0.0d, 0.0d, -1.0d, 0.0d), 1.0E-6);
        assertEquals(Math.PI * 1.5d, Vector.getOrientation(0.0d, 0.0d, 0.0d, -1.0d), 1.0E-6);

        IRandom R = new MersenneTwister64(0);
        int T = 10000;
        for (int t = 0; t < T; t++)
        {
            double angle = 2.0d * Math.PI * R.nextDouble();
            double l = R.nextDouble();
            double xs = -1.0d + 2.0d * R.nextDouble();
            double ys = -1.0d + 2.0d * R.nextDouble();
            double xe = xs + Math.cos(angle) * l;
            double ye = ys + Math.sin(angle) * l;
            double orientation = Vector.getOrientation(xs, ys, xe, ye);
            assertEquals(angle, orientation, 1.0E-6);
        }
    }

    /**
     * Test "getOrientation (3D; double)".
     */
    @Test
    public void getOrientation3DDouble()
    {
        assertTrue(Vector.areVectorsEqual(new double[]{0.0d, 0.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{0.0d, 0.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{Math.PI, 0.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{0.0d, Math.PI / 2.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{0.0d, -Math.PI / 2.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d, -1.0d, 0.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{Math.PI / 2.0d, 0.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d)));
        assertTrue(Vector.areVectorsEqual(new double[]{Math.PI * 1.5d, 0.0d}, Vector.getOrientation(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -1.0d)));

        IRandom R = new MersenneTwister64(0);
        int T = 10000;
        for (int t = 0; t < T; t++)
        {
            double[][] cases = new double[][]
                    {
                            {0.0d, -Math.PI / 2.0d + R.nextDouble() * Math.PI},
                            {R.nextDouble() * 2.0d * Math.PI, 0.0d},
                            {R.nextDouble() * 2.0d, -Math.PI / 2.0d + Math.PI * R.nextDouble()},

                    };

            for (double[] as : cases)
            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                double xe = xs + Math.cos(as[0]) * Math.cos(as[1]) * l;
                double ye = ys + Math.cos(as[0]) * Math.sin(as[1]) * l;
                double ze = zs + Math.sin(as[0]) * l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xe, ye, ze);
                assertEquals(2, orientation.length);
                assertEquals(as[0], orientation[0], 1.0E-6);
                assertEquals(as[1], orientation[1], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == pi/2
                double ze = zs + l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xs, ys, ze);
                assertEquals(2, orientation.length);
                assertEquals(Math.PI / 2.0d, orientation[0], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == -pi/2
                double ze = zs - l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xs, ys, ze);
                assertEquals(2, orientation.length);
                assertEquals(Math.PI * 1.5d, orientation[0], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == 0
                // as[1] = 0
                double xe = xs + l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xe, ys, zs);
                assertEquals(2, orientation.length);
                assertEquals(0.0d, orientation[0], 1.0E-6);
                assertEquals(0.0d, orientation[1], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == 0
                // as[1] = pi
                double xe = xs - l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xe, ys, zs);
                assertEquals(2, orientation.length);
                assertEquals(Math.PI, orientation[0], 1.0E-6);
                assertEquals(0.0d, orientation[1], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == 0
                // as[1] = pi / 2
                double ye = ys + l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xs, ye, zs);
                assertEquals(2, orientation.length);
                assertEquals(0.0d, orientation[0], 1.0E-6);
                assertEquals(Math.PI / 2.0d, orientation[1], 1.0E-6);
            }

            {
                double l = R.nextDouble();
                double xs = -1.0d + 2.0d * R.nextDouble();
                double ys = -1.0d + 2.0d * R.nextDouble();
                double zs = -1.0d + 2.0d * R.nextDouble();
                // as[0] == 0
                // as[1] = - pi / 2
                double ye = ys - l;
                double[] orientation = Vector.getOrientation(xs, ys, zs, xs, ye, zs);
                assertEquals(2, orientation.length);
                assertEquals(0.0d, orientation[0], 1.0E-6);
                assertEquals(-Math.PI / 2.0d, orientation[1], 1.0E-6);
            }
        }
    }

    /**
     * Test "getOrientation (2D; float)".
     */
    @Test
    public void getOrientation2DFloat()
    {
        assertEquals(0.0f, Vector.getOrientation(0.0f, 0.0f, 0.0f, 0.0f), 1.0E-6f);
        assertEquals(Math.PI / 2.0f, Vector.getOrientation(0.0f, 0.0f, 0.0f, 1.0f), 1.0E-6f);
        assertEquals(Math.PI, Vector.getOrientation(0.0f, 0.0f, -1.0f, 0.0f), 1.0E-6f);
        assertEquals(Math.PI * 1.5f, Vector.getOrientation(0.0f, 0.0f, 0.0f, -1.0f), 1.0E-6);

        IRandom R = new MersenneTwister64(0);
        int T = 10000;
        for (int t = 0; t < T; t++)
        {
            double angle = 2.0d * Math.PI * R.nextDouble();
            double l = R.nextDouble();
            double xs = (-1.0d + 2.0d * R.nextDouble());
            double ys = (-1.0d + 2.0d * R.nextDouble());
            double xe = (xs + Math.cos(angle) * l);
            double ye = (ys + Math.sin(angle) * l);
            float orientation = Vector.getOrientation((float) xs, (float) ys, (float) xe, (float) ye);
            assertEquals(angle, orientation, 1.0E-3);

        }
    }


}