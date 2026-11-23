package ch.epfl.cs107.play.data.json;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/** Used to read JSON files and write to JSON files */
public class JSONParser {
    /** The valid file extensions for JSON files */
    private static final String[] FILE_EXTENSIONS = new String[] { ".json", ".txt" };

    /**
     * Used to read JSON files and get their contents as a JSONObject
     * @param path (String): the path to the file
     * @return (JSONObject): the contents of the file
     */
    public static JSONValue readJSONFromFile(String path) {
        validatePath(path);

        // Read the file
        try {
            String content = java.nio.file.Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return new Parser(content).parseContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Used to write a JSONObject to a file
     * @param path (String): the path to the file
     * @param json (JSONObject): the JSONObject to write
     */
    public static void writeJSONToFile(String path, JSONObject json) {
        validatePath(path);

        // Check if the JSONObject is null
        if (json == null) {
            throw new IllegalArgumentException("JSON cannot be null");
        }

        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(json.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Used to validate a file path
     * @param path (String): the path to validate
     */
    private static void validatePath(String path) {
        // Check if the path is null or empty
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }

        // Check if the file extension is valid
        boolean valid = false;
        for (String extension : FILE_EXTENSIONS) {
            int length = extension.length();
            String ext = path.substring(path.length() - length);
            if (ext.toLowerCase().equals(extension)) {
                if (ext.equals(".txt")) {
                    System.err.println("Warning in file: " + path);
                    System.err.println("It is recommended to use the .json file extension for JSON files instead of .txt");
                }
                valid = true;
                break;
            }
        }
        if (!valid) throw new IllegalArgumentException("Invalid file extension: ." + path.split("\\.")[1]);
    }

    private static class Parser {
        private static final int EOF = -1;

        private final String content;
        private final int length;
        private int pindex;

        public Parser(String content) {
            this.content = content;
            this.length = content.length();
            this.pindex = 0;
        }

        public JSONValue parseContent() throws IOException {
            JSONValue result = parseValue();
            skipWhitespace();
            if (pindex < length)
                parseError("expected end of file");
            return result;
        }

        private JSONValue parseValue() throws IOException {
            skipWhitespace();
            int c = currentChar();
            switch (c) {
                case '{' -> {
                    return parseObject();
                }
                case '[' -> {
                    return parseArray();
                }
                case '"' -> {
                    return parseStringValue();
                }
                case 't' -> {
                    skipKeyword("true");
                    return JSONBoolean.TRUE;
                }
                case 'f' -> {
                    skipKeyword("false");
                    return JSONBoolean.FALSE;
                }
                case 'n' -> {
                    skipKeyword("null");
                    return JSONNull.NULL;
                }
                case '-' -> {
                    return parseNumber();
                }
                default -> {
                    if (isDigit(c))
                        return parseNumber();
                    else
                        throw makeParseError("unexpected character '" + content.charAt(pindex) + "'");
                }
            }
        }

        private void skipWhitespace() throws IOException {
            while (pindex < length) {
                switch (content.charAt(pindex)) {
                    case ' ', '\n', '\r', '\t' -> {
                        pindex++;
                    }
                    default -> {
                        return;
                    }
                }
            }
        }

        private JSONValue parseNumber() throws IOException {
            /* Go through all the characters that can make up a number,
             * but don't actually figure out the numerical value.
             */

            int start = pindex;

            if (currentChar() == '-')
                pindex++;

            // Integral part
            if (currentChar() == '0') {
                // 0 must be alone; it cannot be followed by other digits
                pindex++;
                if (isDigit(currentChar()))
                    parseError("unexpected digit after leading 0");
            } else {
                // skip through subsequent digits
                skipDigits();
            }

            // Fractional part
            if (currentCharOrEOF() == '.') {
                pindex++;
                if (!isDigit(currentChar()))
                    parseError("expected digit after decimal point");
                skipDigits();
            }

            // Exponent
            if (currentCharOrEOF() == 'e' || currentCharOrEOF() == 'E') {
                pindex++;
                if (currentChar() == '+' || currentChar() == '-')
                    pindex++;
                if (!isDigit(currentChar()))
                    parseError("expected digit for exponent part");
                skipDigits();
            }

            return new JSONNumber(content.substring(start, pindex));
        }

        private JSONValue parseStringValue() throws IOException {
            return new JSONString(parseString());
        }

        private String parseString() throws IOException {
            assert currentChar() == '"';
            pindex++;

            StringBuilder b = new StringBuilder();

            while (currentChar() != '"') {
                if (currentChar() == '\\') {
                    // escape sequence
                    pindex++;
                    switch (currentChar()) {
                        case '"' -> {
                            b.append('\"');
                            pindex++;
                        }
                        case '\\' -> {
                            b.append('\\');
                            pindex++;
                        }
                        case '/' -> {
                            b.append('/');
                            pindex++;
                        }
                        case 'b' -> {
                            b.append('\b');
                            pindex++;
                        }
                        case 'f' -> {
                            b.append('\"');
                            pindex++;
                        }
                        case 'n' -> {
                            b.append('\f');
                            pindex++;
                        }
                        case 'r' -> {
                            b.append('\r');
                            pindex++;
                        }
                        case 't' -> {
                            b.append('\n');
                            pindex++;
                        }
                        case 'u' -> {
                            pindex++;
                            for (int i = 0; i < 4; i++) {
                                if (!isHexDigit(currentChar())) {
                                    parseError("expected hex digit");
                                }
                                pindex++;
                            }
                            b.append((char) Integer.parseInt(content.substring(pindex - 4, pindex), 16));
                        }
                        default -> {
                            parseError("unexpected escape sequence '" + currentChar() + "'");
                        }
                    }
                } else {
                    // normal character
                    b.append(content.charAt(pindex)); // we need a char, here
                    pindex++;
                }
            }

            pindex++; // skip the terminating "
            return b.toString();
        }

        private void skipKeyword(String keyword) throws IOException {
            if (!content.startsWith(keyword, pindex))
                parseError("expected keyword " + keyword);
            pindex += keyword.length();
        }

        private JSONValue parseArray() throws IOException {
            assert currentChar() == '[';
            pindex++;

            skipWhitespace();
            if (currentChar() == ']')
                return new JSONArray();

            ArrayList<JSONValue> elements = new ArrayList<>();

            while (true) {
                elements.add(parseValue());
                skipWhitespace();
                switch (currentChar()) {
                    case ']' -> {
                        pindex++;
                        return new JSONArray(elements);
                    }
                    case ',' -> {
                        // continue
                        pindex++;
                    }
                    default -> {
                        parseError("expected ']' or ','");
                    }
                }
            }
        }

        private JSONValue parseObject() throws IOException {
            assert currentChar() == '{';
            pindex++;

            skipWhitespace();
            if (currentChar() == '}')
                return new JSONObject();

            LinkedHashMap<String, JSONValue> values = new LinkedHashMap<>();

            while (true) {
                skipWhitespace();
                if (currentChar() != '"')
                    parseError("expected string key");
                String key = parseString();

                skipWhitespace();
                if (currentChar() != ':')
                    parseError("expected ':'");
                pindex++;

                JSONValue value = parseValue();

                values.put(key, value);

                skipWhitespace();
                switch (currentChar()) {
                    case '}' -> {
                        pindex++;
                        return new JSONObject(values);
                    }
                    case ',' -> {
                        // continue
                        pindex++;
                    }
                    default -> {
                        parseError("expected '}' or ','");
                    }
                }
            }
        }

        private boolean isDigit(int c) {
            return c >= '0' && c <= '9';
        }

        private boolean isHexDigit(int c) {
            return isDigit(c) || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
        }

        private void skipDigits() {
            while (isDigit(currentCharOrEOF()))
                pindex++;
        }

        private int currentChar() throws IOException {
            checkNotEOF();
            return content.charAt(pindex);
        }

        private int currentCharOrEOF() {
            if (pindex >= length)
                return EOF;
            else
                return content.charAt(pindex);
        }

        private void checkNotEOF() throws IOException {
            if (pindex >= length)
                parseError("unexpected end of file");
        }

        private void parseError(String msg) throws IOException {
            throw makeParseError(msg);
        }

        private IOException makeParseError(String msg) {
            if (pindex >= length) {
                return new IOException("JSON parse error at end of file: " + msg);
            } else {
                int line = 1;
                int column = 1;
                for (int index = 0; index < pindex; index++) {
                    if (content.charAt(index) == '\n') {
                        line++;
                        column = 1;
                    } else {
                        column++;
                    }
                }

                char c = content.charAt(pindex);

                return new IOException("JSON parse error at " + line + ":" + column + " (char is '" + c + "'): " + msg);
            }
        }
    }
}
