package Butler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    public static String[] splitCommand(String input) {
        int space = input.indexOf(' ');
        if (space == -1) return new String[] { input, "" };
        return new String[] { input.substring(0, space), input.substring(space + 1) };
    }

    public static String[] splitOnce(String s, String delim) {
        int pos = s.indexOf(delim);
        return new String[] { s.substring(0, pos), s.substring(pos + delim.length()) };
    }

    // ---- Level 8 date parsing helpers ----
    public static LocalDate parseLocalDate(String s) throws ButlerException {
        try {
            return LocalDate.parse(s); // yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
        }
    }

    public static LocalDateTime parseLocalDateTime(String s) throws ButlerException {
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        try {
            if (s.contains("T")) {
                return LocalDateTime.parse(s); // ISO-8601, e.g., 2019-10-15T18:00
            } else if (s.contains(":")) {
                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(s, f2);
            } else {
                return LocalDateTime.parse(s, f1);
            }
        } catch (DateTimeParseException e) {
            throw new ButlerException("Please use datetime format 'yyyy-MM-dd HHmm' or ISO 'yyyy-MM-ddTHH:mm'.");
        }
    }
}

