package popupmenu.item;

import container.GlobalContainer;
import container.PlotContainer;
import io.image.PlotImageSaver;
import swing.imagesaver.ImageSaver;
import utils.Projection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extension of {@link AbstractItem}.
 * Allows saving a plot ({@link plot.AbstractPlot}) as an image.
 *
 * @author MTomczyk
 */


public class SaveAsImage extends AbstractItem implements ActionListener
{
    /**
     * Object used to select a file and save the image.
     */
    protected ImageSaver _imageSaver;

    /**
     * Plot image saver (delegate to {@link ImageSaver}).
     */
    protected PlotImageSaver _plotImageSaver;

    /**
     * Default constructor.
     */
    public SaveAsImage()
    {
        super("Save as image...");
        _plotImageSaver = new PlotImageSaver();
        _imageSaver = new ImageSaver(_plotImageSaver, false);
    }

    /**
     * Setter for containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    @Override
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        super.setContainers(GC, PC);
        _plotImageSaver.setPlotContainer(PC);
    }


    /**
     * Invoked when an action occurs. Displays an image saver (frame for file selection).
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        super.actionPerformed(e);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        _imageSaver.setSize(Projection.getP(width * 0.33f), Projection.getP(height * 0.33f));
        _imageSaver.setLocationRelativeTo(null);
        _imageSaver.setVisible(true);
    }
}
