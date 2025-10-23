package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.RemarkCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
// Assuming Prefix, ArgumentTokenizer, and ParserUtil exist
// import java.util.stream.Stream;

/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    // Define the prefix needed for the update content (u/UPDATE)
    // In a real project, this would be defined in CliSyntax.
    private static final Prefix PREFIX_UPDATE = new Prefix("u/");

    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        // Assume ArgumentTokenizer and ArgumentMultimap exist for robust parsing
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_UPDATE);

        // Check for required update prefix and ensure an index (preamble) is present
        if (!argMultimap.getValue(PREFIX_UPDATE).isPresent() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        Index index;

        try {
            // Preamble is expected to be the one-based index
            // Assuming ParserUtil.parseIndex exists to handle number conversion and validation
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), pe);
        }

        String remarkContent = argMultimap.getValue(PREFIX_UPDATE).get().trim();
        if (remarkContent.isEmpty()) {
            throw new ParseException(RemarkCommand.MESSAGE_EMPTY_REMARK);
        }

        return new RemarkCommand(index, remarkContent);
    }
}
