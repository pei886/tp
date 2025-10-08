package loopin.projectbook.testutil;

import loopin.projectbook.model.ProjectBook;
import loopin.projectbook.model.person.Person;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private ProjectBook projectBook;

    public AddressBookBuilder() {
        projectBook = new ProjectBook();
    }

    public AddressBookBuilder(ProjectBook projectBook) {
        this.projectBook = projectBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withPerson(Person person) {
        projectBook.addPerson(person);
        return this;
    }

    public ProjectBook build() {
        return projectBook;
    }
}
