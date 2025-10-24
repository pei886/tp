package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.teammember.TeamMember;

/**
 * Adds a {@code TeamMember} to the project book.
 * The {@code AddTeamMemberCommand} creates and adds a new team member with the
 * specified details (name, committee, phone, email) into the project book.
 * If the member already exists, a {@code CommandException} is thrown.
 */
public class AddTeamMemberCommand extends Command {

    public static final String COMMAND_WORD = "addt";

    public static final String MESSAGE_SUCCESS = "New team member added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the project book";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a team member to the project book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_COMMITEE + "COMMITTEE "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_COMMITEE + "Operations"
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com ";

    private final TeamMember toAdd;

    /**
     * Creates an {@code AddTeamMemberCommand} to add the specified {@code TeamMember}.
     *
     * @param member the team member to be added to the project book, must not be {@code null}
     */
    public AddTeamMemberCommand(TeamMember member) {
        requireNonNull(member);
        this.toAdd = member;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatPerson(toAdd)));
    }
}
