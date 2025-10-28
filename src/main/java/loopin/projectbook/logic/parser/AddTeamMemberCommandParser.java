package loopin.projectbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TAG;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import loopin.projectbook.logic.commands.AddTeamMemberCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.tag.Tag;

/**
 * Parses user input to create a new AddTeamMember object
 */
public class AddTeamMemberCommandParser implements Parser<AddTeamMemberCommand> {

    /**
     * Parses the given {@code String} of arguments and constructs an {@code AddTeamMemberCommand}.
     * The input string is tokenized using the defined prefixes {@code n/}, {@code c/}, {@code p/},
     * {@code e/} {@code t/} corresponding to the team memmber's name, committee, phone, email, and telegram.
     *
     * @param args the full command arguments string to parse
     * @return an {@code AddTeamMemberCommand} object representing the parsed input
     * @throws ParseException if the input format is invalid or required fields are missing
     */
    public AddTeamMemberCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COMMITEE,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_COMMITEE, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTeamMemberCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_COMMITEE,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Telegram telegram = ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get());
        Committee committee = ParserUtil.parseCommittee(argMultimap.getValue(PREFIX_COMMITEE).get());
        Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<Remark> remarks = new HashSet<Remark>();
        List<Project> projects = new ArrayList<Project>();

        TeamMember member = new TeamMember(name, committee, phone, email, telegram, tags, remarks, projects);


        return new AddTeamMemberCommand(member);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
