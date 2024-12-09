package visualization.heatmap3D;

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
            Test1_NonLinearBoxes.main(args);
            Test2_Labels.main(args);
            Test3_Data.main(args);
            Test4_DataBigger.main(args);
            //Test5_DataBigger_Animated.main(args);
            //Test6_DataBiggerBigger_Animated.main(args);
            Test7_DataBiggerBigger.main(args);
            Test8_DataBiggerBigger_Black.main(args);
            Test9_Edges.main(args);
            Test10_Markers.main(args);
            Test11_Styles.main(args);
            Test12_DataSwap.main(args);
            Test13_NonLinearData.main(args);
            //Test14_Masking.main(args);
            //Test15_AlreadySorted.main(args);
            Test16_Colorbar.main(args);
            Test17_WithData.main(args);
            Test18_RatioDisplay.main(args);
            Test19_SuperBig.main(args);

        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
