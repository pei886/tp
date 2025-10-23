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

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Remark> remarks = new HashSet<>(); // empty by default. TODO: change implementation

    /**
     * Name, email and tags must be present and non null but phone and address can be null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Set<Remark> remarks) {
        requireAllNonNull(name, email, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.remarks.addAll(remarks);
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

    public Address getAddress() {
        return address;
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
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && remarks.equals(otherPerson.remarks);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, remarks);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("remarks", remarks)
                .toString();
    }

}
