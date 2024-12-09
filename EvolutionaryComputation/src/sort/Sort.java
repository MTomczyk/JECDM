package sort;

import datastructure.graph.bst.BST;
import datastructure.graph.bst.INodeValue;
import datastructure.graph.bst.TreeNode;
import population.Specimen;
import valuewrapper.DoubleWrapper;

import java.util.ArrayList;

/**
 * Series of auxiliary sorting procedures.
 *
 * @author MTomczyk
 */


public class Sort
{
    /**
     * Supportive wrapper of the value linked to the specimen object: first aux score of its alternative.
     */
    public static class SpecimenNode extends DoubleWrapper implements INodeValue
    {
        /**
         * Stored specimen.
         */
        private final Specimen _specimen;

        /**
         * Parameterized constructor (uses the first aux value).
         *
         * @param specimen specimen to be stored
         */
        public SpecimenNode(Specimen specimen)
        {
            _specimen = specimen;
            _value = _specimen.getAlternative().getAuxScore();
        }

        /**
         * Parameterized constructor (uses the arbitrary value).
         *
         * @param specimen specimen to be stored
         * @param value    value assigned to the specimen
         */
        public SpecimenNode(Specimen specimen, double value)
        {
            _specimen = specimen;
            _value = value;
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
         * Returns value linked to the first aux score of the alternative linked to the stored specimen.
         *
         * @return the first aux score of the alternative linked to the stored specimen
         */
        @Override
        public double getValue()
        {
            return _value;
        }

        /**
         * Returns stored specimen.
         *
         * @return stored specimen
         */
        public Specimen getSpecimen()
        {
            return _specimen;
        }

        /**
         * Return specimen's hash code.
         *
         * @return specimen's hash code
         */
        @Override
        public int hashCode()
        {
            return _specimen.hashCode();
        }

        /**
         * Checks if this node equals other (compares stored specimens).
         *
         * @param other other node
         * @return true if specimens are equal; false otherwise
         */
        @Override
        public boolean equals(Object other)
        {
            if (!(other instanceof SpecimenNode)) return false;
            return _specimen.equals(((SpecimenNode) other)._specimen);
        }
    }

    /**
     * Method sorts specimens based on their alternatives' first performance scores (sorting procedure is based on the binary search tree).
     *
     * @param specimen       array of specimens (population)
     * @param ascendingOrder if true: specimens are sorted in ascending order of their (first) performance values.
     */
    public static void sortByPerformanceValue(ArrayList<Specimen> specimen, boolean ascendingOrder)
    {
        BST bst = new BST(specimen.size());
        for (Specimen s : specimen) bst.insert(new SpecimenNode(s, s.getEvaluations()[0]));

        doSorting(specimen, ascendingOrder, bst);
    }

    /**
     * Method sorts specimens based on their alternatives' first aux scores (sorting procedure is based on the binary search tree).
     *
     * @param specimen       array of specimens (population)
     * @param ascendingOrder if true: specimens are sorted in ascending order of their (first) aux values.
     */
    public static void sortByAuxValue(ArrayList<Specimen> specimen, boolean ascendingOrder)
    {
        BST bst = new BST(specimen.size());
        for (Specimen s : specimen) bst.insert(new SpecimenNode(s));

        doSorting(specimen, ascendingOrder, bst);
    }

    /**
     * Auxiliary method that performs the sorting (BST-based)
     *
     * @param specimen       input specimen array
     * @param ascendingOrder if true: specimens are sorted in ascending order of their stored values.
     * @param bst            constructed BST
     */
    private static void doSorting(ArrayList<Specimen> specimen, boolean ascendingOrder, BST bst)
    {
        int pos = 0;

        if (ascendingOrder)
        {
            TreeNode TN = bst.getMinNode();
            specimen.set(pos++, ((SpecimenNode) TN.getNodeValue()).getSpecimen());

            while ((TN = bst.getInorderSuccessor(TN)) != null)
                specimen.set(pos++, ((SpecimenNode) TN.getNodeValue()).getSpecimen());
        }
        else
        {
            TreeNode TN = bst.getMaxNode();
            specimen.set(pos++, ((SpecimenNode) TN.getNodeValue()).getSpecimen());

            while ((TN = bst.getInorderPredecessor(TN)) != null)
                specimen.set(pos++, ((SpecimenNode) TN.getNodeValue()).getSpecimen());
        }
    }
}
