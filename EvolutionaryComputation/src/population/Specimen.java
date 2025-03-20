package population;

import alternative.Alternative;
import alternative.IAlternativeWrapper;

/**
 * Class representing a specimen.
 *
 * @author MTomczyk
 */

public class Specimen implements IAlternativeWrapper
{
    /**
     * Chromosome object used to represent a solution in the genetic/decision/phenotypic/problem-specific space.
     */
    protected Chromosome _chromosome;

    /**
     * ({@link alternative.Alternative}) object is primarily used to represent a solution in the objective space, but
     * also it provides a bridge between the DecisionSupport module and the EvolutionaryComputation module.
     */
    protected Alternative _alternative;

    /**
     * Unique ID assigned to a specimen.
     */
    protected SpecimenID _id;

    /**
     * Default constructor.
     */
    public Specimen()
    {

    }

    /**
     * Parameterized constructor. The id of the specimen is zeroed, while the number of criteria (objectives) is passed to the ({@link alternative.Alternative}) object.
     *
     * @param criteria the number of criteria (objectives) passed to the ({@link alternative.Alternative}) object
     */
    public Specimen(int criteria)
    {
        this(criteria, new SpecimenID(0, 0, 0, 0));
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria the number of criteria (objectives) passed to the ({@link alternative.Alternative}) object
     * @param id       the unique id assigned to the specimen
     */
    public Specimen(int criteria, SpecimenID id)
    {
        _alternative = new Alternative(id.toString(), criteria);
        _id = id;
    }

    /**
     * Parameterized constructor.
     *
     * @param id the unique id assigned to the specimen
     */
    public Specimen(SpecimenID id)
    {
        _id = id;
    }

    /**
     * Parameterized constructor.
     *
     * @param evaluations evaluation vector
     */
    public Specimen(double[] evaluations)
    {
        this(null, evaluations);
    }

    /**
     * Parameterized constructor.
     *
     * @param id          the unique id assigned to the specimen
     * @param evaluations evaluation vector
     */
    public Specimen(int id, double[] evaluations)
    {
        this(new SpecimenID(0, 0, 0, id), evaluations);
    }

    /**
     * Parameterized constructor.
     *
     * @param id          the unique id assigned to the specimen
     * @param evaluations evaluation vector
     */
    public Specimen(SpecimenID id, double[] evaluations)
    {
        String name = "";
        if (id != null) name = id.toString();
        _alternative = new Alternative(name, evaluations);
        _id = id;
    }


    /**
     * Constructs a deep copy of the specimen.
     *
     * @return constructed deep copy
     */
    public Specimen getClone()
    {
        Specimen specimen = new Specimen();
        if (_alternative != null) specimen.setAlternative(_alternative.getClone());
        if (_chromosome != null) specimen.setChromosome(_chromosome.getClone());
        if (_id != null) specimen.setID(_id.getClone());
        return specimen;
    }

    /**
     * Checks if this specimen has the same chromosome as the other specimen.
     *
     * @param s       specimen to be compared with
     * @param epsilon epsilon-tolerance when comparing doubles (can be null)
     * @return true = both specimens have the same chromosomes; false = otherwise
     */
    public boolean isChromosomeEqual(Specimen s, Double epsilon)
    {
        return _chromosome.isEqual(s.getChromosome(), epsilon);
    }

    /**
     * Prints info on the specimen object.
     */
    public void print()
    {
        System.out.println("Specimen: " + System.lineSeparator());
        if (_id != null) System.out.println("  id = " + _id);
        if (_chromosome != null) _chromosome.print();
    }

    /**
     * Print info on the specimen's evaluations (objective space).
     */
    public void printEvaluations()
    {
        printEvaluations(16);
    }

    /**
     * Print info on the specimen's evaluations (objective space).
     *
     * @param prec decimal precision when printing doubles
     */
    public void printEvaluations(int prec)
    {
        if (_id != null) System.out.println(_id + " = ");
        double[] e = _alternative.getPerformanceVector();
        for (int i = 0; i < e.length; i++)
        {
            String rule = "%." + prec + "f";
            if (i < e.length - 1) rule += " ";
            System.out.printf(String.format(rule, e[i]));
        }
        System.out.println();
    }

    /**
     * Getter for the double decision vector (stored in the first gene in the chromosome (default)).
     *
     * @return decision vector (doubles); if the decision vector is not instantiated, the method returns null;
     */
    public double[] getDoubleDecisionVector()
    {
        return getDoubleDecisionVector(0);
    }

    /**
     * Getter for the double decision vector (stored in the specified gene).
     *
     * @param gene gene index
     * @return decision vector (doubles); if the decision vector is not instantiated, the method returns null;
     */
    public double[] getDoubleDecisionVector(int gene)
    {
        if (_chromosome == null) return null;
        if (_chromosome._genes == null) return null;
        if (_chromosome._genes.length <= gene) return null;
        return _chromosome._genes[gene]._dv;
    }

    /**
     * Getter for the integer decision vector (stored in the first gene in the chromosome (default)).
     *
     * @return decision vector (integers); if the decision vector is not instantiated, the method returns null;
     */
    public int[] getIntDecisionVector()
    {
        return getIntDecisionVector(0);
    }

    /**
     * Getter for the integer decision vector (stored in the specified gene).
     *
     * @param gene gene index
     * @return decision vector (integers); if the decision vector is not instantiated, the method returns null;
     */
    public int[] getIntDecisionVector(int gene)
    {
        if (_chromosome == null) return null;
        if (_chromosome._genes == null) return null;
        return _chromosome._genes[gene]._iv;
    }


    /**
     * Getter for the boolean decision vector (stored in the first gene in the chromosome (default)).
     *
     * @return decision vector (booleans); if the decision vector is not instantiated, the method returns null;
     */
    public boolean[] getBooleanDecisionVector()
    {
        if (_chromosome == null) return null;
        if (_chromosome._genes == null) return null;
        if (_chromosome._genes.length == 0) return null;
        return _chromosome._genes[0]._bv;
    }

    /**
     * Getter for the boolean decision vector (stored in the specified gene).
     *
     * @param gene gene index
     * @return decision vector (booleans); if the decision vector is not instantiated, the method returns null;
     */
    public boolean[] getBooleanDecisionVector(int gene)
    {
        if (_chromosome == null) return null;
        if (_chromosome._genes == null) return null;
        if (_chromosome._genes.length <= gene) return null;
        return _chromosome._genes[gene]._bv;
    }


    /**
     * Setter for the chromosome.
     *
     * @param c chromosome object
     */
    public void setChromosome(Chromosome c)
    {
        _chromosome = c;
    }

    /**
     * Getter for the chromosome.
     *
     * @return chromosome object
     */
    public Chromosome getChromosome()
    {
        return _chromosome;
    }

    /**
     * Setter for the evaluation vector (objective space). The method terminates if the alternative object is not instantiated.
     *
     * @param evaluations evaluation vector (double performance vector)
     */
    public void setEvaluations(double[] evaluations)
    {
        if (_alternative == null) return;
        _alternative.setPerformanceVector(evaluations);
    }

    /**
     * Getter for the evaluation vector (objective space). The method returns null if the alternative object is not instantiated.
     *
     * @return evaluation vector (double performance vector)
     */
    public double[] getEvaluations()
    {
        if (_alternative == null) return null;
        return _alternative.getPerformanceVector();
    }

    /**
     * Getter for the performance vector (reference).
     *
     * @return performance vector (reference)
     */
    @Override
    public double[] getPerformanceVector()
    {
        return getEvaluations();
    }


    /**
     * Sets auxiliary score linked to the specimen.
     *
     * @param v auxiliary score
     */
    public void setAuxScore(double v)
    {
        _alternative.setAuxScore(v);
    }

    /**
     * Setter for the alternative.
     *
     * @param alternative alternative object
     */
    public void setAlternative(Alternative alternative)
    {
        _alternative = alternative;
    }

    /**
     * Getter for the alternative.
     *
     * @return alternative object
     */
    @Override
    public Alternative getAlternative()
    {
        return _alternative;
    }

    /**
     * Returns the wrapped alternative's name.
     *
     * @return alternative's name
     */
    public String getName()
    {
        return _alternative.getName();
    }


    /**
     * Setter for the specimen id.
     *
     * @param id specimen id
     */
    public void setID(SpecimenID id)
    {
        _id = id;
        if (getAlternative() != null) getAlternative().setName(id.toString());
    }

    /**
     * Getter for the specimen id.
     *
     * @return specimen id
     */
    public SpecimenID getID()
    {
        return _id;
    }

    /**
     * Overrides the hashCode() method. The hash is calculated based on the specimen id (which should be unique).
     *
     * @return calculated hash code.
     */
    @Override
    public int hashCode()
    {
        if (_id == null) return 0;
        return _id._hashCode;
    }

    /**
     * Overrides the equals method. The comparison is based on the specimen id only (e.g., the evaluation vectors can be the same accidentally).
     *
     * @param o other object (should be an instance of this class).
     * @return true = specimens are the same; false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Specimen s)) return false;
        return (_id.isEqual(s._id));
    }
}
