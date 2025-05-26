package io.trial;

import exception.TrialException;
import io.AbstractIO;
import scenario.Scenario;

import java.io.File;

/**
 * Abstract class that provides fields/functionalities.
 *
 * @author MTomczyk
 */


public abstract class AbstractTrialIO extends AbstractIO
{

    /**
     * ID of a test run being currently processed.
     */
    protected final int _trialID;

    /**
     * Parameterized constructor.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario currently processed scenario
     * @param trialID  ID of a test run being currently processed
     */
    public AbstractTrialIO(String path, String filename, Scenario scenario, int trialID)
    {
        super(path, filename, scenario, 1);
        _trialID = trialID;
        _fullPath = getFullPath(path, filename, trialID);
    }

    /**
     * Creates and returns the full path based on the folder path and the file name filename.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param trialID  ID of the trial being currently processed
     * @return full path
     */
    protected String getFullPath(String path, String filename, int trialID)
    {
        return path + File.separatorChar + filename + "_" + _trialID;
    }

    /**
     * Auxiliary method for creating a file object at the trial level. The method first checks if a file is a
     * directory (if true, the exception is thrown). Then, if the file already exists, the method attempts to delete it.
     * If it fails, another exception is thrown. Finally, a file object is created (but the file itself has not been
     * instantiated yet).
     *
     * @return file object
     * @throws TrialException scenario-level exception can be thrown and propagated higher
     */
    protected File getFileAtTrialLevel() throws TrialException
    {
        File file = new File(_fullPath);
        if (file.isDirectory())
            throw new TrialException("The path points to a directory, not a file", null, this.getClass(), _scenario, _trialID);

        if (file.exists())
        {
            boolean deleted = file.delete();
            if (!deleted) throw new TrialException("Could not remove the already existing file",
                    null, this.getClass(), _scenario, _trialID);
        }
        return file;
    }
}
