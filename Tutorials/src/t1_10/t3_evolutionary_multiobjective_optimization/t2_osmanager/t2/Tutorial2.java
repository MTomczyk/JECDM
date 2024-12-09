package t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.t2;


import criterion.Criteria;
import exception.PhaseException;
import os.IOSChangeListener;
import os.ObjectiveSpaceManager;
import population.ISpecimenGetter;
import population.SpecimensContainer;
import t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common.DummyGetter;
import t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common.PrintingListener;

/**
 * Tutorial on the {@link ObjectiveSpaceManager} class.
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create OS Params container (do not use default OS):
        ObjectiveSpaceManager.Params pOSM = new ObjectiveSpaceManager.Params();
        pOSM._criteria = Criteria.constructCriteria("C", 2, false);

        // Do not use incumbent:
        pOSM._updateUtopiaUsingIncumbent = false;
        pOSM._updateNadirUsingIncumbent = false;

        pOSM._specimenGetters = new ISpecimenGetter[]{
                new DummyGetter(new double[][][]{
                        {
                                {-0.5d, 0.5d},
                                {1.5d, -1.5d},
                                {1.0d, 1.0d}
                        },
                        {
                                {0.5d, 0.5d},
                                {0.25d, -4.5d},
                                {0.75d, 0.0d}
                        }
                })
        };

        pOSM._listeners = new IOSChangeListener[]{new PrintingListener()};

        ObjectiveSpaceManager OSM = new ObjectiveSpaceManager(pOSM);

        try
        {
            System.out.println("Update no. 1 ==========================================");
            // Specimens container must be provided (even if empty):
            OSM.update(new SpecimensContainer(), null);
            System.out.println("Update no. 2 ==========================================");
            OSM.update(new SpecimensContainer(), null);

        } catch (PhaseException e)
        {
            throw new RuntimeException(e);
        }
    }
}