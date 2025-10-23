package loopin.projectbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Remark;
import loopin.projectbook.model.person.Remark.Status;

/**
 * Resolves (marks as COMPLETED) a specific remark for a person.
 */
public class ResolveRemarkCommand extends Command {

    public static final String COMMAND_WORD = "resolve";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a pending remark of the person identified by the first index as resolved. \n"
            + "The second index specifies the remark (e.g., if the person has two remarks, index '2' resolves the second one).\n"
            + "Parameters: PERSON_INDEX REMARK_INDEX (both must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_RESOLVE_REMARK_SUCCESS =
            "Remark for %1$s resolved: \"%2$s\".";
    public static final String MESSAGE_INVALID_REMARK_INDEX = "The remark index provided is invalid or the person has no remarks.";
    public static final String MESSAGE_REMARK_ALREADY_RESOLVED = "The specified remark is already resolved (COMPLETED).";

    private final Index personIndex;
    private final Index remarkIndex; // 0-based index of the remark within the person's remarks list

    /**
     * @param personIndex of the person in the filtered list
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

        // Use an ArrayList conversion of the Set for indexed access
        List<Remark> currentRemarks = List.copyOf(personToEdit.getRemarks());

        if (remarkIndex.getZeroBased() >= currentRemarks.size() || currentRemarks.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_REMARK_INDEX);
        }

        Remark remarkToResolve = currentRemarks.get(remarkIndex.getZeroBased());

        if (remarkToResolve.status == Status.COMPLETED) {
            throw new CommandException(MESSAGE_REMARK_ALREADY_RESOLVED);
        }

        // 1. Create the resolved remark (new object with COMPLETED status)
        Remark resolvedRemark = remarkToResolve.resolve();

        // 2. Create a new Person with the updated remark set
        Person resolvedPerson = personToEdit.withResolvedRemark(remarkToResolve, resolvedRemark);

        // 3. Update the model (model.setPerson handles the replacement)
        model.setPerson(personToEdit, resolvedPerson);

        // 4. Success Output
        return new CommandResult(String.format(MESSAGE_RESOLVE_REMARK_SUCCESS,
                resolvedPerson.getName().fullName,
                resolvedRemark.content));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ResolveRemarkCommand)) {
            return false;
        }

        ResolveRemarkCommand otherResolveCommand = (ResolveRemarkCommand) other;
        return personIndex.equals(otherResolveCommand.personIndex)
                && remarkIndex.equals(otherResolveCommand.remarkIndex);
    }
}