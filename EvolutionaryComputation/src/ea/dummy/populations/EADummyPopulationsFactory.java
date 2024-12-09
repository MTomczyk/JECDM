package ea.dummy.populations;

import ea.EA;
import ea.factory.IEAFactory;

/**
 * Factory that returns sequentially fixed instances of {@link ea.dummy.populations.EADummyPopulations}.
 *
 * @author MTomczyk
 */


public class EADummyPopulationsFactory implements IEAFactory
{
    /**
     * Fixed instances of {@link ea.dummy.populations.EADummyPopulations}.
     */
    private final EADummyPopulations[] _dummyEAs;

    /**
     * Used to decide which EA to return (counter is incremented after every instance of an EA is returned).
     */
    private int _counter = 0;

    /**
     * Parameterized constructor.
     *
     * @param dummyEAs fixed instances of {@link ea.dummy.populations.EADummyPopulations}
     */
    public EADummyPopulationsFactory(EADummyPopulations[] dummyEAs)
    {
        _dummyEAs = dummyEAs;
    }


    /**
     * Returns the next instance of a dummy EA with fixed populations.
     *
     * @return instance of a dummy EA with fixed populations (returns null in the case when an excessive number of calls of this method was performed)
     */
    @Override
    public EA getInstance()
    {
        if (_dummyEAs == null) return null;
        if (_counter >= _dummyEAs.length) return null;
        return _dummyEAs[_counter++];
    }
}
