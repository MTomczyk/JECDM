package os;

import criterion.Criteria;
import ea.EA;
import ea.EATimestamp;
import exception.PhaseException;
import population.*;
import problem.moo.AbstractMOOProblemBundle;
import space.os.ObjectiveSpace;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class responsible for keeping and updating info on the objective space of the problem being solved.
 *
 * @author MTomczyk
 */
public class ObjectiveSpaceManager
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * The evolutionary algorithm
         */
        public EA _ea = null;

        /**
         * If true, the update procedure will terminate immediately after being called. This flag can be used to surpass
         * updating data (e.g. when the OS is known in advance and is assumed to be fixed).
         */
        public boolean _doNotUpdateOS = false;

        /**
         * If true, objective space is updated based not only on the current population but the historical data as well (compare with the incumbent).
         */
        public boolean _updateUtopiaUsingIncumbent = false;

        /**
         * If true, objective space is updated based not only on the current population but the historical data as well (compare with the incumbent).
         */
        public boolean _updateNadirUsingIncumbent = false;

        /**
         * Considered criteria.
         */
        public Criteria _criteria;

        /**
         * Initial objective space object (can be null).
         */
        public final ObjectiveSpace _os;

        /**
         * Array of "objective space changed" listeners (can be null; will be set by a dedicated method in {@link ea.AbstractEABundle}).
         */
        public IOSChangeListener[] _listeners = null;

        /**
         * When true, the manager will store the timestamps that indicate when the knowledge on the objective space was updated.
         */
        public boolean _storeTimestamps = true;

        /**
         * Provide specimens for analysis. If null, the {@link PopulationGetter} and {@link OffspringGetter} will be used.
         */
        public ISpecimenGetter[] _specimenGetters = null;

        /**
         * Default constructor.
         */
        public Params()
        {
            this(null);
        }

        /**
         * Parameterized constructor.
         *
         * @param os wrapped objective space
         */
        public Params(ObjectiveSpace os)
        {
            _os = os;
        }
    }

    /**
     * Auxiliary params getter. The params is parameterized based on the provided input data.
     *
     * @param problemBundle              problem bundle that provides essential data on the objective space (e.g., Pareto front bounds)
     * @param supplyInitialOSData        if true, the initially constructed objective space object will be supplied with data from the problem bundle
     * @param dynamicObjectiveRanges     if true, the OS manager to be instantiated is supposed not to terminate the update method (see {@link ObjectiveSpaceManager#_doNotUpdateOS})
     * @param updateUtopiaUsingIncumbent if true, utopia constructed during the update step will be finally compared with its incumbent counterpart
     * @param updateNadirUsingIncumbent  if true, utopia constructed during the update step will be finally compared with its incumbent counterpart
     * @param criteria                   considered criteria
     * @return params container
     */
    public static ObjectiveSpaceManager.Params getInstantiatedParams(AbstractMOOProblemBundle problemBundle,
                                                                     boolean supplyInitialOSData,
                                                                     boolean dynamicObjectiveRanges,
                                                                     boolean updateUtopiaUsingIncumbent,
                                                                     boolean updateNadirUsingIncumbent,
                                                                     Criteria criteria)
    {
        ObjectiveSpace os;
        if (supplyInitialOSData) os = new ObjectiveSpace(problemBundle._utopia.clone(),
                problemBundle._nadir.clone(), problemBundle._paretoFrontBounds, problemBundle._optimizationDirections);
        else os = new ObjectiveSpace(null, null, null, problemBundle._optimizationDirections);

        ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params(os);
        pOS._doNotUpdateOS = !dynamicObjectiveRanges;
        pOS._updateUtopiaUsingIncumbent = updateUtopiaUsingIncumbent;
        pOS._updateNadirUsingIncumbent = updateNadirUsingIncumbent;
        pOS._criteria = criteria;
        pOS._storeTimestamps = false;
        return pOS;
    }


    /**
     * The evolutionary algorithm
     */
    private EA _ea;

    /**
     * If true, the update procedure will terminate immediately after being called. This flag can be used to surpass
     * updating data (e.g. when the OS is known in advance and is assumed to be fixed).
     */
    private final boolean _doNotUpdateOS;

    /**
     * If true, objective space is updated based not only on the current population but the historical data as well (compare with the incumbent).
     */
    private final boolean _updateUtopiaUsingIncumbent;

    /**
     * If true, objective space is updated based not only on the current population but the historical data as well (compare with the incumbent).
     */
    private final boolean _updateNadirUsingIncumbent;

    /**
     * Considered criteria.
     */
    private final Criteria _criteria;

    /**
     * Objective space object.
     */
    public ObjectiveSpace _os;

    /**
     * Auxiliary flag indicating that the data on the objective space has been changed.
     */
    private boolean _changed;

    /**
     * Array of "objective space changed" listeners (can be null).
     */
    private IOSChangeListener[] _listeners;

    /**
     * When true, the manager will store the timestamps that indicate when the knowledge on the objective space was updated.
     */
    private final boolean _storeTimestamps;

    /**
     * This array captures timestamps (generation/steady-state repeat) indicating when the knowledge on the objective space was updated.
     */
    private final LinkedList<EATimestamp> _notificationTimes;

    /**
     * Provide specimens for analysis. If null, the {@link PopulationGetter} and {@link OffspringGetter} will be used.
     */
    private ISpecimenGetter[] _specimenGetters;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ObjectiveSpaceManager(Params p)
    {
        _doNotUpdateOS = p._doNotUpdateOS;
        _ea = p._ea;
        _criteria = p._criteria;
        _listeners = p._listeners;
        _os = p._os;
        _updateUtopiaUsingIncumbent = p._updateUtopiaUsingIncumbent;
        _updateNadirUsingIncumbent = p._updateNadirUsingIncumbent;
        _changed = false;
        _storeTimestamps = p._storeTimestamps;
        _specimenGetters = p._specimenGetters;
        if (_specimenGetters == null)
        {
            _specimenGetters = new ISpecimenGetter[2];
            _specimenGetters[0] = new PopulationGetter();
            _specimenGetters[1] = new OffspringGetter();
        }

        if (p._storeTimestamps) _notificationTimes = new LinkedList<>();
        else _notificationTimes = null;
    }


    /**
     * Setter for the EA.
     *
     * @param ea ea
     */
    public void setEA(EA ea)
    {
        _ea = ea;
    }

    /**
     * Setter for the listeners for the triggered change in the objective space (can be null).
     *
     * @param listener array of listeners
     */
    public void setOSChangeListeners(IOSChangeListener[] listener)
    {
        _listeners = listener;
    }

    /**
     * Potentially updates utopia/nadir points based on the candidate point
     *
     * @param u utopia point
     * @param n nadir point
     * @param e candidate point
     */
    private void update(double[] u, double[] n, double[] e)
    {
        if (e == null) return;
        for (int i = 0; i < _criteria._no; i++)
        {
            if (_criteria._c[i].isGain())
            {
                if (Double.compare(e[i], u[i]) > 0) u[i] = e[i];
                if (Double.compare(e[i], n[i]) < 0) n[i] = e[i];
            }
            else
            {
                if (Double.compare(e[i], u[i]) < 0) u[i] = e[i];
                if (Double.compare(e[i], n[i]) > 0) n[i] = e[i];
            }
        }
    }

    /**
     * Updates the knowledge on the objective space.
     *
     * @param specimensContainer specimens container maintained by the EA (wraps e.g., current population, mating pool, offspring)
     * @param timestamp          current generation no. and steady-state repeat
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    public void update(SpecimensContainer specimensContainer, EATimestamp timestamp) throws PhaseException
    {
        update(specimensContainer, timestamp, true);
    }

    /**
     * Updates the knowledge on the objective space.
     *
     * @param specimensContainer specimens container maintained by the EA (e.g., current population, mating pool, offspring)
     * @param timestamp          current generation no. and steady-state repeat
     * @param notifyAboutChange  if true, the method accordingly updates the _changed flag and triggers the listener
     * @return the method returns true if a change in OS is reported; false otherwise
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    public boolean update(SpecimensContainer specimensContainer, EATimestamp timestamp, boolean notifyAboutChange) throws PhaseException
    {
        if (_doNotUpdateOS) return false;

        ObjectiveSpace os = ObjectiveSpace.getOSMaximallySpanned(_criteria.getCriteriaTypes()); // worst-case spanned os
        int cs = _criteria._no;

        if (specimensContainer != null)
        {
            for (ISpecimenGetter sg : _specimenGetters)
            {
                if (sg == null) continue;
                ArrayList<Specimen> specimens = sg.getSpecimens(specimensContainer);
                if (specimens == null) continue;
                for (Specimen s : specimens) update(os._utopia, os._nadir, s.getEvaluations());
            }
        }


        // if "update globally", the current utopia/nadir are to be compared with historical data
        if ((_updateUtopiaUsingIncumbent) && (_os != null))
            update(os._utopia, os._nadir, _os._utopia);
        if ((_updateNadirUsingIncumbent) && (_os != null))
            update(os._utopia, os._nadir, _os._nadir);


        // build ranges based on the computed utopia and nadir points
        for (int i = 0; i < cs; i++)
        {
            double l = os._utopia[i];
            double r = os._nadir[i];
            if (_criteria._c[i].isGain())
            {
                l = os._nadir[i];
                r = os._utopia[i];
            }

            if (Double.compare(l, r) > 0)
            {
                throw new PhaseException("The calculated range for the " + i + "-th objective is invalid (left = " + l + " ; right = " + r + ")", this.getClass());
            }
            os._ranges[i].setValues(l, r);
        }

        // If "store timestamps = true", save the timestamp in the case os has changed
        if ((_storeTimestamps) && ((_os == null) || (!os.isEqual(_os)))) //noinspection DataFlowIssue
            _notificationTimes.add(timestamp);

        // accordingly set "changed flag"
        if (notifyAboutChange)
        {
            if (_os == null) _changed = true;
            else _changed = !os.isEqual(_os);
        }
        else
            _changed = false;

        // update the stored os object
        ObjectiveSpace previous = _os;
        _os = os;

        // notify the listeners about the change
        if ((_changed) && (_listeners != null))
        {
            for (IOSChangeListener cl : _listeners) cl.action(_ea, os, previous);
        }

        return _changed;
    }
}
