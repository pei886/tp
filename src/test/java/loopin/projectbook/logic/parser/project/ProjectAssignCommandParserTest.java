package loopin.projectbook.logic.parser.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.projectcommands.ProjectAssignCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link ProjectAssignCommandParser}.
 */
public class ProjectAssignCommandParserTest {

    private final ProjectAssignCommandParser parser = new ProjectAssignCommandParser();

    @Test
    public void parse_indexForm_success() throws Exception {
        // tests: "1 project/Website Revamp"
        ProjectAssignCommand cmd = parser.parse("1 project/Website Revamp");
        assertNotNull(cmd);
    }

    @Test
    public void parse_nameForm_success() throws Exception {
        // tests: "n/Alex Tan project/Website Revamp"
        ProjectAssignCommand cmd = parser.parse("n/Alex Tan project/Website Revamp");
        assertNotNull(cmd);
    }

    @Test
    public void parse_missingProject_failure() {
        // tests: missing project/ -> fail
        assertThrows(ParseException.class, () -> parser.parse("1"));
    }

    @Test
    public void parse_missingIndexAndName_failure() {
        // tests: no index and no n/ -> fail
        assertThrows(ParseException.class, () -> parser.parse("project/Website"));
    }
}
