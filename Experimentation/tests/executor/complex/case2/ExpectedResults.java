package executor.complex.case2;

/**
 * Provides expected data for this case.
 *
 * @author MTomczyk
 */
class ExpectedResults
{
    /**
     * Case trials = 4, population size = 3, generations = 10, evaluations = 3, min 0.
     */
    protected static final double[][] _t4ps3gen10e3_min0 = {
            {0.400000000, 1.318408910, 2.333333333},
            {-0.416146837, 0.336570169, 1.166666667},
            {-0.989992497, -0.095392478, 0.777777778},
            {-0.653643621, -0.044598324, 0.583333333},
            {-2.000000000, -0.212417787, 0.466666667},
            {-4.200000000, -0.629401873, 0.960170287},
            {-6.800000000, -1.440095865, 0.753902254},
            {-9.800000000, -2.673875008, 0.250000000},
            {-13.200000000, -3.972227010, 0.222222222},
            {-17.000000000, -5.159767882, 0.200000000},
    };

    /**
     * Case trials = 4, population size = 3, generations = 10, evaluations = 3, min 0.
     */
    protected static final double[][] _t4ps3gen10e3_sum1 = {
            {5.000000000, 13.943916043, 20.866366745},
            {2.500000000, 7.539374447, 11.000000000},
            {1.000000000, 7.738772008, 14.333333333},
            {-0.250000000, 9.479098344, 20.500000000},
            {-1.400000000, 11.437594942, 29.000000000},
            {-2.500000000, 14.642272083, 39.666666667},
            {-3.571428571, 18.752753748, 52.428571429},
            {-4.625000000, 22.780955738, 67.250000000},
            {-5.666666667, 27.788852632, 84.111111111},
            {-6.700000000, 33.837144901, 103.000000000}
    };

    /**
     * Case trials = 4, population size = 2, generations = 5, evaluations = 3, min 0.
     */
    protected static final double[][] _t4ps2gen5e3_mean2_full = {
            {1.000000000, 15.515519434, 49.791926582},
            {2.000000000, 3.772526193, 5.791926582},
            {1.097369094, 3.483926545, 6.005003752},
            {0.122562483, 3.511435168, 6.673178190},
            {-0.339535765, 3.750573832, 7.641831093}
    };


    /**
     * Case trials = 4, population size = 2, generations = 5, evaluations = 3, min 0.
     */
    protected static final double[][] _t4ps2gen5e3_mean2_twoStats = {
            {1.000000000, 49.791926582},
            {2.000000000, 5.791926582},
            {1.097369094, 6.005003752},
            {0.122562483, 6.673178190},
            {-0.339535765, 7.641831093}
    };

    /**
     * Expected final results (variant 6_10).
     */
    public final static Double[][][] _expectedFinalResults6_10 = {

            {
                    {-0.339535765, 7.641831093, null, null, null, null},
                    {null, null, null, null, null, null}
            },

            {
                    {null, null, null, null, null, null},
                    {null, null, null, -17.000000000, 0.200000000, -5.159767882}
            },
            {
                    {null, null, null, null, null, null},
                    {null, null, null, -6.700000000, 103.000000000, 33.837144901}
            },

    };

    /**
     * Expected final results (variant 9).
     */
    public final static Double[][][] _expectedFinalResults9 = {

            {
                    {null, null, null, null, null, null},
                    {null, null, null, -17.000000000, -5.159767882, 0.200000000}
            },
            {
                    {null, null, null, null, null, null},
                    {null, null, null, -6.700000000, 33.837144901, 103.000000000}
            },
            {
                    {-0.339535765, null, 7.641831093, null, null, null},
                    {null, null, null, null, null, null}
            },
    };


    /**
     * Expected final results (variant 12).
     */
    public final static Double[][][] _expectedFinalResults11_12 = {

            {
                    {-17.000000000, -5.159767882, 0.200000000, null, null, null},
                    {null, null, null, null, null, null}
            },
            {
                    {-6.700000000, 33.837144901, 103.000000000, null, null, null, null, null, null},
                    {null, null, null, null, null, null}
            },
            {
                    {null, null, null, null, null, null},
                    {null, null, null, -0.339535765, null, 7.641831093}
            },

    };


    /**
     * Expected final results (variant 15).
     */
    public final static Double[][][] _expectedFinalResults15 = {

            {
                    {null, null, null, null, 4.0d, null, 1.0d, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, null, null, null, 4.0d, null, 1.0d, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, 4.0d, null, 1.0d, null, null, null, null},
            },

    };
}
