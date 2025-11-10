package alternative;

import criterion.Criterion;
import space.Vector;

import java.util.ArrayList;

/**
 * This class represents an alternative.
 *
 * @author MTomczyk
 */
public class Alternative implements IAlternativeWrapper
{
    /**
     * Name of the alternative.
     */
    private String _name;

    /**
     * Performance vector.
     */
    private double[] _performance;

    /**
     * Vector of auxiliary scores attained by the alternative.
     */
    private double[] _auxScores;

    /**
     * Parameterized constructor. The performance vector is instantiated as a zero vector of length equal to the number
     * of criteria.
     *
     * @param name     the name of the alternative
     * @param criteria the number of criteria
     */
    public Alternative(String name, int criteria)
    {
        this(name, new double[criteria], null);
    }

    /**
     * Parameterized constructor. The performance vector is set as provided (copy by reference).
     *
     * @param name        name of the alternative
     * @param performance performance vector
     */
    public Alternative(String name, double[] performance)
    {
        this(name, performance, null);
    }

    /**
     * Parameterized constructor. The performance vector is set as provided (copy by reference).
     *
     * @param name        name of the alternative
     * @param performance performance vector
     * @param auxScores   array of auxiliary scores that can be linked to the alternative
     */
    public Alternative(String name, double[] performance, double[] auxScores)
    {
        _name = name;
        _performance = performance;
        _auxScores = auxScores;
    }

    /**
     * Construct a deep copy of the object.
     *
     * @return a deep copy of the object
     */
    public Alternative getClone()
    {
        return new Alternative(getName(), getPerformanceVector().clone(), _auxScores.clone());
    }

    /**
     * Creates an array of length of n of alternatives (objects).
     *
     * @param prefixName common prefix for names of all alternatives; e.g., if prefixName = "A", then the names are
     *                   "A0", "A1", "A2", and so on.
     * @param n          the number of alternatives to create
     * @param criteria   the number of criteria
     * @return a vector of length n of alternatives
     */
    public static ArrayList<Alternative> getAlternativeArray(String prefixName, int n, int criteria)
    {
        return getAlternativeArray(prefixName, n, criteria, 0);
    }

    /**
     * Creates an array of length of n of alternatives (objects).
     *
     * @param prefixName            common prefix for names of all alternatives; e.g., if prefixName = "A", then the
     *                              names are "A" + suffixStartingCounter, "A" + (suffixStartingCounter + 1),  "A" +
     *                              (suffixStartingCounter + 2), and so on
     * @param n                     the number of alternatives to create (at least 0)
     * @param criteria              the number of criteria
     * @param suffixStartingCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                              alternative will be named "A1", and so on)
     * @return a vector of length n of alternatives (null, if the input data is invalid)
     */
    public static ArrayList<Alternative> getAlternativeArray(String prefixName, int n, int criteria, int suffixStartingCounter)
    {
        if (n < 0) return null;
        if (criteria < 0) return null;
        ArrayList<Alternative> result = new ArrayList<>(n);
        for (int i = suffixStartingCounter; i < n + suffixStartingCounter; i++)
            result.add(new Alternative(String.format("%s%d", prefixName, i), criteria));
        return result;
    }

    /**
     * Creates an alternatives array using an evaluation matrix (each row is linked to a different alternative).
     *
     * @param prefixName common prefix for names of all alternatives; e.g., if prefixName = "A", then the names are
     *                   "A0", "A1", "A2", and so on
     * @param e          evaluation matrix
     * @return a vector of length n of alternatives
     */
    public static ArrayList<Alternative> getAlternativeArray(String prefixName, double[][] e)
    {
        return getAlternativeArray(prefixName, e, 0);
    }

    /**
     * Creates an alternatives array using an evaluation matrix (each row is linked to a different alternative).
     *
     * @param prefixName           common prefix for names of all alternatives; e.g., if prefixName = "A", then the
     *                             names are "A" + suffixStartingCounter, "A" + (suffixStartingCounter + 1),  "A" +
     *                             (suffixStartingCounter + 2), and so on
     * @param e                    evaluation matrix
     * @param suffixStarterCounter suffix starting counter for the names of alternatives (e.g., if = 1, the first
     *                             alternative will be named "A1", and so on)
     * @return a vector of length n of alternatives (null, if the input data is invalid, e.g., when e == null)
     */
    public static ArrayList<Alternative> getAlternativeArray(String prefixName, double[][] e, int suffixStarterCounter)
    {
        if (e == null) return null;
        ArrayList<Alternative> result = new ArrayList<>(e.length);
        int idx = 0;
        for (int i = suffixStarterCounter; i < e.length + suffixStarterCounter; i++)
        {
            if (e[idx] == null) return null;
            result.add(new Alternative(String.format("%s%d", prefixName, i), e[idx++]));
        }
        return result;
    }

    /**
     * Setter for performance vector.
     *
     * @param doublePerformance performance vector
     */
    public void setPerformanceVector(double[] doublePerformance)
    {
        _performance = doublePerformance;
    }

    /**
     * Getter for the performance vector (important note: reference is returned, not a clone).
     *
     * @return performance vector (important note: reference is returned, not a clone)
     */
    @Override
    public double[] getPerformanceVector()
    {
        return _performance;
    }


    /**
     * Set k-th performance value.
     *
     * @param performance performance value
     * @param k           index
     */
    public void setPerformanceAt(double performance, int k)
    {
        _performance[k] = performance;
    }

