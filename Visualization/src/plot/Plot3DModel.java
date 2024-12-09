package plot;

import component.axis.gl.Axis3D;
import component.drawingarea.AbstractDrawingArea;
import component.drawingarea.DrawingArea3D;
import component.pane.Pane;
import container.GLInitComponentsContainer;
import container.PlotContainer;
import dataset.IDataSet;
import dataset.painter.Painter3D;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;

import java.util.ArrayList;
import java.util.LinkedList;

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
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.IDS_AND_RENDER_UPDATER_ON_RESIZE, _PC.getPlotID(), workers);
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
     */
    @Override
    protected void setDataSetsReference(ArrayList<IDataSet> dataSets)
    {
        _dataSets = dataSets;
        ArrayList<Painter3D> painters3D = new ArrayList<>(dataSets.size());
        for (IDataSet ds : dataSets)
            if ((ds != null) && (ds.getPainter() != null) && (ds.getPainter() instanceof Painter3D))
                painters3D.add((Painter3D) ds.getPainter());
        ((DrawingArea3D) _CC.getDrawingArea()).setPainters3D(painters3D);
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
