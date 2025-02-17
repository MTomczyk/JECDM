package io.image;

import plot.AbstractPlot;

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
 * Provides means for storing a buffered image as a file.
 *
 * @author MTomczyk
 */


public class ImageSaver
{


    /**
     * Method for saving the image.
     *
     * @param image     current image
     * @param filePath  filePath (without extension)
     * @param extension file extension
     * @param quality   image quality (linked to file compression (from 0.0f to 1.0f)
     */
    public static void saveImage(BufferedImage image, String filePath, String extension, float quality)
    {
        ImageSaver.saveImage(null, image, new File(filePath), extension, quality);
    }

    /**
     * Method for saving the image.
     *
     * @param plot         can be null; if provided, message dialogues associated with the plot will pop up in case of errors
     * @param image        current image
     * @param selectedFile file (path without extension)
     * @param extension    file extension
     * @param quality      image quality (linked to file compression (from 0.0f to 1.0f)
     */
    public static void saveImage(AbstractPlot plot, BufferedImage image, File selectedFile, String extension, float quality)
    {
        String fullPath = selectedFile.getPath() + "." + extension;
        FileImageOutputStream ios;

        try
        {
            ios = new FileImageOutputStream(new File(fullPath));
        } catch (Exception e)
        {
            if (plot != null)
            {
                JOptionPane.showMessageDialog(plot, "Could not create a file (" + fullPath + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            else System.out.println("Could not create a file (" + fullPath + ")");
            return;
        }


        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(extension);
        ImageWriter writer;

        try
        {
            writer = it.next();
        } catch (Exception e)
        {
            if (plot != null)
            {
                JOptionPane.showMessageDialog(plot, "No image writer found  (for extension = " + extension + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            else System.out.println("No image writer found  (for extension = " + extension + ")");
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
            if (plot != null)
            {
                JOptionPane.showMessageDialog(plot, "Could not save an image (" + selectedFile.getName() + "." + extension + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            else System.out.println("Could not save an image (" + selectedFile.getName() + "." + extension + ")");

            return;
        }

        writer.dispose();
        image.flush();
    }


}