    /**
     * Getter for the k-th performance value.
     *
     * @param k index
     * @return k-th performance value
     */
    public double getPerformanceAt(int k)
    {
        return _performance[k];
    }

    /**
     * Getter for the auxiliary scores (important note: reference is returned, not a clone).
     *
     * @return vector of auxiliary scores (important note: reference is returned, not a clone)
     */
    public double[] getAuxScores()
    {
        return _auxScores;
    }

    /**
     * Getter for the k-th auxiliary score.
     *
     * @param k index
     * @return k-th auxiliary score
     */
    public double getAuxScoreAt(int k)
    {
        return _auxScores[k];
    }

    /**
     * Getter for the 0-th auxiliary score.
     *
     * @return k-th auxiliary score
     */
    public double getAuxScore()
    {
        return _auxScores[0];
    }

    /**
     * Setter for auxiliary scores.
     *
     * @param auxScores auxiliary scores (copy by reference)
     */
    public void setAuxScores(double[] auxScores)
    {
        _auxScores = auxScores;
    }

    /**
     * Setter for the auxiliary scores. If they are nulled, the method instantiates them as a 1-element vector
     * containing the auxScore value.
     *
     * @param auxScore auxiliary score to be stored
     */
    public void setAuxScore(double auxScore)
    {
        if (_auxScores == null) setAuxScores(new double[]{auxScore});
        else if (_auxScores.length != 1) setAuxScores(new double[]{auxScore});
        else _auxScores[0] = auxScore;
    }

    /**
     * Getter for the name of the alternative.
     *
     * @return name of the alternative
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Setter for alternative name.
     *
     * @param name name of the alternative
     */
    public void setName(String name)
    {
        _name = name;
    }


    /**
     * Prints information on the alternative.
     * Additional info is displayed if the criteria vector is provided (can be null).
     *
     * @param criteria vector of criteria
     */
    public void printInfoOnAlternatives(Criterion[] criteria)
    {
        printInfoOnAlternative(16, criteria);
    }

    /**
     * Prints information on the alternative.
     *
     * @param decimalPrecision decimal precision (for floats)
     */
    public void printInfoOnAlternatives(int decimalPrecision)
    {
        printInfoOnAlternative(decimalPrecision, null);
    }

    /**
     * Returns the default string representation of the alternative (viewed as name).
     *
     * @return string representation
     */
    public String toString()
    {
        return _name;
    }

    /**
     * Prints information on the alternative.
     * Additional info is displayed if the criteria vector is provided (can be null).
     *
     * @param decimalPrecision decimal precision
     * @param criteria         criteria (can be null)
     */
    public void printInfoOnAlternative(int decimalPrecision, Criterion[] criteria)
    {
        System.out.println(getStringRepresentation(decimalPrecision, criteria));
    }

    /**
     * Returns the string representation of the alternative.
     * Additional info is displayed if the criteria are provided (can be null).
     *
     * @param decimalPrecision decimal precision
     * @param criteria         criteria (can be null)
     * @return string representation (info on the alternative)
     */
    public String getStringRepresentation(int decimalPrecision, Criterion[] criteria)
    {
        String ls = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("Alternative: ").append(_name).append(ls);
        sb.append("Performance: ").append(ls);
        sb.append(getPerformanceVectorAsString(_performance, criteria, decimalPrecision));

        if (_auxScores != null)
        {
            sb.append("Auxiliary scores: ").append(ls);
            sb.append(getPerformanceVectorAsString(_auxScores, null, decimalPrecision));
        }
        return sb.toString();
    }

    /**
     * Auxiliary method for constructing an information on the evaluations (string).
     *
     * @param vector           double vector to print
     * @param criteria         criteria
     * @param decimalPrecision decimal precision
     * @return string representation of the alternative's evaluations
     */
    private String getPerformanceVectorAsString(double[] vector, Criterion[] criteria, int decimalPrecision)
    {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();
        for (int c = 0; c < vector.length; c++)
        {
            if (criteria != null)
            {
                String rule = "   %s = %." + decimalPrecision + "f" + ls;
                sb.append(String.format(rule, criteria[c].getName(), vector[c]));
            }
            else
            {
                String rule = "   %d = %." + decimalPrecision + "f" + ls;
                sb.append(String.format(rule, c, vector[c]));
            }
        }
        return sb.toString();
    }

    /**
     * Method for comparing if two alternatives are the same.
     * IMPORTANT NOTE: based simply on comparing their names (String.equals), not performances.
     *
     * @param o the second object to be compared with
     * @return true = objects are the same (have the same names); false = otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Alternative)) return false;
        return _name.equals(((Alternative) o)._name);
    }

    /**
     * Auxiliary method for checking if the object has a performance vector that is identical to another alternative's
     * performance vector.
     *
     * @param alternative another alternative
     * @return true if both vectors are equal; false otherwise (even if some entity, e.g., another alternative, is null
     * while its counterpart not).
     */
    public boolean haveTheSamePerformanceAs(Alternative alternative)
    {
        if (alternative == null) return false;
        return Vector.areVectorsEqual(_performance, alternative._performance);
    }

    /**
     * Getter for the alternative (returns itself).
     *
     * @return alternative
     */
    @Override
    public Alternative getAlternative()
    {
        return this;
    }
}
