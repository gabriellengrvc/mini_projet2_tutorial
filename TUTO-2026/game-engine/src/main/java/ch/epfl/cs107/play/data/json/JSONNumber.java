package ch.epfl.cs107.play.data.json;

public final class JSONNumber extends JSONValue {
    // Technically, a JSON number does not have a fixed interpretation
    private final String value;

    /**
     * Creates a new JSON number from its string representation.
     * @throws NumberFormatException if the value is not a valid JSON number string.
     */
    public JSONNumber(String value) {
        this.value = value;
    }

    public JSONNumber(int value) {
        this.value = String.valueOf(value);
    }

    public JSONNumber(long value) {
        this.value = String.valueOf(value);
    }

    public JSONNumber(double value) {
        this.value = String.valueOf(value);
    }

    @Override
    protected String toJSONString() {
      return value;
    }

    /**
     * Returns the string representation of this JSON number.
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Tests whether the value of this number can be parsed as an int.
     * @return true if it can, false otherwise
     */
    public boolean isInt() {
        try {
            getAsInt();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns the value of this JSON number as an int.
     *
     * @throws NumberFormatException if it cannot be parsed as an int
     */
    public int getAsInt() {
        return Integer.parseInt(value);
    }

    /**
     * Tests whether the value of this number can be parsed as an long.
     * @return true if it can, false otherwise
     */
    public boolean isLong() {
        try {
            getAsLong();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns the value of this JSON number as an long.
     *
     * @throws NumberFormatException if it cannot be parsed as an long
     */
    public long getAsLong() {
        return Long.parseLong(value);
    }

    /**
     * Returns the value of this JSON number as a double.
     * This operation always succeeds.
     */
    public double getDouble() {
        return Double.parseDouble(value);
    }
}
