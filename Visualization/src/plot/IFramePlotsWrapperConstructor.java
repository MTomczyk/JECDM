package plot;

import frame.Frame;
import plotswrapper.AbstractPlotsWrapper;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link Frame} that are
 * supposed to wrap plots wrappers.
 *
 * @author MTomczyk
 */
public interface IFramePlotsWrapperConstructor
{
    /**
     * The main method.
     *
     * @param plotsWrapper plots wrapper
     * @return frame
     */
    Frame getFrame(AbstractPlotsWrapper plotsWrapper);
}
