package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;

public final class ProjectAssignCommand extends Command {
    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "assign";
    public static final String MESSAGE_USAGE =
            "project assign: Assign a person to a project.\n"
                    + "  Format: project assign INDEX p/PROJECT_NAME";

    public static final String MESSAGE_SUCCESS = "Assigned %s to %s.";
    public static final String MESSAGE_ALREADY = "%s is already in this project.";
    public static final String MESSAGE_NO_PROJECT = "Project '%s' does not exist.";
    public static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid.";

    private final Index index;
    private final String projectName;

    public ProjectAssignCommand(Index index, String projectName) {
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
        Project project = model.findProjectByName(projectName)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_NO_PROJECT, projectName)));

        Person target = lastShown.get(index.getZeroBased());
        if (project.hasMember(target)) {
            throw new CommandException(String.format(MESSAGE_ALREADY, target.getName()));
        }

        project.assignPerson(target);
        model.setProject(project);
        return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName(), projectName));
    }
}
