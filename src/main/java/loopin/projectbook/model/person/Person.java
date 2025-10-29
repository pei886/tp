package loopin.projectbook.model.person;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.project.LastUpdate;
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
    private final Optional<Phone> phone;
    private final Email email;
    private final Optional<Telegram> telegram;
    private final Role role;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Remark> remarks = new HashSet<>(); // Changed to Set
    private List<Project> projects = new ArrayList<>(); //list of projects the person is part of, empty by default
    /**
     * All fields must be present and non null.
     *
     * Phone and telegram are optional.
     */
    protected Person(Name name, Role role, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Tag> tags, Set<Remark> remarks, List<Project> projects) {
        requireAllNonNull(name, role, phone, email, telegram, tags, remarks, projects);
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.tags.addAll(tags);
        this.remarks.addAll(remarks);
        this.projects.addAll(projects);
    }

    public Name getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Optional<Telegram> getTelegram() {
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
        boolean isSameEmail = getEmail().equals(otherPerson.getEmail());

        // If phone and telegram are empty, they are not considered as duplicates
        boolean isSamePhone = getPhone().isPresent() && getPhone().equals(otherPerson.getPhone());
        boolean isSameTelegram = getTelegram().isPresent() && getTelegram().equals(otherPerson.getTelegram());

        return isSamePhone || isSameEmail || isSameTelegram;
    }

    /**
     * Creates a copy of the existing person with the same role but updated fields.
     */
    public abstract Person createCopy(Name name, Optional<Phone> phone, Email email, Optional<Telegram> telegram,
            Set<Tag> tags, Set<Remark> remarks, List<Project> projects);

    public boolean hasRemark(Remark remark) {
        return remarks.contains(remark);
    }

    /**
     * Returns a new immutable Person with a remark.
     */
    public Person withNewRemark(Remark newRemark) {
        Person updatedPerson = createCopy(name, phone, email, telegram, tags, remarks, projects);
        updatedPerson.remarks.addAll(this.remarks);
        updatedPerson.remarks.add(newRemark);

        // Update all associated projects
        for (Project project : this.projects) {
            LastUpdate update = LastUpdate.remarkAdded(updatedPerson.getName(), newRemark.toString());
            project.recordUpdate(update);
        }
        return updatedPerson;
    }
    /**
     * Returns a new immutable Person with the specified remark resolved (replaced).
     */
    public Person withResolvedRemark(Remark oldRemark, Remark resolvedRemark) {
        Person updatedPerson = createCopy(name, phone, email, telegram, tags, remarks, projects);
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
        return createCopy(name, phone, email, telegram, tags, updatedRemarks, projects);
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

    /**
     * Returns a list of the projects that the person is in
     * @return
     */
    public List<Project> getProjects() {
        return this.projects;
    }

    /**
     * Returns number of projects the person has
     * @return
     */
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
        return Objects.hash(name, role, phone, email, telegram, tags, remarks, projects);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone.map(p -> p.value).orElse("nil"))
                .add("email", email)
                .add("telegram", telegram.map(t -> t.value).orElse("nil"))
                .add("tags", tags)
                .add("remarks", remarks)
                .toString();
    }

}
