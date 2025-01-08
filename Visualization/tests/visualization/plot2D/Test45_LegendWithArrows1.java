package visualization.plot2D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory2D;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.NumberFields;
import space.Range;

import java.util.ArrayList;

/**
 * Test the legend.
 *
 * @author MTomczyk
 */
public class Test45_LegendWithArrows1
{
    /**
     * Runs the test.
     *
     * @param args not used.
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._xAxisTitle = "X";
        pP._yAxisTitle = "Y";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._scheme = new WhiteScheme();
        pP._scheme._numbers.put(NumberFields.LEGEND_NO_ENTRIES_PER_COLUMN_LIMIT, 10);
        pP._drawLegend = true;
        Plot2D plot2D = new Plot2D(pP);

        Frame frame = new Frame(plot2D, 0.5f);

        ArrayList<IDataSet> dataSets = new ArrayList<>();
        dataSets.add(DSFactory2D.getReferenceDS("DS1", new MarkerStyle(5.0f, Color.BLACK, Marker.SQUARE)));
        dataSets.add(DSFactory2D.getReferenceDS("DS2", new MarkerStyle(5.0f, Color.BLACK, Marker.SQUARE,
                new LineStyle(1.0f, Color.RED))));
        dataSets.add(DSFactory2D.getReferenceDS("DS3", new MarkerStyle(10.0f, Gradient.getViridisGradient(),
                0, Marker.CIRCLE)));

        dataSets.add(DSFactory2D.getReferenceDS("DS4", new LineStyle(2.0f, Color.BLUE)));
        dataSets.add(DSFactory2D.getReferenceDS("DS5", new LineStyle(3.0f, Color.RED)));
        dataSets.add(DSFactory2D.getReferenceDS("DS5", new LineStyle(4.0f, Gradient.getPlasmaGradient(), 0)));

        dataSets.add(DSFactory2D.getReferenceDS("DS6", new MarkerStyle(10.0f, Color.BLACK, Marker.SQUARE),
                new LineStyle(4.0f, Gradient.getPlasmaGradient(), 0)));
        dataSets.add(DSFactory2D.getReferenceDS("DS7", new MarkerStyle(10.0f, Gradient.getRedBlueGradient(), 0, Marker.CIRCLE),
                new LineStyle(2.0f, Gradient.getPlasmaGradient(), 0)));

        dataSets.add(DSFactory2D.getReferenceDS("DS8", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(new ArrowStyle(4.0f, 6.0f, Color.BLUE, Arrow.TRIANGULAR_2D), null)));
        dataSets.add(DSFactory2D.getReferenceDS("DS9", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(new ArrowStyle(4.0f, 6.0f, Gradient.getRedBlueGradient(), 0, Arrow.TRIANGULAR_2D), null)));
        dataSets.add(DSFactory2D.getReferenceDS("DS10", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(new ArrowStyle(4.0f, 6.0f, Color.BLUE, Arrow.TRIANGULAR_REVERSED_2D), null)));
        dataSets.add(DSFactory2D.getReferenceDS("DS11", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(new ArrowStyle(2.0f, 6.0f, Gradient.getRedBlueGradient(), 0, Arrow.TRIANGULAR_REVERSED_2D), null)));

        dataSets.add(DSFactory2D.getReferenceDS("DS12", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(null, new ArrowStyle(8.0f, 4.0f, Color.RED, Arrow.TRIANGULAR_REVERSED_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS13", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(null, new ArrowStyle(5.0f, 5.0f, Gradient.getViridisGradient(),
                        0, Arrow.TRIANGULAR_REVERSED_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS14", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(null, new ArrowStyle(8.0f, 4.0f, Color.RED, Arrow.TRIANGULAR_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS15", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(null, new ArrowStyle(5.0f, 5.0f, Gradient.getViridisGradient(),
                        0, Arrow.TRIANGULAR_2D))));

        dataSets.add(DSFactory2D.getReferenceDS("DS16", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(
                        new ArrowStyle(4.0f, 4.0f, Gradient.getRedBlueGradient(), 0, Arrow.TRIANGULAR_2D),
                        new ArrowStyle(5.0f, 5.0f, Gradient.getViridisGradient(), 0, Arrow.TRIANGULAR_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS17", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(
                        new ArrowStyle(4.0f, 4.0f, Color.BLUE, Arrow.TRIANGULAR_2D),
                        new ArrowStyle(5.0f, 5.0f, Color.RED, Arrow.TRIANGULAR_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS18", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(
                        new ArrowStyle(4.0f, 4.0f, Gradient.getRedBlueGradient(), 0, Arrow.TRIANGULAR_2D),
                        new ArrowStyle(5.0f, 5.0f, Color.RED, Arrow.TRIANGULAR_2D))));
        dataSets.add(DSFactory2D.getReferenceDS("DS19", new LineStyle(2.0f, Color.GREEN),
                new ArrowStyles(new ArrowStyle(5.0f, 5.0f, Color.RED, Arrow.TRIANGULAR_2D),
                        new ArrowStyle(4.0f, 4.0f, Gradient.getRedBlueGradient(), 0, Arrow.TRIANGULAR_2D))));

        plot2D.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
