package io.image;

import container.PlotContainer;
import swing.imagesaver.IFileSaveDelegate;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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

        String fullPath = selectedFile.getPath() + "." + extension;
        FileImageOutputStream ios;

        try
        {
            ios = new FileImageOutputStream(new File(fullPath));
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(_PC.getPlot(),
                    "Could not create a file (" + fullPath + ")",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(extension);
        ImageWriter writer;

        try
        {
            writer = it.next();
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(_PC.getPlot(),
                    "No image writer found  (for extension = " + extension + ")",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        if (!extension.equalsIgnoreCase("bmp")) iwp.setCompressionQuality(quality);
        writer.setOutput(ios);
        IIOImage ioImage = new IIOImage(image, null, null);

        try
        {
            writer.write(null, ioImage, iwp);
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(_PC.getPlot(),
                    "Could not save an image (" + selectedFile.getName() + "." + extension + ")",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        writer.dispose();
        image.flush();
    }
}
