package tools.ivemo.heatmap.feature;

import population.Specimen;

/**
 * Always returns a generation in which an input solution was generated.
 *
 * @author MTomczyk
 */

public class GenerationWhenConstructed implements IFeatureGetter
{
    @Override
    public double getFeature(Specimen specimen, int generation, int steadyStateRepeat)
    {
        return specimen.getID()._generation;
    }
}
