package space;

import org.junit.jupiter.api.Test;
import statistics.incrementalcontainer.IncrementalContainerFloat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for {@link space.VectorPathInterpolation} class.
 *
 * @author MTomczyk
 */
class VectorPathInterpolationTest
{
    /**
     * Checks if two float arrays are equal.
     *
     * @param exp expected result
     * @param ans obtained result
     */
    private void compare(float[] exp, float[] ans)
    {
        assertEquals(exp.length, ans.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], ans[i], 0.0001f);
    }

    /**
     * Test 1.
     */
    @Test
    void addPoint1()
    {
        VectorPathInterpolation V = new VectorPathInterpolation(3);
        float [] p = V.getInterpolatedPosition(1);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(0, 0, 0);
        p = V.getInterpolatedPosition(1);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(1, 2, 1);
        p = V.getInterpolatedPosition(2);
        compare(new float[]{2.0f, 4.0f}, p);

        V.addPoint(-1, -23, 1); // SKIPPED
        p = V.getInterpolatedPosition(2);
        compare(new float[]{2.0f, 4.0f}, p);


        V.addPoint(2, 3, 2);

        IncrementalContainerFloat[] vx = V.getVX();
        IncrementalContainerFloat[] vy = V.getVY();
        assertEquals(2, vx.length);
        assertEquals(2, vy.length);

        compare(new float[] {1.0f, 1.0f}, vx[0]._data);
        compare(new float[] {2.0f, 1.0f}, vy[0]._data);

        compare(new float[] {0.0f}, vx[1]._data);
        compare(new float[] {-1.0f}, vy[1]._data);

        p = V.getInterpolatedPosition(4);
        compare(new float[]{4.0f, 3.0f}, p);
    }

    /**
     * Test 2.
     */
    @Test
    void addPoint2()
    {
        VectorPathInterpolation V = new VectorPathInterpolation(3, 0.01f);
        float [] p = V.getInterpolatedPosition(100);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(0, 0, 0);
        p = V.getInterpolatedPosition(100);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(1, 2, 100);
        p = V.getInterpolatedPosition(200);
        compare(new float[]{2.0f, 4.0f}, p);

        V.addPoint(-1, -23, 100); // SKIPPED
        p = V.getInterpolatedPosition(200);
        compare(new float[]{2.0f, 4.0f}, p);


        V.addPoint(2, 3, 200);

        IncrementalContainerFloat[] vx = V.getVX();
        IncrementalContainerFloat[] vy = V.getVY();
        assertEquals(2, vx.length);
        assertEquals(2, vy.length);

        compare(new float[] {1.0f, 1.0f}, vx[0]._data);
        compare(new float[] {2.0f, 1.0f}, vy[0]._data);
        compare(new float[] {0.0f}, vx[1]._data);
        compare(new float[] {-1.0f}, vy[1]._data);
        p = V.getInterpolatedPosition(400);
        compare(new float[]{4.0f, 3.0f}, p);
    }

    /**
     * Test 3.
     */
    @Test
    void addPoint3()
    {
        VectorPathInterpolation V = new VectorPathInterpolation(2);
        float [] p = V.getInterpolatedPosition(1);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(0, 0, 0);
        p = V.getInterpolatedPosition(1);
        compare(new float[]{0.0f, 0.0f}, p);

        V.addPoint(2, 1, 4);
        p = V.getInterpolatedPosition(9);
        compare(new float[]{4.5f, 2.25f}, p);

        IncrementalContainerFloat[] vx = V.getVX();
        IncrementalContainerFloat[] vy = V.getVY();
        assertEquals(1, vx.length);
        assertEquals(1, vy.length);

        compare(new float[] {0.5f}, vx[0]._data);
        compare(new float[] {0.25f}, vy[0]._data);
    }

}