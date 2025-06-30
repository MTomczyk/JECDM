package io.trial;

import exception.TrialException;
import scenario.Scenario;

/**
 * Interface for classes responsible for storing per-trial results in files.
 *
 * @author MTomczyk
 */
public interface ITrialLoader
{
    /**
     * Creates a new instance of the object. Intended to be used by the trial executor to clone the initial object
     * instance one time per each performance indicator (i.e., one clone will be mapped to one performance indicator).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario scenario being currently processed
     * @param trialID  ID of the test run being currently processed
     * @return new object instance
     * @throws TrialException the trial exception can be cast 
     */
    ITrialLoader getInstance(String path, String filename, Scenario scenario, int trialID) throws TrialException;

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    String getFileSuffix();

    /**
     * The implementation should load a file and instantiate the input stream.
     *
     * @throws TrialException the trail-level exception can be thrown  (e.g., then the requested path is invalid)
     */
    void load() throws TrialException;

    /**
     * The implementation should load the given input data from the file.
     *
     * @param size determines the number of elements to load (by default = doubles)
     * @return elements loaded
     * @throws TrialException the trail-level exception can be thrown 
     */
    double[] retrieve(int size) throws TrialException;

    /**
     * The implementation should close the maintained input stream.
     *
     * @throws TrialException the trail-level exception can be thrown 
     */
    void close() throws TrialException;

}
