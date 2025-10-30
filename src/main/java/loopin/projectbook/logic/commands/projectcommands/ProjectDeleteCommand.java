package loopin.projectbook.logic.commands.projectcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX;
import static loopin.projectbook.logic.Messages.MESSAGE_NO_PROJECT;

import java.util.List;
import java.util.Optional;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.project.Project;

/**
 * Deletes an existing project by index in the current filtered list, or by exact (case-insensitive) name.
 *
 * Usage:
 * {@code
 * project delete INDEX
 * project delete n/NAME
 * }
 *
 * Cascading cleanup (e.g., removing back-references from members) should be handled by
 * {@link Model#deleteProject(Project)}.
 */
public class ProjectDeleteCommand extends Command {

    public static final String COMMAND_WORD = "project";
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

    /** Creates a delete-by-index command. */
    public ProjectDeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /** Creates a delete-by-name command. */
    public ProjectDeleteCommand(String targetName) {
        requireNonNull(targetName);
        String trimmed = targetName.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be blank.");
        }

        this.targetIndex = null;
        this.targetName = trimmed;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (targetIndex != null) {
            List<Project> lastShownList = model.getFilteredProjectList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
            }
            Project toDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deleteProject(toDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PROJECT_SUCCESS, toDelete.getName()));
        }

        Optional<Project> match = model.findProjectByName(targetName);
        if (match.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_PROJECT, targetName));
        }
        Project toDelete = match.get();
        model.deleteProject(toDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PROJECT_SUCCESS, toDelete.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ProjectDeleteCommand)) {
            return false;
        }
        ProjectDeleteCommand o = (ProjectDeleteCommand) other;
        return java.util.Objects.equals(targetIndex, o.targetIndex)
                && java.util.Objects.equals(targetName, o.targetName);
    }
}
