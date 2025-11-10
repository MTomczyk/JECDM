package y2025.ERS.e1_auxiliary;

import color.gradient.Color;
import color.gradient.ColorPalettes;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import ea.EA;
import emo.utils.decomposition.goal.IGoal;
import model.constructor.Report;
import model.internals.value.scalarizing.LNorm;
import visualization.updaters.sources.EASource;

/**
 * Provides common methods.
 *
 * @author MTomczyk
 */
public class Common
{
    /**
     * Returns a data set associated with EA's population
     *
     * @param ea   ea
     * @param size marker size
     * @return data set
     */
    public static IDataSet getMethodDS(EA ea, float size)
    {
        EASource source = new EASource(ea);
        double[][] data = source.createData();
        return DSFactory3D.getDS("Population (size = " + data.length + ")",
                data, new MarkerStyle(size, ColorPalettes.getFromDefaultPalette(0), 0.5f,
                        Marker.SPHERE_HIGH_POLY_3D));
    }

    /**
     * Returns a data set associated with EA's population
     *
     * @param goals goals
     * @param size  marker size
     * @return data set
     */
    public static IDataSet getModelsDS(IGoal[] goals, float size)
    {
        double[][] data = new double[goals.length][];
        for (int i = 0; i < goals.length; i++) data[i] = goals[i].getParams()[0].clone();
        return DSFactory3D.getDS("Compatible weight vectors", data, new MarkerStyle(size,
                ColorPalettes.getFromDefaultPalette(0), Marker.SPHERE_HIGH_POLY_3D));
    }

    /**
     * Returns a data set associated with EA's population
     *
     * @param report FRS report
     * @param size   marker size
     * @return data set
     */
    public static IDataSet getModelsDS(Report<LNorm> report, float size)
    {
        double[][] data = new double[report._models.size()][];
        for (int i = 0; i < report._models.size(); i++) data[i] = report._models.get(i).getWeights().clone();
        int total = report._acceptedNewlyConstructedModels + report._rejectedNewlyConstructedModels;
        double nom = report._acceptedNewlyConstructedModels;
        double denom = (report._acceptedNewlyConstructedModels + report._rejectedNewlyConstructedModels);
        String sr = String.format("%.2f", (float) nom / denom * 100.0f).replace(',','.');
        String label = "Compatible weight vectors (no = " + report._models.size() + "; examined = " + total + "; SR = " + sr + "%)";
        return DSFactory3D.getDS(label, data, new MarkerStyle(size, ColorPalettes.getFromDefaultPalette(0), 0.01f,
                Marker.SPHERE_HIGH_POLY_3D));
    }


    /**
     * Returns a data set associated with EA's population
     *
     * @param pareto Pareto optimal points for DTLZ2
     * @return data set
     */
    public static IDataSet getParetoDS(double[][] pareto)
    {
        IDataSet ds = DSFactory3D.getDS("Pareto front", pareto,
                new MarkerStyle(0.01f, Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D));
        ds.setDisplayableOnLegend(false);
        return ds;
    }

    /**
     * Returns a data set associated with a sampled model.
     *
     * @param sampled sampled model
     * @return data set
     */
    public static IDataSet getSampledModelDS(LNorm sampled)
    {
        if (sampled == null) return DSFactory3D.getDS("Sampled model", (double[][]) null,
                new MarkerStyle(0.025f, new Color(0.95f, 0.6f, 0.01f), Marker.CUBE_3D,
                        new LineStyle(1.0f, Color.BLACK, 0.01f, Line.REGULAR), 0.01f));

        double[][] data = new double[][]{sampled.getWeights().clone()};
        return DSFactory3D.getDS("Sampled model",
                data, new MarkerStyle(0.025f, new Color(0.95f, 0.6f, 0.01f), Marker.CUBE_3D,
                        new LineStyle(1.0f, Color.BLACK, 0.01f, Line.REGULAR), 0.01f));
    }

    /**
     * Returns a boundary DS
     *
     * @return data set
     */
    public static IDataSet getBoundaryDS()
    {


        double[][] data = new double[][]{

                //{0.5d, 0.0d, 0.5d},
                //{0.273467262, 0.0d, 0.726532738},

                {1.0d / 3.0d, 1.0d / 3.0d, 1.0d / 3.0d},
                {0.208906512, 0.394833307, 0.394833307},

                {1.0d / 3.0d, 1.0d / 3.0d, 1.0d / 3.0d},
                {0.376824441, 0.246351118, 0.376824441},

                {0.18028592, 0.34074039, 0.47897369},
                {0.208906512, 0.394833307, 0.394833307},

                {0.18028592, 0.34074039, 0.47897369},
                {0.221133423, 0.191371495, 0.587495082}, //ere

                {0.273993138, 0.17912457, 0.546882292}, //here
                {0.376824441, 0.246351118, 0.376824441},

                {0.221133423, 0.191371495, 0.587495082},
                {0.273993138, 0.17912457, 0.546882292},

        };
        return DSFactory3D.getDS("Boundary of compatible weight space",
                data, new LineStyle(0.01f, ColorPalettes.getFromDefaultPalette(3),
                        Line.POLY_OCTO), null, false, true);
    }
}
