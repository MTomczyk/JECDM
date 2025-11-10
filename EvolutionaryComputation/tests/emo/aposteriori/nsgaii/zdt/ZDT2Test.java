package emo.aposteriori.nsgaii.zdt;

import color.Color;
import dataset.DSFactory2D;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
import exception.EAException;
import exception.RunnerException;
import frame.Frame;
import org.junit.jupiter.api.Test;
import plot.Plot2D;
import plot.Plot2DFactory;
import problem.Problem;
import problem.moo.zdt.ZDTBundle;
import random.L32_X64_MIX;
import runner.IRunner;
import runner.Runner;
import scheme.enums.ColorFields;
import selection.Tournament;
import visualization.updaters.sources.EASource;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test performance of NSGA-II applied to ZDT2.
 *
 * @author MTomczyk
 */
public class ZDT2Test
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        (new ZDT2Test()).test1();
    }

    /**
     * Tests the method.
     */
    @Test
    public void test1()
    {
        ZDTBundle problem = ZDTBundle.getBundle(Problem.ZDT2);
        NSGAIIBuilder b = new NSGAIIBuilder(new L32_X64_MIX(0));
        b.setProblemImplementations(problem);
        b.setCriteria(problem._criteria);
        b.setParentsSelector(new Tournament(2));
        b.setPopulationSize(50);
        b.setDynamicOSBoundsLearningPolicy();

        Plot2D plot2D = Plot2DFactory.getPlot(
                "f1", "f2",
                problem._displayRanges[0], problem._displayRanges[1], 5, 10,
                1.5f, scheme -> scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE), null
        );
        Frame frame = new Frame(plot2D);
        plot2D.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);


        String msg = null;
        try
        {
            NSGAII nsgaii = b.getInstance();
            EASource eaSource = new EASource(nsgaii);
            IRunner runner = new Runner(nsgaii);
            runner.init();
            for (int g = 1; g < 1000; g++)
            {
                runner.executeSingleGeneration(g, null);
                plot2D.getModel().setDataSet(DSFactory2D.getDS("NSGA-II", eaSource.createData(),
                        new MarkerStyle(1.0f, color.gradient.Color.RED, Marker.SQUARE)));
            }
        } catch (EAException | RunnerException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}
