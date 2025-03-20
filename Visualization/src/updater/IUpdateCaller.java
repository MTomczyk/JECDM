package updater;

import dataset.IDataSet;
import plot.PlotModel;

import java.util.ArrayList;

/**
 * Auxiliary interface to which the final update of the plot data set is delegated from {@link DataUpdater}.
 *
 * @author MTomczyk
 */
public interface IUpdateCaller
{
    /**
     * The main method's signature.
     *
     * @param model        plot model
     * @param dataSets     data set to be supplied to plot model
     * @param updateDRs    if true, the display ranges update is supposed to be called
     * @param updateLegend if true, the legend update is supposed to be called
     */
    void update(PlotModel model, ArrayList<IDataSet> dataSets, boolean updateDRs, boolean updateLegend);
}
