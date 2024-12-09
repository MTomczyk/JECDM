package container;

import java.util.HashMap;

/**
 * This class provides a basic means of storing top/scenario/trial-level data.
 * Data can be stored in hash maps (strings are used as keys).
 * This data-handling strategy can be convenient, but it also lessens interpretability.
 *
 * @author MTomczyk
 */
public abstract class AbstractDataContainer
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Hash map for storing integers.
         */
        public HashMap<String, Integer> _mapInteger = null;

        /**
         * Hash map for storing doubles.
         */
        public HashMap<String, Double> _mapDouble = null;

        /**
         * Hash map for storing strings.
         */
        public HashMap<String, String> _mapString = null;

        /**
         * Hash map for storing objects (should be cast when retrieved).
         */
        public HashMap<String, Object> _mapObject = null;
    }


    /**
     * Hash map for storing integers.
     */
    protected HashMap<String, Integer> _mapInteger;

    /**
     * Hash map for storing doubles.
     */
    protected HashMap<String, Double> _mapDouble;

    /**
     * Hash map for storing strings.
     */
    protected HashMap<String, String> _mapString;

    /**
     * Hash map for storing objects (should be cast when retrieved).
     */
    protected HashMap<String, Object> _mapObject;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractDataContainer(Params p)
    {
        if (p != null) passMapsFromParams(p);
    }

    /**
     * Auxiliary method that passes references to the map objects from the abstract container to the params container.
     *
     * @param p params container
     */
    protected void passParams(Params p)
    {
        p._mapInteger = _mapInteger;
        p._mapDouble = _mapDouble;
        p._mapString = _mapString;
        p._mapObject = _mapObject;
    }

    /**
     * Auxiliary method that passes references to the map objects from the params container to the abstract container.
     *
     * @param p params container
     */
    protected void passMapsFromParams(Params p)
    {
        _mapInteger = p._mapInteger;
        _mapDouble = p._mapDouble;
        _mapString = p._mapString;
        _mapObject = p._mapObject;
    }

    /**
     * Auxiliary method for storing integer-like data.
     *
     * @param key   key for the value (integer)
     * @param value value to be stored
     */
    public void storeInteger(String key, Integer value)
    {
        if (_mapInteger == null) _mapInteger = new HashMap<>(); // lazy init
        _mapInteger.put(key, value);
    }

    /**
     * Getter for a stored integer value (accessed via a key).
     *
     * @param key key for the value
     * @return the value (the method returns null if the linked hash map is not instantiated or does not contain a requested key)
     */
    public Integer getInteger(String key)
    {
        if (_mapInteger == null) return null;
        if (!_mapInteger.containsKey(key)) return null;
        return _mapInteger.get(key);
    }

    /**
     * Auxiliary method for storing double-like data.
     *
     * @param key   key for the value (double)
     * @param value value to be stored
     */
    public void storeDouble(String key, Double value)
    {
        if (_mapDouble == null) _mapDouble = new HashMap<>(); // lazy init
        _mapDouble.put(key, value);
    }

    /**
     * Getter for a stored double value (accessed via a key).
     *
     * @param key key for the value
     * @return the value (the method returns null if the linked hash map is not instantiated or does not contain a requested key)
     */
    public Double getDouble(String key)
    {
        if (_mapDouble == null) return null;
        if (!_mapDouble.containsKey(key)) return null;
        return _mapDouble.get(key);
    }

    /**
     * Auxiliary method for storing string-like data.
     *
     * @param key   key for the value (string)
     * @param value value to be stored
     */
    public void storeString(String key, String value)
    {
        if (_mapString == null) _mapString = new HashMap<>(); // lazy init
        _mapString.put(key, value);
    }

    /**
     * Getter for a stored string value (accessed via a key).
     *
     * @param key key for the value
     * @return the value (the method returns null if the linked hash map is not instantiated or does not contain a requested key)
     */
    public String getString(String key)
    {
        if (_mapString == null) return null;
        if (!_mapString.containsKey(key)) return null;
        return _mapString.get(key);
    }

    /**
     * Auxiliary method for storing object-like data.
     *
     * @param key   key for the value (object)
     * @param value value to be stored
     */
    public void storeObject(String key, Object value)
    {
        if (_mapObject == null) _mapObject = new HashMap<>(); // lazy init
        _mapObject.put(key, value);
    }

    /**
     * Getter for a stored string value (accessed via a key).
     *
     * @param key key for the value
     * @return the value (the method returns null if the linked hash map is not instantiated or does not contain a requested key)
     */
    public Object getObject(String key)
    {
        if (_mapObject == null) return null;
        if (!_mapObject.containsKey(key)) return null;
        return _mapObject.get(key);
    }

    /**
     * Auxiliary method for clearing the data.
     */
    protected void dispose()
    {
        _mapInteger = null;
        _mapDouble = null;
        _mapString = null;
        _mapObject = null;
    }
}
