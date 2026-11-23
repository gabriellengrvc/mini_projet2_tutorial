package ch.epfl.cs107.play.data.json;

public final class JSONBoolean extends JSONValue {
    private final boolean value;

    private JSONBoolean(boolean value) {
        this.value = value;
    }

    public static final JSONBoolean FALSE = new JSONBoolean(false);
    public static final JSONBoolean TRUE = new JSONBoolean(true);

    public static JSONBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Returns the boolean value of this JSON boolean.
     */
    public boolean getValue() {
        return value;
    }

    @Override
    protected String toJSONString() {
        return String.valueOf(value);
    }
}
