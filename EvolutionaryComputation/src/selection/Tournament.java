package selection;

import population.Parents;
import population.Specimen;
import random.IRandom;
import random.Shuffle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the {@link ISelect} interface for selecting parents.
 * Solutions are compared based on their (first) auxiliary scores.
 *
 * @author MTomczyk
 */


public class Tournament extends AbstractSelect implements ISelect
{
    /**
     * Supportive inner class for passing various parameters to the main class.
     */
    public static class Params extends AbstractSelect.Params
    {
        /**
         * Tournament size.
         */
        public int _size = 2;

        /**
         * Aux score preference direction: true = gain (to be maximized); false = cost (to be minimized)
         * Used for determining the tournament winner.
         */
        public boolean _preferenceDirection = true;

        /**
         * If true, the selection is with replacement; false -> without replacement
         * (without replacement -> the scope is selection of parents for one offspring, i.e., the parents cannot be repeated).
         */
        public boolean _withReplacement = true;

        /**
         * Auxiliary flag used when selecting without replacement. The flag should be selected based on the number of
         * parents/mating pool size ratio. If the ratio {@literal <} 50%, then the selection of parents is done by sampling
         * (the candidate can be neglected when it was already selected), and the flag is set to true. Otherwise,
         * the chances of a "positive hit" are small. If so, the flag should be set to false, and the selection is
         * supported by using an auxiliary permutation index.
         */
        public boolean _useSampling = true;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param size        tournament size
         * @param noOffspring no. offspring solutions to be generated
         */
        public Params(int size, int noOffspring)
        {
            this(size, noOffspring, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param size                tournament size
         * @param noOffspring         no. offspring solutions to be generated
         * @param preferenceDirection aux score preference direction: true = gain (to be maximized); false = cost (to be minimized); used for determining the tournament winner
         */
        public Params(int size, int noOffspring, boolean preferenceDirection)
        {
            super(noOffspring);
            _size = size;
            _preferenceDirection = preferenceDirection;
        }
    }

    /**
     * Tournament size.
     */
    private final int _size;

    /**
     * Aux score preference direction: true = gain (to be maximized); false = cost (to be minimized)
     * Used for determining the tournament winner.
     */
    private final boolean _preferenceDirection;

    /**
     * If true, the selection is with replacement; false -> without replacement (without replacement -> the scope is
     * a single tournament intending to select a single specimen-parent).
     * Also, if false, the tournament size is {@literal <}{@literal =} the mating pool size (assert).
     * Note that if false, it may negatively affect the execution time.
     */
    private final boolean _withReplacement;

    /**
     * Auxiliary flag used when selecting without replacement. The flag should be selected based on the number of
     * parents/mating pool size ratio. If the ratio {@literal <} than 50%, then the selection of parents is done by sampling
     * (the candidate can be neglected when it was already selected), and the flag is set to true. Otherwise,
     * the chances of a "positive hit" are small. If so, the flag should be set to false, and the selection is
     * supported by using an auxiliary permutation index.
     */
    protected final boolean _useSampling;


    /**
     * Auxiliary object for shuffling integers (used when sampling without replacement).
     */
    private final Shuffle<Integer> _sh;

    /**
     * Parameterized constructor.
     *
     * @param size        tournament size
     * @param noOffspring no. offspring solutions to be generated
     */
    public Tournament(int size, int noOffspring)
    {
        this(new Params(size, noOffspring));
    }

    /**
     * Parameterized constructor. Inner class instance (Params) is passed as a container for parameters.
     *
     * @param params params container
     */
    public Tournament(Params params)
    {
        super(params);
        _size = params._size;
        _preferenceDirection = params._preferenceDirection;
        _withReplacement = params._withReplacement;
        _useSampling = params._useSampling;
        if (!_withReplacement)
        {
            if (!_useSampling) _sh = new Shuffle<>();
            else _sh = null;
        }
        else _sh = null;
    }


    /**
     * Performs tournament selection.
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @return selected parents
     */
    @Override
    public ArrayList<Parents> selectParents(ArrayList<Specimen> matingPool, IRandom R)
    {
        ArrayList<Parents> P = new ArrayList<>(_noOffspring);

        // set auxiliary pointers
        int[] _pointers = null;

        if ((!_withReplacement) && (!_useSampling))
        {
            _pointers = new int[matingPool.size()];
            for (int p = 0; p < matingPool.size(); p++) _pointers[p] = p;
        }

        // Construct the desired number of parents (pairs)
        for (int o = 0; o < _noOffspring; o++)
        {
            ArrayList<Specimen> parents = new ArrayList<>(_noParentsPerOffspring);
            Set<Specimen> selected = null;

            if (!_withReplacement)
            {
                selected = new HashSet<>(); // hashed by their ids.
                if (!_useSampling) //noinspection DataFlowIssue
                    _sh.shuffle(_pointers, R);
            }

            for (int p = 0; p < _noParentsPerOffspring; p++)
            {
                Specimen winner;
                if (_withReplacement) winner = draw(matingPool, R);
                else
                {
                    if (_useSampling) winner = drawWithReplacementSampling(matingPool, R, selected);
                    else winner = drawWithReplacementPointers(matingPool, R, selected, _pointers);
                }


                for (int r = 1; r < _size; r++)
                {
                    Specimen counter = null;
                    if (_withReplacement)
                    {

                        try
                        {
                            counter = draw(matingPool, R);
                        } catch (RuntimeException e)
                        {
                            System.out.println();
                            System.out.println(matingPool.size());
                            System.out.println("HERE " + _useSampling + " " + _withReplacement);
                            System.out.println(e.getMessage());
                        }

                    }
                    else
                    {
                        if (_useSampling) counter = drawWithReplacementSampling(matingPool, R, selected);
                        else counter = drawWithReplacementPointers(matingPool, R, selected, _pointers);
                    }

                    //noinspection DataFlowIssue
                    if (((_preferenceDirection) && (Double.compare(counter.getAlternative().getAuxScore(), winner.getAlternative().getAuxScore()) > 0))
                            || ((!_preferenceDirection) && (Double.compare(counter.getAlternative().getAuxScore(), winner.getAlternative().getAuxScore()) < 0)))
                        winner = counter; // replacement
                }


                parents.add(winner);

                if (!_withReplacement) selected.add(winner);
            }
            P.add(new Parents(parents));

        }


        return P;
    }

    /**
     * Supportive method for randomly drawing a specimen.
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @return selected specimen.
     */
    private Specimen draw(ArrayList<Specimen> matingPool, IRandom R)
    {
        return matingPool.get(R.nextInt(matingPool.size()));
    }

    /**
     * Supportive method for randomly drawing a specimen that has not yet been drawn. The selection is done by sampling.
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @param selected   already selected parents
     * @return selected specimen.
     */
    private Specimen drawWithReplacementSampling(ArrayList<Specimen> matingPool, IRandom R, Set<Specimen> selected)
    {
        Specimen candidate = matingPool.get(R.nextInt(matingPool.size()));
        while (selected.contains(candidate))
            candidate = matingPool.get(R.nextInt(matingPool.size()));
        return candidate;
    }

    /**
     * Supportive method for randomly drawing a specimen that was not yet drawn.
     * The selection is supported by the auxiliary permutation of pointers.
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @param selected   the already selected specimens (can be null -> if so, it is ignored).
     * @param pointers   auxiliary data structure
     * @return selected specimen.
     */
    private Specimen drawWithReplacementPointers(ArrayList<Specimen> matingPool, IRandom R, Set<Specimen> selected, int[] pointers)
    {
        int idx = R.nextInt(matingPool.size());
        Specimen candidate = matingPool.get(pointers[idx]);
        if (selected == null) return candidate;
        while (selected.contains(candidate))
        {
            idx = (idx + 1) % matingPool.size();
            candidate = matingPool.get(pointers[idx]);
        }
        return candidate;
    }


}
