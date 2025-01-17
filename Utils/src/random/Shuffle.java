package random;

import java.util.ArrayList;

/**
 * Generic class for shuffling objects stored in arrays.
 *
 * @author MTomczyk
 */


public class Shuffle<T>
{
    /**
     * Random number generator.
     */
    private final IRandom _rg;

    /**
     * Default constructor.
     */
    public Shuffle()
    {
        this(null);
    }

    /**
     * Parameterized constructor.
     *
     * @param rg random number generator
     */
    public Shuffle(IRandom rg)
    {
        _rg = rg;
    }

    /**
     * Shuffles objects stored in an array (uniform distribution).
     *
     * @param input input array
     */
    public void shuffle(ArrayList<T> input)
    {
        for (int i = input.size() - 1; i >= 0; i--)
        {
            int p = _rg.nextInt(i + 1);
            T tmp = input.get(p);
            for (int j = p; j < i; j++)
                input.set(j, input.get(j + 1));
            input.set(i, tmp);
        }
    }

    /**
     * Shuffles objects stored in an array (uniform distribution).
     *
     * @param input input array
     */
    public void shuffle(T[] input)
    {
        for (int i = input.length - 1; i >= 0; i--)
        {
            int p = _rg.nextInt(i + 1);
            T tmp = input[p];
            System.arraycopy(input, p + 1, input, p, i - p);
            input[i] = tmp;
        }
    }

    /**
     * Shuffles objects stored in an array (uniform distribution).
     *
     * @param input input array
     */
    public void shuffle(int[] input)
    {
        shuffle(input, _rg);
    }

    /**
     * Shuffles objects stored in an array (uniform distribution).
     *
     * @param input input array
     * @param rg    random number generator
     */
    public void shuffle(int[] input, IRandom rg)
    {
        for (int i = input.length - 1; i >= 0; i--)
        {
            int p = rg.nextInt(i + 1);
            int tmp = input[p];
            System.arraycopy(input, p + 1, input, p, i - p);
            input[i] = tmp;
        }
    }
}
