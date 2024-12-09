package t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.t1;


import criterion.Criteria;
import exception.PhaseException;
import os.IOSChangeListener;
import os.ObjectiveSpaceManager;
import population.ISpecimenGetter;
import population.SpecimensContainer;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common.DummyGetter;
import t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common.PrintingListener;

/**
 * Tutorial on the {@link os.ObjectiveSpaceManager} class.
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create default OS (2 dimensions, [0-1] ranges, objectives to be minimized):
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2), new boolean[2]);

        // Create OS Params container:
        ObjectiveSpaceManager.Params pOSM = new ObjectiveSpaceManager.Params(os);
        // Define criteria:
        pOSM._criteria = Criteria.constructCriteria("C", 2, false);
        // Update both points using an incumbent:
        pOSM._updateUtopiaUsingIncumbent = true;
        pOSM._updateNadirUsingIncumbent = true;

        // Create dummy getter:
        pOSM._specimenGetters = new ISpecimenGetter[]{
                new DummyGetter(new double[][][]{
                        {
                                {-0.5d, 0.5d},
                                {1.5d, -1.5d},
                                {0.9d, 0.9d}
                        },
                        { // no change
                                {-0.5d, 0.5d},
                                {1.5d, -1.5d},
                                {0.9d, 0.9d}
                        },
                        {
                                {-2.5d, 0.5d},
                                {2.5d, -4.5d},
                                {1.0d, 0.0d}
                        }
                })
        };

        // Create printing listener:
        pOSM._listeners = new IOSChangeListener[]{new PrintingListener()};

        // Create OS manager:
        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOSM);

        try
        {
            System.out.println("Update no. 1 ==========================================");
            // Specimens container must be provided (even if empty):
            OSM.update(new SpecimensContainer(), null);
            System.out.println("Update no. 2 ==========================================");
            OSM.update(new SpecimensContainer(), null);
            System.out.println("Update no. 3 ==========================================");
            OSM.update(new SpecimensContainer(), null);

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
