package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * Abstract class providing common methods.
 *
 * @author MTomczyk
 */
abstract sealed class AbstractConstruct implements IConstruct permits DoubleConstruct, IntConstruct, BoolConstruct, ChromosomeConstruct
{
    /**
     * Creates a specimen array of size set as imposed by {@link EA#getPopulationSize()}. The array is instantiated
     * with specimens whose evaluation vectors lengths are set as imposed by the number of criteria stored in {@link EA#getCriteria()}.
     *
     * @param ea evolutionary algorithm
     * @return specimen array
     * @throws PhaseException exception can be thrown and propagated higher
     */
    protected ArrayList<Specimen> createSpecimenArray(EA ea) throws PhaseException
    {
        ArrayList<Specimen> S = new ArrayList<>(ea.getPopulationSize());
        int criteria = ea.getCriteria()._no;
        for (int s = 0; s < ea.getPopulationSize(); s++) S.add(new Specimen(criteria));
        return S;
    }
}
