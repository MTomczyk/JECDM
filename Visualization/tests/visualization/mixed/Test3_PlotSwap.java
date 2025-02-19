package visualization.mixed;

import color.gradient.Color;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import scheme.WhiteScheme;

import java.util.ArrayList;

/**
 * Tests {@link plotswrapper.PlotsWrapperModel#replacePlotWith(int, AbstractPlot, boolean)}
 *
 * @author MTomczyk
 */
public class Test3_PlotSwap
{
    public static void main(String[] args)
    {
        Plot3D plot3D;
        {
            Plot3D.Params pP = new Plot3D.Params();
            pP._xAxisTitle = "X";
            pP._yAxisTitle = "Y";
            pP._zAxisTitle = "Z";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();
            pP._scheme = WhiteScheme.getForPlot3D();
            plot3D = new Plot3D(pP);
        }

        Plot2D plot2D;
        {
            Plot2D.Params pP = new Plot2D.Params();
            pP._xAxisTitle = "X";
            pP._yAxisTitle = "Y";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();
            pP._scheme = new WhiteScheme();
            plot2D = new Plot2D(pP);
        }

        Frame frame = new Frame(plot3D, 0.5f, 0.5f);
        frame.setVisible(true);

        {
            ArrayList<IDataSet> DSs = new ArrayList<>();
            DSs.add(DSFactory3D.getDS("DS1", new double[][]{{-1.0d, -1.0d, -1.0d},
                    {0.0d, 0.0d, 0.0d}, {1.0d, 1.0d, 1.0d}}, new MarkerStyle(0.1f, Color.RED, Marker.CUBE_3D)));
            plot3D.getModel().setDataSets(DSs);
        }

        AbstractPlot[] plots = new AbstractPlot[]{plot2D, plot3D};
        for (int i = 0; i < 20; i++)
        {
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            frame.getModel().getPlotsWrapper().getModel().replacePlotWith(0, plots[i % 2], false);
        }
    }
}
