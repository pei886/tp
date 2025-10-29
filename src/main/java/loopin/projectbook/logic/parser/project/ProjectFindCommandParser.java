package loopin.projectbook.logic.parser.project;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import loopin.projectbook.logic.commands.projectcommands.ProjectFindCommand;
import loopin.projectbook.logic.parser.Parser;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.project.ProjectNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new ProjectFindCommand object.
 */
public class ProjectFindCommandParser implements Parser<ProjectFindCommand> {

    @Override
    public ProjectFindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProjectFindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new ProjectFindCommand(
                new ProjectNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
