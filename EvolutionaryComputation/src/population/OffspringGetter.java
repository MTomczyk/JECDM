package population;

import java.util.ArrayList;

/**
 * Implementation of {@link ISpecimenGetter} for retrieving the offspring array.
 *
 * @author MTomczyk
 */
public class OffspringGetter implements ISpecimenGetter
{
    /**
     * Retrieves the offspring array.
     *
     * @param container container
     * @return specimen array
     */
    @Override
    public ArrayList<Specimen> getSpecimens(SpecimensContainer container)
    {
        return container.getOffspring();
    }
}
