package loopin.projectbook.logic.commands.projectcommands;

import static java.util.Objects.requireNonNull;

import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.project.ProjectNameContainsKeywordsPredicate;

/**
 * Finds and lists all projects whose names contain any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class ProjectFindCommand extends Command {

    public static final String COMMAND_WORD = "project";
    public static final String SUBCOMMAND = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUBCOMMAND
            + ": Finds all projects whose names contain any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " " + SUBCOMMAND + " app manager website";

    private final ProjectNameContainsKeywordsPredicate predicate;

    public ProjectFindCommand(ProjectNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredProjectList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PROJECTS_LISTED_OVERVIEW, model.getFilteredProjectList().size()),
                false, false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ProjectFindCommand
                && predicate.equals(((ProjectFindCommand) other).predicate));
    }
}
