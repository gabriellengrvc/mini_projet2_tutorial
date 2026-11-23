package ch.epfl.cs107.play.data.json;

import java.util.*;

/** Used to represent json objects */
public final class JSONObject extends JSONValue {
    /** The map of values stored in this object */
    private final LinkedHashMap<String, JSONValue> values = new LinkedHashMap<>();

    public JSONObject() {
    }

    public JSONObject(LinkedHashMap<String, JSONValue> values) {
        this.values.putAll(values);
    }

    /**
     * Adds a value to this object
     * @param key (String): The key of the value to add
     * @param value (JSONValue): The value to add
     * @return (JSONObject): This object
     */
    public JSONObject add(String key, JSONValue value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (values.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate key: " + key);
        }
        values.put(key, value);
        return this;
    }

    /**
     * Returns all the values in this object
     * @return The value of this object
     */
    public Map<String, JSONValue> get() {
        return values;
    }

    /**
     * Returns the value with the given key
     * @param key (String): The key of the value to return
     * @return (JSONValue): The value with the given key
     */
    public JSONValue get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        return values.get(key);
    }

    /**
     * Returns whether this object contains the given key
     * @param key (String): The key to check
     * @return (boolean): Whether this object contains the given key
     */
    public boolean containsKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return values.containsKey(key);
    }

    /**
     * Removes the value with the given key
     * @param key (String): The key of the value to remove
     * @return (JSONObject): This object
     */
    public JSONObject remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        values.remove(key);
        return this;
    }

    /**
     * Returns the set of keys in this object
     * @return The set of keys in this object
     */
    public Set<String> keySet() {
        return values.keySet();
    }

    @Override
    protected String toJSONString() {
        if (values.isEmpty()) return "{}";

        String indent = "  ";
        StringBuilder builder = new StringBuilder().append("{\n");
        boolean firstEntry = true;

        for (Map.Entry<String, JSONValue> entry : values.entrySet()) {
            if (!firstEntry) builder.append(",\n");
            builder.append(indent).append("\"").append(entry.getKey()).append("\": ");

            if (entry.getValue() instanceof JSONObject || entry.getValue() instanceof JSONArray) {
                // (?m)^ is a regex that activates multiline mode to match the start of each line
                String nestedJson = (entry.getValue() + "").replaceAll("(?m)^", indent);
                builder.append(nestedJson);
            } else {
                builder.append(entry.getValue());
            }
            firstEntry = false;
        }

        return builder.append("\n}").toString().replaceAll(":\\s+", ": ");
    }

    public JSONObject getObject(String key) {
        return ((JSONObject) get(key));
    }

    public JSONArray getArray(String key) {
        return ((JSONArray) get(key));
    }

    public boolean getBoolean(String key) {
        return ((JSONBoolean) get(key)).getValue();
    }

    public int getInt(String key) {
        return ((JSONNumber) get(key)).getAsInt();
    }

    public String getString(String key) {
        return ((JSONString) get(key)).getString();
    }
}
