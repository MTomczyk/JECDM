package visualization.parallelcoordinate2D;

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
            Test1_WrongPainter.main(args);
            Test2_CorrectPainter.main(args);
            Test3_Massive.main(args);
            Test4_MassiveAlpha.main(args);
            Test5_1DIM.main(args);
            Test6_Colorbar.main(args);
            Test7_Colorbar_NoMiddleTicks.main(args);
            Test8_BlackScheme.main(args);
            //Test9_AnimatedChanges.main(args);
            Test10_Disable_DSs.main(args);
            Test11_SingleDataPoints.main(args);

        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
