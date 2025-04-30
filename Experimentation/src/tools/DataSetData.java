package tools;

/**
 * Auxiliary class providing bounds for data matrix contained in an Excel file.
 *
 * @author MTomczyk
 */
public class DataSetData
{
    /**
     * Data set name.
     */
    public final String _name;

    /**
     * Index of the column associated with data set x-coordinate values (starting from 0, points to a row in the
     * already derived from Excel data matrix).
     */
    public final int _xIndex;

    /**
     * Index of the column associated with data set y-coordinate values (starting from 0, points to a row in the
     * already derived from Excel data matrix).
     */
    public final int _yIndex;

    /**
     * Index of the column associated with data upper deviation from y (e.g., standard deviation;
     * starting from 0, points to a row in the already derived from Excel data matrix; not used if null).
     */
    public final Integer _yPrimaryUpperDeviationIndex;

    /**
     * Index of the column associated with data lower deviation from y (e.g., standard deviation;
     * starting from 0, points to a row in the already derived from Excel data matrix; not used if null).
     */
    public final Integer _yPrimaryLowerDeviationIndex;

    /**
     * Auxiliary multiplier that can be used to rescale the upper deviation. For instance, if
     * the primary deviations point to a standard deviation, they should be premultiplied by 0.5f (so that upper and
     * lower deviations give a standard deviation in total).
     */
    public final float _primaryUpperMultiplier;

    /**
     * Auxiliary multiplier that can be used to rescale the lower deviation. For instance, if
     * the primary deviations point to a standard deviation, they should be premultiplied by 0.5f (so that upper and
     * lower deviations give a standard deviation in total).
     */
    public final float _primaryLowerMultiplier;

    /**
     * Parameterized constructor.
     *
     * @param name   data set name
     * @param xIndex index of the column associated with data set x-coordinate values (starting
     *               from 0, points to a row in the already derived from Excel data matrix)
     * @param yIndex index of the column associated with data set y-coordinate values (starting
     *               from 0, points to a row in the already derived from Excel data matrix)
     */
    public DataSetData(String name,
                       int xIndex,
                       int yIndex)
    {
        this(name, xIndex, yIndex, null, null, 1.0f, 1.0f);
    }


    /**
     * Parameterized constructor.
     *
     * @param name                        data set name
     * @param xIndex                      index of the column associated with data set x-coordinate values (starting
     *                                    from 0, points to a row in the already derived from Excel data matrix)
     * @param yIndex                      index of the column associated with data set y-coordinate values (starting
     *                                    from 0, points to a row in the already derived from Excel data matrix)
     * @param yPrimaryUpperDeviationIndex index of the column associated with data upper deviation from y (e.g.,
     *                                    standard deviation; starting from 0, points to a row in the already derived
     *                                    from Excel data matrix; not used if null)
     * @param yPrimaryLowerDeviationIndex index of the column associated with data lower deviation from y (e.g.,
     *                                    standard deviation; starting from 0, points to a row in the already derived
     *                                    from Excel data matrix; not used if null)
     * @param primaryUpperMultiplier      auxiliary multiplier that can be used to rescale the upper deviation; for
     *                                    instance, if the primary deviations point to a standard deviation, they should
     *                                    be premultiplied by 0.5f (so that upper and lower deviations give a standard
     *                                    deviation in total)
     * @param primaryLowerMultiplier      auxiliary multiplier that can be used to rescale the lower deviation; for
     *                                    instance, if the primary deviations point to a standard deviation, they should
     *                                    be premultiplied by 0.5f (so that upper and lower deviations give a standard
     *                                    deviation in total)
     */
    public DataSetData(String name,
                       int xIndex,
                       int yIndex,
                       int yPrimaryUpperDeviationIndex,
                       int yPrimaryLowerDeviationIndex,
                       float primaryUpperMultiplier,
                       float primaryLowerMultiplier)
    {
        this(name, xIndex, yIndex, (Integer) yPrimaryUpperDeviationIndex, (Integer) yPrimaryLowerDeviationIndex, primaryUpperMultiplier, primaryLowerMultiplier);
    }

