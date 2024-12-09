package relation.equalevaluations;

import alternative.Alternative;
import relation.IBinaryRelation;
import relation.Relations;
import space.Vector;
import utils.Constants;

/**
 * Implementation of {@link IBinaryRelation} for checking alternatives equality (evaluations).
 *
 * @author MTomczyk
 */
public class EqualEvaluations implements IBinaryRelation
{
    /**
     * Epsilon-tolerance for comparing doubles (one's evaluations must be better/worse than other's at least by epsilon to be considered "different").
     */
    private final double _epsilon;

    /**
     * Default constructor.
     */
    public EqualEvaluations()
    {
        this(Constants.EPSILON);
    }

    /**
     * Parameterized constructor.
     *
     * @param epsilon  epsilon-tolerance for comparing doubles (A's evaluations must be better than B's at least by
     *                 epsilon to be considered "better")
     */
    public EqualEvaluations(double epsilon)
    {
        _epsilon = epsilon;
    }

    /**
     * Checks if the binary relation holds (equality).
     *
     * @param A the first alternative
     * @param B the second alternative
     * @return True -> A is equal to B, false otherwise
     */
    @Override
    public boolean isHolding(Alternative A, Alternative B)
    {
        return Vector.areVectorsEqual(A.getPerformanceVector(), B.getPerformanceVector(), _epsilon);
    }

    /**
     * Returns the relation type.
     *
     * @return type
     */
    @Override
    public Relations getType()
    {
        return Relations.EQUALITY;
    }
}
