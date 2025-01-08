package dataset.painter.arrowsutils;

import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.enums.Arrow;
import space.Vector;


/**
 * Provides auxiliary method associated with triangular (reversed) arrows (2D).
 *
 * @author MTomczyk
 */
public class Triangular2D implements IArrowProjectionDataConstructor
{
    /**
     * Returns the arrow projected data array (2D case) to be initialized.
     * For TRIANGULAR_2D and TRIANGULAR_REVERSED_2D.
     *
     * @param noLinesWithArrows no lines in the data set with arrows.
     * @return 2 * noLinesWithArrows
     */
    @Override
    public float[] getArrowProjectedDataArray(int noLinesWithArrows)
    {
        return new float[6 * noLinesWithArrows];
    }

    /**
     * Returns the stride of the arrow projected data array (2D case) to be initialized.
     * For TRIANGULAR_2D and TRIANGULAR_REVERSED_2D.
     *
     * @return 6
     */
    @Override
    public int getArrowProjectedDataSizeArrowStride()
    {
        return 6;
    }

    /**
     * Returns the number of vertices.
     *
     * @return no. vertices
     */
    @Override
    public int getNoVertices()
    {
        return 3;
    }

    /**
     * Fills the arrow projected data array entry (2D case; single arrow).
     *
     * @param array   array to be filled
     * @param offset  offset in the array
     * @param lines   projected lines data
     * @param lOffset offset in lines pointing the data point coordinate
     * @param dv      line normalized direction vector
     * @param length  arrow length
     * @param width   arrow width
     * @param arrow   arrow style
     */
    @Override
    public void fillArrowProjectedDataArray(float[] array, int offset, float[] lines, int lOffset, float[] dv,
                                            float length, float width, Arrow arrow)
    {
        if ((!arrow.equals(Arrow.TRIANGULAR_2D)) &&
                (!arrow.equals(Arrow.TRIANGULAR_REVERSED_2D))) return;

        float hs = length / 2.0f;
        float hw = width / 2.0f;

        float[] pdv = Vector.getPerpendicularVector2D(dv);
        if (pdv == null)
        {
            array[offset] = lines[lOffset];
            array[offset + 1] = lines[lOffset + 1];
            array[offset + 2] = lines[lOffset];
            array[offset + 3] = lines[lOffset + 1];
            array[offset + 4] = lines[lOffset];
            array[offset + 5] = lines[lOffset + 1];
            return;
        }

        dv = dv.clone();
        dv[0] *= hs;
        dv[1] *= hs;

        pdv[0] *= hw;
        pdv[1] *= hw;

        if (arrow == Arrow.TRIANGULAR_2D)
        {
            array[offset] = lines[lOffset] + dv[0];
            array[offset + 1] = lines[lOffset + 1] + dv[1];
            dv[0] = lines[lOffset] - dv[0];
            dv[1] = lines[lOffset + 1] - dv[1];
        }
        else
        {
            array[offset] = lines[lOffset] - dv[0];
            array[offset + 1] = lines[lOffset + 1] - dv[1];
            dv[0] = lines[lOffset] + dv[0];
            dv[1] = lines[lOffset + 1] + dv[1];
        }

        array[offset + 2] = dv[0] - pdv[0];
        array[offset + 3] = dv[1] - pdv[1];

        array[offset + 4] = dv[0] + pdv[0];
        array[offset + 5] = dv[1] + pdv[1];
    }

    /**
     * Returns the width that corresponds the projection (e.g., the width will be relative for 2D java swing; constant for 3D Open GL).
     *
     * @param as arrow style (provides reference width)
     * @param GC global container
     * @param PC plot container
     * @return modified width
     */
    @Override
    public float getModifiedWidth(ArrowStyle as, GlobalContainer GC, PlotContainer PC)
    {
        return as._relativeSize.getSize(GC, PC, as._width);
    }

    /**
     * Returns the arrow height that corresponds the projection (e.g., the width will be relative for 2D java swing; constant for 3D Open GL).
     *
     * @param as arrow style (provides reference height)
     * @param GC global container
     * @param PC plot container
     * @return modified height
     */
    @Override
    public float getModifiedLength(ArrowStyle as, GlobalContainer GC, PlotContainer PC)
    {
        return as._relativeSize.getSize(GC, PC, as._size);
    }
}
