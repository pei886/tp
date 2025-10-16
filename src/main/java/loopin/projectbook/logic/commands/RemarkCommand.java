package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;
import java.util.List;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Remark; // The conceptual class from step 1

/**
 * Creates a custom remark that a person needs to be contacted for an update.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a custom remark that a person needs to be contacted for an update.\n"
            + "Parameters: INDEX u/UPDATE\n" // Use 'u/' prefix in description
            + "Example: " + COMMAND_WORD + " 1 u/funding has been secured";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Remarked that %1$s needs to be updated on \"%2$s\" (pending).";
    public static final String MESSAGE_DUPLICATE_REMARK = "This person is already marked with the specified remark.";
    public static final String MESSAGE_EMPTY_REMARK = "The remark content cannot be empty!";

    private final Index targetIndex;
    private final String remarkContent;

    /**
     * @param targetIndex of the person in the filtered person list to add the remark to
     * @param remarkContent the update message to be created
     */
    public RemarkCommand(Index targetIndex, String remarkContent) {
        this.targetIndex = targetIndex;
        this.remarkContent = remarkContent;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToRemark = lastShownList.get(targetIndex.getZeroBased());
        Remark newRemark = new Remark(remarkContent);

        // Duplicate Handling: Reject if same person and same normalized remark already exists
        // This assumes a 'hasRemark' method on the Person class (see implementation notes).
        if (personToRemark.hasRemark(newRemark)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMARK);
        }

        // Create a new Person object with the added remark
        // This assumes a 'withNewRemark' method on the Person class (see implementation notes).
        Person remarkedPerson = personToRemark.withNewRemark(newRemark);

        // Update the model to reflect the change
        model.setPerson(personToRemark, remarkedPerson);

        // Success Output
        return new CommandResult(String.format(MESSAGE_ADD_REMARK_SUCCESS,
                remarkedPerson.getName().fullName,
                remarkContent));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand otherRemarkCommand = (RemarkCommand) other;
        return targetIndex.equals(otherRemarkCommand.targetIndex)
                && remarkContent.equals(otherRemarkCommand.remarkContent);
    }
}