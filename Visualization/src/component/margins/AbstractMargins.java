package component.margins;


import component.AbstractSwingComponent;
import container.PlotContainer;
import scheme.AbstractScheme;
import scheme.enums.Align;
import scheme.enums.FlagFields;
import scheme.enums.SizeFields;
import utils.Size;

/**
 * Supportive class representing plot margins.
 *
 * @author MTomczyk
 */


public abstract class AbstractMargins extends AbstractSwingComponent
{
    /**
     * Left margin size.
     */
    protected Size _left;

    /**
     * Top margin size.
     */
    protected Size _top;

    /**
     * Right margin size.
     */
    protected Size _right;

    /**
     * Bottom margin size.
     */
    protected Size _bottom;

    /**
     * Parameterized constructor.
     *
     * @param PC plot container: allows easy access to various plot components/functionalities (required to provide)
     */
    public AbstractMargins(PlotContainer PC)
    {
        super("Margins", PC);
        _left = new Size();
        _top = new Size();
        _right = new Size();
        _bottom = new Size();

    }

    /**
     * Getter for the left margin.
     * @return left margin
     */
    public Size getLeft()
    {
        return _left;
    }

    /**
     * Getter for the right margin.
     * @return right margin
     */
    public Size getRight()
    {
        return _right;
    }

    /**
     * Getter for the top margin.
     * @return top margin
     */
    public Size getTop()
    {
        return _top;
    }

    /**
     * Getter for the bottom margin.
     * @return bottom margin
     */
    public Size getBottom()
    {
        return _bottom;
    }

    /**
     * Update scheme.
     *
     * @param scheme scheme object
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        float RV = _PC.getReferenceValueGetter().getReferenceValue();

        _align = Align.GLOBAL;

        _left.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.MARGIN_LEFT_USE_RELATIVE_SIZE));
        _left.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_LEFT_SIZE_FIXED));
        _left.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER));
        _left.computeActualSize(RV);

        _top.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.MARGIN_TOP_USE_RELATIVE_SIZE));
        _top.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_TOP_SIZE_FIXED));
        _top.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER));
        _top.computeActualSize(RV);

        _right.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.MARGIN_RIGHT_USE_RELATIVE_SIZE));
        _right.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_RIGHT_SIZE_FIXED));
        _right.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER));
        _right.computeActualSize(RV);

        _bottom.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.MARGIN_BOTTOM_USE_RELATIVE_SIZE));
        _bottom.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_BOTTOM_SIZE_FIXED));
        _bottom.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER));
        _bottom.computeActualSize(RV);
    }

    /**
     * Updates bounds of the panel.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setLocationAndSize(int x, int y, int w, int h)
    {
        super.setLocationAndSize(x, y, w, h);
        float RV = _PC.getReferenceValueGetter().getReferenceValue();
        _left.computeActualSize(RV);
        _right.computeActualSize(RV);
        _top.computeActualSize(RV);
        _bottom.computeActualSize(RV);
    }
}
