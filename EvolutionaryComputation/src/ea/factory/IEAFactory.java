package ea.factory;

import ea.EA;

/**
 * Interface for factories responsible for generating new instances of {@link EA}.
 *
 * @author MTomczyk
 */

public interface IEAFactory
{
    /**
     * Getter for the new EA instance.
     *
     * @return EA instance
     */
    EA getInstance();
}
