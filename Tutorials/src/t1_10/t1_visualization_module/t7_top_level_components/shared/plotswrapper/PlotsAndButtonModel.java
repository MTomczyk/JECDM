package t1_10.t1_visualization_module.t7_top_level_components.shared.plotswrapper;


import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.PlotsWrapperModel;
import plotwrapper.AbstractPlotWrapper;

import javax.swing.*;

/**
 * Model for the {@link PlotsAndButton} class.
 *
 * @author MTomczyk
 */
public class PlotsAndButtonModel extends PlotsWrapperModel
{

    /**
     * Parameterized constructor.
     *
     * @param plotsWrapper reference to plots wrapper.
     * @param wrappers     plots to be displayed (wrapped).
     */
    public PlotsAndButtonModel(AbstractPlotsWrapper plotsWrapper, AbstractPlotWrapper[] wrappers)
    {
        super(plotsWrapper, wrappers);
    }

    /**
     * Reference to the button.
     */
    private JButton _button;

    /**
     * Auxiliary method for instantiating the button.
     *
     * @param label button label.
     */
    protected void createButton(String label)
    {
        _button = new JButton(label);
    }

    /**
     * Button getter.
     *
     * @return button getter.
     */
    protected JButton getButton()
    {
        return _button;
    }
}
