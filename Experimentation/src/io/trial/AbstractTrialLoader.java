package io.trial;

import exception.TrialException;
import scenario.Scenario;


/**
 * Abstract implementation of {@link ITrialLoader}. Provides common fields/functionalities.
 *
 * @author MTomczyk
 */


public abstract class AbstractTrialLoader extends AbstractTrialIO implements ITrialLoader
{
    /**
     * Parameterized constructor.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario currently processed scenario
     * @param trialID  ID of a test run being currently processed
     */
    public AbstractTrialLoader(String path, String filename, Scenario scenario, int trialID)
    {
        super(path, filename, scenario, trialID);
    }

    /**
     * Creates and returns the full path based on the folder path, filename, and saver's suffix (e.g., extension).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param trialID  ID of a trial being currently processed
     * @return full path
     */
    @Override
    protected String getFullPath(String path, String filename, int trialID)
    {
        return super.getFullPath(path, filename, trialID) + getFileSuffix();
    }

    /**
     * Creates a new instance of the object. Intended to be used by the trial executor to clone the initial object
     * instance one time per each performance indicator (i.e., one clone will be mapped with one performance indicator).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario scenario being currently processed
     * @param trialID  ID of the test run being currently processed
     * @return new object instance
     */
    @Override
    public ITrialLoader getInstance(String path, String filename, Scenario scenario, int trialID) throws TrialException
    {
        throw new TrialException("The \"get instance\" method is not implemented", null, this.getClass(), _scenario, _trialID);
    }

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return null;
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws TrialException the trail-level exception can be thrown  (e.g., then the requested path is invalid)
     */
    @Override
    public void load() throws TrialException
    {
        throw new TrialException("The \"load\" method is not implemented", null, this.getClass(), _scenario, _trialID);
    }

    /**
     * The implementation should store the given input data in the created file.
     *
     * @param size determines the number of elements to load (e.g., doubles)
     * @return elements loaded
     * @throws TrialException the trail-level exception can be thrown 
     */
    @Override
    public double[] retrieve(int size) throws TrialException
    {
        throw new TrialException("The \"retrieve\" method is not implemented", null, this.getClass(), _scenario, _trialID);
    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws TrialException the trail-level exception can be thrown 
     */
    @Override
    public void close() throws TrialException
    {
        throw new TrialException("The \"close\" method is not implemented", null, this.getClass(), _scenario, _trialID);
    }

}
