package t1_10.t1_visualization_module.t2_plot3d.t1;

import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.Dimension;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot (altering panes and axes).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1b
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._scheme = WhiteScheme.getForPlot3D();
        //pP._useAlphaChannel = true;

        // Disable the 3D drawing area outline
        pP._drawCube = false;
        // Specify that only the bottom pane will be rendered.
        pP._paneAlignments = new Align[]{Align.BOTTOM};

        // Specify alignments for 3D axes. Note that the alignments can be provided arbitrarily; the axis-alignment
        // matching will be done automatically. Additionally, multiple (i.e., more than 1) alignments that match
        // a particular axis type (e.g., X-axis) can be specified. If so, multiple such axes will be drawn. In this
        // example, the matching axes are as follows: Y-axis (BACK_RIGHT), X-axis (FRONT_TOP), and two Z-axes
        // (LEFT_BOTTOM and RIGHT_TOP). Note that using ``draw axis'' flag has no effect.
        pP._axesAlignments = new Align[]{Align.BACK_RIGHT, Align.FRONT_TOP, Align.LEFT_BOTTOM, Align.RIGHT_TOP};

        // The plot drawing area is spanned on [-0.5, 0.5] bound by default. These bounds can be, however, customized.
        // The line below resizes the drawing area long the X-axis.
        pP._xProjectionBound = new Dimension(-1.0f, 2.0f);

        Plot3D plot = new Plot3D(pP);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 1200, 800);

        frame.setVisible(true);
    }
}