    /**
     * Parameterized constructor.
     *
     * @param name                        data set name
     * @param xIndex                      index of the column associated with data set x-coordinate values (starting
     *                                    from 0, points to a row in the already derived from Excel data matrix)
     * @param yIndex                      index of the column associated with data set y-coordinate values (starting
     *                                    from 0, points to a row in the already derived from Excel data matrix)
     * @param yPrimaryUpperDeviationIndex index of the column associated with data upper deviation from y (e.g.,
     *                                    standard deviation; starting from 0, points to a row in the already derived
     *                                    from Excel data matrix; not used if null)
     * @param yPrimaryLowerDeviationIndex index of the column associated with data lower deviation from y (e.g.,
     *                                    standard deviation; starting from 0, points to a row in the already derived
     *                                    from Excel data matrix; not used if null)
     * @param primaryUpperMultiplier      auxiliary multiplier that can be used to rescale the upper deviation; for
     *                                    instance, if the primary deviations point to a standard deviation, they should
     *                                    be premultiplied by 0.5f (so that upper and lower deviations give a standard
     *                                    deviation in total)
     * @param primaryLowerMultiplier      auxiliary multiplier that can be used to rescale the lower deviation; for
     *                                    instance, if the primary deviations point to a standard deviation, they should
     *                                    be premultiplied by 0.5f (so that upper and lower deviations give a standard
     *                                    deviation in total)
     */
    protected DataSetData(String name,
                          int xIndex,
                          int yIndex,
                          Integer yPrimaryUpperDeviationIndex,
                          Integer yPrimaryLowerDeviationIndex,
                          float primaryUpperMultiplier,
                          float primaryLowerMultiplier)
    {
        _name = name;
        _xIndex = xIndex;
        _yIndex = yIndex;
        _yPrimaryUpperDeviationIndex = yPrimaryUpperDeviationIndex;
        _yPrimaryLowerDeviationIndex = yPrimaryLowerDeviationIndex;
        _primaryUpperMultiplier = primaryUpperMultiplier;
        _primaryLowerMultiplier = primaryLowerMultiplier;
    }

    /**
     * This method creates an object suitably parameterized for handling data comprising x-coordinates, y-coordinates,
     * and standard deviations around y-coordinates.
     *
     * @param name     data set name
     * @param xIndex   index of the column associated with data set x-coordinate values (starting
     *                 from 0, points to a row in the already derived from Excel data matrix)
     * @param yIndex   index of the column associated with data set y-coordinate values (starting
     *                 from 0, points to a row in the already derived from Excel data matrix)
     * @param stdIndex index of the column associated with standard deviations, (starting from 0, points to a row in
     *                 the already derived from Excel data matrix)
     * @return suitably parameterized object
     */
    public static DataSetData getForDataWithStandardDeviation(String name,
                                                              int xIndex,
                                                              int yIndex,
                                                              int stdIndex)
    {
        return new DataSetData(name, xIndex, yIndex, stdIndex, stdIndex, 0.5f, 0.5f);
    }

    /**
     * This method creates an object suitably parameterized for handling data comprising x-coordinates and y-coordinates.
     *
     * @param name     data set name
     * @param xIndex   index of the column associated with data set x-coordinate values (starting
     *                 from 0, points to a row in the already derived from Excel data matrix)
     * @param yIndex   index of the column associated with data set y-coordinate values (starting
     *                 from 0, points to a row in the already derived from Excel data matrix)
     * @return suitably parameterized object
     */
    public static DataSetData getData(String name, int xIndex, int yIndex)
    {
        return new DataSetData(name, xIndex, yIndex, null, null, 0.0f, 0.0f);
    }

    /**
     * This method checks if the primary deviations are used (upper and lower points must not be null)
     *
     * @return true, if the primary deviations are used; false otherwise
     */
    public boolean arePrimaryDeviationsUsed()
    {
        return _yPrimaryUpperDeviationIndex != null && _yPrimaryLowerDeviationIndex != null;
    }
}
