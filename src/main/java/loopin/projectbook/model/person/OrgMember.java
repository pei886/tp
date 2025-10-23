package loopin.projectbook.model.person;

import java.util.Set;

import loopin.projectbook.model.tag.Tag;

/**
 * Represents an Organisation Member in the project book.
 */
public class OrgMember extends Person {

    private final Organisation organisation;

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public OrgMember(Name name, Organisation organisation, Phone phone, Email email, Set<Tag> tags, Set<Remark> remarks) {
        super(name, phone, email, null, tags, remarks);
        this.organisation = organisation;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public String getRole() {
        return "Organisation: " + organisation.toString();
    }

    /**
     * Returns true if both persons are Organisation Members with the same Organisation and have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(OrgMember otherPerson) {
        return this.getOrganisation().equals(otherPerson.getOrganisation()) && super.isSamePerson(otherPerson);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        // instanceof handles nulls
        if (!(other instanceof Volunteer)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return super.equals(otherPerson);
    }
}
