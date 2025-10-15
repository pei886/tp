package loopin.projectbook.model;


import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import loopin.projectbook.commons.util.ToStringBuilder;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;
import loopin.projectbook.model.person.UniquePersonList;
import loopin.projectbook.model.project.UniqueProjectList;

/**
 * Wraps all data at the project-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class ProjectBook implements ReadOnlyProjectBook {

    private final UniquePersonList persons;
    private final UniqueProjectList projects;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        projects = new UniqueProjectList();
    }

    public ProjectBook() {}

    /**
     * Creates an ProjectBook using the Persons in the {@code toBeCopied}
     */
    public ProjectBook(ReadOnlyProjectBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code ProjectBook} with {@code newData}.
     */
    public void resetData(ReadOnlyProjectBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the project book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the project book.
     * The person must not already exist in the project book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the project book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the project book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code ProjectBook}.
     * {@code key} must exist in the project book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Returns true if a project with the same identity as {@code project} exists in the project book.
     */
    public boolean hasProject(Project project) {
        requireNonNull(project);
        return projects.contains(project);
    }

    /**
     * Adds a project to the project book.
     */
    public void addProject(Project p) {
        projects.add(p);
    }

    /**
     * Replaces by id the given project {@code project}.
     * {@code project} must exist in the project book.
     */
    public void setPerson(Project project) {
        requireNonNull(project);
        projects.setProject(project);
    }

    public java.util.Optional<Project> findProjectByName(String name) {
        return projects.findByName(name);
    }

    public javafx.collections.ObservableList<Project> getProjectList() {
        return projects.asUnmodifiableObservableList();
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ProjectBook)) {
            return false;
        }

        ProjectBook otherProjectBook = (ProjectBook) other;
        return persons.equals(otherProjectBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
