package team.creative.neolittletiles.common.converter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SNBTParser - String NBT parser for LittleTiles blueprint compatibility
 * 
 * Parses SNBT (String NBT) format used by LittleTiles exports:
 * - Compound tags: {key:value, key2:value2}
 * - List tags: [value1, value2, value3]
 * - Integer arrays: [I;1,2,3,4]
 * - String values: "quoted_string" or unquoted_string
 * - Numbers: integers, floats, longs, etc.
 * 
 * Based on Minecraft NBT format and LittleTiles export requirements
 */
public class SNBTParser {
    
    private String input;
    private int position;
    private int length;
    
    // Regex patterns for NBT parsing
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?[bslfdBSLFD]?");
    private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|[a-zA-Z0-9._+-]+");
    private static final Pattern ARRAY_PREFIX_PATTERN = Pattern.compile("\\[([BILS]);");
    
    public SNBTParser(String input) {
        this.input = input.trim();
        this.position = 0;
        this.length = this.input.length();
    }
    
    /**
     * Parse SNBT string to data structure
     * @param snbt SNBT format string
     * @return Parsed data object
     */
    public static Object parse(String snbt) {
        SNBTParser parser = new SNBTParser(snbt);
        try {
            return parser.parseValue();
        } catch (Exception e) {
            System.err.println("SNBT parsing failed: " + e.getMessage());
            System.err.println("At position " + parser.position + " in: " + 
                             snbt.substring(0, Math.min(100, snbt.length())));
            return null;
        }
    }
    
    /**
     * Parse any NBT value (compound, list, primitive)
     * @return Parsed value
     */
    private Object parseValue() {
        skipWhitespace();
        
        if (position >= length) {
            throw new RuntimeException("Unexpected end of input");
        }
        
        char c = input.charAt(position);
        
        switch (c) {
            case '{':
                return parseCompound();
            case '[':
                return parseList();
            case '"':
                return parseQuotedString();
            default:
                if (Character.isDigit(c) || c == '-') {
                    return parseNumber();
                } else if (Character.isLetter(c) || c == '_') {
                    return parseUnquotedString();
                } else {
                    throw new RuntimeException("Unexpected character: " + c);
                }
        }
    }
    
    /**
     * Parse compound tag: {key:value, key2:value2}
     * @return Map representing compound
     */
    private Map<String, Object> parseCompound() {
        Map<String, Object> compound = new HashMap<>();
        
        expect('{');
        skipWhitespace();
        
        // Handle empty compound
        if (peek() == '}') {
            advance();
            return compound;
        }
        
        while (true) {
            skipWhitespace();
            
            // Parse key
            String key = parseKey();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            
            // Parse value
            Object value = parseValue();
            compound.put(key, value);
            
            skipWhitespace();
            
            if (peek() == '}') {
                advance();
                break;
            } else if (peek() == ',') {
                advance();
                skipWhitespace();
                // Handle trailing comma
                if (peek() == '}') {
                    advance();
                    break;
                }
            } else {
                throw new RuntimeException("Expected ',' or '}' in compound");
            }
        }
        
        return compound;
    }
    
    /**
     * Parse list tag: [value1, value2, value3] or array: [I;1,2,3]
     * @return List representing the parsed structure
     */
    private List<Object> parseList() {
        expect('[');
        skipWhitespace();
        
        // Check for typed array prefix
        Matcher arrayMatcher = ARRAY_PREFIX_PATTERN.matcher(input.substring(position));
        if (arrayMatcher.lookingAt()) {
            return parseTypedArray(arrayMatcher.group(1).charAt(0));
        }
        
        List<Object> list = new ArrayList<>();
        
        // Handle empty list
        if (peek() == ']') {
            advance();
            return list;
        }
        
        while (true) {
            skipWhitespace();
            Object value = parseValue();
            list.add(value);
            
            skipWhitespace();
            
            if (peek() == ']') {
                advance();
                break;
            } else if (peek() == ',') {
                advance();
                skipWhitespace();
                // Handle trailing comma
                if (peek() == ']') {
                    advance();
                    break;
                }
            } else {
                throw new RuntimeException("Expected ',' or ']' in list");
            }
        }
        
        return list;
    }
    
