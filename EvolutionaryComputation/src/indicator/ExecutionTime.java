package indicator;

import ea.EA;
import ea.IEA;

/**
 * Indicator that returns {@link EA#getExecutionTime()} (the time is calculates in ms).
 *
 * @author MTomczyk
 */
public class ExecutionTime extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Parameterized constructor.
     */
    public ExecutionTime()
    {
        super(true);
    }

    /**
     * Main method for evaluating EA's performance (returns execution time in ms)
     *
     * @param ea evolutionary algorithm
     * @return performance value
     */
    @Override
    public double evaluate(IEA ea)
    {
        return ea.getExecutionTime();
    }

    /**
     * The implementation must overwrite the toString() method.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "EXECUTION_TIME";
    }

    /**
     * For cloning: must be implemented to work properly with the experimentation module.
     * Deep copy must be created, but it should consider object's INITIAL STATE.
     *
     * @return cloned instance
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new ExecutionTime();
    }

}
