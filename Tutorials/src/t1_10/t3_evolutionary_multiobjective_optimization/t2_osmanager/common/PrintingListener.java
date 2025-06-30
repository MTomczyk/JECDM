package t1_10.t3_evolutionary_multiobjective_optimization.t2_osmanager.common;

import ea.EA;
import exception.PhaseException;
import os.IOSChangeListener;
import space.os.ObjectiveSpace;

/**
 * A dummy listener that prints the report on the previous and current objective space objects.
 *
 * @author MTomczyk
 */
public class PrintingListener implements IOSChangeListener
{
    /**
     * Action to be performed when there is a change in the objective space.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS previous objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {
        System.out.println("Printing listener action triggered:");
        System.out.println("Previous objective space:");
        if (prevOS != null) prevOS.print();
        else System.out.println("None");
        System.out.println("Current objective space:");
        if (os != null) os.print();
        else System.out.println("None");
    }
}
