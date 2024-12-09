package dataset.painter.style.enums;

/**
 * Line styles (for 2D and 3D rendering).
 */
public enum Line
{
    /**
     * Normal, straight line (default for 2D and 3D, for 3D the line sizes are determined based on the raster size).
     */
    REGULAR,

    /**
     * Experimental: Mode for the 3D rendering (only for lines data; NOT marker edges)
     * Lines are represented as polygons stretchered along the lines imposed by the coordinates.
     * The line joints are quads.
     */
    POLY_QUAD,

    /**
     * Experimental: Mode for the 3D rendering (only for lines data; NOT marker edges)
     * Lines are represented as polygons stretchered along the lines imposed by the coordinates.
     * The line joints are octagons.
     */
    POLY_OCTO,
}
