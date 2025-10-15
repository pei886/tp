package loopin.projectbook.logic.commands;

import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;

import loopin.projectbook.model.person.Volunteer;

/**
 * Adds a volunteer to the project book.
 */
public class AddVolunteerCommand extends AddCommand {

    public static final String COMMAND_WORD = "addv";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a volunteer to the project book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com ";

    public static final String MESSAGE_SUCCESS = "New volunteer added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the project book";

    /**
     * Creates an AddVolunteerCommand to add the specified {@code Volunteer}
     */
    public AddVolunteerCommand(Volunteer volunteer) {
        super(volunteer);
    }

    @Override
    protected String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        // instanceof handles nulls
        if (!(other instanceof AddVolunteerCommand)) {
            return false;
        } else {
            return super.equals(other);
        }
    }
}
