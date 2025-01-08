package layoutmanager;

import component.AbstractSwingComponent;
import component.axis.swing.AbstractAxis;
import component.legend.Dimensions;
import container.ComponentsContainer;
import container.Notification;
import container.PlotContainer;
import plot.AbstractPlot;
import scheme.AbstractScheme;
import utils.Projection;

import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Organizes the layout as follows: the drawing area is located in the center,
 * while the supportive elements (title, colorbar, etc.) are located above/below/left/right,
 * as imposed by their alignment fields. Exception: the legend can be located in the drawing area.
 *
 * @author MTomczyk
 */


public class BaseLayoutManager extends AbstractLayoutManager implements ILayoutManager
{
    /**
     * Constructs and wraps auxiliary parameters for positioning.
     */
    protected static class SegmentsDimensions
    {
        /**
         * Parameterized constructor
         *
         * @param CC components container
         * @param w main panel's width
         * @param h main panel's height
         */
        public SegmentsDimensions(ComponentsContainer CC, int w, int h)
        {
            // segments x
            _sx = new int[]{0, Projection.getP(CC.getMargins().getLeft()._actualSize),
                    Projection.getP_sub(w, CC.getMargins().getRight()._actualSize)};

            _sy = new int[]{0, Projection.getP(CC.getMargins().getTop()._actualSize),
                    Projection.getP_sub(h, CC.getMargins().getBottom()._actualSize)};

            _sw = new int[]{_sx[1], _sx[2] - _sx[1], w - _sx[2]};

            // segment height
            _sh = new int[]{_sy[1], _sy[2] - _sy[1], h - _sy[2]};
        }

        /**
         * Segments' x-coordinates.
         */
        protected final int[] _sx;

        /**
         * Segments' y-coordinates.
         */
        protected final int[] _sy;

        /**
         * Segments' widths.
         */
        protected final int[] _sw;

        /**
         * Segments' height.
         */
        protected final int[] _sh;
    }

    /**
     * Reference to center elements.
     */
    protected ArrayList<AbstractSwingComponent> _centerElements;

    /**
     * Reference to left elements.
     */
    protected ArrayList<AbstractSwingComponent> _leftElements;

    /**
     * Reference to top elements.
     */
    protected ArrayList<AbstractSwingComponent> _topElements;

    /**
     * Reference to right elements.
     */
    protected ArrayList<AbstractSwingComponent> _rightElements;

    /**
     * Reference to bottom elements.
     */
    protected ArrayList<AbstractSwingComponent> _bottomElements;

    /**
     * Parameterized constructor.
     *
     * @param PC plot container: allows easy access to various plot-related components/functionalities
     * @param CC container for various plot components
     */
    public BaseLayoutManager(PlotContainer PC, ComponentsContainer CC)
    {
        super(PC, CC);

        _centerElements = new ArrayList<>(2);
        _centerElements.add(_CC.getDrawingArea());

        _leftElements = new ArrayList<>(2);
        _topElements = new ArrayList<>(2);
        _rightElements = new ArrayList<>(2);
        _bottomElements = new ArrayList<>(2);
    }

    /**
     * Adds elements to the plot (JPanel) in the correct order.
     *
     * @param plot the plot
     */
    @Override
    public void addElementsInCorrectOrder(AbstractPlot plot)
    {
        if (_CC.getLegend() != null) plot.add(_CC.getLegend());
        if (_CC.getColorbar() != null) plot.add(_CC.getColorbar());
        if (_CC.getTitle() != null) plot.add(_CC.getTitle());
        if (_CC.getAxes() != null) for (AbstractAxis a : _CC.getAxes()) plot.add(a);
        if (_CC.getDrawingArea() != null) plot.add(_CC.getDrawingArea());
    }

