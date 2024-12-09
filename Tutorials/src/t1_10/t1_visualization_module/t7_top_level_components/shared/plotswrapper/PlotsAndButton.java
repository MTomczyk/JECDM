package t1_10.t1_visualization_module.t7_top_level_components.shared.plotswrapper;

import color.Color;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.PlotsWrapperModel;
import plotswrapper.TwoPlotsHorizontally;
import plotwrapper.AbstractPlotWrapper;

import java.awt.*;

/**
 * Plot container that organizes two plots horizontally.
 *
 * @author MTomczyk
 */
public class PlotsAndButton extends TwoPlotsHorizontally
{
    /**
     * Params container.
     */
    public static class Params extends TwoPlotsHorizontally.Params
    {
        /**
         * Button label.
         */
        public final String _buttonLabel;

        /**
         * Parameterized constructor.
         *
         * @param leftWrapper  left plot wrapper
         * @param rightWrapper right plot wrapper
         * @param buttonLabel  button label
         */
        public Params(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper, String buttonLabel)
        {
            super(leftWrapper, rightWrapper);
            _buttonLabel = buttonLabel;
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public PlotsAndButton(Params p)
    {
        super(p);
    }

    /**
     * Parameterized constructor.
     *
     * @param leftWrapper  left plot wrapper
     * @param rightWrapper right plot wrapper
     * @param buttonLabel  button label
     */
    public PlotsAndButton(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper, String buttonLabel)
    {
        this(new Params(leftWrapper, rightWrapper, buttonLabel));
    }

    /**
     * Method for creating the model instance.
     *
     * @param p params container
     * @return plots wrapper model
     */
    @Override
    protected PlotsWrapperModel getModelInstance(AbstractPlotsWrapper.Params p)
    {
        return new PlotsAndButtonModel(this, p._wrappers);
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    @Override
    protected void instantiateGUI(AbstractPlotsWrapper.Params p)
    {
        setBackground(Color.WHITE);
        PlotsAndButton.Params pp = (PlotsAndButton.Params) p;
        ((PlotsAndButtonModel) _M).createButton(pp._buttonLabel);
    }


    /**
     * Instantiates the layout. The two wrapped plots are assumed to occupy most of the upper space (placed left and right,
     * equally spanned), while the button is assigned to the lower part of JPanel.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(AbstractPlotsWrapper.Params p)
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
        gbc.weightx = 50.0d;
        gbc.weighty = 99.0d;
        GBL.setConstraints(_M._wrappers[0], gbc);
        add(_M._wrappers[0]);

        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 50.0d;
        gbc.weighty = 99.0d;
        GBL.setConstraints(_M._wrappers[1], gbc);
        add(_M._wrappers[1]);

        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 100.0d;
        gbc.weighty = 1.0d;

        GBL.setConstraints(((PlotsAndButtonModel) _M).getButton(), gbc);
        add(((PlotsAndButtonModel) _M).getButton());
    }

}
