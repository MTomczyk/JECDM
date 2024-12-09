package tools.ivemo.heatmap.runners;


import ea.EA;
import exception.RunnerException;

/**
 * Runner with custom implementation "postExecuteSingleSteadyStateRepeat" of {@link runner.AbstractRunner} class.
 * Used to update heatmap data after every steady state repeat.
 *
 * @author MTomczyk
 */

public class SteadyStateUpdateRunner extends AbstractUpdateRunner
{
    /**
     * Parameterized constructor.
     *
     * @param p params container.
     */
    public SteadyStateUpdateRunner(Params p)
    {
        super(p);
    }

    /**
     * Post "execute single steady-state repeat" phase.
     *
     * @param generation        generation number
     * @param steadyStateRepeat steady-state repeat number
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void postExecuteSingleSteadyStateRepeat(EA ea, int generation, int steadyStateRepeat) throws RunnerException
    {
        try
        {
            super.postExecuteSingleSteadyStateRepeat(ea, generation, steadyStateRepeat);
            processSpecimens(ea.getSpecimensContainer().getPopulation(), generation, steadyStateRepeat);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("post init phase", e);
        }
    }
}
