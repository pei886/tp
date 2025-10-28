package loopin.projectbook.model.person;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a Person in the project book.
 * Guarantees: name is present and not null, field values are validated, immutable.
 */
public abstract class Person {

    /**
     * Logger for Person class
     */
    private static final Logger logger = LogsCenter.getLogger(Person.class);

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;
    private final Role role;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Remark> remarks = new HashSet<>(); // Changed to Set
    private List<Project> projects = new ArrayList<>(); //list of projects the person is part of, empty by default
    /**
     * Name, email and tags must be present and non null but phone and telegram can be null.
     */
    protected Person(Name name, Role role, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks) {
        requireAllNonNull(name, email, tags);
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.tags.addAll(tags);
        this.remarks.addAll(remarks);
    }

    public Name getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same phone, email or telegram.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        } else if (otherPerson == null) {
            return false;
        }

        // email is guaranteed non-null
        boolean isSameEmail = otherPerson.getEmail().equals(getEmail());

        // phone and telegram can be null.
        // Check if they are non-null and then if they are equal.
        // Assumes .equals() handles a null argument gracefully (returns false).
        boolean isSamePhone = getPhone() != null && getPhone().equals(otherPerson.getPhone());
        boolean isSameTelegram = getTelegram() != null && getTelegram().equals(otherPerson.getTelegram());

        return isSamePhone || isSameEmail || isSameTelegram;
    }

    /**
     * Creates a copy of the existing person with the same role but updated fields.
     */
    public abstract Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags, Set<Remark> remarks);

    public boolean hasRemark(Remark remark) {
        return remarks.contains(remark);
    }

    /**
     * Returns a new immutable Person with a remark.
     */
    public Person withNewRemark(Remark newRemark) {
        Person updatedPerson = createCopy(name, phone, email, telegram, tags, remarks);
        updatedPerson.remarks.addAll(this.remarks);
        updatedPerson.remarks.add(newRemark);
        return updatedPerson;
    }
    /**
     * Returns a new immutable Person with the specified remark resolved (replaced).
     */
    public Person withResolvedRemark(Remark oldRemark, Remark resolvedRemark) {
        Person updatedPerson = createCopy(name, phone, email, telegram, tags, remarks);
        updatedPerson.remarks.addAll(this.remarks);
        updatedPerson.remarks.remove(oldRemark);
        updatedPerson.remarks.add(resolvedRemark);
        return updatedPerson;
    }

    /**
     * Returns a new immutable Person with the specified remark removed.
     */
    public Person withRemarkRemoved(Remark remarkToRemove) {
        Set<Remark> updatedRemarks = new HashSet<>(this.remarks);
        updatedRemarks.remove(remarkToRemove);
        return createCopy(name, phone, email, telegram, tags, updatedRemarks);
    }

    /**
     * Adds a project to the list of projects a person is part of
     * @param p project to be added
     */
    public void addProject(Project p) {
        if (this.projects.contains(p)) {
            throw new IllegalStateException("Person is already in that project");
        }
        this.projects.add(p);
        logger.fine("Project added to person.");
    }

    public int getNumberOfProjects() {
        return this.projects.size();
    }

    /**
     * Add a getter for the UI
     */
    public Set<Remark> getRemarks() {
        return Collections.unmodifiableSet(remarks);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        // Non-null fields
        return name.equals(otherPerson.name)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && remarks.equals(otherPerson.remarks)
                // Nullable fields
                && Objects.equals(role, otherPerson.role)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(telegram, otherPerson.telegram);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, tags, remarks, role, phone, telegram);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("telegram", telegram)
                .add("tags", tags)
                .add("remarks", remarks)
                .toString();
    }

}