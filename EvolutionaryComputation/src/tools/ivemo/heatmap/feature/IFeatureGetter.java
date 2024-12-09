package tools.ivemo.heatmap.feature;

import population.Specimen;

/**
 * Interface for objects responsible for deriving specimen-related features.
 *
 * @author MTomczyk
 */

public interface IFeatureGetter
{
    /**
     * Returns a feature value linked to the specimen.
     *
     * @param specimen          input specimen
     * @param generation        generation no. when the method is called
     * @param steadyStateRepeat steady-state repeat no. when the method is called
     * @return feature value
     */
    double getFeature(Specimen specimen, int generation, int steadyStateRepeat);
}
