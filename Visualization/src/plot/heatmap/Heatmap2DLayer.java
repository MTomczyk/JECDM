package plot.heatmap;

import color.gradient.Gradient;
import component.AbstractSwingComponent;
import component.axis.ticksupdater.ITicksDataGetter;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import plot.heatmap.utils.Coords;
import space.normalization.INormalization;
import space.normalization.minmax.AbstractMinMaxNormalization;
import utils.Projection;

import java.awt.*;

/**
 * Additional layer that can be used to display a heatmap (Swing component).
 *
 * @author MTomczyk
 */
public class Heatmap2DLayer extends AbstractSwingComponent
{
    /**
     * Heatmap model.
     */
    protected HeatmapLayerModel _HM;


    /**
     * Parameterized constructor.
     *
     * @param p  params container
     * @param PC plot container
     */
    public Heatmap2DLayer(Heatmap2D.Params p, PlotContainer PC)
    {
        super("Heatmap 2D layer", PC);
        _HM = new HeatmapLayerModel(p._xDiv, p._yDiv, 1,
                new AbstractMinMaxNormalization[]{p._xBucketCoordsNormalizer, p._yBucketCoordsNormalizer},
                2, p._gradient, p._heatmapDisplayRange);
        _HM.setMask(new boolean[][][]{null});
    }


    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        boolean sortedMode = _HM.isInSortedMode();
        double[][][] rawData = _HM.getRawData();
        Coords[] sortedCoords = _HM.getSortedCoords();
        double[] sortedValues = _HM.getSortedValues();
        Gradient gradient = _HM.getHeatmapGradient();
        DisplayRangesManager DRM = _HM.getHeatmapDRM();
        ITicksDataGetter XT = _HM.getXBucketCoordsTicksDataGetter();
        ITicksDataGetter YT = _HM.getYBucketCoordsTicksDataGetter();
        DisplayRangesManager.DisplayRange dr = _HM.getHeatmapDisplayRange();

        if (sortedMode)
        {
            if (sortedCoords == null) return;
            if (sortedValues == null) return;
            if (sortedCoords.length != sortedValues.length) return;
            if (sortedCoords.length < 1) return;
        }
        else
        {
            if (rawData == null) return;
            if (rawData.length < 1) return;
            if (rawData[0] == null) return;
        }

        if (DRM == null) return;
        if (gradient == null) return;
        if (XT == null) return;
        if (YT == null) return;
        if (dr == null) return;

        Graphics g2 = g.create();

        INormalization xNormalizer = DRM.getDisplayRangeForXAxis().getNormalizer();
        INormalization yNormalizer = DRM.getDisplayRangeForYAxis().getNormalizer();

        float[] xt = XT.getTicksLocations().clone();
        float[] yt = YT.getTicksLocations().clone();

        int y;
        int ny = Projection.getP(_translationVector[1] + (float) (1.0d - yNormalizer.getNormalized(yt[0])) * (_primaryDrawingArea.height - 1));
        int x;
        int nx;

        if (sortedMode) // use sorted coords
        {
            Coords c;
            double v;
            int left = _HM.getLeftIndicesBound();
            int right = _HM.getRightIndicesBound();
            for (int i = left; i <= right; i++)
            {
                c = sortedCoords[i];
                if (_HM.isMasked(c._x, c._y)) continue;

                v = sortedValues[i];
                y = Projection.getP(_translationVector[1] + (float) (1.0d - yNormalizer.getNormalized(yt[c._y])) * (_primaryDrawingArea.height - 1));
                ny = Projection.getP(_translationVector[1] + (float) (1.0d - yNormalizer.getNormalized(yt[c._y + 1])) * (_primaryDrawingArea.height - 1));
                x = Projection.getP(_translationVector[0] + (float) (xNormalizer.getNormalized(xt[c._x])) * (_primaryDrawingArea.width - 1));
                nx = Projection.getP(_translationVector[0] + (float) (xNormalizer.getNormalized(xt[c._x + 1])) * (_primaryDrawingArea.width - 1));
                paintBucket(dr, gradient, v, g2, x, nx, y, ny);
            }
        }
        else // use raw data
        {
            double[][] DATA = rawData[0];
            for (int j = 0; j < _HM.getDivisions()[1]; j++) // rows
            {
                if (j > DATA.length - 1) break;
                y = ny;
                ny = Projection.getP(_translationVector[1] + (float) (1.0d - yNormalizer.getNormalized(yt[j + 1])) * (_primaryDrawingArea.height - 1));
                if (DATA[j] == null) continue;
                nx = Projection.getP(_translationVector[0] + (float) xNormalizer.getNormalized(xt[0]) * (_primaryDrawingArea.width - 1));

                for (int i = 0; i < _HM.getDivisions()[0]; i++) // columns
                {
                    x = nx;
                    nx = Projection.getP(_translationVector[0] + (float) xNormalizer.getNormalized(xt[i + 1]) * (_primaryDrawingArea.width - 1));
                    // have y, ny, x, nx (buckets rectangle coordinates)
                    if (Double.compare(DATA[j][i], Double.NEGATIVE_INFINITY) == 0) continue; // skip the value
                    if (i > DATA[j].length - 1) continue;
                    if (_HM.isMasked(i, j)) continue;
                    if (!_HM.isValueAccepted(DATA[j][i], true)) continue;
                    paintBucket(dr, gradient, DATA[j][i], g2, x, nx, y, ny);
                }
            }
        }
        g2.dispose();
    }

    /**
     * Auxiliary method that paints the bucket given the coordinates and the bucket's value.
     *
     * @param dr       heatmap display range
     * @param gradient heatmap gradient
     * @param value    bucket value
     * @param g2       Java Awing graphics context
     * @param x1       one of the x-coordinate
     * @param x2       the other x-coordinate
     * @param y1       one of the y-coordinates
     * @param y2       y-coordinate
     */
    private void paintBucket(DisplayRangesManager.DisplayRange dr, Gradient gradient,
                             double value, Graphics g2, int x1, int x2, int y1, int y2)
    {
        double norm = dr.getNormalizer().getNormalized(value);
        if ((Double.compare(norm, 0) < 0) || (Double.compare(norm, 1.0d) > 0)) return;
        color.Color color = gradient.getColor((float) norm);
        g2.setColor(color);
        int dx = Math.min(x1, x2);
        int dy = Math.min(y1, y2);
        int dnx = Math.max(x1, x2);
        int dny = Math.max(y1, y2);
        g2.fillRect(dx, dy, dnx - dx, dny - dy);
    }


    /**
     * Can be called to clear memory.
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        super.dispose();
        _HM.dispose();
    }

}
