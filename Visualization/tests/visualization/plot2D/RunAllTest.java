package visualization.plot2D;

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
            String[] args = new String[]{"F"};
            Test1.main(args);
            Test2.main(args);
            Test3.main(args);
            Test4.main(args);
            Test5.main(args);
            Test6.main(args);
            Test7.main(args);
            Test8.main(args);
            Test9.main(args);
            Test10_DS_Swap.main(args);
            Test11_Animator.main(args);
            Test12_Massive_MeasureIDSTimes.main(args);
            Test13_Massive_MeasureIDSTimes.main(args);
            Test14_Alpha.main(args);
            Test15_Alpha.main(args);
            Test16_RightClickPopUp.main(args);
            Test17_SpaceNormalizationTicksGetter.main(args);
            Test18_SpaceNormalizationTicksGetter.main(args);
            Test19_SpaceNormalizationTicksGetter.main(args);
            Test20_ExtremePoints.main(args);
            Test22_BlackScheme.main(args);
            Test23_ClipTest.main(args);
            //Test24_AnimatedChanges.main(args);
            //Test25_AnimatedChanges_Lines.main(args);
            //Test26_OnDemand.main(args);
            Test28_DisplayRangesSwapped.main(args);
            Test29_BrokenLine.main(args);
            Test30_TwoPointTestGradientLine.main(args);
            Test31_Legend_Surpassed_sizes.main(args);
            Test32_MarkerStyles.main(args);
            Test33_MarkerStyles_Gradient.main(args);
            Test34_Disable_DSs.main(args);
            Test39_Gradients.main(args);
            Test40_SpaceNormalizationTicksGetter.main(args);
            Test41_SpaceNormalizationTicksGetter.main(args);
            Test42_SpaceNormalizationTicksGetter.main(args);
            Test43_Arrows.main(args);
            Test44_Arrows.main(args);
            Test45_LegendWithArrows1.main(args);
            Test47_SelectiveUpdate1.main(args);
            Test48_TwoPlotsWithSplit.main(args);

        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
