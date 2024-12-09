package swing.imagesaver;

import java.io.File;

/**
 * Interface for objects responsible for receiving the input data from {@link ImageSaver} and saving the image.
 *
 * @author MTomczyk
 */


public interface IFileSaveDelegate
{
    /**
     * Method for saving the image.
     *
     * @param currentDirectory current directory
     * @param selectedFile     file
     * @param extension        file extension
     * @param transparency     if true, transparency is considered (will not work with unsupported file formats)
     * @param quality          image quality (linked to file compression)
     */
    void saveImage(File currentDirectory, File selectedFile, String extension, boolean transparency, float quality);
}
