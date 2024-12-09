package relation;

import alternative.Alternative;

/**
 * Interface for classes implementing the concept of a binary relation.
 *
 * @author MTomczyk
 */
public interface IBinaryRelation
{
    /**
     * Checks if a relation R for a pair (A, B) holds (ARB).
     *
     * @param A the first alternative
     * @param B the second alternative
     * @return true if ARB, false otherwise
     */
    boolean isHolding(Alternative A, Alternative B);

    /**
     * Returns relation type.
     *
     * @return relation type (id)
     */
    Relations getType();
}
