package loopin.projectbook.logic.commands.personcommands;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import loopin.projectbook.model.person.teammember.TeamMember;

/**
 * Adds a {@code TeamMember} to the project book.
 * The {@code AddTeamMemberCommand} creates and adds a new team member with the
 * specified details (name, committee, phone, email) into the project book.
 * If the member already exists, a {@code CommandException} is thrown.
 */
public class AddTeamMemberCommand extends AddCommand {

    public static final String COMMAND_WORD = "addt";
    public static final String MESSAGE_SUCCESS = "New team member added: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a team member to the project book. "
            + "\nParameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_COMMITEE + "COMMITTEE "
            + "[" + PREFIX_PHONE + "PHONE] "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_TELEGRAM + "TELEGRAM] "
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jane Doe "
            + PREFIX_COMMITEE + "Operations "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "janed@example.com "
            + PREFIX_TELEGRAM + "jane_d ";

    /**
     * Creates an {@code AddTeamMemberCommand} to add the specified {@code TeamMember}.
     *
     * @param teamMember the team member to be added to the project book, must not be {@code null}.
     */
    public AddTeamMemberCommand(TeamMember teamMember) {
        super(teamMember);
    }

    @Override
    public String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AddTeamMemberCommand)) {
            return false;
        } else {
            return super.equals(other);
        }
    }

}
