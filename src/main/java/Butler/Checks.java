package Butler;

/**
 * Utility class providing common validation checks for user input and task indices.
 * <p>
 * All methods throw a {@link ButlerException} with a descriptive message
 * when the validation fails.
 */
public class Checks {
    private Checks() {} // prevent instantiation

    /**
     * Ensures that a given string is not null or empty.
     *
     * @param s   the string to check
     * @param msg the error message to include in the exception
     * @throws ButlerException if the string is null or empty
     */
    public static void ensureNonEmpty(String s, String msg) throws ButlerException {
        if (s == null || s.isEmpty()) throw new ButlerException(msg);
    }

    /**
     * Ensures that a given string contains the specified substring.
     *
     * @param s      the string to search
     * @param needle the substring that must be present
     * @param msg    the error message to include in the exception
     * @throws ButlerException if the string is null or does not contain the substring
     */
    public static void ensureContains(String s, String needle, String msg) throws ButlerException {
        if (s == null || !s.contains(needle)) throw new ButlerException(msg);
    }

    /**
     * Parses a string into an integer task index.
     *
     * @param s the string to parse
     * @return the integer value of the string
     * @throws ButlerException if the string cannot be parsed into a valid integer
     */
    public static int parseIndex(String s) throws ButlerException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new ButlerException("Please provide a valid task number.");
        }
    }

    /**
     * Ensures that a given task index is within the valid range of task list indices.
     *
     * @param idx  the index to check (1-based)
     * @param size the size of the task list
     * @param msg  the error message to include in the exception
     * @throws ButlerException if the index is less than 1 or greater than the list size
     */
    public static void ensureIndexInRange(int idx, int size, String msg) throws ButlerException {
        if (idx < 1 || idx > size) throw new ButlerException(msg);
    }
}
