package dataset.painter.style;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link MarkerStyle}.
 *
 * @author MTomczyk
 */
public interface IMarkerStyleConstructor
{
    /**
     * The main method for constructing and returning a marker style.
     *
     * @return line style
     */
    MarkerStyle getMarkerStyle();
}
