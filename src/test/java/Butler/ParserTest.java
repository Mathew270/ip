package Butler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void splitCommand_noSpace_returnsCommandAndEmptyArgs() {
        assertArrayEquals(new String[]{"list", ""}, Parser.splitCommand("list"));
    }

    @Test
    void splitCommand_singleSpace_returnsCommandAndArgs() {
        assertArrayEquals(new String[]{"todo", "read book"}, Parser.splitCommand("todo read book"));
    }

    @Test
    void splitCommand_multipleSpaces_preservesRemainingVerbatim() {
        assertArrayEquals(new String[]{"todo", "   read"}, Parser.splitCommand("todo    read"));
    }

    @Test
    void splitOnce_multipleOccurrences_splitsAtFirstOnly() {
        String[] p = Parser.splitOnce("deadline a /by 2024-10-10 /by extra", " /by ");
        assertArrayEquals(new String[]{"deadline a", "2024-10-10 /by extra"}, p);
    }

    @Test
    void parseLocalDate_validIso_returnsDateObject() throws ButlerException {
        LocalDate d = Parser.parseLocalDate("2019-10-15");
        assertEquals(LocalDate.of(2019, 10, 15), d);
    }

    @Test
    void parseLocalDate_invalidFormat_throwsButlerException() {
        Executable ex = () -> Parser.parseLocalDate("15/10/2019");
        ButlerException be = assertThrows(ButlerException.class, ex);
        assertTrue(be.getMessage().contains("yyyy-MM-dd"));
    }

    @Test
    void parseLocalDateTime_isoTFormat_returnsDateTime() throws ButlerException {
        LocalDateTime dt = Parser.parseLocalDateTime("2019-12-02T18:00");
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), dt);
    }

    @Test
    void parseLocalDateTime_spaceHHmmFormat_returnsDateTime() throws ButlerException {
        LocalDateTime dt = Parser.parseLocalDateTime("2019-12-02 1400");
        assertEquals(LocalDateTime.of(2019, 12, 2, 14, 0), dt);
    }

    @Test
    void parseLocalDateTime_spaceHHColonMMFormat_returnsDateTime() throws ButlerException {
        LocalDateTime dt = Parser.parseLocalDateTime("2019-12-02 14:30");
        assertEquals(LocalDateTime.of(2019, 12, 2, 14, 30), dt);
    }

    @Test
    void parseLocalDateTime_invalidFormat_throwsButlerException() {
        Executable ex = () -> Parser.parseLocalDateTime("02/12/2019 6pm");
        ButlerException be = assertThrows(ButlerException.class, ex);
        assertTrue(be.getMessage().contains("yyyy-MM-dd HHmm") || be.getMessage().contains("yyyy-MM-ddTHH:mm"));
    }
}

