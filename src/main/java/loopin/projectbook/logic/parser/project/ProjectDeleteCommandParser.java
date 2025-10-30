package loopin.projectbook.logic.parser.project;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.projectcommands.ProjectDeleteCommand;
import loopin.projectbook.logic.parser.ArgumentMultimap;
import loopin.projectbook.logic.parser.ArgumentTokenizer;
import loopin.projectbook.logic.parser.Parser;
import loopin.projectbook.logic.parser.ParserUtil;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ProjectDeleteCommand object.
 * Accepts either:
 *   - a single positional index, or
 *   - n/NAME
 */
public class ProjectDeleteCommandParser implements Parser<ProjectDeleteCommand> {

    @Override
    public ProjectDeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        Optional<String> nameOpt = argMultimap.getValue(PREFIX_NAME);
        if (nameOpt.isPresent()) {
            String name = nameOpt.get().trim();
            if (name.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ProjectDeleteCommand.MESSAGE_USAGE));
            }
            return new ProjectDeleteCommand(name);
        }

        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ProjectDeleteCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(preamble);
            return new ProjectDeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ProjectDeleteCommand.MESSAGE_USAGE));
        }
    }
}
