package t1_10.t1_visualization_module.t2_plot3d.t4;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot (colorbar and scheme customization).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial4
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();

        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true);

        // Let's specify some arbitrarily selected normalization functions for each dimension:
        pP._pDisplayRangesManager._DR[0].setNormalizer(new Gamma(2.0f));
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Gamma(4.5f));
        pP._pDisplayRangesManager._DR[2].setNormalizer(new Gamma(0.5f));
        pP._pDisplayRangesManager._DR[3].setNormalizer(new Gamma(0.75d));

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";
        pP._zAxisTitle = "Z-coordinate";

        pP._scheme = WhiteScheme.getForPlot3D(0.3f);
        pP._scheme._sizes.put(SizeFields.COLORBAR_SHRINK, 0.66f);

        pP._useAlphaChannel = true;
        pP._colorbar = new Colorbar(Gradient.getPlasmaGradient(), "Distance",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));

        Plot3D plot = new Plot3D(pP);

        ArrayList<IDataSet> dataSets = new ArrayList<>(4);
        {
            int noSamples = 100000;
            IRandom R = new MersenneTwister64(0);
            double[][] data = new double[noSamples][4];
            for (int i = 0; i < noSamples; i++)
            {
                double x = R.nextGaussian() * 0.25d;
                double y = R.nextGaussian() * 0.25d;
                double z = R.nextGaussian() * 0.25d;
                double d = Math.sqrt(x * x + y * y + z * z);
                data[i][0] = x;
                data[i][1] = y;
                data[i][2] = z;
                data[i][3] = d;
            }

            // Specify the gradient and the display range association
            MarkerStyle ms = new MarkerStyle(0.01f, Gradient.getPlasmaGradient(), 3, Marker.SPHERE_LOW_POLY_3D);
            dataSets.add(DataSet.getFor3D("DS1", data, ms));
        }

        // Axes and grids can be customized in the following way:
        NumberFormat format = new DecimalFormat();
        ITicksDataGetter xTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[0],5, format);
        xTDG.setForcedUnnormalizedLocations(new float[]{-1.0f, -0.5f, 0.0f, 0.5f, 1.0f});
        ITicksDataGetter yTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[1],5, format);
        yTDG.setForcedUnnormalizedLocations(new float[]{-1.0f, -0.5f, 0.0f, 0.5f, 1.0f});
        ITicksDataGetter zTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[2],5, format);
        zTDG.setForcedUnnormalizedLocations(new float[]{-1.0f, -0.5f, 0.0f, 0.5f, 1.0f});
        ITicksDataGetter cbTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[3],5, format);
        cbTDG.setForcedUnnormalizedLocations(new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f});
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setGridlinesTicksDataGetters(xTDG, yTDG, zTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForXAxes(xTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForYAxes(yTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForZAxes(zTDG);
        plot.getComponentsContainer().getColorbar().getAxis().setTicksDataGetter(cbTDG);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 1400, 1200);
        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
