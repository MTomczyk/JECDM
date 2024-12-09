package component.title;

import container.PlotContainer;


/**
 * Default implementation of {@link AbstractTitle} class.
 *
 * @author MTomczyk
 */


public class Title extends AbstractTitle
{
    /**
     * Parameterized constructor
     *
     * @param title plot title to be displayed
     * @param PC    plot container: allows easy access to various plot components/functionalities (required to provide)
     */
    public Title(String title, PlotContainer PC)
    {
        super(new Params(title, "title", PC));
    }
}
