package executor.complex.case3;

/**
 * Provides expected data for this case.
 *
 * @author MTomczyk
 */
class ExpectedResults34
{
    /**
     * Sheets' names (level 3).
     */
    protected static String[] _sheets3 = {"MEAN0", "MEAN1", "MEAN2"};

    /**
     * Sheets' names (level 2; fixed gen 10).
     */
    protected static String[] _sheetsFixedGen10 = {"MEAN0", "MEAN1", "MEAN2"};

    /**
     * Sheets' names (level 2; fixed gen 20).
     */
    protected static String[] _sheetsFixedGen20 = {"MEAN0", "MEAN1", "MEAN2"};

    /**
     * Sheets' names (level 2; fixed PS 2 gen 10).
     */
    protected static String[] _sheetsFixedPS2Gen10 = {"MEAN0", "MEAN1"};

    /**
     * Sheets' names (level 2; fixed PS 2 gen 20).
     */
    protected static String[] _sheetsFixedPS2Gen20 = {"MEAN0", "MEAN1"};

    /**
     * Sheets' names (level 2; fixed PS 2 gen 10).
     */
    protected static String[] _sheetsFixedPS3Gen10 = {"MEAN0", "MEAN1", "MEAN2"};

    /**
     * Sheets' names (level 2; fixed PS 2 gen 20).
     */
    protected static String[] _sheetsFixedPS3Gen20 = {"MEAN0", "MEAN1", "MEAN2"};


    /**
     * Results for the ranker (level 3).
     */
    protected static Double[][][] _expectedRanks3 = {
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.790661597, null, 0.875000000, null, 0.248213079},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.790661597, null, 0.875000000, null, 0.248213079, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, 1.000000000, null, 0.000000000, 1.000000000, 3.000000000, 1.750000000, null, 0.345920793, null, 0.375000000, null, 0.386476231},
                    {3.000000000, null, 0.000000000, null, 3.000000000, 1.000000000, 1.250000000, 0.345920793, null, 0.375000000, null, 0.386476231, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.420347942, null, 0.625000000, null, 0.148914673},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.420347942, null, 0.625000000, null, 0.148914673, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, 2.000000000, null, 0.000000000, 2.000000000, 2.000000000, 1.500000000, null, 0.569976560, null, 0.625000000, null, 0.772829993},
                    {2.000000000, null, 0.000000000, null, 2.000000000, 2.000000000, 1.500000000, 0.569976560, null, 0.625000000, null, 0.772829993, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.613826083, null, 0.625000000, null, 0.563702862},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.613826083, null, 0.625000000, null, 0.563702862, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
    };

    /**
     * Results for the ranker (level 2; fixed gen 10).
     */
    protected static Double[][][] _expectedRanksFixedGen10 = {
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, 1.000000000, null, 0.000000000, 1.000000000, 3.000000000, 1.750000000, null, 0.345920793, null, 0.375000000, null, 0.386476231},
                    {3.000000000, null, 0.000000000, null, 3.000000000, 1.000000000, 1.250000000, 0.345920793, null, 0.375000000, null, 0.386476231, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, 2.000000000, null, 0.000000000, 2.000000000, 2.000000000, 1.500000000, null, 0.569976560, null, 0.625000000, null, 0.772829993},
                    {2.000000000, null, 0.000000000, null, 2.000000000, 2.000000000, 1.500000000, 0.569976560, null, 0.625000000, null, 0.772829993, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
    };

    /**
     * Results for the ranker (level 2; fixed gen 20).
     */
    protected static Double[][][] _expectedRanksFixedGen20 = {
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.790661597, null, 0.875000000, null, 0.248213079},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.790661597, null, 0.875000000, null, 0.248213079, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.420347942, null, 0.625000000, null, 0.148914673},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.420347942, null, 0.625000000, null, 0.148914673, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.613826083, null, 0.625000000, null, 0.563702862},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.613826083, null, 0.625000000, null, 0.563702862, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
    };

    /**
     * Results for the ranker (level 1; fixed ps 2 gen 10).
     */
    protected static Double[][][] _expectedRanksFixedPS2Gen10 = {
            {
                    {null, 1.000000000, null, 0.000000000, 1.000000000, 3.000000000, 1.750000000, null, 0.345920793, null, 0.375000000, null, 0.386476231},
                    {3.000000000, null, 0.000000000, null, 3.000000000, 1.000000000, 1.250000000, 0.345920793, null, 0.375000000, null, 0.386476231, null},
            },
            {
                    {null, 2.000000000, null, 0.000000000, 2.000000000, 2.000000000, 1.500000000, null, 0.569976560, null, 0.625000000, null, 0.772829993},
                    {2.000000000, null, 0.000000000, null, 2.000000000, 2.000000000, 1.500000000, 0.569976560, null, 0.625000000, null, 0.772829993, null},
            },
    };

    /**
     * Results for the ranker (level 1; fixed ps 2 gen 20).
     */
    protected static Double[][][] _expectedRanksFixedPS2Gen20 = {
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
    };

    /**
     * Results for the ranker (level 1; fixed ps 3 gen 10).
     */
    protected static Double[][][] _expectedRanksFixedPS3Gen10 = {
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            {
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
    };

    /**
     * Results for the ranker (level 1; fixed ps 3 gen 20).
     */
    protected static Double[][][] _expectedRanksFixedPS3Gen20 = {
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.790661597, null, 0.875000000, null, 0.248213079},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.790661597, null, 0.875000000, null, 0.248213079, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.420347942, null, 0.625000000, null, 0.148914673},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.420347942, null, 0.625000000, null, 0.148914673, null},
            },
            {
                    {null, 3.000000000, null, 0.000000000, 3.000000000, 1.000000000, 1.250000000, null, 0.613826083, null, 0.625000000, null, 0.563702862},
                    {1.000000000, null, 0.000000000, null, 1.000000000, 3.000000000, 1.750000000, 0.613826083, null, 0.625000000, null, 0.563702862, null},
            },
    };


}
