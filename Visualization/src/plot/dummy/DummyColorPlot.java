package plot.dummy;

import color.Color;
import plot.AbstractPlot;
import scheme.enums.ColorFields;

/**
 * Dummy implementation of {@link AbstractPlot} class. The plot is illustrated as a filled rectangle (background color is set).
 *
 * @author MTomczyk
 */
public class DummyColorPlot extends AbstractPlot
{
    /**
     * Parameterized constructor.
     *
     * @param backgroundColor background color.
     */
    public DummyColorPlot(Color backgroundColor)
    {
        super(new Params());
        setOpaque(false);
        _surpassedColors.put(ColorFields.PLOT_BACKGROUND, backgroundColor);
    }
}
