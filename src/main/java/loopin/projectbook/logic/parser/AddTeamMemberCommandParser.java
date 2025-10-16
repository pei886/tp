package loopin.projectbook.logic.parser;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;

import loopin.projectbook.logic.commands.AddTeamMemberCommand;
import loopin.projectbook.logic.parser.exceptions.ParseException;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.teammember.Committee;
import loopin.projectbook.model.teammember.TeamMember;

import java.util.stream.Stream;

/**
 * Parses user input to create a new AddTeamMember object
 */
public class AddTeamMemberCommandParser implements Parser<AddTeamMemberCommand>{

    public AddTeamMemberCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COMMITEE, PREFIX_PHONE, PREFIX_EMAIL);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_COMMITEE, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTeamMemberCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_COMMITEE, PREFIX_PHONE, PREFIX_EMAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Committee committee = ParserUtil.parseCommittee(argMultimap.getValue(PREFIX_COMMITEE).get());

        TeamMember member = new TeamMember(name, phone, email, committee);

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
