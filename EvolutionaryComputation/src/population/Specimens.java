package population;

import alternative.AbstractAlternatives;

import java.util.ArrayList;

/**
 * Wrapper for arrays of specimens. Used for passing populations into different methods from the DecisionSupport module.
 *
 * @author MTomczyk
 */
public class Specimens extends AbstractAlternatives<Specimen>
{
    /**
     * Parameterized constructor. Accepts specimen arrays as an input.
     *
     * @param specimens array of specimens.
     */
    public Specimens(ArrayList<Specimen> specimens)
    {
        super(specimens);
    }
}
