package component.margins;


import container.PlotContainer;

/**
 * Supportive class representing plot margins.
 *
 * @author MTomczyk
 */


public class Margins extends AbstractMargins
{
    /**
     * Parameterized constructor.
     *
     * @param PC plot container: allows easy-access to various plot components/functionalities (required to provide)
     */
    public Margins(PlotContainer PC)
    {
        super(PC);
        setOpaque(false);
    }
}
