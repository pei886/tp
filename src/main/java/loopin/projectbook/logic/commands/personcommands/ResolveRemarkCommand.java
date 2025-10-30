package loopin.projectbook.logic.commands.personcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Remark;

/**
 * Resolves (removes) a remark from an existing person in the project book.
 */
public class ResolveRemarkCommand extends Command {

    public static final String COMMAND_WORD = "resolve";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the remark identified by the remark index from the person identified by the person index.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) REMARK_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 1";

    public static final String MESSAGE_RESOLVE_REMARK_SUCCESS = "Removed remark for Person: %1$s";
    public static final String MESSAGE_REMARK_ALREADY_RESOLVED = "Removed remark for Person: %1$s";
    public static final String MESSAGE_INVALID_REMARK_DISPLAYED_INDEX = "The remark index provided is invalid.";

    private final Index personIndex;
    private final Index remarkIndex;

    /**
     * @param personIndex of the person in the filtered person list
     * @param remarkIndex of the remark in the person's remark list
     */
    public ResolveRemarkCommand(Index personIndex, Index remarkIndex) {
        requireNonNull(personIndex);
        requireNonNull(remarkIndex);
        this.personIndex = personIndex;
        this.remarkIndex = remarkIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());

        // Remarks are stored as a Set (no order), but displayed to the user as a list.
        // We convert the Set to a List to find the remark by its "displayed index".
        List<Remark> remarksList = new ArrayList<>(personToEdit.getRemarks());

        if (remarkIndex.getZeroBased() >= remarksList.size()) {
            throw new CommandException(MESSAGE_INVALID_REMARK_DISPLAYED_INDEX);
        }

        Remark remarkToRemove = remarksList.get(remarkIndex.getZeroBased());

        // We create a new person with the remark removed.
        // This requires the .withRemarkRemoved() method (see Fix #2 below)
        Person editedPerson = personToEdit.withRemarkRemoved(remarkToRemove);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_RESOLVE_REMARK_SUCCESS, Messages.formatPerson(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ResolveRemarkCommand)) {
            return false;
        }
        ResolveRemarkCommand otherCommand = (ResolveRemarkCommand) other;
        return personIndex.equals(otherCommand.personIndex)
                && remarkIndex.equals(otherCommand.remarkIndex);
    }
}
