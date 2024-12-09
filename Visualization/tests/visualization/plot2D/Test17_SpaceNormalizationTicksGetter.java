package visualization.plot2D;

import color.gradient.Color;
import component.drawingarea.DrawingArea2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import space.normalization.minmax.LinearWithFlip;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Tests various axes ticks locations / labels / and rendering space normalizers.
 *
 * @author MTomczyk
 */
public class Test17_SpaceNormalizationTicksGetter
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
        pP._scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        pP._scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d), new Range(0.0d, 2.0d));
        pP._pDisplayRangesManager._DR[0].setNormalizer(new Gamma(-1.0d, 1.0d, 2.0f));
        pP._pDisplayRangesManager._DR[1].setNormalizer(new LinearWithFlip(0.0d, 2.0d, 1.0d));

        double [][] D = new double[100][2];
        for (int i = 0; i < 100; i++)
        {
            D[i][0] = -1.0d + 2.0d * (i / 99.0d);
            D[i][1] = 2.0d * (i / 99.0d);
        }

        IDataSet ds = DataSet.getFor2D("DS 1", D, new MarkerStyle(2, Color.RED, Marker.SQUARE));
        ArrayList<IDataSet> dss = new ArrayList<>();
        dss.add(ds);

        Plot2D plot = new Plot2D(pP); // can be adjusted before frame is constructed
        plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setForcedNormalizedLocations(new float[]{0.0f, 0.4f, 0.7f, 0.9f, 1.0f});
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setForcedNormalizedLocations(new float[]{0.0f, 1.0f/3.0f, 2.0f/3.0f, 1.0f});
        DrawingArea2D d2d = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();
        d2d.getMainGrid().getHorizontalTicksDataGetter().setForcedNormalizedLocations(new float[]{0.0f, 1.0f/3.0f, 2.0f/3.0f, 1.0f});
        d2d.getMainGrid().getVerticalTicksDataGetter().setForcedNormalizedLocations(new float[]{0.0f, 0.4f, 0.7f, 0.9f, 1.0f});
        d2d.getAuxGrid().getHorizontalTicksDataGetter().setForcedNormalizedLocations(new float[]{0.5f/3.0f, 1.5f/3.0f, 2.5f/3.0f});
        d2d.getAuxGrid().getVerticalTicksDataGetter().setForcedNormalizedLocations(new float[]{0.2f, 0.55f, 0.8f, 0.95f});

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";
        Frame frame = new Frame(pF);

        plot.getModel().setDataSets(dss, true);
        frame.setVisible(true);
    }
}
