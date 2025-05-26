package plot;

import frame.Frame;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link frame.Frame} that are
 * supposed to wrap a single 2D plot {@link plot.Plot2D}.
 *
 * @author MTomczyk
 */
public interface IFramePlot2DConstructor
{
    /**
     * The main method.
     *
     * @param plot2D plot to be wrapped
     * @return frame
     */
    Frame getFrame(Plot2D plot2D);
}
