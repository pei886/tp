package loopin.projectbook.logic.commands.personcommands;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import loopin.projectbook.model.person.orgmember.OrgMember;

/**
 * Adds an {@code OrgMember} to the project book.
 */
public class AddOrgMemberCommand extends AddCommand {

    public static final String COMMAND_WORD = "addo";
    public static final String MESSAGE_SUCCESS = "New organisation member added: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an organisation member to the project book. "
            + "\nParameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ORGANISATION + "ORGANISATION "
            + "[" + PREFIX_PHONE + "PHONE] "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_TELEGRAM + "TELEGRAM] "
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Shi Wei "
            + PREFIX_ORGANISATION + "Eventive Solutions "
            + PREFIX_PHONE + "98381263 "
            + PREFIX_EMAIL + "shiwei@eventive.com "
            + PREFIX_TELEGRAM + "ShiWei623 ";


    /**
     * Creates an AddOrgMemberCommand to add the specified {@code OrgMember}
     */
    public AddOrgMemberCommand(OrgMember orgMember) {
        super(orgMember);
    }

    @Override
    public String getSuccessMessage() {
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
