package loopin.projectbook.logic.parser.person;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import loopin.projectbook.logic.commands.personcommands.FindRoleCommand;
import loopin.projectbook.logic.parser.Parser;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.person.RoleMatchesPredicate;

/**
 * Parses input arguments and creates a new FindRoleCommand object.
 */
public class FindRoleCommandParser implements Parser<FindRoleCommand> {

    @Override
    public FindRoleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();

        if (trimmedArgs.isEmpty() || trimmedArgs.length() != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindRoleCommand.MESSAGE_USAGE));
        }

        char roleChar = trimmedArgs.charAt(0);

        if (roleChar == 't' || roleChar == 'v' || roleChar == 'o') {
            return new FindRoleCommand(new RoleMatchesPredicate(roleChar));
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindRoleCommand.MESSAGE_USAGE));
        }
    }
}
