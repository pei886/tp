package loopin.projectbook.model;

import javafx.collections.ObservableList;
import loopin.projectbook.model.person.Person;

/**
 * Unmodifiable view of an project book
 */
public interface ReadOnlyProjectBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
