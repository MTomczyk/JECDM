package component;

import color.Color;
import com.jogamp.opengl.GL2;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import gl.IVBOComponent;
import gl.VBOManager;
import plot.AbstractPlot;
import scheme.AbstractScheme;
import scheme.enums.*;

import java.util.HashMap;

/**
 * Abstract class representing different plot elements based on VBO (3D rendering).
 *
 * @author MTomczyk
 */

public abstract class AbstractVBOComponent implements IVBOComponent
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
     * VBO Manager.
     */
    protected VBOManager _vbo;

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
     * Component alignment.
     */
    protected Align _align;

    /**
     * Background color.
     */
    protected Color _backgroundColor;

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
    public AbstractVBOComponent(String name, PlotContainer PC)
    {
        this(new Params(name, PC));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractVBOComponent(Params p)
    {
        _name = p._name;
        _PC = p._PC;
        _surpassedAlignments = new HashMap<>(2);
        _surpassedFlags = new HashMap<>(2);
        _surpassedColors = new HashMap<>(2);
        _surpassedNumbers = new HashMap<>(2);
        _surpassedSizes = new HashMap<>(2);
        _surpassedFonts = new HashMap<>(2);
        _align = null;
        _backgroundColor = null;
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
     * Can be called to clear memory.
     */
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: dispose method called");
        _GC = null;
        _PC = null;
        _surpassedAlignments = null;
        _surpassedColors = null;
        _surpassedFonts = null;
        _surpassedSizes = null;
        _surpassedNumbers = null;
        _surpassedFlags = null;
    }

    /**
     * Can be called to draw object.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void draw(GL2 gl)
    {
        if (_vbo != null) _vbo.render(gl);
    }

    /**
     * Can be called to instantiate VBO buffers (but the data has not yet been transferred to GPU).
     */
    @Override
    public void createBuffers()
    {

    }

    /**
     * Can be called to instantiate VBO buffers on the GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeInitialDataTransfer(GL2 gl)
    {
        if (_vbo != null) _vbo.initialDataTransfer(gl);
    }

    /**
     * The method returns true if some of the wrapped buffers need to be sent to the GPU for the first time.
     *
     * @return true, if the update is required, false = otherwise.
     */
    @Override
    public boolean isInitialUpdateRequested()
    {
        if (_vbo != null) return _vbo.isInitializationRequested();
        return false;
    }

    /**
     * Can be called to update data in the VBO that has already been instantiated and sent to GPU  (but the updated is not yet transferred to GPU).
     */
    @Override
    public void updateBuffers()
    {

    }

    /**
     * Can be called to update the VBO that is already instantiated in GPU.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void executeUpdate(GL2 gl)
    {
        // must be explicitly overwritten to indicate that this buffer can be changing
        //if (_vbo != null) _vbo.updateData(gl);
    }

    /**
     * The method returns true if some of the wrapped buffers were updated and the data needs to be sent to GPU.
     *
     * @return true, if the update is required, false = otherwise.
     */
    public boolean isUpdateRequested()
    {
        if (_vbo != null) return _vbo.isUpdateRequested();
        return false;
    }

    /**
     * Can be called to remove GL-related data.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void dispose(GL2 gl)
    {
        if (_vbo != null) _vbo.dispose(gl);
        _vbo = null;
    }


    /**
     * Can be called to check whether the buffer objects have not been created yet.
     *
     * @return true, buffer objects have not been created yet.
     */
    public boolean areVBOsNull()
    {
        return _vbo == null;
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
