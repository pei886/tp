package loopin.projectbook.model;

import javafx.collections.ObservableList;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;

/**
 * Unmodifiable view of an project book
 */
public interface ReadOnlyProjectBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the projects list.
     * This list will not contain any duplicate projects.
     */
    ObservableList<Project> getProjectList();
}
