package population;


import print.PrintUtils;

/**
 *  Class representing a gene.
 *
 * @author MTomczyk
 */
public class Gene
{
    /**
     * Vector of doubles.
     */
    public double[] _dv;

    /**
     * Vector of integers.
     */
    public int[] _iv;

    /**
     * Vector of booleans.
     */
    public boolean[] _bv;

    /**
     * References to other genes (e.g., tree-like dependencies can be defined). Cyclic references should be avoided.
     */
    public Gene[] _gene;

    /**
     * Auxiliary variable that can be used when defining more complex dependencies ( = 0 by default).
     */
    public int _level;

    /**
     * Default constructor.
     */
    public Gene()
    {

    }

    /**
     * Parameterized constructor. Instantiates integers (copy by reference) and sets level to 0. The remaining fields are nulled.
     *
     * @param bv booleans
     */
    public Gene(boolean[] bv)
    {
        _level = 0;
        _bv = bv;
        _dv = null;
        _iv = null;
    }

    /**
     * Parameterized constructor. Instantiates integers (copy by reference) and sets level to 0. The remaining fields are nulled.
     *
     * @param iv integers
     */
    public Gene(int[] iv)
    {
        _level = 0;
        _iv = iv;
        _dv = null;
        _bv = null;
    }

    /**
     * Parameterized constructor. Instantiates doubles as a zero vector of length n and sets level to 0.
     *
     * @param n the number of doubles.
     */
    public Gene(int n)
    {
        this(new double[n], 0);
    }

    /**
     * Parameterized constructor. Instantiates doubles (copy by reference) and sets level to 0. The remaining fields are nulled.
     *
     * @param dv doubles
     */
    public Gene(double[] dv)
    {
        this(dv, 0);
    }

    /**
     * Parameterized constructor. Instantiates doubles (copy by reference) and sets level as requested. The remaining fields are nulled.
     *
     * @param dv    doubles
     * @param level gene level
     */
    public Gene(double[] dv, int level)
    {
        _dv = dv;
        _iv = null;
        _gene = null;
        _level = level;
    }

    /**
     * Checks if this object is equal to the other. The method thoroughly inspects all fields and checks their equality.
     * If there are graph-like dependencies between genes, the equality is verified recursively (avoid cycling dependencies).
     *
     * @param g       gene object to be compared with
     * @param epsilon precision (epsilon) considered when comparing doubles. Can be null.
     * @return true = both genes are equal; false = otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEqual(Gene g, Double epsilon)
    {
        if ((_dv == null) && (g._dv != null)) return false;
        if ((_dv != null) && (g._dv == null)) return false;
        if (_dv != null)
        {
            if (_dv.length != g._dv.length) return false;
            for (int i = 0; i < _dv.length; i++)
            {
                if (epsilon != null) if (Math.abs(_dv[i] - g._dv[i]) > epsilon) return false;
                else if (Double.compare(_dv[i], g._dv[i]) != 0) return false;
            }
        }

        if ((_iv == null) && (g._iv != null)) return false;
        if ((_iv != null) && (g._iv == null)) return false;
        if (_iv != null)
        {
            if (_iv.length != g._iv.length) return false;
            for (int i = 0; i < _iv.length; i++)
                if (_iv[i] != g._iv[i]) return false;
        }

        if ((_gene == null) && (g._gene != null)) return false;
        if ((_gene != null) && (g._gene == null)) return false;
        if (_gene != null)
        {
            if (_gene.length != g._gene.length) return false;
            for (int i = 0; i < _gene.length; i++)
                if (!_gene[i].isEqual(g._gene[i], epsilon)) return false;
        }

        return true;
    }


    /**
     * Constructs a deep copy of the object.
     *
     * @return constructed deep copy
     */
    public Gene getClone()
    {
        Gene ng = new Gene();
        if (_dv != null) ng._dv = _dv.clone();
        if (_iv != null) ng._iv = _iv.clone();
        ng._level = _level;
        if (_gene != null)
        {
            ng._gene = new Gene[_gene.length];
            for (int i = 0; i < _gene.length; i++)
                ng._gene[i] = _gene[i].getClone();
        }
        return ng;
    }

    /**
     * Prints info on the object.
     */
    public void print()
    {
        String d1 = PrintUtils.getDashes(_level + 1);
        if (_dv != null)
        {
            String d2 = PrintUtils.getVectorOfDoubles(_dv, 3);
            System.out.println(d1 + " " + d2);
        }

        if (_iv != null)
        {
            String d2 = PrintUtils.getVectorOfIntegers(_iv);
            System.out.println(d1 + " " + d2);
        }

        if (_gene != null)
            for (int i = 0; i < _gene.length; i++)
            {
                System.out.println(d1 + " gene " + i);
                _gene[i].print();
            }
    }
}
