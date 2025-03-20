package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Set;

/**
 * Provides various functionalities related to i/o handling.
 *
 * @author MTomczyk
 */
public class FileUtils
{
    /**
     * Returns the path to the main JECDM module (...\JECDM\JECDM\).
     *
     * @return the path to the main JECDM module
     * @throws IOException the exception will be thrown if the canonical path cannot be derived
     */
    public static Path getPathToJECDM() throws IOException
    {
        String cp = new File(".").getCanonicalPath();
        int idx = cp.indexOf("JECDM");
        return Paths.get(cp.substring(0, idx) + "JECDM" + File.separatorChar + "JECDM" + File.separatorChar);
    }

    /**
     * Helps to construct an absolute path to a folder (package) containing a class.
     *
     * @param cl         class
     * @param moduleName module name
     * @param mainFolder e.g., "src" or "tests"
     * @param fs         folder separator
     * @return path to the folder containing a class (relative to project's path)
     * @throws IOException the exception will be thrown if the canonical path cannot be derived
     */
    public static Path getPathRelatedToClass(Class<?> cl, String moduleName, String mainFolder, char fs) throws IOException
    {
        Path JECDM = getPathToJECDM();
        String path = JECDM.toString() + fs + moduleName + File.separatorChar + mainFolder + fs +
                cl.getPackage().getName().replace('.', fs) + fs;
        return Paths.get(path);
    }

    /**
     * Helps to construct an absolute path to a file based on a class location (the class should be in the same folder as
     * the requested file).
     *
     * @param cl         class
     * @param filename   requested file name
     * @param moduleName module name
     * @param mainFolder e.g., "src" or "tests"
     * @param folderSep  folder separator
     * @return path to the folder containing a class (relative to project's path)
     * @throws IOException the exception will be thrown if the canonical path cannot be derived
     */
    public static Path getFilePathRelatedToClass(Class<?> cl, String filename, String moduleName,
                                                 String mainFolder, char folderSep) throws IOException
    {
        Path prefix = getPathRelatedToClass(cl, moduleName, mainFolder, folderSep);
        return Paths.get(prefix.toString() + folderSep + filename);
    }

    /**
     * Returns the lines stored in the file.
     * The processing is handled by the buffered reader.
     *
     * @param br            instantiated buffered reader
     * @param appendNewline if true, is parsed line is additionally ended with the new line symbol
     * @return lines (list)
     * @throws IOException IO exception can be thrown
     */
    public static LinkedList<String> getLines(BufferedReader br, boolean appendNewline) throws IOException
    {
        LinkedList<String> lines = new LinkedList<>();
        String line;
        while ((line = br.readLine()) != null)
        {
            lines.add(line);
            if (appendNewline) lines.add(System.lineSeparator());
        }
        return lines;
    }

    /**
     * Checks if a given string consists of only [a-z], [A-Z], and (optionally) some additionally specified characters.
     * The inspection is based on comparing unicode chars. If the string is empty, the method returns true
     *
     * @param str input string
     * @param o   optionally (can be null) specified characters (provided as a set)
     * @return true, if the input string matches the specified pattern; false otherwise
     */
    public static boolean isAlphanumeric(String str, Set<Character> o)
    {
        for (int i = 0; i < str.length(); i++)
        {
            int code = str.codePointAt(i);
            if (
                    !((code >= 65 && code <= 90)) &&
                            !((code >= 97 && code <= 122)) &&
                            !((code >= 48 && code <= 57)) &&
                            !((o != null) && (o.contains(str.charAt(i))))
            )
                return false;
        }

        return true;
    }


    /**
     * Method for removing a folder/file and all its subfolders and files (recursively; DANGEROUS).
     * The method is terminated immediately if the input path is invalid. Note that the .delete() call for folder/file
     * may not be successful. In that case, the removal is skipped unless an additional wait and attempts parameters are
     * provided. The latter determines how many times the removal will be attempted, while the former the time delay
     * between subsequent calls.
     *
     * @param path     path pointing to a folder/file (absolute)
     * @param attempts determines the number of removal attempts (per each file; can be null = not used)
     * @param wait     determines the delay between subsequent removal attempts (in ms; can be null = not used)
     * @param notify   if true, the path to a file/folder being removed will be printed
     * @throws InterruptedException interrupted exception
     */
    public static void removeFolderRecursively(String path, Integer attempts, Integer wait, boolean notify) throws InterruptedException
    {
        File root = new File(path);
        if (!root.exists()) return;
        int a = 0;
        int w = 0;
        if (attempts != null) a = attempts;
        if (wait != null) w = wait;
        removeRecursively(root, a, w, notify);
    }

    /**
     * Method for removing a folder/file and all its subfolders and files (recursively). Note that the .delete() call
     * for folder/file may not be successful. In that case, the removal is skipped unless an additional wait and attempts
     * parameters are provided. The latter determines how many times the removal will be attempted, while the former the
     * time delay between subsequent calls.
     *
     * @param file     file
     * @param attempts determines the number of removal attempts (per each file; can be null = not used)
     * @param wait     determines the delay between subsequent removal attempts (in ms; can be null = not used)
     * @param notify   if true, the path to a file/folder being removed will be printed
     * @throws InterruptedException interrupted exception
     */
    private static void removeRecursively(File file, Integer attempts, Integer wait, boolean notify) throws InterruptedException
    {
        if (!file.exists()) return;
        if (file.isDirectory())
        {
            File[] subs = file.listFiles();
            if (subs != null) for (File f : subs) removeRecursively(f, attempts, wait, notify);
            // delete directory attempts
            for (int i = 0; i < attempts; i++)
            {
                if (notify) System.out.println("Removing: " + file);
                if (file.delete()) break;
                Thread.sleep(wait);
            }
        }
        else
        {
            // delete file attempts
            for (int i = 0; i < attempts; i++)
            {
                if (notify) System.out.println("Removing: " + file);
                if (file.delete()) break;
                Thread.sleep(wait);
            }
        }
    }
}
