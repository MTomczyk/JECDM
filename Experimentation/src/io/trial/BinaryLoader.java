package io.trial;

import exception.TrialException;
import scenario.Scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The main implementation of {@link ITrialLoader} for binary files.
 *
 * @author MTomczyk
 */


public class BinaryLoader extends AbstractTrialLoader implements ITrialLoader
{
    /**
     * Default constructor.
     */
    public BinaryLoader()
    {
        super("", "", null, 0);
    }

    /**
     * Parameterized constructor.
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario currently processed scenario
     * @param trialID  ID of a test run being currently processed
     */
    protected BinaryLoader(String path, String filename, Scenario scenario, int trialID)
    {
        super(path, filename, scenario, trialID);
    }

    /**
     * Creates a new instance of the object. Intended to be used by the trial executor to clone the initial object
     * instance one time per each performance indicator (i.e., one clone will be mapped to one performance indicator).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @param scenario currently processed scenario
     * @param trialID  ID of a test run being currently processed
     * @return new object instance
     * @throws TrialException the trail-level exception can be thrown 
     */
    @Override
    public ITrialLoader getInstance(String path, String filename, Scenario scenario, int trialID) throws TrialException
    {
        try
        {
            return new BinaryLoader(path, filename, scenario, trialID);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, _scenario, _trialID);
        }
    }

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return ".bin";
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws TrialException the trail-level exception can be thrown  (e.g., then the requested path is invalid)
     */
    @Override
    public void load() throws TrialException
    {
        File file = new File(_fullPath);
        if (file.isDirectory())
            throw new TrialException("The path points to a directory, not a file", null, this.getClass(), _scenario, _trialID);

        try
        {
            _fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e)
        {
            throw new TrialException(e.toString(), this.getClass(), e, _scenario, _trialID);
        }
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
        try
        {
            byte[] data = _fileInputStream.readNBytes(8 * size);
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.order(ByteOrder.BIG_ENDIAN);
            double[] r = new double[size];
            for (int i = 0; i < size; i++) r[i] = buffer.getDouble();
            return r;

        } catch (IOException e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, _scenario, _trialID);
        }

    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws TrialException the trail-level exception can be thrown 
     */
    @Override
    public void close() throws TrialException
    {
        try
        {
            if (_fileInputStream != null) _fileInputStream.close();
        } catch (IOException e)
        {
            throw new TrialException(e.toString(), this.getClass(), e, _scenario, _trialID);
        }
    }


}
