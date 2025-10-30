package loopin.projectbook.logic.commands.personcommands;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_COMMITEE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_NAME;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_ORGANISATION;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static loopin.projectbook.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static loopin.projectbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import loopin.projectbook.commons.core.index.Index;
import loopin.projectbook.commons.util.CollectionUtil;
import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.logic.Messages;
import loopin.projectbook.logic.commands.Command;
import loopin.projectbook.logic.commands.CommandResult;
import loopin.projectbook.logic.commands.exceptions.CommandException;
import loopin.projectbook.model.Model;
import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.person.orgmember.OrgMember;
import loopin.projectbook.model.person.orgmember.Organisation;
import loopin.projectbook.model.person.teammember.Committee;
import loopin.projectbook.model.person.teammember.TeamMember;
import loopin.projectbook.model.person.volunteer.Volunteer;

/**
 * Edits fields of an existing {@link Person} in the project book.
 *
 * Usage:
 * {@code
 * edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [tg/TELEGRAM] [c/COMMITTEE] [o/ORGANISATION]
 * }
 *
 * Role-specific constraints:
 *   - {@link TeamMember} cannot edit Organisation.
 *   - {@link OrgMember} cannot edit Committee.
 *   - {@link Volunteer} cannot edit role-specific fields (Committee/Organisation).
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_TELEGRAM + "TELEGRAM] "
            + "[" + PREFIX_COMMITEE + "COMMITTEE] "
            + "[" + PREFIX_ORGANISATION + "ORGANISATION]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the project book.";
    public static final String MESSAGE_NOT_PERMITTED_FOR_ROLE = "This field does not exist for this role.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * Constructs an {@code EditCommand}.
     *
     * @param index index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with; at least one field must be present
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatPerson(editedPerson),
                false, false, true, false));
    }

    /**
     * Creates and returns a new {@link Person} (or subclass) based on {@code original} and {@code edits}.
     *
     * SLAP steps:
     *   - Assemble common fields
     *   - Enforce role-specific constraints
     *   - Construct the correct subclass
     *
     * @param original existing person to edit; must not be {@code null}
     * @param edits optional updates to apply
     * @return updated {@link Person}
     * @throws CommandException if role-specific constraints are violated
     */
    private static Person createEditedPerson(Person original, EditPersonDescriptor edits) throws CommandException {
        assert original != null;

        // Assemble common fields
        Name name = edits.nameOr(original.getName());
        Optional<Phone> phone = edits.phoneOr(original.getPhone());
        Email email = edits.emailOr(original.getEmail());
        Optional<Telegram> telegram = edits.telegramOr(original.getTelegram());

        var remarks = original.getRemarks();
        var projects = original.getProjects();

        // Role-specific branching
        if (original instanceof TeamMember tm) {
            ensureNoOrganisationForTeamMember(edits);
            return new TeamMember(
                    name,
                    edits.getCommittee().orElse(tm.getCommittee()),
                    phone, email, telegram, remarks, projects
            );
        }
        if (original instanceof OrgMember om) {
            ensureNoCommitteeForOrgMember(edits);
            return new OrgMember(
                    name,
                    edits.getOrganisation().orElse(om.getOrganisation()),
                    phone, email, telegram, remarks, projects
            );
        }

        // Volunteer
        ensureNoRoleOnlyFieldsForVolunteer(edits);
        return new Volunteer(name, phone, email, telegram, remarks, projects);
    }

    /**
     * Ensures that {@code organisation} is not edited for a {@link TeamMember}.
     *
     * @throws CommandException if {@code organisation} is present
     */
    private static void ensureNoOrganisationForTeamMember(EditPersonDescriptor edits) throws CommandException {
        if (edits.getOrganisation().isPresent()) {
            throw new CommandException(MESSAGE_NOT_PERMITTED_FOR_ROLE);
        }
    }

    /**
     * Ensures that {@code committee} is not edited for an {@link OrgMember}.
     *
     * @throws CommandException if {@code committee} is present
     */
    private static void ensureNoCommitteeForOrgMember(EditPersonDescriptor edits) throws CommandException {
        if (edits.getCommittee().isPresent()) {
            throw new CommandException(MESSAGE_NOT_PERMITTED_FOR_ROLE);
        }
    }

    /**
     * Ensures that volunteers do not receive role-specific fields.
     *
     * @throws CommandException if {@code committee} or {@code organisation} is present
     */
    private static void ensureNoRoleOnlyFieldsForVolunteer(EditPersonDescriptor edits) throws CommandException {
        if (edits.getCommittee().isPresent() || edits.getOrganisation().isPresent()) {
            throw new CommandException(MESSAGE_NOT_PERMITTED_FOR_ROLE);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Optional<Phone> phone;
        private Email email;
        private Optional<Telegram> telegram;
        private Committee committee;
        private Organisation organisation;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTelegram(toCopy.telegram);
            setCommittee(toCopy.committee);
            setOrganisation(toCopy.organisation);
        }

        /** Returns updated value if the descriptor specifies it; otherwise returns current. */
        public Optional<Phone> phoneOr(Optional<Phone> current) {
            return phone != null ? phone : current;
        }

        /** Returns updated value if the descriptor specifies it; otherwise returns current. */
        public Optional<Telegram> telegramOr(Optional<Telegram> current) {
            return telegram != null ? telegram : current;
        }

        public Name nameOr(Name current) {
            return name != null ? name : current;
        }

        public Email emailOr(Email current) {
            return email != null ? email : current;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, telegram, committee, organisation);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Optional<Phone> phone) {
            this.phone = phone;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        /** Returns true iff the descriptor explicitly includes a phone edit (present or explicit removal). */
        public boolean hasPhoneEdit() {
            return phone != null;
        }

        /** Returns the edited phone value when present; empty = explicit removal. Undefined if !hasPhoneEdit(). */
        public Optional<Phone> editedPhone() {
            return phone;
        }

        /** Returns true iff the descriptor explicitly includes a telegram edit (present or explicit removal). */
        public boolean hasTelegramEdit() {
            return telegram != null;
        }

        /**
         * Returns the edited telegram value when present;
         * empty = explicit removal. Undefined if !hasTelegramEdit().
         */
        public Optional<Telegram> editedTelegram() {
            return telegram;
        }

        public void setTelegram(Optional<Telegram> telegram) {
            this.telegram = telegram;
        }

        public void setCommittee(Committee committee) {
            this.committee = committee;
        }

        public Optional<Committee> getCommittee() {
            return Optional.ofNullable(committee);
        }

        public void setOrganisation(Organisation organisation) {
            this.organisation = organisation;
        }

        public Optional<Organisation> getOrganisation() {
            return Optional.ofNullable(organisation);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(telegram, otherEditPersonDescriptor.telegram)
                    && Objects.equals(committee, otherEditPersonDescriptor.committee)
                    && Objects.equals(organisation, otherEditPersonDescriptor.organisation);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("telegram", telegram)
                    .add("committee", committee)
                    .add("organisation", organisation)
                    .toString();
        }
    }
}
