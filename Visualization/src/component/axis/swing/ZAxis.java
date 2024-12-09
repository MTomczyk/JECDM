package component.axis.swing;

import container.PlotContainer;

/**
 * Z-axis implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class ZAxis extends AbstractAxis
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
            super("Z-axis", PC);
            _type = AbstractAxis.Type.Z;
            _fields = Fields.getFieldsForZAxis();
            _dimensions = 2;
        }
    }

    /**
     * Parameterized constructor for the X-axis.
     *
     * @param p params container
     */
    public ZAxis(Params p)
    {
        super(p);
    }
}
