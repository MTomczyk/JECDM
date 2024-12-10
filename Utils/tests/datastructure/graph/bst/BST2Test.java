package datastructure.graph.bst;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several exhaustive tests for BST class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class BST2Test
{
    /**
     * Supportive "node value" class used for testing BSTs.
     */
    public static class TestObject implements INodeValue
    {
        /**
         * Test types.
         */
        public enum TEST_TYPE
        {
            /**
             * A test enum
             */
            TEST_A,

            /**
             * B test enum
             */
            TEST_B,

            /**
             * C test enum
             */
            TEST_C
        }

        /**
         * ID of node value.
         */
        public final int _id;

        /**
         * Object type.
         */
        public TEST_TYPE _type;

        /**
         * Parameterized constructor.
         *
         * @param type node type
         * @param id   node id
         */
        public TestObject(TEST_TYPE type, int id)
        {
            _type = type;
            _id = id;
        }

        /**
         * Method for comparing two nodes. Used when building the tree.
         *
         * @param otherNode other node the current node is to be compared with
         * @return 0 = nodes are equal, 1 the current node is associated with a strictly greater value, -1 the current node is considered strictly smaller.
         */
        public int compare(INodeValue otherNode)
        {
            return Double.compare(getValue(), otherNode.getValue());
        }

        /**
         * Can be implemented to retrieve a value associated with the node.
         * Note that this method can be used to avoid node values casting.
         * This method is not used by BST itself. The BST relies on compareTo method when inserting/deleting the nodes.
         *
         * @return value
         */
        @Override
        public double getValue()
        {
            if (_type == TEST_TYPE.TEST_C) return 2;
            if (_type == TEST_TYPE.TEST_B) return 1;
            return 0;
        }

        /**
         * Returns hash code based on the id.
         */
        @Override
        public int hashCode()
        {
            return _id;
        }

        /**
         * Verified whether this node equals other (IDs are compared).
         */
        @Override
        public boolean equals(Object other)
        {
            if (!(other instanceof TestObject c)) return false;
            return _id == c._id;
        }

        /**
         * Returns string representation.
         *
         * @return string representation
         */
        @Override
        public String toString()
        {
            return _id + " ; " + _type;
        }
    }

    /**
     * Test 1. Simple insertions and search.
     */
    @Test
    void test1()
    {
        BST bst = new BST(10, false);
        TestObject.TEST_TYPE[] types = new TestObject.TEST_TYPE[]{TestObject.TEST_TYPE.TEST_A, TestObject.TEST_TYPE.TEST_B,
        TestObject.TEST_TYPE.TEST_C, TestObject.TEST_TYPE.TEST_B, TestObject.TEST_TYPE.TEST_C, TestObject.TEST_TYPE.TEST_A};
        for (int i = 0; i < types.length; i++)
            assertTrue(bst.insert(new TestObject(types[i], i)));

        assertEquals(6, bst.getTN_Map().size());
        assertEquals(0, bst.getRoot()._nodeValue.getValue(), 0.0001d);
        assertEquals(0, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(2, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);

        TreeNode tn = bst.getMinNode();
        assertEquals(TestObject.TEST_TYPE.TEST_A, ((TestObject) tn.getNodeValue())._type);
        assertEquals(0, tn._nodeValue.getValue(), 0.000001d);

        tn = bst.getInorderSuccessor(tn);
        assertEquals(TestObject.TEST_TYPE.TEST_A, ((TestObject) tn.getNodeValue())._type);
        assertEquals(0, tn._nodeValue.getValue(), 0.000001d);

        tn = bst.getInorderSuccessor(tn);
        assertEquals(TestObject.TEST_TYPE.TEST_B, ((TestObject) tn.getNodeValue())._type);
        assertEquals(1, tn._nodeValue.getValue(), 0.000001d);

        tn = bst.getInorderSuccessor(tn);
        assertEquals(TestObject.TEST_TYPE.TEST_B, ((TestObject) tn.getNodeValue())._type);
        assertEquals(1, tn._nodeValue.getValue(), 0.000001d);

        tn = bst.getInorderSuccessor(tn);
        assertEquals(TestObject.TEST_TYPE.TEST_C, ((TestObject) tn.getNodeValue())._type);
        assertEquals(2, tn._nodeValue.getValue(), 0.000001d);

        tn = bst.getInorderSuccessor(tn);
        assertEquals(TestObject.TEST_TYPE.TEST_C, ((TestObject) tn.getNodeValue())._type);
        assertEquals(2, tn._nodeValue.getValue(), 0.000001d);
    }


}