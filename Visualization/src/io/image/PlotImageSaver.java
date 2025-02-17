package io.image;

import container.PlotContainer;
import swing.imagesaver.IFileSaveDelegate;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Implementation of {@link IFileSaveDelegate} responsible for saving rendering results of {@link plot.AbstractPlot}.
 *
 * @author MTomczyk
 */


public class PlotImageSaver implements IFileSaveDelegate
{
    /**
     * Plot container.
     */
    private PlotContainer _PC;

    /**
     * Setter for plot container.
     *
     * @param PC plot container
     */
    public void setPlotContainer(PlotContainer PC)
    {
        _PC = PC;
    }

    /**
     * Method for saving the image.
     *
     * @param currentDirectory current directory
     * @param selectedFile     file
     * @param extension        file extension
     * @param transparency     if true, transparency is considered (will not work with unsupported file formats)
     * @param quality          image quality (linked to file compression (from 0.0f to 1.0f)
     */
    @Override
    public void saveImage(File currentDirectory, File selectedFile, String extension, boolean transparency, float quality)
    {
        BufferedImage image;

        try
        {
            image = _PC.getPlot().getPlotScreenshot(transparency);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(_PC.getPlot(),
                    "Could not obtain plot screenshot",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        ImageSaver.saveImage(_PC.getPlot(), image, selectedFile, extension, quality);
    }


}
