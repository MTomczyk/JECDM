package population;

import java.util.Objects;

/**
 * Class representing a unique ID that can be assigned to a specimen ({@link population.Specimen}).
 *
 * @author MTomczyk
 */

public class SpecimenID
{
    /**
     * ID of the algorithm that constructed a specimen.
     */
    public final int _geneticID;

    /**
     * Generation indicating when the specimen was constructed.
     */
    public final int _generation;

    /**
     * Iteration when the specimen was constructed when the method is run in the steady-state mode.
     */
    public final int _steadyStateRepeat;

    /**
     * The unique number assigned to a solution (individual per generation).
     */
    public final int _no;

    /**
     * Hash code determined based on the specimen ID.
     */
    public final int _hashCode;

    /**
     * Parameterized constructor.
     *
     * @param no The unique number assigned to a solution (individual per generation)
     */
    public SpecimenID(int no)
    {
        this(0, 0, 0, no);
    }

    /**
     * Parameterized constructor.
     *
     * @param geneticID         ID of the algorithm that constructed a specimen
     * @param generation        Generation indicating when the specimen was constructed
     * @param steadyStateRepeat Iteration when the specimen was constructed when the method is run in the steady-state mode
     * @param no                The unique number assigned to a solution (individual per generation)
     */
    public SpecimenID(int geneticID, int generation, int steadyStateRepeat, int no)
    {
        _geneticID = geneticID;
        _generation = generation;
        _steadyStateRepeat = steadyStateRepeat;
        _no = no;
        _hashCode = Objects.hash(_no, _generation, _geneticID, _steadyStateRepeat);
    }

    /**
     * Constructs a string representing when and who (algorithm) constructed the specimen.
     *
     * @return the constructed string
     */
    @Override
    public String toString()
    {
        return "[EA id = " + _geneticID + ", generation = " + _generation + ", steady-state repeat = " + _steadyStateRepeat + ", no. = " + _no + "]";
    }

    /**
     * Constructs a deep copy of the object.
     *
     * @return the constructed copy
     */
    public SpecimenID getClone()
    {
        return new SpecimenID(_geneticID, _generation, _steadyStateRepeat, _no);
    }

    /**
     * Checks if this object is the same as the other one provided.
     *
     * @param id id of the other specimen
     * @return true = both ids are the same; false = otherwise
     */
    public boolean isEqual(SpecimenID id)
    {
        if (_geneticID != id._geneticID) return false;
        if (_generation != id._generation) return false;
        if (_steadyStateRepeat != id._steadyStateRepeat) return false;
        return _no == id._no;
    }
}
