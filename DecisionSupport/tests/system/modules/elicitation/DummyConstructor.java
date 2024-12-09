package system.modules.elicitation;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.constructor.IReferenceSetConstructor;

import java.util.LinkedList;

/**
 * Dummy implementation of {@link IReferenceSetConstructor} (for testing).
 *
 * @author MTomczyk
 */
class DummyConstructor implements IReferenceSetConstructor
{
    /**
     * First alternative ID;
     */
    private final int _fID;

    /**
     * Second alternative ID;
     */
    private final int _sID;

    /**
     * Parameterized constructor.
     * @param fID first alternative ID
     * @param sID second alternative ID
     */
    public DummyConstructor(int fID, int sID)
    {
        _fID = fID;
        _sID = sID;
    }


    /**
     * Implemented method should notify about the reference set size the main construction procedure will create.
     *
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return expected size of the reference set
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @SuppressWarnings("RedundantThrows")
    @Override
    public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        return 2;
    }

    /**
     * Main method for constructing a reference set (or sets). The method is supposed to return null if no valid
     * reference sets can be constructed (the elements of the output list cannot be null)
     *
     * @param dmContext                  current decision-making context
     * @param filteredAlternatives filtered alternatives (passed the termination and reduction steps)
     * @return constructed reference set (or sets); should return null if no such sets were generated (the elements cannot be null)
     * @throws ReferenceSetsConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
    {
        LinkedList<ReferenceSet> rs = new LinkedList<>();
        rs.add(new ReferenceSet(filteredAlternatives.get(_fID), filteredAlternatives.get(_sID)));
        return rs;
    }
}
