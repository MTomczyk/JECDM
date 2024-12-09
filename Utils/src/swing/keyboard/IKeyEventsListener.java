package swing.keyboard;

/**
 * Own listener for key-related events. Supports code unification.
 *
 * @author MTomczyk
 */
public interface IKeyEventsListener
{
    /**
     * Notifies that the key has been pressed.
     *
     * @param key key code
     */
    void notifyKeyPressed(int key);

    /**
     * Notifies that the key has been released.
     *
     * @param key key code
     */
    void notifyKeyReleased(int key);


    /**
     * Can be used to notify whether all keys have been released.
     */
    void notifyAllKeysReleased();
}
