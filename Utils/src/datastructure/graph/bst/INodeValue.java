package datastructure.graph.bst;

/**
 * General interface for definitions of graph nodes storing double values.
 *
 * @author MTomczyk
 */
public interface INodeValue
{
    /**
     * Method for comparing two nodes. Used when building the tree.
     *
     * @param otherNode other node the current node is to be compared with
     * @return 0 = nodes are equal, 1 the current node is associated with a strictly greater value, -1 the current node is considered strictly smaller.
     */
    int compare(INodeValue otherNode);

    /**
     * Can be implemented to retrieve a value associated with the node.
     * Note that this method can be used to avoid node value casting.
     * BST does not use this method itself. The BST relies on the compareTo method when inserting/deleting the nodes.
     *
     * @return value
     */
    double getValue();

    /**
     * Returns the hash code.
     *
     * @return hash code
     */
    int hashCode();

    /**
     * Checks equality with another node.
     *
     * @param other other node
     * @return true = both nodes are equal; false = otherwise.
     */
    boolean equals(Object other);

    /**
     * Returns string representation.
     *
     * @return string representation
     */
    String toString();
}
