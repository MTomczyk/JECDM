package t1_10.t1_visualization_module.t7_top_level_components.shared.plotwrapper;

import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapperModel;

import javax.swing.*;

/**
 * Model for the {@link PlotAndButton} class.
 *
 * @author MTomczyk
 */
public class PlotAndButtonModel extends PlotWrapperModel
{

    /**
     * Parameterized constructor.
     *
     * @param plotWrapper reference to plot wrapper.
     */
    public PlotAndButtonModel(AbstractPlotWrapper plotWrapper)
    {
        super(plotWrapper);
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
