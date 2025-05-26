package unified;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import scenario.CrossedScenarios;

import java.util.*;

/**
 * Auxiliary abstract class providing functionalities for unifying indicators and statistic functions.
 *
 * @author MTomczyk
 */
public class AbstractUnified<T>
{
    /**
     * Generic interface for retrieving entities to be unified.
     *
     * @author MTomczyk
     */
    public interface IEntityGetter<T>
    {
        /**
         * Getter for entities to be unified.
         *
         * @param SDC scenario data container
         * @return entities to be unified
         */
        T[] getEntities(AbstractScenarioDataContainer SDC);
    }

    /**
     * Entity getter.
     */
    protected final IEntityGetter<T> _entityGetter;

    /**
     * Entities map (key = entity's name; value = index in the organized array)
     */
    public final HashMap<String, Integer> _entitiesMap;

    /**
     * Unified entities (and potentially sorted).
     */
    public final ArrayList<T> _entities;

    /**
     * Parameterized constructor
     *
     * @param entityGetter     object responsible for retrieving the processed objects
     * @param errorMessage     default error message to be printed
     * @param SDCs             scenario data getters
     * @param requestedNames   requested unified names
     * @param crossedScenarios crossed scenarios being currently processed
     * @throws CrossedScenariosException crossed scenarios exception can be thrown and propagated higher
     */
    public AbstractUnified(IEntityGetter<T> entityGetter,
                           String errorMessage,
                           AbstractScenarioDataContainer[] SDCs,
                           String[] requestedNames,
                           CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        _entityGetter = entityGetter;

        LinkedList<T> entities = new LinkedList<>();
        Set<String> names = new HashSet<>();
        for (AbstractScenarioDataContainer sdc : SDCs)
        {
            T[] ents = _entityGetter.getEntities(sdc);
            for (T e : ents)
            {
                if (!names.contains(e.toString()))
                {
                    names.add(e.toString());
                    entities.add(e);
                }
            }
        }


        _entities = new ArrayList<>(entities.size());
        _entitiesMap = new HashMap<>(entities.size());

        _entities.addAll(entities);
        for (int i = 0; i < _entities.size(); i++)
            _entitiesMap.put(_entities.get(i).toString(), i);

        // compare with the request
        if (requestedNames != null)
        {
            boolean valid = true;
            Set<String> uMap = new HashSet<>();
            for (String s : requestedNames) if (s != null) uMap.add(s.toUpperCase());
            for (String s : names) // must be a subset
                if (!uMap.contains(s))
                {
                    valid = false;
                    break;
                }

            if (!valid)
            {
                StringBuilder suffix = new StringBuilder(" (existing = ");
                for (int i = 0; i < entities.size(); i++)
                {
                    suffix.append(_entities.get(i).toString());
                    if (i < entities.size() - 1) suffix.append(", ");
                }
                suffix.append("; requested = ");
                for (int i = 0; i < requestedNames.length; i++)
                {
                    suffix.append(requestedNames[i]);
                    if (i < requestedNames.length - 1) suffix.append(", ");
                }
                suffix.append(")");
                throw new CrossedScenariosException(errorMessage + suffix, null, this.getClass(), crossedScenarios);
            }

            ArrayList<T> newOrder = new ArrayList<>(_entities.size());
            for (String s : requestedNames)
            {
                if (_entitiesMap.containsKey(s))
                {
                    int oldIndex = _entitiesMap.get(s);
                    newOrder.add(_entities.get(oldIndex));
                }
            }
            _entities.clear();
            _entities.addAll(newOrder);

            _entitiesMap.clear(); // subset
            for (int i = 0; i < _entities.size(); i++)
                _entitiesMap.put(_entities.get(i).toString(), i);
        }
    }

}
