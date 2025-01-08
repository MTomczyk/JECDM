package dataset.painter.arrowsutils;

import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.enums.Arrow;

/**
 * Interface for auxiliary classes responsible for filling data in {@link dataset.painter.IDSArrows}.
 *
 * @author MTomczyk
 */
public interface IArrowProjectionDataConstructor
{
    /**
     * Returns the arrow projected data array to be initialized.
     *
     * @param noLinesWithArrows no lines in the data set with arrows.
     * @return data array
     */
    float[] getArrowProjectedDataArray(int noLinesWithArrows);

    /**
     * Returns the stride of the arrow projected data array to be initialized.
     *
     * @return 2
     */
    int getArrowProjectedDataSizeArrowStride();

    /**
     * Returns the number of vertices.
     *
     * @return no. vertices
     */
    int getNoVertices();

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
    void fillArrowProjectedDataArray(float[] array, int offset, float[] lines, int lOffset, float[] dv, float length, float width, Arrow arrow);

    /**
     * Returns the width that corresponds the projection (e.g., the width will be relative for 2D java swing; constant for 3D Open GL).
     *
     * @param as arrow style (provides reference width)
     * @param GC global container
     * @param PC plot container
     * @return modified width
     */
    float getModifiedWidth(ArrowStyle as, GlobalContainer GC, PlotContainer PC);

    /**
     * Returns the arrow length that corresponds the projection (e.g., the width will be relative for 2D java swing; constant for 3D Open GL).
     *
     * @param as arrow style (provides reference length (size))
     * @param GC global container
     * @param PC plot container
     * @return modified height
     */
    float getModifiedLength(ArrowStyle as, GlobalContainer GC, PlotContainer PC);
}
