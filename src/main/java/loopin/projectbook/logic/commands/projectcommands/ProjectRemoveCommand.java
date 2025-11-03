package loopin.projectbook.logic.commands.projectcommands;

import static java.util.Objects.requireNonNull;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Removes a person from an existing project.
 *
 * Usage:
 * {@code
 * project remove INDEX project/PROJECT_NAME
 * project remove n/NAME project/PROJECT_NAME
 * }
 *
 * Person resolution uses {@link BaseProjectMemberCommand#resolveTargetPerson(Model, String, Index)}.
 */
public final class ProjectRemoveCommand extends BaseProjectMemberCommand {
    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "remove";
    public static final String MESSAGE_USAGE =
            "project remove: Remove a person from a project.\n"
                    + "Formats:\n"
                    + "project remove INDEX project/PROJECT_NAME\n"
                    + "project remove n/NAME project/PROJECT_NAME\n"
                    + "Examples:\n"
                    + "project remove 1 project/MyProject\n"
                    + "project remove n/Alex Tan project/MyProject";

    public static final String MESSAGE_SUCCESS = "Removed %s from the project %s.";
    public static final String MESSAGE_NOT_IN = "%s is not in this project.";

    private final Index index;
    private final String name;
    private final ProjectName projectName;

    /**
     * Creates a command to remove the person at {@code index} from the project named {@code projectName}.
     *
     * @param index index of the person in the current filtered person list
     * @param projectName validated project name value object
     */
    public ProjectRemoveCommand(Index index, ProjectName projectName) {
        this.index = index;
        this.name = null;
        this.projectName = projectName;
    }

    /**
     * Creates a command to remove the person called {@code name} from the project named {@code projectName}.
     *
     * @param name exact (case-insensitive) person name
     * @param projectName validated project name value object
     */
    public ProjectRemoveCommand(String name, ProjectName projectName) {
        requireNonNull(name);
        this.index = null;
        this.name = name;
        this.projectName = projectName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Project project = resolveProjectByName(model, projectName);

        Person target = resolveTargetPerson(model, name, index);
        target.removeProject(project);

        if (!project.hasMember(target)) {
            throw new CommandException(String.format(MESSAGE_NOT_IN, target.getName()));
        }

        project.removePerson(target);
        model.setProject(project);
        model.setPersonInPlace(target);
        return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName(), projectName));
    }
}
