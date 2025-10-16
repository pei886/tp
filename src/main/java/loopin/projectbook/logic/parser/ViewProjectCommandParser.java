package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PROJECT;

import java.util.stream.Stream;

import loopin.projectbook.logic.commands.ViewProjectCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.project.ProjectName;

/**
 * Parses input arguments and creates a new ViewProjectCommand object
 */
public class ViewProjectCommandParser implements Parser<ViewProjectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewProjectCommand
     * and returns a ViewProjectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewProjectCommand parse(String args) throws ParseException {
        System.out.println("DEBUG Parser: args = '" + args + "'");

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROJECT);
        System.out.println("DEBUG Parser: preamble = '" + argMultimap.getPreamble() + "'");
        System.out.println("DEBUG Parser: PREFIX_PROJECT present = " + arePrefixesPresent(argMultimap, PREFIX_PROJECT));

        if (!arePrefixesPresent(argMultimap, PREFIX_PROJECT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewProjectCommand.MESSAGE_USAGE));
        }

        ProjectName projectName = new ProjectName(ParserUtil.parseProjectName(
                argMultimap.getValue(PREFIX_PROJECT).get()).toString());
        return new ViewProjectCommand(projectName);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
