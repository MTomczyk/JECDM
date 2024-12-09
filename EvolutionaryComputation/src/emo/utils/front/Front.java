package emo.utils.front;

import criterion.Criteria;
import population.Specimen;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Auxiliary class that provides various methods related to identification of non-dominated front.
 *
 * @author MTomczyk
 */
public class Front
{
    /**
     * Dominance relation used when comparing solutions.
     */
    private final Dominance _dominance;

    /**
     * Parameterized constructor.
     *
     * @param criteria criteria used to establish a dominance relation (used when comparing solutions)
     */
    public Front(Criteria criteria)
    {
        this(new Dominance(criteria));
    }

    /**
     * Parameterized constructor.
     *
     * @param dominance dominance relation used when comparing solutions
     */
    public Front(Dominance dominance)
    {
        _dominance = dominance;
    }

    /**
     * Auxiliary method for identifying non-dominated specimens from the solution set
     *
     * @param specimens the input solution set, e.g., population
     * @return non-dominated solution (subset of the input specimen set)
     */
    public LinkedList<Specimen> getNonDominatedSpecimens(ArrayList<Specimen> specimens)
    {
        LinkedList<Specimen> nds = new LinkedList<>();

        for (int s1 = 0; s1 < specimens.size(); s1++)
        {
            Specimen A = specimens.get(s1);
            boolean passed = true;
            for (int s2 = 0; s2 < specimens.size(); s2++)
            {
                if (s1 == s2) continue;
                if (_dominance.isHolding(specimens.get(s2).getAlternative(), A.getAlternative()))
                {
                    passed = false;
                    break;
                }
            }
            if (passed) nds.add(A);
        }
        return nds;
    }
}
