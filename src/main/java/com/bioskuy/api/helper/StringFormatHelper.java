package com.bioskuy.api.helper;

/**
 * A utility class for common string formatting operations.
 * This class cannot be instantiated, and all methods are static.
 */
public final class StringFormatHelper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StringFormatHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Capitalizes the first letter of a given string.
     * If the string is null or empty, it will be returned as is.
     * <p>
     * Example: "hello world" will become "Hello world".
     *
     * @param str The input string.
     * @return The string with its first letter capitalized, or the original string if it's null or empty.
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
