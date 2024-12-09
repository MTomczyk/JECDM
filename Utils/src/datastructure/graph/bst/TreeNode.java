package datastructure.graph.bst;

/**
 * Represents a tree node.
 *
 * @author MTomczyk
 */


public class TreeNode
{
    /**
     * 0/1 coloring used when the tree is in the self-balancing mode {{@link BST#_selfBalancing}}.
     * True = black color; false = red color.
     */
    protected boolean _color = true;

    /**
     * Reference to a parent node (null = this node is a root).
     */
    protected TreeNode _parent;

    /**
     * Reference to the "left" children node (can be null).
     */
    protected TreeNode _leftChildren;

    /**
     * Reference to the "right" children node (can be null).
     */
    protected TreeNode _rightChildren;

    /**
     * Wrapper for a value associated with the node.
     */
    protected INodeValue _nodeValue;


    /**
     * Parameterized constructor.
     *
     * @param nodeValue value wrapper
     */
    public TreeNode(INodeValue nodeValue)
    {
        _parent = null;
        _leftChildren = null;
        _rightChildren = null;
        _nodeValue = nodeValue;
    }

    /**
     * Returns node sibling (if it exists).
     *
     * @return node sibling (null if does not exist).
     */
    public TreeNode getSibling()
    {
        if (_parent == null) return null;
        if (isLeftChildren()) return _parent._rightChildren;
        else return _parent._leftChildren;
    }

    /**
     * Nulls the parent/children references.
     */
    public void clearConnections()
    {
        _parent = null;
        _leftChildren = null;
        _rightChildren = null;
    }

    /**
     * Checks if the node is a left child of its parent.
     *
     * @return true -> the node is left children of its parent; false = otherwise (also triggered when there is no parent).
     */
    public boolean isLeftChildren()
    {
        if ((_parent == null) || (_parent._leftChildren == null)) return false;
        return _parent._leftChildren.equals(this);
    }

    /**
     * Checks if the node is the right child of its parent.
     *
     * @return true -> the node is right children of its parent; false = otherwise (also triggered when there is no parent).
     */
    public boolean isRightChildren()
    {
        if ((_parent == null) || (_parent._rightChildren == null)) return false;
        return _parent._rightChildren.equals(this);
    }

    /**
     * Returns true if the left child is red (false is returned if it is black or there is no left child).
     *
     * @return answer
     */
    public boolean isLeftChildrenRed()
    {
        return (_leftChildren != null) && (!_leftChildren._color);
    }

    /**
     * Returns true if the right child is red (false is returned if it is black or there is no left child).
     *
     * @return answer
     */
    public boolean isRightChildrenRed()
    {
        return (_rightChildren != null) && (!_rightChildren._color);
    }

    /**
     * Checks if the node has two children.
     *
     * @return true is a node has two children; false otherwise
     */
    public boolean hasTwoChildren()
    {
        return ((_leftChildren != null) && (_rightChildren != null));
    }

    /**
     * Returns the hash code calculated based on the id.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return _nodeValue.hashCode();
    }

    /**
     * Checks equality with another node based on their type and id.
     *
     * @param other other node
     * @return true = both nodes are equal; false = otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof TreeNode c)) return false;
        return _nodeValue.equals(c._nodeValue);
    }

    @Override
    public String toString()
    {
        return "[" + _nodeValue.toString() + "]";
    }

    /**
     * Getter for the reference to a parent node (null = this node is a root).
     *
     * @return parent node
     */
    public TreeNode getParent()
    {
        return _parent;
    }

    /**
     * Getter for the reference to the "left" children node (can be null).
     *
     * @return left children
     */
    public TreeNode getLeftChildren()
    {
        return _leftChildren;
    }

    /**
     * Getter for the reference to the "right" children node (can be null).
     *
     * @return right children
     */
    public TreeNode getRightChildren()
    {
        return _rightChildren;
    }

    /**
     * Getter for the wrapper for a value associated with the node.
     *
     * @return node value
     */
    public INodeValue getNodeValue()
    {
        return _nodeValue;
    }
}
