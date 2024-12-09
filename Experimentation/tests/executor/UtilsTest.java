package executor;

import org.junit.jupiter.api.Test;
import utils.Log;

/**
 * Provides some tests for {@link Log} class.
 *
 * @author MTomczyk
 */
class UtilsTest
{
    /**
     * Test 1
     */
    @Test
    void log()
    {
        String s1 = Log.getLog("================================================", 0, false);
        System.out.print(s1);
        String s2 = Log.getLog("TEST MESSAGE\nand the new line", 3, true);
        System.out.print(s2);
        String s3 = Log.getLog("xyz xyz xyz", 7, true);
        System.out.print(s3);
        String s4 = Log.getLog("xyz xyz xyz", 7, false);
        System.out.print(s4);
        String s5 = Log.getLog("xyz xyz xyz", 0, true);
        System.out.print(s5);
        String s6 = Log.getLog("xyz xyz xyz", 0, false);
        System.out.print(s6);
    }
}