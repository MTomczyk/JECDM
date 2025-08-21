package ea;

import org.junit.jupiter.api.Test;
import phase.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link AbstractPhasesEA}.
 *
 * @author MTomczyk
 */
class AbstractPhasesEATest
{
    /**
     * Tests {@link AbstractPhasesEA#establishPhases(PhaseAssignment[], PhaseAssignment.Assignment)}.
     */
    @Test
    void establishPhases()
    {
        {
            assertNull(AbstractPhasesEA.establishPhases(null, null));
            assertNull(AbstractPhasesEA.establishPhases(new PhaseAssignment[0], null));
            assertNull(AbstractPhasesEA.establishPhases(new PhaseAssignment[]{null}, null));
        }
        {
            PhaseAssignment[] phaseAssignments = new PhaseAssignment[4];
            phaseAssignments[0] = new PhaseAssignment(new InitEnds(), PhaseAssignment.Assignment.INIT);
            phaseAssignments[1] = new PhaseAssignment(new FinalizeStep(), PhaseAssignment.Assignment.STEP);
            phaseAssignments[2] = null;
            phaseAssignments[3] = new PhaseAssignment(new SortByAuxValue(true),
                    PhaseAssignment.Assignment.INIT);
            IPhase[] phases = AbstractPhasesEA.establishPhases(phaseAssignments, PhaseAssignment.Assignment.INIT);
            assertNotNull(phases);
            assertEquals(2, phases.length);
            assertEquals(InitEnds.class, phases[0].getClass());
            assertEquals(SortByAuxValue.class, phases[1].getClass());
        }

    }
}