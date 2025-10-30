package loopin.projectbook.logic.commands.projectcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.parser.CliSyntax.*;

import java.util.Optional;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.project.ProjectName;

/**
 * Views details of a specific project including its description and team members.
 */
public class ViewProjectCommand extends Command {

    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views details of a project.\n"
            + "Parameters: "
            + PREFIX_PROJECT + "PROJECT_NAME "
            + "Example: " + COMMAND_WORD + " project/Beach Cleanup";

    public static final String MESSAGE_PROJECT_NOT_FOUND = "Project \"%s\" not found.";
    public static final String MESSAGE_PROJECT_SUCCESS = "Project details are shown below.\n";

    private final ProjectName projectName;

    public ViewProjectCommand(ProjectName projectName) {
        this.projectName = projectName;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Optional<Project> project = model.findProjectByName(projectName.toString());

        if (project.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_PROJECT_NOT_FOUND, projectName));
        }

        Project p = project.get();
        StringBuilder output = new StringBuilder(MESSAGE_PROJECT_SUCCESS);

        output.append("Description:\n");
        output.append(p.getDescription()).append("\n");

        output.append("Team members:\n");
        p.getAllPeople().forEach(person -> {
            output.append("- ").append(person.getName()).append("\n");
            output.append("  ").append(person.getPhone()).append("\n");
            output.append("  ").append(person.getEmail()).append("\n");
        });

        return new CommandResult(MESSAGE_PROJECT_SUCCESS, false, false, false, true);

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewProjectCommand)) {
            return false;
        }

        ViewProjectCommand otherCommand = (ViewProjectCommand) other;
        return projectName.equals(otherCommand.projectName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("projectName", projectName)
                .toString();
    }
}
