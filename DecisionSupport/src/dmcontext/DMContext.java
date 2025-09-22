package dmcontext;

import alternative.AbstractAlternatives;
import criterion.Criteria;
import random.IRandom;
import space.normalization.INormalization;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.EnumSet;

/**
 * This supportive class helps integrate methods from the EMO stream with the methods from MCDA. The EMO implies a
 * dynamically changing environment (solutions, objective space, etc.). This class is used as a container for such data,
 * which is passed throughout processing. Additionally, this class serves as a top-level, i.e., global, data container.
 *
 * @author MTomczyk
 */
public class DMContext
{
    /**
     * Reason for calling/running the decision support system
     */
    public enum Reason
    {
        /**
         * New iteration began. It is a regular reason associated with the need to execute both the preference
         * elicitation attempt and the consequent model construction process.
         */
        REGULAR_ITERATION,

        /**
         * Should be associated with a case when an objective space has changed and, thus, there is a need to execute
         * the model construction process (since the change in the normalizations could have invalidated some of the
         * learnt models).
         */
        OS_CHANGED,

        /**
         * Reserved for custom purposes.
         */
        CUSTOM1,
        /**
         * Reserved for custom purposes.
         */
        CUSTOM2,

        /**
         * Reserved for custom purposes.
         */
        CUSTOM3,
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Field representing current iteration (e.g., generation).
         */
        public Integer _currentIteration;

        /**
         * This field is supposed to contain all the alternatives maintained at some specific processing stage.
         */
        public AbstractAlternatives<?> _currentAlternativesSuperset;

        /**
         * This field is supposed to store the data on the current objective space (can be null).
         */
        public ObjectiveSpace _currentOS;

        /**
         * Normalization builder that will be used to generate normalization objects using the OSs (if null,
         * the standard linear builder will be used ({@link StandardLinearBuilder})). Important note: it is
         * recommended that all normalization functions built via this object extend
         * {@link space.normalization.minmax.AbstractMinMaxNormalization}.
         */
        public INormalizationBuilder _normalizationBuilder;

        /**
         * This flag indicates whether the OS changed from one iteration to another.
         */
        public boolean _osChanged;

        /**
         * Random number generator (should be provided when some random-based components are involved; they will try
         * using this object).
         */
        public IRandom _R;

        /**
         * Reason(s) for running the system (Reason.REGULAR_ITERATION by default).
         */
        public EnumSet<Reason> _reasons = EnumSet.of(Reason.REGULAR_ITERATION);

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param currentAlternativesSuperset this field is supposed to contain all the alternatives maintained at some
         *                                    specific processing stage
         * @param currentOS                   this field is supposed to store the data on the current objective space
         * @param osChanged                   this flag indicates whether the OS changed from one iteration to another
         *                                    (if so, the previous OS and the current one should differ)
         * @param currentIteration            field representing current iteration (e.g., generation)
         * @param normalizationBuilder        normalization builder that will be used to generate normalization objects
         *                                    using the OSs
         * @param R                           random number generator
         */
        protected Params(AbstractAlternatives<?> currentAlternativesSuperset,
                         ObjectiveSpace currentOS,
                         boolean osChanged,
                         int currentIteration,
                         INormalizationBuilder normalizationBuilder,
                         IRandom R)
        {
            _currentAlternativesSuperset = currentAlternativesSuperset;
            _currentOS = currentOS;
            _osChanged = osChanged;
            _currentIteration = currentIteration;
            _normalizationBuilder = normalizationBuilder;
            _R = R;
        }
    }


    /**
     * This field is supposed to contain all the alternatives maintained at some specific processing stage.
     */
    private final AbstractAlternatives<?> _currentAlternativesSuperset;

    /**
     * This field is supposed to store the data on the current objective space  (can be null).
     */
    private final ObjectiveSpace _currentOS;

    /**
     * Normalizations object that can be used to rescale (normalize) the current objective space. Important note: it is
     * recommended that all normalization functions extend
     * {@link space.normalization.minmax.AbstractMinMaxNormalization}.
     */
    private final INormalization[] _normalizationsCurrentOS;

    /**
     * This flag indicates whether the OS changed from one iteration to another (if so, the previous OS and the current
     * one should differ).
     */
    private final boolean _osChanged;

    /**
     * Random number generator (should be provided when some random-based components are involved; they will try using
     * this object).
     */
    private final IRandom _R;

    /**
     * Field representing current iteration (e.g., generation).
     */
    private final int _currentIteration;

    /**
     * Field representing current timestamp (date and time; stored automatically when creating the object instance).
     */
    private final LocalDateTime _currentTimestamp;

