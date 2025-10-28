package loopin.projectbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.ResolveRemarkCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;

//TODO: FIX IMPLEMENTATION. It doesn't seem to resolve a remark completely.

/**
 * Parses input arguments and creates a new ResolveRemarkCommand object.
 */
public class ResolveRemarkCommandParser implements Parser<ResolveRemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ResolveRemarkCommand
     * and returns a ResolveRemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ResolveRemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Tokenize without any prefixes, as both arguments are indices in the preamble.
        // We use ArgumentTokenizer defensively, even with no prefixes.
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        // Preamble contains both PERSON_INDEX and REMARK_INDEX, separated by space.
        String trimmedPreamble = argMultimap.getPreamble().trim();

        if (trimmedPreamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResolveRemarkCommand.MESSAGE_USAGE));
        }

        // Split the preamble into two tokens (index strings)
        List<String> indexStrings = Arrays.asList(trimmedPreamble.split("\\s+"));

        if (indexStrings.size() != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResolveRemarkCommand.MESSAGE_USAGE));
        }

        Index personIndex;
        Index remarkIndex;

        try {
            // First token is the PERSON_INDEX
            personIndex = ParserUtil.parseIndex(indexStrings.get(0));
            // Second token is the REMARK_INDEX
            remarkIndex = ParserUtil.parseIndex(indexStrings.get(1));
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResolveRemarkCommand.MESSAGE_USAGE), pe);
        }

        return new ResolveRemarkCommand(personIndex, remarkIndex);
    }
}
