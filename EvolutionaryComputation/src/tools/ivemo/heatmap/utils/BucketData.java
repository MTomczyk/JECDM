package tools.ivemo.heatmap.utils;

import datastructure.listarray.LinkedListArray;

/**
 * Container for bucket data (per trial). Used to store and provide test samples obtained for a bucket.
 *
 * @author MTomczyk
 */

public class BucketData
{
    /**
     * Linked list array data type is used for efficiency (hybrid list/array structure).
     */
    public LinkedListArray _LA;

    /**
     * Final value.
     */
    public double _value;

    /**
     * Parameterized constructor.Instantiates fields.
     *
     * @param inArraySize in-list fixed array size for the list-array hybrid data structure all bucket samples captured during the execution of an EA or when aggregating trial statistics.
     */
    public BucketData(int inArraySize)
    {
        _LA = new LinkedListArray(inArraySize);
        _value = Double.NEGATIVE_INFINITY;
    }
}
