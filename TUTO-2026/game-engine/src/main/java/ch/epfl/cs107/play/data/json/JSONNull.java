package ch.epfl.cs107.play.data.json;

public final class JSONNull extends JSONValue {
    private JSONNull() {}

    public static final JSONNull NULL = new JSONNull();

    @Override
    protected String toJSONString()  {
      return "null";
    }
}
