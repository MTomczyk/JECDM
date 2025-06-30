package tools.ivemo.heatmap.runners;


import exception.RunnerException;

/**
 * Runner with custom implementation "postExecuteSingleGenerationPhase" of {@link runner.AbstractRunner} class.
 * Used to update heatmap data after every generation is finished.
 *
 * @author MTomczyk
 */

public class GenerationalUpdateRunner extends AbstractUpdateRunner
{
    /**
     * Parameterized constructor.
     *
     * @param p params container.
     */
    public GenerationalUpdateRunner(Params p)
    {
        super(p);
    }

    /**
     * Post "execute single generation" phase.
     *
     * @param generation        current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1 mapping)
     * @throws RunnerException the exception can be captured when executing the method 
     */
    @Override
    public void postExecuteSingleGenerationPhase(int generation, int [] generationLimits) throws RunnerException
    {
        try
        {
        super.postExecuteSingleGenerationPhase(generation, generationLimits);
        processSpecimens(_eas[0].getSpecimensContainer().getPopulation(), generation, 0);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("post init phase", e);
        }
    }
}
