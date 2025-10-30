package loopin.projectbook.logic.parser.person;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.personcommands.FindRoleCommand;
import loopin.projectbook.model.person.RoleMatchesPredicate;

public class FindRoleCommandParserTest {

    private final FindRoleCommandParser parser = new FindRoleCommandParser();

    @Test
    public void parse_validArgs_success() {
        // Team Member
        assertParseSuccess(parser, "t", new FindRoleCommand(new RoleMatchesPredicate('t')));
        // Volunteer
        assertParseSuccess(parser, "v", new FindRoleCommand(new RoleMatchesPredicate('v')));
        // OrgMember
        assertParseSuccess(parser, "o", new FindRoleCommand(new RoleMatchesPredicate('o')));
        // Uppercase should work too
        assertParseSuccess(parser, "T", new FindRoleCommand(new RoleMatchesPredicate('t')));
    }

    @Test
    public void parse_invalidArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindRoleCommand.MESSAGE_USAGE);

        // Empty string
        assertParseFailure(parser, "", expectedMessage);

        // Too long
        assertParseFailure(parser, "tv", expectedMessage);

        // Invalid letter
        assertParseFailure(parser, "x", expectedMessage);

        // Random word
        assertParseFailure(parser, "team", expectedMessage);

        // Extra spaces
        assertParseFailure(parser, "   ", expectedMessage);
    }
}
