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
import static loopin.projectbook.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static loopin.projectbook.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static loopin.projectbook.logic.commands.CommandTestUtil.TELEGRAM_DESC_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.TELEGRAM_DESC_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static loopin.projectbook.logic.commands.CommandTestUtil.VALID_TELEGRAM_AMY;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TAG;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static loopin.projectbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static loopin.projectbook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static loopin.projectbook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static loopin.projectbook.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.EditCommand;
import loopin.projectbook.logic.commands.EditCommand.EditPersonDescriptor;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.tag.Tag;
import loopin.projectbook.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TELEGRAM_DESC, Telegram.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_TELEGRAM_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + TELEGRAM_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withTelegram(VALID_TELEGRAM_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_THIRD_PERSON;

        // name
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        assertParseSuccess(parser, userInput, new EditCommand(targetIndex, descriptor));

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        assertParseSuccess(parser, userInput, new EditCommand(targetIndex, descriptor));

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        assertParseSuccess(parser, userInput, new EditCommand(targetIndex, descriptor));

        // telegram
        userInput = targetIndex.getOneBased() + TELEGRAM_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withTelegram(VALID_TELEGRAM_AMY).build();
        assertParseSuccess(parser, userInput, new EditCommand(targetIndex, descriptor));

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, userInput, new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + TELEGRAM_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_FRIEND + PHONE_DESC_AMY + TELEGRAM_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + TELEGRAM_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM));

        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_TELEGRAM_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_TELEGRAM_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
