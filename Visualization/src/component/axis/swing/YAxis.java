package component.axis.swing;

import container.PlotContainer;

/**
 * Y-axis implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class YAxis extends AbstractAxis
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
            super("Y-axis", PC);
            _type = AbstractAxis.Type.Y;
            _fields = Fields.getFieldsForYAxis();
            _dimensions = 2;
        }
    }


    /**
     * Parameterized constructor for the X-axis.
     *
     * @param p params container
     */
    public YAxis(Params p)
    {
        super(p);
    }
}
