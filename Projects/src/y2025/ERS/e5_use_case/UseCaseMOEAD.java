package y2025.ERS.e5_use_case;

import color.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DRMPFactory;
import emo.aposteriori.moead.MOEAD;
import emo.aposteriori.moead.MOEADBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.EAException;
import exception.RunnerException;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Plot3D;
import plot.Plot3DFactory;
import plot.PlotUtils;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.cw.cw1.CW1Bundle;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Wrap;
import runner.IRunner;
import runner.Runner;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import selection.Random;
import utils.Screenshot;
import visualization.updaters.sources.EASource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Performs the use-case simulation.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class UseCaseMOEAD
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        boolean useERS = true;

        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = CW1Bundle.getBundle(
                () -> SBX.getConstrained(1.0d, 10.0d, new Wrap()),
                () -> PM.getConstrained(1.0d / 5.0d, 10.0d, new Wrap())
        );
        MOEADBuilder MB = new MOEADBuilder(R);
        MB.setSimilarity(new Euclidean());
        MB.setNeighborhoodSize(10);
        IGoal[] goals = GoalsFactory.getLNormsDND(3, 50, Double.POSITIVE_INFINITY);

        System.out.println("Population size = " + goals.length);
        MB.setGoals(goals);
        MB.setParentsSelector(new Random());
        MB.setCriteria(problemBundle._criteria);
        MB.setProblemImplementations(problemBundle);
        MB.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);

        Gradient gradient = Gradient.getViridisGradient();

        // Create plot 3D
        Plot3D plot3D = Plot3DFactory.getPlot(
                WhiteScheme.getForPlot3D(),
                "M", "A", "I",
                DRMPFactory.getFor3D(problemBundle._displayRanges[0],
                        //new Range(6.1d, 9.0d),
                        problemBundle._displayRanges[1],
                        problemBundle._displayRanges[2]),
                3, 3, 3,
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                2.2f, 2.0f, scheme ->
                {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._sizes.put(SizeFields.AXIS3D_X_TICK_LABEL_OFFSET, 0.1f);
                    scheme._sizes.put(SizeFields.AXIS3D_Z_TICK_LABEL_OFFSET, 0.05f);
                },
                pP -> pP._axesAlignments = new Align[]{
                        Align.FRONT_BOTTOM,
                        Align.LEFT_BOTTOM,
                        Align.FRONT_RIGHT}, null);


        Frame frame = new Frame(plot3D, 1000, 1000);
        plot3D.getModel().notifyDisplayRangesChangedListeners();

        plot3D.getModel().updatePlotRotation(354.92697f, 49.02439f);
        plot3D.getModel().updateCameraTranslation(-0.01668f, 0.02500f, 1.73368f);

        frame.setVisible(true);

        try
        {
            MOEAD moead = MB.getInstance();
            EASource eaSource = new EASource(moead);

            IRunner runner = new Runner(moead, moead.getExpectedNumberOfSteadyStateRepeats());
            runner.init();

            plot3D.getModel().setDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                    new MarkerStyle(0.015f, gradient, 2, Marker.SPHERE_HIGH_POLY_3D)));

            for (int g = 1; g < 2000; g++)
            {
                runner.executeSingleGeneration(g, null);

                plot3D.getModel().setDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                        new MarkerStyle(0.015f, gradient, 2, Marker.SPHERE_HIGH_POLY_3D)));


            }

            Thread.sleep(100);

            Screenshot screenshot = plot3D.getModel().requestScreenshotCreation(1000, 1000, false);
            screenshot._barrier.await();
            Path path = FileUtils.getPathRelatedToClass(UseCaseMOEAD.class,
                    "Projects", "src", File.separatorChar);
            ImageSaver.saveImage(screenshot._image, path + File.separator
                    + "uc_pf_approx", "jpg", 1.0f);

        } catch (EAException | RunnerException | InterruptedException | IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
