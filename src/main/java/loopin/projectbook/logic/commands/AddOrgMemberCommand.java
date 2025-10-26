package loopin.projectbook.logic.commands;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TAG;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import loopin.projectbook.model.person.orgmember.OrgMember;

/**
 * Adds an organisation member to the project book.
 */
public class AddOrgMemberCommand extends AddCommand {

    public static final String COMMAND_WORD = "addo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an organisation member to the project book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ORGANISATION + "ORGANISATION "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_TELEGRAM + "TELEGRAM "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_TELEGRAM + "johndoe123 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New Organisation Member added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the project book";

    /**
     * Creates an AddOrgMemberCommand to add the specified {@code OrgMember}
     */
    public AddOrgMemberCommand(OrgMember orgMember) {
        super(orgMember);
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        // instanceof handles nulls
        if (!(other instanceof AddOrgMemberCommand)) {
            return false;
        } else {
            return super.equals(other);
        }
    }
}
