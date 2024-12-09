package history;

import exeption.HistoryException;
import preference.IPreferenceInformation;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

/**
 * The main class that represents the history of preference elicitation. It stores all the collected preference examples
 * and provides auxiliary data and functionalities.
 *
 * @author MTomczyk
 */
public class History
{
    /**
     * List of all preference examples collected (wrapped using {@link PreferenceInformationWrapper}).
     * Note that this list can be altered, e.g., on the decision maker's will or by {@link inconsistency.IInconsistencyHandler}
     * -- the later can only alter the history copies stored in the model system.
     * Important note: it is assumed that the examples are kept sorted from the oldest to the newest.
     */
    private final LinkedList<PreferenceInformationWrapper> _preferenceInformation;

    /**
     * Object's name..
     */
    private final String _name;


    /**
     * Counter for the registered preference information (used as the preference information ID).
     */
    private int _registrationCounter = 0;

    /**
     * Parameterized constructor.
     *
     * @param name object's name.
     */
    public History(String name)
    {
        _preferenceInformation = new LinkedList<>();
        _name = name;
    }

    /**
     * This method registers new preference examples (adds to the list). The method also validates the provided examples
     * (their order and uniqueness). If the validation fails, the method throws an exception.
     *
     * @param preferenceInformation preference example to be added (provided via wrappers)
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public void registerPreferenceInformation(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws HistoryException
    {
        _preferenceInformation.addAll(preferenceInformation);
        validate(_preferenceInformation);
    }

    /**
     * This method registers new preference examples (adds to the list). Additionally, it supplies the preference
     * statement with the timestamps (iteration and date-time). The method also validates the provided examples (their
     * order and uniqueness). If the validation fails, the method throws an exception.
     *
     * @param preferenceInformation preference example to be added
     * @param iteration             timestamp (iteration)
     * @param addTimestamp          if true, the timestamp in the form of date and time is added
     * @return registered preference examples (wrapped)
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public LinkedList<PreferenceInformationWrapper> registerPreferenceInformation(LinkedList<IPreferenceInformation> preferenceInformation, int iteration,
                                                                                  boolean addTimestamp) throws HistoryException
    {
        LinkedList<PreferenceInformationWrapper> piw = new LinkedList<>();
        for (IPreferenceInformation pi : preferenceInformation)
        {
            LocalDateTime timestamp = null;
            if (addTimestamp) timestamp = LocalDateTime.now();
            piw.add(new PreferenceInformationWrapper(pi, _registrationCounter++, iteration, timestamp));

        }
        registerPreferenceInformation(piw);
        return piw;
    }

    /**
     * This method registers a new preference example (adds to the list). Additionally, it supplies the preference
     * statement with the timestamps (iteration and date-time). The method also validates the provided examples
     * (their order and uniqueness). If the validation fails, the method throws an exception. Adds the registration timestamp.
     *
     * @param preferenceInformation preference example to be added
     * @param iteration             timestamp (iteration)
     * @return registered preference examples (wrapped)
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public LinkedList<PreferenceInformationWrapper> registerPreferenceInformation(IPreferenceInformation preferenceInformation, int iteration) throws HistoryException
    {
        return registerPreferenceInformation(preferenceInformation, iteration, true);
    }


    /**
     * This method registers a new preference example (adds to the list). Additionally, it supplies the preference
     * statement with the timestamps (iteration and date-time). The method also validates the provided examples
     * (their order and uniqueness). If the validation fails, the method throws an exception.
     *
     * @param preferenceInformation preference example to be added
     * @param iteration             timestamp (iteration)
     * @param addTimestamp          if true, the timestamp in the form of date and time is added
     * @return registered preference examples (wrapped)
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public LinkedList<PreferenceInformationWrapper> registerPreferenceInformation(IPreferenceInformation preferenceInformation, int iteration, boolean addTimestamp) throws HistoryException
    {
        LinkedList<IPreferenceInformation> pi = new LinkedList<>();
        pi.add(preferenceInformation);
        return registerPreferenceInformation(pi, iteration, addTimestamp);
    }


    /**
     * Auxiliary method for updating the history of preference elicitation. It REPLACES all the preference examples
     * stored with new ones (by copying elements, not the list reference). The new examples should be a subset
     * of the current history. Effectively, this method can be called to reduce the history. The method also validates
     * the provided examples (their order and uniqueness). If the validation fails, the method throws an exception.
     * Finally, the report on the update is returned.
     *
     * @param wrappers        new preference examples that are to replace the old ones (provided via wrappers)
     * @param updateIteration update timestamp (iteration)
     * @param updateDateTime  update timestamp (date and time)
     * @return report on the update
     * @throws HistoryException the exception can be thrown and propagated higher
     */
    public Report updateHistoryWithASubset(LinkedList<PreferenceInformationWrapper> wrappers,
                                           int updateIteration, LocalDateTime updateDateTime) throws HistoryException
    {
        Set<PreferenceInformationWrapper> existing = new HashSet<>(_preferenceInformation);
        for (PreferenceInformationWrapper w : wrappers)
            if (!existing.contains(w))
                throw new HistoryException("The preference example = " + w.toString() + " does not exist in the history", this.getClass());

        validate(wrappers);

        Report report = new Report();
        report._numberOfPreferenceExamplesBeforeUpdate = _preferenceInformation.size();
        report._numberOfPreferenceExamplesAfterUpdate = wrappers.size();
        report._iteration = updateIteration;
        report._dateTime = updateDateTime;

        _preferenceInformation.clear();
        for (PreferenceInformationWrapper w : wrappers)
        {
            existing.remove(w);
            _preferenceInformation.add(w);
        }

        report._numberOfPreferenceExamplesRemovedDuringUpdate = existing.size();
        report._removedPreferenceExamples = new LinkedList<>(existing.stream().toList());
        return report;
    }

