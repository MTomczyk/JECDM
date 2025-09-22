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
     * The expected number of offspring to be constructed from the selected parents.
     */
    public final int _noOffspringToConstruct;

    /**
     * Parameterized constructor that accepts the selected parents (array) as input. Sets the expected number of
     * offspring to be constructed from the selected parents to 1.
     *
     * @param parents parents (specimens) selected for mating
     */
    public Parents(ArrayList<Specimen> parents)
    {
        this(parents, 1);
    }


    /**
     * Parameterized constructor that accepts the selected parents (array) as input.
     *
     * @param parents     parents (specimens) selected for mating
     * @param noOffspring the expected number of offspring to be constructed from the selected parents
     */
    public Parents(ArrayList<Specimen> parents, int noOffspring)
    {
        _parents = parents;
        _noOffspringToConstruct = noOffspring;
    }

    /**
     * Parameterized constructor that accepts the selected parents (two specimens) as input. Sets the expected number of
     * offspring to be constructed from the selected parents to 1.
     *
     * @param p1 the first parent
     * @param p2 the second parent
     */
    public Parents(Specimen p1, Specimen p2)
    {
        this(p1, p2, 1);
    }

    /**
     * Parameterized constructor that accepts the selected parents (two specimens) as input.
     *
     * @param p1          the first parent
     * @param p2          the second parent
     * @param noOffspring the expected number of offspring to be constructed from the selected parents
     */
    public Parents(Specimen p1, Specimen p2, int noOffspring)
    {
        _parents = new ArrayList<>(2);
        _parents.add(p1);
        _parents.add(p2);
        _noOffspringToConstruct = noOffspring;
    }
}
