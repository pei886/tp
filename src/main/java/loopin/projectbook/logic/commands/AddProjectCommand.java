package loopin.projectbook.logic.commands;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.project.Project;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.parser.CliSyntax.*;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

public class AddProjectCommand extends Command {
    public static final String COMMAND_WORD = "project add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a project to the project book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Beach Cleanup "
            + PREFIX_DESCRIPTION + "Annual cleanup at East Coast ";

    public static final String MESSAGE_SUCCESS = "New project added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROJECT = "This project already exists in the project book";

    private final Project toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Project}
     */
    public AddProjectCommand(Project project) {
        requireNonNull(project);
        toAdd = project;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasProject(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PROJECT);
        }

        model.addProject(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatProject(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddProjectCommand)) {
            return false;
        }

        AddProjectCommand otherAddProjectCommand = (AddProjectCommand) other;
        return toAdd.equals(otherAddProjectCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
