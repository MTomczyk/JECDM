package visualization.plot2D;

import color.gradient.Gradient;
import component.drawingarea.DrawingArea2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test39_Gradients
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
        pP._drawLegend = true;
        pP._drawAuxGridlines = false;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(
                new Range(0.0d, 1.0d), false, false,
                new Range(0.0d, 1.0d), true, false
        );


        Plot2D plot = new Plot2D(pP);
        DrawingArea2D d2d = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();
        d2d.getMainGrid().getHorizontalTicksDataGetter().setNoTicks(12);
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNoTicks(12);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();

        float w = 5.0f;
        double Y = 0.0d;
        {
            LineStyle ls = new LineStyle(w, Gradient.getViridisGradient(), 0);
            dss.add(DataSet.getFor2D("Viridis", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getViridisGradient(10, false), 0);
            dss.add(DataSet.getFor2D("Viridis10", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getViridisGradientInverse(), 0);
            dss.add(DataSet.getFor2D("ViridisInv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getViridisGradient(10, false, true), 0);
            dss.add(DataSet.getFor2D("Viridis10Inv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getPlasmaGradient(), 0);
            dss.add(DataSet.getFor2D("Plasma", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getPlasmaGradient(10, false), 0);
            dss.add(DataSet.getFor2D("Plasma10", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getPlasmaGradientInverse(), 0);
            dss.add(DataSet.getFor2D("PlasmaInv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getPlasmaGradient(10, false, true), 0);
            dss.add(DataSet.getFor2D("Plasma10Inv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getMagmaGradient(), 0);
            dss.add(DataSet.getFor2D("Magma", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getMagmaGradient(10, false), 0);
            dss.add(DataSet.getFor2D("Magma10", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getMagmaGradientInverse(), 0);
            dss.add(DataSet.getFor2D("MagmaInv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getMagmaGradient(10, false, true), 0);
            dss.add(DataSet.getFor2D("Magma10Inv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getInfernoGradient(), 0);
            dss.add(DataSet.getFor2D("Inferno", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getInfernoGradient(10, false), 0);
            dss.add(DataSet.getFor2D("Inferno10", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getInfernoGradientInverse(), 0);
            dss.add(DataSet.getFor2D("InfernoInv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getInfernoGradient(10, false, true), 0);
            dss.add(DataSet.getFor2D("Inferno10Inv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getCividisGradient(), 0);
            dss.add(DataSet.getFor2D("Cividis", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getCividisGradient(10, false), 0);
            dss.add(DataSet.getFor2D("Cividis10", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getCividisGradientInverse(), 0);
            dss.add(DataSet.getFor2D("CividisInv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }
        {
            LineStyle ls = new LineStyle(w, Gradient.getCividisGradient(10, false, true), 0);
            dss.add(DataSet.getFor2D("Cividis10Inv", new double[][]{{0.0d, Y}, {1.0d, Y}}, ls));
            Y++;
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        plot.getModel().setDataSets(dss, true);

        frame.setVisible(true);
    }
}
