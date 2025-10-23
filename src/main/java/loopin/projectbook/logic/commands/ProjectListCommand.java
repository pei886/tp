package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.model.Model.PREDICATE_SHOW_ALL_PROJECTS;

import loopin.projectbook.model.Model;

/**
 * Lists all persons in the project book to the user.
 */
public class ProjectListCommand extends Command {

    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "list";
    public static final String MESSAGE_USAGE =
            "project list: Lists all current projects.\n"
                    + "Format: project list\n";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
        return new CommandResult(MESSAGE_SUCCESS, false, false, false, true);
    }
}
