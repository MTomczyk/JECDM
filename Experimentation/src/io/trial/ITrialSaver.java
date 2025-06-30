package io.trial;

import exception.TrialException;
import scenario.Scenario;

/**
 * Interface for classes responsible for storing per-trial results in files.
 *
 * @author MTomczyk
 */
public interface ITrialSaver
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
     * @throws TrialException the trial-level exception can be cast 
     */
    ITrialSaver getInstance(String path, String filename, Scenario scenario, int trialID) throws TrialException;

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    String getFileSuffix();

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws TrialException the trail-level exception can be thrown  (e.g., then the requested path is invalid)
     */
    void create() throws TrialException;

    /**
     * The implementation should store the given input data in the created file.
     *
     * @param data       input data to be stored
     * @param offset     starting index in the data array
     * @param length     represents how many values should be stored (i.e., data[offset]:data[offset + length - 1] (inclusive) should be stored))
     * @throws TrialException the trail-level exception can be thrown 
     */
    void store(double[] data, int offset, int length) throws TrialException;

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws TrialException the trail-level exception can be thrown 
     */
    void close() throws TrialException;

}
