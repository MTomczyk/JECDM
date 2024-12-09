package drmanager;

import dataset.Data;
import org.junit.jupiter.api.Test;
import space.Range;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Several tests for {@link DisplayRangesManager} class
 *
 * @author MTomczyk
 */
class DisplayRangesManagerTest
{
    /**
     * Test 1: 2 nulled DRs, update dynamically = true, from scratch = false
     */
    @Test
    void updateDisplayRanges1()
    {
        DisplayRangesManager.Report merged ;

        DisplayRangesManager.Params pDR = DisplayRangesManager.Params.getFor2D();
        DisplayRangesManager DRM = new DisplayRangesManager(pDR);
        DisplayRangesManager.Report REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {3.0d, 4.0d}}), null);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);
        assertNull(REP._previousRanges[0]);
        assertNull(REP._previousRanges[1]);
        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(3.0d, DRM._DR[0].getR().getRight());
        assertEquals(2.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        merged = REP;

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);

        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(3.0d, REP._previousRanges[0].getRight());
        assertEquals(2.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        merged.mergeWith(REP);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertTrue(merged._displayRangeChanged[1]);

        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(5.0d, REP._previousRanges[0].getRight());
        assertEquals(1.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 5.0d}}), null);
        merged.mergeWith(REP);

        assertTrue(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertTrue(merged._displayRangeChanged[1]);

        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(5.0d, REP._previousRanges[0].getRight());
        assertEquals(1.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(5.0d, DRM._DR[1].getR().getRight());

        assertEquals(1.0d, DRM.getDisplayRangeForXAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForXAxis().getR().getRight());
        assertEquals(1.0d, DRM.getDisplayRangeForYAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForYAxis().getR().getRight());
    }

    /**
     * Test 2: 2 fixed DRs, update dynamically = false
     */
    @Test
    void updateDisplayRanges2()
    {
        DisplayRangesManager.Params pDR = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), new Range(2.0d, 3.0d));

        DisplayRangesManager DRM = new DisplayRangesManager(pDR);
        DisplayRangesManager.Report REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {3.0d, 4.0d}}), null);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);

        assertEquals(0.0d, REP._previousRanges[0].getLeft());
        assertEquals(1.0d, REP._previousRanges[0].getRight());
        assertEquals(2.0d, REP._previousRanges[1].getLeft());
        assertEquals(3.0d, REP._previousRanges[1].getRight());

        assertEquals(0.0d, DRM._DR[0].getR().getLeft());
        assertEquals(1.0d, DRM._DR[0].getR().getRight());
        assertEquals(2.0d, DRM._DR[1].getR().getLeft());
        assertEquals(3.0d, DRM._DR[1].getR().getRight());

        assertEquals(0.0d, DRM.getDisplayRangeForXAxis().getR().getLeft());
        assertEquals(1.0d, DRM.getDisplayRangeForXAxis().getR().getRight());
        assertEquals(2.0d, DRM.getDisplayRangeForYAxis().getR().getLeft());
        assertEquals(3.0d, DRM.getDisplayRangeForYAxis().getR().getRight());
    }

    /**
     * Test 3: 2 nulled DRs, update dynamically = true, from scratch = true
     */
    @Test
    void updateDisplayRanges3()
    {
        DisplayRangesManager.Params pDR = new DisplayRangesManager.Params();
        pDR._DR = new DisplayRangesManager.DisplayRange[2];
        pDR._DR[0] = new DisplayRangesManager.DisplayRange(null, true, true);
        pDR._DR[1] = new DisplayRangesManager.DisplayRange(null, true, true);

        DisplayRangesManager DRM = new DisplayRangesManager(pDR);
        DisplayRangesManager.Report REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {3.0d, 4.0d}}), null);
        DisplayRangesManager.Report merged = REP;

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);
        assertNull(REP._previousRanges[0]);
        assertNull(REP._previousRanges[1]);
        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(3.0d, DRM._DR[0].getR().getRight());
        assertEquals(2.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        merged.mergeWith(REP);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertTrue(merged._displayRangeChanged[1]);


        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(3.0d, REP._previousRanges[0].getRight());
        assertEquals(2.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        merged.mergeWith(REP);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertTrue(merged._displayRangeChanged[1]);

        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(5.0d, REP._previousRanges[0].getRight());
        assertEquals(1.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(4.0d, DRM._DR[1].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {5.0d, 5.0d}}), null);
        merged.mergeWith(REP);

        assertTrue(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertTrue(REP._displayRangeChanged[1]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertTrue(merged._displayRangeChanged[1]);

        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(5.0d, REP._previousRanges[0].getRight());
        assertEquals(1.0d, REP._previousRanges[1].getLeft());
        assertEquals(4.0d, REP._previousRanges[1].getRight());

        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());
        assertEquals(2.0d, DRM._DR[1].getR().getLeft());
        assertEquals(5.0d, DRM._DR[1].getR().getRight());

        assertEquals(1.0d, DRM.getDisplayRangeForXAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForXAxis().getR().getRight());
        assertEquals(2.0d, DRM.getDisplayRangeForYAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForYAxis().getR().getRight());
    }

    /**
     * Test 4: 2 nulled DRs, update dynamically = true, from scratch = true, different mapping
     */
    @Test
    void updateDisplayRanges4()
    {
        DisplayRangesManager.Params pDR = new DisplayRangesManager.Params();
        pDR._DR = new DisplayRangesManager.DisplayRange[3];
        pDR._DR[0] = new DisplayRangesManager.DisplayRange(null, true, true);
        pDR._DR[1] = null;
        pDR._DR[2] = new DisplayRangesManager.DisplayRange(null, true, true);
        pDR._attIdx_to_drIdx = new Integer[]{2, 0, 1};

        DisplayRangesManager DRM = new DisplayRangesManager(pDR);
        DisplayRangesManager.Report REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {3.0d, 4.0d}}), null);
        DisplayRangesManager.Report merged = REP;

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);
        assertTrue(REP._displayRangeChanged[2]);

        assertNull(REP._previousRanges[0]);
        assertNull(REP._previousRanges[1]);
        assertNull(REP._previousRanges[2]);
        assertEquals(1.0d, DRM._DR[2].getR().getLeft());
        assertEquals(3.0d, DRM._DR[2].getR().getRight());
        assertEquals(2.0d, DRM._DR[0].getR().getLeft());
        assertEquals(4.0d, DRM._DR[0].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        REP.mergeWith(REP);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);
        assertTrue(REP._displayRangeChanged[2]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertFalse(merged._displayRangeChanged[1]);
        assertTrue(merged._displayRangeChanged[2]);

        assertEquals(1.0d, REP._previousRanges[2].getLeft());
        assertEquals(3.0d, REP._previousRanges[2].getRight());
        assertEquals(2.0d, REP._previousRanges[0].getLeft());
        assertEquals(4.0d, REP._previousRanges[0].getRight());

        assertEquals(1.0d, DRM._DR[2].getR().getLeft());
        assertEquals(5.0d, DRM._DR[2].getR().getRight());
        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(4.0d, DRM._DR[0].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 1.0}, {5.0d, 4.0d}}), null);
        REP.mergeWith(REP);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);
        assertFalse(REP._displayRangeChanged[2]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertFalse(merged._displayRangeChanged[1]);
        assertTrue(merged._displayRangeChanged[2]);

        assertEquals(1.0d, REP._previousRanges[2].getLeft());
        assertEquals(5.0d, REP._previousRanges[2].getRight());
        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(4.0d, REP._previousRanges[0].getRight());

        assertEquals(1.0d, DRM._DR[2].getR().getLeft());
        assertEquals(5.0d, DRM._DR[2].getR().getRight());
        assertEquals(1.0d, DRM._DR[0].getR().getLeft());
        assertEquals(4.0d, DRM._DR[0].getR().getRight());

        REP = DRM.updateDisplayRanges(new Data(new double[][]{{1.0d, 2.0}, {5.0d, 5.0d}}), null);
        REP.mergeWith(REP);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertFalse(REP._displayRangeChanged[1]);
        assertFalse(REP._displayRangeChanged[2]);

        assertTrue(merged._displayRangesChanged);
        assertTrue(merged._displayRangeChanged[0]);
        assertFalse(merged._displayRangeChanged[1]);
        assertTrue(merged._displayRangeChanged[2]);

        assertEquals(1.0d, REP._previousRanges[2].getLeft());
        assertEquals(5.0d, REP._previousRanges[2].getRight());
        assertEquals(1.0d, REP._previousRanges[0].getLeft());
        assertEquals(4.0d, REP._previousRanges[0].getRight());

        assertEquals(1.0d, DRM._DR[2].getR().getLeft());
        assertEquals(5.0d, DRM._DR[2].getR().getRight());
        assertEquals(2.0d, DRM._DR[0].getR().getLeft());
        assertEquals(5.0d, DRM._DR[0].getR().getRight());

        assertEquals(1.0d, DRM.getDisplayRangeForXAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForXAxis().getR().getRight());
        assertEquals(2.0d, DRM.getDisplayRangeForYAxis().getR().getLeft());
        assertEquals(5.0d, DRM.getDisplayRangeForYAxis().getR().getRight());
    }

    /**
     * Test 5: Update of a single display range
     */
    @Test
    void updateDisplayRanges5()
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[3];
        p._DR[1] = new DisplayRangesManager.DisplayRange(null, true, false);
        DisplayRangesManager DRM = new DisplayRangesManager(p);

        double[][] data = new double[][]{{1.0d, 2.0d}, {-5.0d, 2.0d}, {10.0d, 2.0d, 12.0d}};
        DisplayRangesManager.Report REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertNull(REP._previousRanges[0]);
        assertEquals(-5.0d, DRM._DR[1].getR().getLeft());
        assertEquals(12.0d, DRM._DR[1].getR().getRight());

        data = new double[][]{{-6.0d, 2.0d}, {-5.0d, 2.0d}, {10.0d, 2.0d, 20.0d}};
        REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertEquals(-5.0d, REP._previousRanges[0].getLeft());
        assertEquals(12.0d, REP._previousRanges[0].getRight());
        assertEquals(-6.0d, DRM._DR[1].getR().getLeft());
        assertEquals(20.0d, DRM._DR[1].getR().getRight());
    }

    /**
     * Test 6: Update of a single display range
     */
    @Test
    void updateDisplayRanges6()
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[3];
        p._DR[1] = new DisplayRangesManager.DisplayRange(null, true, true);
        DisplayRangesManager DRM = new DisplayRangesManager(p);

        double[][] data = new double[][]{{1.0d, 2.0d}, {-5.0d, 2.0d}, {10.0d, 2.0d, 12.0d}};
        DisplayRangesManager.Report REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertNull(REP._previousRanges[0]);
        assertEquals(-5.0d, DRM._DR[1].getR().getLeft());
        assertEquals(12.0d, DRM._DR[1].getR().getRight());

        data = new double[][]{{-1.0d, 2.0d}, null, {3.0d}};
        REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertTrue(REP._displayRangesChanged);
        assertTrue(REP._displayRangeChanged[0]);
        assertEquals(-5.0d, REP._previousRanges[0].getLeft());
        assertEquals(12.0d, REP._previousRanges[0].getRight());
        assertEquals(-1.0d, DRM._DR[1].getR().getLeft());
        assertEquals(3.0d, DRM._DR[1].getR().getRight());
    }


    /**
     * Test 7: Update of a single display range
     */
    @Test
    void updateDisplayRanges7()
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[3];
        p._DR[1] = new DisplayRangesManager.DisplayRange(new Range(0.0d, 3.0d), false, true);
        DisplayRangesManager DRM = new DisplayRangesManager(p);

        double[][] data = new double[][]{{1.0d, 2.0d}, {-5.0d, 2.0d}, {10.0d, 2.0d, 12.0d}};
        DisplayRangesManager.Report REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertEquals(0.0d, REP._previousRanges[0].getLeft());
        assertEquals(3.0d, REP._previousRanges[0].getRight());
        assertEquals(0.0d, DRM._DR[1].getR().getLeft());
        assertEquals(3.0d, DRM._DR[1].getR().getRight());

        data = new double[][]{{-1.0d, 2.0d}, null, {3.0d}};
        REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertEquals(0.0d, REP._previousRanges[0].getLeft());
        assertEquals(3.0d, REP._previousRanges[0].getRight());
        assertEquals(0.0d, DRM._DR[1].getR().getLeft());
        assertEquals(3.0d, DRM._DR[1].getR().getRight());
    }

    /**
     * Test 8: Update of a single display range
     */
    @Test
    void updateDisplayRanges8()
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[3];
        p._DR[1] = new DisplayRangesManager.DisplayRange(null, false, true);
        DisplayRangesManager DRM = new DisplayRangesManager(p);

        double[][] data = new double[][]{{1.0d, 2.0d}, {-5.0d, 2.0d}, {10.0d, 2.0d, 12.0d}};
        DisplayRangesManager.Report REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertNull(REP._previousRanges[0]);
        assertNull(DRM._DR[1].getR());

        data = new double[][]{{-1.0d, 2.0d}, null, {3.0d}};
        REP = DRM.updateSingleDisplayRange(data, 1, true);

        assertFalse(REP._displayRangesChanged);
        assertFalse(REP._displayRangeChanged[0]);
        assertNull(REP._previousRanges[0]);
        assertNull(DRM._DR[1].getR());
    }
}