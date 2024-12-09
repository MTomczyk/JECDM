package component;

import color.Color;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import plot.AbstractPlot;
import scheme.AbstractScheme;
import scheme.enums.*;
import utils.DrawUtils;
import utils.Projection;
import utils.Size;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Abstract class representing different swing-based components of a plot.
 * Class wraps JPanel.
 *
 * @author MTomczyk
 */

public abstract class AbstractSwingComponent extends JPanel
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlot.Params
    {
        /**
         * Component name.
         */
        public String _name;

        /**
         * Plot container: allows accessing various plot components.
         */
        public PlotContainer _PC;

        /**
         * Parameterized constructor.
         *
         * @param name component name
         * @param PC   plot container: allows accessing various plot components
         */
        public Params(String name, PlotContainer PC)
        {
            _name = name;
            _PC = PC;
        }

    }

    /**
     * Component name.
     */
    protected String _name;

    /**
     * Global container: allows accessing various components of the main frame.
     */
    protected GlobalContainer _GC;

    /**
     * Plot container: allows accessing various plot components.
     */
    protected PlotContainer _PC;

    /**
     * Background color.
     */
    protected Color _backgroundColor;

    /**
     * Border color.
     */
    protected Color _borderColor;

    /**
     * Border width.
     */
    protected Size _borderWidth;

    /**
     * Border stroke.
     */
    protected BasicStroke _borderStroke;

    /**
     * Component alignment.
     */
    protected Align _align;

    /**
     * Primary drawing area (should be enclosed within panel bounds).
     */
    protected Rectangle _primaryDrawingArea = new Rectangle();

    /**
     * Supportive translation vector used when transforming absolute coordinates into the ones represented by
     * the bounds imposed by the primary drawing area.
     */
    protected float[] _translationVector = new float[2];

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (alignments).
     */
    protected HashMap<AlignFields, Align> _surpassedAlignments;

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (colors).
     */
    protected HashMap<ColorFields, Color> _surpassedColors;

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (fonts).
     */
    protected HashMap<FontFields, String> _surpassedFonts;

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (floats).
     */
    protected HashMap<SizeFields, Float> _surpassedSizes;

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (numbers).
     */
    protected HashMap<NumberFields, Integer> _surpassedNumbers;

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (flags).
     */
    protected HashMap<FlagFields, Boolean> _surpassedFlags;

    /**
     * Parameterized constructor.
     *
     * @param name component name
     * @param PC   p
     */
    public AbstractSwingComponent(String name, PlotContainer PC)
    {
        this(new Params(name, PC));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractSwingComponent(Params p)
    {
        _name = p._name;
        _PC = p._PC;
        _surpassedAlignments = new HashMap<>(2);
        _surpassedFlags = new HashMap<>(2);
        _surpassedColors = new HashMap<>(2);
        _surpassedNumbers = new HashMap<>(2);
        _surpassedSizes = new HashMap<>(2);
        _surpassedFonts = new HashMap<>(2);
        _borderWidth = new Size();
    }


    /**
     * Can be used to set a global container.
     *
     * @param GC global container: allows accessing various components of the main frame
     */
    public void establishGlobalContainer(GlobalContainer GC)
    {
        _GC = GC;
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: global container is set");
    }


    /**
     * Called to update the component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    public void updateScheme(AbstractScheme scheme)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: update scheme method called");
    }

    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        //super.paintComponent(g); // do not use it
        //Graphics g2 = g.create();
        drawBackground(g);
        drawBorder(g);
        //g2.dispose();
    }

    /**
     * Updates bounds of the panel.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    public void setLocationAndSize(int x, int y, int w, int h)
    {
        Dimension size = new Dimension(w, h);
        setPreferredSize(size);
        setSize(size);
        setLocation(x, y);
    }

    /**
     * Updates bounds of the primary drawing area (should be enclosed within the panel bounds).
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    public void setPrimaryDrawingArea(int x, int y, int w, int h)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set primary drawing area method called");

        if (_primaryDrawingArea == null)
        {
            _primaryDrawingArea = new Rectangle();
            _translationVector = new float[2];
        }

        _primaryDrawingArea.x = x;
        _primaryDrawingArea.y = y;
        _primaryDrawingArea.width = w;
        _primaryDrawingArea.height = h;
        _translationVector[0] = _primaryDrawingArea.x - getX();
        _translationVector[1] = _primaryDrawingArea.y - getY();
    }

    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    public void updateRelativeFields()
    {

    }

    /**
     * Getter for the primary drawing area.
     *
     * @return primary drawing area
     */
    public Rectangle getPrimaryDrawingArea()
    {
        return _primaryDrawingArea;
    }

    /**
     * Draws background.
     *
     * @param g Java AWT Graphics context
     */
    protected void drawBackground(Graphics g)
    {
        if ((_backgroundColor != null) && (_primaryDrawingArea != null))
        {
            g.setColor(_backgroundColor);
            g.fillRect(Projection.getP(_translationVector[0]), Projection.getP(_translationVector[1]),
                    _primaryDrawingArea.width, _primaryDrawingArea.height);
        }
    }


    /**
     * Draws border.
     *
     * @param g Java AWT Graphics context
     */
    protected void drawBorder(Graphics g)
    {
        if ((_borderColor != null) && (_borderStroke != null) && (_primaryDrawingArea != null))
        {
            g.setColor(_borderColor);
            ((Graphics2D) g).setStroke(_borderStroke);
            DrawUtils.drawBorder(g, _translationVector[0], _translationVector[1], _primaryDrawingArea.width, _primaryDrawingArea.height);
        }
    }

    /**
     * Supportive method for establishing line stroke.
     *
     * @param width size object containing data on fixed and relative size.
     * @return basic stroke
     */
    protected BasicStroke getStroke(Size width)
    {
        return getStroke(width, 0);
    }

    /**
     * Supportive method for establishing line stroke.
     *
     * @param width    size object containing data on fixed and relative size.
     * @param noDashes if 0 -> solid line is drawn; if > 0 -> a dashed line is drawn (the parameter determines the number of dashes).
     * @return basic stroke
     */
    protected BasicStroke getStroke(Size width, int noDashes)
    {
        width.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());
        if (Float.compare(width._actualSize, 0.0f) > 0)
        {
            float lw = width._actualSize;
            if (Float.compare(width._actualSize, 1.0f) < 0) lw = 1.0f;

            if (noDashes > 0)
                return new BasicStroke(lw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{noDashes}, 0);
            else if (noDashes == 0) return new BasicStroke(width._actualSize);
            else return null;
        }
        else return null;
    }


    /**
     * Can be called to clear memory.
     */
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: dispose method called");
        _GC = null;
        _PC = null;
        _backgroundColor = null;
        _borderColor = null;
        _borderWidth = null;
        _borderStroke = null;
        _align = null;
        _primaryDrawingArea = null;
        _surpassedAlignments = null;
        _surpassedColors = null;
        _surpassedFonts = null;
        _surpassedSizes = null;
        _surpassedNumbers = null;
        _surpassedFlags = null;
        _translationVector = null;
    }

    /**
     * Returns the alignment.
     *
     * @return component alignment
     */
    public Align getAlignment()
    {
        return _align;
    }

    /**
     * Getter for the component name.
     *
     * @return component name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Getter for the background color.
     *
     * @return background color
     */
    public Color getBackgroundColor()
    {
        return _backgroundColor;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (alignments).
     *
     * @return surpassed alignments
     */
    public HashMap<AlignFields, Align> getSurpassedAlignments()
    {
        return _surpassedAlignments;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (colors).
     *
     * @return surpassed colors
     */
    public HashMap<ColorFields, Color> getSurpassedColors()
    {
        return _surpassedColors;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (floats).
     *
     * @return surpassed sizes
     */
    public HashMap<SizeFields, Float> getSurpassedSizes()
    {
        return _surpassedSizes;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (numbers).
     *
     * @return surpassed numbers
     */
    public HashMap<NumberFields, Integer> getSurpassedNumbers()
    {
        return _surpassedNumbers;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (flags).
     *
     * @return surpassed flags
     */
    public HashMap<FlagFields, Boolean> getSurpassedFlags()
    {
        return _surpassedFlags;
    }

    /**
     * Surpassed: the contained (key, values) pairs can be used to surpass the indications of the current scheme (fonts).
     *
     * @return surpassed fonts
     */
    public HashMap<FontFields, String> getSurpassedFonts()
    {
        return _surpassedFonts;
    }


}
