package y2024.gecco2024.case3.save;

import color.gradient.Gradient;
import criterion.Criteria;
import dataset.painter.style.enums.Bucket;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.nsgaii.NSGAIIBundle;
import frame.Frame;
import phase.PhasesBundle;
import plot.heatmap.utils.Coords;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import selection.Tournament;
import space.Range;
import statistics.Mean;
import statistics.Min;
import tools.ivemo.heatmap.Heatmap2DProcessor;
import tools.ivemo.heatmap.feature.Generation;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;
import tools.ivemo.heatmap.io.save.SaveBinary;
import tools.ivemo.heatmap.io.save.SaveInitFile;
import tools.ivemo.heatmap.visualization.FrameFactory;



/**
 * Executes processing for Case #3.
 *
 * @author MTomczyk
 */

@SuppressWarnings("DuplicatedCode")
public class Execute
{

    /**
     * The main method
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        // Sets DPI scaling to 1
        System.setProperty("sun.java2d.uiScale", "1");

        // Constant seed of 0 is used for reproducibility
        IRandom R = new MersenneTwister64(0);

        String frameTitle = "Case #3 heatmap (NSGA-II; 2D; generation; min; avg)"; // experiment title
        String fileName = "nsgaii_2d_generation_min_average"; // file name (without extension) for files to be generated
        String pathPrefix = "./Projects/src/y2024/gecco2024/case3/save/"; // prefix for paths for files to be generated (case folder)
        String heatmapTitle = "Expected earliest generation when found"; // heatmap tile
        String xAxisTitle = "f1"; // title to be displayed on the X-axis
        String yAxisTitle = "f2"; // title to be displayed on the Y-axis

        int trials = 20; // number of test runs
        int dimensions = 2; // number of dimensions
        int populationSize = 30; // population size
        int offspringSize = 30; // offspring size
        int generations = 201; // no. generations (stopping criterion); not 200, because 0th generation (construction of the initial population) also accounts
        int xAxisDivisions = 20; // no. of divisions on the X-axis (discretization level, determines the total. no buckets for the heatmap)
        int yAxisDivisions = 20; // no. of divisions on the Y-axis (discretization level, determines the total. no buckets for the heatmap)
        boolean printFPS = false; // can be used to report FPS for rendering

        float frameSize = 0.5f; // frame size; relative to screen size

        Range xDisplayRange = new Range(0.0d, 2.0d); // display range for the X-axis (subspace to be discretized)
        Range yDisplayRange = new Range(0.0d, 2.0d); // display range for the Y-axis (subspace to be discretized)
        Range heatmapDisplayRange = new Range(0.0d, generations - 1); // set the heatmap normalization bounds to (0, generations); generations is here the maximum possible bucket value
        Gradient gradient = Gradient.getViridisGradient(); // use the viridis gradient for the heatmap

        // Prepare the experiment
        Heatmap2DProcessor.Params pHP = new Heatmap2DProcessor.Params();
        pHP._title = frameTitle;
        pHP._notify = true; // true -> heatmap data processor prints some statuses to the console
        pHP._trials = trials; // set the number of test run
        pHP._generations = generations; // set the number of generations
        pHP._featureGetter = new Generation(); // set the feature getter: Generation = a solution contained in a bucked in a given generation and trial is evaluated as the generation number
        pHP._trialStatistics = new Min(); // determine the trial-bucket statistic as the minimum of the gathered samples
        pHP._finalStatistics = new Mean(); // determine the final-bucket statistic as the average of the collected minima
        pHP._xAxisDivisions = xAxisDivisions; // set the number of X-axis divisions
        pHP._xAxisDisplayRange = xDisplayRange; // set the heatmap objective space bound on the X-axis
        pHP._yAxisDivisions = yAxisDivisions; // set the number of Y-axis divisions
        pHP._yAxisDisplayRange = yDisplayRange; // set the heatmap objective space bound on the Y-axis
        pHP._eaFactory = () -> { // Factory object construct new instance of an EA.

            Problem problem = Problem.DTLZ2; // Problem ID selection: DTLZ2
            // Bundle object contains all essential fields linked to the selected problem;
            // Note that the same random generator object is passed. It will ensure that all the numbers
            // will be drawn from the same probability distribution (EA's are supposed NOT TO re-seed the generation, i.e.,
            // only "next value" functions can be called; the number of decision variables is set to 100
            DTLZBundle problemBundle = DTLZBundle.getBundle(problem, dimensions, 20);
            Criteria criteria = Criteria.constructCriteria("C", dimensions, false); // create criteria labels

            NSGAIIBundle.Params pAB = new NSGAIIBundle.Params(criteria); // Creates a default NSGA-II bundle parameterization (contains essential fields linked to the proper execution of NSGA-II)
            pAB._construct = problemBundle._construct; // algorithm takes the "initial population constructor" EA step from the problem bundle
            pAB._reproduce = problemBundle._reproduce;  // algorithm takes the "reproduce solutions" EA step from the problem bundle
            pAB._evaluate = problemBundle._evaluate;  // algorithm takes the "evaluate solutions" EA step from the problem bundle
            pAB._initialNormalizations = problemBundle._normalizations; // Pareto front objective space bounds are provided a priori and are fixed (used to normalize solutions' objective functions values)
            pAB._osManager = null;

            Tournament.Params pT = new Tournament.Params(); // Construct tournament selection object.
            pT._size = 2; // tournament size
            pT._preferenceDirection = false; // lesser score is better (as imposed by the implemented NSGA-II sorting procedure)
            pAB._select = new Tournament(pT); // assign the selection procedure to the NSGA-II bundle parameterization

            NSGAIIBundle algorithmBundle = new NSGAIIBundle(pAB); // construct the NSGA-II bundle
            EA.Params pEA = new EA.Params("NSGA-II", criteria); // instantiate the EA
            // copy and paste EA phases from the NSGA-II bundle to the general EA instance
            pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(algorithmBundle._phasesBundle);
            pEA._id = 0; // set EA id to zero
            pEA._R = R; //set the random number generator
            pEA._populationSize = populationSize; // set the population size
            pEA._offspringSize = offspringSize; // set the offspring size
            return new EA(pEA); // returns new EA instance (the purpose of the EA factory).
        };

        Heatmap2DProcessor h2D = new Heatmap2DProcessor(pHP); // generate heatmap processor

        h2D.executeProcessing(); // execute general processing
        h2D.generateSortedInputData(); // constructs the bucked-data (coordinates + value) sorted according to bucket values

        // Saving the init and .hm file
        FrameParams FP = new FrameParams(); // params for the main frame
        FP._frameSize = frameSize; // set the plot frame size (relative to screen size)
        FP._frameTitle = frameTitle; // set frame title
        FP._printFPS = printFPS; // report FPS for rendering

        PlotParams[] PP = new PlotParams[]{new PlotParams()}; // params for the associated plot
        PP[0]._fileName = fileName; // set file name (without extension)
        PP[0]._title = null; // no title for the plot
        PP[0]._dimensions = dimensions; // set the number of dimensions
        PP[0]._scheme = new WhiteScheme(); // set the plot scheme to white
        PP[0]._gradient = gradient; // set the heatmap gradient to "Viridis"
        PP[0]._heatmapDisplayRange = heatmapDisplayRange; // set the heatmap normalization bounds to (0, generations); generations is here the maximum possible bucket value
        PP[0]._heatmapTitle = heatmapTitle;
        PP[0]._bucketStyle = Bucket.SQUARE_2D; // Set marker style to square (the only type allowed).
        PP[0]._xAxisTitle = xAxisTitle; // set the X-axis title
        PP[0]._yAxisTitle = yAxisTitle; // set the Y-axis title
        PP[0]._xAxisDivisions = xAxisDivisions; // set the number of divisions on the X-axis accordingly to the experiment setting
        PP[0]._yAxisDivisions = yAxisDivisions; // set the number of divisions on the Y-axis accordingly to the experiment setting
        PP[0]._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(xDisplayRange, yDisplayRange); // instantiates
        // the params for the display range manager (X and Y-axis bounds).

        // Save the init file
        SaveInitFile saveInitFile = new SaveInitFile(FP, PP);
        try
        {
            System.out.println("Saving init file...");
            saveInitFile.save(pathPrefix);
        } catch (Exception e)
        {
            System.out.println("Error during saving: " + e.getMessage());
        }

        System.out.println("No sorted elements...");
        System.out.println("Coords = " + h2D.getSortedCoords().length);
        System.out.println("Sorted values = " + h2D.getSortedValues()._sortedValues.length);

        // Save the binary file
        SaveBinary saveBinary = new SaveBinary(PP[0], h2D, true);
        try
        {
            System.out.println("Saving binary data file...");
            saveBinary.save(pathPrefix);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Finally, display the generated plot for the inspection
        Coords[][] coords = new Coords[1][];
        coords[0] = h2D.getSortedCoords();

        Frame pf;
        try
        {
            pf = FrameFactory.getFrame(FP, PP, coords);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        pf.setVisible(true);
    }
}
