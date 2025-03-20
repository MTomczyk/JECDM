package updater;

import dataset.IDataSet;
import plot.PlotModel;

import java.util.ArrayList;

/**
 * Default implementation of {@link IUpdateCaller}.
 *
 * @author MTomczyk
 */
public class DefaultUpdateCaller implements IUpdateCaller
{
    /**
     * The main method's signature.
     *
     * @param model        plot model
     * @param dataSets     data set to be supplied to plot model
     * @param updateDRs    if true, the display ranges update is supposed to be called
     * @param updateLegend if true, the legend update is supposed to be called
     */
    @Override
    public void update(PlotModel model, ArrayList<IDataSet> dataSets, boolean updateDRs, boolean updateLegend)
    {
        model.setDataSets(dataSets, updateDRs, updateLegend);
    }
}
