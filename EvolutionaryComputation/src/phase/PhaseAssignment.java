package phase;

import ea.EATimestamp;
import ea.IEA;

/**
 * Auxiliary class that determines the phase assignment: (i) to be executed within the {@link IEA#init()} method,
 * (ii) within the {@link IEA#step(EATimestamp)} method.
 *
 * @author MTomczyk
 */
public class PhaseAssignment
{
    /**
     * Possible assignments.
     */
    public enum Assignment
    {
        /**
         * Corresponds to the initialization step (the zero-th generation). In this step, an initial population is
         * constructed and evaluated, typically, and auxiliary operations are performed.
         */
        INIT,

        /**
         * Corresponds to a regular generation of an EA (involves the reproduction step, etc.).
         */
        STEP
    }

    /**
     * Phase to be assigned.
     */
    public final IPhase _phase;

    /**
     * Assignment.
     */
    public final Assignment _assignment;

    /**
     * Parameterized constructor.
     *
     * @param phase      phase to be assigned; note that the same object can be assigned to INIT and STEP
     * @param assignment assignment
     */
    public PhaseAssignment(IPhase phase, Assignment assignment)
    {
        _phase = phase;
        _assignment = assignment;
    }
}
