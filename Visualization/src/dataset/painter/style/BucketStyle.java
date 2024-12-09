package dataset.painter.style;

import color.gradient.Color;
import dataset.painter.style.enums.Bucket;
import dataset.painter.style.size.IRelativeSize;
import dataset.painter.style.size.RelativeToMinDrawingAreaBoundsSize;

/**
 * Encapsulates various parameters related to a bucket style (for 3D rendering/{@link plot.heatmap.Heatmap3D}).
 *
 * @author MTomczyk
 */


public class BucketStyle
{
    /**
     * Bucket style.
     */
    public final Bucket _style;

    /**
     * Bucket size (used when the bucket style == POINT; relative size from 0 to 1; can be 0.0 -> object is not drawn).
     */
    public final float _size;

    /**
     * Object responsible for establishing relative size.
     */
    public IRelativeSize _relativeSize = new RelativeToMinDrawingAreaBoundsSize();

    /**
     * If true, the buckets are filled with color.
     */
    public final boolean _fillBuckets;

    /**
     * If true, bucket edges are drawn.
     */
    public final boolean _drawEdges;

    /**
     * Edge style (if null -> edges are not drawn).
     */
    public final LineStyle _edgeStyle;


    /**
     * Returns a default style: buckets are filled (rectangular cuboid); but edges are not drawn.
     *
     * @return bucket style.
     */
    public static BucketStyle getDefault()
    {
        return new BucketStyle();
    }

    /**
     * Returns the following configuration: buckets are rendered as points using GL_POINT mode, edges are not drawn
     *
     * @param size point size
     * @return bucket style
     */
    public static BucketStyle getGlPointsStyle(float size)
    {
        return new BucketStyle(size);
    }

    /**
     * Returns the following configuration: buckets are not filled; but their edges are drawn (edges of a rectangular cuboid).
     *
     * @param lineStyle line style
     * @return bucket style
     */
    public static BucketStyle getOnlyEdgesStyle(LineStyle lineStyle)
    {
        return new BucketStyle(null, 0.0f, lineStyle);
    }


    /**
     * Returns the following configuration: buckets are filled (rectangular cuboid); and their edges are drawn
     * (edges of a rectangular cuboid).
     *
     * @param lineStyle line style
     * @return bucket style
     */
    public static BucketStyle getFillAndEdges(LineStyle lineStyle)
    {
        return new BucketStyle(Bucket.CUBE_3D, 0.0f, lineStyle);
    }


    /**
     * Default constructor. Bucket style = CUBE, edges are not drawn.
     */
    public BucketStyle()
    {
        this(Bucket.CUBE_3D, 0.0f, null);
    }

    /**
     * Parameterized constructor. Buckets are drawn as points (GL_POINTS). Edges are not drawn.
     *
     * @param size bucket size (points)
     */
    public BucketStyle(float size)
    {
        this(Bucket.POINT_3D, size, null);
    }


    /**
     * Parameterized constructor (buckets are not filled).
     *
     * @param edgeWidth edge width
     * @param edgeColor edge color
     */
    public BucketStyle(float edgeWidth, Color edgeColor)
    {
        this(null, 0.0f, new LineStyle(edgeWidth, edgeColor));
    }

    /**
     * Parameterized constructor (buckets are not filled).
     *
     * @param edgeStyle edge style
     */
    public BucketStyle(LineStyle edgeStyle)
    {
        this(null, 0.0f, edgeStyle);
    }


    /**
     * Parameterized constructor (set size to 0).
     *
     * @param style     bucket style
     * @param edgeStyle edge style
     */
    public BucketStyle(Bucket style, LineStyle edgeStyle)
    {
        this(style, 0.0f, edgeStyle);
    }

    /**
     * Parameterized constructor.
     *
     * @param style     bucket style
     * @param size      bucket size (used when the style == POINT)
     * @param edgeStyle edge style
     */
    public BucketStyle(Bucket style, float size, LineStyle edgeStyle)
    {
        _size = size;
        _fillBuckets = style != null;
        _style = style;
        if (edgeStyle != null)
        {
            _drawEdges = edgeStyle.isDrawable();
            _edgeStyle = edgeStyle;
        }
        else
        {
            _drawEdges = true;
            _edgeStyle = null;
        }
    }

}