    /**
     * Parse typed array: [I;1,2,3,4] or [B;1b,2b,3b]
     * @param type Array type character (I, B, L, S)
     * @return List of typed values
     */
    private List<Object> parseTypedArray(char type) {
        // Skip array prefix [I; or [B; etc.
        while (position < length && input.charAt(position) != ';') {
            position++;
        }
        if (position < length && input.charAt(position) == ';') {
            position++; // Skip semicolon
        }
        skipWhitespace();
        
        List<Object> array = new ArrayList<>();
        
        // Handle empty array
        if (peek() == ']') {
            advance();
            return array;
        }
        
        while (true) {
            skipWhitespace();
            Object value = parseNumber();
            
            // Convert to appropriate type
            if (value instanceof Number) {
                Number num = (Number) value;
                switch (type) {
                    case 'B':
                        array.add(num.byteValue());
                        break;
                    case 'S':
                        array.add(num.shortValue());
                        break;
                    case 'I':
                        array.add(num.intValue());
                        break;
                    case 'L':
                        array.add(num.longValue());
                        break;
                    default:
                        array.add(value);
                }
            } else {
                array.add(value);
            }
            
            skipWhitespace();
            
            if (peek() == ']') {
                advance();
                break;
            } else if (peek() == ',') {
                advance();
                skipWhitespace();
                // Handle trailing comma
                if (peek() == ']') {
                    advance();
                    break;
                }
            } else {
                throw new RuntimeException("Expected ',' or ']' in typed array");
            }
        }
        
        return array;
    }
    
    /**
     * Parse quoted string: "text with spaces"
     * @return Unescaped string content
     */
    private String parseQuotedString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        
        while (position < length) {
            char c = input.charAt(position);
            
            if (c == '"') {
                advance();
                return sb.toString();
            } else if (c == '\\') {
                advance();
                if (position >= length) {
                    throw new RuntimeException("Unterminated escape sequence");
                }
                char escaped = input.charAt(position);
                switch (escaped) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    default:
                        sb.append(escaped);
                }
                advance();
            } else {
                sb.append(c);
                advance();
            }
        }
        
        throw new RuntimeException("Unterminated quoted string");
    }
    
    /**
     * Parse unquoted string: simple_identifier or minecraft:stone
     * @return String value
     */
    private String parseUnquotedString() {
        StringBuilder sb = new StringBuilder();
        
        while (position < length) {
            char c = input.charAt(position);
            if (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '+' || c == '-' || c == ':') {
                sb.append(c);
                advance();
            } else {
                break;
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Parse key (string that can be quoted or unquoted)
     * @return Key string
     */
    private String parseKey() {
        skipWhitespace();
        if (peek() == '"') {
            return parseQuotedString();
        } else {
            return parseUnquotedString();
        }
    }
    
    /**
     * Parse number with optional type suffix
     * @return Number value
     */
    private Object parseNumber() {
        StringBuilder sb = new StringBuilder();
        
        // Handle negative sign
        if (peek() == '-') {
            sb.append(advance());
        }
        
        // Parse digits and decimal point
        while (position < length) {
            char c = input.charAt(position);
            if (Character.isDigit(c) || c == '.') {
                sb.append(c);
                advance();
            } else {
                break;
            }
        }
        
        // Check for type suffix
        char suffix = 0;
        if (position < length) {
            char c = input.charAt(position);
            if ("bslfdBSLFD".indexOf(c) >= 0) {
                suffix = c;
                advance();
            }
        }
        
        String numberStr = sb.toString();
        
        try {
            // Parse based on suffix or content
            switch (Character.toLowerCase(suffix)) {
                case 'b':
                    return Byte.parseByte(numberStr);
                case 's':
                    return Short.parseShort(numberStr);
                case 'l':
                    return Long.parseLong(numberStr);
                case 'f':
                    return Float.parseFloat(numberStr);
                case 'd':
                    return Double.parseDouble(numberStr);
                default:
                    if (numberStr.contains(".")) {
                        return Double.parseDouble(numberStr);
                    } else {
                        long value = Long.parseLong(numberStr);
                        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                            return (int) value;
                        } else {
                            return value;
                        }
                    }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format: " + numberStr);
        }
    }
    
    /**
     * Skip whitespace characters
     */
    private void skipWhitespace() {
        while (position < length && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
    
    /**
     * Peek at current character without advancing
     * @return Current character or 0 if at end
     */
    private char peek() {
        return position < length ? input.charAt(position) : 0;
    }
    
    /**
     * Advance position and return current character
     * @return Character that was advanced over
     */
    private char advance() {
        return position < length ? input.charAt(position++) : 0;
    }
    
    /**
     * Expect specific character and advance
     * @param expected Expected character
     */
    private void expect(char expected) {
        if (position >= length) {
            throw new RuntimeException("Expected '" + expected + "' but reached end of input");
        }
        
        char actual = input.charAt(position);
        if (actual != expected) {
            throw new RuntimeException("Expected '" + expected + "' but found '" + actual + "'");
        }
        
        position++;
    }
}