package interaction.reference;

import exeption.ReferenceSetsConstructorException;
import utils.StringUtils;

import java.util.*;

/**
 * Container-like class that wraps all reference sets constructed via {@link ReferenceSetsConstructor}.
 *
 * @author MTomczyk
 */
public class ReferenceSets
{
    /**
     * Represents the total number of constructed reference sets.
     */
    private final int _noSets;

    /**
     * Stores the unique reference sets' sizes.
     */
    private final int[] _uniqueSizes;

    /**
     * Map connecting a reference size with a list of associated reference sets.
     */
    private final HashMap<Integer, LinkedList<ReferenceSet>> _referenceSets;

    /**
     * Auxiliary method that creates a joint set from the two inputs.
     *
     * @param A the first input
     * @param B the second input
     * @return joint set
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public static ReferenceSets createJointSet(ReferenceSets A, ReferenceSets B) throws ReferenceSetsConstructorException
    {
        int totalNo = 0;
        if (A != null) totalNo += A.getNoSets();
        if (B != null) totalNo += B.getNoSets();
        if (totalNo == 0) return new ReferenceSets(0, new int[0], new HashMap<>());

        Set<Integer> uniqueSizes = new HashSet<>();
        if (A != null) for (Integer s : A.getUniqueSizes()) uniqueSizes.add(s);
        if (B != null) for (Integer s : B.getUniqueSizes()) uniqueSizes.add(s);
        ArrayList<Integer> sorted = new ArrayList<>(uniqueSizes.size());
        sorted.addAll(uniqueSizes);
        sorted.sort(Integer::compare);
        int[] uSizes = new int[sorted.size()];
        for (int i = 0; i < sorted.size(); i++) uSizes[i] = sorted.get(i);

        HashMap<Integer, LinkedList<ReferenceSet>> map = new HashMap<>();
        for (Integer s : uSizes)
        {
            LinkedList<ReferenceSet> sets = new LinkedList<>();
            map.put(s, sets);
            if ((A != null) && (A.getReferenceSets().containsKey(s))) sets.addAll(A.getReferenceSets().get(s));
            if ((B != null) && (B.getReferenceSets().containsKey(s))) sets.addAll(B.getReferenceSets().get(s));
        }

        return new ReferenceSets(totalNo, uSizes, map);
    }

    /**
     * Parameterized constructor,
     *
     * @param noSets        represents the total number of constructed reference sets
     * @param uniqueSizes   stores the unique reference sets' sizes
     * @param referenceSets map connecting a reference size with a list of associated reference sets
     * @throws ReferenceSetsConstructorException the exception can be thrown 
     */
    public ReferenceSets(int noSets, int[] uniqueSizes, HashMap<Integer, LinkedList<ReferenceSet>> referenceSets)
            throws ReferenceSetsConstructorException
    {
        if (uniqueSizes.length != referenceSets.size())
            throw new ReferenceSetsConstructorException("The number of unique sizes (" + uniqueSizes.length +
                    ") differs from the provided map size (" + referenceSets.size() + ")", this.getClass());

        for (int s : uniqueSizes)
            if (!referenceSets.containsKey(s))
                throw new ReferenceSetsConstructorException("The map does not contain a key = " + s, this.getClass());

        for (int s : uniqueSizes)
            if (referenceSets.get(s) == null)
                throw new ReferenceSetsConstructorException("The value (reference sets) for key = " + s + " is null", this.getClass());

        for (int s : uniqueSizes)
            if (referenceSets.get(s).isEmpty())
                throw new ReferenceSetsConstructorException("The reference sets for key = " + s + " is an empty array", this.getClass());

        for (int s : uniqueSizes)
            for (ReferenceSet rs : referenceSets.get(s))
                if (rs == null)
                    throw new ReferenceSetsConstructorException("One of the reference sets for key = " + s + " is null", this.getClass());

        int totalNo = 0;
        for (int s : uniqueSizes)
        {
            totalNo += referenceSets.get(s).size();
            for (ReferenceSet rs : referenceSets.get(s))
                if (rs.getSize() != s)
                    throw new ReferenceSetsConstructorException("The reference set size = " + rs.getSize() + " and it differs from its connected key = " + s, this.getClass());
        }

        if (totalNo != noSets)
            throw new ReferenceSetsConstructorException("The reported total number of reference sets stored (" + noSets + ") differs from the actual state of " + totalNo, this.getClass());

        _noSets = noSets;
        _uniqueSizes = uniqueSizes;
        _referenceSets = referenceSets;
    }


    /**
     * Getter for the total number of constructed reference sets.
     *
     * @return the total number of constructed reference sets
     */
    public int getNoSets()
    {
        return _noSets;
    }

    /**
     * Getter for the unique reference sets' sizes.
     *
     * @return unique reference sets' sizes
     */
    public int[] getUniqueSizes()
    {
        return _uniqueSizes;
    }

    /**
     * Getter for the map that connects a reference size with a list of associated reference sets.
     *
     * @return map that connects a reference size with a list of associated reference sets
     */
    public HashMap<Integer, LinkedList<ReferenceSet>> getReferenceSets()
    {
        return _referenceSets;
    }

    /**
     * Creates and returns a string representation of all reference sets. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @return string representation
     */
    public String[] getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Creates and returns a string representation of all reference sets. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    public String[] getStringRepresentation(int indent)
    {
        String ind = StringUtils.getIndent(indent);
        LinkedList<String> lines = new LinkedList<>();

        lines.add(ind + "Constructed reference sets = " + _noSets);
        lines.add(ind + "Total number of unique sizes = " + _uniqueSizes.length);

        for (int uniqueSize : _uniqueSizes)
        {
            LinkedList<ReferenceSet> RSs = _referenceSets.get(uniqueSize);
            if (RSs.size() != 1)
                lines.add(ind + "There are " + RSs.size() + " reference sets with size = " + uniqueSize);
            else lines.add(ind + "There is " + RSs.size() + " reference set with size = " + uniqueSize);
            for (int j = 0; j < RSs.size(); j++)
            {
                lines.add(ind + "Printing reference set no. " + j);
                String S = RSs.get(j).getStringRepresentation(indent + 2);
                lines.add(S);
            }
        }

        return StringUtils.getArrayFromList(lines);
    }

}
