package loopin.projectbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Optional;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.EditCommand;
import loopin.projectbook.logic.commands.EditCommand.EditPersonDescriptor;
import loopin.projectbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link EditCommand}.
 */
public class EditCommandParser implements Parser<EditCommand> {

    @Override
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                args,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM,
                PREFIX_COMMITEE, PREFIX_ORGANISATION
        );

        Index index = parseIndexOrThrow(map.getPreamble());

        map.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM,
                PREFIX_COMMITEE, PREFIX_ORGANISATION
        );

        EditPersonDescriptor descriptor = buildDescriptor(map);

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, descriptor);
    }

    // ---- helpers ----

    private static Index parseIndexOrThrow(String preamble) throws ParseException {
        try {
            return ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE),
                    pe
            );
        }
    }

    private static EditPersonDescriptor buildDescriptor(ArgumentMultimap map) throws ParseException {
        EditPersonDescriptor d = new EditPersonDescriptor();

        if (map.getValue(PREFIX_NAME).isPresent()) {
            d.setName(ParserUtil.parseName(map.getValue(PREFIX_NAME).get()));
        }
        if (map.getValue(PREFIX_PHONE).isPresent()) {
            d.setPhone(Optional.of(ParserUtil.parsePhone(map.getValue(PREFIX_PHONE).get())));
        }
        if (map.getValue(PREFIX_EMAIL).isPresent()) {
            d.setEmail(ParserUtil.parseEmail(map.getValue(PREFIX_EMAIL).get()));
        }
        if (map.getValue(PREFIX_TELEGRAM).isPresent()) {
            d.setTelegram(Optional.of(ParserUtil.parseTelegram(map.getValue(PREFIX_TELEGRAM).get())));
        }
        if (map.getValue(PREFIX_COMMITEE).isPresent()) {
            d.setCommittee(ParserUtil.parseCommittee(map.getValue(PREFIX_COMMITEE).get()));
        }
        if (map.getValue(PREFIX_ORGANISATION).isPresent()) {
            d.setOrganisation(ParserUtil.parseOrganisation(map.getValue(PREFIX_ORGANISATION).get()));
        }

        return d;
    }

    private static Optional<String> get(ArgumentMultimap map, Prefix prefix) {
        return map.getValue(prefix);
    }
}
