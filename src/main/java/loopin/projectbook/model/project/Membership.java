package loopin.projectbook.model.project;

import loopin.projectbook.model.person.Person;

/**
 * Represents a minimal Membership used for compilation purposes.
 */
public class Membership {

    private Person person;

    public Membership(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return person.toString();
    }
}
