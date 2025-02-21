package combinatorics;

/**
 * Provides various auxiliary methods.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Calculates the binomial coefficient (Newton's symbol), i.e., n!/(k!(n - k))!
     * The method returns 0 if k &gt; n or n, k &lt; 0.
     * @param n n-value
     * @param k k-value
     * @return result (0 if k &gt; n or n, k &lt; 0)
     */
    public static int calculateBinomialCoefficient(int n, int k)
    {
        if (k > n) return 0;
        if (k < 0) return 0;
        if (n == k) return 1;
        if (k == 0) return 1;

        int max = Math.max(k, n - k);
        int min = Math.min(k , n - k);
        int nom = 1;
        for (int i = max + 1; i <= n; i++) nom *= i;
        int denom = 1;
        for (int i = 1; i <= min; i++) denom *= i;
        return nom / denom;
    }
}
