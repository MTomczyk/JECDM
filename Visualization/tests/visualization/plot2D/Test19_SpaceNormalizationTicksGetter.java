package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Tests various axes ticks locations / labels / and rendering space normalizers.
 *
 * @author MTomczyk
 */
public class Test19_SpaceNormalizationTicksGetter
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = false;
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), true, false, new Linear());
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), true, false, new Linear());
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false, new Gamma(0.2f));
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        ArrayList<IDataSet> dss = new ArrayList<>();
        {
            int points = 10000;
            double[][] data = new double[points][3];
            IRandom R = new MersenneTwister64(1);
            for (int i = 0; i < points; i++)
            {
                data[i][0] = R.nextGaussian() * 0.35f;
                data[i][1] = R.nextGaussian() * 0.35f;
                data[i][2] = Math.sqrt(data[i][0] * data[i][0] + data[i][1] * data[i][1]);
            }
            MarkerStyle ms = new MarkerStyle(1.0f, Gradient.getViridisGradient(100, false),
                    2, Marker.SQUARE);
            LineStyle ls = null;
            IDataSet ds = DataSet.getFor2D("data", data, ms, ls);
            dss.add(ds);
        }

        pP._colorbar = new Colorbar(Gradient.getViridisGradient(10, false), "colorbar",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 10));

        Plot2D plot = new Plot2D(pP);
        plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.5f, 0.0f, 1.0f});
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.5f, 0.0f, 1.0f});
        DrawingArea2D d2 = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();
        d2.getMainGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 10));
        d2.getMainGrid().setVerticalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[0], 10));
        d2.getAuxGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 10));
        d2.getAuxGrid().setVerticalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[0], 10));
        d2.getMainGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.5f, 0.0f, 1.0f});
        d2.getMainGrid().getVerticalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.5f, 0.0f, 1.0f});
        d2.getAuxGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.25f, 0.5f});
        d2.getAuxGrid().getVerticalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{-0.25f, 0.5f});

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";
        Frame frame = new Frame(pF);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        plot.getModel().setDataSets(dss, true);
        frame.setVisible(true);
    }
}
