package y2025.SoftwareX_JECDM;

import criterion.Criteria;
import ea.AbstractEA;
import ea.EATimestamp;
import ea.IEA;
import exception.EAException;
import random.IRandom;
import random.L32_X64_MIX;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for jMetal's NSGA-II implementation. Note that it attempts to access an external jar. The jar is not
 * provided as part of this framework. Its default executable method instantiates NSGA-II to solve a DTLZ2 algorithm.
 * As for the input (args[]), three parameters must be supplied: the number of objectives considered, the population
 * size, and the number of generations. The program returns the total execution time in ms (console output).
 * IMPORTANT: If using your own external jar, please provide a valid path to it in the class constructor.
 *
 * @author MTomczyk
 */
public class JMETAL extends AbstractEA implements IEA
{
    /**
     * Tests the wrapper.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int M = 2;
        int generations = 100;
        int populationSize = 100;
        JMETAL jmetal = new JMETAL(M, populationSize, generations, new L32_X64_MIX(0));
        try
        {
            jmetal.init();
            for (int i = 1; i < generations; i++) jmetal.step(new EATimestamp(i, 0));
            System.out.println(jmetal.getExecutionTime());
        } catch (EAException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * Params container.
     */
    public static class Params extends AbstractEA.Params
    {

        /**
         * Parameterized constructor.
         *
         * @param name                  name of the EA
         * @param id,                   unique ID of the EA
         * @param R                     random number generator.
         * @param computeExecutionTimes flag indicating whether the total execution time (as well as other
         *                              implementation-dependent times) are measured or not (in ms)
         * @param criteria              considered criteria
         */
        protected Params(String name, int id, IRandom R, boolean computeExecutionTimes, Criteria criteria)
        {
            super(name, id, R, computeExecutionTimes, criteria);
        }
    }

    /**
     * Process builder (executes the jar).
     */
    private final ProcessBuilder _processBuilder;


    /**
     * Parameterized constructor.
     *
     * @param M              the number of objectives considered
     * @param populationSize the population size
     * @param generations    the number of generations the algorithm is supposed to run for
     * @param R              random number generator
     */
    public JMETAL(int M, int populationSize, int generations, IRandom R)
    {
        super(new Params("NSGAII_JMETAL", 0, R, true,
                Criteria.constructCriteria("C", M, false)));

        String path = "D:" + File.separatorChar + "experiments" + File.separatorChar + "2025" + File.separatorChar
                + "SoftwareX_JECDM" + File.separatorChar + "JMETAL_NSGAII.jar";
        _processBuilder = new ProcessBuilder("java", "-jar", path, String.valueOf(M),
                String.valueOf(populationSize), String.valueOf(generations));
        _processBuilder.redirectErrorStream(true);
    }

    /**
     * This implementation executes the whole evolutionary process and stores the execution time in a dedicated field.
     * The {@link JMETAL#step(EATimestamp)} method has no effect.
     *
     * @throws EAException an exception can be thrown and propagated higher
     */
    @Override
    public void init() throws EAException
    {
        Process process;
        try
        {
            process = _processBuilder.start();
        } catch (IOException e)
        {
            throw new EAException("Could not start the process", null, this.getClass());
        }

        try (InputStream inputStream = process.getInputStream())
        {
            byte[] output = inputStream.readAllBytes();
            String sTime = new String(output);
            sTime = sTime.substring(0, sTime.length() - 1);
            setExecutionTime(Double.parseDouble(sTime.substring(0, sTime.length() - 1)));
        } catch (IOException e)
        {
            throw new EAException("Could not retrieve the output", null, this.getClass());
        }

        int exitCode;
        try
        {
            exitCode = process.waitFor();
            if (exitCode != 0) throw new EAException("The exit code is not zero", null, this.getClass());
        } catch (InterruptedException e)
        {
            throw new EAException("Error occurred when retrieving the exit code", null, this.getClass());
        }
    }

    /**
     * This method has no effect
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException never thrown
     */
    @Override
    public void step(EATimestamp timestamp) throws EAException
    {

    }

    /**
     * Returns the EA's name.
     *
     * @return Returns JMETAl.
     */
    @Override
    public String getName()
    {
        return "JMETAl";
    }
}