    /**
     * This method can be called to remove an existing preference information from the history (the requested
     * object must exist in the history; if not, the method returns false).
     *
     * @param piw preference information to be removed (must exist in the history)
     * @return true, if the preference information was successfully removed, false otherwise
     */
    public boolean remove(PreferenceInformationWrapper piw)
    {
        ListIterator<PreferenceInformationWrapper> iterator = _preferenceInformation.listIterator();
        PreferenceInformationWrapper p;
        while (iterator.hasNext())
        {
            p = iterator.next();
            if (p.equals(piw))
            {
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the number of preference examples stored.
     *
     * @return the number of preference examples stored
     */
    public int getNoPreferenceExamples()
    {
        return _preferenceInformation.size();
    }

    /**
     * Getter for all stored preference statements.
     * Returned as a copy. The list elements are also cloned (deep copy is not guaranteed).
     *
     * @return copy of the list of all stored preference statements
     * @throws HistoryException history exception can be thrown and propagated higher (thrown when the input preference information is null)
     */
    public LinkedList<PreferenceInformationWrapper> getPreferenceInformationCopy() throws HistoryException
    {
        LinkedList<PreferenceInformationWrapper> copy = new LinkedList<>();
        for (PreferenceInformationWrapper piw : _preferenceInformation)
            copy.add(piw.getClone());
        return copy;
    }

    /**
     * Auxiliary method for validating the preference example wrappers. The input wrappers must be sorted in a non-decreasing
     * order of iteration (and timestamp if used). Additionally, they must be unique (checked by verifying IDs). If some
     * of these conditions are not satisfied, the method throws an appropriate exception.
     *
     * @param wrappers preference example wrappers to be examined
     * @throws HistoryException history exception can be thrown and propagated higher
     */
    private void validate(LinkedList<PreferenceInformationWrapper> wrappers) throws HistoryException
    {
        Integer previousIteration = null;
        LocalDateTime previousDateTime = null;
        for (PreferenceInformationWrapper w : wrappers)
        {
            if ((previousIteration != null) && (previousIteration > w._iteration))
                throw new HistoryException("The preference examples are not sorted in a non-decreasing order of iteration", this.getClass());
            previousIteration = w._iteration;

            if ((previousDateTime != null) && (w._dateTime != null) && (previousDateTime.isAfter(w._dateTime)))
                throw new HistoryException("The preference examples are not sorted in a non-decreasing order of date and time", this.getClass());
            if (w._dateTime != null) previousDateTime = w._dateTime;
        }

        Set<PreferenceInformationWrapper> piws = new HashSet<>();
        for (PreferenceInformationWrapper piw : wrappers)
        {
            if (piws.contains(piw))
                throw new HistoryException("The preference example = " + piw.toString() + " is not unique", this.getClass());
            piws.add(piw);
        }
    }

    /**
     * Returns the object's name.
     *
     * @return object's name
     */
    @Override
    public String toString()
    {
        return _name;
    }

    /**
     * Auxiliary method that returns a string containing the object's name and string representations of all preference
     * examples stored.
     *
     * @return full string representation
     */
    public String getFullStringRepresentation()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_name);
        if (!_preferenceInformation.isEmpty()) sb.append(System.lineSeparator());
        int idx = 0;
        for (PreferenceInformationWrapper piw : _preferenceInformation)
        {
            sb.append(piw.toString());
            if (idx++ < _preferenceInformation.size() - 1) sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
