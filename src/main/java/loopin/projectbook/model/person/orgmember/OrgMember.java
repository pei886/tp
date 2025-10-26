package loopin.projectbook.model.person.orgmember;

import java.util.Set;

import loopin.projectbook.model.person.Email;
import loopin.projectbook.model.person.Name;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.person.Phone;
import loopin.projectbook.model.person.Telegram;
import loopin.projectbook.model.tag.Tag;

/**
 * Represents an Organisation Member in the project book.
 */
public class OrgMember extends Person {

    private final Organisation organisation;

    /**
     * Name, email and tags must be present and non null but phone can be null.
     */
    public OrgMember(Name name, Organisation organisation, Phone phone, Email email, Telegram telegram, Set<Tag> tags) {
        super(name, phone, email, telegram, tags);
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
        if (!(other instanceof OrgMember)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return super.equals(otherPerson);
    }
}
