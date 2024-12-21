package io;

import exception.CrossedScenariosException;
import exception.ScenarioException;
import scenario.CrossedScenarios;
import scenario.Scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Abstract class that provides common fields/functionalities related IO operations.
 *
 * @author MTomczyk
 */


public abstract class AbstractIO
{
    /**
     * File output stream.
     */
    protected FileOutputStream _fileOutputStream;

    /**
     * File input stream.
     */
    protected FileInputStream _fileInputStream;

    /**
     * Currently processed scenario.
     */
    protected final Scenario _scenario;

    /**
     * Currently processed crossed scenarios.
     */
    protected final CrossedScenarios _crossedScenarios;

    /**
     * Full (absolute) path to the file.
     */
    protected String _fullPath;

    /**
     *  The level of cross-analysis (should be at least 2)/
     */
    protected int _level;

    /**
     * Parameterized constructor.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario currently processed scenario
     * @param level  the level of cross-analysis (should be at least 2)
     */
    public AbstractIO(String path, String filename, Scenario scenario, int level)
    {
        _level = level;
        _scenario = scenario;
        _crossedScenarios = null;
        _fullPath = getFullPath(path, filename);
    }

    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios currently processed crossed scenarios
     * @param level  the level of cross-analysis (should be at least 2)
     */
    public AbstractIO(String path, String filename, CrossedScenarios crossedScenarios, int level)
    {
        _level = level;
        _scenario = null;
        _crossedScenarios = crossedScenarios;
        _fullPath = getFullPath(path, filename);
    }

    /**
     * Creates and returns the full path based on the folder path and the file name filename.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @return full path
     */
    protected String getFullPath(String path, String filename)
    {
        return path + File.separatorChar + filename;
    }

    /**
     * Auxiliary method for creating a file object at the scenario level. The method first checks if a file is a
     * directory (if true, the exception is thrown). Then, if the file already exists, the method attempts to delete it.
     * If it fails, another exception is thrown. Finally, a file object is created (but the file itself has not been
     * instantiated yet).
     *
     * @return file object
     * @throws ScenarioException scenario-level exception can be thrown and propagated higher
     */
    protected File getFileAtScenarioLevel() throws ScenarioException
    {
        File file = new File(_fullPath);
        if (file.isDirectory())
            throw new ScenarioException("The path points to a directory, not a file", this.getClass(), _scenario);

        if (file.exists())
        {
            boolean deleted = file.delete();
            if (!deleted) throw new ScenarioException("Could not remove the already existing file",
                    this.getClass(), _scenario);
        }
        return file;
    }

    /**
     * Auxiliary method for creating a file object at the crossed scenarios level. The method first checks if a file is a
     * directory (if true, the exception is thrown). Then, if the file already exists, the method attempts to delete it.
     * If it fails, another exception is thrown. Finally, a file object is created (but the file itself has not been
     * instantiated yet).
     *
     * @return file object
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown and propagated higher
     */
    protected File getFileAtCrossedScenariosLevel() throws CrossedScenariosException
    {
        try
        {
            return getFileAtScenarioLevel();
        } catch (ScenarioException e)
        {
            throw new CrossedScenariosException(e.getMessage(), this.getClass(), e, _crossedScenarios);
        }
    }


}
