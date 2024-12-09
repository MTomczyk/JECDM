package emo.utils.decomposition.nsgaiii;

import population.Specimen;
import random.IRandom;

import java.util.LinkedList;

/**
 * Implementation of {@link ISpecimenResolveTie} that resolves the tie randomly.
 *
 * @author MTomczyk
 */
public class RandomSpecimen implements ISpecimenResolveTie
{
    /**
     * Returns the index of a specimen to be returned randomly.
     *
     * @param specimens specimens assigned to the assignment object (are expected to be sorted from the most relevant the least)
     * @param R         random number generator
     * @return selected assignment
     */
    @Override
    public int getSpecimenIndex(LinkedList<Specimen> specimens, IRandom R)
    {
        return R.nextInt(specimens.size()); // get from a list is costly, but workaround would cost more
    }
}
