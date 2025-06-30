package interaction.reference;

import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.Status;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.IValidator;
import interaction.reference.validator.RequiredSpread;
import system.dm.DM;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class responsible for constructing reference sets (alternatives to be compared by the decision maker).
 *
 * @author MTomczyk
 */
public class ReferenceSetsConstructor
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Objects responsible for constructing reference sets (the same for all decision makers).
         */
        public LinkedList<IReferenceSetConstructor> _commonConstructors;


        /**
         * Objects responsible for constructing reference sets (each list is unique to each decision maker;
         * accessed via decision maker's string representation).
         */
        public HashMap<String, LinkedList<IReferenceSetConstructor>> _dmConstructors;


        /**
         * Creates default object instance (default parameterization) that uses a random pairs constructor
         * {@link RandomPairs} with {@link RequiredSpread} validator (threshold of 0.0001 is used). The constructor
         * is common to all decision maker's.
         *
         * @return default object instance
         */
        public static Params getDefault()
        {
            return getDefault(0.0001d);
        }


        /**
         * Creates default object instance (default parameterization) that uses a random pairs constructor
         * {@link RandomPairs}
         * with {@link RequiredSpread} validator. The constructor is common to all decision maker's.
         *
         * @param thComparison the threshold for the validator used by random pair generator
         * @return default object instance
         */
        public static Params getDefault(double thComparison)
        {
            Params p = new Params();
            p._commonConstructors = new LinkedList<>();
            p._commonConstructors.add(new RandomPairs(new IValidator[]{new RequiredSpread(thComparison)}));
            p._dmConstructors = null;
            return p;
        }
    }

    /**
     * Objects responsible for constructing reference sets.
     */
    private final LinkedList<IReferenceSetConstructor> _commonConstructors;

    /**
     * Objects responsible for constructing reference sets (each list is unique to each decision maker;
     * accessed via decision maker's string representation). The constructor is common to all decision maker's.
     */
    private final HashMap<String, LinkedList<IReferenceSetConstructor>> _dmConstructors;


    /**
     * Creates default object instance that uses a random pairs constructor {@link RandomPairs}
     * with {@link RequiredSpread} validator (threshold of 0.0001). The constructor is common to all decision maker's.
     *
     * @return default object instance (null, if the creation process failed)
     */
    public static ReferenceSetsConstructor getDefault()
    {
        return getDefault(0.0001d);
    }

    /**
     * Creates default object instance that uses a random pairs constructor {@link RandomPairs} with
     * {@link RequiredSpread} validator. The constructor is common to all decision maker's.
     *
     * @param thComparison the threshold for the validator used by random pair generator
     * @return default object instance
     */
    public static ReferenceSetsConstructor getDefault(double thComparison)
    {
        return new ReferenceSetsConstructor(Params.getDefault(thComparison));
    }

    /**
     * Parameterized constructor.
     *
     * @param commonReferenceSetConstructor reference set constructor (common to all decision-makers)
     */
    public ReferenceSetsConstructor(IReferenceSetConstructor commonReferenceSetConstructor)
    {
        _commonConstructors = new LinkedList<>();
        _commonConstructors.add(commonReferenceSetConstructor);
        _dmConstructors = null;
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ReferenceSetsConstructor(Params p)
    {
        _commonConstructors = p._commonConstructors;
        _dmConstructors = p._dmConstructors;
    }

    /**
     * Auxiliary method that can be called to validate some essential fields.
     *
     * @param DMs decision makers (identifiers)
     * @throws ReferenceSetsConstructorException the exception will be thrown if the validation fails
     */
    public void validate(DM[] DMs) throws ReferenceSetsConstructorException
    {
        boolean empty1 = _commonConstructors == null || _commonConstructors.isEmpty();
        boolean empty2 = _dmConstructors == null || _dmConstructors.isEmpty();

        if (empty1 && empty2)
            throw new ReferenceSetsConstructorException("The reference sets constructors are not provided " +
                    "(neither the common nor unique for each decision maker)", this.getClass());

        if (_commonConstructors != null)
        {
            for (IReferenceSetConstructor c : _commonConstructors)
                if (c == null)
                    throw new ReferenceSetsConstructorException("One of the provided reference sets constructors is null", this.getClass());
        }

        if (_dmConstructors != null)
        {
            Set<String> dms = new HashSet<>();
            for (DM dm : DMs) dms.add(dm.getName());
            Set<String> ks = _dmConstructors.keySet();

            for (String name : ks)
                if (!dms.contains(name))
                    throw new ReferenceSetsConstructorException("The decision maker's identifier for the name = " + name + " is not provided", this.getClass());

            for (String k : ks)
            {
                if (_dmConstructors.get(k) == null)
                    throw new ReferenceSetsConstructorException("The reference sets constructors lists for the decision maker = " + k + " are not provided (the array is null)",
                            this.getClass());
                if (_dmConstructors.get(k).isEmpty())
                    throw new ReferenceSetsConstructorException("The reference sets constructors lists for the decision maker = " + k + " are not provided (the array is empty)",
                            this.getClass());
                for (IReferenceSetConstructor c : _dmConstructors.get(k))
                    if (c == null)
                        throw new ReferenceSetsConstructorException("One of the provided reference sets constructors for the decision maker =  " + k + " is null", this.getClass());
            }
        }
    }

    /**
     * The main method for constructing reference sets (wrapped via {@link Result#_referenceSetsContainer}).
     *
     * @param dmContext      current decision-making context
     * @param DMs            decision makers (identifiers)
     * @param refiningResult result of the refining process
     * @return constructed reference sets (wrapped via {@link Result#_referenceSetsContainer}).
     * @throws ReferenceSetsConstructorException the exception can be thrown
     */
    public Result constructReferenceSets(DMContext dmContext, DM[] DMs,
                                         interaction.refine.Result refiningResult) throws ReferenceSetsConstructorException
    {
        validate(dmContext, refiningResult);
        Result result = new Result(dmContext, refiningResult);

        long pT = System.nanoTime();

        ReferenceSets commonSets = null;
        HashMap<DM, ReferenceSets> dmSets = null;
        int noSets = 0;

        if ((_commonConstructors != null) && (!_commonConstructors.isEmpty()))
        {
            commonSets = constructReferenceSets(result, dmContext, refiningResult, _commonConstructors);
            if (commonSets == null) // TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES
            {
                result._processingTime = (double) (System.nanoTime() - pT) / 1000000.0d;
                result._referenceSetsContainer = null;
                return result;
            }
            noSets += commonSets.getNoSets();
        }

        if (_dmConstructors != null)
        {
            if (DMs == null) throw new ReferenceSetsConstructorException("The per-decision-makers reference sets " +
                    "are expected to be constructed but the decision makers' identifiers are not provided (the array is null)", this.getClass());

            dmSets = new HashMap<>(DMs.length);
            for (DM dm : DMs)
            {
                LinkedList<IReferenceSetConstructor> constructors = _dmConstructors.get(dm.getName());
                if (constructors == null) continue;
                ReferenceSets rs = constructReferenceSets(result, dmContext, refiningResult, constructors);

                if (rs == null) // TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES
                {
                    result._processingTime = (double) (System.nanoTime() - pT) / 1000000.0d;
                    result._referenceSetsContainer = null;
                    return result;
                }

                noSets += rs.getNoSets();
                dmSets.put(dm, rs);
            }
        }


        if (noSets == 0) result._status = Status.PROCESS_ENDED_BUT_NO_REFERENCE_SETS_WERE_CONSTRUCTED;
        else result._status = Status.PROCESS_ENDED_SUCCESSFULLY;

        result._referenceSetsContainer = new ReferenceSetsContainer(noSets, commonSets, dmSets);
        result._processingTime = (double) (System.nanoTime() - pT) / 1000000.0d;
        return result;
    }


    /**
     * Auxiliary method for creating reference sets given the input reference sets constructors.
     *
     * @param result         construction result object that can be filled by this method
     * @param dmContext      current decision-making context
     * @param refiningResult result of the refining process
     * @param constructors   reference sets constructors.
     * @return reference sets
     * @throws ReferenceSetsConstructorException the exception can be thrown
     */
    private ReferenceSets constructReferenceSets(Result result,
                                                 DMContext dmContext,
                                                 interaction.refine.Result refiningResult,
                                                 LinkedList<IReferenceSetConstructor> constructors) throws ReferenceSetsConstructorException
    {

        HashMap<Integer, LinkedList<ReferenceSet>> constructedSets = new HashMap<>(constructors.size());
        HashMap<Integer, LinkedList<IReferenceSetConstructor>> partitionedConstructors = new HashMap<>(constructors.size());
        Set<Integer> setsSizes = new HashSet<>(constructors.size());

        int minRequired = Integer.MAX_VALUE;
        for (IReferenceSetConstructor c : constructors)
        {
            int size = c.getExpectedSize(refiningResult._refinedAlternatives);
            if (!setsSizes.contains(size))
            {
                constructedSets.put(size, new LinkedList<>());
                LinkedList<IReferenceSetConstructor> partition = new LinkedList<>();
                partition.add(c);
                partitionedConstructors.put(size, partition);
            }
            else partitionedConstructors.get(size).add(c);

            setsSizes.add(size);
            if (size < minRequired) minRequired = size;
        }

        if (refiningResult._refinedAlternatives.size() < minRequired)
        {
            result._status = Status.TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES;
            result._terminatedDueToNotEnoughAlternatives = true;
            result._terminatedDueToNotEnoughAlternativesMessage = "Not enough alternatives (required = " +
                    minRequired + " but " + refiningResult._refinedAlternatives.size() + " remained after reduction)";
            return null;
        }

        int noSets = 0;
        int[] uniqueSizes = new int[setsSizes.size()];

        int idx = 0;
        for (Integer size : setsSizes)
        {
            uniqueSizes[idx++] = size;
            LinkedList<ReferenceSet> cs = constructedSets.get(size);
            for (IReferenceSetConstructor c : partitionedConstructors.get(size))
            {
                LinkedList<ReferenceSet> sets = c.constructReferenceSets(dmContext, refiningResult._refinedAlternatives);
                if (sets != null)
                {
                    for (ReferenceSet rs : sets)
                        if (rs == null)
                            throw new ReferenceSetsConstructorException("The constructed list of reference sets contains null elements", this.getClass());
                    noSets += sets.size();
                    cs.addAll(sets);
                }
            }
        }

        // post filtering
        LinkedList<Integer> passedUniqueSizes = new LinkedList<>();
        for (Integer size : setsSizes)
            if (constructedSets.get(size).isEmpty())
                constructedSets.remove(size);
            else passedUniqueSizes.add(size);

        uniqueSizes = new int[passedUniqueSizes.size()];
        idx = 0;
        for (Integer s : passedUniqueSizes) uniqueSizes[idx++] = s;

        return new ReferenceSets(noSets, uniqueSizes, constructedSets);
    }

    /**
     * Auxiliary method for performing basic validation.
     *
     * @param dmContext       current decision-making context
     * @param refiningResults results of the refining process
     * @throws ReferenceSetsConstructorException exception can be thrown
     */
    private void validate(DMContext dmContext, interaction.refine.Result refiningResults) throws ReferenceSetsConstructorException
    {
        if (dmContext == null)
            throw new ReferenceSetsConstructorException("Decision-making context is not provided", this.getClass());
        if (dmContext.getCriteria() == null)
            throw new ReferenceSetsConstructorException("Criteria are not supplied", this.getClass());
        if (refiningResults == null)
            throw new ReferenceSetsConstructorException("Refining results are not provided", this.getClass());
        if (refiningResults._refinedAlternatives == null)
            throw new ReferenceSetsConstructorException("Refined alternatives are not provided (the array is null)", this.getClass());
    }


}
