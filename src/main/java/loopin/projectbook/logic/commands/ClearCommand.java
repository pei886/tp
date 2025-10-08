package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;

import loopin.projectbook.model.Model;
import loopin.projectbook.model.ProjectBook;

/**
 * Clears the project book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Project book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setProjectBook(new ProjectBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