    /**
     * Used to properly set elements' dimensions.
     *
     * @param g current rendering context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    @Override
    public void positionElements(Graphics g, int x, int y, int w, int h)
    {
        Notification.printNotification(_GC, _PC, "Layout manager [id = " + PlotContainer.getID(_PC) + "]: position elements method called");

        // update margins first
        _CC.getMargins().setLocationAndSize(x, y, w, h);

        SegmentsDimensions SD = new SegmentsDimensions(_CC, w, h);
        assert SD._sw[0] + SD._sw[1] + SD._sw[2] == w;
        assert SD._sh[0] + SD._sh[1] + SD._sh[2] == h;

        positionCenterElements(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        positionLeftElements(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        positionTopElements(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        positionRightElements(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        positionBottomElements(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        positionLegend(g, x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        doPostPositioning(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
    }

    /**
     * Used to properly set legend's dimensions.
     *
     * @param g current graphics context
     * @param x plot main panel x-coordinate
     * @param y plot main panel y-coordinate
     * @param w plot main panel width
     * @param h plot main panel height
     */
    public void updateLegend(Graphics g, int x, int y, int w, int h)
    {
        SegmentsDimensions SD = new SegmentsDimensions(_CC, w, h);
        positionLegend(g, x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
        doPostPositioning(x, y, w, h, SD._sx, SD._sy, SD._sw, SD._sh);
    }

    /**
     * Auxiliary method positioning plot center components.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void positionCenterElements(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        ListIterator<AbstractSwingComponent> it = _centerElements.listIterator();
        AbstractSwingComponent e;
        while (it.hasNext())
        {
            e = it.next();
            e.setLocationAndSize(sx[0], sy[0], w, h);
            e.setPrimaryDrawingArea(sx[1], sy[1], sw[1], sh[1]);
        }
    }

    /**
     * Auxiliary method positioning plot left components.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void positionLeftElements(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        ListIterator<AbstractSwingComponent> it = _leftElements.listIterator();
        AbstractSwingComponent e;
        while (it.hasNext())
        {
            e = it.next();
            e.setLocationAndSize(sx[0], sy[0], sw[0] + sw[1], h);
            e.setPrimaryDrawingArea(sx[0], sy[1], sw[0], sh[1]);
        }
    }

    /**
     * Auxiliary method positioning plot top components.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void positionTopElements(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        ListIterator<AbstractSwingComponent> it = _topElements.listIterator();
        AbstractSwingComponent e;
        while (it.hasNext())
        {
            e = it.next();
            e.setLocationAndSize(sx[0], sy[0], w, sh[0] + sh[1]);
            e.setPrimaryDrawingArea(sx[1], sy[0], sw[1], sh[0]);
        }
    }

    /**
     * Auxiliary method positioning plot top components.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void positionRightElements(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        ListIterator<AbstractSwingComponent> it = _rightElements.listIterator();
        AbstractSwingComponent e;
        while (it.hasNext())
        {
            e = it.next();
            e.setLocationAndSize(sx[1], sy[0], sw[1] + sw[2], h);
            e.setPrimaryDrawingArea(sx[2], sy[1], sw[2], sh[1]);
        }
    }

    /**
     * Auxiliary method positioning plot bottom components.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void positionBottomElements(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        ListIterator<AbstractSwingComponent> it = _bottomElements.listIterator();
        AbstractSwingComponent e;
        while (it.hasNext())
        {
            e = it.next();
            e.setLocationAndSize(sx[0], sy[1], w, sh[1] + sh[2]);
            e.setPrimaryDrawingArea(sx[1], sy[2], sw[1], sh[2]);
        }
    }

    /**
     * Auxiliary method executing post-positioning (overwrites previous positioning results).
     * To be overwritten.
     *
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    protected void doPostPositioning(int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {

    }


    /**
     * Supportive method for positioning the legend.
     *
     * @param g  current graphics context
     * @param x  plot x-coordinate
     * @param y  plot y-coordinate
     * @param w  plot width
     * @param h  plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    private void positionLegend(Graphics g, int x, int y, int w, int h, int[] sx, int[] sy, int[] sw, int[] sh)
    {
        if (_CC.getLegend() == null) return;

        switch (_CC.getLegend().getAlignment())
        {
            case LEFT_TOP, CENTER_TOP, RIGHT_TOP, LEFT_CENTER,
                    CENTER_CENTER, RIGHT_CENTER, LEFT_BOTTOM,
                    CENTER_BOTTOM, RIGHT_BOTTOM -> _CC.getLegend().setLocationAndSize(sx[1], sy[1], sw[1], sh[1]);
            case LEFT_TOP_EXTERNAL, LEFT_CENTER_EXTERNAL, LEFT_BOTTOM_EXTERNAL ->
                    _CC.getLegend().setLocationAndSize(sx[0], sy[0], sw[0], h);
            case RIGHT_TOP_EXTERNAL, RIGHT_CENTER_EXTERNAL, RIGHT_BOTTOM_EXTERNAL ->
                    _CC.getLegend().setLocationAndSize(sx[2], sy[0], sw[2], h);

        }

        _CC.getLegend().updateRelativeFields(); // execution order matters!
        _CC.getLegend().calculateExpectedDimensions(g);
        Dimensions d = _CC.getLegend().getExpectedDimensions();

        int[] iD = new int[]{Projection.getP(d._expectedDimensions[0]), Projection.getP(d._expectedDimensions[1])};
        float cO = _CC.getLegend().getOffset()._actualSize;
        int[] lO = new int[]{Projection.getP(cO), Projection.getP(cO)};

        switch (_CC.getLegend().getAlignment())
        {
            case LEFT_TOP -> _CC.getLegend().setPrimaryDrawingArea(sx[1] + lO[0], sy[1] + lO[1], iD[0], iD[1]);
            case CENTER_TOP, TOP ->
                    _CC.getLegend().setPrimaryDrawingArea(Projection.getP(sx[1] + sw[1] / 2.0f - iD[0] / 2.0f), sy[1] + lO[1], iD[0], iD[1]);
            case RIGHT_TOP ->
                    _CC.getLegend().setPrimaryDrawingArea(sx[1] + sw[1] - lO[0] - iD[0], sy[1] + lO[1], iD[0], iD[1]);

            case LEFT_CENTER, LEFT -> _CC.getLegend().setPrimaryDrawingArea(sx[1] + lO[0],
                    Projection.getP(sy[1] + sh[1] / 2.0f - iD[1] / 2.0f), iD[0], iD[1]);
            case CENTER_CENTER ->
                    _CC.getLegend().setPrimaryDrawingArea(Projection.getP(sx[1] + sw[1] / 2.0f - iD[0] / 2.0f),
                            Projection.getP(sy[1] + sh[1] / 2.0f - iD[1] / 2.0f), iD[0], iD[1]);
            case RIGHT_CENTER, RIGHT -> _CC.getLegend().setPrimaryDrawingArea(sx[1] + sw[1] - lO[0] - iD[0],
                    Projection.getP(sy[1] + sh[1] / 2.0f - iD[1] / 2.0f), iD[0], iD[1]);

            case LEFT_BOTTOM -> _CC.getLegend().setPrimaryDrawingArea(sx[1] + lO[0],
                    sy[1] + sh[1] - iD[1] - lO[1], iD[0], iD[1]);
            case CENTER_BOTTOM, BOTTOM ->
                    _CC.getLegend().setPrimaryDrawingArea(Projection.getP(sx[1] + sw[1] / 2.0f - iD[0] / 2.0f),
                            sy[1] + sh[1] - iD[1] - lO[1], iD[0], iD[1]);
            case RIGHT_BOTTOM -> _CC.getLegend().setPrimaryDrawingArea(sx[1] + sw[1] - lO[0] - iD[0],
                    sy[1] + sh[1] - iD[1] - lO[1], iD[0], iD[1]);

            case LEFT_TOP_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sw[0] - lO[0] - iD[0],
                    sy[1], iD[0], iD[1]);
            case LEFT_CENTER_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sw[0] - lO[0] - iD[0],
                    Projection.getP(sy[1] + (sh[1] - iD[1]) / 2.0f), iD[0], iD[1]);
            case LEFT_BOTTOM_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sw[0] - lO[0] - iD[0],
                    sy[1] + sh[1] - iD[1], iD[0], iD[1]);

            case RIGHT_TOP_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sx[2] + lO[0], sy[1], iD[0], iD[1]);
            case RIGHT_CENTER_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sx[2] + lO[0],
                    Projection.getP(sy[1] + (sh[1] - iD[1]) / 2.0f), iD[0], iD[1]);
            case RIGHT_BOTTOM_EXTERNAL -> _CC.getLegend().setPrimaryDrawingArea(sx[2] + lO[0],
                    sy[1] + sh[1] - iD[1], iD[0], iD[1]);
        }
    }

    /**
     * Update scheme. Responsible mainly for assigning components to different areas (center, right, left, etc.).
     *
     * @param scheme scheme object
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        ArrayList<AbstractSwingComponent> newCE = new ArrayList<>(_centerElements.size());
        ArrayList<AbstractSwingComponent> newLE = new ArrayList<>(_leftElements.size());
        ArrayList<AbstractSwingComponent> newRE = new ArrayList<>(_rightElements.size());
        ArrayList<AbstractSwingComponent> newTE = new ArrayList<>(_topElements.size());
        ArrayList<AbstractSwingComponent> newBE = new ArrayList<>(_bottomElements.size());

        if (_CC.getDrawingArea() != null) newCE.add(_CC.getDrawingArea());

        for (AbstractSwingComponent e : _CC.getComponents())
        {
            if (e == null) continue;
            switch (e.getAlignment())
            {
                case RIGHT, RIGHT_TOP_EXTERNAL, RIGHT_CENTER_EXTERNAL, RIGHT_BOTTOM_EXTERNAL -> newRE.add(e);
                case BOTTOM -> newBE.add(e);
                case LEFT, LEFT_BOTTOM_EXTERNAL, LEFT_CENTER_EXTERNAL, LEFT_TOP_EXTERNAL -> newLE.add(e);
                case TOP -> newTE.add(e);
                case CENTER_CENTER -> newCE.add(e);
                default ->
                {
                }
            }
        }

        _centerElements = newCE;
        _leftElements = newLE;
        _topElements = newTE;
        _rightElements = newRE;
        _bottomElements = newBE;
    }


    /**
     * Can be called to clear data.
     */
    @Override
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, "Base layout manager [id = " + PlotContainer.getID(_PC) + "]: dispose method called");
        //noinspection DuplicatedCode

        _centerElements = null;
        _leftElements = null;
        _topElements = null;
        _rightElements = null;
        _bottomElements = null;
        _CC = null;
    }
}
