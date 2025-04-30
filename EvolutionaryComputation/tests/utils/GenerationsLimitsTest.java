package utils;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link GenerationsLimits} class.
 *
 * @author MTomczyk
 */
class GenerationsLimitsTest
{
    /**
     * Tests {@link GenerationsLimits#getLimit(String, int)} method.
     */
    @Test
    void getLimit()
    {
        {
            //noinspection ConstantValue
            assertNull(GenerationsLimits.getInstance(null));
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{});
            assertNull(GL);
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{null});
            assertNull(GL);
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit("ABC", 0, 0), null
            });
            assertNull(GL);
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit("DTLZ2", 100, 50)
            });
            assertNotNull(GL);
            assertNull(GL.getLimit("TEST", 0));
            assertEquals(150, GL.getLimit("DTLZ2", 3));
            assertEquals(150, GL.getLimit("dtlz2", 3));
            assertNull(GL.getLimit("dtlz1", 0));
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit(Pattern.compile("DTLZ2"), 100, 50)
            });
            assertNotNull(GL);
            assertNull(GL.getLimit("TEST", 1));
            assertEquals(150, GL.getLimit("DTLZ2", 3));
            assertNull(GL.getLimit("dtlz2", 3));
            assertNull(GL.getLimit("dtlz1", 1));
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit("DTLZ[1-7]", 100, 50)
            });
            assertNotNull(GL);
            assertNull(GL.getLimit("TEST", 1));
            assertEquals(150, GL.getLimit("DTLZ2", 3));
            assertEquals(150, GL.getLimit("dtlz2", 3));
            assertEquals(50, GL.getLimit("dtlz1", 1));
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit("^DTLZ", 100, 50)
            });
            assertNotNull(GL);
            assertNull(GL.getLimit("TEST", 1));
            assertEquals(150, GL.getLimit("DTLZ2", 3));
            assertEquals(150, GL.getLimit("dtlz2", 3));
            assertEquals(50, GL.getLimit("dtlz1", 1));
        }
        {
            GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                    new GenerationsLimits.ProblemLimit("DTLZ1|DTLZ[5-7]|WFG[2-9]", 400, 100),
                    new GenerationsLimits.ProblemLimit("DTLZ2|DTLZ4", 200, 50),
                    new GenerationsLimits.ProblemLimit("DTLZ3", 1000, 250),
                    new GenerationsLimits.ProblemLimit("WFG1ALPHA02", 1000, 200)
            });
            assertNotNull(GL);
            assertNull(GL.getLimit("TEST", 0));
            assertEquals(500, GL.getLimit("DTLZ1", 3));
            assertEquals(500, GL.getLimit("DTLZ5", 3));
            assertEquals(500, GL.getLimit("DTLZ6", 3));
            assertEquals(500, GL.getLimit("DTLZ7", 3));
            assertEquals(500, GL.getLimit("WFG2", 3));
            assertEquals(500, GL.getLimit("WFG3", 3));
            assertEquals(500, GL.getLimit("WFG4", 3));
            assertEquals(500, GL.getLimit("WFG5", 3));
            assertEquals(500, GL.getLimit("WFG6", 3));
            assertEquals(500, GL.getLimit("WFG7", 3));
            assertEquals(500, GL.getLimit("WFG8", 3));
            assertEquals(500, GL.getLimit("WFG9", 3));
            assertEquals(250, GL.getLimit("DTLZ2", 3));
            assertEquals(250, GL.getLimit("DTLZ4", 3));
            assertEquals(1250, GL.getLimit("DTLZ3", 3));
            assertEquals(1200, GL.getLimit("WFG1ALPHA02", 3));
        }
    }
}