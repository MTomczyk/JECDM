package visualization.heatmap2D;

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
            Test1.main(args);
            Test2_ManyPoints.main(args);
            Test3_NonLinearBoxes.main(args);
            Test4_NonLinearGradientColorbar.main(args);
            Test5_Masking.main(args);
            Test6_IntervalFiltering.main(args);
            Test7_IntervalFiltering_Save.main(args);
            Test8_IntervalFiltering_BlackScheme.main(args);
            Test9_Mixed.main(args);
            //Test10_AnimatedChanges.main(args);
            Test11_ManyPoints_SortedMode.main(args);
            Test12_NonLinearBoxes_SortedMode.main(args);
            Test13_NonLinearGradientLegend_SortedMode.main(args);
            Test14_Masking_SortedMode.main(args);
            Test15_IntervalFiltering_SortedMode.main(args);
            Test16_Mixed_SortedMode.main(args);
            //Test17_AnimatedChanges_SortedMode.main(args);
            Test18_IntervalFiltering_Normalized.main(args);
            //Test19_IntervalFiltering_Unnormalized_Animated.main(args);

        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
