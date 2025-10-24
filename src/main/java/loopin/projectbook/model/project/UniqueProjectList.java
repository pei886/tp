package loopin.projectbook.model.project;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loopin.projectbook.model.project.exceptions.DuplicateProjectException;
import loopin.projectbook.model.project.exceptions.ProjectNotFoundException;

/**
 * A list of projects that enforces uniqueness between its elements and does not allow nulls.
 * A project is considered unique by comparing using {@code Project#equals(Project)}.
 */
public class UniqueProjectList implements Iterable<Project> {
    private final ObservableList<Project> internalList = FXCollections.observableArrayList();
    private final ObservableList<Project> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent project as the given argument.
     */
    public boolean contains(Project toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(p -> p.equals(toCheck));
    }

    /**
     * Finds a project by its name.
     *
     * @param name the project name to search for
     * @return an {@code Optional} containing the first matching project, or {@code Optional.empty()} if none found
     */
    public Optional<Project> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        String normalisedName = name.trim().replaceAll("\\s+", " ").toLowerCase();
        return internalList.stream()
                .filter(p -> p.getName().toString()
                        .equalsIgnoreCase(normalisedName))
                .findFirst();
    }

    /**
     * Adds a project to the list.
     * The project must not already exist in the list.
     *
     * @throws DuplicateProjectException if the project already exists
     */
    public void add(Project toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateProjectException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the project {@code target} in the list with the updated version.
     * {@code target} must exist in the list.
     *
     * @throws ProjectNotFoundException if the target project does not exist
     */
    public void setProject(Project target) {
        requireNonNull(target);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ProjectNotFoundException();
        }

        internalList.set(index, target);
    }

    /**
     * Replaces the contents of this list with {@code projects}.
     */
    public void setProjects(List<Project> projects) {
        requireNonNull(projects);
        internalList.setAll(projects);
    }

    /**
     * Removes the equivalent project from the list.
     * The project must exist in the list.
     *
     * @throws ProjectNotFoundException if the project does not exist
     */
    public void remove(Project toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ProjectNotFoundException();
        }
    }

    /**
     * Removes the project with the given name (case-insensitive, normalised).
     *
     * @param name name of the project to remove
     * @throws ProjectNotFoundException if no project with the given name exists
     */
    public void removeByName(String name) {
        Optional<Project> match = findByName(name);
        if (match.isEmpty()) {
            throw new ProjectNotFoundException();
        }
        internalList.remove(match.get());
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Project> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Project> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueProjectList)) {
            return false;
        }

        UniqueProjectList otherUniqueProjectList = (UniqueProjectList) other;
        return internalList.equals(otherUniqueProjectList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

}
