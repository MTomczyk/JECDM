package updater;

import random.IRandom;

/**
 * This implementation of IDataSource generates a specified number of data points upon request. These points are of
 * requested dimensionality (parameter), and their attributes are drawn randomly from the Gaussian distribution with
 * means and standard deviations provided as parameters.
 *
 * @author MTomczyk
 */
public class GaussianGenerator extends AbstractSource implements IDataSource
{
    /**
     * The number of points to generate when requested.
     */
    private final int _n;

    /**
     * Space dimensionality.
     */
    private final int _m;

    /**
     * Means (i-th element is linked to i-th dimension).
     */
    private final double[] _means;

    /**
     * Standard deviations (i-th element is linked to i-th dimension).
     */
    private final double[] _stds;

    /**
     * Random number generator.
     */
    private final IRandom _R;

    /**
     * Parameterized constructor.
     *
     * @param n     the number of points to generate when requested
     * @param m     space dimensionality
     * @param means means (i-th element is linked to i-th dimension)
     * @param stds  standard deviations (i-th element is linked to i-th dimension)
     * @param R     random number generator
     */
    public GaussianGenerator(int n, int m, double[] means, double[] stds, IRandom R)
    {
        _n = n;
        _m = m;
        _means = means;
        _stds = stds;
        _R = R;
    }

    /**
     * Creates new data and returns it (random points drawn from the Gaussian distribution).
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        double[][] p = new double[_n][_m];
        for (int i = 0; i < _n; i++)
            for (int j = 0; j < _m; j++)
                p[i][j] = _means[j] + _R.nextGaussian() * _stds[j];
        return p;
    }
}

