package swing.keyboard;

import java.awt.event.KeyEvent;

/**
 * Provides useful constants/functionalities for key-related events.
 *
 * @author MTomczyk
 */


public class KeyUtils
{
    /**
     * List of codes for ``regular keys.''
     */
    public static final int[] REGULAR_KEYS_CODES = new int[]
            {
                    KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
                    KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9,
                    KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E,
                    KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J,
                    KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_O,
                    KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T,
                    KeyEvent.VK_U, KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y,
                    KeyEvent.VK_Z
            };

    /**
     * List of codes for ``special keys.''
     */
    public static final int[] SPECIAL_KEYS_CODES = new int[]
            {
                    KeyEvent.VK_SPACE
            };
}