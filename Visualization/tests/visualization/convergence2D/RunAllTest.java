package visualization.convergence2D;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs all visualization tests and catches exceptions.
 *
 * @author MTomczyk
 */


public class RunAllTest
{
    /**
     * Test
     */
    @Test
    void Test()
    {
        boolean passed = true;

        try
        {
            String [] args = new String[]{"F"};
            Test1_ConvergencePlot.main(args);
            Test2_ConvergencePlot_Save.main(args);
            Test3_ConvergencePlot_BlackScheme.main(args);
            Test4_ConvergencePlot_ExtremeCases.main(args);
            //Test5_AnimatedChanges.main(args);
            Test6_BrokenLine.main(args);
            Test7_Disable_DSs.main(args);
        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
