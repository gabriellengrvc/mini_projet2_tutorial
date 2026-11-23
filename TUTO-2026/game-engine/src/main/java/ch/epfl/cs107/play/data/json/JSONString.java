package ch.epfl.cs107.play.data.json;

/** Used to represent json strings, ints, doubles and booleans */
public final class JSONString extends JSONValue {
    /** The value of the string */
    private String value;

    /**
     * Creates a new JSONString with the given value
     * @param value (String): The value of the string
     */
    public JSONString(String value) {
        set(value);
    }
    /**
     * Sets the value of this object
     * @param value (String): The new value of this object
     */
    public void set(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
    }


    /**
     * Returns the value of this object
     * @return (String): The value of this object as a String
     */
    public String getString() {
        return value;
    }

    @Override
    protected String toJSONString() {
        return "\"" + value.replaceAll("([\\\\\"])", "$1") + "\"";
    }
}
