package io.trial;

import exception.TrialException;
import scenario.Scenario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The main implementation of {@link ITrialSaver}.
 * Using a binary file for trial-level data storage helps keep disk memory level consumption at a lower level
 * (however, it makes the manual inspection of the files more difficult). Note that the big endian encoding is used.
 *
 * @author MTomczyk
 */


public class BinarySaver extends AbstractTrialSaver implements ITrialSaver
{
    /**
     * Default constructor.
     */
    public BinarySaver()
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
    protected BinarySaver(String path, String filename, Scenario scenario, int trialID)
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
    public ITrialSaver getInstance(String path, String filename, Scenario scenario, int trialID) throws TrialException
    {
        try
        {
            return new BinarySaver(path, filename, scenario, trialID);
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
    public void create() throws TrialException
    {
        File file = getFileAtTrialLevel();

        try
        {
            _fileOutputStream = new FileOutputStream(file, false);
        } catch (FileNotFoundException e)
        {
            throw new TrialException(e.toString(), this.getClass(), e, _scenario, _trialID);
        }
    }

    /**
     * The implementation should store the given input data in the created file.
     *
     * @param data   input data to be stored
     * @param offset starting index in the data array
     * @param length represents how many values should be stored (i.e., data[offset]:data[offset + length - 1] (inclusive) should be stored))
     * @throws TrialException the trail-level exception can be thrown  (e.g., then the requested path is invalid)
     */
    @Override
    public void store(double[] data, int offset, int length) throws TrialException
    {
        if (data == null)
            throw new TrialException("The data is not provided (the array is null)", null, this.getClass(), _scenario, _trialID);
        if (data.length == 0)
            throw new TrialException("The data is not provided (the array is empty)", null, this.getClass(), _scenario, _trialID);
        if (length < 1)
            throw new TrialException("The length should not be less than 1", null, this.getClass(), _scenario, _trialID);
        if (offset < 0)
            throw new TrialException("The offset should not be less than 0", null, this.getClass(), _scenario, _trialID);
        if (offset + length > data.length)
            throw new TrialException("The offset + length exceeds the input data length", null, this.getClass(), _scenario, _trialID);

        byte[] bytes = new byte[8 * length]; // 8 bytes per value
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        for (int i = offset; i < offset + length; i++) buffer.putDouble(data[i]);
        try
        {
            _fileOutputStream.write(bytes);
        } catch (IOException e)
        {
            throw new TrialException(e.toString(), this.getClass(), e, _scenario, _trialID);
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
            if (_fileOutputStream == null) return;
            _fileOutputStream.close();
        } catch (IOException e)
        {
            throw new TrialException(e.toString(), this.getClass(), e, _scenario, _trialID);
        }
    }


}
