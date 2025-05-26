package dataset.painter.style;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link LineStyle}.
 *
 * @author MTomczyk
 */
public interface ILineStyleConstructor
{
    /**
     * The main method for constructing and returning a line style.
     *
     * @return line style
     */
    LineStyle getLineStyle();
}
