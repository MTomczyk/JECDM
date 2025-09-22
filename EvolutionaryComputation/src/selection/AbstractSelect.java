package selection;

import ea.IEA;
import exception.PhaseException;
import population.Parents;
import population.Specimen;
import population.SpecimensContainer;
import random.IRandom;
import reproduction.ReproductionStrategy;

import java.util.ArrayList;

/**
 * Abstract class for the {@link selection.ISelect} interface. Effectively, it implements a random selection.
 *
 * @author MTomczyk
 */
public abstract class AbstractSelect implements ISelect
{

    /**
     * Supportive inner class for passing various parameters to the main class.
     */
    public static class Params
    {
        /**
         * Number of parents per one offspring.
         */
        public int _noParentsPerOffspring = 2;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param noParentsPerOffspring no parents per offspring (to be selected)
         */
        public Params(int noParentsPerOffspring)
        {
            _noParentsPerOffspring = noParentsPerOffspring;
        }
    }

    /**
     * Parameterized constructor (to be called using "super"). Instantiates basic fields.
     *
     * @param params params container
     */
    public AbstractSelect(Params params)
    {
        _noParentsPerOffspring = params._noParentsPerOffspring;
    }

    /**
     * Number of parents per one offspring.
     */
    protected final int _noParentsPerOffspring;

    /**
     * The mating pool (dynamic state; set by {@link AbstractSelect#initProcessing(IEA)}; cleared
     * by {@link AbstractSelect#finalizeProcessing(IEA)}).
     */
    protected ArrayList<Specimen> _matingPool;

    /**
     * The random number generator (dynamic state; set by {@link AbstractSelect#initProcessing(IEA)}; cleared
     * by {@link AbstractSelect#finalizeProcessing(IEA)}).
     */
    protected IRandom _R;

    /**
     * The offspring size (dynamic state; set by {@link AbstractSelect#initProcessing(IEA)}; cleared
     * by {@link AbstractSelect#finalizeProcessing(IEA)}).
     */
    protected int _offspringSize;

    /**
     * The reproduction strategy (dynamic state; set by {@link AbstractSelect#initProcessing(IEA)}; cleared
     * by {@link AbstractSelect#finalizeProcessing(IEA)}).
     */
    protected ReproductionStrategy _RS;

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the beginning of the selection process.
     *
     * @param ea evolutionary algorithm being processed
     */
    protected void initProcessing(IEA ea)
    {
        _matingPool = ea.getSpecimensContainer().getMatingPool();
        _R = ea.getR();
        _offspringSize = ea.getOffspringSize();
        _RS = ea.getReproductionStrategy();
    }

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the beginning of the construction of one
     * Parents object.
     *
     * @param ea evolutionary algorithm being processed
     */
    protected void initParentsProcessing(IEA ea)
    {

    }

    /**
     * Auxiliary method that can be overwritten to perform custom operations at the end of the selection process.
     *
     * @param ea evolutionary algorithm being processed
     */
    protected void finalizeProcessing(IEA ea)
    {
        _matingPool = null;
        _R = null;
        _offspringSize = 0;
        _RS = null;
    }

