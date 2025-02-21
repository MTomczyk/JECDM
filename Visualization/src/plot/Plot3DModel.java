package plot;

import component.axis.gl.Axis3D;
import component.drawingarea.AbstractDrawingArea;
import component.drawingarea.DrawingArea3D;
import component.pane.Pane;
import container.GLInitComponentsContainer;
import container.PlotContainer;
import dataset.IDataSet;
import dataset.painter.Painter3D;
import gl.IVBOComponent;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Model for the {@link Plot3D} class.
 *
 * @author MTomczyk
 */
public class Plot3DModel extends PlotModel
{
    /**
     * Component container for (parent) objects that require open gl initialization.
     * The initialization is executed after all plot components are successfully established.
     */
    private final GLInitComponentsContainer _GLCC;

    /**
     * Parameterized constructor.
     *
     * @param plot reference to the plot.
     * @param PC   plot container
     */
    public Plot3DModel(AbstractPlot plot, PlotContainer PC)
    {
        super(plot, PC);
        _GLCC = new GLInitComponentsContainer();
        _GLCC.setComponents(new LinkedList<>());
    }

    /**
     * Auxiliary method that updates plot IDS, starting from the second level.
     * The method is invoked by all window-resized-like events.
     */
    @Override
    public void updatePlotIDSsAndRenderOnScreenResize()
    {
        if (_CC.getDrawingArea() == null) return;
        AbstractDrawingArea da = _CC.getDrawingArea();
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        //workers.add(da.createIDSUpdater(EventTypes.ON_RESIZE)); // there is no need to recalculate IDS
        workers.add(da.createRenderUpdater(EventTypes.ON_RESIZE));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(_GC.getBlockTypeID(BlockTypes.IDS_AND_RENDER_UPDATER_ON_RESIZE), _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }


    /**
     * Auxiliary method that instantiates list of objects listening for changes in display ranges.
     */
    @Override
    protected void instantiateDisplayRangesChangedListeners()
    {
        _drChangedListeners = new LinkedList<>();
        Pane[] panes = ((DrawingArea3D) _CC.getDrawingArea()).getPanes();
        if (panes != null) for (Pane p : panes)
            if (p != null) _drChangedListeners.add(p);

        Axis3D[] axes = ((DrawingArea3D) _CC.getDrawingArea()).getAxes();
        if (axes != null) for (Axis3D a : axes)
            if (a != null) _drChangedListeners.add(a);

        if (_CC.getColorbar() != null) _drChangedListeners.add(_CC.getColorbar());
    }

    /**
     * Protected data sets setter (just sets the reference).
     *
     * @param updateMatchingOnly if true, he method updates data sets whose names match names of those provided; if
     *                           a data set is in the input but not in the currently maintained set, it is ignored;
     *                           if a data set is not in the input but is in te currently maintained set, it remains
     *                           in the set but; if false, the method removes the currently maintained sets and uses
     *                           the input as the new ones
     */
    @Override
    protected void setDataSetsReference(ArrayList<IDataSet> dataSets, boolean updateMatchingOnly)
    {
        super.setDataSetsReference(dataSets, updateMatchingOnly);
        ArrayList<Painter3D> painters3D = new ArrayList<>(dataSets.size());
        DrawingArea3D d3d = (DrawingArea3D) _CC.getDrawingArea();

        if (updateMatchingOnly)
        {
            LinkedList<IVBOComponent> cPainters3D = d3d.getPainters3D();
            LinkedList<IVBOComponent> forRemoval = new LinkedList<>();
            ListIterator<IVBOComponent> cPainters3DIt = null;
            if (cPainters3D != null) cPainters3DIt = cPainters3D.listIterator();

            for (IDataSet ds : dataSets)
                if ((ds != null) && (ds.getPainter() != null) && (ds.getPainter() instanceof Painter3D))
                {
                    // It is assumed that the no. elements match
                    painters3D.add((Painter3D) ds.getPainter());
                    IVBOComponent previousCorrespondingPainter = null;
                    if ((cPainters3DIt != null) && (cPainters3DIt.hasNext()))
                        previousCorrespondingPainter = cPainters3DIt.next();
                    if (!ds.areIDSUpdatesSkipped()) forRemoval.add(previousCorrespondingPainter);
                }

            d3d.setPainters3DForRemoval(forRemoval);
            d3d.setPainters3D(painters3D);
        }
        else
        {
            d3d.setPainters3DForRemoval(d3d.getPainters3D());
            for (IDataSet ds : dataSets)
                if ((ds != null) && (ds.getPainter() != null) && (ds.getPainter() instanceof Painter3D))
                    painters3D.add((Painter3D) ds.getPainter());
            d3d.setPainters3D(painters3D);
        }
    }


    /**
     * Getter for the component container for (parent) objects that require open gl initialization.
     * The initialization is executed after all plot components are successfully established.
     *
     * @return container for the components requiring the initialization
     */
    public GLInitComponentsContainer getGLInitComponentsContainer()
    {
        return _GLCC;
    }
}
