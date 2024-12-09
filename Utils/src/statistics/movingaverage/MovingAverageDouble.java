package statistics.movingaverage;

import java.util.Arrays;

/**
 * Helps to calculate moving average of doubles.
 *
 * @author MTomczyk
 */

public class MovingAverageDouble extends AbstractMovingAverage
{
    /**
     * Array of doubles.
     */
    private final double[] _data;

    /**
     * Nominator (average = nominator / denominator).
     */
    private double _nom;


    /**
     * Determines the window size.
     *
     * @param windowSize window size.
     */
    public MovingAverageDouble(int windowSize)
    {
        super();
        _data = new double[windowSize];
        reset();
    }

    /**
     * Resets the moving average.
     */
    public void reset()
    {
        Arrays.fill(_data, 0.0d);
        _pointer = 0;
        _denom = 0;
        _nom = 0.0d;
        _counter = 0;
    }


    /**
     * Adds new value to the window.
     *
     * @param v new value
     */
    @SuppressWarnings("DuplicatedCode")
    public void addData(double v)
    {
        if (_denom >= _data.length) _nom -= _data[_pointer];
        else _denom++;
        _nom += v;
        _data[_pointer] = v;
        _pointer++;
        if (_pointer > _data.length - 1) _pointer = 0;

        _counter++;
        if (_counter >= _recalculateFromScratchEvery)
        {
            _counter = 0;
            _nom = 0.0d;
            for (double d : _data) _nom += d;
        }
    }

    /**
     * Returns the most recently added element. Returns null if the data collection is empty.
     *
     * @return the most recently added element
     */
    public Double getLastEntry()
    {
        if (_denom == 0) return null;
        if (_pointer == 0) return _data[_data.length - 1];
        return _data[_pointer - 1];
    }

    /**
     * Returns the average. If the size of the provided data is 0, null is returned.
     *
     * @return moving average
     */
    public Double getAverage()
    {
        if (_denom == 0) return null;
        return _nom / _denom;
    }


}
