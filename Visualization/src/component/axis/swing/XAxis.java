package component.axis.swing;

import container.PlotContainer;

/**
 * X-axis implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class XAxis extends AbstractAxis
{
    /**
     * Params container.
     */
    public static class Params extends AbstractAxis.Params
    {
        /**
         * Parameterized constructor.
         * @param PC plot container
         */
        public Params(PlotContainer PC)
        {
            super("X-axis", PC);
            _type = AbstractAxis.Type.X;
            _fields = Fields.getFieldsForXAxis();
            _dimensions = 2;
        }
    }

    /**
     * Parameterized constructor for the X-axis.
     *
     * @param p params container
     */
    public XAxis(Params p)
    {
        super(p);
    }
}
