package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TAG;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import loopin.projectbook.logic.commands.AddOrgMemberCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddOrgMemberCommand object
 */
public class AddOrgMemberCommandParser implements Parser<AddOrgMemberCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddOrgMemberCommand
     * and returns an AddOrgMemberCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddOrgMemberCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ORGANISATION,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ORGANISATION, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddOrgMemberCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ORGANISATION, PREFIX_PHONE, PREFIX_EMAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Organisation organisation = ParserUtil.parseOrganisation(argMultimap.getValue(PREFIX_ORGANISATION).get());
        Optional<Phone> phone =
                argMultimap.getValue(PREFIX_PHONE).isPresent()
                ? Optional.of(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()))
                : Optional.empty();
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Optional<Telegram> telegram =
                argMultimap.getValue(PREFIX_TELEGRAM).isPresent()
                ? Optional.of(ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get()))
                : Optional.empty();
        Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<Remark> remarks = new HashSet<>();
        List<Project> projects = new ArrayList<>();

        OrgMember orgMember = new OrgMember(name, organisation, phone, email, telegram, tags, remarks, projects);

        return new AddOrgMemberCommand(orgMember);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
