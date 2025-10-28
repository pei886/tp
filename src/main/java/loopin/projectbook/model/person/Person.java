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
    private final List<Remark> remarks = new ArrayList<>(); // empty by default. TODO: change implementation
    private List<Project> projects = new ArrayList<>(); //list of projects the person is part of, empty by default
    private final Set<Remark> remarks = new HashSet<>(); // empty by default. TODO: change implementation

    /**
     * Name, email and tags must be present and non null but phone and telegram can be null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Set<Remark> remarks) {
    protected Person(Name name, Role role, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
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

        boolean isSamePhone = otherPerson.getPhone().equals(getPhone());
        boolean isSameEmail = otherPerson.getEmail().equals(getEmail());
        boolean isSameTelegram = otherPerson.getTelegram().equals(getTelegram());

        return isSamePhone || isSameEmail || isSameTelegram;
    }

    /**
     * Creates a copy of the existing person with the same role but updated fields.
     */
    public abstract Person createCopy(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags);

    public boolean hasRemark(Remark remark) {
        return remarks.contains(remark);
    }

    /**
     * Returns a new immutable Person with a remark.
     */
    public Person withNewRemark(Remark newRemark) {
        Set<Remark> updatedRemarks = new HashSet<>(this.remarks);
        updatedRemarks.add(newRemark);
        return new Person(name, phone, email, address, tags, updatedRemarks);
        // Implementation must create a copy of the existing person and add the new remark.
        Person updatedPerson = createCopy(name, phone, email, telegram, tags);
        updatedPerson.remarks.addAll(this.remarks);
        updatedPerson.remarks.add(newRemark);
        return updatedPerson;
    }
    /**
    * Returns a new immutable Person with the specified remark resolved (replaced).
    */
    public Person withResolvedRemark(Remark oldRemark, Remark resolvedRemark) {
        Set<Remark> updatedRemarks = new HashSet<>(this.remarks);

        // Remove the old pending remark (relies on Remark.equals() comparing content).
        // Since the content is the same, removing the old one and adding the new one works.
        // It's safer to check if removal succeeded, but we rely on equals() being content-only.
        updatedRemarks.remove(oldRemark);
        updatedRemarks.add(resolvedRemark);

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

    // Add a getter for the UI
    public List<Remark> getRemarks() {
        return Collections.unmodifiableList(remarks);
        return new Person(name, phone, email, address, tags, updatedRemarks);
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
        return name.equals(otherPerson.name)
                && role.equals(otherPerson.role)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && remarks.equals(otherPerson.remarks);
                && telegram.equals(otherPerson.telegram)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, remarks);
        return Objects.hash(name, phone, email, telegram, tags);
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
