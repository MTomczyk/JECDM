package utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link StringUtils}.
 *
 * @author MTomczyk
 */
class StringUtilsTest
{
    /**
     * Test 1.
     */
    @Test
    void getDeltaTime()
    {
        {
            LocalDateTime ldt1 = LocalDateTime.of(0, 1, 1, 0, 0, 0);
            LocalDateTime ldt2 = LocalDateTime.of(0, 1, 2, 0, 0, 0);
            String delta = StringUtils.getDeltaTime(ldt1, ldt2);
            System.out.println(delta);
            assertEquals("[days = 1, hours = 0, minutes = 0, seconds = 0, millis = 0]", delta);
        }
        {
            LocalDateTime ldt1 = LocalDateTime.of(0, 1, 1, 0, 0, 0);
            LocalDateTime ldt2 = LocalDateTime.of(0, 1, 2, 0, 0, 1);
            String delta = StringUtils.getDeltaTime(ldt1, ldt2);
            System.out.println(delta);
            assertEquals("[days = 1, hours = 0, minutes = 0, seconds = 1, millis = 0]", delta);
        }
        {
            LocalDateTime ldt1 = LocalDateTime.of(0, 1, 1, 0, 0, 0);
            LocalDateTime ldt2 = LocalDateTime.of(0, 1, 3, 5, 6, 1);
            String delta = StringUtils.getDeltaTime(ldt1, ldt2);
            System.out.println(delta);
            assertEquals("[days = 2, hours = 5, minutes = 6, seconds = 1, millis = 0]", delta);
        }
    }
}