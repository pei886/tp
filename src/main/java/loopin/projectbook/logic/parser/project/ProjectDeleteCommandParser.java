package loopin.projectbook.logic.parser.project;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.projectcommands.ProjectDeleteCommand;
import loopin.projectbook.logic.parser.ArgumentMultimap;
import loopin.projectbook.logic.parser.ArgumentTokenizer;
import loopin.projectbook.logic.parser.Parser;
import loopin.projectbook.logic.parser.ParserUtil;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link ProjectDeleteCommand}.
 *
 * Forms:
 *   INDEX
 *   n/NAME
 */
public class ProjectDeleteCommandParser implements Parser<ProjectDeleteCommand> {

    @Override
    public ProjectDeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (map.getValue(PREFIX_NAME).isPresent()) {
            String name = map.getValue(PREFIX_NAME).get().trim();
            if (name.isEmpty()) {
                throw usageError();
            }
            return new ProjectDeleteCommand(name);
        }

        String preamble = map.getPreamble().trim();
        if (preamble.isEmpty()) {
            throw usageError();
        }

        Index index = parseIndexOrUsage(preamble);
        return new ProjectDeleteCommand(index);
    }

    // ---- helpers ----

    private static ParseException usageError() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ProjectDeleteCommand.MESSAGE_USAGE));
    }

    private static Index parseIndexOrUsage(String preamble) throws ParseException {
        try {
            return ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw usageError();
        }
    }
}
