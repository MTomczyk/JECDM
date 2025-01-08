package dataset.painter.arrowsutils;

import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.enums.Arrow;
import space.Vector;

/**
 * Provides auxiliary method associated with triangular (reversed) arrows (3D).
 *
 * @author MTomczyk
 */
public class Triangular3D implements IArrowProjectionDataConstructor
{
    /**
     * Returns the arrow projected data array (2D case) to be initialized.
     * For TRIANGULAR_3D and TRIANGULAR_REVERSED_3D.
     *
     * @param noLinesWithArrows no lines in the data set with arrows.
     * @return 2 * noLinesWithArrows
     */
    @Override
    public float[] getArrowProjectedDataArray(int noLinesWithArrows)
    {
        return new float[4 * 3 * noLinesWithArrows];
    }

    /**
     * Returns the stride of the arrow projected data array (3D case) to be initialized.
     * For TRIANGULAR_3D and TRIANGULAR_REVERSED_3D.
     *
     * @return 12
     */
    @Override
    public int getArrowProjectedDataSizeArrowStride()
    {
        return 12;
    }

    /**
     * Returns the number of vertices.
     *
     * @return no. vertices
     */
    @Override
    public int getNoVertices()
    {
        return 4;
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
    public void fillArrowProjectedDataArray(float[] array, int offset, float[] lines, int lOffset, float[] dv, float length,
                                            float width, Arrow arrow)
    {
        if ((!arrow.equals(Arrow.TRIANGULAR_3D)) &&
                (!arrow.equals(Arrow.TRIANGULAR_REVERSED_3D))) return;

        float hs = length / 2.0f;

        float[] pdv = Vector.getPerpendicularVector3D(dv);
        if (pdv == null)
        {
            array[offset] = lines[lOffset];
            array[offset + 1] = lines[lOffset + 1];
            array[offset + 2] = lines[lOffset + 2];
            array[offset + 3] = lines[lOffset];
            array[offset + 4] = lines[lOffset + 1];
            array[offset + 5] = lines[lOffset + 2];
            array[offset + 6] = lines[lOffset];
            array[offset + 7] = lines[lOffset + 1];
            array[offset + 8] = lines[lOffset + 2];
            array[offset + 9] = lines[lOffset];
            array[offset + 10] = lines[lOffset + 1];
            array[offset + 11] = lines[lOffset + 2];
            return;
        }

        dv = dv.clone();
        float[] ppdv = Vector.getCrossProduct3D(dv, pdv);
        Vector.normalize(ppdv);

        float shift = width * (float) Math.sqrt(3.0f);
        ppdv[0] *= shift;
        ppdv[1] *= shift;
        ppdv[2] *= shift;

        dv[0] *= hs;
        dv[1] *= hs;
        dv[2] *= hs;

        if (arrow == Arrow.TRIANGULAR_3D)
        {
            array[offset] = lines[lOffset] + dv[0];
            array[offset + 1] = lines[lOffset + 1] + dv[1];
            array[offset + 2] = lines[lOffset + 2] + dv[2];
            dv[0] = lines[lOffset] - dv[0];
            dv[1] = lines[lOffset + 1] - dv[1];
            dv[2] = lines[lOffset + 2] - dv[2];
        }
        else
        {
            array[offset] = lines[lOffset] - dv[0];
            array[offset + 1] = lines[lOffset + 1] - dv[1];
            array[offset + 2] = lines[lOffset + 2] - dv[2];
            dv[0] = lines[lOffset] + dv[0];
            dv[1] = lines[lOffset + 1] + dv[1];
            dv[2] = lines[lOffset + 2] + dv[2];
        }

        array[offset + 3] = dv[0] + pdv[0] * 2.0f * width;
        array[offset + 4] = dv[1] + pdv[1] * 2.0f * width;
        array[offset + 5] = dv[2] + pdv[2] * 2.0f * width;

        array[offset + 6] = dv[0] - pdv[0] * width + ppdv[0];
        array[offset + 7] = dv[1] - pdv[1] * width + ppdv[1];
        array[offset + 8] = dv[2] - pdv[2] * width + ppdv[2];

        array[offset + 9] = dv[0] - pdv[0] * width - ppdv[0];
        array[offset + 10] = dv[1] - pdv[1] * width - ppdv[1];
        array[offset + 11] = dv[2] - pdv[2] * width - ppdv[2];
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
        return as._width;
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
        return as._size;
    }
}
