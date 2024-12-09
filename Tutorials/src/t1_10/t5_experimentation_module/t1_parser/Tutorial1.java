package t1_10.t5_experimentation_module.t1_parser;

import executor.ExperimentPerformer;
import parser.Parser;
import print.PrintUtils;

/**
 * This tutorial shows how to set the console arguments that can be passed to the experiment executor
 * {@link ExperimentPerformer}; see the {@link parser.Parser} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("ExtractMethodRecommender")
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String [] args)
    {
        // Case #1
        System.out.println("Case 1:");
        {
            String [] a = new String[]{"THREADS=100",};

            Parser parser = new Parser();
            Parser.Result result = parser.parse(a);

            PrintUtils.printLines(result.generateLogLines());
        }
        // Case #2
        System.out.println("Case 2:");
        {
            String [] a = new String[]
                    {
                            "ThReADs=100",
                            "DISABLED_TRIALS=[0-20,25,75-99]"
                    };

            Parser parser = new Parser();
            Parser.Result result = parser.parse(a);

            PrintUtils.printLines(result.generateLogLines());
        }
        // Case #3
        System.out.println("Case 3:");
        {
            String [] a = new String[]
                    {
                            "Threads=10",
                            "ThReADs=50",
                            "DISABLED_TRIALS=[10]",
                            "DISABLED_TRIALS=[20]",
                            "SOMETHING_WRONG",
                            "ANOTHER_ERROR",
                            "DISABLED_SCENARIOS= [(KEY1:V11;KEY2:V23),(KEY1:V12)]",
                            "DISABLED_SCENARIOS=[(KEY4:V42;KEY3:V32),(KEY5:V51)]",
                    };

            Parser parser = new Parser();
            Parser.Result result = parser.parse(a);

            PrintUtils.printLines(result.generateLogLines());
        }
    }
}
