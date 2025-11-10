package popupmenu;

import color.Color;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import popupmenu.item.AbstractItem;
import scheme.AbstractScheme;
import scheme.enums.*;
import utils.FontProcessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main abstract class for maintaining a rick mouse button click popup menu associated with {@link plot.AbstractPlot}.
 *
 * @author MTomczyk
 */


public abstract class AbstractRightClickPopupMenu extends JPopupMenu
{
    /**
     * Global container.
     */
    protected GlobalContainer _GC;

    /**
     * Plot container.
     */
    protected PlotContainer _PC;

    /**
     * Array of contained items.
     */
    protected ArrayList<AbstractItem> _items;

    /**
     * Font used when displaying item label.
     */
    protected FontProcessor _font;

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
     * Default constructor.
     */
    public AbstractRightClickPopupMenu()
    {
        _items = new ArrayList<>(10);
        _font = new FontProcessor();
    }

    /**
     * Releases action listeners.
     */
    public void unregisterListeners()
    {
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [id = " + PlotContainer.getID(_PC) + "]: unregister listeners method called");
        for (AbstractItem item : _items) item.unregisterListeners();
    }


    /**
     * Setter for containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        _GC = GC;
        _PC = PC;
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [ plot id = " + PlotContainer.getID(_PC) + "]: set containers method called");
        for (AbstractItem i : _items) i.setContainers(GC, PC);
    }

    /**
     * main method for adding items.
     *
     * @param item popup menu item
     */
    public void addItem(AbstractItem item)
    {
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [ plot id = " + PlotContainer.getID(_PC) + "]: add item method called");
        item.setMenu(this);
        _items.add(item);
        add(item);
    }

    /**
     * Can be called to accordingly update scheme.
     *
     * @param scheme plot scheme
     */
    public void updateScheme(AbstractScheme scheme)
    {
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [ plot id = " + PlotContainer.getID(_PC) + "]: update scheme method called");
        _font._fontName = scheme.getFonts(_surpassedFonts, FontFields.POPUP_MENU_ITEM);
        _font._size.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.POPUP_MENU_ITEM_FONT_SIZE_FIXED));
        _font._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.POPUP_MENU_ITEM_FONT_SIZE_RELATIVE_MULTIPLIER));
        _font._size.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.POPUP_MENU_ITEM_FONT_USE_RELATIVE_SIZE));
        _font._color = scheme.getColors(_surpassedColors, ColorFields.POPUP_MENU_ITEM_FONT);
        updateRelativeFields();
    }

    /**
     * Clears the data.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [ plot id = " + PlotContainer.getID(_PC) + "]: dispose method called");
        //noinspection DuplicatedCode
        _GC = null;
        _PC = null;
        _items = null;
        _font = null;
        _surpassedSizes = null;
        _surpassedColors = null;
        _surpassedFlags = null;
        _surpassedAlignments = null;
        _surpassedNumbers = null;
        _surpassedFonts = null;
    }


    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    public void updateRelativeFields()
    {
        Notification.printNotification(_GC, _PC, "Abstract right click popup menu [id = " + PlotContainer.getID(_PC) + "]: update relative fields method called");
        float RV = _PC.getReferenceValueGetter().getReferenceValue();
        _font._size.computeActualSize(RV);
        _font.prepareFont();
        for (AbstractItem i : _items) i.setFont(_font._font);
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
