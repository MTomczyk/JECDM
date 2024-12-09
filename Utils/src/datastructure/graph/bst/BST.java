package datastructure.graph.bst;


import java.util.HashMap;

/**
 * Class representing a binary search tree.
 * Insertion and deletion perform similarly to Java TreeSet in terms of performance,
 * but getting tree nodes is much faster due to employing an additional hash map
 * that associates node values with tree nodes.
 *
 * @author MTomczyk
 */

public class BST
{
    /**
     * Auxiliary class used when removing a node.
     * It is used when the tree is run in the self-balancing mode.
     */
    protected static class RemoveStatus
    {
        /**
         * Resulting node after removal.
         */
        public final TreeNode _u;

        /**
         * If true, either the removed or the replacing node was red.
         */
        public final boolean _atLeastOneRed;

        /**
         * Sibling to u.
         */
        public final TreeNode _sibling;

        /**
         * Parent to either u (or its predecessor) or sibling (upper node).
         */
        public TreeNode _parent;

        /**
         * Parameterized constructor.
         *
         * @param u             resulting node after removal
         * @param atLeastOneRed if true, either the removed or the replacing node was red
         * @param sibling       sibling to u
         * @param parent        parent to either u (or its predecessor) or sibling (upper node)
         */
        public RemoveStatus(TreeNode u, boolean atLeastOneRed, TreeNode sibling, TreeNode parent)
        {
            _u = u;
            _atLeastOneRed = atLeastOneRed;
            _sibling = sibling;
            _parent = parent;
        }

    }

    /**
     * If true, BST is a red-black self-balancing tree.
     */
    protected boolean _selfBalancing;

    /**
     * Associative array is used to find a node with a linked value quickly.
     */
    private HashMap<INodeValue, TreeNode> _TN_Map;

    /**
     * Reference to the root.
     */
    private TreeNode _root;

    /**
     * Dynamically updated reference to the node having the minimal value.
     */
    private TreeNode _minNode;

    /**
     * Dynamically updated reference to the node having the maximal value.
     */
    private TreeNode _maxNode;

    /**
     * Method for nulling references (helpful when using System.gc()).
     */
    public void releaseReferences()
    {
        _TN_Map = null;
        _root = null;
        _minNode = null;
        _maxNode = null;
    }

    /**
     * Parameterized constructor.
     *
     * @param size expected size of the tree (if possible, provide the upper bound); used for instantiating the associative array
     */
    public BST(int size)
    {
        this(size, true);
    }

    /**
     * Parameterized constructor.
     *
     * @param size          expected size of the tree (if possible, provide the upper bound); used for instantiating the associative array
     * @param selfBalancing if true, BST is a red-black self-balancing tree.
     */
    public BST(int size, boolean selfBalancing)
    {
        _TN_Map = new HashMap<>(size);
        _root = null;
        _minNode = null;
        _maxNode = null;
        _selfBalancing = selfBalancing;
    }

    /**
     * Method for inserting new objects to the BST
     *
     * @param newObject new object (wrapped; see {@link INodeValue})
     * @return true = insertion was successful; false = otherwise (e.g., if the object is already in the BST).
     */
    public boolean insert(INodeValue newObject)
    {
        if (_TN_Map.containsKey(newObject)) return false;

        TreeNode newNode = new TreeNode(newObject); // update associative array
        _TN_Map.put(newObject, newNode);

        if (_root == null) // "empty tree" case
        {
            _root = newNode;
            _minNode = newNode;
            _maxNode = newNode;
            if (_selfBalancing) newNode._color = true; // rb
            return true;
        }

        // insertion procedure
        if ((_maxNode != null) && (newObject.compare(_maxNode._nodeValue) >= 0))
        {
            newNode._parent = _maxNode;
            _maxNode._rightChildren = newNode;
            _maxNode = newNode;
        }
        else if ((_minNode != null) && (newObject.compare(_minNode._nodeValue) < 0))
        {
            newNode._parent = _minNode;
            _minNode._leftChildren = newNode;
            _minNode = newNode;
        }
        else
        {
            TreeNode currentNode = _root;
            while (((currentNode._leftChildren != null) && (newObject.compare(currentNode._nodeValue) < 0)) ||
                    ((currentNode._rightChildren != null) && (newObject.compare(currentNode._nodeValue) >= 0)))
            {
                if (newObject.compare(currentNode._nodeValue) < 0) currentNode = currentNode._leftChildren;
                else currentNode = currentNode._rightChildren;
            }

            if (newObject.compare(currentNode._nodeValue) < 0) currentNode._leftChildren = newNode;
            else currentNode._rightChildren = newNode;
            newNode._parent = currentNode;
        }

        newNode._color = false;

        if (_selfBalancing)
        {
            TreeNode cN = newNode;
            while ((cN._parent != null) && (!cN._parent._color))
            {
                TreeNode uncle = cN._parent.getSibling();
                TreeNode parent = cN._parent;
                TreeNode grandparent = cN._parent._parent;
                if ((uncle != null) && (!uncle._color))
                {
                    uncle._color = true;
                    parent._color = true;
                    grandparent._color = false;
                    cN = grandparent;
                }
                else
                {
                    if ((cN.isLeftChildren()) && (parent.isLeftChildren())) // case 1
                    {
                        rightRotation(grandparent);
                        swapColors(parent, grandparent);
                    }
                    else if ((cN.isRightChildren()) && (parent.isLeftChildren())) // case 2
                    {
                        leftRotation(parent);
                        rightRotation(grandparent);
                        swapColors(cN, grandparent);
                    }
                    else if ((cN.isRightChildren()) && (parent.isRightChildren())) // case 3
                    {
                        leftRotation(grandparent);
                        swapColors(parent, grandparent);
                    }
                    else if ((cN.isLeftChildren()) && (parent.isRightChildren())) // case 3
                    {
                        rightRotation(parent);
                        leftRotation(grandparent);
                        swapColors(cN, grandparent);
                    }
                    break;
                }
            }

            if ((cN == _root) && (!cN._color)) cN._color = true;
        }

        return true;
    }

