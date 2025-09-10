package Butler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing user input into commands and arguments,
 * and for converting string representations of dates and times
 * into strongly typed {@link LocalDate} and {@link LocalDateTime} objects.
 */
public class Parser {

    /** Public delimiters to keep consistency across classes. */
    public static final String DELIM_BY   = "/by ";
    public static final String DELIM_FROM = "/from ";
    public static final String DELIM_TO   = "/to ";

    /** Date-time patterns (avoid magic strings). */
    private static final DateTimeFormatter F_YYYY_MM_DD_HHMM =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter F_YYYY_MM_DD_HH_COLON_MM =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Splits a raw user command into the command word and its arguments.
     * <p>
     * For example, {@code "todo read book"} will be split into:
     * <pre>
     * ["todo", "read book"]
     * </pre>
     * If there is no space in the input, the second element will be an empty string.
     *
     * @param input the raw user input
     * @return a string array with two elements: the command and the arguments
     */
    public static String[] splitCommand(String input) {
        assert input != null : "input must not be null";
        int space = input.indexOf(' ');
        if (space == -1) return new String[] { input, "" };
        return new String[] { input.substring(0, space), input.substring(space + 1) };
    }

    /**
     * Splits a string into two parts at the first occurrence of the given delimiter.
     *
     * @param s     the string to split
     * @param delim the delimiter at which to split
     * @return a two-element string array: text before the delimiter and text after
     */
    public static String[] splitOnce(String s, String delim) {
        assert s != null && delim != null : "s and delim must not be null";
        assert s.contains(delim) : "Precondition: delimiter must exist in string";
        int pos = s.indexOf(delim);
        return new String[] { s.substring(0, pos), s.substring(pos + delim.length()) };
    }

    // ---- Level 8 date parsing helpers ----

    /**
     * Parses a string into a {@link LocalDate}.
     * <p>
     * The string must be in ISO format: {@code yyyy-MM-dd}.
     *
     * @param s the string to parse
     * @return the corresponding {@link LocalDate}
     * @throws ButlerException if the string is not in the expected format
     */
    public static LocalDate parseLocalDate(String s) throws ButlerException {
        assert s != null && !s.isBlank() : "date string must be non-null and non-blank";
        try {
            return LocalDate.parse(s); // yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
        }
    }

    /**
     * Parses a string into a {@link LocalDateTime}.
     * <p>
     * Supported formats:
     * <ul>
     *     <li>{@code yyyy-MM-ddTHH:mm} (ISO-8601, e.g., {@code 2019-10-15T18:00})</li>
     *     <li>{@code yyyy-MM-dd HHmm} (e.g., {@code 2019-10-15 1800})</li>
     *     <li>{@code yyyy-MM-dd HH:mm} (e.g., {@code 2019-10-15 18:00})</li>
     * </ul>
     *
     * @param s the string to parse
     * @return the corresponding {@link LocalDateTime}
     * @throws ButlerException if the string is not in one of the supported formats
     */
    public static LocalDateTime parseLocalDateTime(String s) throws ButlerException {
        assert s != null && !s.isBlank() : "datetime string must be non-null and non-blank";
        try {
            if (s.contains("T")) {
                return LocalDateTime.parse(s); // ISO-8601, e.g., 2019-10-15T18:00
            } else if (s.contains(":")) {
                return LocalDateTime.parse(s, F_YYYY_MM_DD_HH_COLON_MM);
            } else {
                return LocalDateTime.parse(s, F_YYYY_MM_DD_HHMM);
            }
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use datetime format 'yyyy-MM-dd HHmm' or ISO 'yyyy-MM-ddTHH:mm'.");
        }
    }
}
