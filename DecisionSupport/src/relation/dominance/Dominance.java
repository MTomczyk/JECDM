package relation.dominance;

import alternative.Alternative;
import criterion.Criteria;
import relation.IBinaryRelation;
import relation.Relations;
import utils.Constants;

/**
 * Class representing the dominance relation.
 *
 * @author MTomczyk
 */

public class Dominance implements IBinaryRelation
{
    /**
     * Considered criteria.
     */
    private final Criteria _criteria;

    /**
     * Epsilon-tolerance for comparing doubles (one's evaluations must be better than other's at least by epsilon to be considered "better").
     */
    private final double _epsilon;

    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     */
    public Dominance(Criteria criteria)
    {
        this(criteria, Constants.EPSILON);
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param epsilon  epsilon-tolerance for comparing doubles (A's evaluations must be better than B's at least by
     *                 epsilon to be considered "better")
     */
    public Dominance(Criteria criteria, double epsilon)
    {
        _criteria = criteria;
        _epsilon = epsilon;
    }

    /**
     * Checks if the binary relation holds.
     *
     * @param A the first alternative
     * @param B the second alternative
     * @return True -> A dominates B, false otherwise
     */
    @Override
    public boolean isHolding(Alternative A, Alternative B)
    {
        return DominanceUtils.isDominating(A, B, _criteria, _epsilon);
    }

    /**
     * Returns the relation type.
     *
     * @return type
     */
    @Override
    public Relations getType()
    {
        return Relations.DOMINANCE;
    }
}