    /**
     * Swaps colors of two nodes.
     *
     * @param A the first node
     * @param B the second node
     */
    private void swapColors(TreeNode A, TreeNode B)
    {
        boolean tmp = A._color;
        A._color = B._color;
        B._color = tmp;
    }

    /**
     * Executes the left rotation around a node. Assumes that the input node has the right child.
     *
     * @param X input node
     */
    private void leftRotation(TreeNode X)
    {
        if (X._rightChildren == null) return;
        TreeNode R = X._rightChildren;
        TreeNode OP = X._parent;
        boolean left = X.isLeftChildren();
        X._parent = R;
        X._rightChildren = R._leftChildren;
        if (R._leftChildren != null) R._leftChildren._parent = X;
        R._leftChildren = X;

        R._parent = OP;
        if (OP != null)
        {
            if (left) OP._leftChildren = R;
            else OP._rightChildren = R;
        }
        else _root = R; // set new root
    }

    /**
     * Executes the right rotation around a node. Assumes that the input node has a left child.
     *
     * @param X input node
     */
    private void rightRotation(TreeNode X)
    {
        if (X._leftChildren == null) return;
        TreeNode L = X._leftChildren;
        TreeNode OP = X._parent;
        boolean left = X.isLeftChildren();
        X._parent = L;
        X._leftChildren = L._rightChildren;
        if (L._rightChildren != null) L._rightChildren._parent = X;
        L._rightChildren = X;

        L._parent = OP;
        if (OP != null)
        {
            if (left) OP._leftChildren = L;
            else OP._rightChildren = L;
        }
        else _root = L; // set new root
    }


    /**
     * Method for removing a node from BST.
     *
     * @param nodeValue node to be removed
     * @return true = removal was successful; false = otherwise (e.g., there was no such node in the BST)
     */
    public boolean remove(INodeValue nodeValue)
    {
        TreeNode nodeToDelete = _TN_Map.get(nodeValue);
        if (nodeToDelete == null) return false;

        RemoveStatus rs = remove(nodeToDelete);

        if ((!_selfBalancing) || (_root == null)) return true;

        if (rs._atLeastOneRed)
        {
            // if not nulled (leaf clear) -> make it black (1-children case in removal)
            if (rs._u != null) rs._u._color = true;
        }

        TreeNode u = rs._u;
        TreeNode s = rs._sibling;
        TreeNode p = rs._parent;
        boolean db = true;


        //u cant be null at this stage
        while ((db) && (!_root.equals(u)))
        {
            if (s == null)
            {
                u = p;
                if (u != null)
                {
                    p = u._parent;
                    s = u.getSibling();
                }
            }
            else if (!s._color)
            {
                p._color = false;
                s._color = true;
                if (s.isLeftChildren()) rightRotation(p);
                else leftRotation(p);
            }
            else
            {
                boolean slcr = s.isLeftChildrenRed();
                boolean srcr = s.isRightChildrenRed();
                if ((slcr) || (srcr))
                {
                    if (slcr)
                    {
                        if (s.isLeftChildren())
                        {
                            s._leftChildren._color = s._color;
                            s._color = p._color;
                            rightRotation(p);
                        }
                        else
                        {
                            s._leftChildren._color = p._color;
                            rightRotation(s);
                            leftRotation(p);
                        }
                    }
                    else
                    {
                        if (s.isLeftChildren())
                        {
                            s._rightChildren._color = p._color;
                            leftRotation(s);
                            rightRotation(p);
                        }
                        else
                        {
                            s._rightChildren._color = s._color;
                            s._color = p._color;
                            leftRotation(p);
                        }
                    }
                    p._color = true;
                    db = false;
                }
                else
                {
                    s._color = false;
                    if (p._color)
                    {
                        u = p;
                        p = u._parent;
                        s = u.getSibling();
                    }
                    else
                    {
                        p._color = true;
                        db = false;
                    }
                }
            }

        }

        return true;
    }

