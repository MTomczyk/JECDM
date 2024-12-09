package t1_10.t4_decision_support_module.t4_decision_support_system.t2_use_case;

import updater.AbstractDataProcessor;
import updater.IDataProcessor;

/**
 * Splits the data produced by {@link HistorySource} into pairs of double [] rows, each stored as a different linked
 * list element. Further, these elements are interlaced with null to illustrate finally independent lines.
 *
 * @author MTomczyk
 */
public class SplitProcessor extends AbstractDataProcessor implements IDataProcessor
{
    /**
     * Parameterized constructor (enable cumulative mode; disable interlace nulls).
     */
    public SplitProcessor()
    {
        super(new Params(true, false));
    }

    /**
     * Can be called to update the internal data maintained by the updater.
     * (takes control over the "\_cumulatedData").
     *
     * @param sourceData new data to be processed
     */
    @Override
    public void update(double[][] sourceData)
    {
        if (sourceData == null) return;
        for (int off = 0; off < sourceData.length; off += 2)
        {
            if (sourceData[off] == null) continue;
            _cumulatedData.add(new double[][]{sourceData[off], sourceData[off + 1]});
            _cumulatedData.add(null);
        }
    }
}
