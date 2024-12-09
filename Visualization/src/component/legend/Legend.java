package component.legend;


import container.PlotContainer;


/**
 * Default implementation of {@link AbstractLegend}. One-column legend.
 *
 * @author MTomczyk
 */
public class Legend extends AbstractLegend
{
    /**
     * Params container.
     */
    public static class Params extends AbstractLegend.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name component name
         * @param PC   plot container: allows accessing various plot components
         */
        public Params(String name, PlotContainer PC)
        {
            super(name, PC);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param PC reference to plot container
     */
    public Legend(PlotContainer PC)
    {
        super(new AbstractLegend.Params("Legend", PC));
    }


    /**
     * Auxiliary method for instantiating entry painter.
     */
    @Override
    protected void instantiateEntryPainter()
    {
        _entryPainter = new BasicEntryPainter();
    }


}
