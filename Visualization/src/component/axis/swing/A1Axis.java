package component.axis.swing;

import container.PlotContainer;

/**
 * A1-axis implementation of {@link AbstractAxis}.
 *
 * @author MTomczyk
 */
public class A1Axis extends AbstractAxis
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
            super("A1-axis", PC);
            _type = Type.A1;
            _fields = Fields.getFieldsForA1Axis();
            _dimensions = 2;
        }
    }



    /**
     * Parameterized constructor for the A1-axis.
     *
     * @param p params container
     */
    public A1Axis(Params p)
    {
        super(p);
    }
}
