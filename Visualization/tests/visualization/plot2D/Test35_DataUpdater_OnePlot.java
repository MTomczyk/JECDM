package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import updater.CyclicDataSource;
import updater.DataUpdater;
import updater.IDataSource;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test35_DataUpdater_OnePlot
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
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = false;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "distance",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        int sets = 1000;
        int points = 1000;
        double[][][] data = new double[sets][points][3];
        IRandom R = new MersenneTwister64(1);

        for (int s = 0; s < sets; s++)
        {
            float d = (float) s / (sets - 1);
            d /= 4.0f;
            for (int i = 0; i < points; i++)
            {
                data[s][i][0] = R.nextGaussian() * d;
                data[s][i][1] = R.nextGaussian() * d;
                data[s][i][2] = Math.sqrt(data[s][i][0] * data[s][i][0] + data[s][i][1] * data[s][i][1]);
            }
        }

        IDataSource source = new CyclicDataSource(data);
        IDataSet reference = DataSet.getFor2D("data set", new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.SQUARE));

        DataUpdater DU;
        try
        {
            DU = DataUpdater.getSimpleDataUpdater(frame.getModel().getPlotsWrapper(), source,reference);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }


        frame.setVisible(true);
        plot.getModel().notifyDisplayRangesChangedListeners();

        for (int i = 0; i < 50000; i++)
        {
            if (frame.isTerminating()) break;
            DU.update();
            try
            {
                Thread.sleep(5);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
