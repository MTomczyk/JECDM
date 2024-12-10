package io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link FileUtils}.
 *
 * @author MTomczyk
 */
class FileUtilsTest
{
    /**
     * Test 1.
     */
    @Test
    void getPathRelatedToClass()
    {
        Path path;
        String msg = "";
        try
        {
            path = FileUtils.getPathRelatedToClass(FileUtils.class, "Utils", "src", File.separatorChar);
            String common = "JECDM" + File.separatorChar + "JECDM";
            int idx = path.toString().indexOf(common);
            String relative = path.toString().substring(idx + common.length() + 1);
            System.out.println(relative);
            assertEquals("Utils" + File.separatorChar + "src" + File.separatorChar + "io", relative);
            Path filepath = FileUtils.getFilePathRelatedToClass(FileUtils.class, "file.txt", "Utils", "src", File.separatorChar);
            idx = filepath.toString().indexOf(common);
            relative = filepath.toString().substring(idx + common.length() + 1);
            System.out.println(relative);
            assertEquals("Utils" + File.separatorChar + "src" + File.separatorChar + "io" + File.separatorChar + "file.txt", relative);

        } catch (IOException e)
        {
            msg = e.getMessage();
        }
        assertEquals("", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void isAlphanumeric()
    {
        {
            assertTrue(FileUtils.isAlphanumeric("ABCD", null));
            assertTrue(FileUtils.isAlphanumeric("abcd", null));
            assertTrue(FileUtils.isAlphanumeric("ABCD1234", null));
            assertTrue(FileUtils.isAlphanumeric("abcd1234", null));
            assertTrue(FileUtils.isAlphanumeric("1234", null));
            assertTrue(FileUtils.isAlphanumeric("", null));
            Set<Character> allowed = new HashSet<>();
            allowed.add('!');
            allowed.add('@');
            allowed.add('#');
            allowed.add('Ę');
            assertTrue(FileUtils.isAlphanumeric("!", allowed));
            assertTrue(FileUtils.isAlphanumeric("@", allowed));
            assertTrue(FileUtils.isAlphanumeric("#", allowed));
            assertTrue(FileUtils.isAlphanumeric("Ę", allowed));
        }
        {
            assertFalse(FileUtils.isAlphanumeric("ABCD!", null));
            assertFalse(FileUtils.isAlphanumeric("abcd@", null));
            assertFalse(FileUtils.isAlphanumeric("ABCD1234#", null));
            assertFalse(FileUtils.isAlphanumeric("abcd1234!", null));
            assertFalse(FileUtils.isAlphanumeric("1234@", null));
            assertFalse(FileUtils.isAlphanumeric("Ę", null));
            Set<Character> allowed = new HashSet<>();
            allowed.add('!');
            allowed.add('@');
            allowed.add('#');
            allowed.add('Ę');
            assertFalse(FileUtils.isAlphanumeric("$", allowed));
            assertFalse(FileUtils.isAlphanumeric("%", allowed));
            assertFalse(FileUtils.isAlphanumeric("^", allowed));
            assertFalse(FileUtils.isAlphanumeric("Ź", allowed));
        }
    }

    /**
     * Test 1.
     */
    @Test
    void removeRecursively()
    {
        String path = null;
        String msg = "";
        try
        {
            path = String.valueOf(FileUtils.getPathRelatedToClass(FileUtils.class, "Utils", "tests", File.separatorChar));
        } catch (IOException e)
        {
            msg = e.getMessage();
        }
        assertEquals("", msg);

        File f = new File(path + File.separatorChar + "TEST");
        assertTrue(f.mkdir());

        try
        {
            f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "a.bmp");
            assertTrue(f.createNewFile());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1");
        assertTrue(f.mkdir());

        try
        {
            f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1" + File.separatorChar + "b.bmp");
            assertTrue(f.createNewFile());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F2");
        assertTrue(f.mkdir());

        try
        {
            f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F2" + File.separatorChar + "c.bmp");
            assertTrue(f.createNewFile());
            f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F2" + File.separatorChar + "d.bmp");
            assertTrue(f.createNewFile());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1" + File.separatorChar + "F3");
        assertTrue(f.mkdir());
        f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1" + File.separatorChar + "F3"
                + File.separatorChar + "F4");
        assertTrue(f.mkdir());

        try
        {
            f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1" + File.separatorChar + "F3"
                    + File.separatorChar + "F4" + File.separatorChar + "e.bmp");
            assertTrue(f.createNewFile());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        f = new File(path + File.separatorChar + "TEST" + File.separatorChar + "F1" + File.separatorChar + "F3"
                + File.separatorChar + "F5");
        assertTrue(f.mkdir());

        try
        {
            FileUtils.removeFolderRecursively(path + File.separatorChar + "TEST", 5, 100, true);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        assertFalse(new File(path + File.separatorChar + "TEST").exists());
    }
}