package tools.ivemo.heatmap.visualization;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

import javax.swing.*;
import java.awt.*;

/**
 * Contains some common functions.
 *
 * @author MTomczyk
 */
public class GUIUtils
{
    /**
     * Establishes the heatmap panel layout (grid bag layout)
     *
     * @param parent     parent wrapper
     * @param plot       plot (top object)
     * @param rangePanel range panel (bottom object)
     */
    public static void establishLayout(AbstractPlotWrapper parent, AbstractPlot plot, JPanel rangePanel)
    {
        GridBagLayout GBL = new GridBagLayout();
        parent.setLayout(GBL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0d;
        gbc.weighty = 0.98d;
        parent.add(plot, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0d;
        gbc.weighty = 0.02d;
        parent.add(rangePanel, gbc);
    }
}
