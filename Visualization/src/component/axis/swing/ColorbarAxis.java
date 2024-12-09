package component.axis.swing;

import container.PlotContainer;

/**
 * Colorbar-axis implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class ColorbarAxis extends AbstractAxis
{
    /**
     * Params container.
     */
    public static class Params extends AbstractAxis.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param PC plot container
         */
        public Params(PlotContainer PC)
        {
            super("Colorbar axis", PC);
            _type = Type.COLORBAR;
            _fields = Fields.getFieldsForColorbarAxis();
            _dimensions = 2;
        }
    }


    /**
     * Parameterized constructor for the A1-axis.
     *
     * @param p params container
     */
    public ColorbarAxis(Params p)
    {
        super(p);
    }
}
