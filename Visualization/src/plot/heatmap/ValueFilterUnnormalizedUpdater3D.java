package plot.heatmap;

import space.Range;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating the heatmap mask filter
 *
 * @author MTomczyk
 */
class ValueFilterUnnormalizedUpdater3D extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer model.
     */
    private final HeatmapLayerModel _layerModel;

    /**
     * Left normalized bound [0, 1] (should be strictly smaller than the right bound).
     */
    private final double _leftNormalizedBound;

    /**
     * Right normalized bound [0, 1] (should be strictly smaller than the right bound).
     */
    private final double _rightNormalizedBound;

    /**
     * This constructor assumes that the user can provide bounds in the normalized [0,1] space.
     * It is the construct's responsibility fto determine the corresponding bounds in the original data space.
     * The left bound should be smaller/equal to the right, and both should be in [0, 1] range.
     * If these conditions are not satisfied, the bounds are set to 0 and 1, respectively.
     * The method uses the current display range stored by the heatmap model (so the filter may be required to be
     * set up again after the display range is changed).
     *
     * @param layerModel           heatmap layer model
     * @param leftNormalizedBound  left normalized bound [0, 1] (should be smaller/equal to the right bound)
     * @param rightNormalizedBound left normalized bound [0, 1] (should be smaller/equal to the left bound)
     */
    public ValueFilterUnnormalizedUpdater3D(HeatmapLayerModel layerModel,
                                            double leftNormalizedBound,
                                            double rightNormalizedBound)
    {
        if ((Double.compare(leftNormalizedBound, rightNormalizedBound) > 0)
                || (Double.compare(leftNormalizedBound, 0.0d) < 0) || (Double.compare(leftNormalizedBound, 1.0d) > 0)
                || (Double.compare(rightNormalizedBound, 0.0d) < 0) || (Double.compare(rightNormalizedBound, 1.0d) > 0))
        {
            leftNormalizedBound = 0.0d;
            rightNormalizedBound = 1.0d;
        }

        _leftNormalizedBound = leftNormalizedBound;
        _rightNormalizedBound = rightNormalizedBound;
        _layerModel = layerModel;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        double left = _layerModel.getHeatmapDisplayRange().getNormalizer().getUnnormalized(_leftNormalizedBound);
        double right = _layerModel.getHeatmapDisplayRange().getNormalizer().getUnnormalized(_rightNormalizedBound);
        _layerModel.setValueFilter(new Range(left, right));
        _layerModel.determineTheIndicesInterval();
        notifyTermination();
        return null;
    }

    /**
     * Catches exceptions.
     */
    @Override
    public void done()
    {
        if (isCancelled()) return;
        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
