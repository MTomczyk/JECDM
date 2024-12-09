package population;

import java.util.ArrayList;

/**
 * Wrapper for selected parents for mating.
 *
 * @author MTomczyk
 */


public class Parents
{
    /**
     * Selected parents.
     */
    public final ArrayList<Specimen> _parents;

    /**
     * Parameterized constructor that accepts the selected parents (array) as input.
     * @param parents parents (specimens) selected for mating
     */
    public Parents(ArrayList<Specimen> parents)
    {
        _parents = parents;
    }

    /**
     * Parameterized constructor that accepts the selected parents (two specimens) as input.
     * @param p1 the first parent
     * @param p2 the second parent
     */
    public Parents(Specimen p1, Specimen p2)
    {
        _parents = new ArrayList<>(2);
        _parents.add(p1);
        _parents.add(p2);
    }
}
