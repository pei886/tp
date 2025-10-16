package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Removes a person (by index in the current person list) from an existing project.
 * Usage: {@code project remove INDEX project/PROJECT_NAME}
 *
 */
public final class ProjectRemoveCommand extends Command {
    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "remove";
    public static final String MESSAGE_USAGE =
            "project remove: Remove a person from a project.\n"
                    + "Format: project remove INDEX project/PROJECT_NAME\n"
                    + "Example: project remove 1 project/MyProject";

    public static final String MESSAGE_SUCCESS = "Removed %s from the project %s.";
    public static final String MESSAGE_NOT_IN = "%s is not in this project.";
    public static final String MESSAGE_NO_PROJECT = "Project '%s' does not exist.";
    public static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid.";

    private final Index index;
    private final ProjectName projectName;

    /**
     * Constructs a command to remove the person at {@code index} from the project named {@code projectName}.
     *
     * @param index index of the person in the current filtered person list
     * @param projectName the validated project name value object
     */
    public ProjectRemoveCommand(Index index, ProjectName projectName) {
        this.index = index;
        this.projectName = projectName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShown = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShown.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        String lookup = projectName.toString();
        Project project = model.findProjectByName(lookup)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_NO_PROJECT, lookup)));

        Person target = lastShown.get(index.getZeroBased());
        if (!project.hasMember(target)) {
            throw new CommandException(String.format(MESSAGE_NOT_IN, target.getName()));
        }

        project.removePerson(target);
        model.setProject(project);
        return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName(), projectName));
    }

}