    /**
     * Method for removing a node from BST.
     *
     * @param nodeToDelete node to be removed
     * @return the method returns a node that replaced the removed node in the hierarchy
     */
    private RemoveStatus remove(TreeNode nodeToDelete)
    {
        _TN_Map.remove(nodeToDelete._nodeValue); // remove original mapping

        if (nodeToDelete.hasTwoChildren())
        {
            // find inorder successor
            TreeNode IOS = getInorderSuccessor(nodeToDelete);
            // replace values
            nodeToDelete._nodeValue = IOS._nodeValue; // passes node value, that's all; no changes in tree structure
            _TN_Map.replace(IOS._nodeValue, nodeToDelete); // change in mapping required
            nodeToDelete = IOS;
        }

        // two cases remain

        // no children case
        if ((nodeToDelete._leftChildren == null) && (nodeToDelete._rightChildren == null))
        {
            RemoveStatus rs = null;
            if (_selfBalancing)
                rs = new RemoveStatus(null, !nodeToDelete._color, nodeToDelete.getSibling(), nodeToDelete._parent);


            // lose references (mapping already changed); no removal required
            if (nodeToDelete._parent == null)
            {
                _root = null; // last node case
                _minNode = null;
                _maxNode = null;
            }
            else
            {
                if (nodeToDelete.isLeftChildren())
                {
                    nodeToDelete._parent._leftChildren = null;
                    if (nodeToDelete.equals(_minNode)) _minNode = nodeToDelete._parent;
                }
                else if (nodeToDelete.isRightChildren())
                {
                    nodeToDelete._parent._rightChildren = null;
                    if (nodeToDelete.equals(_maxNode)) _maxNode = nodeToDelete._parent;
                }
            }
            nodeToDelete.clearConnections();
            return rs;
        }
        else // just one child
        {
            boolean rightChildren = nodeToDelete._leftChildren == null;
            TreeNode children = nodeToDelete._leftChildren;
            if (rightChildren) children = nodeToDelete._rightChildren;

            RemoveStatus rs = null;
            if (_selfBalancing)
                rs = new RemoveStatus(children, !nodeToDelete._color || !children._color, nodeToDelete.getSibling(), nodeToDelete._parent);

            // min/max
            if ((rightChildren) && (nodeToDelete.equals(_minNode)))
                _minNode = getInorderSuccessor(nodeToDelete);
            if ((!rightChildren) && (nodeToDelete.equals(_maxNode)))
                _maxNode = getInorderPredecessor(nodeToDelete);

            // in this case, tree structure is going to change
            children._parent = nodeToDelete._parent;
            if (nodeToDelete._parent == null) _root = children;
            else
            {
                if (nodeToDelete.isLeftChildren()) nodeToDelete._parent._leftChildren = children;
                else if (nodeToDelete.isRightChildren()) nodeToDelete._parent._rightChildren = children;
            }

            nodeToDelete.clearConnections();

            return rs;
        }
    }

    /**
     * Recursive method. Calculates tree height.
     *
     * @return tree height
     */
    public int getHeight()
    {
        if (_root == null) return 0;
        return getHeight(_root);
    }

    /**
     * Recursive method. Calculates tree height. Internally called.
     *
     * @param node called node
     * @return tree height
     */
    private int getHeight(TreeNode node)
    {
        if (node == null) return 0;
        int h1 = 1;
        int h2 = 1;
        if (node._leftChildren != null) h1 = 1 + getHeight(node._leftChildren);
        if (node._rightChildren != null) h2 = 1 + getHeight(node._rightChildren);
        return Math.max(h1, h2);
    }

