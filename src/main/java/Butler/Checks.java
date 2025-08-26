package Butler;

public class Checks {
    private Checks() {} // utility class

    public static void ensureNonEmpty(String s, String msg) throws ButlerException {
        if (s == null || s.isEmpty()) throw new ButlerException(msg);
    }

    public static void ensureContains(String s, String needle, String msg) throws ButlerException {
        if (s == null || !s.contains(needle)) throw new ButlerException(msg);
    }

    public static int parseIndex(String s) throws ButlerException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new ButlerException("Please provide a valid task number.");
        }
    }

    public static void ensureIndexInRange(int idx, int size, String msg) throws ButlerException {
        if (idx < 1 || idx > size) throw new ButlerException(msg);
    }
}

