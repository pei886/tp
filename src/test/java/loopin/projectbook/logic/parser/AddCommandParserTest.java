package loopin.projectbook.logic.parser;

import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static loopin.projectbook.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static loopin.projectbook.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static loopin.projectbook.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static loopin.projectbook.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static loopin.projectbook.logic.commands.CommandTestUtil.INVALID_TELEGRAM_DESC;
import static loopin.projectbook.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static loopin.projectbook.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static loopin.projectbook.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static loopin.projectbook.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static loopin.projectbook.logic.commands.CommandTestUtil.TELEGRAM_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.TELEGRAM_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static loopin.projectbook.testutil.TypicalPersons.AMY;
import static loopin.projectbook.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.AddVolunteerCommand;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.volunteer.Volunteer;
import loopin.projectbook.model.tag.Tag;
import loopin.projectbook.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddVolunteerCommandParser parser = new AddVolunteerCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + TELEGRAM_DESC_BOB + TAG_DESC_FRIEND,
                new AddVolunteerCommand((Volunteer) expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + TELEGRAM_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddVolunteerCommand((Volunteer) expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString =
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple telegrams
        assertParseFailure(parser, TELEGRAM_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TELEGRAM));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser,
                NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + TELEGRAM_DESC_AMY,
                new AddVolunteerCommand((Volunteer) expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddVolunteerCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + TELEGRAM_DESC_BOB,
                expectedMessage);

        // missing telegram prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_TELEGRAM_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_TELEGRAM_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + TELEGRAM_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Email.MESSAGE_CONSTRAINTS);

        // invalid telegram
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_TELEGRAM_DESC
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Telegram.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB
                        + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_TELEGRAM_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + TELEGRAM_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddVolunteerCommand.MESSAGE_USAGE));
    }
}
