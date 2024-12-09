package emo.utils.decomposition.neighborhood.constructor;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalWrapper;
import emo.utils.decomposition.neighborhood.Neighborhood;
import emo.utils.decomposition.similarity.ISimilarity;

/**
 * Neighborhood constructor founded on the insertion-sort method.
 *
 * @author MTomczyk
 */

public class InsertionSortConstructor extends AbstractNeighborhoodConstructor implements INeighborhoodConstructor
{
    /**
     * Constructs and returns a neighborhood structure for a given family of goals (to be overwritten).
     *
     * @param family     goals family
     * @param similarity similarity measure
     * @param size       neighborhood size (should be smaller/equal the total number of goals stored in the scope)
     * @param scope      if provided, neighbors are supposed to be determined using the provided families (it can be a subset of all families, stored in an arbitrary order)
     * @return constructed neighborhood
     */
    @Override
    public Neighborhood getNeighborhood(Family family, ISimilarity similarity, int size, Family[] scope)
    {
        GoalID[][] N = new GoalID[family.getSize()][size];

        if (size == 1) // special case
        {
            for (int i = 0; i < family.getSize(); i++)
                N[i][0] = family.getGoal(i).getID(); // analyzed goal is its closes neighbor.
            return new Neighborhood(N);
        }

        boolean lessIsPreferred = similarity.isLessPreferred();
        int lastIndex = size - 1; // aux field

        double[] tS = new double[size]; // temporarily stored similarities (pre data)
        GoalWrapper[] tG = new GoalWrapper[size]; // temporarily stored goals (pre data)
        FamilyID[] tF = new FamilyID[size]; // temporarily stored family IDs (pre data)

        for (int i = 0; i < family.getSize(); i++)
        {
            GoalWrapper refGoal = family.getGoal(i); // reference goal to be compared with

            // clear pre-data
            for (int t = 0; t < size; t++)
            {
                tS[t] = Double.NEGATIVE_INFINITY;
                tG[t] = null;
                tF[t] = null;
            }

            int pointer = 0;

            for (Family pF : scope)
            {
                for (GoalWrapper aG : pF.getGoals()) // analyzed goal
                {
                    double s = similarity.calculateSimilarity(refGoal, aG);

                    if (pointer == 0) //explicit assignment
                    {
                        tS[0] = s;
                        tG[0] = aG;
                        tF[0] = pF.getID();
                        pointer++;
                        continue;
                    }

                    // check if pointer exceeds
                    if (pointer > lastIndex)
                    {
                        // check skipping
                        if (((lessIsPreferred) && (Double.compare(s, tS[lastIndex]) >= 0)) ||
                                ((!lessIsPreferred) && (Double.compare(s, tS[lastIndex]) <= 0))) continue;
                        else
                        {
                            tS[lastIndex] = s;
                            tG[lastIndex] = aG;
                            tF[lastIndex] = pF.getID();
                        }
                    }
                    else // default overwrite
                    {
                        tS[pointer] = s;
                        tG[pointer] = aG;
                        tF[pointer] = pF.getID();
                    }

                    // do swaps
                    int idx = pointer;
                    if (idx > lastIndex) idx = lastIndex;

                    while ((idx > 0) &&
                            (((lessIsPreferred) && (tS[idx] < tS[idx - 1])) ||
                                    ((!lessIsPreferred) && (tS[idx] > tS[idx - 1]))) )
                    {
                        //do swap
                        double tmpSim = tS[idx - 1];
                        tS[idx - 1] = tS[idx];
                        tS[idx] = tmpSim;

                        FamilyID tmpF = tF[idx - 1];
                        tF[idx - 1] = tF[idx];
                        tF[idx] = tmpF;

                        GoalWrapper tmpG = tG[idx - 1];
                        tG[idx - 1] = tG[idx];
                        tG[idx] = tmpG;

                        idx--;
                    }

                    if (pointer <= lastIndex) pointer++; // increment the pointer

                }
            }

            // fill neighborhood data
            for (int j = 0; j < size; j++)
            {
                assert tG[j] != null;
                N[i][j] = tG[j].getID();
            }
        }

        return new Neighborhood(N);
    }

}
