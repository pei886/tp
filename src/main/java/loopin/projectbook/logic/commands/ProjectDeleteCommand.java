package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.project.Project;

/**
 * Deletes a project either by its index in the currently displayed project list
 * or by its name.
 */
public class ProjectDeleteCommand extends Command {

    public static final String COMMAND_WORD = "project delete";
    public static final String SUBCOMMAND = "delete";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a project.\n"
            + "Formats:\n"
            + "project delete INDEX\n"
            + "project delete n/NAME\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " 2\n"
            + "  " + COMMAND_WORD + " n/Website Revamp";

    public static final String MESSAGE_DELETE_PROJECT_SUCCESS = "Deleted project: %s";

    private final Index targetIndex;
    private final String targetName;

    /** Deletes by index. */
    public ProjectDeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /** Deletes by name. */
    public ProjectDeleteCommand(String targetName) {
        requireNonNull(targetName);
        this.targetIndex = null;
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetIndex != null) {
            List<Project> lastShownList = model.getFilteredProjectList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }
            Project toDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deleteProject(toDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PROJECT_SUCCESS, toDelete.getName()));
        }

        Optional<Project> match = model.findProjectByName(targetName);
        if (match.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_PROJECT_NOT_FOUND_BY_NAME, targetName));
        }
        Project toDelete = match.get();
        model.deleteProject(toDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PROJECT_SUCCESS, toDelete.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) { return true; }
        if (!(other instanceof ProjectDeleteCommand)) { return false; }
        ProjectDeleteCommand o = (ProjectDeleteCommand) other;
        return java.util.Objects.equals(targetIndex, o.targetIndex)
                && java.util.Objects.equals(targetName, o.targetName);
    }
}
