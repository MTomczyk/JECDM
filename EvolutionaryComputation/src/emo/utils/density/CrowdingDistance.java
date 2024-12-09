package emo.utils.density;

import datastructure.graph.bst.BST;
import datastructure.graph.bst.INodeValue;
import datastructure.graph.bst.TreeNode;
import population.Specimen;
import space.normalization.INormalization;
import valuewrapper.DoubleWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Class for calculating solutions' crowding distances.
 *
 * @author MTomczyk
 */


public class CrowdingDistance
{
    /**
     * Supportive class for sorting specimens using binary sorting tree.
     */
    public static class SpecimenCriterion extends DoubleWrapper implements INodeValue
    {
        /**
         * Specimen object.
         */
        private final Specimen _specimen;

        /**
         * Parameterized constructor.
         *
         * @param specimen           input specimen
         * @param criterionID id of the considered criterion
         */
        public SpecimenCriterion(Specimen specimen, int criterionID)
        {
            _specimen = specimen;
            _value = specimen.getEvaluations()[criterionID];
        }

        /**
         * Method for comparing two nodes. Used when building the tree.
         *
         * @param otherNode other node the current node is to be compared with
         * @return 0 = nodes are equal, 1 the current node is associated with a strictly greater value, -1 the current node is considered strictly smaller.
         */
        public int compare(INodeValue otherNode)
        {
            return Double.compare(_value, otherNode.getValue());
        }

        /**
         * Returns relevant value.
         *
         * @return specimen's evaluation at specified criterion.
         */
        @Override
        public double getValue()
        {
            return _value;
        }


        /**
         * Returns the contained specimen object.
         *
         * @return specimen object.
         */
        public Specimen getSpecimen()
        {
            return _specimen;
        }

        /**
         * Checks if this object equals the other.
         *
         * @param o other node
         * @return true = both objects are equal; false = otherwise.
         */
        @Override
        public boolean equals(Object o)
        {
            if (o == this) return true;
            if (!(o instanceof SpecimenCriterion other)) return false;
            return _specimen.getID().isEqual(other._specimen.getID());
        }

        /**
         * Returns the hash code of the node (hashed according to specimen's id).
         *
         * @return calculated hash code
         */
        @Override
        public final int hashCode()
        {
            return _specimen.getID()._no;
        }
    }

    /**
     * The number of considered criteria.
     */
    private final int _criteria;

    /**
     * Parameterized constructor.
     *
     * @param criteria array of criteria
     */
    public CrowdingDistance(int criteria)
    {
        _criteria = criteria;
    }


    /**
     * Calculates crowding distances.
     *
     * @param front          non-dominated front (IDs of specimens in the provided array)
     * @param specimens      array of specimens
     * @param normalizations normalizations (optional; can be null)
     * @param infinity       substitute for the infinite number
     * @return array of calculated crowding distances; note that the array is of length equal to the "front" size,
     * and its subsequent elements correspond to subsequent IDs in the "front" list.
     */
    public double[] calculateCrowdingDistanceInFront(LinkedList<Integer> front,
                                                     ArrayList<Specimen> specimens,
                                                     INormalization[] normalizations,
                                                     double infinity)
    {

        double[] result = new double[front.size()];

        BST[] BSTs = new BST[_criteria];

        for (int c = 0; c < _criteria; c++)
        {
            BSTs[c] = new BST(specimens.size());
            for (Integer idx : front) BSTs[c].insert(new SpecimenCriterion(specimens.get(idx), c));
        }

        int resultIDX = 0;
        for (Integer idx : front)
        {
            double[] crowding = new double[_criteria];

            for (int c = 0; c < _criteria; c++)
            {
                TreeNode TN = BSTs[c].getTN_Map().get(new SpecimenCriterion(specimens.get(idx), c));
                TreeNode previous = BSTs[c].getInorderPredecessor(TN);
                if (previous == null)
                {
                    Arrays.fill(crowding, infinity);
                    break;
                }
                TreeNode next = BSTs[c].getInorderSuccessor(TN);
                if (next == null)
                {
                    Arrays.fill(crowding, infinity);
                    break;
                }

                if (normalizations != null)
                {
                    INormalization n = normalizations[c];
                    double vA = next.getNodeValue().getValue();
                    double vB = previous.getNodeValue().getValue();
                    crowding[c] = Math.abs(n.getNormalized(vA) - n.getNormalized(vB));
                }
                else
                {
                    double vA = next.getNodeValue().getValue();
                    double vB = previous.getNodeValue().getValue();
                    crowding[c] = Math.abs(vA - vB);
                }
            }
            double r = 0.0f;
            for (double c : crowding) r += c;
            r /= crowding.length;
            if (Double.compare(crowding[0], infinity) == 0) r = infinity;
            result[resultIDX++] = r;
        }
        return result;
    }

    /**
     * Auxiliary method for identifying the most suitable crowding-distance normalization factor.
     *
     * @param cd input, unprocessed crowding-distance values
     * @return crowding-distance normalization factor (divider)
     */
    public static double identifyDivider(double[] cd)
    {
        double max = Double.NEGATIVE_INFINITY;
        for (double v : cd)
        {
            if (Double.compare(v, Double.POSITIVE_INFINITY) == 0) continue;
            if (v > max) max = v;
        }

        if (Double.compare(max, Double.NEGATIVE_INFINITY) == 0) return Double.POSITIVE_INFINITY;
        else return max + 1.0d;
    }
}