    /**
     * Can be called to check if the BST assigned proper colors to nodes (when running in the self-balancing mode).
     *
     * @return true if colors are assigned properly (always returns true if the tree is not self-balancing); false otherwise
     */
    protected boolean areColorsValid()
    {
        if (_root == null) return true;
        if (!_selfBalancing) return true;

        if (!_root._color) return false;
        if ((_root._leftChildren != null) && (!areColorsValid(_root._leftChildren))) return false;
        return (_root._rightChildren == null) || (areColorsValid(_root._rightChildren));
    }

    /**
     * Can be called to check if the BST assigned proper colors to nodes (when running in the self-balancing mode).
     * Internally called.
     *
     * @param node called node
     * @return true if colors are assigned properly (always returns true if the tree is not self-balancing); false otherwise
     */
    private boolean areColorsValid(TreeNode node)
    {
        if ((!node._color) && (!node._parent._color)) return false;
        if ((node._leftChildren != null) && (!areColorsValid(node._leftChildren))) return false;
        return (node._rightChildren == null) || (areColorsValid(node._rightChildren));
    }

    /**
     * Auxiliary method for printing tree (printing) where the nodes are indicated as black ('b') or red ('r').
     */
    public void printTreeColors()
    {
        int h = getHeight();
        if (h == 0) return;
        int w = (int) (Math.pow(2, h) + 0.1d) - 1;
        char[][] m = new char[h][w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++) m[i][j] = ' ';

        int mid = w / 2;
        if (_root._color) m[0][mid] = 'b';
        else m[0][mid] = 'r';

        if (_root._leftChildren != null) fillColorTable(m, _root._leftChildren, 1, 0, mid - 1);
        if (_root._rightChildren != null) fillColorTable(m, _root._rightChildren, 1, mid + 1, w - 1);
        for (int i = 0; i < h; i++)
            System.out.println(m[i]);
    }

    /**
     * Auxiliary method filling the tree color char matrix to be printed.
     *
     * @param m     color matrix
     * @param node  considered node
     * @param level tree level
     * @param lb    left bound (inclusive) for the character window
     * @param rb    right bound (inclusive) for the character window
     */
    private void fillColorTable(char[][] m, TreeNode node, int level, int lb, int rb)
    {
        int mid = lb + (rb - lb) / 2;
        if (node._color) m[level][mid] = 'b';
        else m[level][mid] = 'r';
        if (node._leftChildren != null)
            fillColorTable(m, node._leftChildren, level + 1, lb, mid - 1);
        if (node._rightChildren != null)
            fillColorTable(m, node._rightChildren, level + 1, mid + 1, rb);
    }


    /**
     * Identifies an inorder successor of a node.
     *
     * @param node input node
     * @return inorder successor
     */
    public TreeNode getInorderSuccessor(TreeNode node)
    {
        TreeNode currentNode = node;
        if ((currentNode._rightChildren == null) && (currentNode._parent == null)) return null;
        else if (currentNode._rightChildren == null)
        {
            while ((currentNode._parent != null) && (currentNode.equals(currentNode._parent._rightChildren)))
                currentNode = currentNode._parent;
            if (currentNode._parent == null) return null;
            currentNode = currentNode._parent;
        }
        else
        {
            currentNode = currentNode._rightChildren;
            while (currentNode._leftChildren != null) currentNode = currentNode._leftChildren;
        }
        return currentNode;
    }

    /**
     * Identifies an inorder predecessor of a node.
     *
     * @param node input node
     * @return inorder predecessor
     */
    public TreeNode getInorderPredecessor(TreeNode node)
    {
        TreeNode currentNode = node;
        if ((currentNode._leftChildren == null) && (currentNode._parent == null)) return null;
        else if (currentNode._leftChildren == null)
        {
            while ((currentNode._parent != null) && (currentNode.equals(currentNode._parent._leftChildren)))
                currentNode = currentNode._parent;
            if (currentNode._parent == null) return null;
            currentNode = currentNode._parent;
        }
        else
        {
            currentNode = currentNode._leftChildren;
            while (currentNode._rightChildren != null) currentNode = currentNode._rightChildren;
        }
        return currentNode;
    }

    /**
     * Getter for an associative array mapping values to nodes.
     *
     * @return associative array
     */
    public HashMap<INodeValue, TreeNode> getTN_Map()
    {
        return _TN_Map;
    }

    /**
     * Getter for the reference to the root node.
     *
     * @return reference to the root node
     */
    public TreeNode getRoot()
    {
        return _root;
    }

    /**
     * Getter for a reference to the min node.
     *
     * @return min node
     */
    public TreeNode getMinNode()
    {
        return _minNode;
    }

    /**
     * Getter for a reference to the max node.
     *
     * @return reference to the max node
     */
    public TreeNode getMaxNode()
    {
        return _maxNode;
    }

}
