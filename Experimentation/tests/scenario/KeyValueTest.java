package scenario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains some tests for the {@link KeyValue} class.
 *
 * @author MTomczyk
 */
class KeyValueTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        Key key = new Key("test", "T");
        key.setOrder(5);
        Value val = new Value("val", false);
        KeyValue kv = new KeyValue(key, val);
        assertEquals("TEST_VAL", kv.toString());
        assertEquals(5, kv.hashCode());
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        Key[] keys = new Key[]{
                new Key("AaA", "a"),
                new Key("aAa", "a"),
                new Key("CCC", "c"),
                new Key("aaa", "a")
        };

        Value[] values = new Value[]{
                new Value("123", false),
                new Value("321", false),
                new Value("111", false),
                new Value("123", false),
        };


        KeyValue[] kv = new KeyValue[]{
                new KeyValue(keys[0], values[0]),
                new KeyValue(keys[1], values[1]),
                new KeyValue(keys[2], values[2]),
                new KeyValue(keys[3], values[3]),
        };

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if ((i == j) || ((i == 0) && (j == 3)) || ((i == 3) && (j == 0)))
                    assertEquals(kv[i], kv[j]);


    }
}