    /**
     * This method generates an array of parents selected for reproduction. This general method takes into account
     * the indications imposed by {@link ReproductionStrategy} kept by {@link IEA}. For instance, the Parents object
     * will contain the data on the expected number of offspring that should be generated from them. The assumptions
     * are as follows: <br>
     * - The total number of the expected offspring should equal {@link IEA#getOffspringSize()}. <br>
     * - The parents are selected from {@link SpecimensContainer#getMatingPool()}. <br>
     * - If {@link ReproductionStrategy} is provided and some constraints are violated, an exception will be thrown.
     *
     * @param ea evolutionary algorithm
     * @return selected parents
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    @Override
    public ArrayList<Parents> selectParents(IEA ea) throws PhaseException
    {
        initProcessing(ea);


        int remaining = ea.getReproductionStrategy().getOffspringLimitPerGeneration() -
                ea.getSpecimensContainer().getSpecimensConstructedDuringGeneration();
        int toSelect = Math.max(0, Math.min(_offspringSize, remaining));
        ArrayList<Parents> parents = new ArrayList<>(toSelect);
        if (toSelect == 0) return parents;

        if (_RS == null)
        {
            for (int i = 0; i < toSelect; i++)
            {
                initParentsProcessing(ea);
                parents.add(selectParents(_matingPool, _R, 1)); // DONE
            }
        }
        else
        {
            if (_RS.isReproductionStrategyConstant())
            {
                int noOff = _RS.getConstantNoOffspringFromParents();
                if (noOff == 1)
                {
                    for (int i = 0; i < toSelect; i++)
                    {
                        initParentsProcessing(ea);
                        parents.add(selectParents(_matingPool, _R, 1)); // DONE
                    }
                }
                else // the general case
                {
                    int mull = (toSelect / noOff);
                    int diff = toSelect - mull * noOff;
                    if ((!_RS.isOffspringThresholdingEnabled()) && (diff != 0))
                    {
                        throw PhaseException.getInstanceWithSource(
                                "It is expected to generate " + noOff + " offspring solutions from " +
                                        "one Parents object, but it is not a divisor of the expected total offspring " +
                                        "size of " + _offspringSize + " (reproduction limit = "
                                        + ea.getReproductionStrategy().getOffspringLimitPerGeneration() + ";" +
                                        " capped to " + toSelect + ")," +
                                        " and offspring thresholding is set to disabled.",
                                this.getClass());
                    }
                    for (int i = 0; i < mull - 1; i++)
                    {
                        initParentsProcessing(ea);
                        parents.add(selectParents(_matingPool, _R, noOff));
                    }
                    initParentsProcessing(ea);
                    parents.add(selectParents(_matingPool, _R, noOff - diff)); // accounts for shrinkage
                } // consider the dynamic case
            }
            else
            {
                int totalOff = 0;
                int cnt = 0;
                while (totalOff < toSelect)
                {
                    int eo = Math.max(1, _RS.getNoOffspringFromParentsGenerator().getNoOffspringPerParents(ea, cnt++, totalOff));
                    if (totalOff + eo > toSelect)
                    {
                        if (!_RS.isOffspringThresholdingEnabled())
                            throw PhaseException.getInstanceWithSource(
                                    "The dynamic generation of the expected number of offspring to " +
                                            "produce indicates the number of " + eo + ". When added to the already generated " +
                                            "numbers (" + totalOff + "), it would exceed the expected total offspring size of " +
                                            toSelect + " (the offspring thresholding is set to disabled; the true offspring size is "
                                            + _offspringSize + "; reproduction limit = "
                                            + ea.getReproductionStrategy().getOffspringLimitPerGeneration() + ";" +
                                            " capped to " + toSelect + ").", this.getClass());
                        else eo = toSelect - totalOff;
                    }
                    initParentsProcessing(ea);
                    parents.add(selectParents(_matingPool, _R, eo));
                    totalOff += eo;
                }
            }
        }

        finalizeProcessing(ea);
        return parents;
    }

    /**
     * Auxiliary method signature for selecting one Parents object from an input specimens array. Random selection.
     * Sets the expected number of offspring to generate to 1.
     *
     * @param specimens input specimens array
     * @param R         random number generator
     * @return parents object
     */
    protected Parents selectParents(ArrayList<Specimen> specimens, IRandom R)
    {
        return selectParents(specimens, R, 1);
    }

    /**
     * Auxiliary method signature for selecting one Parents object from an input specimens array. Random selection.
     *
     * @param specimens              input specimens array
     * @param R                      random number generator
     * @param noOffspringToConstruct the expected number of offspring to be constructed from the selected parents
     * @return parents object
     */
    protected Parents selectParents(ArrayList<Specimen> specimens, IRandom R, int noOffspringToConstruct)
    {
        ArrayList<Specimen> parents = new ArrayList<>(_noParentsPerOffspring);
        for (int i = 0; i < _noParentsPerOffspring; i++)
            parents.add(specimens.get(R.nextInt(specimens.size())));
        return new Parents(parents, noOffspringToConstruct);
    }

}
