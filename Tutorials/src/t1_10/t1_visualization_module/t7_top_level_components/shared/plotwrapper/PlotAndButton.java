package t1_10.t1_visualization_module.t7_top_level_components.shared.plotwrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapperModel;

import java.awt.*;

/**
 * Extension of {@link AbstractPlotWrapper} that contains a panel and a button.
 *
 * @author MTomczyk
 */
public class PlotAndButton extends AbstractPlotWrapper
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlotWrapper.Params
    {
        /**
         * Button label.
         */
        public final String _buttonLabel;

        /**
         * Parameterized constructor.
         *
         * @param plot        plot to be displayed
         * @param buttonLabel button label
         */
        public Params(AbstractPlot plot, String buttonLabel)
        {
            super(plot);
            _buttonLabel = buttonLabel;
        }
    }


    /**
     * Parameterized constructor.
     *
     * @param plot        plot to be displayed
     * @param buttonLabel button label
     */
    public PlotAndButton(AbstractPlot plot, String buttonLabel)
    {
        this(new Params(plot, buttonLabel));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public PlotAndButton(Params p)
    {
        super(p);
    }

    /**
     * Method for creating the model instance (creates a custom model).
     *
     * @param p params container
     * @return plot wrapper model
     */
    @Override
    protected PlotWrapperModel getModelInstance(AbstractPlotWrapper.Params p)
    {
        return new PlotAndButtonModel(this);
    }


    /**
     * This custom "instantiateGUI" method initialized the button.
     *
     * @param p params container
     */
    @Override
    protected void instantiateGUI(AbstractPlotWrapper.Params p)
    {
        setBackground(Color.WHITE);
        Params pp = (Params) p;
        ((PlotAndButtonModel) _M).createButton(pp._buttonLabel);
    }

    /**
     * Instantiates the layout. The wrapped plot is assumed to occupy most of the upper space, while the button is assigned to the lower part of JPanel.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(AbstractPlotWrapper.Params p)
    {
        GridBagLayout GBL = new GridBagLayout();
        setLayout(GBL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 99.0d;
        gbc.weighty = 99.0d;
        GBL.setConstraints(_M.getPlot(), gbc);
        add(_M.getPlot());

     //   gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0d;
        gbc.weighty = 1.0d;

        GBL.setConstraints(((PlotAndButtonModel) _M).getButton(), gbc);
        add(((PlotAndButtonModel) _M).getButton());
    }
}
