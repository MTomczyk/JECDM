package t1_10.t1_visualization_module.t8_data_updater.t1;

import color.gradient.Gradient;
import component.drawingarea.DrawingArea3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.TwoPlotsHorizontally;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import t1_10.t1_visualization_module.t8_data_updater.shared.DistanceProcessor;
import t1_10.t1_visualization_module.t8_data_updater.shared.GaussianGenerator;
import updater.*;

import java.text.DecimalFormat;

/**
 * This tutorial shows how to construct and use data updater (i.e., {@link DataUpdater}).
 * Note that this is a revised version of {@link Tutorial1}.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1Alternative
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        AbstractPlot[] plots = new AbstractPlot[2];

        {
            Plot3D.Params pP = new Plot3D.Params();
            pP._xAxisTitle = "X";
            pP._yAxisTitle = "Y";
            pP._zAxisTitle = "Z";
            pP._drawLegend = true;

            pP._scheme = WhiteScheme.getForPlot3D();
            // Adjust scheme
            pP._scheme.setAllFontsTo("Times New Roman");
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE);
            pP._scheme.rescale(1.5f, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.075f);

            pP._pDisplayRangesManager = new DisplayRangesManager.Params();
            pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
            for (int i = 0; i < 4; i++)
                pP._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(null, true);
            pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};
            plots[0] = new Plot3D(pP);

            // Adjust formatters:
            for (int i = 0; i < 3; i++)
                ((DrawingArea3D) plots[0].getComponentsContainer().getDrawingArea()).getAxes()[i].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
        }

        {
            ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(3);
            pP._axesTitles = new String[]{"D1", "D2", "D3"};
            pP._xAxisTitle = "Dimension";
            pP._yAxisTitle = "Coordinate";
            pP._drawLegend = true;

            pP._scheme = WhiteScheme.getForPCP2D();
            // Adjust scheme
            pP._scheme.setAllFontsTo("Times New Roman");
            pP._scheme.rescale(1.25f, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);

            pP._pDisplayRangesManager = new DisplayRangesManager.Params();
            pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[5];
            for (int i = 0; i < 3; i++)
                pP._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), true);
            pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true);
            pP._pDisplayRangesManager._DR[4] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false);
            pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};

            plots[1] = new ParallelCoordinatePlot2D(pP);
            // Adjust formatters:
            for (int i = 0; i < 4; i++)
                plots[1].getComponentsContainer().getAxes()[i].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
        }

        TwoPlotsHorizontally wrapper = new TwoPlotsHorizontally(plots[0], plots[1]);
        Frame frame = new Frame(wrapper, 1600, 1000);

        for (int i = 0; i < 2; i++)
        {
            RightClickPopupMenu menu = new RightClickPopupMenu();
            menu.addItem(new SaveAsImage());
            plots[i].getController().addRightClickPopupMenu(menu);
        }

        IRandom R = new MersenneTwister64(0);
        double[] means0 = new double[]{0.0d, 0.0d, 0.0d};
        double[] means1 = new double[]{1.0d, -1.0d, 0.5d};

        DataUpdater.Params pP = new DataUpdater.Params(wrapper);

        pP._dataSources = new IDataSource[2];
        pP._dataSources[0] = new GaussianGenerator(100, 3, means0, new double[]{0.3d, 0.2d, 0.3d}, R);
        pP._dataSources[1] = new GaussianGenerator(100, 3, means1, new double[]{0.1d, 0.5d, 0.1d}, R);

        pP._dataProcessors = new IDataProcessor[2];
        pP._dataProcessors[0] = new DistanceProcessor(3, means0);
        pP._dataProcessors[1] = new DistanceProcessor(3, means1);

        pP._sourcesToProcessors = new SourceToProcessors[2];
        pP._sourcesToProcessors[0] = new SourceToProcessors(0);
        pP._sourcesToProcessors[1] = new SourceToProcessors(1);

        pP._processorToPlots = new ProcessorToPlots[2];
        int[] plotIds = new int[]{0, 1};


        {
            IDataSet RDS0 = DataSet.getFor3D("DS0", new MarkerStyle(0.02f, Gradient.getViridisGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
            IDataSet RDS1 = DataSet.getForParallelCoordinatePlot2D("DS0", 3, new LineStyle(0.1f, Gradient.getViridisGradient(), 3));
            IDataSet[] referenceDataSets = new IDataSet[]{RDS0, RDS1};
            pP._processorToPlots[0] = new ProcessorToPlots(plotIds, referenceDataSets);
        }
        {
            IDataSet RDS0 = DataSet.getFor3D("DS1", new MarkerStyle(0.02f, Gradient.getPlasmaGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
            IDataSet RDS1 = DataSet.getForParallelCoordinatePlot2D("DS1", 3, new LineStyle(0.1f, Gradient.getPlasmaGradient(), 3));
            IDataSet[] referenceDataSets = new IDataSet[]{RDS0, RDS1};
            pP._processorToPlots[1] = new ProcessorToPlots(plotIds, referenceDataSets);
        }

        DataUpdater dataUpdater;
        try
        {
            dataUpdater = new DataUpdater(pP);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Display the frame:
        frame.setVisible(true);

        for (int i = 0; i < 20; i++)
        {
            dataUpdater.update();
        }
    }
}