    /**
     * Represents the consistent family of criteria.
     */
    private final Criteria _criteria;

    /**
     * Date and time representing when the system started processing.
     */
    private final LocalDateTime _systemStartingTimestamp;

    /**
     * Reason(s) for running the system (Reason.REGULAR_ITERATION by default).
     */
    private final EnumSet<Reason> _reasons;

    /**
     * Parameterized constructor.
     *
     * @param criteria                    represents the consistent family of criteria
     * @param systemStartingTimestamp     date and time representing when the system started processing
     * @param currentAlternativesSuperset this field is supposed to contain all the alternatives maintained at some
     *                                    specific processing stage
     * @param currentOS                   this field is supposed to store the data on the current objective space
     * @param osChanged                   this flag indicates whether the OS changed from one iteration to another
     *                                    (if so, the previous OS and the current one should differ)
     * @param currentIteration            field representing current iteration (e.g., generation)
     */
    public DMContext(Criteria criteria,
                     LocalDateTime systemStartingTimestamp,
                     AbstractAlternatives<?> currentAlternativesSuperset,
                     ObjectiveSpace currentOS,
                     boolean osChanged,
                     int currentIteration)
    {
        this(criteria, systemStartingTimestamp, currentAlternativesSuperset, currentOS, osChanged, currentIteration, new StandardLinearBuilder());
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria                    represents the consistent family of criteria
     * @param systemStartingTimestamp     date and time representing when the system started processing
     * @param currentAlternativesSuperset this field is supposed to contain all the alternatives maintained at some
     *                                    specific processing stage
     * @param currentOS                   this field is supposed to store the data on the current objective space
     * @param osChanged                   this flag indicates whether the OS changed from one iteration to another
     *                                    (if so, the previous OS and the current one should differ)
     * @param currentIteration            field representing current iteration (e.g., generation)
     * @param normalizationBuilder        normalization builder that will be used to generate normalization objects
     *                                    using the OSs
     */
    public DMContext(Criteria criteria,
                     LocalDateTime systemStartingTimestamp,
                     AbstractAlternatives<?> currentAlternativesSuperset,
                     ObjectiveSpace currentOS,
                     boolean osChanged,
                     int currentIteration,
                     INormalizationBuilder normalizationBuilder)
    {
        this(new Params(currentAlternativesSuperset, currentOS, osChanged, currentIteration, normalizationBuilder, null),
                criteria, systemStartingTimestamp);
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria                    represents the consistent family of criteria
     * @param systemStartingTimestamp     date and time representing when the system started processing
     * @param currentAlternativesSuperset this field is supposed to contain all the alternatives maintained at some
     *                                    specific processing stage
     * @param currentOS                   this field is supposed to store the data on the current objective space
     * @param osChanged                   this flag indicates whether the OS changed from one iteration to another
     *                                    (if so, the previous OS and the current one should differ)
     * @param currentIteration            field representing current iteration (e.g., generation)
     * @param normalizationBuilder        normalization builder that will be used to generate normalization objects
     *                                    using the OSs
     * @param R                           random number generator
     */
    public DMContext(Criteria criteria,
                     LocalDateTime systemStartingTimestamp,
                     AbstractAlternatives<?> currentAlternativesSuperset,
                     ObjectiveSpace currentOS,
                     boolean osChanged,
                     int currentIteration,
                     INormalizationBuilder normalizationBuilder,
                     IRandom R)
    {
        this(new Params(currentAlternativesSuperset, currentOS, osChanged, currentIteration, normalizationBuilder, R),
                criteria, systemStartingTimestamp);
    }

    /**
     * Parameterized constructor.
     *
     * @param p                       params container
     * @param criteria                represents the consistent family of criteria
     * @param systemStartingTimestamp date and time representing when the system started processing
     */
    public DMContext(Params p, Criteria criteria, LocalDateTime systemStartingTimestamp)
    {
        _criteria = criteria;
        _systemStartingTimestamp = systemStartingTimestamp;
        _currentAlternativesSuperset = p._currentAlternativesSuperset;
        _currentOS = p._currentOS;
        _osChanged = p._osChanged;
        _currentIteration = p._currentIteration;
        _currentTimestamp = LocalDateTime.now();
        _R = p._R;
        _reasons = p._reasons == null ? null : p._reasons.clone();
        INormalizationBuilder nb = p._normalizationBuilder;
        if (nb == null) nb = new StandardLinearBuilder();
        if (_currentOS != null) _normalizationsCurrentOS = nb.getNormalizations(p._currentOS);
        else _normalizationsCurrentOS = null;
    }

    /**
     * Getter for the criteria
     *
     * @return criteria
     */
    public Criteria getCriteria()
    {
        return _criteria;
    }

    /**
     * Getter for the field that is supposed to contain all the alternatives maintained at some specific processing
     * stage.
     *
     * @return all the alternatives maintained at some specific processing stage
     */
    public AbstractAlternatives<?> getCurrentAlternativesSuperset()
    {
        return _currentAlternativesSuperset;
    }

    /**
     * Getter for the field that is supposed to store the data on the current objective space.
     *
     * @return data on the current objective space
     */
    public ObjectiveSpace getCurrentOS()
    {
        return _currentOS;
    }

    /**
     * Can be called to check whether the OS changed from one iteration to another (if so, the previous OS and the
     * current
     * one should differ).
     *
     * @return the flag indicating whether the OS changed from one iteration to another
     */
    public boolean isOsChanged()
    {
        return _osChanged;
    }

    /**
     * Getter for the current iteration (e.g., generation).
     *
     * @return current iteration
     */
    public int getCurrentIteration()
    {
        return _currentIteration;
    }

    /**
     * Field representing current timestamp (date and time; stored automatically when creating the object instance).
     *
     * @return current timestamp
     */
    public LocalDateTime getCurrentDateTime()
    {
        return _currentTimestamp;
    }

    /**
     * Getter for the normalizations object that can be used to rescale (normalize) the current objective space.
     *
     * @return the normalizations
     */
    public INormalization[] getNormalizationsCurrentOS()
    {
        return _normalizationsCurrentOS;
    }

    /**
     * Getter for the timestamp (date and time) representing when the system started processing.
     *
     * @return timestamp (date and time)
     */
    public LocalDateTime getSystemStartingTimestamp()
    {
        return _systemStartingTimestamp;
    }

    /**
     * Getter for the random number generator.
     *
     * @return random number generator
     */
    public IRandom getR()
    {
        return _R;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    public String getStringRepresentation()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Current iteration = ").append(_currentIteration).append(System.lineSeparator());
        if (_currentTimestamp != null)
            sb.append("Current date and time = ").append(StringUtils.getTimestamp(_currentTimestamp)).append(System.lineSeparator());
        if (_systemStartingTimestamp != null)
            sb.append("System starting date and time = ").append(StringUtils.getTimestamp(_systemStartingTimestamp)).append(System.lineSeparator());
        if (_currentAlternativesSuperset != null)
            sb.append("No. alternatives = ").append(_currentAlternativesSuperset.size()).append(System.lineSeparator());
        if (_criteria != null)
            sb.append("Criteria = ").append(_criteria).append(System.lineSeparator());
        if (_currentOS != null)
        {
            sb.append("Objective space data: ").append(System.lineSeparator());
            sb.append(_currentOS).append(System.lineSeparator());
        }
        sb.append("Objective space changed = ").append(_osChanged).append(System.lineSeparator());
        if (_normalizationsCurrentOS != null)
        {
            sb.append("Normalization objects:").append(System.lineSeparator());
            for (INormalization normalizationsCurrentO : _normalizationsCurrentOS)
            {
                sb.append(normalizationsCurrentO.toString());
                sb.append(System.lineSeparator());
            }
        }
        if (_R != null) sb.append("Random number generator provided = true");
        else sb.append("Random number generator provided = false");
        if (_reasons != null)
        {
            sb.append("Reasons for running = ");
            if (_reasons.isEmpty()) sb.append("None").append(System.lineSeparator());
            else
            {
                int cnt = 0;
                int l = _reasons.size();
                for (Reason r : _reasons)
                {
                    sb.append(r);
                    if (cnt < l - 1) sb.append(", ");
                    else sb.append(System.lineSeparator());
                }
            }

        }

        return sb.toString();
    }

    /**
     * The method for checking if the input was one of the reasons for running the system.
     *
     * @param reason questioned reason
     * @return true, if the input was one of the reasons; false otherwise
     */
    public boolean isReasonForRunning(Reason reason)
    {
        if (_reasons == null) return false;
        return _reasons.contains(reason);
    }

    /**
     * The method for constructing and returning a clone of the reasons for running the system.
     *
     * @return cloned  reasons for running the system (null, if the decision-making context's provided reasons are null)
     */
    public EnumSet<Reason> getClonedReasons()
    {
        if (_reasons == null) return null;
        else return _reasons.clone();
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return getStringRepresentation();
    }

}
