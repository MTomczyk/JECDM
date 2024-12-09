package relation;

import alternative.Alternative;

/**
 * Interface for classes implementing the concept of a unary relation.
 *
 * @author MTomczyk
 */
public interface IUnaryRelation
{
    /**
     * Checks if a relation R for an alternative A holds.
     *
     * @param A the alternative
     * @return true if ARB, false otherwise
     */
    boolean isHolding(Alternative A);

    /**
     * Returns relation type.
     *
     * @return relation type (id)
     */
    Relations getType();
}
