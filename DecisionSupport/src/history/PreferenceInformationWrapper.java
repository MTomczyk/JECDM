package history;

import exeption.HistoryException;
import preference.IPreferenceInformation;
import utils.StringUtils;

import java.time.LocalDateTime;

/**
 * Wrapper for the preference information. It is supposed to store additional technical fields and provide related functionalities.
 *
 * @author MTomczyk
 */
public class PreferenceInformationWrapper
{
    /**
     * Wrapped preference example.
     */
    public final IPreferenceInformation _preferenceInformation;

    /**
     * ID associated with the example. The ID is used as a hash code, and when checking equality.
     */
    public final int _id;

    /**
     * Timestamp (iteration) associated with the example.
     */
    public final int _iteration;

    /**
     * Timestamp (date and time) associated with the example.
     */
    public final LocalDateTime _dateTime;

    /**
     * Parameterized constructor.
     *
     * @param preferenceInformation preference example to be wrapped
     * @param id                    id associated with the preference example
     * @param iteration             timestamp (iteration) associated with the preference example
     * @param dateTime              timestamp (date and time) associated with the preference example is instantiated
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    protected PreferenceInformationWrapper(IPreferenceInformation preferenceInformation, int id, int iteration, LocalDateTime dateTime) throws HistoryException
    {
        if (preferenceInformation == null) throw new HistoryException("The preference information is not provided (is null)", this.getClass());
        _preferenceInformation = preferenceInformation;
        _id = id;
        _iteration = iteration;
        _dateTime = dateTime;
    }

    /**
     * Returns test instance.
     * @param preferenceInformation preference information to be wrapped
     * @return test instance
     */
    public static PreferenceInformationWrapper getTestInstance(IPreferenceInformation preferenceInformation)
    {
        try
        {
            return new PreferenceInformationWrapper(preferenceInformation, 0, 0,null);
        } catch (HistoryException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the hash code (id is used for this purpose).
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _id;
    }

    /**
     * Method for checking equality (based on comparing the ID).
     *
     * @param obj other object
     * @return comparison result (true if objects are equal).
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof PreferenceInformationWrapper o)) return false;
        return _id == o._id;
    }

    /**
     * Returns a string representation.
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        return "[" + _preferenceInformation.toString() + " ; id = " + _id + " ; iteration = " + _iteration + " ; date time = " + StringUtils.getTimestamp(_dateTime) + "]";
    }

    /**
     * Returns a cloned object. Note that it is not guaranteed that the {@link PreferenceInformationWrapper#_preferenceInformation}
     * will deliver a completely deep copy.
     *
     * @return cloned object
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public PreferenceInformationWrapper getClone() throws HistoryException
    {
        return new PreferenceInformationWrapper(_preferenceInformation, _id, _iteration, _dateTime);
    }
}
