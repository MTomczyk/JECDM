package emo.utils.decomposition.nsgaiii;

import population.Specimen;
import random.IRandom;

import java.util.LinkedList;

/**
 * Interface for classes resolving a tie during specimen selection triggered when the niche count of the selected
 * goal/assignment > 1.
 *
 * @author MTomczyk
 */
public interface ISpecimenResolveTie
{
    /**
     * Returns the index of a specimen to be returned.
     *
     * @param specimens specimens assigned to the assignment object (are expected to be sorted from the most relevant the least)
     * @param R random number generator
     * @return specimen index
     */
    int getSpecimenIndex(LinkedList<Specimen> specimens, IRandom R);
}
