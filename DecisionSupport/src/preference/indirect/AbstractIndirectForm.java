package preference.indirect;

import alternative.Alternative;
import preference.AbstractPreferenceInformation;
import preference.IPreferenceInformation;
import relation.Relations;

/**
 * Abstract implementation of {@link IPreferenceInformation}, dedicated to indirect preference judgments.
 *
 * @author MTomczyk
 */
public abstract class AbstractIndirectForm extends AbstractPreferenceInformation implements IPreferenceInformation
{
    /**
     * Judged alternatives.
     */
    protected Alternative[] _alternatives;

    /**
     * Relation(s) between the alternatives (implementation-dependent).
     */
    protected final Relations[] _relations;

    /**
     * Parameterized constructor.
     *
     * @param name     name of the preference information (string representation)
     * @param relation relation(s) between the alternatives (implementation-dependent).
     */
    protected AbstractIndirectForm(String name, Relations relation)
    {
        super(name);
        _relations = new Relations[]{relation};
    }

    @Override
    public boolean isIndirect()
    {
        return true;
    }
}
