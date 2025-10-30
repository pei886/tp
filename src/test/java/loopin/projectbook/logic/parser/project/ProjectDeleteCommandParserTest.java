package loopin.projectbook.logic.parser.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.projectcommands.ProjectDeleteCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link ProjectDeleteCommandParser}.
 */
public class ProjectDeleteCommandParserTest {

    private final ProjectDeleteCommandParser parser = new ProjectDeleteCommandParser();

    @Test
    public void parse_indexForm_success() throws Exception {
        // tests: "1"
        ProjectDeleteCommand cmd = parser.parse("1");
        assertNotNull(cmd);
    }

    @Test
    public void parse_nameForm_success() throws Exception {
        // tests: " n/Website Revamp  "
        ProjectDeleteCommand cmd = parser.parse(" n/Website Revamp  ");
        assertNotNull(cmd);
    }

    @Test
    public void parse_blankName_failure() {
        // tests: "n/   " -> fail
        assertThrows(ParseException.class, () -> parser.parse("n/   "));
    }

    @Test
    public void parse_emptyArgs_failure() {
        // tests: "" -> fail
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parse_nonNumericIndex_failure() {
        // tests: "abc" -> fail
        assertThrows(ParseException.class, () -> parser.parse("abc"));
    }
}
