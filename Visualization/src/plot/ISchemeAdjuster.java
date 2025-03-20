package plot;

import scheme.AbstractScheme;

/**
 * Simple interface used for plot-related classes to adjust the scheme object when creating the plot instance on the
 * fly (e.g., {@link Plot2DFactory#getPlot(String, String, double)}).
 *
 * @author MTomczyk
 */
public interface ISchemeAdjuster
{
    /**
     * The main method's signature.
     *
     * @param scheme scheme object to be modified
     */
    void adjust(AbstractScheme scheme);
}
