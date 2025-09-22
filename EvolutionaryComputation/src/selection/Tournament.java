package selection;

import ea.IEA;
import population.Parents;
import population.Specimen;
import random.IRandom;
import random.Shuffle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the {@link ISelect} interface for selecting parents. Solutions are compared based on their (first)
 * auxiliary scores (by default, can be changed via providing a custom implementation of {@link IComparator}).
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
         * Aux score preference direction: true = gain (to be maximized); false = cost (to be minimized) Used for
         * determining the tournament winner.
         */
        public boolean _preferenceDirection = true;

        /**
         * If true, the selection is with replacement; false -> without replacement (without replacement -> the scope is
         * selection of parents for one offspring, i.e., the parents cannot be repeated).
         */
        public boolean _withReplacement = true;

        /**
         * Auxiliary flag used when selecting without replacement. The flag should be selected based on the number of
         * parents/mating pool size ratio. If the ratio {@literal <} 50%, then the selection of parents is done by
         * sampling (the candidate can be neglected when it was already selected), and the flag is set to true.
         * Otherwise, the chances of a "positive hit" are small. If so, the flag should be set to false, and the
         * selection is supported by using an auxiliary permutation index.
         */
        public boolean _useSampling = true;

        /**
         * An auxiliary specimen comparator (can be null). If null, it will be instantiated as {@link ByAux} (compares
         * specimens based on their first auxiliary values) using the {@link Params#_preferenceDirection} flag. One can
         * provide one's own implementation to define a custom in-tournament comparison.
         */
        public IComparator _comparator = null;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param size tournament size
         */
        public Params(int size)
        {
            this(size, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param size                tournament size
         * @param preferenceDirection aux score preference direction: true = gain (to be maximized); false = cost (to be
         *                            minimized); used for determining the tournament winner
         */
        public Params(int size, boolean preferenceDirection)
        {
            _size = size;
            _preferenceDirection = preferenceDirection;
        }

        /**
         * Parameterized constructor.
         *
         * @param size       tournament size
         * @param comparator one can provide one's own implementation to define a custom in-tournament comparison
         */
        public Params(int size, IComparator comparator)
        {
            _size = size;
            _comparator = comparator;
        }
    }

    /**
     * Tournament size.
     */
    private final int _size;

    /**
     * If true, the selection is with replacement; false -> without replacement (without replacement: the scope is a
     * single tournament intending to select a single specimen-parent). Also, if false, the tournament size is
     * {@literal <}{@literal =} the mating pool size (assert). Note that if false, it may negatively affect the
     * execution time.
     */
    private final boolean _withReplacement;

    /**
     * Auxiliary flag used when selecting without replacement. The flag should be selected based on the number of
     * parents/mating pool size ratio. If the ratio {@literal <} than 50%, then the selection of parents is done by
     * sampling (the candidate can be neglected when it was already selected), and the flag is set to true. Otherwise,
     * the chances of a "positive hit" are small. If so, the flag should be set to false, and the selection is supported
     * by using an auxiliary permutation index.
     */
    protected final boolean _useSampling;


    /**
     * Auxiliary object for shuffling integers (used when sampling without replacement).
     */
    private final Shuffle<Integer> _sh;


    /**
     * Specimen comparator used to perform in-tournament comparisons.
     */
    private final IComparator _comparator;

    /**
     * Parameterized constructor. Solutions are compared based on their (first) auxiliary scores (by default, can be
     * changed via providing a custom implementation of {@link IComparator} via a different constructor).
     *
     * @param size tournament size
     */
    public Tournament(int size)
    {
        this(new Params(size));
    }

    /**
     * Parameterized constructor.
     *
     * @param size       tournament size
     * @param comparator one can provide one's own implementation to define a custom in-tournament comparison
     */
    public Tournament(int size, IComparator comparator)
    {
        this(new Params(size, comparator));
    }

    /**
     * Auxiliary pointers (dynamic state; set by {@link AbstractSelect#initProcessing(IEA)}; cleared
     * by {@link AbstractSelect#finalizeProcessing(IEA)}).
     */
    private int[] _pointers;

    /**
     * Auxiliary set (dynamic state; set by {@link AbstractSelect#initParentsProcessing(IEA)}; cleared
     * by {@link AbstractSelect#initParentsProcessing(IEA)}).
     */
    private Set<Specimen> _selected;

    /**
     * Parameterized constructor. Inner class instance (Params) is passed as a container for parameters.
     *
     * @param p params container
     */
    public Tournament(Params p)
    {
        super(p);
        _size = p._size;
        _withReplacement = p._withReplacement;
        _useSampling = p._useSampling;
        if (!_withReplacement)
        {
            if (!_useSampling) _sh = new Shuffle<>();
            else _sh = null;
        } else _sh = null;
        if (p._comparator != null) _comparator = p._comparator;
        else _comparator = new ByAux(p._preferenceDirection);
    }

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the beginning of the selection process.
     *
     * @param ea evolutionary algorithm being processed
     */
    @Override
    protected void initProcessing(IEA ea)
    {
        super.initProcessing(ea);
        _pointers = null;
        if ((!_withReplacement) && (!_useSampling))
        {
            _pointers = new int[_matingPool.size()];
            for (int p = 0; p < _matingPool.size(); p++) _pointers[p] = p;
        }
    }

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the beginning of the construction of one
     * Parents object.
     *
     * @param ea evolutionary algorithm being processed
     */
    @Override
    protected void initParentsProcessing(IEA ea)
    {
        super.initParentsProcessing(ea);

        if (!_withReplacement)
        {
            _selected = new HashSet<>(); // hashed by their ids.
            if (!_useSampling) //noinspection DataFlowIssue
                _sh.shuffle(_pointers, _R);
        }
    }

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the end of the selection process.
     *
     * @param ea evolutionary algorithm being processed
     */
    @Override
    protected void finalizeProcessing(IEA ea)
    {
        super.finalizeProcessing(ea);
        _pointers = null;
        _selected = null;
    }

    /**
     * Auxiliary method signature for selecting one Parents object from an input specimens array. Random selection.
     *
     * @param specimens              input specimens array
     * @param R                      random number generator
     * @param noOffspringToConstruct the expected number of offspring to be constructed from the selected parents
     * @return parents object
     */
    @Override
    protected Parents selectParents(ArrayList<Specimen> specimens, IRandom R, int noOffspringToConstruct)
    {
        ArrayList<Specimen> parents = new ArrayList<>(_noParentsPerOffspring);
        for (int p = 0; p < _noParentsPerOffspring; p++)
        {
            Specimen winner;
            if (_withReplacement) winner = draw(_matingPool, _R);
            else
            {
                if (_useSampling) winner = drawWithReplacementSampling(_matingPool, _R, _selected);
                else winner = drawWithReplacementPointers(_matingPool, _R, _selected, _pointers);
            }

            for (int r = 1; r < _size; r++)
            {
                Specimen counter;
                if (_withReplacement) counter = draw(_matingPool, _R);
                else
                {
                    if (_useSampling) counter = drawWithReplacementSampling(_matingPool, _R, _selected);
                    else counter = drawWithReplacementPointers(_matingPool, _R, _selected, _pointers);
                }
                if (_comparator.compare(counter, winner) == 1) winner = counter; // replacement
            }

            parents.add(winner);

            if (!_withReplacement) _selected.add(winner);
        }
        return new Parents(parents, noOffspringToConstruct);
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
     * Supportive method for randomly drawing a specimen that has not yet been drawn. The selection is done by
     * sampling.
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
     * Supportive method for randomly drawing a specimen that was not yet drawn. The selection is supported by the
     * auxiliary permutation of pointers.
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
