package component.cube;


import color.Color;
import com.jogamp.opengl.GL2;
import component.AbstractVBOComponent;
import container.PlotContainer;
import gl.IVBOComponent;
import gl.VBOManager;
import gl.vboutils.VBOUtils;
import scheme.AbstractScheme;
import scheme.enums.ColorFields;
import space.Dimension;

/**
 * Cube object. Draws edges/boundaries of the subspace to be drawn.
 * Cube is a simplified term. The shape can also be a cuboid.
 *
 * @author MTomczyk
 */
public class Cube extends AbstractVBOComponent implements IVBOComponent
{
    /**
     * Parameterized constructor.
     *
     * @param PC reference to the plot container
     */
    public Cube(PlotContainer PC)
    {
        super("Cube", PC);
    }

    /**
     * Cube color.
     */
    private Color _edgeColor = null;

    /**
     * Can be called to draw object.
     *
     * @param gl open gl rendering context
     */
    @Override
    public void draw(GL2 gl)
    {
        gl.glLineWidth(1.0f);

        if (_edgeColor == null) return;

        if (_vbo != null)
        {
            gl.glColor4f(_edgeColor._r, _edgeColor._g, _edgeColor._b, _edgeColor._a);
            _vbo.render(gl);
        }
    }

    /**
     * Called to update component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        _edgeColor = scheme.getColors(_surpassedColors, ColorFields.CUBE_3D_EDGE);
    }

    /**
     * Can be called to instantiate VBO buffers (but the data is not yet transferred to GPU).
     */
    @Override
    public void createBuffers()
    {
        Dimension [] D = _PC.getDrawingArea().getRenderingData().getCopyOfProjectionBounds();
        float[] vertices = VBOUtils.getCuboidVertices(D);
        short [] indices = VBOUtils._cuboidIndices;
        _vbo = new VBOManager(vertices, indices, null, GL2.GL_LINES, 3, 0);
    }
}
