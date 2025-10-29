package loopin.projectbook.logic.commands.personcommands;

import static java.util.Objects.requireNonNull;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.RoleMatchesPredicate;

/**
 * Finds and lists all persons in the project book whose role matches the specified role type.
 * Accepted role shortcuts: t (Team Member), v (Volunteer), o (Organisation Member)
 */
public class FindRoleCommand extends Command {

    public static final String COMMAND_WORD = "findrole";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons by role.\n"
            + "Parameters: Role letter(t/v/o)\n"
            + "Example: " + COMMAND_WORD + " t\n"
            + "returns a list of all team members";

    private final RoleMatchesPredicate predicate;

    public FindRoleCommand(RoleMatchesPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()),
                false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindRoleCommand
                && predicate.equals(((FindRoleCommand) other).predicate));
    }
}