package population;

import java.util.ArrayList;

/**
 * Implementation of {@link ISpecimenGetter} for retrieving a population of specimens.
 *
 * @author MTomczyk
 */
public class PopulationGetter implements ISpecimenGetter
{
    /**
     * Retrieves the population.
     *
     * @param container container
     * @return specimen array
     */
    @Override
    public ArrayList<Specimen> getSpecimens(SpecimensContainer container)
    {
        return container.getPopulation();
    }
}
