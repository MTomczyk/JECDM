package visualization.plot3D;

import org.junit.jupiter.api.Test;
import visualization.plot2D.Test29_BrokenLine;

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
            Test2_Panes_Axes.main(args);
            Test3_Panes_Axes.main(args);
            Test4_Panes_Axes.main(args);
            Test5_BlackScheme.main(args);
            Test6_DataSets1.main(args);
            Test7_DataSets2.main(args);
            Test8_DataSets3_SwappedDisplayRanges.main(args);
            Test9_DataSets4.main(args);
            Test10_DataSets5_RightClick.main(args);
            Test11_Spiral.main(args);
            Test12_DifferentNormalizations.main(args);
            //Test13_Animation_Fixed.main(args);
            //Test14_Animation_Dynamic_Save.main(args);
            Test15_SpiralLegend.main(args);
            Test16_SpiralLegend_Colorbar.main(args);
            Test17_Legend.main(args);
            Test18_MarkerStyles.main(args);
            Test19_ManySpheres.main(args);
            Test20_DisableDSs.main(args);
            Test21_SpiralLegend_PolyQuadLine.main(args);
            Test22_PolyQuadLine.main(args);
            Test23_PolyQuadLine_Random.main(args);
            Test24_SpiralLegend_PolyOctoLine.main(args);
            Test25_PolyOctoLine.main(args);
            Test26_PolyOctoLine_Random.main(args);
            Test27_ProjectionBounds.main(args);
            Test28_ProjectionBounds.main(args);
            Test29_BrokenLine.main(args);
            Test30_Arrows.main(args);
            Test31_Arrows.main(args);
            Test32_LegendWithArrows1.main(args);

        } catch (Exception e)
        {
            passed = false;
            System.out.println(e.getMessage());
        }
        assertTrue(passed);
    }
}
