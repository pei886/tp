package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TAG;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;

/**
 * Adds a person to the project book.
 */
public abstract class AddCommand extends Command {

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the project book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    public abstract String getSuccessMessage();

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(getSuccessMessage(), Messages.formatPerson(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
