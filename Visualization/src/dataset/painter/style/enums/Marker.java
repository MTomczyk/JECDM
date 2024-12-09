package dataset.painter.style.enums;

/**
 * Marker styles (for 2D and 3D rendering).
 */
public enum Marker
{
    /**
     * Circle.
     */
    CIRCLE,

    /**
     * Square
     */
    SQUARE,

    /**
     * Triangle (oriented down).
     */
    TRIANGLE_DOWN,

    /**
     * Triangle (oriented up).
     */
    TRIANGLE_UP,

    /**
     * Triangle (oriented left).
     */
    TRIANGLE_LEFT,

    /**
     * Triangle (oriented right).
     */
    TRIANGLE_RIGHT,

    /**
     * Pentagon.
     */
    PENTAGON,

    /**
     * Star
     */
    STAR,

    /**
     * Hexagon (oriented vertically).
     */
    HEXAGON_VERT,

    /**
     * Hexagon (oriented horizontally).
     */
    HEXAGON_HOR,

    /**
     * Diamond (oriented vertically).
     */
    DIAMOND_VERT,

    /**
     * Diamond (oriented horizontally).
     */
    DIAMOND_HOR,


    /**
     * Cube 3D
     */
    CUBE_3D,

    /**
     * Point 3D. Note: sizes are determined based on the raster size.
     */
    POINT_3D,

    /**
     * Tetrahedron 3D (triangles/oriented up).
     */
    TETRAHEDRON_UP_3D,

    /**
     * Tetrahedron 3D (triangles/oriented down).
     */
    TETRAHEDRON_DOWN_3D,

    /**
     * Tetrahedron 3D (triangles/oriented left).
     */
    TETRAHEDRON_LEFT_3D,

    /**
     * Tetrahedron 3D (triangles/oriented right).
     */
    TETRAHEDRON_RIGHT_3D,

    /**
     * Tetrahedron 3D (triangles/oriented front).
     */
    TETRAHEDRON_FRONT_3D,

    /**
     * Tetrahedron 3D (triangles/oriented back).
     */
    TETRAHEDRON_BACK_3D,

    /**
     * Sphere 3D (icosphere: low number of vertices).
     */
    SPHERE_LOW_POLY_3D,

    /**
     * Sphere 3D (icosphere: medium number of vertices).
     */
    SPHERE_MEDIUM_POLY_3D,

    /**
     * Sphere 3D (icosphere: high number of vertices).
     */
    SPHERE_HIGH_POLY_3D,
}
