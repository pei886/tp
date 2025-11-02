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
 * Maintains a list of {@link Project} with uniqueness enforced and no null elements allowed.
 *
 * Uniqueness is determined by {@link Project#equals(Object)}. As such, adding and updating rely on
 * {@code equals} for identity. Name-based convenience lookups are provided via {@link #findByName(String)}
 * and {@link #removeByName(String)} which perform case-insensitive matching with whitespace normalization.
 *
 * This class supports a minimal set of list operations and exposes an unmodifiable view suitable for UI binding.
 */
public class UniqueProjectList implements Iterable<Project> {
    private final ObservableList<Project> internalList = FXCollections.observableArrayList();
    private final ObservableList<Project> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns {@code true} if the list contains an equivalent project to {@code toCheck}.
     *
     * @param toCheck project to test for containment; must not be {@code null}
     * @return whether an equivalent project exists in this list
     */
    public boolean contains(Project toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(p -> p.equals(toCheck));
    }

    /**
     * Finds the first project whose name exactly matches {@code name} under normalization
     * (trim + collapse internal whitespace + case-insensitive).
     *
     * @param name project name to search; may be {@code null}
     * @return an {@link Optional} containing the first matching project, or {@link Optional#empty()} if none found
     */
    public Optional<Project> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        String needle = normalizeName(name);
        return internalList.stream()
                .filter(p -> normalizeName(p.getName().toString()).equals(needle))
                .findFirst();
    }

    /**
     * Normalizes a project name by trimming, collapsing internal whitespace to a single space, and lowercasing.
     */
    private static String normalizeName(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    /**
     * Adds {@code toAdd} to the list.
     *
     * @param toAdd project to add; must not be {@code null}
     * @throws DuplicateProjectException if an equivalent project already exists
     */
    public void add(Project toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateProjectException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the existing project equal to {@code target} with {@code target}.
     *
     * Note: callers typically mutate fields of a project and pass the same instance back;
     * this method finds by {@code equals} and replaces at the same index.
     *
     * @param target updated project instance; must not be {@code null}
     * @throws ProjectNotFoundException if no equivalent project is present
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
     * Replaces the entire contents with {@code projects}.
     *
     * @param projects replacement list; must not be {@code null}
     */
    public void setProjects(List<Project> projects) {
        requireNonNull(projects);
        if (!projectsAreUnique(projects)) {
            throw new DuplicateProjectException();
        }
        internalList.setAll(projects);
    }

    private boolean projectsAreUnique(List<Project> projects) {
        for (int i = 0; i < projects.size() - 1; i++) {
            for (int j = i + 1; j < projects.size(); j++) {
                if (projects.get(i).equals(projects.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Removes the project equal to {@code toRemove}.
     *
     * @param toRemove project to remove; must not be {@code null}
     * @throws ProjectNotFoundException if no equivalent project exists
     */
    public void remove(Project toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ProjectNotFoundException();
        }
    }

    /**
     * Removes the project with the given {@code name} using the same normalization as {@link #findByName(String)}.
     *
     * @param name name of the project to remove
     * @throws ProjectNotFoundException if no project with the given name exists
     */
    public void removeByName(String name) {
        Project match = findByName(name).orElseThrow(ProjectNotFoundException::new);
        internalList.remove(match);
    }

    /**
     * Returns an unmodifiable view of the backing list for UI binding.
     *
     * @return unmodifiable {@link ObservableList}
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
