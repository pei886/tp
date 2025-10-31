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
 * Assigns a person to an existing project.
 *
 * Usage:
 * {@code
 * project assign INDEX project/PROJECT_NAME
 * project assign n/NAME project/PROJECT_NAME
 * }
 *
 * Person resolution uses {@link BaseProjectMemberCommand#resolveTargetPerson(Model, String, Index)}.
 */
public final class ProjectAssignCommand extends BaseProjectMemberCommand {
    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "assign";
    public static final String MESSAGE_USAGE =
            "project assign: Assign a person to a project.\n"
                    + "Formats:\n"
                    + "project assign INDEX project/PROJECT_NAME\n"
                    + "project assign n/NAME project/PROJECT_NAME\n"
                    + "Examples:\n"
                    + "project assign 1 project/MyProject\n"
                    + "project assign n/Alex Tan project/MyProject";

    public static final String MESSAGE_SUCCESS = "Assigned %s to the project %s.";
    public static final String MESSAGE_ALREADY = "%s is already in this project.";

    private final Index index;
    private final String name;
    private final ProjectName projectName;

    /**
     * Creates a command to assign the person at {@code index} to the project named {@code projectName}.
     *
     * @param index index of the person in the current filtered person list
     * @param projectName validated project name value object
     */
    public ProjectAssignCommand(Index index, ProjectName projectName) {
        this.index = index;
        this.name = null;
        this.projectName = projectName;
    }

    /**
     * Creates a command to assign the person called {@code name} to the project named {@code projectName}.
     *
     * @param name exact (case-insensitive) person name
     * @param projectName validated project name value object
     */
    public ProjectAssignCommand(String name, ProjectName projectName) {
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

        if (project.hasMember(target)) {
            throw new CommandException(String.format(MESSAGE_ALREADY, target.getName()));
        }

        project.assignPerson(target);
        target.addProject(project);
        model.setProject(project);
        model.setPersonInPlace(target);

        return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName(), projectName));
    }
}
