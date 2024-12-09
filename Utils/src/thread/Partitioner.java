package thread;

/**
 * Auxiliary methods for generating begin-ends for parallelized threads working on the same array-like data.
 *
 * @author MTomczyk
 */

public class Partitioner
{
    /**
     * Generating ends for parallelized threads working on the same array-like data.
     *
     * @param l array length
     * @param t number of threads
     * @return array whose elements are ends [begin1, end1, begin2, end2, ...] per every thread (left sides are closed, right sides are open)
     */
    public static int[] generatePartitions(int l, int t)
    {
        int[] a = new int[t * 2];

        float ratio = (float) l / t;

        a[0] = 0;
        int p = 0;

        for (int i = 1; i < t; i++)
        {
            int c = (int) (ratio * i + 0.5f);
            if (c <= p) c = p + 1;
            p = c;

            if (c <= l) a[2 * i - 1] = c;
            else a[2 * i - 1] = -1;

            if (c >= l) c = -1;
            a[2 * i] = c;
        }
        if (a[a.length - 2] == -1) a[a.length - 1] = -1;
        else a[a.length - 1] = l;

        return a;
    }

    /**
     * Returns a valid number of threads that should process the input array.
     * @param p begin-ends array (see generatePartitions() method)
     * @param t the expected number of threads (upper limit)
     * @return a valid number of threads that should process the input array
     */
    public static int getValidNoThreads(int[] p, int t)
    {
        int vn = 0;
        for (int i = 0; i < t; i++)
            if (p[2 * i] != -1) vn++;
            else break;
        return vn;
    }
}
