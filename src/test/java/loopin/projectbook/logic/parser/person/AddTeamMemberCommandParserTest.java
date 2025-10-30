package loopin.projectbook.logic.parser.person;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.commands.CommandTestUtil.*;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.commands.personcommands.AddTeamMemberCommand;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;

public class AddTeamMemberCommandParserTest {

    private final AddTeamMemberCommandParser parser = new AddTeamMemberCommandParser();

    private static final String VALID_COMMITTEE_ALPHA = "Alpha";
    private static final String COMMITTEE_DESC_ALPHA = " c/" + VALID_COMMITTEE_ALPHA;
    private static final String INVALID_COMMITTEE_DESC = " c/"; // empty

    @Test
    public void parse_allFieldsPresent_success() {
        TeamMember expectedMember = new TeamMember(
                new Name(VALID_NAME_BOB),
                new Committee(VALID_COMMITTEE_ALPHA),
                java.util.Optional.of(new Phone(VALID_PHONE_BOB)),
                new Email(VALID_EMAIL_BOB),
                java.util.Optional.of(new Telegram(VALID_TELEGRAM_BOB)),
                new java.util.HashSet<>(),
                new java.util.ArrayList<>()
        );

        assertParseSuccess(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                new AddTeamMemberCommand(expectedMember));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // Missing phone and telegram
        TeamMember expectedMember = new TeamMember(
                new Name(VALID_NAME_BOB),
                new Committee(VALID_COMMITTEE_ALPHA),
                java.util.Optional.empty(),
                new Email(VALID_EMAIL_BOB),
                java.util.Optional.empty(),
                new java.util.HashSet<>(),
                new java.util.ArrayList<>()
        );

        assertParseSuccess(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + EMAIL_DESC_BOB,
                new AddTeamMemberCommand(expectedMember));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddTeamMemberCommand.MESSAGE_USAGE);

        // Missing name
        assertParseFailure(parser,
                COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);

        // Missing committee
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);

        // Missing email
        assertParseFailure(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + INVALID_PHONE_DESC + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + INVALID_EMAIL_DESC + TELEGRAM_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid telegram
        assertParseFailure(parser,
                NAME_DESC_BOB + COMMITTEE_DESC_ALPHA + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_TELEGRAM_DESC,
                Telegram.MESSAGE_CONSTRAINTS);

        // empty committee
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_COMMITTEE_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                String.format(Committee.MESSAGE_CONSTRAINTS));
    }
}
