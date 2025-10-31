package loopin.projectbook.logic.parser.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.projectcommands.ProjectRemoveCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link ProjectRemoveCommandParser}.
 */
public class ProjectRemoveCommandParserTest {

    private final ProjectRemoveCommandParser parser = new ProjectRemoveCommandParser();

    @Test
    public void parse_indexForm_success() throws Exception {
        // tests: "1 project/Website" -> remove person at index 1 from project
        ProjectRemoveCommand cmd = parser.parse("1 project/Website");
        assertNotNull(cmd);
    }

    @Test
    public void parse_nameForm_success() throws Exception {
        // tests: "n/Alex Tan project/Website"
        ProjectRemoveCommand cmd = parser.parse("n/Alex Tan project/Website");
        assertNotNull(cmd);
    }

    @Test
    public void parse_missingProject_failure() {
        // tests: no project/ -> fail
        assertThrows(ParseException.class, () -> parser.parse("1"));
    }

    @Test
    public void parse_missingIndexAndName_failure() {
        // tests: neither index nor n/ -> fail
        assertThrows(ParseException.class, () -> parser.parse("project/Alpha"));
    }
}
