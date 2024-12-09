package population;

import java.util.ArrayList;

/**
 * Simple interface for retrieving specimen arrays form specimen container.
 *
 * @author MTomczyk
 */
public interface ISpecimenGetter
{
    /**
     * Retrieves specimen array from the container.
     *
     * @param container container
     * @return specimen array
     */
    ArrayList<Specimen> getSpecimens(SpecimensContainer container);
}
