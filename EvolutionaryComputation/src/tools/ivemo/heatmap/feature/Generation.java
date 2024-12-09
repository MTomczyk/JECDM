package tools.ivemo.heatmap.feature;

import population.Specimen;

/**
 * Always returns a generation in which an input solution exists in the population (does not equal a moment
 * when a solution was generated, see {@link tools.ivemo.heatmap.feature.GenerationWhenConstructed}).
 *
 * @author MTomczyk
 */

public class Generation implements IFeatureGetter
{
    @Override
    public double getFeature(Specimen specimen, int generation, int steadyStateRepeat)
    {
        return generation;
    }
}
