package loopin.projectbook.model.person;

import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents a Person in the project book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final List<Remark> remarks = new ArrayList<>(); // empty by default. TODO: change implementation

    /**
     * Name, email and tags must be present and non null but phone and telegram can be null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        requireAllNonNull(name, email, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
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

    public String getRole() {
        return "Unknown Role";
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && (otherPerson.getName().equals(getName())
                || otherPerson.getEmail().equals(getEmail()));
    }

    // Add this method for duplicate checking in RemarkCommand
    public boolean hasRemark(Remark remark) {
        return remarks.contains(remark); // leverages UpdateRemark's equals()
    }

    // Add this method for adding a remark (returns a new immutable Person)
    /**
     * Returns a new immutable Person with a remark.
     */
    public Person withNewRemark(Remark newRemark) {
        // Implementation must create a copy of the existing person and add the new remark.
        Person updatedPerson = new Person(name, phone, email, telegram, tags);
        updatedPerson.remarks.addAll(this.remarks);
        updatedPerson.remarks.add(newRemark);
        return updatedPerson;
    }

    // Add a getter for the UI
    public List<Remark> getRemarks() {
        return Collections.unmodifiableList(remarks);
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
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
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
                .toString();
    }

}
