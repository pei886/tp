package loopin.projectbook.model;

import static java.util.Objects.requireNonNull;
import static loopin.projectbook.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import loopin.projectbook.commons.core.GuiSettings;
import loopin.projectbook.commons.core.LogsCenter;
import loopin.projectbook.model.person.Person;
import loopin.projectbook.model.project.Project;

/**
 * Represents the in-memory model of the project book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ProjectBook projectBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Project> filteredProjects;

    /**
     * Initializes a ModelManager with the given projectBook and userPrefs.
     */
    public ModelManager(ReadOnlyProjectBook projectBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(projectBook, userPrefs);

        logger.fine("Initializing with project book: " + projectBook + " and user prefs " + userPrefs);

        this.projectBook = new ProjectBook(projectBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.projectBook.getPersonList());
        filteredProjects = new FilteredList<>(this.projectBook.getProjectList());
    }

    public ModelManager() {
        this(new ProjectBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getProjectBookFilePath() {
        return userPrefs.getProjectBookFilePath();
    }

    @Override
    public void setProjectBookFilePath(Path projectBookFilePath) {
        requireNonNull(projectBookFilePath);
        userPrefs.setProjectBookFilePath(projectBookFilePath);
    }

    //=========== ProjectBook ================================================================================

    @Override
    public void setProjectBook(ReadOnlyProjectBook projectBook) {
        this.projectBook.resetData(projectBook);
    }

    @Override
    public ReadOnlyProjectBook getProjectBook() {
        return projectBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return projectBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        projectBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        projectBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        projectBook.setPerson(target, editedPerson);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Project} backed by the internal list of
     * {@code versionedProjectBook}
     */
    @Override
    public ObservableList<Project> getFilteredProjectList() {
        return filteredProjects;
    }

    @Override
    public void addProject(Project project) {
        requireNonNull(project);
        projectBook.addProject(project);
    }

    @Override
    public boolean hasProject(Project project) {
        requireNonNull(project);
        return projectBook.hasProject(project);
    }

    @Override
    public void setProject(Project project) {
        requireNonNull(project);
        projectBook.setProject(project);
    }

    @Override
    public java.util.Optional<Project> findProjectByName(String name) {
        return projectBook.findProjectByName(name);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedProjectBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return projectBook.equals(otherModelManager.projectBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
