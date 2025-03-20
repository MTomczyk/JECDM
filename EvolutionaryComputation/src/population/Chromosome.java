package population;

/**
 * Class representing a solution in the genetic/decision/phenotypic/problem-specific space.
 * It is a genes wrapper ({@link population.Gene}).
 *
 * @author MTomczyk
 */


public class Chromosome
{
    /**
     * Vector of genes ({@link population.Gene}).
     */
    public Gene[] _genes;

    /**
     * Default constructor.
     */
    public Chromosome()
    {

    }

    /**
     * Parameterized constructor. Instantiates one gene and assigns integers.
     *
     * @param v integers
     */
    public Chromosome(int[] v)
    {
        _genes = new Gene[]{new Gene(v)};
    }

    /**
     * Parameterized constructor. Instantiates one gene and assigns doubles.
     *
     * @param v doubles
     */
    public Chromosome(double[] v)
    {
        _genes = new Gene[]{new Gene(v)};
    }

    /**
     * Parameterized constructor. Instantiates one gene and assigns booleans.
     *
     * @param v doubles
     */
    public Chromosome(boolean[] v)
    {
        _genes = new Gene[]{new Gene(v)};
    }

    /**
     * Parameterized constructor. Passes and stores one gene object.
     *
     * @param gene a gene object
     */
    public Chromosome(Gene gene)
    {
        _genes = new Gene[]{gene};
    }

    /**
     * Parameterized constructor. Passes and stores vector of genes.
     *
     * @param genes vector of genes
     */
    public Chromosome(Gene[] genes)
    {
        _genes = genes;
    }

    /**
     * Retrieves doubles from the first gene object.
     *
     * @return doubles from the first gene object
     */
    public double[] getDoubles()
    {
        return _genes[0]._dv;
    }

    /**
     * Retrieves doubles from the specified gene object.
     *
     * @param gene gene index
     * @return doubles from the specified gene object
     */
    public double[] getDoubles(int gene)
    {
        return _genes[gene]._dv;
    }

    /**
     * Retrieves ints from the first gene object.
     *
     * @return ints from the first gene object
     */
    public int[] getIntegers()
    {
        return _genes[0]._iv;
    }

    /**
     * Retrieves ints from the specified gene object.
     *
     * @param gene gene index
     * @return ints from the specified gene object
     */
    public int[] getIntegers(int gene)
    {
        return _genes[gene]._iv;
    }

    /**
     * Retrieves booleans from the first gene object.
     *
     * @return booleans from the first gene object
     */
    public boolean[] getBooleans()
    {
        return _genes[0]._bv;
    }

    /**
     * Retrieves booleans from the specified gene object.
     *
     * @param gene gene index
     * @return booleans from the specified gene object
     */
    public boolean[] getBooleans(int gene)
    {
        return _genes[gene]._bv;
    }

    /**
     * Constructs a deep copy of the object.
     *
     * @return constructed deep copy
     */
    public Chromosome getClone()
    {
        Gene[] genes = null;
        if (_genes != null)
        {
            genes = new Gene[_genes.length];
            for (int i = 0; i < genes.length; i++) genes[i] = _genes[i].getClone();
        }
        return new Chromosome(genes);
    }

    /**
     * Checks if the object is same as the other one based on their contents.
     *
     * @param c       the other chromosome
     * @param epsilon epsilon-tolerance when comparing doubles (can be null)
     * @return true = chromosomes are equal; false = otherwise
     */
    public boolean isEqual(Chromosome c, Double epsilon)
    {
        if ((_genes == null) && (c._genes == null)) return true;
        if ((_genes == null) || (c._genes == null)) return true;
        if (_genes.length != c._genes.length) return false;

        for (int i = 0; i < _genes.length; i++)
        {
            if (!_genes[i].isEqual(c._genes[i], epsilon))
                return false;
        }
        return true;
    }

    /**
     * Prints information on the chromosome (genes).
     */
    public void print()
    {
        if (_genes != null)
            for (int i = 0; i < _genes.length; i++)
            {
                System.out.println("gene " + i);
                _genes[i].print();
            }
    }
}
