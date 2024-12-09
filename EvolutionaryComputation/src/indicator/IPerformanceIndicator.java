package indicator;

import ea.EA;

/**
 * Interfaces for classes responsible for evaluating the performance of EA's.
 *
 * @author MTomczyk
 */
public interface IPerformanceIndicator
{
    /**
     * Main method for evaluating EA's performance
     *
     * @param ea evolutionary algorithm
     * @return performance value
     */
    double evaluate(EA ea);

    /**
     * Method for identifying preference direction.
     *
     * @return preference direction (if true, less is preferred; if true, more is preferred)
     */
    boolean isLessPreferred();

    /**
     * The implementation must overwrite the toString() method.
     *
     * @return string representation
     */
    @Override
    String toString();

    /**
     * For cloning: must be implemented to work properly with the experimentation module.
     * Deep copy must be created, but it should consider object's INITIAL STATE.
     *
     * @return cloned instance
     */
    IPerformanceIndicator getInstanceInInitialState();
}
