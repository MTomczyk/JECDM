package swing.imagesaver;

/**
 * Some GUI tests for {@link ImageSaver}.
 *
 * @author MTomczyk
 */
class ImageSaverTest
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        ImageSaver saver = new ImageSaver(null, true);
        saver.setSize(500, 500);
        saver.setLocationRelativeTo(null);
        saver.setVisible(true);
        saver.pack();
    }
}