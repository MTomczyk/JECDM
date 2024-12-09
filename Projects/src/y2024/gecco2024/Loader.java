package y2024.gecco2024;

import frame.Frame;
import swing.LoadingFrame;
import tools.ivemo.heatmap.io.load.LoadBinary;
import tools.ivemo.heatmap.io.load.LoadInitFile;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;
import tools.ivemo.heatmap.visualization.FrameFactory;

import javax.swing.*;

/**
 * Main loader class that loads heatmap data and performs visualization.
 *
 * @author MTomczyk
 */

public class Loader
{
    /**
     * Main method to be executed.
     *
     * @param args if null, the current directory is assumed to be the working directory; optionally the path prefix to
     *             the files to be loaded can be provided as the first element of the args array
     */
    public static void main(String[] args)
    {
        // DPI TEST SCALE
       // System.setProperty("sun.java2d.uiScale", "1");
        // DPI TEST SCALE

        // Prepare fields
        LoadingFrame loadingFrame = new LoadingFrame(0.075d);

        Frame frame = null;
        FrameParams FP;
        PlotParams[] PP;
        LoadBinary binary;

        // try catch -> if error, display dialogue with a message
        try
        {
            String pathPrefix = "./"; // set the path prefix to the current directory
            if ((args != null) && (args.length > 0)) pathPrefix = args[0];
            System.out.println("Path prefix = " + pathPrefix);

            SwingUtilities.invokeAndWait(() -> loadingFrame.setVisible(true)); // show the loading frame
            LoadInitFile loadInit = new LoadInitFile(); // Load init file

            System.out.println("Loading init file...");
            loadInit.load(pathPrefix);

            FP = loadInit._FP; // get frame params
            PP = loadInit._PP; // get plot params
            FP._printFPS = true; //

            binary = new LoadBinary(PP, true); // load binary data
            binary.load(pathPrefix);

        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage()); // error occurred, display message

            if (loadingFrame.isVisible()) // terminate the loading frame
            {
                SwingUtilities.invokeLater(() -> {
                    loadingFrame.setVisible(false);
                    loadingFrame.dispose();
                });
            }

            return;
        }

        // try catch -> if error, display dialogue with a message
        try
        {
            frame = FrameFactory.getFrame(FP, PP, binary._loadedHeatmapData); // create heatmap frame

            SwingUtilities.invokeLater(() -> { // hide the loading frame
                loadingFrame.setVisible(false);
                loadingFrame.dispose();
            });

            frame.setVisible(true);

        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage()); // error occurred, display message

            SwingUtilities.invokeLater(() -> { // terminate the loading frame
                loadingFrame.setVisible(false);
                loadingFrame.dispose();
            });

            if (frame != null) // terminate the heatmap frame
            {
                frame.getController().stopBackgroundThreads();
                frame.setVisible(false);
                frame.dispose();
            }
        }

    }
}
