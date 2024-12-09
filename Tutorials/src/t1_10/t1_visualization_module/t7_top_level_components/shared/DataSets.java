package t1_10.t1_visualization_module.t7_top_level_components.shared;

import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import random.IRandom;

/**
 * Provides factory methods for constructing pre-defined data sets.
 *
 * @author MTomczyk
 */
public class DataSets
{
    /**
     * This method creates a data set consisting of points being randomly drawn from the Gaussian distribution.
     *
     * @param name data set name
     * @param ms   marker style
     * @param std  standard deviation for the Gaussian distribution
     * @param R    random number generator
     * @return data set
     */
    public static IDataSet getDataSetFor2DGaussianDistribution(String name, MarkerStyle ms, float std, IRandom R)
    {
        int noSamples = 100000;
        double[][] data = new double[noSamples][3];
        for (int i = 0; i < noSamples; i++)
        {
            double x = R.nextGaussian() * std;
            double y = R.nextGaussian() * std;
            double d = Math.sqrt(x * x + y * y);
            data[i][0] = x;
            data[i][1] = y;
            data[i][2] = d;
        }
        return dataset.DataSet.getFor2D(name, data, ms);
    }

    /**
     * This method creates a data set consisting of points being randomly drawn from the Gaussian distribution.
     *
     * @param name data set name
     * @param ms   marker style
     * @param std  standard deviation for the Gaussian distribution
     * @param R    random number generator
     * @return data set
     */
    public static IDataSet getDataSetFor3DGaussianDistribution(String name, MarkerStyle ms, float std, IRandom R)
    {
        int noSamples = 100000;
        double[][] data = new double[noSamples][4];
        for (int i = 0; i < noSamples; i++)
        {
            double x = R.nextGaussian() * std;
            double y = R.nextGaussian() * std;
            double z = R.nextGaussian() * std;
            double d = Math.sqrt(x * x + y * y + z * z);
            data[i][0] = x;
            data[i][1] = y;
            data[i][2] = z;
            data[i][3] = d;
        }
        return dataset.DataSet.getFor3D(name, data, ms);
    }
}
