package datastructure.graph.bst;

import org.apache.commons.math4.legacy.stat.StatUtils;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import valuewrapper.DoubleWrapper;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Several exhaustive tests for BST class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class BST1Test
{
    /**
     * Supportive "node value" class used for testing BSTs.
     */
    public static class TestObject extends DoubleWrapper implements INodeValue
    {
        /**
         * ID of node value.
         */
        public final int _id;

        /**
         * Parameterized constructor.
         *
         * @param value value linked to the node
         * @param id    node id
         */
        public TestObject(double value, int id)
        {
            _value = value;
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
            return Double.compare(_value, otherNode.getValue());
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
            return _id + " ; " + _value;
        }
    }

    /**
     * Test 1. Simple insertions and search.
     */
    @Test
    void test1()
    {
        BST bst = new BST(10, false);
        double[] vals = new double[]{10, 8, 3, 9, 8.5, 9, 12, 14, 13, 15, 17};
        for (int i = 0; i < vals.length; i++)
            assertTrue(bst.insert(new TestObject(vals[i], i)));

        assertEquals(11, bst.getTN_Map().size());
        assertEquals(10.0d, bst.getRoot()._nodeValue.getValue(), 0.0001d);
        assertEquals(3.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(17.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);

        assertEquals(5, bst.getHeight());

        TreeNode currentNode = bst.getMinNode();
        assertEquals(3.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(8.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(8.5d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(9.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(9.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(10.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(12.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(13.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(14.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(15.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertEquals(17.0d, currentNode._nodeValue.getValue(), 0.0001d);
        currentNode = bst.getInorderSuccessor(currentNode);
        assertNull(currentNode);
    }

    /**
     * Test 2. Search verification.
     */
    @Test
    void test2()
    {
        int trials = 1000;
        int items = 1000;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < trials; t++)
        {
            ArrayList<TestObject> A = new ArrayList<>(items);
            for (int i = 0; i < items; i++) A.add(new TestObject(R.nextDouble(), i));

            BST bst = new BST(items, false);
            for (int i = 0; i < items; i++) bst.insert(A.get(i));
            A.sort(TestObject::compare);
            TreeNode currentNode = bst.getMinNode();
            assertEquals(A.get(0).getValue(), currentNode._nodeValue.getValue(), 0.0001d);
            for (int i = 1; i < items; i++)
            {
                currentNode = bst.getInorderSuccessor(currentNode);
                assertEquals(A.get(i).getValue(), currentNode._nodeValue.getValue(), 0.0001d);
            }
            currentNode = bst.getInorderSuccessor(currentNode);
            assertNull(currentNode);
        }
    }

    /**
     * Test 3. Step by step verification.
     */
    @Test
    void test3()
    {
        BST bst = new BST(10, false);
        double[] vals = new double[]{10};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(1, bst.getHeight());
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(0, bst.getHeight());
        assertNull(bst.getRoot());
        assertNull(bst.getMinNode());
        assertNull(bst.getMaxNode());
        assertEquals(0, bst.getTN_Map().size());
    }

    /**
     * Test 4. Step by step verification.
     */
    @Test
    void test4()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10, 7};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(2, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[1], 1)));
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(1, bst.getHeight());
        assertEquals(10.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(10.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(1, bst.getTN_Map().size());
    }

    /**
     * Test 5. Step by step verification.
     */
    @Test
    void test5()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{12.0d, 13.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(2, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[1], 1)));
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(1, bst.getHeight());
        assertEquals(12.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(12.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(1, bst.getTN_Map().size());
    }

    /**
     * Test 6. Step by step verification.
     */
    @Test
    void test6()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 12.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(2, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._nodeValue);
        assertEquals(2, bst.getHeight());
        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(12.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(2, bst.getTN_Map().size());
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._leftChildren._parent._nodeValue);
        assertNull(bst.getRoot()._rightChildren);
        assertNull(bst.getRoot()._parent);
    }

    /**
     * Test 7. Step by step verification.
     */
    @Test
    void test7()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 12.0d, 15.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(3, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._nodeValue);
        assertEquals(2, bst.getHeight());
        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(15.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(3, bst.getTN_Map().size());

        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[3], 3), bst.getRoot()._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._leftChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._parent._nodeValue);
    }

    /**
     * Test 8. Step by step verification.
     */
    @Test
    void test8()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 12.0d, 15.0d, 11.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(3, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._nodeValue);
        assertEquals(3, bst.getHeight());
        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(15.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(4, bst.getTN_Map().size());
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._leftChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._rightChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._rightChildren._parent._nodeValue);
        assertNull(bst.getRoot()._leftChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren);
        assertEquals(new TestObject(vals[3], 3), bst.getRoot()._rightChildren._rightChildren._nodeValue);
        assertNull(bst.getRoot()._rightChildren._leftChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren._rightChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren._leftChildren);
    }

    /**
     * Test 9. Step by step verification.
     */
    @Test
    void test9()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 12.0d, 15.0d, 11.0d, 11.5};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(4, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._nodeValue);
        assertEquals(3, bst.getHeight());

        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(15.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(5, bst.getTN_Map().size());

        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._leftChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[4], 4), bst.getRoot()._rightChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._leftChildren._parent._nodeValue);
        assertNull(bst.getRoot()._leftChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren);
        assertEquals(new TestObject(vals[5], 5), bst.getRoot()._rightChildren._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._leftChildren._parent._nodeValue);
        assertEquals(new TestObject(vals[3], 3), bst.getRoot()._rightChildren._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._rightChildren._rightChildren._parent._nodeValue);
        assertNull(bst.getRoot()._rightChildren._leftChildren._leftChildren);
        assertNull(bst.getRoot()._rightChildren._leftChildren._rightChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren._rightChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren._leftChildren);
    }

    /**
     * Test 10. Step by step verification.
     */
    @Test
    void test10()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(2, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[0], 0)));
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._nodeValue);
        assertEquals(1, bst.getHeight());
        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(8.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(1, bst.getTN_Map().size());
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._nodeValue);
        assertNull(bst.getRoot()._leftChildren);
        assertNull(bst.getRoot()._leftChildren);
    }

    /**
     * Test 11. Step by step verification.
     */
    @Test
    void test11()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 12.0d, 11.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(3, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[2], 2)));
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(2, bst.getHeight());
        assertEquals(8.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(11.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(3, bst.getTN_Map().size());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[1], 1), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._leftChildren._parent._nodeValue);
        assertNull(bst.getRoot()._leftChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren);
        assertEquals(new TestObject(vals[3], 3), bst.getRoot()._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._rightChildren._parent._nodeValue);
        assertNull(bst.getRoot()._rightChildren._leftChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren);
    }

    /**
     * Test 12. Step by step verification.
     */
    @Test
    void test12()
    {
        BST bst = new BST(2, false);
        double[] vals = new double[]{10.0d, 8.0d, 9.0d, 12.0d};
        for (int i = 0; i < vals.length; i++) assertTrue(bst.insert(new TestObject(vals[i], i)));
        assertEquals(3, bst.getHeight());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertTrue(bst.remove(new TestObject(vals[1], 1)));
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(2, bst.getHeight());
        assertEquals(9.0d, bst.getMinNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(12.0d, bst.getMaxNode()._nodeValue.getValue(), 0.0001d);
        assertEquals(3, bst.getTN_Map().size());
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(vals[2], 2), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._leftChildren._parent._nodeValue);
        assertNull(bst.getRoot()._leftChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren);
        assertEquals(new TestObject(vals[3], 3), bst.getRoot()._rightChildren._nodeValue);
        assertEquals(new TestObject(vals[0], 0), bst.getRoot()._rightChildren._parent._nodeValue);
        assertNull(bst.getRoot()._rightChildren._leftChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren);
    }

    /**
     * Test 13. Massive test.
     */
    @Test
    void test13()
    {
        int trials = 100;
        int items = 100;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < trials; t++)
        {
            ArrayList<TestObject> A = new ArrayList<>(items);
            for (int i = 0; i < items; i++) A.add(new TestObject(2 * i + R.nextDouble(), i));

            BST bst = new BST(items, false);
            for (int i = 0; i < items; i++)
            {
                bst.insert(A.get(i));

                double[] c = new double[i + 1];
                for (int j = 0; j <= i; j++) c[j] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.00001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.00001d);
            }

            for (int i = 0; i < items; i++)
            {
                double[] c = new double[items - i];
                for (int j = i; j < items; j++) c[j - i] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.00001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.00001d);
                assertTrue(bst.remove(A.get(i)));
            }

            assertEquals(0, bst.getTN_Map().size());
            assertNull(bst.getRoot());
            assertNull(bst.getMaxNode());
            assertNull(bst.getMinNode());
            assertEquals(0, bst.getHeight());
        }
    }

    /**
     * Test 14. Massive test.
     */
    @Test
    void test14()
    {
        int trials = 100;
        int items = 100;

        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        for (int t = 0; t < trials; t++)
        {
            ArrayList<TestObject> A = new ArrayList<>(items);
            for (int i = 0; i < 100; i++) A.add(new TestObject(1000 - 2 * i + R.nextDouble(), i));

            BST bst = new BST(items, false);
            for (int i = 0; i < items; i++)
            {
                bst.insert(A.get(i));

                double[] c = new double[i + 1];
                for (int j = 0; j <= i; j++) c[j] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.00001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.00001d);
            }

            for (int i = 0; i < items; i++)
            {
                double[] c = new double[items - i];
                for (int j = i; j < items; j++) c[j - i] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.00001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.00001d);
                assertTrue(bst.remove(A.get(i)));
            }

            assertEquals(0, bst.getTN_Map().size());
            assertNull(bst.getRoot());
            assertNull(bst.getMaxNode());
            assertNull(bst.getMinNode());
            assertEquals(0, bst.getHeight());
        }
    }

    /**
     * Test 15. Massive test.
     */
    @Test
    void test15()
    {
        int trials = 100;
        int items = 100;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < trials; t++)
        {
            ArrayList<TestObject> A = new ArrayList<>(items);
            for (int i = 0; i < items; i++)
            {
                double d = R.nextDouble();
                A.add(new TestObject(d, i));
            }

            BST bst = new BST(items, false);
            for (int i = 0; i < items; i++)
            {
                bst.insert(A.get(i));

                double[] c = new double[i + 1];
                for (int j = 0; j <= i; j++) c[j] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.0000001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.0000001d);
            }

            for (int i = 0; i < items; i++)
            {
                double[] c = new double[items - i];
                for (int j = i; j < items; j++) c[j - i] = A.get(j).getValue();
                assertEquals(StatUtils.max(c), bst.getMaxNode()._nodeValue.getValue(), 0.0000001d);
                assertEquals(StatUtils.min(c), bst.getMinNode()._nodeValue.getValue(), 0.0000001d);
                assertTrue(bst.remove(A.get(i)));
            }

            assertNull(bst.getMaxNode());
            assertNull(bst.getMinNode());
            assertNull(bst.getRoot());
            assertEquals(0, bst.getTN_Map().size());
            assertEquals(0, bst.getHeight());
        }
    }

    /**
     * Test 16. Massive examination.
     */
    @Test
    void test16()
    {
        int trials = 1000;
        int items = 1000;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < trials; t++)
        {
            ArrayList<Double> tests = new ArrayList<>(items);
            for (int i = 0; i < items; i++) tests.add(R.nextDouble());

            BST bst = new BST(items, true);
            for (int i = 0; i < items; i++) bst.insert(new TestObject(tests.get(i), i));

            tests.sort(Double::compareTo);

            {
                TreeNode TN = bst.getMaxNode();
                int pnt = tests.size() - 1;
                assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);

                while ((TN = bst.getInorderPredecessor(TN)) != null)
                    assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);
            }
            {
                TreeNode TN = bst.getMinNode();
                int pnt = 0;
                assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);

                while ((TN = bst.getInorderSuccessor(TN)) != null)
                    assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);
            }

        }
    }


    /**
     * Test 17. Self-balancing tree. Simple case (insertion).
     */
    @Test
    void test17()
    {
        {
            BST bst = new BST(4, false);
            double[] data = new double[]{3, 21, 32, 15};

            bst.insert(new TestObject(data[0], 0));
            bst.insert(new TestObject(data[1], 1));
            bst.insert(new TestObject(data[2], 2));
            bst.insert(new TestObject(data[3], 3));

            assertEquals(3, bst.getHeight());

            assertNull(bst.getRoot()._parent);
            assertEquals(new TestObject(data[0], 0), bst.getRoot()._nodeValue);
            assertEquals(new TestObject(data[1], 1), bst.getRoot()._rightChildren._nodeValue);
            assertNull(bst.getRoot()._leftChildren);

            assertEquals(new TestObject(data[2], 2), bst.getRoot()._rightChildren._rightChildren._nodeValue);
            assertNull(bst.getRoot()._rightChildren._rightChildren._leftChildren);
            assertNull(bst.getRoot()._rightChildren._rightChildren._rightChildren);
            assertEquals(new TestObject(data[1], 1), bst.getRoot()._rightChildren._rightChildren._parent._nodeValue);

            assertEquals(new TestObject(data[3], 3), bst.getRoot()._rightChildren._leftChildren._nodeValue);
            assertNull(bst.getRoot()._rightChildren._leftChildren._leftChildren);
            assertNull(bst.getRoot()._rightChildren._leftChildren._rightChildren);
            assertEquals(new TestObject(data[1], 1), bst.getRoot()._rightChildren._leftChildren._parent._nodeValue);
        }

        {
            BST bst = new BST(5, true);
            double[] data = new double[]{3, 21, 32, 15, 10};

            bst.insert(new TestObject(data[0], 0));
            bst.insert(new TestObject(data[1], 1));
            bst.insert(new TestObject(data[2], 2));
            bst.insert(new TestObject(data[3], 3));
            bst.insert(new TestObject(data[4], 4));

            assertEquals(3, bst.getHeight());

            assertNull(bst.getRoot()._parent);
            assertEquals(new TestObject(data[1], 1), bst.getRoot()._nodeValue);
            assertEquals(new TestObject(data[4], 4), bst.getRoot()._leftChildren._nodeValue);
            assertEquals(new TestObject(data[2], 2), bst.getRoot()._rightChildren._nodeValue);

            assertNull(bst.getRoot()._rightChildren._leftChildren);
            assertNull(bst.getRoot()._rightChildren._rightChildren);

            assertEquals(bst.getRoot()._nodeValue, bst.getRoot()._leftChildren._parent._nodeValue);
            assertEquals(bst.getRoot()._nodeValue, bst.getRoot()._rightChildren._parent._nodeValue);
            assertEquals(new TestObject(data[0], 0), bst.getRoot()._leftChildren._leftChildren._nodeValue);
            assertEquals(new TestObject(data[3], 3), bst.getRoot()._leftChildren._rightChildren._nodeValue);

            assertNull(bst.getRoot()._leftChildren._leftChildren._leftChildren);
            assertNull(bst.getRoot()._leftChildren._leftChildren._rightChildren);

            assertNull(bst.getRoot()._leftChildren._rightChildren._leftChildren);
            assertNull(bst.getRoot()._leftChildren._rightChildren._rightChildren);

            assertTrue(bst.getRoot()._color);
            assertTrue(bst.getRoot()._rightChildren._color);
            assertTrue(bst.getRoot()._leftChildren._color);
            assertFalse(bst.getRoot()._leftChildren._leftChildren._color);
            assertFalse(bst.getRoot()._leftChildren._rightChildren._color);

            assertTrue(bst.areColorsValid());
            bst.printTreeColors();
        }
    }


    /**
     * Test 18. Self-balancing tree. Simple case (insertion).
     */
    @Test
    void test18()
    {
        BST bst = new BST(6, true);
        double[] data = new double[]{5, 4, 3, 6, 2, 2.5};

        bst.insert(new TestObject(data[0], 0));
        bst.insert(new TestObject(data[1], 1));
        bst.insert(new TestObject(data[2], 2));
        bst.insert(new TestObject(data[3], 3));
        bst.insert(new TestObject(data[4], 4));
        bst.insert(new TestObject(data[5], 5));

        assertNull(bst.getRoot()._parent);
        assertEquals(new TestObject(data[1], 1), bst.getRoot()._nodeValue);
        assertEquals(new TestObject(data[5], 5), bst.getRoot()._leftChildren._nodeValue);
        assertEquals(new TestObject(data[0], 0), bst.getRoot()._rightChildren._nodeValue);

        assertEquals(bst.getRoot()._nodeValue, bst.getRoot()._leftChildren._parent._nodeValue);
        assertEquals(bst.getRoot()._nodeValue, bst.getRoot()._rightChildren._parent._nodeValue);

        assertEquals(new TestObject(data[4], 4), bst.getRoot()._leftChildren._leftChildren._nodeValue);
        assertEquals(new TestObject(data[2], 2), bst.getRoot()._leftChildren._rightChildren._nodeValue);
        assertEquals(new TestObject(data[5], 5), bst.getRoot()._leftChildren._leftChildren._parent._nodeValue);
        assertEquals(new TestObject(data[5], 5), bst.getRoot()._leftChildren._rightChildren._parent._nodeValue);

        assertNull(bst.getRoot()._leftChildren._leftChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._leftChildren._rightChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren._leftChildren);
        assertNull(bst.getRoot()._leftChildren._rightChildren._rightChildren);

        assertEquals(new TestObject(data[0], 0), bst.getRoot()._rightChildren._rightChildren._parent._nodeValue);
        assertNull(bst.getRoot()._rightChildren._leftChildren);
        assertEquals(new TestObject(data[3], 3), bst.getRoot()._rightChildren._rightChildren._nodeValue);
        assertNull(bst.getRoot()._rightChildren._rightChildren._leftChildren);
        assertNull(bst.getRoot()._rightChildren._rightChildren._rightChildren);

        assertTrue(bst.getRoot()._color);
        assertTrue(bst.getRoot()._rightChildren._color);
        assertTrue(bst.getRoot()._leftChildren._color);
        assertFalse(bst.getRoot()._rightChildren._rightChildren._color);
        assertFalse(bst.getRoot()._leftChildren._leftChildren._color);
        assertFalse(bst.getRoot()._leftChildren._rightChildren._color);

        bst.printTreeColors();
        assertTrue(bst.areColorsValid());
    }


    /**
     * Test 19. Self-balancing tree. Simple case (removal).
     */
    @Test
    void test19()
    {
        BST bst = new BST(6, true);
        double[] data = new double[]{5, 4, 3, 6, 2, 2.5};

        ArrayList<TestObject> objs = new ArrayList<>(6);
        for (int i = 0; i < 6; i++)
        {
            TestObject to = new TestObject(data[i], i);
            objs.add(to);
            assertTrue(bst.insert(to));
            assertTrue(bst.areColorsValid());
        }

        for (int i = 0; i < 6; i++)
        {
            assertTrue(bst.remove(objs.get(i)));
            assertTrue(bst.areColorsValid());
            bst.printTreeColors();
        }
    }

    /**
     * Test 20. Massive. Self-balancing tree. Simple case (removal).
     */
    @Test
    void test20()
    {
        int trials = 200;
        int items = 200;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < trials; t++)
        {
            ArrayList<Double> tests = new ArrayList<>(items);
            for (int i = 0; i < items; i++) tests.add(R.nextDouble());

            BST bst = new BST(items, true);
            for (int i = 0; i < items; i++)
            {
                assertTrue(bst.insert(new TestObject(tests.get(i), i)));
                assertTrue(bst.areColorsValid());
            }

            for (int i = 0; i < items; i++)
            {
                assertTrue(bst.remove(new TestObject(tests.get(i), i)));
                assertTrue(bst.areColorsValid());
            }
        }

    }

    /**
     * Test 21 (comparison with TreeSort).
     */
    @Test
    void test21()
    {
        int trials = 100;
        int items = 100000;

        IRandom R = new MersenneTwister64(1);

        double[] addTimesBST = new double[trials];
        double[] addTimesBSTb = new double[trials];
        double[] addTimesTS = new double[trials];
        double[] findTimesBST = new double[trials];
        double[] findTimesBSTb = new double[trials];
        double[] findTimesTS = new double[trials];
        double[] heightBST = new double[trials];
        double[] heightBSTb = new double[trials];
        double[] removeTimesBST = new double[trials];
        double[] removeTimesBSTb = new double[trials];
        double[] removeTimesTS = new double[trials];


        for (int t = 0; t < trials; t++)
        {
            ArrayList<Double> tests = new ArrayList<>(items);
            for (int i = 0; i < items; i++)
            {
                tests.add(R.nextDouble());
                //tests.add((double) i);
            }

            //  for (Double d: tests)
            //     System.out.println(d);


            // ADD =================================================================
            long startTime = System.currentTimeMillis();
            BST bst = new BST(items, false);
            for (int i = 0; i < items; i++) bst.insert(new TestObject(tests.get(i), i));
            addTimesBST[t] = System.currentTimeMillis() - startTime;
            // ADD =================================================================
            startTime = System.currentTimeMillis();
            BST bstb = new BST(items, true);
            for (int i = 0; i < items; i++)
                bstb.insert(new TestObject(tests.get(i), i));
            addTimesBSTb[t] = System.currentTimeMillis() - startTime;

            assertTrue(bstb.areColorsValid());
            // ADD =================================================================
            startTime = System.currentTimeMillis();
            TreeSet<Double> ts = new TreeSet<>();
            for (int i = 0; i < items; i++) ts.add(tests.get(i));
            addTimesTS[t] = System.currentTimeMillis() - startTime;

            // comparison
            {
                tests.sort(Double::compareTo);
                {
                    TreeNode TN = bst.getMaxNode();
                    int pnt = tests.size() - 1;
                    assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);

                    while ((TN = bst.getInorderPredecessor(TN)) != null)
                        assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);
                }
                {
                    TreeNode TN = bst.getMinNode();
                    int pnt = 0;
                    assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);

                    while ((TN = bst.getInorderSuccessor(TN)) != null)
                        assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);
                }

                {
                    TreeNode TN = bstb.getMaxNode();
                    int pnt = tests.size() - 1;
                    assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);

                    while ((TN = bstb.getInorderPredecessor(TN)) != null)
                        assertEquals(tests.get(pnt--), TN._nodeValue.getValue(), 0.001d);
                }
                {
                    TreeNode TN = bstb.getMinNode();
                    int pnt = 0;
                    assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);

                    while ((TN = bstb.getInorderSuccessor(TN)) != null)
                        assertEquals(tests.get(pnt++), TN._nodeValue.getValue(), 0.001d);
                }
            }


            // FIND =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++)
            {
                Object o = bst.getTN_Map().get(new TestObject(tests.get(i), i));
                assertNotNull(o);
            }
            findTimesBST[t] = System.currentTimeMillis() - startTime;
            // FIND =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++)
            {
                Object o = bstb.getTN_Map().get(new TestObject(tests.get(i), i));
                assertNotNull(o);
            }
            findTimesBSTb[t] = System.currentTimeMillis() - startTime;
            // FIND =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++)
            {
                Object o = ts.descendingSet().tailSet(tests.get(i)).iterator();
                assertNotNull(o);
            }
            findTimesTS[t] = System.currentTimeMillis() - startTime;

            // HEIGHT =================================================================
            heightBST[t] = bst.getHeight();
            heightBSTb[t] = bstb.getHeight();


            // REMOVE =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++) bst.remove(new TestObject(tests.get(i), i));
            removeTimesBST[t] = System.currentTimeMillis() - startTime;
            // REMOVE =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++) bstb.remove(new TestObject(tests.get(i), i));
            removeTimesBSTb[t] = System.currentTimeMillis() - startTime;
            // REMOVE =================================================================
            startTime = System.currentTimeMillis();
            for (int i = 0; i < items; i++) ts.remove(tests.get(i));
            removeTimesTS[t] = System.currentTimeMillis() - startTime;

        }

        double averageAddTimesBST = StatUtils.mean(addTimesBST);
        double averageAddTimesBSTb = StatUtils.mean(addTimesBSTb);
        double averageAddTimesTS = StatUtils.mean(addTimesTS);
        System.out.println("Add times = " + averageAddTimesBST + " vs " + averageAddTimesBSTb + " vs " + averageAddTimesTS);

        double averageFindTimesBST = StatUtils.mean(findTimesBST);
        double averageFindTimesBSTb = StatUtils.mean(findTimesBSTb);
        double averageFindTimesTS = StatUtils.mean(findTimesTS);
        System.out.println("Search times = " + averageFindTimesBST + " vs " + averageFindTimesBSTb + " vs " + averageFindTimesTS);


        double averageHeightBST = StatUtils.mean(heightBST);
        double averageHeightBSTb = StatUtils.mean(heightBSTb);
        System.out.println("Average height = " + averageHeightBST + " vs " + averageHeightBSTb);


        double averageRemoveTimesBST = StatUtils.mean(removeTimesBST);
        double averageRemoveTimesBSTb = StatUtils.mean(removeTimesBSTb);
        double averageRemoveTimesTS = StatUtils.mean(removeTimesTS);
        System.out.println("Remove times = " + averageRemoveTimesBST + " vs " + averageRemoveTimesBSTb + " vs " + averageRemoveTimesTS);
    }

}