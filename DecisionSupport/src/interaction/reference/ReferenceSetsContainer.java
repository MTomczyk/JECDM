package interaction.reference;

import system.dm.DM;
import utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Container for constructed reference sets (sets of alternatives to be evaluated by the decision maker).
 * It mainly consists of two fields. First, {@link ReferenceSetsContainer#_commonReferenceSets} provides reference sets that
 * are common to all decision makers registered in the system. Second, {@link ReferenceSetsContainer#_dmReferenceSets}
 * provides reference sets that are unique to each decision maker (accessed via {@link DM}). Important note:
 * it is assumed that the reference sets are not duplicated in both fields, i.e., when a reference set is included
 * in the first field, it should not be added to the second.
 *
 * @author MTomczyk
 */


public class ReferenceSetsContainer
{
    /**
     * Reference sets that are common to all decision makers registered in the system.
     */
    private final ReferenceSets _commonReferenceSets;

    /**
     * Reference sets that are unique to each decision maker.
     */
    private final HashMap<DM, ReferenceSets> _dmReferenceSets;

    /**
     * Represents the total number of constructed reference sets.
     */
    private final int _noSets;

    /**
     * Parameterized constructor.
     *
     * @param noSets          represents the total number of constructed reference sets
     * @param referenceSets   reference sets that are common to all decision makers registered in the system
     * @param dmReferenceSets reference sets that are unique to each decision maker
     */
    public ReferenceSetsContainer(int noSets, ReferenceSets referenceSets, HashMap<DM, ReferenceSets> dmReferenceSets)
    {
        _commonReferenceSets = referenceSets;
        _dmReferenceSets = dmReferenceSets;
        _noSets = noSets;
    }

    /**
     * Auxiliary method constructing the string representation of the container. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @return string representation
     */
    public String[] getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Auxiliary method constructing the string representation of the container. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    public String[] getStringRepresentation(int indent)
    {
        String ind = StringUtils.getIndent(indent);
        LinkedList<String> lines = new LinkedList<>();

        if (_commonReferenceSets != null)
        {
            lines.add(ind + "Common reference sets:");
            String[] S = _commonReferenceSets.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        else
        {
            lines.add(ind + "Common reference sets:");
            lines.add(ind + "  None");
        }

        if (_dmReferenceSets != null)
        {
            lines.add(ind + "Reference sets unique to each decision maker:");
            Set<DM> keys = _dmReferenceSets.keySet();
            if (!keys.isEmpty())
            {
                for (DM dm : keys)
                {
                    lines.add(ind + "Reference sets unique to " + dm.getName() + ":");
                    String[] S = _dmReferenceSets.get(dm).getStringRepresentation(indent + 2);
                    lines.addAll(Arrays.asList(S));
                }
            }
            else
            {
                lines.add(ind + "Reference sets unique to each decision maker:");
                lines.add(ind + "  None");
            }
        }
        else
        {
            lines.add(ind + "Reference sets unique to each decision maker:");
            lines.add(ind + "  None");
        }

        return StringUtils.getArrayFromList(lines);
    }

    /**
     * Getter for the common reference sets (common to all decision makers).
     *
     * @return common reference sets
     */
    public ReferenceSets getCommonReferenceSets()
    {
        return _commonReferenceSets;
    }

    /**
     * Getter for the per-decision-maker reference sets (unique to all decision makers).
     *
     * @return per-decision-maker reference sets
     */
    public HashMap<DM, ReferenceSets> getDMReferenceSets()
    {
        return _dmReferenceSets;
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

}
